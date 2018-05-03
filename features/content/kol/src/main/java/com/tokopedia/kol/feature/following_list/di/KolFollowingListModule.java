package com.tokopedia.kol.feature.following_list.di;

import com.tokopedia.kol.feature.following_list.domain.interactor
        .GetKolFollowingListLoadMoreUseCase;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetKolFollowingListUseCase;
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList;
import com.tokopedia.kol.feature.following_list.view.presenter.KolFollowingListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 24/04/18.
 */

@Module
public class KolFollowingListModule {

    @KolFollowingListScope
    @Provides
    public KolFollowingList.Presenter providesPresenter(
            GetKolFollowingListUseCase getKolFollowingListUseCase,
            GetKolFollowingListLoadMoreUseCase getKolFollowingListLoadMoreUseCase) {
        return new KolFollowingListPresenter(
                getKolFollowingListUseCase,
                getKolFollowingListLoadMoreUseCase);
    }
}
