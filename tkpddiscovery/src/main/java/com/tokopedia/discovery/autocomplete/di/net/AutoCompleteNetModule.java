package com.tokopedia.discovery.autocomplete.di.net;

import com.tokopedia.discovery.newdiscovery.constant.DiscoveryBaseURL;
import com.tokopedia.discovery.newdiscovery.di.qualifier.AutoCompleteQualifier;
import com.tokopedia.discovery.newdiscovery.di.qualifier.NoAuth;
import com.tokopedia.discovery.autocomplete.di.AutoCompleteScope;
import com.tokopedia.discovery.newdiscovery.network.BrowseApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@AutoCompleteScope
@Module(includes = AutoCompleteOkHttpClientModule.class)
public class AutoCompleteNetModule {
    @AutoCompleteScope
    @AutoCompleteQualifier
    @Provides
    BrowseApi provideSearchAutoCompleteApi(Retrofit.Builder builder,
                               @NoAuth OkHttpClient okHttpClient) {
        return builder.baseUrl(DiscoveryBaseURL.Ace.ACE_DOMAIN)
                .client(okHttpClient)
                .build()
                .create(BrowseApi.class);
    }
}
