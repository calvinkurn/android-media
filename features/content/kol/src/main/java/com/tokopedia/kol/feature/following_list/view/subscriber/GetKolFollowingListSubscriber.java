package com.tokopedia.kol.feature.following_list.view.subscriber;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.kol.feature.following_list.domain.model.KolFollowingDomain;
import com.tokopedia.kol.feature.following_list.domain.model.KolFollowingResultDomain;
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by yfsx on 28/12/17.
 */

public class GetKolFollowingListSubscriber extends Subscriber<KolFollowingResultDomain> {
    private KolFollowingList.View mainView;

    public GetKolFollowingListSubscriber(KolFollowingList.View mainView) {
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
    public void onNext(KolFollowingResultDomain kolFollowingResultDomain) {
        mainView.hideLoading();
        if (kolFollowingResultDomain.getKolFollowingDomainList().size() != 0) {
            mainView.onSuccessGetKolFollowingList(mappingViewModel(kolFollowingResultDomain));
        } else {
            mainView.onSuccessGetKolFollowingListEmptyState();
        }
    }

    public static KolFollowingResultViewModel mappingViewModel(KolFollowingResultDomain domain) {
        return new KolFollowingResultViewModel(
                domain.isCanLoadMore(),
                mappingViewModels(domain.getKolFollowingDomainList()),
                domain.getLastCursor(),
                domain.getButtonText(),
                domain.getButtonApplink());
    }

    private static List<KolFollowingViewModel> mappingViewModels(List<KolFollowingDomain> domainList) {
        List<KolFollowingViewModel> viewModelList = new ArrayList<>();
        for (KolFollowingDomain domain : domainList) {
            KolFollowingViewModel viewModel = new KolFollowingViewModel(
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
