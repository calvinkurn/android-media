package com.tokopedia.challenges.view.fragments.submit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.domain.usecase.GetChallegeTermsUseCase;
import com.tokopedia.challenges.domain.usecase.GetChallengeSettingUseCase;
import com.tokopedia.challenges.domain.usecase.GetDetailsSubmissionsUseCase;
import com.tokopedia.challenges.domain.usecase.IntializeMultiPartUseCase;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.analytics.ChallengesMoengageAnalyticsTracker;
import com.tokopedia.challenges.view.fragments.submit.IChallengesSubmitContract.Presenter;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.model.upload.UploadFingerprints;
import com.tokopedia.challenges.view.service.UploadChallengeService;
import com.tokopedia.challenges.view.utils.ChallengesCacheHandler;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.imagepicker.common.util.ImageUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChallengesSubmitPresenter extends BaseDaggerPresenter<IChallengesSubmitContract.View> implements Presenter {

    GetChallengeSettingUseCase mGetChallengeSettingUseCase;
    GetChallegeTermsUseCase mGetChallegeTermsUseCase;
    IntializeMultiPartUseCase mIntializeMultiPartUseCase;
    public ChallengeSettings settings;
    public static final String ACTION_UPLOAD_COMPLETE = "action.upload.complete";
    public static final String ACTION_UPLOAD_FAIL = "action.upload.fail";
    GetDetailsSubmissionsUseCase getDetailsSubmissionsUseCase;
    private String postId;
    private String[] videoExtensions = {
            "mp4", "m4v", "mov", "ogv"
    };


    @Inject
    public ChallengesSubmitPresenter(GetChallengeSettingUseCase mGetChallengeSettingUseCase, GetChallegeTermsUseCase mGetChallegeTermsUseCase, IntializeMultiPartUseCase mIntializeMultiPartUseCase, GetDetailsSubmissionsUseCase getDetailsSubmissionsUseCase) {
        this.mGetChallengeSettingUseCase = mGetChallengeSettingUseCase;
        this.mGetChallegeTermsUseCase = mGetChallegeTermsUseCase;
        this.mIntializeMultiPartUseCase = mIntializeMultiPartUseCase;
        this.getDetailsSubmissionsUseCase = getDetailsSubmissionsUseCase;
    }

    @Override
    public void attachView(IChallengesSubmitContract.View view) {
        super.attachView(view);
        getView().setChallengeData();
        mGetChallengeSettingUseCase.setCHALLENGE_ID(getView().getChallengeId());
        mGetChallegeTermsUseCase.setCHALLENGE_ID(getView().getChallengeId());
        mIntializeMultiPartUseCase.setCHALLENGE_ID(getView().getChallengeId());
    }


    @Override
    public void onSubmitButtonClick() {
        String title = getView().getImageTitle().trim();
        String description = getView().getDescription().trim();
        String filePath = getView().getImage();
        if (filePath == null || filePath.isEmpty()) {
            String errorMsg = null;
            if (settings.isAllowVideos() && settings.isAllowPhotos()) {
                errorMsg = getView().getContext().getResources().getString(R.string.ch_error_msg_select_image_video);
            } else if (settings.isAllowPhotos()) {
                errorMsg = getView().getContext().getResources().getString(R.string.ch_error_msg_select_image);
            } else if (settings.isAllowVideos()) {
                errorMsg = getView().getContext().getResources().getString(R.string.ch_error_msg_select_video);
            }
            getView().setSnackBarErrorMessage(errorMsg);
            return;
        } else if (!isValidateTitle(title)) {
            return;
        } else if (!isValidateDescription(description)) {
            return;
        } else if (ImageUtils.isImageType(getView().getContext(), filePath)) {
            if (!isValidateImageSize(filePath)) {
                getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.ch_error_msg_wrong_size));
                return;
            }
        } else {
            if (!isValidateVidoeSize(filePath)) {
                getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.ch_error_msg_wrong_video_size));
                return;
            }
        }

        mIntializeMultiPartUseCase.generateRequestParams(title, description, filePath);
        getView().showProgress("Uploading");
        mIntializeMultiPartUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                //  getView().hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) return;
                if (e instanceof UnknownHostException) {
                    getView().setSnackBarErrorMessage(getView().getActivity().getString(R.string.ch_unknown_host_exp_error_msg));
                } else {
                    getView().setSnackBarErrorMessage("Mengunggah Gambar");
                }
                getView().hideProgress();
                e.printStackTrace();

            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                if(!isViewAttached()) return;
                RestResponse res1 = restResponse.get(UploadFingerprints.class);
                UploadFingerprints fingerprints = res1.getData();
                postId = fingerprints.getNewPostId();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ACTION_UPLOAD_COMPLETE);
                intentFilter.addAction(ACTION_UPLOAD_FAIL);
                getView().getContext().registerReceiver(receiver, intentFilter);
                if (fingerprints.getTotalParts() > fingerprints.getPartsCompleted()) {
                    getView().showMessage(getView().getActivity().getString(R.string.ch_uploading_start_msg));
                    getView().getContext().startService(UploadChallengeService.getIntent(getView().getContext(), fingerprints, getView().getChallengeId(), filePath, postId));
                } else {
                    Intent intent1 = new Intent(ChallengesSubmitPresenter.ACTION_UPLOAD_COMPLETE);
                    intent1.putExtra(Utils.QUERY_PARAM_SUBMISSION_ID, fingerprints.getNewPostId());
                    intent1.putExtra(Utils.QUERY_PARAM_FILE_PATH, filePath);
                    getView().sendBroadcast(intent1);
                }
                ChallengesCacheHandler.resetCache();
                ChallengesMoengageAnalyticsTracker.challengeSubmitStart(getView().getActivity(), getView().getChallengeTitle(),
                        getView().getChallengeId(), postId);

            }
        });
    }

    public void deinit() {
        getView().getContext().unregisterReceiver(receiver);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (getView() != null) {
                getView().hideProgress();
                if (intent.getAction() == ACTION_UPLOAD_COMPLETE) {
                    if (!TextUtils.isEmpty(postId)) {
                        getSubmissionDetail();
                        ChallengesCacheHandler.resetCache();
                        ChallengesMoengageAnalyticsTracker.challengeSubmitFinished(getView().getActivity(), getView().getChallengeTitle(),
                                getView().getChallengeId(), postId);
                    }
                    getView().saveLocalpath(intent.getStringExtra(Utils.QUERY_PARAM_SUBMISSION_ID), intent.getStringExtra(Utils.QUERY_PARAM_FILE_PATH));
                } else if (intent.getAction() == ACTION_UPLOAD_FAIL) {
                    getView().setSnackBarErrorMessage(getView().getActivity().getResources().getString(R.string.ch_uploading_failed));
                }
                deinit();
            }
        }
    };


    private boolean isValidateImageSize(@NonNull String fileLoc) {
        File file = new File(fileLoc);
        return file.length() / 1000 <= Utils.MAX_IMAGE_SIZE_IN_KB;
    }

    private boolean isValidateVidoeSize(@NonNull String fileLoc) {
        File file = new File(fileLoc);
        return file.length() / 1000 <= (Utils.MAX_VIDEO_SIZE_IN_KB);
    }

    private boolean isValidateDescription(@NonNull String description) {
        if (description.length() <= 0) {
            getView().setSnackBarErrorMessage(getView().getActivity().getResources().getString(R.string.ch_error_msg_desc_blank));
            return false;
        } else if (description.length() > 300) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.ch_error_msg_wrong_descirption_size));
            return false;
        } else
            return true;
    }

    private boolean isValidateTitle(@NonNull String title) {
        if (title.length() <= 0) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.ch_error_msg_title_blank));
            return false;
        } else if (title.length() > 50) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.ch_error_msg_wrong_title_size));
            return false;
        } else
            return true;
    }

    @Override
    public void onCancelButtonClick() {
        getView().finish();
    }

    @Override
    public void onSelectedImageClick() {
        ChallengeSettings settings = getView().getChallengeSettings();
        if (settings.isAllowVideos() && settings.isAllowPhotos()) {
            getView().selectImageVideo();
        } else if (settings.isAllowPhotos()) {
            getView().selectImage();
        } else if (settings.isAllowVideos()) {
            getView().selectVideo();
        }
    }

    public void getSubmissionDetail() {
        getView().showProgress("Please wait");
        getDetailsSubmissionsUseCase.setRequestParams(postId);
        getDetailsSubmissionsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) return;
                getView().hideProgress();
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                if (!isViewAttached()) return;
                getView().hideProgress();
                RestResponse res1 = restResponse.get(SubmissionResult.class);
                SubmissionResult submissionResult = res1.getData();
                if (submissionResult != null) {
                    Intent intent = new Intent(getView().getActivity(), SubmitDetailActivity.class);
                    intent.putExtra(Utils.QUERY_PARAM_SUBMISSION_RESULT, submissionResult);
                    intent.putExtra(Utils.QUERY_PARAM_FROM_SUBMISSION, true);
                    getView().getActivity().startActivity(intent);
                    getView().getActivity().finish();
                }
            }
        });
    }

    @Override
    public void setSubmitButtonText() {
        settings = getView().getChallengeSettings();
        if (settings.isAllowVideos() && settings.isAllowPhotos()) {
            getView().setSubmitButtonText(getView().getActivity().getString(R.string.ch_submit_photo_video));
            getView().setChooseImageText(getView().getActivity().getString(R.string.ch_choose_image_title));
        } else if (settings.isAllowPhotos()) {
            getView().setSubmitButtonText(getView().getActivity().getString(R.string.ch_submit_photo));
            getView().setChooseImageText(getView().getActivity().getString(R.string.ch_choose_image_title_photo));
        } else if (settings.isAllowVideos()) {
            getView().setSubmitButtonText(getView().getActivity().getString(R.string.ch_submit_video));
            getView().setChooseImageText(getView().getActivity().getString(R.string.ch_choose_image_title_video));
        }
    }

    public boolean isDeviceSupportVideo() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }
        return true;
    }

    public boolean checkAttachmentVideo(String videoPath) {
        if (videoPath == null) return false;
        boolean isExtensionAllow = false;
        for (String extension : videoExtensions) {
            if (videoPath.toLowerCase(Locale.US).endsWith(extension)) {
                isExtensionAllow = true;
                break;
            }
        }
        return isExtensionAllow;
    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
