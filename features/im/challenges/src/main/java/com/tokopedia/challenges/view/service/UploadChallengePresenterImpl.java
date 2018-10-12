package com.tokopedia.challenges.view.service;

import android.util.Log;

import com.tokopedia.challenges.domain.usecase.UploadAWSUseCase;
import com.tokopedia.common.network.data.model.RestResponse;
import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class UploadChallengePresenterImpl implements IUploadChallengeServiceContract.UploadChallengePresenter {
    private IUploadChallengeServiceContract.UploadChallengeListener uploadChallengeService;

    private UploadAWSUseCase uploadAWSUseCase;


    @Inject
    public UploadChallengePresenterImpl(UploadAWSUseCase uploadAWSUseCase) {
        this.uploadAWSUseCase = uploadAWSUseCase;
    }

    public void attach(IUploadChallengeServiceContract.UploadChallengeListener uploadChallengeService) {
        this.uploadChallengeService = uploadChallengeService;
    }

    @Override
    public void uploadChallange() {
        uploadAWSUseCase.setChallengeId(uploadChallengeService.getChallengeId());
        uploadAWSUseCase.setFilePath(uploadChallengeService.getUploadFilePath());
        uploadAWSUseCase.setUploadFingerprints(uploadChallengeService.getUploadFingerprints());
        uploadAWSUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                uploadChallengeService.onProgressComplete();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                uploadChallengeService.onProgressFail();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Log.e("uploadchallenge",typeRestResponseMap.toString());
            }
        });
    }
}
