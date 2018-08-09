package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.challenges.domain.usecase.GetActiveChallengesUseCase;
import com.tokopedia.challenges.domain.usecase.GetPastChallengesUseCase;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.common.network.data.model.RestResponse;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class ChallengeHomePresenter extends BaseDaggerPresenter<ChallengesBaseContract.View> implements ChallengesBaseContract.Presenter {

    private GetActiveChallengesUseCase getActiveChallengesUseCase;
    GetPastChallengesUseCase getPastChallengesUseCase;

    @Inject
    public ChallengeHomePresenter(GetActiveChallengesUseCase getActiveChallengesUseCase, GetPastChallengesUseCase getPastChallengesUseCase) {
        this.getActiveChallengesUseCase = getActiveChallengesUseCase;
        this.getPastChallengesUseCase = getPastChallengesUseCase;
    }

    public void getOpenChallenges() {
        getView().showProgressBarView();

        getActiveChallengesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().removeProgressBarView();
                getView().showErrorNetwork(
                        ErrorHandler.getErrorMessage(getView().getActivity(), e));
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                getView().removeProgressBarView();
                RestResponse res1 = restResponse.get(Challenge.class);
                int responseCodeOfResponse1 = res1.getCode();
                Challenge mainDataObject = res1.getData();
                if (mainDataObject != null && mainDataObject.getResults() != null && mainDataObject.getResults().size() > 0) {
                    getView().setChallengeDataToUI(mainDataObject.getResults());
                } else {
                    getView().renderEmptyList();
                }
                CommonUtils.dumper("data data data data data data data");

            }
        });
    }

    public void getPastChallenges() {
        getView().showProgressBarView();
        getPastChallengesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().removeProgressBarView();
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                //Success scenario e.g. HTTP 200 OK
                RestResponse res1 = restResponse.get(Challenge.class);
                int responseCodeOfResponse1 = res1.getCode();
                Challenge mainDataObject = res1.getData();
                if (mainDataObject != null && mainDataObject.getResults() != null && mainDataObject.getResults().size() > 0) {
                    getView().setChallengeDataToUI(mainDataObject.getResults());
                } else {
                   getView().renderEmptyList();
                }
                getView().removeProgressBarView();
            }
        });
    }

}
