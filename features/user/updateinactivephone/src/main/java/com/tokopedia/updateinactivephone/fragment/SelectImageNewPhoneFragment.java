package com.tokopedia.updateinactivephone.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventConstants;
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventTracking;

import java.io.File;
import java.util.ArrayList;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

public class SelectImageNewPhoneFragment extends TkpdBaseV4Fragment {

    private static final int REQUEST_CODE_PHOTO_ID = 1001;
    private static final int MAX_IMAGE_SIZE_IN_KB = 10 * 1024 * 1024;
    private static final int REQUEST_CODE_PAYMENT_PROOF = 2001;
    private static final String PAYMENT_ID_IMAGE_PATH = "paymentIdImagePath";
    private static final String PHOTO_ID_IMAGE_PATH = "photoIdImagePath";
    private RelativeLayout idPhotoViewRelativeLayout, accountViewRelativeLayout;
    private ImageView idPhotoView, accountPhotoView;
    private ImagePickerBuilder imagePickerBuilder;

    public static Fragment getInstance() {
        return new SelectImageNewPhoneFragment();
    }

    private ImageView uploadIdPhoto;
    private ImageView uploadPaymentPhoto;
    private TextView continueButton;
    private SelectImageInterface selectImageInterface;

    private String photoIdPath;
    private String accountIdPath;

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(MainApplication.getAppContext(),getScreenName());
        UpdateInactivePhoneEventTracking.eventViewPhotoUploadScreen(getActivity());
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        try {
            selectImageInterface = (SelectImageInterface) context;
        } catch (Exception e) {

        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            String paymentIdImagePath = savedInstanceState.getString(PAYMENT_ID_IMAGE_PATH);
            String photoIdImagePath = savedInstanceState.getString(PHOTO_ID_IMAGE_PATH);

            if (!TextUtils.isEmpty(photoIdImagePath)) {
                selectImageInterface.setPhotoIdImagePath(photoIdImagePath);
                loadImageToImageView(idPhotoView, photoIdImagePath);
            }

            if (!TextUtils.isEmpty(paymentIdImagePath)) {
                selectImageInterface.setAccountPhotoImagePath(paymentIdImagePath);
                loadImageToImageView(accountPhotoView, paymentIdImagePath);
            }

        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_inactive_form_request, parent, false);

        uploadIdPhoto = view.findViewById(R.id.upload_id_photo_button);
        uploadPaymentPhoto = view.findViewById(R.id.upload_account_book_button);
        continueButton = view.findViewById(R.id.button_submit);
        idPhotoViewRelativeLayout = view.findViewById(R.id.upload_id_photo_view);
        idPhotoView = view.findViewById(R.id.photo_id);

        accountViewRelativeLayout = view.findViewById(R.id.upload_account_book_view);
        accountPhotoView = view.findViewById(R.id.account_book);

        prepareView();
        return view;
    }

    private void prepareView() {
        uploadPaymentPhoto.setOnClickListener(onUploadAccountBook());
        uploadIdPhoto.setOnClickListener(onUploadPhotoId());
        accountViewRelativeLayout.setOnClickListener(onUploadAccountBook());
        idPhotoViewRelativeLayout.setOnClickListener(onUploadPhotoId());

        continueButton.setOnClickListener(view -> {

            UpdateInactivePhoneEventTracking.eventClickPhotoProceed(view.getContext());
            selectImageInterface.onContinueButtonClick();
        });
    }

    private View.OnClickListener onUploadPhotoId() {
        return v -> {
            ImagePickerBuilder builder = getImagePickerBuilder();
            Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
            startActivityForResult(intent, REQUEST_CODE_PHOTO_ID);
        };
    }

    private View.OnClickListener onUploadAccountBook() {
        return v -> {
            ImagePickerBuilder builder = getImagePickerBuilder();
            Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
            startActivityForResult(intent, REQUEST_CODE_PAYMENT_PROOF);
        };
    }

    private ImagePickerBuilder getImagePickerBuilder() {
        if (imagePickerBuilder == null) {

            imagePickerBuilder = new ImagePickerBuilder(getString(R.string.choose_image),
                    new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, MAX_IMAGE_SIZE_IN_KB,
                    DEFAULT_MIN_RESOLUTION, null, true,
                    null, null);
        }
        return imagePickerBuilder;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_PHOTO_ID:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                        String imagePath = imageUrlOrPathList.get(0);
                        uploadIdPhoto.setVisibility(View.GONE);
                        idPhotoViewRelativeLayout.setVisibility(View.VISIBLE);
                        photoIdPath = imagePath;
                        loadImageToImageView(idPhotoView, imagePath);
                        selectImageInterface.setPhotoIdImagePath(imagePath);

                        Bundle bundle = new Bundle();
                        bundle.putString(PAYMENT_ID_IMAGE_PATH, accountIdPath);
                        bundle.putString(PHOTO_ID_IMAGE_PATH, photoIdPath);

                        onSaveInstanceState(bundle);
                    }
                }
                break;
            case REQUEST_CODE_PAYMENT_PROOF:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                        String imagePath = imageUrlOrPathList.get(0);
                        uploadPaymentPhoto.setVisibility(View.GONE);
                        accountViewRelativeLayout.setVisibility(View.VISIBLE);
                        accountIdPath = imagePath;
                        loadImageToImageView(accountPhotoView, imagePath);
                        selectImageInterface.setAccountPhotoImagePath(imagePath);

                        Bundle bundle = new Bundle();
                        bundle.putString(PAYMENT_ID_IMAGE_PATH, accountIdPath);
                        bundle.putString(PHOTO_ID_IMAGE_PATH, photoIdPath);

                        onSaveInstanceState(bundle);
                    }
                }
                break;
        }
        setSubmitButton();
    }

    private void setSubmitButton() {
        if (selectImageInterface.isValidPhotoIdPath()) {
            MethodChecker.setBackground(continueButton,
                    MethodChecker.getDrawable(getActivity(),
                            R.drawable.green_button_rounded
                    ));
            continueButton.setTextColor(MethodChecker.getColor(getActivity(),
                    R.color.white));
            continueButton.setClickable(true);
            continueButton.setEnabled(true);
        } else {
            continueButton.setClickable(false);
            continueButton.setEnabled(false);
            continueButton.setTextColor(getResources().getColor(R.color.black_26));
        }
    }

    private void loadImageToImageView(ImageView imageView, String imagePath) {
        ImageHandler.loadImageFromFile(getActivity(), imageView, new File(imagePath));
    }

    @Override
    protected String getScreenName() {
        return UpdateInactivePhoneEventConstants.Screen.SELECT_IMAGE_TO_UPLOAD;
    }

    public interface SelectImageInterface {
        void onContinueButtonClick();

        boolean isValidPhotoIdPath();

        void setAccountPhotoImagePath(String imagePath);

        void setPhotoIdImagePath(String imagePath);

    }
}
