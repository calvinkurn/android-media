package com.tokopedia.profile.following_list.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.feedcomponent.di.CoroutineDispatcherModule;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.profile.following_list.data.mapper.FollowerMapper;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowerListUseCase;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowingListLoadMoreUseCase;
import com.tokopedia.profile.following_list.domain.interactor.GetFollowingListUseCase;
import com.tokopedia.profile.following_list.domain.interactor.GetShopFollowingListUseCase;
import com.tokopedia.profile.following_list.view.listener.FollowingList;
import com.tokopedia.profile.following_list.view.presenter.UserFollowingListPresenter;
import com.tokopedia.profile.following_list.view.presenter.ShopFollowingListPresenter;
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingResultViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.ShopFollowingResultViewModel;
import com.tokopedia.profile.following_list.view.viewmodel.ShopFollowingViewModel;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 24/04/18.
 */

@Module(includes = {CoroutineDispatcherModule.class})
public class FollowingListModule {

    @FollowingListScope
    @Provides
    public FollowingList.Presenter<UserFollowingViewModel, UserFollowingResultViewModel> provideKolFollowingListPresenter(
            GetFollowingListUseCase getFollowingListUseCase,
            GetFollowingListLoadMoreUseCase getFollowingListLoadMoreUseCase,
            GetFollowerListUseCase getFollowerListUseCase
    ) {
        return new UserFollowingListPresenter(
                getFollowingListUseCase,
                getFollowingListLoadMoreUseCase,
                getFollowerListUseCase
        );
    }

    @FollowingListScope
    @Provides
    public FollowingList.Presenter<ShopFollowingViewModel, ShopFollowingResultViewModel> provideShopFollowingListPresenter(
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

    @FollowingListScope
    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase() {
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }

    @FollowingListScope
    @Provides
    ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase( @ApplicationContext Context context) {
        return new ToggleFavouriteShopUseCase( new GraphqlUseCase(), context.getResources());
    }

    @FollowingListScope
    @Provides
    public FollowerMapper providesKolFollowerMapper() {
        return new FollowerMapper();
    }
}
