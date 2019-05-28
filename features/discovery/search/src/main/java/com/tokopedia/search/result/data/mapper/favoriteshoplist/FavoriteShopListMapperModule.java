package com.tokopedia.search.result.data.mapper.favoriteshoplist;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.domain.model.FavoriteShopListModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Response;
import rx.functions.Func1;

@SearchScope
@Module
public class FavoriteShopListMapperModule {

    @SearchScope
    @Provides
    Func1<Response<FavoriteShopListModel>, FavoriteShopListModel> provideFavoriteShopListMapper() {
        return new FavoriteShopListMapper();
    }

    @SearchScope
    @Provides
    Func1<Throwable, Response<FavoriteShopListModel>> provideFavoriteShopListErrorMapper() {
        return new FavoriteShopListErrorMapper();
    }
}
