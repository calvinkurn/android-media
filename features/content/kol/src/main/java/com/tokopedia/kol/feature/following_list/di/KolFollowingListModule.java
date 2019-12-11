package com.tokopedia.kol.feature.following_list.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.feedcomponent.di.CoroutineDispatcherModule;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.feature.following_list.data.mapper.KolFollowerMapper;
import com.tokopedia.kol.feature.following_list.data.query.GetShopFollowingQueryProvider;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetFollowerListUseCase;
import com.tokopedia.kol.feature.following_list.domain.interactor
        .GetKolFollowingListLoadMoreUseCase;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetKolFollowingListUseCase;
import com.tokopedia.kol.feature.following_list.domain.interactor.GetShopFollowingListUseCase;
import com.tokopedia.kol.feature.following_list.view.listener.KolFollowingList;
import com.tokopedia.kol.feature.following_list.view.presenter.KolFollowingListPresenter;
import com.tokopedia.kol.feature.following_list.view.presenter.ShopFollowingListPresenter;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingResultViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.KolFollowingViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.ShopFollowingResultViewModel;
import com.tokopedia.kol.feature.following_list.view.viewmodel.ShopFollowingViewModel;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.kol.feature.following_list.domain.interactor.GetShopFollowingListUseCase.QUERY_USER_SHOP_FOLLOWING;

/**
 * @author by milhamj on 24/04/18.
 */

@Module(includes = {CoroutineDispatcherModule.class})
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
            ToggleFavouriteShopUseCase toggleFavouriteShopUseCase,
            UserSessionInterface userSession
    ) {
        return new ShopFollowingListPresenter(
                context,
                getShopFollowingListUseCase,
                toggleFavouriteShopUseCase,
                userSession
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
    @Named(QUERY_USER_SHOP_FOLLOWING)
    String providesGetShopFollowingQuery() {
        return GetShopFollowingQueryProvider.INSTANCE.getQuery();
    }

    @KolFollowingListScope
    @Provides
    public KolFollowerMapper providesKolFollowerMapper() {
        return new KolFollowerMapper();
    }
}
