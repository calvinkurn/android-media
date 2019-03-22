package com.tokopedia.shop.page.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.reputation.common.domain.interactor.DeleteReputationSpeedDailyCacheUseCase;
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase;
import com.tokopedia.shop.note.domain.interactor.DeleteShopNoteUseCase;
import com.tokopedia.shop.page.di.scope.ShopPageScope;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductAceUseCase;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductTomeUseCase;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@ShopPageScope
@Module
public class ShopPageModule {

    @ShopPageScope
    @Provides
    public DeleteShopInfoCacheUseCase provideDeleteShopInfoUseCase() {
        return new DeleteShopInfoCacheUseCase();
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
    public DeleteShopNoteUseCase provideDeleteShopNoteUseCase() {
        return new DeleteShopNoteUseCase();
    }

    @ShopPageScope
    @Provides
    public DeleteReputationSpeedDailyCacheUseCase provideDeleteReputationSpeedDailyCacheUseCase() {
        return new DeleteReputationSpeedDailyCacheUseCase();
    }

    @ShopPageScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}