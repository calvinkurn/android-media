package com.tokopedia.shop.common.di.module;

import com.tokopedia.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.shop.common.data.source.cloud.api.ShopWSApi;
import com.tokopedia.shop.common.di.ShopCommonModule;
import com.tokopedia.shop.common.di.ShopQualifier;
import com.tokopedia.shop.common.di.ShopWSQualifier;
import com.tokopedia.shop.common.di.scope.ShopScope;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoByDomainUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ShopScope
@Module(includes = ShopCommonModule.class)
public class ShopModule {

    @ShopScope
    @Provides
    public ShopApi provideShopApi(@ShopQualifier Retrofit retrofit) {
        return retrofit.create(ShopApi.class);
    }

    @ShopScope
    @Provides
    public ShopWSApi provideShopWsApi(@ShopWSQualifier Retrofit retrofit) {
        return retrofit.create(ShopWSApi.class);
    }

    @ShopScope
    @Provides
    public GetShopInfoByDomainUseCase provideGetShopInfoByDomainUseCase(ShopCommonRepository shopCommonRepository) {
        return new GetShopInfoByDomainUseCase(shopCommonRepository);
    }

    @ShopScope
    @Provides
    public ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase(ShopCommonRepository shopCommonRepository) {
        return new ToggleFavouriteShopUseCase(shopCommonRepository);
    }
}