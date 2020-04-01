package com.tokopedia.profile.following_list.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.profile.following_list.domain.model.FollowingDomain;
import com.tokopedia.profile.following_list.domain.model.FollowingResultDomain;
import com.tokopedia.profile.following_list.view.listener.FollowingList;
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingResultViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class GetFollowingListSubscriber extends Subscriber<FollowingResultDomain> {
    private FollowingList.View mainView;

    public GetFollowingListSubscriber(FollowingList.View mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        mainView.hideLoading();
        mainView.onErrorGetKolFollowingList(
                ErrorHandler.getErrorMessage(mainView.getContext(), throwable)
        );
    }

    @Override
    public void onNext(FollowingResultDomain followingResultDomain) {
        mainView.hideLoading();
        if (followingResultDomain.getFollowingDomainList().size() != 0) {
            mainView.onSuccessGetKolFollowingList(mappingViewModel(followingResultDomain));
        } else {
            mainView.onSuccessGetKolFollowingListEmptyState();
        }
    }

    public static UserFollowingResultViewModel mappingViewModel(FollowingResultDomain domain) {
        return new UserFollowingResultViewModel(
                domain.isCanLoadMore(),
                mappingViewModels(domain.getFollowingDomainList()),
                domain.getLastCursor(),
                domain.getButtonText(),
                domain.getButtonApplink());
    }

    private static List<UserFollowingViewModel> mappingViewModels(List<FollowingDomain> domainList) {
        List<UserFollowingViewModel> viewModelList = new ArrayList<>();
        for (FollowingDomain domain : domainList) {
            UserFollowingViewModel viewModel = new UserFollowingViewModel(
                    domain.getId(),
                    domain.getAvatarUrl(),
                    domain.getProfileApplink(),
                    domain.getProfileUrl(),
                    domain.isInfluencer(),
                    domain.getName());
            viewModelList.add(viewModel);
        }
        return viewModelList;
    }
}
