package com.tokopedia.challenges.view.fragments.submit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.domain.usecase.GetChallegeTermsUseCase;
import com.tokopedia.challenges.domain.usecase.GetChallengeSettingUseCase;
import com.tokopedia.challenges.domain.usecase.GetDetailsSubmissionsUseCase;
import com.tokopedia.challenges.domain.usecase.IntializeMultiPartUseCase;
import com.tokopedia.challenges.view.activity.SubmitDetailActivity;
import com.tokopedia.challenges.view.fragments.submit.IChallengesSubmitContract.Presenter;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResult;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.model.upload.UploadFingerprints;
import com.tokopedia.challenges.view.service.UploadChallengeService;
import com.tokopedia.challenges.view.utils.Utils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.imagepicker.common.util.ImageUtils;

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
            getView().setSnackBarErrorMessage("Please select image");
            return;
        } else if (!isValidateTitle(title)) {
            return;
        } else if (!isValidateDescription(description)) {
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
                //  getView().hideProgress();
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
                postId = fingerprints.getNewPostId();
                if (fingerprints.getTotalParts() > fingerprints.getPartsCompleted()) {
                    getView().showMessage("Upload Initiated Please Wait");
                    getView().getContext().startService(UploadChallengeService.getIntent(getView().getContext(), fingerprints, getView().getChallengeId(), filePath,postId));
                    getView().getContext().registerReceiver(receiver, new IntentFilter(ACTION_UPLOAD_COMPLETE));
                }

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
                getView().showMessage("Konten Anda diterima!");
                getView().hideProgress();
                Utils.FROMNOCACHE = true;
                if (!TextUtils.isEmpty(postId)) {
                    getSubmissionDetail();
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
        if (description.length() <= 0){
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_desc_blank));
            return false;
        }else if (description.length() > 50) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_wrong_descirption_size));
            return false;
        } else
            return true;
    }

    private boolean isValidateTitle(@NonNull String title) {
        if (title.length() <= 0){
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_title_blank));
        return false;
        }else if (title.length() > 50) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_wrong_title_size));
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
                getView().hideProgress();
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                getView().hideProgress();
                RestResponse res1 = restResponse.get(SubmissionResult.class);
                SubmissionResult submissionResult = res1.getData();
                if (submissionResult != null) {
                    Intent intent = new Intent(getView().getActivity(), SubmitDetailActivity.class);
                    intent.putExtra("submissionsResult", submissionResult);
                    intent.putExtra("fromSubmission", true);
                    getView().getActivity().startActivity(intent);
                    // ShareBottomSheet.show(((AppCompatActivity) getView().getActivity()).getSupportFragmentManager(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getTitle(), submissionResult.getSharing().getMetaTags().getOgUrl(), submissionResult.getSharing().getMetaTags().getOgTitle(), submissionResult.getSharing().getMetaTags().getOgImage(), submissionResult.getId(), Utils.getApplinkPathForBranch(ChallengesUrl.AppLink.SUBMISSION_DETAILS, submissionResult.getId()), false);
                    getView().getActivity().finish();
                }
            }
        });
    }

    @Override
    public void setSubmitButtonText() {
        ChallengeSettings settings = getView().getChallengeSettings();
        if (settings.isAllowVideos() && settings.isAllowPhotos()) {
            getView().setSubmitButtonText(getView().getActivity().getString(R.string.submit_photo_video));
            getView().setChooseImageText(getView().getActivity().getString(R.string.choose_image_title));
        } else if (settings.isAllowPhotos()) {
            getView().setSubmitButtonText(getView().getActivity().getString(R.string.submit_photo));
            getView().setChooseImageText(getView().getActivity().getString(R.string.choose_image_title_photo));
        } else if (settings.isAllowVideos()) {
            getView().setSubmitButtonText(getView().getActivity().getString(R.string.submit_video));
            getView().setChooseImageText(getView().getActivity().getString(R.string.choose_image_title_video));
        }
    }
}
