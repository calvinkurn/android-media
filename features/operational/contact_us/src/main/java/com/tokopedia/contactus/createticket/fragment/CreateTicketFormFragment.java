package com.tokopedia.contactus.createticket.fragment;

/**
 * Created by nisie on 8/12/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.contactus.createticket.activity.ContactUsCreateTicketActivity;
import com.tokopedia.contactus.createticket.utilities.CreateTicketProgressDialog;
import com.tokopedia.contactus.createticket.widget.LinearLayoutManager;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.createticket.adapter.ImageUploadAdapter;
import com.tokopedia.contactus.createticket.listener.CreateTicketFormFragmentView;
import com.tokopedia.contactus.createticket.model.ImageUpload;
import com.tokopedia.contactus.createticket.model.solution.SolutionResult;
import com.tokopedia.contactus.createticket.presenter.CreateTicketFormFragmentPresenter;
import com.tokopedia.contactus.createticket.presenter.CreateTicketFormFragmentPresenterImpl;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.imagepicker.common.ImagePickerBuilder;
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor;
import com.tokopedia.imagepicker.common.ImagePickerRouterKt;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Tkpd_Eka on 8/13/2015.
 */
public class CreateTicketFormFragment extends TkpdBaseV4Fragment
        implements CreateTicketFormFragmentView, ContactUsConstant {

    private static final int REQUEST_CODE_IMAGE = 1001;
    private static final String SAVED_STATE_OF_CREATE_FORM_FRAGMENT = "saved state of CreateTicketFormFragment";
    private EditText mainCategory;
    private EditText detail;
    private TextView attachmentNote;
    private View mainView;
    private RecyclerView attachment;
    private EditText phoneNumber;
    private EditText name;
    private EditText email;
    private TextView nameTitle;
    private TextView emailTitle;
    private TextView detailTextView;
    private TextView attachmentLabelTextView;
    ImageUploadAdapter imageAdapter;
    CreateTicketProgressDialog progressDialog;
    private FinishContactUsListener finishContactUsListener;
    private Bundle savedState;

    private CreateTicketFormFragmentPresenter presenter;

    public static CreateTicketFormFragment createInstance(Bundle extras) {
        CreateTicketFormFragment fragment = new CreateTicketFormFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        findingViewsId(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setViewListener();
        setInitFieldValue();
    }

    private void findingViewsId(View view) {
        mainCategory = view.findViewById(R.id.main_category);
        detail = view.findViewById(R.id.detail);
        attachmentNote = view.findViewById(R.id.attachment_note);
        mainView = view.findViewById(R.id.main);
        attachment = view.findViewById(R.id.attachment);
        phoneNumber = view.findViewById(R.id.phone_number);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        nameTitle = view.findViewById(R.id.name_text);
        emailTitle = view.findViewById(R.id.email_text);
        detailTextView = view.findViewById(R.id.detail_text);
        attachmentLabelTextView = view.findViewById(R.id.attachment_note);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(getOptionsMenuEnable());
        if (activity instanceof FinishContactUsListener) {
            finishContactUsListener = (FinishContactUsListener) activity;
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        setHasOptionsMenu(getOptionsMenuEnable());
        if (activity instanceof FinishContactUsListener) {
            finishContactUsListener = (FinishContactUsListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        initialPresenter();
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateToArguments();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        restoreStateFromArguments();
    }

    private void setInitFieldValue() {
        if (getArguments() != null) {
            if (!TextUtils.isEmpty(getArguments().getString(ContactUsCreateTicketActivity.PARAM_DESCRIPTION_TITLE, null))) {
                detailTextView.setText(getArguments().getString(ContactUsCreateTicketActivity.PARAM_DESCRIPTION_TITLE));
            }
            if (!TextUtils.isEmpty(getArguments().getString(ContactUsCreateTicketActivity.PARAM_ATTACHMENT_TITLE, null))) {
                attachmentLabelTextView.setText(getArguments().getString(ContactUsCreateTicketActivity.PARAM_ATTACHMENT_TITLE));
            }
            if (!TextUtils.isEmpty(getArguments().getString(ContactUsCreateTicketActivity.PARAM_DESCRIPTION, null))) {
                detail.setText(getArguments().getString(ContactUsCreateTicketActivity.PARAM_DESCRIPTION));
            }
        }
    }

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = new Bundle();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b == null) b = new Bundle();
            b.putBundle(SAVED_STATE_OF_CREATE_FORM_FRAGMENT, savedState);
        }
    }

    private void restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b == null) b = new Bundle();
        savedState = b.getBundle(SAVED_STATE_OF_CREATE_FORM_FRAGMENT);
        if (savedState == null) {
            onFirstTimeLaunched();
        }
    }

    private void onFirstTimeLaunched() {
        presenter.initForm();
    }

    private boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.talk_add_new, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send) {
            KeyboardHandler.DropKeyboard(getActivity(), getView());
            presenter.sendTicket();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initialPresenter() {
        presenter = new CreateTicketFormFragmentPresenterImpl(this, finishContactUsListener);
    }

    private int getFragmentLayout() {
        return R.layout.fragment_create_ticket;
    }

    protected void initView(View view) {
        UserSessionInterface userSession = new UserSession(getContext());
        if (userSession.isLoggedIn()) {
            nameTitle.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            emailTitle.setVisibility(View.GONE);
        }
        imageAdapter = ImageUploadAdapter.createAdapter(getActivity());
        imageAdapter.setCanUpload(true);

        attachment.setLayoutManager(new LinearLayoutManager(getActivity(),
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
        attachment.setAdapter(imageAdapter);
        progressDialog = new CreateTicketProgressDialog(getActivity(), CreateTicketProgressDialog.NORMAL_PROGRESS);

    }

    protected void setViewListener() {
        imageAdapter.setListener(new ImageUploadAdapter.ProductImageListener() {
            @Override
            public View.OnClickListener onUploadClicked(final int position) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showImagePickerDialog();

                    }
                };
            }

            @Override
            public View.OnClickListener onImageClicked(final int position, ImageUpload imageUpload) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                };
            }

        });
    }

    private void showImagePickerDialog() {
        ImagePickerBuilder builder = ImagePickerBuilder.getOriginalImageBuilder(requireContext());
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalGlobal.IMAGE_PICKER);
        ImagePickerRouterKt.putImagePickerBuilder(intent, builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    public String getTicketCategoryId() {
        return String.valueOf(getArguments().getInt(PARAM_LAST_CATEGORY_ID));
    }

    @Override
    public void showLoading() {
        progressDialog.showDialog();
    }

    @Override
    public void finishLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();

    }

    @Override
    public void setResult(SolutionResult solutionResult) {
        setHasOptionsMenu(true);
        mainView.setVisibility(View.VISIBLE);
        mainCategory.setText(solutionResult.getSolutions().getName());
        if (solutionResult.getSolutions().hasAttachment()) {
            attachmentNote.setText(solutionResult.getSolutions().getNote());
            attachmentNote.setVisibility(View.VISIBLE);
        } else {
            attachmentNote.setVisibility(View.GONE);
        }
        finishLoading();
    }

    @Override
    public EditText getMessage() {
        return detail;
    }

    @Override
    public void showError(String error) {
        if (error.equals(""))
            NetworkErrorHelper.showSnackbar(getActivity());
        else
            NetworkErrorHelper.showSnackbar(getActivity(), error);


    }

    @Override
    public ArrayList<ImageUpload> getAttachment() {
        return imageAdapter.getList();
    }

    @Override
    public void removeErrorEmptyState() {
        NetworkErrorHelper.hideEmptyState(getView());
    }

    @Override
    public void showErrorEmptyState(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        setHasOptionsMenu(false);
        if (error.equals(""))
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), retryClickedListener);
        else
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, retryClickedListener);

    }

    @Override
    public void showErrorValidation(EditText view, String error) {
        view.setError(error);
        view.requestFocus();
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber.getText().toString();
    }

    @Override
    public TextView getAttachmentNote() {
        return attachmentNote;
    }

    @Override
    public EditText getName() {
        return name;
    }

    @Override
    public EditText getEmail() {
        return email;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
        presenter.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            List<String> imagePathList = ImagePickerResultExtractor.extract(data).getImageUrlOrPathList();
            if (imagePathList.size() <= 0) {
                return;
            }
            String imagePath = imagePathList.get(0);
            if (!TextUtils.isEmpty(imagePath)) {
                int position = imageAdapter.getList().size();
                ImageUpload image = new ImageUpload();
                image.setPosition(position);
                image.setImageId("image" + UUID.randomUUID().toString());
                image.setFileLoc(imagePath);
                imageAdapter.addImage(image);
            }
        }
    }

    public interface FinishContactUsListener {
        void onFinishCreateTicket();
    }
}