package com.tokopedia.autocomplete.di.net;

import com.tokopedia.autocomplete.di.AutoCompleteScope;
import com.tokopedia.autocomplete.di.qualifier.AutoCompleteQualifier;
import com.tokopedia.autocomplete.di.qualifier.NoAuth;
import com.tokopedia.autocomplete.network.AutocompleteBaseURL;
import com.tokopedia.autocomplete.network.BrowseApi;

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
        return builder.baseUrl(AutocompleteBaseURL.Ace.ACE_DOMAIN)
                .client(okHttpClient)
                .build()
                .create(BrowseApi.class);
    }
}
