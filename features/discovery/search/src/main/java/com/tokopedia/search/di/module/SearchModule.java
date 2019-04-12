package com.tokopedia.search.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.di.module.ApiModule;
import com.tokopedia.discovery.newdiscovery.di.module.AttributeModule;
import com.tokopedia.discovery.newdiscovery.di.module.BannerModule;
import com.tokopedia.discovery.newdiscovery.di.module.CatalogModule;
import com.tokopedia.discovery.newdiscovery.di.module.GuidedSearchModule;
import com.tokopedia.discovery.newdiscovery.di.module.ShopModule;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.search.presentation.presenter.SearchPresenterModule;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = {
        SearchPresenterModule.class,
        GuidedSearchModule.class,
        BannerModule.class,
        ApiModule.class,
        CatalogModule.class,
        ShopModule.class,
        AttributeModule.class
})
public class SearchModule {

    @SearchScope
    @Provides
    SearchTracking provideSearchTracking(@ApplicationContext Context context,
                                         UserSessionInterface userSessionInterface) {
        return new SearchTracking(context, userSessionInterface);
    }
}
