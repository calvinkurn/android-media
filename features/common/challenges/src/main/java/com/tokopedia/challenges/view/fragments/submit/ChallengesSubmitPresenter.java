package com.tokopedia.challenges.view.fragments.submit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.data.source.ChallengesUrl;
import com.tokopedia.challenges.domain.usecase.GetChallegeTermsUseCase;
import com.tokopedia.challenges.domain.usecase.GetChallengeSettingUseCase;
import com.tokopedia.challenges.domain.usecase.GetDetailsSubmissionsUseCase;
import com.tokopedia.challenges.domain.usecase.GetMySubmissionsListUseCase;
import com.tokopedia.challenges.domain.usecase.IntializeMultiPartUseCase;
import com.tokopedia.challenges.view.fragments.submit.IChallengesSubmitContract.Presenter;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.model.upload.UploadFingerprints;
import com.tokopedia.challenges.view.service.UploadChallengeService;
import com.tokopedia.challenges.view.share.ShareBottomSheet;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.usecase.RequestParams;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChallengesSubmitPresenter extends BaseDaggerPresenter<IChallengesSubmitContract.View> implements Presenter {

    GetChallengeSettingUseCase mGetChallengeSettingUseCase;
    GetChallegeTermsUseCase mGetChallegeTermsUseCase;
    IntializeMultiPartUseCase mIntializeMultiPartUseCase;
    public ChallengeSettings settings;
    public static final String ACTION_UPLOAD_COMPLETE = "action.upload.complete";
    GetDetailsSubmissionsUseCase getDetailsSubmissionsUseCase;
    String postId;

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
        getView().setChallengeTitle(getView().getChallengeResult().getTitle());
        getView().setChallengeDescription(getView().getChallengeResult().getDescription());
        mGetChallengeSettingUseCase.setCHALLENGE_ID(getView().getChallengeResult().getId());
        mGetChallegeTermsUseCase.setCHALLENGE_ID(getView().getChallengeResult().getId());
        mIntializeMultiPartUseCase.setCHALLENGE_ID(getView().getChallengeResult().getId());
        getView().showProgress("Configuring");
        mGetChallengeSettingUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {

            @Override
            public void onCompleted() {
                getView().hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgress();
                getView().showMessage("Configuration Fail");
                getView().finish();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                RestResponse res1 = restResponse.get(ChallengeSettings.class);
                settings = res1.getData();
                if (!settings.isUploadAllowed()) {
                    getView().showMessage("Upload Not allowed for this Challenge"); // update challenge as per UX
                    getView().finish();
                }
            }
        });
    }


    @Override
    public void onSubmitButtonClick() {
        String title = getView().getImageTitle();
        String description = getView().getDescription();
        String filePath = getView().getImage();
        if (filePath == null || filePath.isEmpty()) {
            getView().setSnackBarErrorMessage("Please select image");
            return;
        } else if (!isValidateTitle(title)) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_wrong_title_size));  // TODO update messages
            return;
        } else if (!isValidateDescription(description)) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_wrong_descirption_size));
            return;
        } else if (ImageUtils.isImageType(getView().getContext(), filePath)) {
            if (!isValidateImageSize(filePath)) {
                getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_wrong_size));
                return;
            }
        } else {
            if (!isValidateVidoeSize(filePath)) {
                getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_wrong_video_size));
                return;
            }
        }
        mIntializeMultiPartUseCase.generateRequestParams(title, description, filePath);
        getView().showProgress("Uploading");
        mIntializeMultiPartUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                getView().hideProgress();
                getView().showMessage("Upload Initiated Please Wait");
            }

            @Override
            public void onError(Throwable e) {
                getView().hideProgress();
                e.printStackTrace();
                getView().showMessage("Image Uploaded Failed");
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                RestResponse res1 = restResponse.get(UploadFingerprints.class);
                UploadFingerprints fingerprints = res1.getData();
                getView().getContext().startService(UploadChallengeService.getIntent(getView().getContext(), fingerprints, getView().getChallengeResult().getId(), filePath));
                getView().getContext().registerReceiver(receiver, new IntentFilter(ACTION_UPLOAD_COMPLETE));
                postId = fingerprints.getNewPostId();
            }
        });


    }


    public void deinit() {
        getView().getContext().unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction() == ACTION_UPLOAD_COMPLETE) {
                // launch home
                // getView().finish();
                if (!TextUtils.isEmpty(postId)) {
                    getMySubmissionsList();
                }
            }
            deinit();
        }
    };


    private boolean isValidateImageSize(@NonNull String fileLoc) {
        File file = new File(fileLoc);
        return file.length() <= Utils.MB_10;
    }

    private boolean isValidateVidoeSize(@NonNull String fileLoc) {
        File file = new File(fileLoc);
        return file.length() <= (Utils.MB_1 * 100);
    }

    private boolean isValidateDescription(@NonNull String description) {
        if (description.length() < 0 || description.length() > 300)
            return false;
        else
            return true;
    }

    private boolean isValidateTitle(@NonNull String title) {
        if (title.length() < 0 || title.length() > 50)
            return false;
        else
            return true;
    }

    @Override
    public void onCancelButtonClick() {
        getView().finish();
    }

    @Override
    public void onSelectedImageClick() {
        if (settings.isAllowVideos() && settings.isAllowPhotos()) {
            getView().showImageVideoPicker();
        } else if (settings.isAllowPhotos()) {
            getView().selectImage();
            //update UI
        } else if (settings.isAllowVideos()) {
            // update UI
            getView().selectVideo();
        }
    }

    public void getMySubmissionsList() {
        getDetailsSubmissionsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                RestResponse res1 = restResponse.get(SubmissionResult.class);
                SubmissionResult submissionResult = res1.getData();
                if (submissionResult != null) {
                    ShareBottomSheet.show(((AppCompatActivity) getView().getActivity()).getSupportFragmentManager(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getTitle(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getSharing().getMetaTags().getOgTitle(), submissionResult.getSharing().getMetaTags().getOgImage(), submissionResult.getId(), Utils.getApplinkPath(ChallengesUrl.AppLink.SUBMISSION_DETAILS, submissionResult.getId()), false);
                }
            }
        });
    }
}
