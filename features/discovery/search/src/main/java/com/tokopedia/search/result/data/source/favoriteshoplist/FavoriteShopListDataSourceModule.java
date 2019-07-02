package com.tokopedia.search.result.data.source.favoriteshoplist;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.qualifier.TomeQualifier;
import com.tokopedia.search.result.data.mapper.favoriteshoplist.FavoriteShopListMapperModule;
import com.tokopedia.search.result.domain.model.FavoriteShopListModel;
import com.tokopedia.search.result.network.service.TomeApi;
import com.tokopedia.search.result.network.service.TomeApiModule;

import dagger.Module;
import dagger.Provides;
import retrofit2.Response;
import rx.functions.Func1;

@SearchScope
@Module(includes = {
        TomeApiModule.class,
        FavoriteShopListMapperModule.class
})
public class FavoriteShopListDataSourceModule {

    @SearchScope
    @Provides
    FavoriteShopListDataSource provideFavoriteShopListDataSource(
            @TomeQualifier TomeApi tomeApi,
            Func1<Response<FavoriteShopListModel>, FavoriteShopListModel> favoriteShopListMapper,
            Func1<Throwable, Response<FavoriteShopListModel>> favoriteShopListErrorMapper
    ) {
        return new FavoriteShopListDataSource(tomeApi, favoriteShopListMapper, favoriteShopListErrorMapper);
    }
}
