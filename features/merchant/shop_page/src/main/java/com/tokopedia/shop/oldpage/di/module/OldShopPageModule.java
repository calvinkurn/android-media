package com.tokopedia.shop.oldpage.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.shop.R;
import com.tokopedia.shop.common.constant.ShopPageConstant;
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase;
import com.tokopedia.shop.note.domain.interactor.DeleteShopNoteUseCase;
import com.tokopedia.shop.oldpage.di.scope.OldShopPageScope;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductAceUseCase;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductTomeUseCase;
import com.tokopedia.shop.product.domain.interactor.DeleteShopProductUseCase;
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@OldShopPageScope
@Module(includes = ShopViewModelModule.class)
public class OldShopPageModule {

    @OldShopPageScope
    @Provides
    public DeleteShopInfoCacheUseCase provideDeleteShopInfoUseCase(@ApplicationContext Context context) {
        return new DeleteShopInfoCacheUseCase(context);
    }

    @OldShopPageScope
    @Provides
    public DeleteShopProductAceUseCase provideDeleteShopProductAceUseCase(@ApplicationContext Context context) {
        return new DeleteShopProductAceUseCase(context);
    }

    @OldShopPageScope
    @Provides
    public DeleteShopProductTomeUseCase provideDeleteShopProductTomeUseCase(@ApplicationContext Context context) {
        return new DeleteShopProductTomeUseCase(context);
    }

    @OldShopPageScope
    @Provides
    public DeleteShopProductUseCase provideDeleteShopProductUseCase(@ApplicationContext Context context) {
        return new DeleteShopProductUseCase(provideDeleteShopProductAceUseCase(context), provideDeleteShopProductTomeUseCase(context));
    }

    @OldShopPageScope
    @Provides
    public DeleteShopNoteUseCase provideDeleteShopNoteUseCase(@ApplicationContext Context context) {
        return new DeleteShopNoteUseCase(context);
    }

    @OldShopPageScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @OldShopPageScope
    @Provides
    @Named(ShopPageConstant.MODERATE_STATUS_QUERY)
    public String moderateStatusQuery(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.shop_moderate_request_status
        );
    }

    @OldShopPageScope
    @Provides
    @Named(ShopPageConstant.MODERATE_REQUEST_QUERY)
    public String requestQuery(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.mutation_moderate_shop
        );
    }

    @OldShopPageScope
    @Provides
    public StickyLoginUseCase provideStickyLoginUseCase(@ApplicationContext Context context, GraphqlRepository graphqlRepository){
        return new StickyLoginUseCase(context.getResources(), graphqlRepository);
    }
}