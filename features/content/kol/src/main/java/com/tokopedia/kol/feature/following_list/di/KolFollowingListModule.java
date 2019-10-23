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
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.ShopFollowingResultViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.ShopFollowingViewModel;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;

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
    public KolFollowingList.Presenter<KolFollowingViewModel, KolFollowingResultViewModel> provideKolFollowingListPresenter(
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
    public KolFollowingList.Presenter<ShopFollowingViewModel, ShopFollowingResultViewModel> provideShopFollowingListPresenter(
            @ApplicationContext Context context,
            GetShopFollowingListUseCase getShopFollowingListUseCase,
            ToggleFavouriteShopUseCase toggleFavouriteShopUseCase
    ) {
        return new ShopFollowingListPresenter(
                context,
                getShopFollowingListUseCase,
                toggleFavouriteShopUseCase
        );
    }

    @KolFollowingListScope
    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase() {
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }

    @KolFollowingListScope
    @Provides
    ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase( @ApplicationContext Context context) {
        return new ToggleFavouriteShopUseCase( new GraphqlUseCase(), context.getResources());
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
