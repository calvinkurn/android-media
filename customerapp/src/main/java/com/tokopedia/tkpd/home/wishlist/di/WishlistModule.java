package com.tokopedia.tkpd.home.wishlist.di;

import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.di.qualifier.MojitoQualifier;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.wishlist.data.WishlistDataMapper;
import com.tokopedia.tkpd.home.wishlist.data.WishlistDataRepository;
import com.tokopedia.tkpd.home.wishlist.domain.SearchWishlistUsecase;
import com.tokopedia.tkpd.home.wishlist.domain.WishlistRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author kulomady on 2/21/17.
 */
@Module
class WishlistModule {

    @WishlistScope
    @Provides
    SearchWishlistUsecase provideSearchWishlistUsecase(ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread,
                                                       WishlistRepository wishlistRepository) {

        return new SearchWishlistUsecase(threadExecutor, postExecutionThread, wishlistRepository);
    }

    @WishlistScope
    @Provides
    WishlistRepository provideWishlistRepository(MojitoService service,
                                                 WishlistDataMapper wishlistMapper) {

        return new WishlistDataRepository(service, wishlistMapper);
    }

    @WishlistScope
    @Provides
    MojitoService provideMojitoService(@MojitoQualifier Retrofit retrofit) {
        return retrofit.create(MojitoService.class);
    }
}
