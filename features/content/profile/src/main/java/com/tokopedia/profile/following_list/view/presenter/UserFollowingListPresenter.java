package com.tokopedia.profile.following_list.view.presenter;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowerListUseCase;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowingListLoadMoreUseCase;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowingListUseCase;
import com.tokopedia.profile.following_list.view.listener.FollowingList;
import com.tokopedia.profile.following_list.view.subscriber.GetFollowingListLoadMoreSubscriber;
import com.tokopedia.profile.following_list.view.subscriber.GetFollowingListSubscriber;
import com.tokopedia.profile.following_list.view.viewmodel.FollowingViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingResultViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingViewModel;
import com.tokopedia.network.utils.ErrorHandler;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class UserFollowingListPresenter extends BaseDaggerPresenter<FollowingList.View<UserFollowingViewModel, UserFollowingResultViewModel>>
        implements FollowingList.Presenter<UserFollowingViewModel, UserFollowingResultViewModel> {

    private FollowingList.View mainView;
    private final GetFollowingListUseCase getFollowingListUseCase;
    private final GetFollowingListLoadMoreUseCase getFollowingListLoadMoreUseCase;
    private final GetFollowerListUseCase getFollowerList;

    @Inject
    public UserFollowingListPresenter(GetFollowingListUseCase getFollowingListUseCase,
                                      GetFollowingListLoadMoreUseCase getFollowingListLoadMoreUseCase,
                                      GetFollowerListUseCase getFollowerList) {
        this.getFollowingListUseCase = getFollowingListUseCase;
        this.getFollowingListLoadMoreUseCase = getFollowingListLoadMoreUseCase;
        this.getFollowerList = getFollowerList;
    }

    @Override
    public void getFollowingList(int userId) {
        mainView.showLoading();
        if (!getView().isOpenFollowerPage()) {
            getFollowingListUseCase.execute(GetFollowingListUseCase.Companion.getParam(userId),
                    new GetFollowingListSubscriber(mainView));
        } else {
            getFollowersList(userId);
        }
    }

    @Override
    public void getFollowingListLoadMore(int userId, String cursor) {
        if (!getView().isOpenFollowerPage()) {
            getFollowingListLoadMoreUseCase.execute(GetFollowingListLoadMoreUseCase.Companion.getParam(userId, cursor),
                    new GetFollowingListLoadMoreSubscriber(mainView));
        } else {
            getFollowersLoadMore(userId, cursor);
        }

    }

    @Override
    public void attachView(FollowingList.View view) {
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
                    new Subscriber<UserFollowingResultViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    mainView.hideLoading();
                    getView().onErrorGetKolFollowingList(ErrorHandler.getErrorMessage(getView().getContext(), e));
                }

                @Override
                public void onNext(UserFollowingResultViewModel result) {
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
                    new Subscriber<UserFollowingResultViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().onErrorLoadMoreKolFollowingList(ErrorHandler.getErrorMessage(getView().getContext(), e));
                }

                @Override
                public void onNext(UserFollowingResultViewModel result) {
                    getView().onSuccessLoadMoreKolFollowingList(result);
                }
            });
        }
    }

    @Override
    public void unfollowShop(@NonNull FollowingViewModel model) {

    }
}
