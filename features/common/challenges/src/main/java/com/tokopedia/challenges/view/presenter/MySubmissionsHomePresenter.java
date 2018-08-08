package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.domain.usecase.GetActiveChallengesUseCase;
import com.tokopedia.challenges.domain.usecase.GetMySubmissionsListUseCase;
import com.tokopedia.challenges.domain.usecase.GetPastChallengesUseCase;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.challenges.view.model.challengesubmission.SubmissionResponse;
import com.tokopedia.common.network.data.model.RestResponse;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class MySubmissionsHomePresenter extends BaseDaggerPresenter<MySubmissionsBaseContract.View> implements MySubmissionsBaseContract.Presenter {

    private GetMySubmissionsListUseCase getMySubmissionsListUseCase;

    @Inject
    public MySubmissionsHomePresenter(GetMySubmissionsListUseCase getMySubmissionsListUseCase) {
        this.getMySubmissionsListUseCase = getMySubmissionsListUseCase;
    }

    public void getMySubmissionsList() {
        getMySubmissionsListUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                RestResponse res1 = restResponse.get(SubmissionResponse.class);
                int responseCodeOfResponse1 = res1.getCode();
                SubmissionResponse mainDataObject = res1.getData();
                getView().setSubmissionsDataToUI(mainDataObject.getSubmissionResults());
            }
        });
    }

}
