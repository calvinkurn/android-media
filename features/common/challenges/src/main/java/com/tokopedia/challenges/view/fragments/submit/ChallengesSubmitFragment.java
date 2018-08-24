package com.tokopedia.challenges.view.fragments.submit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.ChallengesAnalytics;
import com.tokopedia.challenges.ChallengesModuleRouter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.DaggerChallengesComponent;
import com.tokopedia.challenges.view.customview.ExpandableTextView;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.utils.MarkdownProcessor;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;

import java.io.File;
import java.util.ArrayList;


import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder.DEFAULT_MIN_RESOLUTION;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_CAMERA;
import static com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef.TYPE_GALLERY;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

@RuntimePermissions
public class ChallengesSubmitFragment extends BaseDaggerFragment implements IChallengesSubmitContract.View {

    private ImageView mSelectedImage;
    private ImageView mDeleteImage;
    private AppCompatEditText mEdtImageTitle;
    private AppCompatEditText mEdtImageDescription;
    private TextView mBtnSubmit;
    private TextView mBtnCancel;
    private TextView mShowMore;
    WebView longDescription;
    private ImagePickerBuilder imagePickerBuilder;
    public static final int REQUEST_IMAGE_SELECT = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    private static final int REQUEST_CODE_IMAGE_VIDEO = 2;
    private String mAttachmentPath;
    private  ProgressDialog progress;
    private View parent;
    private ScrollView scrollView;

    @Inject
    ChallengesSubmitPresenter presenter;
    private TextView mChallengeTitle;
    private ChallengeSettings challengeSettings;
    private TextView descriptionShort;
    private String channelId;
    private String channelTitle;
    private String channelDesc;
    @Inject
    public ChallengesAnalytics analytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.challengeSettings = getArguments().getParcelable("challengeSettings");
        this.channelId = getArguments().getString("channelId");
        this.channelTitle = getArguments().getString("channelTitle");
        this.channelDesc = getArguments().getString("channelDesc");

        setHasOptionsMenu(true);
    }

    public static ChallengesSubmitFragment newInstance(Bundle extras) {
        ChallengesSubmitFragment fragment = new ChallengesSubmitFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public ChallengeSettings getChallengeSettings() {
        return challengeSettings;
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
                analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_CHALLENGES,
                        ChallengesAnalytics.EVENT_CATEGORY_SUBMIT_POST,
                        ChallengesAnalytics.EVENT_ACTION_CLICK,
                        ChallengesAnalytics.EVENT_SUBMIT);
                presenter.onSubmitButtonClick();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.sendEventChallenges(ChallengesAnalytics.EVENT_CLICK_CHALLENGES,
                        ChallengesAnalytics.EVENT_CATEGORY_SUBMIT_POST,
                        ChallengesAnalytics.EVENT_ACTION_CLICK,
                        ChallengesAnalytics.EVENT_CANCEL);
                presenter.onCancelButtonClick();
            }
        });

        mSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSelectedImageClick();
            }
        });

        mShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (descriptionShort.getVisibility() == View.VISIBLE) {
                    mShowMore.setText(R.string.see_less);
                    MarkdownProcessor m = new MarkdownProcessor();
                    String html = m.markdown(channelDesc);
                    longDescription.loadDataWithBaseURL("fake://", html, "text/html", "UTF-8", null);
                    descriptionShort.setVisibility(View.GONE);
                    longDescription.setVisibility(View.VISIBLE);
                } else {
                    mShowMore.setText(R.string.see_more);
                    descriptionShort.setVisibility(View.VISIBLE);
                    longDescription.setVisibility(View.GONE);
                    scrollView.scrollTo(0, 0);
                }
            }
        });

        mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageHandler.loadImageWithId(mSelectedImage,R.drawable.ic_upload);
                mAttachmentPath = null;
                mDeleteImage.setVisibility(View.GONE);

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
        return mAttachmentPath;
    }

    @Override
    public void showProgress(String message) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
        }
        if (!progress.isShowing()) {
            progress.setMessage(message);
            progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);
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
        //Toast.makeText(getContext(),showToast,Toast.LENGTH_LONG).show();
    }


    @Override
    public void finish() {
        getActivity().finish();
    }


    public void setSnackBarErrorMessage(String message, View.OnClickListener listener) {
        ToasterNormal
                .make(parent,
                        message,
                        BaseToaster.LENGTH_LONG)
                .setAction(getResources().getString(R.string.title_ok),
                        listener)
                .show();
    }

    @Override
    public void setSnackBarErrorMessage(String message) {
        setSnackBarErrorMessage(message,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    @Override
    public void setChallengeData() {
        mChallengeTitle.setText(channelTitle);
        descriptionShort.setText(channelDesc);
    }

    @Override
    public void selectImageVideo() {
        ChallengesSubmitFragmentPermissionsDispatcher.actionVideoImagePickerWithCheck(ChallengesSubmitFragment.this);
    }

    @Override
    public void selectImage() {
        showImagePickerDialog();
    }

    @Override
    public void selectVideo() {
        ChallengesSubmitFragmentPermissionsDispatcher.actionVideoPickerWithCheck(ChallengesSubmitFragment.this);
    }

    private void initView(View view) {
        mSelectedImage = view.findViewById(R.id.selected_image);
        mDeleteImage = view.findViewById(R.id.delete_image);
        mEdtImageTitle = view.findViewById(R.id.edt_image_title);
        mEdtImageDescription = view.findViewById(R.id.edt_image_description);
        mBtnSubmit = view.findViewById(R.id.btn_submit);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        parent = view.findViewById(R.id.constraint_layout);
        mChallengeTitle = view.findViewById(R.id.challenge_title);
        descriptionShort = view.findViewById(R.id.challenge_description);
        mShowMore = view.findViewById(R.id.show_more);
        longDescription = view.findViewById(R.id.markdownView);
        scrollView = view.findViewById(R.id.scroll_view);
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
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                mAttachmentPath = imageUrlOrPathList.get(0);
                ImageHandler.loadImageFromFile(getContext(), mSelectedImage, new File(mAttachmentPath));
            }
        }else if ((requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_IMAGE_VIDEO )&& resultCode == Activity.RESULT_OK && data != null) {
            mAttachmentPath = ((ChallengesModuleRouter) ((getActivity()).getApplication())).getResultSelectionPath(data);
            ImageHandler.loadImageFromFile(getContext(), mSelectedImage, new File(mAttachmentPath));
        }
        if(mAttachmentPath != null)
            mDeleteImage.setVisibility(View.VISIBLE);

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

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionVideoPicker() {
        startActivityForResult(
                ((ChallengesModuleRouter) ((getActivity()).getApplication())).getGalleryVideoIntent(getActivity()),
                REQUEST_CODE_VIDEO
        );
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionVideoImagePicker() {
        startActivityForResult(
                ((ChallengesModuleRouter) ((getActivity()).getApplication())).getGalleryVideoImageIntent(getActivity()),
                REQUEST_CODE_IMAGE_VIDEO
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChallengesSubmitFragmentPermissionsDispatcher.onRequestPermissionsResult(ChallengesSubmitFragment.this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        ((ChallengesModuleRouter) ((getActivity()).getApplication())).onShowRationale(getActivity(), request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        ((ChallengesModuleRouter) ((getActivity()).getApplication())).onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        ((ChallengesModuleRouter) ((getActivity()).getApplication())).onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public String getChallengeId() {
        return channelId;
    }
}
