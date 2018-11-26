package com.tokopedia.challenges.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.challenges.domain.usecase.GetActiveChallengesUseCase;
import com.tokopedia.challenges.domain.usecase.GetPastChallengesUseCase;
import com.tokopedia.challenges.view.model.Challenge;
import com.tokopedia.common.network.data.model.RestResponse;

import java.lang.reflect.Type;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class ChallengeHomePresenter extends BaseDaggerPresenter<ChallengesBaseContract.View> implements ChallengesBaseContract.Presenter {

    private GetActiveChallengesUseCase getActiveChallengesUseCase;
    private GetPastChallengesUseCase getPastChallengesUseCase;

    @Inject
    public ChallengeHomePresenter(GetActiveChallengesUseCase getActiveChallengesUseCase, GetPastChallengesUseCase getPastChallengesUseCase) {
        this.getActiveChallengesUseCase = getActiveChallengesUseCase;
        this.getPastChallengesUseCase = getPastChallengesUseCase;
    }

    @Override
    public void getOpenChallenges() {
        if (!isViewAttached()) return;
        if (getView().getOpenChallenges() != null) {
            getView().setChallengeDataToUI(getView().getOpenChallenges(), false);
        } else {
            getView().showProgressBarView();
        }
        getPastChallengesUseCase.unsubscribe();
        getActiveChallengesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) return;
                e.printStackTrace();
                getView().removeProgressBarView();
                getView().setSwipeRefreshing();
                getView().showErrorNetwork(
                        ErrorHandler.getErrorMessage(getView().getActivity(), e));
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                if (!isViewAttached()) return;
                getView().removeProgressBarView();
                RestResponse res1 = restResponse.get(Challenge.class);
                Challenge mainDataObject = res1.getData();
                if (mainDataObject != null && mainDataObject.getResults() != null && mainDataObject.getResults().size() > 0) {
                    getView().setChallengeDataToUI(mainDataObject.getResults(), false);
                } else {
                    getView().renderEmptyList();
                }
                getView().setSwipeRefreshing();
            }
        });
    }

    @Override
    public void getPastChallenges() {
        if (!isViewAttached()) return;
        if (getView().getPastChallenges() != null) {
            getView().setChallengeDataToUI(getView().getPastChallenges(), true);
        } else {
            getView().showProgressBarView();
        }
        getActiveChallengesUseCase.unsubscribe();
        getPastChallengesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) return;
                getView().removeProgressBarView();
                getView().setSwipeRefreshing();
                getView().showErrorNetwork(
                        ErrorHandler.getErrorMessage(getView().getActivity(), e));
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> restResponse) {
                if (!isViewAttached()) return;
                RestResponse res1 = restResponse.get(Challenge.class);
                Challenge mainDataObject = res1.getData();
                if (mainDataObject != null && mainDataObject.getResults() != null && mainDataObject.getResults().size() > 0) {
                    getView().setChallengeDataToUI(mainDataObject.getResults(), true);
                } else {
                    getView().renderEmptyList();
                }
                getView().removeProgressBarView();
                getView().setSwipeRefreshing();
            }
        });
    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
