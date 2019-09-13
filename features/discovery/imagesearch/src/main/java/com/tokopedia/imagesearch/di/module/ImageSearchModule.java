package com.tokopedia.imagesearch.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListPresenter;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListPresenterImpl;
import com.tokopedia.imagesearch.data.mapper.ImageProductMapper;
import com.tokopedia.imagesearch.di.scope.ImageSearchScope;
import com.tokopedia.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.imagesearch.search.ImageSearchPresenter;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sachinbansal on 1/10/18.
 */

@ImageSearchScope
@Module
public class ImageSearchModule {

    @ImageSearchScope
    @Provides
    GetImageSearchUseCase getImageSearchUseCase(
            @ApplicationContext Context context,
            ImageProductMapper imageProductMapper) {
        return new GetImageSearchUseCase(context, graphqlUseCase, imageProductMapper);
    }

    @ImageSearchScope
    @Provides
    ImageProductMapper imageProductMapper() {
        return new ImageProductMapper();
    }

    @ImageSearchScope
    @Provides
    GraphqlUseCase graphqlUseCase() {
        return new GraphqlUseCase();
    }

    @ImageSearchScope
    @Provides
    ImageSearchPresenter provideImageSearchPresenter(GetImageSearchUseCase getImageSearchUseCase) {
        return new ImageSearchPresenter(getImageSearchUseCase);
    }

    @ImageSearchScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ImageSearchScope
    @Provides
    ImageProductListPresenter provideImageProductListPresenter(@ApplicationContext Context context) {
        return new ImageProductListPresenterImpl(context);
    }

    @ImageSearchScope
    @Provides
    PermissionCheckerHelper providePermissionCheckerHelper() {
        return new PermissionCheckerHelper();
    }
}
