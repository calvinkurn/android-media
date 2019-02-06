package com.tokopedia.challenges.view.fragments.submit;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.di.ChallengesComponent;
import com.tokopedia.challenges.di.DaggerChallengesComponent;
import com.tokopedia.challenges.view.analytics.ChallengesGaAnalyticsTracker;
import com.tokopedia.challenges.view.analytics.ChallengesMoengageAnalyticsTracker;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.challenges.view.utils.MarkdownProcessor;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.util.NetworkClient;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.VideoPickerBuilder;
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity;
import com.tokopedia.imagepicker.picker.main.view.VideoPickerActivity;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_ROTATE;
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
    private TextView mShowMore;
    WebView longDescription;
    private ImagePickerBuilder imagePickerBuilder;
    public static final int REQUEST_IMAGE_SELECT = 1;
    private static final int REQUEST_CODE_VIDEO = 2;
    private static final int REQUEST_CODE_IMAGE_VIDEO = 2;

    private String mAttachmentPath;
    private ProgressDialog progress;
    private View parent;
    private ScrollView scrollView;
    private TextView txtChooseImageTitle;

    @Inject
    ChallengesSubmitPresenter presenter;
    private TextView mChallengeTitle;
    private ChallengeSettings challengeSettings;
    private TextView descriptionShort;
    private String channelId;
    private String channelTitle;
    private String channelDesc;
    @Inject
    public ChallengesGaAnalyticsTracker analytics;
    private final static String SCREEN_NAME = "challenges/submit_challenge";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.challengeSettings = getArguments().getParcelable(Utils.QUERY_PARAM_CHALLENGE_SETTINGS);
        this.channelId = getArguments().getString(Utils.QUERY_PARAM_CHANNEL_ID);
        this.channelTitle = getArguments().getString(Utils.QUERY_PARAM_CHANNEL_TITLE);
        this.channelDesc = getArguments().getString(Utils.QUERY_PARAM_CHANNEL_DESC);

        setHasOptionsMenu(true);
        ChallengesMoengageAnalyticsTracker.challengeScreenLaunched(getActivity(), "Challenge Submissions");
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
        presenter.setSubmitButtonText();
        return view;

    }

    private void setClickListener() {
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_SUBMIT_POST,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                        ChallengesGaAnalyticsTracker.EVENT_SUBMIT);
                presenter.onSubmitButtonClick();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analytics.sendEventChallenges(ChallengesGaAnalyticsTracker.EVENT_CLICK_CHALLENGES,
                        ChallengesGaAnalyticsTracker.EVENT_CATEGORY_SUBMIT_POST,
                        ChallengesGaAnalyticsTracker.EVENT_ACTION_CLICK,
                        ChallengesGaAnalyticsTracker.EVENT_CANCEL);
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
                    mShowMore.setText(R.string.ch_see_less);
                    MarkdownProcessor m = new MarkdownProcessor();
                    String html = m.markdown(channelDesc);
                    longDescription.loadDataWithBaseURL("fake://", html, "text/html", "UTF-8", null);
                    descriptionShort.setVisibility(View.GONE);
                    longDescription.setVisibility(View.VISIBLE);
                } else {
                    mShowMore.setText(R.string.ch_see_more);
                    descriptionShort.setVisibility(View.VISIBLE);
                    longDescription.setVisibility(View.GONE);
                    scrollView.scrollTo(0, 0);
                }
            }
        });

        mDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageHandler.loadImageWithId(mSelectedImage, R.drawable.ic_upload);
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
            progress.setCancelable(true);
            progress.show();

            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    getActivity().finish();
                }
            });
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
        setSnackBarErrorMessage(message, new View.OnClickListener() {
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
        showVideoImageChooseDialog();
    }

    @Override
    public void selectImage() {
        showImagePickerDialog();
    }

    @Override
    public void selectVideo() {
        if (presenter.isDeviceSupportVideo()) {
            VideoPickerBuilder builder = new VideoPickerBuilder(getString(R.string.ch_choose_video), Utils.MAX_VIDEO_SIZE_IN_KB,
                    0, null);
            Intent intent = VideoPickerActivity.getIntent(getActivity(), builder);
            startActivityForResult(intent, REQUEST_CODE_VIDEO);
        }

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
        txtChooseImageTitle = view.findViewById(R.id.txt_choose_image_title);
    }

    private void showImagePickerDialog() {
        ImagePickerBuilder builder = getImagePickerBuilder();
        Intent intent = ImagePickerActivity.getIntent(getActivity(), builder);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    private ImagePickerBuilder getImagePickerBuilder() {
        if (imagePickerBuilder == null) {
            imagePickerBuilder = new ImagePickerBuilder(getString(R.string.choose_image),
                    new int[]{TYPE_GALLERY, TYPE_CAMERA}, GalleryType.IMAGE_ONLY, Utils.MAX_IMAGE_SIZE_IN_KB,
                    DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.RATIO_1_1, true,
                    new ImagePickerEditorBuilder(
                            new int[]{ACTION_BRIGHTNESS, ACTION_CONTRAST, ACTION_CROP, ACTION_ROTATE},
                            false,
                            null)
                    , null);
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
        } else if ((requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_IMAGE_VIDEO) && resultCode == Activity.RESULT_OK && data != null) {

            ArrayList<String> videoPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (videoPathList != null && videoPathList.size() > 0) {
                String videoPath = videoPathList.get(0);
                if (presenter.checkAttachmentVideo(videoPath)) {
                    mAttachmentPath = videoPath;
                    ImageHandler.loadImageFromFile(getContext(), mSelectedImage, new File(mAttachmentPath));
                } else if (getActivity() != null) {
                    showMessage(getActivity().getString(R.string.ch_error_Video_version_minimum));
                }
            }
        }
        if (mAttachmentPath != null)
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

    @Override
    public String getChallengeId() {
        return channelId;
    }

    @Override
    public void setSubmitButtonText(String text) {
        mBtnSubmit.setText(text);
        txtChooseImageTitle.setText(text);
    }

    @Override
    public void setChooseImageText(String text) {
        txtChooseImageTitle.setText(text);
    }

    private void showVideoImageChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getContext().getResources().getString(R.string.ch_dialog_upload_options));
        builder.setPositiveButton(getContext().getResources().getString(R.string.ch_title_video), (dialogInterface, i) -> selectVideo()).setNegativeButton(getContext().getResources().getString(R.string.ch_title_image), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showImagePickerDialog();
            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void saveLocalpath(String newPostId, String filePath) {
        ChallengesCacheHandler.saveLocalVideoPath(getContext(), newPostId, filePath);
    }

    @Override
    public void sendBroadcast(Intent intent1) {
        getContext().sendBroadcast(intent1);
    }

    @Override
    public String getChallengeTitle() {
        return channelTitle;
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        analytics.sendScreenEvent(getActivity(), SCREEN_NAME);
        super.onResume();
    }
}
