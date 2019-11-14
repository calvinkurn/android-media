package com.tokopedia.profile.following_list.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.profile.following_list.domain.model.FollowingDomain;
import com.tokopedia.profile.following_list.domain.model.FollowingResultDomain;
import com.tokopedia.profile.following_list.view.listener.FollowingList;
import com.tokopedia.profile.following_list.view.viewmodel.ProfileFollowingResultViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.ProfileFollowingViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class GetKolFollowingListSubscriber extends Subscriber<FollowingResultDomain> {
    private FollowingList.View mainView;

    public GetKolFollowingListSubscriber(FollowingList.View mainView) {
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

    public static ProfileFollowingResultViewModel mappingViewModel(FollowingResultDomain domain) {
        return new ProfileFollowingResultViewModel(
                domain.isCanLoadMore(),
                mappingViewModels(domain.getFollowingDomainList()),
                domain.getLastCursor(),
                domain.getButtonText(),
                domain.getButtonApplink());
    }

    private static List<ProfileFollowingViewModel> mappingViewModels(List<FollowingDomain> domainList) {
        List<ProfileFollowingViewModel> viewModelList = new ArrayList<>();
        for (FollowingDomain domain : domainList) {
            ProfileFollowingViewModel viewModel = new ProfileFollowingViewModel(
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
