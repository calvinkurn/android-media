package com.tokopedia.kol.feature.following_list.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.feature.following_list.data.mapper.KolFollowerMapper;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetFollowerListUseCase;
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
            GetKolFollowingListLoadMoreUseCase getKolFollowingListLoadMoreUseCase,
            GetFollowerListUseCase getFollowerList) {
        return new KolFollowingListPresenter(
                getKolFollowingListUseCase,
                getKolFollowingListLoadMoreUseCase,
                getFollowerList);
    }


    @KolFollowingListScope
    @Provides
    public GetFollowerListUseCase providesGetFollowerListUseCase(
            @ApplicationContext Context context,
            GraphqlUseCase graphqlUseCase,
            KolFollowerMapper mapper) {
        return new GetFollowerListUseCase(context, graphqlUseCase, mapper);
    }

    @KolFollowingListScope
    @Provides
    public KolFollowerMapper providesKolFollowerMapper() {
        return new KolFollowerMapper();
    }
}
