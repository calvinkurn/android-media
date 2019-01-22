package com.tokopedia.contactus.createticket.fragment;

/**
 * Created by nisie on 8/12/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.contactus.createticket.ContactUsConstant;
import com.tokopedia.contactus.createticket.activity.ContactUsCreateTicketActivity;
import com.tokopedia.contactus.createticket.adapter.ImageUploadAdapter;
import com.tokopedia.contactus.createticket.listener.CreateTicketFormFragmentView;
import com.tokopedia.contactus.createticket.model.ImageUpload;
import com.tokopedia.contactus.createticket.model.solution.SolutionResult;
import com.tokopedia.contactus.createticket.presenter.CreateTicketFormFragmentPresenter;
import com.tokopedia.contactus.createticket.presenter.CreateTicketFormFragmentPresenterImpl;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;

/**
 * Created by Tkpd_Eka on 8/13/2015.
 */
public class CreateTicketFormFragment extends BasePresenterFragment<CreateTicketFormFragmentPresenter>
        implements CreateTicketFormFragmentView, ContactUsConstant {

    private static final int REQUEST_CODE_IMAGE = 1001;
    @BindView(R2.id.main_category)
    EditText mainCategory;
    @BindView(R2.id.detail)
    EditText detail;
    @BindView(R2.id.attachment_note)
    TextView attachmentNote;
    @BindView(R2.id.main)
    View mainView;
    @BindView(R2.id.attachment)
    RecyclerView attachment;
    @BindView(R2.id.phone_number)
    EditText phoneNumber;
    @BindView(R2.id.name)
    EditText name;
    @BindView(R2.id.email)
    EditText email;
    @BindView(R2.id.name_text)
    TextView nameTitle;
    @BindView(R2.id.email_text)
    TextView emailTitle;
    TextView detailTextView;
    TextView attachmentLabelTextView;
    ImageUploadAdapter imageAdapter;
    TkpdProgressDialog progressDialog;
    ImageUploadHandler imageUploadHandler;
    private FinishContactUsListener finishContactUsListener;

    public static CreateTicketFormFragment createInstance(Bundle extras) {
        CreateTicketFormFragment fragment = new CreateTicketFormFragment();
        Bundle bundle = new Bundle();
        bundle.putAll(extras);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        detailTextView = (TextView) view.findViewById(R.id.detail_text);
        attachmentLabelTextView = (TextView) view.findViewById(R.id.attachment_note);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FinishContactUsListener) {
            finishContactUsListener = (FinishContactUsListener) activity;
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof FinishContactUsListener) {
            finishContactUsListener = (FinishContactUsListener) activity;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setInitFieldValue();
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

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.initForm();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
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

    @Override
    protected void initialPresenter() {
        presenter = new CreateTicketFormFragmentPresenterImpl(this, finishContactUsListener);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_create_ticket;
    }

    @Override
    protected void initView(View view) {
        if (SessionHandler.isV4Login(getActivity())) {
            nameTitle.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            emailTitle.setVisibility(View.GONE);
        }
        imageAdapter = ImageUploadAdapter.createAdapter(getActivity());
        imageAdapter.setCanUpload(true);

        attachment.setLayoutManager(new LinearLayoutManager(getActivity(),
                android.support.v7.widget.LinearLayoutManager.HORIZONTAL, false));
        attachment.setAdapter(imageAdapter);
        progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);

    }

    @Override
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
        ImagePickerBuilder builder = new ImagePickerBuilder(getString(com.tokopedia.contactus.R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, null, true,
                null, null);
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void initialVar() {
        imageUploadHandler = ImageUploadHandler.createInstance(this);
    }

    @Override
    protected void setActionVar() {

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
        presenter.onDestroyView();
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
                int position = imageAdapter.getList().size();
                ImageUpload image = new ImageUpload();
                image.setPosition(position);
                image.setImageId("image" + UUID.randomUUID().toString());
                image.setFileLoc(imagePath);
                imageAdapter.addImage(image);
            }
        }
    }

    public void setFinishContactUsListener(FinishContactUsListener finishContactUsListener) {
        this.finishContactUsListener = finishContactUsListener;
    }

    public interface FinishContactUsListener {
        void onFinishCreateTicket();
    }
}