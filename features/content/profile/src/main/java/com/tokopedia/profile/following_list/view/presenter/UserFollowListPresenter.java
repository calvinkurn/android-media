package com.tokopedia.profile.following_list.view.presenter;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowerListUseCase;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowingListLoadMoreUseCase;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowingListUseCase;
import com.tokopedia.profile.following_list.view.fragment.BaseFollowListFragment;
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

public class UserFollowListPresenter extends BaseDaggerPresenter<FollowingList.View<UserFollowingViewModel, UserFollowingResultViewModel>>
        implements FollowingList.Presenter<UserFollowingViewModel, UserFollowingResultViewModel> {

    private FollowingList.View mainView;
    private final GetFollowingListUseCase getFollowingListUseCase;
    private final GetFollowingListLoadMoreUseCase getFollowingListLoadMoreUseCase;
    private final GetFollowerListUseCase getFollowerListUseCase;

    @Inject
    public UserFollowListPresenter(GetFollowingListUseCase getFollowingListUseCase,
                                   GetFollowingListLoadMoreUseCase getFollowingListLoadMoreUseCase,
                                   GetFollowerListUseCase getFollowerListUseCase) {
        this.getFollowingListUseCase = getFollowingListUseCase;
        this.getFollowingListLoadMoreUseCase = getFollowingListLoadMoreUseCase;
        this.getFollowerListUseCase = getFollowerListUseCase;
    }

    @Override
    public void getFollowList(int userId, BaseFollowListFragment.FollowListType type) {
        switch (type) {
            case Following: getFollowingList(userId); break;
            case Follower: getFollowerList(userId); break;
        }
    }

    @Override
    public void getFollowListLoadMore(int userId, String cursor, BaseFollowListFragment.FollowListType type) {
        switch (type) {
            case Following: getFollowingListLoadMore(userId, cursor); break;
            case Follower: getFollowerListLoadMore(userId, cursor); break;
        }
    }

    private void getFollowingList(int userId) {
        mainView.showLoading();
        getFollowingListUseCase.execute(GetFollowingListUseCase.Companion.getParam(userId),
                new GetFollowingListSubscriber(mainView));
    }

    private void getFollowingListLoadMore(int userId, String cursor) {
        getFollowingListLoadMoreUseCase.execute(GetFollowingListLoadMoreUseCase.Companion.getParam(userId, cursor),
                new GetFollowingListLoadMoreSubscriber(mainView));
    }

    private void getFollowerList(int userId) {
        if (isViewAttached()) {
            getFollowerListUseCase.execute(GetFollowerListUseCase.getFollowerListParam(
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

    private void getFollowerListLoadMore(int userId, String cursor) {
        if (isViewAttached()) {

            getFollowerListUseCase.execute(GetFollowerListUseCase.getFollowerListParam(
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
    public void attachView(FollowingList.View view) {
        super.attachView(view);
        this.mainView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getFollowingListUseCase.unsubscribe();
        getFollowingListLoadMoreUseCase.unsubscribe();
        getFollowerListUseCase.unsubscribe();
    }

    @Override
    public void unfollowShop(@NonNull FollowingViewModel model) {

    }
}
