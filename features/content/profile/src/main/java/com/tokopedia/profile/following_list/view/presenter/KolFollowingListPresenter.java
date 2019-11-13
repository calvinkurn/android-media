package com.tokopedia.profile.following_list.view.presenter;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.kol.R;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowerListUseCase;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowingListLoadMoreUseCase;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowingListUseCase;
import com.tokopedia.profile.following_list.view.listener.KolFollowingList;
import com.tokopedia.profile.following_list.view.subscriber
        .GetKolFollowingListLoadMoreSubscriber;
import com.tokopedia.profile.following_list.view.subscriber.GetKolFollowingListSubscriber;
import com.tokopedia.profile.following_list.view.viewmodel.FollowingViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.KolFollowingResultViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.KolFollowingViewModel;
import com.tokopedia.network.utils.ErrorHandler;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class KolFollowingListPresenter extends BaseDaggerPresenter<KolFollowingList.View<KolFollowingViewModel, KolFollowingResultViewModel>>
        implements KolFollowingList.Presenter<KolFollowingViewModel, KolFollowingResultViewModel> {

    private KolFollowingList.View mainView;
    private final GetFollowingListUseCase getFollowingListUseCase;
    private final GetFollowingListLoadMoreUseCase getFollowingListLoadMoreUseCase;
    private final GetFollowerListUseCase getFollowerList;

    @Inject
    public KolFollowingListPresenter(GetFollowingListUseCase getFollowingListUseCase,
                                     GetFollowingListLoadMoreUseCase getFollowingListLoadMoreUseCase,
                                     GetFollowerListUseCase getFollowerList) {
        this.getFollowingListUseCase = getFollowingListUseCase;
        this.getFollowingListLoadMoreUseCase = getFollowingListLoadMoreUseCase;
        this.getFollowerList = getFollowerList;
    }

    @Override
    public void getKolFollowingList(int userId) {
        mainView.showLoading();
        if (!getView().isOpenFollowerPage()) {
            getFollowingListUseCase.execute(GetFollowingListUseCase.Companion.getParam(userId),
                    new GetKolFollowingListSubscriber(mainView));
        } else {
            getFollowersList(userId);
        }
    }

    @Override
    public void getKolLoadMore(int userId, String cursor) {
        if (!getView().isOpenFollowerPage()) {
            getFollowingListLoadMoreUseCase.execute(GetFollowingListLoadMoreUseCase.Companion.getParam(userId, cursor),
                    new GetKolFollowingListLoadMoreSubscriber(mainView));
        } else {
            getFollowersLoadMore(userId, cursor);
        }

    }

    @Override
    public void attachView(KolFollowingList.View view) {
        super.attachView(view);
        this.mainView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getFollowingListUseCase.unsubscribe();
        getFollowingListLoadMoreUseCase.unsubscribe();
        getFollowerList.unsubscribe();
    }

    @Override
    public void getFollowersList(int userId) {
        if (isViewAttached()) {
            getFollowerList.execute(GetFollowerListUseCase.getFollowerListParam(
                    userId,""),
                    new Subscriber<KolFollowingResultViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mainView.hideLoading();
                    getView().onErrorGetKolFollowingList(ErrorHandler.getErrorMessage(getView().getContext(), e));
                }

                @Override
                public void onNext(KolFollowingResultViewModel result) {
                    mainView.hideLoading();
                    getView().onSuccessGetKolFollowingList(result);
                }
            });
        }
    }

    @Override
    public void getFollowersLoadMore(int userId, String cursor) {
        if (isViewAttached()) {

            getFollowerList.execute(GetFollowerListUseCase.getFollowerListParam(
                    userId, cursor),
                    new Subscriber<KolFollowingResultViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().onErrorLoadMoreKolFollowingList(ErrorHandler.getErrorMessage(getView().getContext(), e));
                }

                @Override
                public void onNext(KolFollowingResultViewModel result) {
                    getView().onSuccessLoadMoreKolFollowingList(result);
                }
            });
        }
    }

    @Override
    public void unfollowShop(@NonNull FollowingViewModel model) {

    }
}
