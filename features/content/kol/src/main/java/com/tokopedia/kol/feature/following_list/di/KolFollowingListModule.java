package com.tokopedia.kol.feature.following_list.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.feature.following_list.data.mapper.KolFollowerMapper;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetFollowerListUseCase;
import com.tokopedia.kol.feature.following_list.domain.interactor
        .GetKolFollowingListLoadMoreUseCase;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetKolFollowingListUseCase;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetShopFollowingListUseCase;
import com.tokopedia.kol.feature.following_list.domain.query.GetShopFollowingQuery;
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList;
import com.tokopedia.kol.feature.following_list.view.presenter.KolFollowingListPresenter;
import com.tokopedia.kol.feature.following_list.view.presenter.ShopFollowingListPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 24/04/18.
 */

@Module
public class KolFollowingListModule {

    @KolFollowingListScope
    @Provides
    @Named(KolFollowingListPresenter.NAME)
    public KolFollowingList.Presenter provideKolFollowingListPresenter(
            GetKolFollowingListUseCase getKolFollowingListUseCase,
            GetKolFollowingListLoadMoreUseCase getKolFollowingListLoadMoreUseCase,
            GetFollowerListUseCase getFollowerListUseCase
    ) {
        return new KolFollowingListPresenter(
                getKolFollowingListUseCase,
                getKolFollowingListLoadMoreUseCase,
                getFollowerListUseCase
        );
    }

    @KolFollowingListScope
    @Provides
    @Named(ShopFollowingListPresenter.NAME)
    public KolFollowingList.Presenter provideShopFollowingListPresenter(
            GetShopFollowingListUseCase getShopFollowingListUseCase
    ) {
        return new ShopFollowingListPresenter(
                getShopFollowingListUseCase
        );
    }

    @KolFollowingListScope
    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase() {
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
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
    public GetShopFollowingListUseCase provideGetShopFollowingListUseCase(MultiRequestGraphqlUseCase multiRequestGraphqlUseCase) {
        return new GetShopFollowingListUseCase(GetShopFollowingQuery.INSTANCE.getQuery(), multiRequestGraphqlUseCase);
    }

    @KolFollowingListScope
    @Provides
    public KolFollowerMapper providesKolFollowerMapper() {
        return new KolFollowerMapper();
    }
}
