package com.tokopedia.flight.cancellation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.design.component.EditTextCompat;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationChooseReasonActivity;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachementAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentAdapter;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentTypeFactory;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationReasonAndProofContract;
import com.tokopedia.flight.cancellation.view.fragment.customview.FlightCancellationViewImageDialogFragment;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationReasonAndProofPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FlightCancellationReasonAndProofFragment extends BaseDaggerFragment
        implements FlightCancellationReasonAndProofContract.View, FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener {
    private static final String EXTRA_CANCELLATION_VIEW_MODEL = "EXTRA_CANCELLATION_VIEW_MODEL";
    private static final String EXTRA_ATTACHMENT_VIEW_MODEL = "EXTRA_ATTACHMENT_VIEW_MODEL";
    private static final String EXTRA_IMAGE_LOCAL = "EXTRA_IMAGE_LOCAL";
    private static final String EXTRA_CANCELLATION_REASON = "EXTRA_CANCELLATION_REASON";
    private static final String EXTRA_CHANGED_IMAGE_INDEX = "EXTRA_CHANGED_IMAGE_INDEX";
    private static final String DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG";
    private static final int REQUEST_CODE_IMAGE = 1001;
    private static final int CHOOSE_REASON_REQUEST_CODE = 1111;

    private LinearLayout container;
    private EditTextCompat tvChooseReason;
    private LinearLayout attachmentContainer;
    private AppCompatTextView attachmentDescription;
    private ProgressBar progressBar;
    private RecyclerView rvAttachments;
    private AppCompatButton btnNext;

    private List<FlightCancellationAttachmentViewModel> attachments;
    private FlightCancellationAttachmentAdapter adapter;
    private FlightCancellationWrapperViewModel flightCancellationViewModel;
    private OnFragmentInteractionListener interactionListener;
    private String fileFromCameraLocTemp;
    private int positionChangedImage = -1;

    private FlightCancellationReasonViewModel selectedReason;

    @Inject
    FlightCancellationReasonAndProofPresenter presenter;

    public FlightCancellationReasonAndProofFragment() {

    }

    public static FlightCancellationReasonAndProofFragment newInstance(FlightCancellationWrapperViewModel flightCancellationViewModel) {
        FlightCancellationReasonAndProofFragment fragment = new FlightCancellationReasonAndProofFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CANCELLATION_VIEW_MODEL, flightCancellationViewModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = (savedInstanceState != null) ? savedInstanceState : getArguments();
        flightCancellationViewModel = arguments.getParcelable(EXTRA_CANCELLATION_VIEW_MODEL);
        fileFromCameraLocTemp = arguments.getString(EXTRA_IMAGE_LOCAL);
        if (arguments.getParcelableArrayList(EXTRA_ATTACHMENT_VIEW_MODEL) != null) {
            attachments = arguments.getParcelableArrayList(EXTRA_ATTACHMENT_VIEW_MODEL);
        }
        if (arguments.containsKey(EXTRA_CANCELLATION_REASON) && arguments.getParcelable(EXTRA_CANCELLATION_REASON) != null) {
            selectedReason = arguments.getParcelable(EXTRA_CANCELLATION_REASON);
        }
        positionChangedImage = arguments.getInt(EXTRA_CHANGED_IMAGE_INDEX, -1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_CANCELLATION_VIEW_MODEL, flightCancellationViewModel);
        outState.putString(EXTRA_IMAGE_LOCAL, fileFromCameraLocTemp);
        outState.putParcelableArrayList(EXTRA_ATTACHMENT_VIEW_MODEL, (ArrayList<? extends Parcelable>) attachments);
        outState.putParcelable(EXTRA_CANCELLATION_REASON, selectedReason);
        outState.putInt(EXTRA_CHANGED_IMAGE_INDEX, positionChangedImage);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        if (attachments == null) {
            attachments = presenter.buildAttachmentList();
        }
        presenter.initialize(attachments);
        presenter.setNextButton();

        buildAttachmentReasonView();
    }

    @Override
    protected void initInjector() {
        getComponent(FlightCancellationComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_cancellation_refundable_step_two, container, false);
        buildView(view);
        return view;
    }

    private void buildView(View view) {
        container = view.findViewById(R.id.container);
        progressBar = view.findViewById(R.id.progress_bar);
        tvChooseReason = view.findViewById(R.id.et_saved_passenger);
        attachmentContainer = view.findViewById(R.id.attachment_container);
        attachmentDescription = view.findViewById(R.id.attachment_description);
        rvAttachments = view.findViewById(R.id.rv_attachments);
        btnNext = view.findViewById(R.id.btn_next);

        FlightCancellationAttachmentTypeFactory adapterTypeFactory = new FlightCancellationAttachementAdapterTypeFactory(this, true);
        adapter = new FlightCancellationAttachmentAdapter(adapterTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAttachments.setLayoutManager(layoutManager);
        rvAttachments.setHasFixedSize(true);
        rvAttachments.setNestedScrollingEnabled(false);
        rvAttachments.setAdapter(adapter);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onNextButtonClicked();
            }
        });

        tvChooseReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(FlightCancellationChooseReasonActivity.createIntent(getContext(), selectedReason),
                        CHOOSE_REASON_REQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.travel_slide_up_in, R.anim.travel_anim_stay);
            }
        });

        hideProgressBar();
    }

    @Override
    protected String getScreenName() {
        return FlightAnalytics.Screen.FLIGHT_CANCELLATION_STEP_TWO;
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView();
        interactionListener = null;
    }

    @Override
    public void addAttachments(List<FlightCancellationAttachmentViewModel> attachments) {
        adapter.addElement(attachments);
    }

    @Override
    public void deleteAllAttachments() {
        adapter.clearAllElements();
    }

    @Override
    public void renderAttachment() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void disableNextButton() {
        btnNext.setEnabled(false);
    }

    @Override
    public void enableNextButton() {
        btnNext.setEnabled(true);
    }

    @Override
    public List<FlightCancellationAttachmentViewModel> getAttachments() {
        return attachments;
    }

    @Override
    public void setAttachment(FlightCancellationAttachmentViewModel attachment, int position) {
        attachments.set(position, attachment);
        adapter.setElement(position, attachment);
    }

    @Override
    public void showRequiredMinimalOneAttachmentErrorMessage(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getView(), getString(resId));
    }

    @Override
    public FlightCancellationWrapperViewModel getCancellationViewModel() {
        return flightCancellationViewModel;
    }

    @Override
    public void showAttachmentGreaterThanPassengersTotalAndRequiredAttachmentErrorMessage(String errorMessage) {
        NetworkErrorHelper.showRedCloseSnackbar(getView(), errorMessage);
    }

    @Override
    public FlightCancellationReasonViewModel getReason() {
        return selectedReason;
    }

    @Override
    public void showFailedToNextStepErrorMessage(String errorMessage) {
        NetworkErrorHelper.showRedCloseSnackbar(getView(), errorMessage);
    }

    @Override
    public void navigateToNextStep(FlightCancellationWrapperViewModel viewModel) {
        interactionListener.goToReview(viewModel);
    }

    @Override
    public void hideAttachmentContainer() {
        attachmentContainer.setVisibility(View.GONE);
    }

    @Override
    public void showAttachmentContainer() {
        attachmentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressBar() {
        container.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        container.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showAttachmentMinDimensionErrorMessage(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getView(), getString(resId));
    }

    @Override
    public void showAttachmentMaxSizeErrorMessage(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getView(), getString(resId));
    }

    @Override
    public void onUploadAttachmentButtonClicked(int positionIndex) {
        positionChangedImage = positionIndex;
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, null, true,
                null, null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    public void deleteAttachement(FlightCancellationAttachmentViewModel element) {
        adapter.removeAttachment(element);
    }

    @Override
    public void viewImage(String filePath) {
        showImageInFragment(filePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imagePathList = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS);
            if (imagePathList == null || imagePathList.size() <= 0) {
                return;
            }
            String imagePath = imagePathList.get(0);
            if (!TextUtils.isEmpty(imagePath)) {
                presenter.onSuccessGetImage(imagePath, positionChangedImage);
            }
            presenter.setNextButton();
        } else if (requestCode == CHOOSE_REASON_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedReason = data.getParcelableExtra(FlightCancellationChooseReasonFragment.EXTRA_SELECTED_REASON);
            renderSelectedReason();
            presenter.setNextButton();
        }
    }

    public void onEstimateRefundActivityResultCancelled() {
        presenter.attachView(this);
        presenter.onComeFromEstimateRefundScreen();
    }

    public interface OnFragmentInteractionListener {

        void goToReview(FlightCancellationWrapperViewModel viewModel);
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        presenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        tvChooseReason.clearFocus();
        KeyboardHandler.hideSoftKeyboard(getActivity());
    }

    private void renderSelectedReason() {
        tvChooseReason.setText(selectedReason.getDetail());
        buildAttachmentReasonView();

        deleteAllAttachments();
        addAttachments(attachments);
        renderAttachment();
    }

    private void buildAttachmentReasonView() {
        if (selectedReason != null && selectedReason.getRequiredDocs() != null &&
                selectedReason.getRequiredDocs().size() > 0) {
            showAttachmentContainer();
        } else {
            hideAttachmentContainer();
        }
    }

    private void showImageInFragment(String filePath) {
        FlightCancellationViewImageDialogFragment dialogFragment = FlightCancellationViewImageDialogFragment.newInstance(filePath);
        dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
    }
}
