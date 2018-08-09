package com.tokopedia.challenges.view.fragments.submit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.DaggerChallengesComponent;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

public class ChallengesSubmitFragment extends BaseDaggerFragment implements IChallengesSubmitContract.View {

    private ImageView mSelectedImage;
    private ImageView mDeleteImage;
    private AppCompatEditText mEdtImageTitle;
    private AppCompatEditText mEdtImageDescription;
    private TextView mBtnSubmit;
    private TextView mBtnCancel;

    private ImagePickerBuilder imagePickerBuilder;
    public static final int REQUEST_IMAGE_SELECT = 1;
    String mImagePath;

    private View parent;


    @Inject
    ChallengesSubmitPresenter presenter;

    public static ChallengesSubmitFragment newInstance() {
        ChallengesSubmitFragment fragment = new ChallengesSubmitFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submit_challenge, container, false);
        initView(view);
        presenter.attachView(this);
        setClickListener();
        return view;

    }

    private void setClickListener() {
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSubmitButtonClick();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCancelButtonClick();
            }
        });

        mSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });
    }


    @Override
    public String getDescription() {
        return mEdtImageDescription.getText().toString();
    }

    @Override
    public void setDescriptionError(String error) {
        mEdtImageDescription.setError(error);
    }

    @Override
    public String getImageTitle() {
        return mEdtImageTitle.getText().toString();
    }

    @Override
    public String getImage() {
        return mImagePath;
    }


    ProgressDialog progress;

    @Override
    public void showProgress(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
        }
        if (!progress.isShowing()) {
            progress.setMessage(message);
            progress.setIndeterminate(true);
            progress.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    public void showMessage(String showToast) {

    }


    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void setSnackBarErrorMessage(String message) {
        ToasterNormal
                .make(parent,
                        message,
                        BaseToaster.LENGTH_LONG)
                .setAction(getResources().getString(R.string.title_ok),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                .show();
    }

    private void initView(View view) {
        mSelectedImage = view.findViewById(R.id.selected_image);
        mDeleteImage = view.findViewById(R.id.delete_image);
        mEdtImageTitle = view.findViewById(R.id.edt_image_title);
        mEdtImageDescription = view.findViewById(R.id.edt_image_description);
        mBtnSubmit = view.findViewById(R.id.btn_submit);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        parent = view.findViewById(R.id.constraint_layout);
    }

    private void showImagePickerDialog() {
        ImagePickerBuilder builder = getImagePickerBuilder();
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    private ImagePickerBuilder getImagePickerBuilder() {
        if (imagePickerBuilder == null) {
            imagePickerBuilder = new ImagePickerBuilder(getString(R.string.choose_image),
                    new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    DEFAULT_MIN_RESOLUTION, null, true,
                    null, null);
        }
        return imagePickerBuilder;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK && data!= null) {
            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (imageUrlOrPathList!= null && imageUrlOrPathList.size() > 0) {
                mImagePath = imageUrlOrPathList.get(0);
                ImageHandler.loadImageFromFile(getContext(), mSelectedImage, new File(mImagePath));
            }
        }

    }


    @Override
    protected void initInjector() {

        NetworkClient.init(getActivity());
        ChallengesComponent component = DaggerChallengesComponent.builder()
                .baseAppComponent(((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();
        component.inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
