package com.tokopedia.challenges.view.fragments.submit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.R;
import com.tokopedia.challenges.domain.usecase.GetChallegeTermsUseCase;
import com.tokopedia.challenges.domain.usecase.GetChallengeSettingUseCase;
import com.tokopedia.challenges.domain.usecase.IntializeMultiPartUseCase;
import com.tokopedia.challenges.view.fragments.submit.IChallengesSubmitContract.Presenter;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.challenges.view.model.upload.ChallengeSettings;
import com.tokopedia.challenges.view.model.upload.UploadFingerprints;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.usecase.RequestParams;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChallengesSubmitPresenter extends BaseDaggerPresenter<IChallengesSubmitContract.View> implements Presenter {

    static private int MB_1 = 1024;
    static private int MB_10 = 10 * MB_1;
    GetChallengeSettingUseCase mGetChallengeSettingUseCase;
    GetChallegeTermsUseCase mGetChallegeTermsUseCase;
    IntializeMultiPartUseCase mIntializeMultiPartUseCase;
    public ChallengeSettings settings;

    @Inject
    public ChallengesSubmitPresenter(GetChallengeSettingUseCase mGetChallengeSettingUseCase, GetChallegeTermsUseCase mGetChallegeTermsUseCase, IntializeMultiPartUseCase mIntializeMultiPartUseCase) {
        this.mGetChallengeSettingUseCase = mGetChallengeSettingUseCase;
        this.mGetChallegeTermsUseCase = mGetChallegeTermsUseCase;
        this.mIntializeMultiPartUseCase = mIntializeMultiPartUseCase;
    }

    @Override
    public void attachView(IChallengesSubmitContract.View view) {
        super.attachView(view);
        getView().setChallengeTitle(getView().getChallengeResult().getTitle());
        getView().setChallengeDescription(getView().getChallengeResult().getDescription());
        mGetChallengeSettingUseCase.setCHALLENGE_ID(getView().getChallengeResult().getId());
        mGetChallegeTermsUseCase.setCHALLENGE_ID(getView().getChallengeResult().getId());
        mIntializeMultiPartUseCase.setCHALLENGE_ID(getView().getChallengeResult().getId());
        mGetChallengeSettingUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                RestResponse res1 = restResponse.get(ChallengeSettings.class);
                settings = res1.getData();
                if(!settings.isUploadAllowed()) {
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
        String imagePath = getView().getImage();
        if(imagePath == null || imagePath.isEmpty()) {
            getView().setSnackBarErrorMessage("Please select image");
        }else if (!isValidateTitle(title)) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_wrong_size));  // TODO update messages
        } else if (!isValidateDescription(description)) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_wrong_size));
        } else if (!isValidateSize(imagePath)) {
            getView().setSnackBarErrorMessage(getView().getContext().getResources().getString(R.string.error_msg_wrong_size));
        } else {
            mIntializeMultiPartUseCase.generateRequestParams(title,description,imagePath);
            mIntializeMultiPartUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Map<Type, RestResponse> restResponse) {

                }
            });
        }

    }

    private boolean isValidateSize(String fileLoc) {
        File file = new File(fileLoc);
        long size = file.length();
        return true;
    }

    private boolean isValidateDescription(@NonNull String description) {
        if (description.length() > 300)
            return false;
        else
            return true;
    }

    private boolean isValidateTitle(@NonNull String title) {
        if (title.length() > 50)
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
        if(settings.isAllowVideos() && settings.isAllowPhotos())  {
            getView().showImageVideoPicker();
        }else if(settings.isAllowPhotos()) {
            getView().selectImage();
            //update UI
        }else if(settings.isAllowVideos()){
            // update UI
            getView().selectVideo();
        }
    }

}
