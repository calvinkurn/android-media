package com.tokopedia.updateinactivephone.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.updateinactivephone.R;
import com.tokopedia.updateinactivephone.presenter.ChangeInactiveFormRequestPresenter;
import com.tokopedia.updateinactivephone.view.ChangeInactiveFormRequest;
import com.tokpedia.updateinactivephone.di.DaggerUpdateInactivePhoneComponent;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

public class ChangeInactiveFormRequestFragment extends BaseDaggerFragment implements ChangeInactiveFormRequest.View {

    private static final int REQUEST_CODE_PHOTO_ID = 1001;
    private static final int MAX_IMAGE_SIZE_IN_KB = 10 * 1024 * 1024;
    private static final int REQUEST_CODE_PAYMENT_PROOF = 2001;
    private RelativeLayout idPhotoViewRelativeLayout, accountViewRelativeLayout;
    private ImageView idPhotoView, accountPhotoView;
    private ImagePickerBuilder imagePickerBuilder;

    public static Fragment getInstance() {
        return new ChangeInactiveFormRequestFragment();
    }

    @Inject
    ChangeInactiveFormRequestPresenter presenter;

    private ImageView uploadIdPhoto;
    private ImageView uploadPaymentPhoto;
    private Button continueButton;

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerUpdateInactivePhoneComponent daggerUpdateInactivePhoneComponent = (DaggerUpdateInactivePhoneComponent)
                DaggerUpdateInactivePhoneComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerUpdateInactivePhoneComponent.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
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

        presenter.attachView(this);
        prepareView();
        return view;
    }

    private void prepareView() {
        uploadPaymentPhoto.setOnClickListener(onUploadAccountBook());
        uploadIdPhoto.setOnClickListener(onUploadPhotoId());
        accountViewRelativeLayout.setOnClickListener(onUploadAccountBook());
        idPhotoViewRelativeLayout.setOnClickListener(onUploadPhotoId());
    }

    private View.OnClickListener onUploadPhotoId() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerBuilder builder = getImagePickerBuilder();
                Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
                startActivityForResult(intent, REQUEST_CODE_PHOTO_ID);
            }
        };
    }

    private View.OnClickListener onUploadAccountBook() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerBuilder builder = getImagePickerBuilder();
                Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
                startActivityForResult(intent, REQUEST_CODE_PAYMENT_PROOF);
            }
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
                        loadImageToImageView(idPhotoView, imagePath);

                        presenter.setPhotoIdImagePath(imagePath);
                        presenter.uploadPhotoIdImage();
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
                        loadImageToImageView(accountPhotoView, imagePath);
                        presenter.setAccountPhotoImagePath(imagePath);
                    }
                }
                break;
        }
        setSubmitButton();
    }

    private void setSubmitButton() {
        if (presenter.isValidPhotoIdPath()) {
            MethodChecker.setBackground(continueButton,
                    MethodChecker.getDrawable(getActivity(),
                            R.drawable.green_button_rounded
                    ));
            continueButton.setTextColor(MethodChecker.getColor(getActivity(),
                    R.color.white));
        }
    }

    private void loadImageToImageView(ImageView imageView, String imagePath) {
        ImageHandler.loadImageFromFile(getActivity(), imageView, new File(imagePath));
    }

    @Override
    protected String getScreenName() {
        return "";
    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void onForbidden() {

    }

}
