package com.tokopedia.shop.page.di.module;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor;
import com.tokopedia.reputation.common.constant.ReputationCommonUrl;
import com.tokopedia.reputation.common.data.interceptor.ReputationAuthInterceptor;
import com.tokopedia.reputation.common.data.repository.ReputationCommonRepositoryImpl;
import com.tokopedia.reputation.common.data.source.ReputationCommonDataSource;
import com.tokopedia.reputation.common.data.source.cloud.ReputationCommonCloudDataSource;
import com.tokopedia.reputation.common.data.source.cloud.api.ReputationCommonApi;
import com.tokopedia.reputation.common.domain.interactor.GetReputationSpeedUseCase;
import com.tokopedia.reputation.common.domain.repository.ReputationCommonRepository;
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.shop.etalase.domain.interactor.DeleteShopEtalaseUseCase;
import com.tokopedia.shop.note.domain.interactor.DeleteShopNoteUseCase;
import com.tokopedia.shop.page.di.ShopInfoReputationSpeedQualifier;
import com.tokopedia.shop.page.di.scope.ShopPageScope;
import com.tokopedia.shop.page.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductAceUseCase;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductTomeUseCase;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@ShopPageScope
@Module
public class ShopPageModule {

    @ShopPageScope
    @Provides
    public DeleteShopInfoUseCase provideDeleteShopInfoUseCase() {
        return new DeleteShopInfoUseCase();
    }

    @ShopPageScope
    @Provides
    public DeleteShopProductAceUseCase provideDeleteShopProductAceUseCase() {
        return new DeleteShopProductAceUseCase();
    }

    @ShopPageScope
    @Provides
    public DeleteShopProductTomeUseCase provideDeleteShopProductTomeUseCase() {
        return new DeleteShopProductTomeUseCase();
    }

    @ShopPageScope
    @Provides
    public DeleteShopProductUseCase provideDeleteShopProductUseCase() {
        return new DeleteShopProductUseCase(provideDeleteShopProductAceUseCase(), provideDeleteShopProductTomeUseCase());
    }

    @ShopPageScope
    @Provides
    public DeleteShopEtalaseUseCase provideDeleteShopEtalaseUseCase() {
        return new DeleteShopEtalaseUseCase();
    }

    @ShopPageScope
    @Provides
    public DeleteShopNoteUseCase provideDeleteShopNoteUseCase() {
        return new DeleteShopNoteUseCase();
    }

    @ShopPageScope
    @Provides
    public ToggleFavouriteShopAndDeleteCacheUseCase provideToggleFavouriteShopAndDeleteCacheUseCase(
            ToggleFavouriteShopUseCase toggleFavouriteShopUseCase,
            DeleteShopInfoUseCase deleteShopInfoUseCase) {
        return new ToggleFavouriteShopAndDeleteCacheUseCase(toggleFavouriteShopUseCase, deleteShopInfoUseCase);
    }
}