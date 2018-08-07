package com.tokopedia.challenges.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.challenges.domain.usecase.GetChallengesUseCase;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.common.network.data.model.RestResponse;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChallengeHomePresenter extends BaseDaggerPresenter<ChallengesBaseContract.View> implements ChallengesBaseContract.Presenter{

    private GetChallengesUseCase getChallengesUseCase;

    @Inject
    public ChallengeHomePresenter(GetChallengesUseCase getChallengesUseCase) {
        this.getChallengesUseCase = getChallengesUseCase;
    }

    public void getOpenChallenges() {
        getChallengesUseCase.execute(new Subscriber<Map<Type,RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                //Success scenario e.g. HTTP 200 OK
                RestResponse res1 = restResponse.get(Challenge.class);
                int responseCodeOfResponse1 = res1.getCode();
                Challenge mainDataObject = res1.getData();

            }
        });
    }

}
