package com.tokopedia.tkpd.beranda.di.module;

import com.tokopedia.core.network.apiservices.ace.apis.SearchApi;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class ApiModule {

    @Provides
    MojitoApi mojitoApi(@MojitoQualifier Retrofit retrofit){
        return retrofit.create(MojitoApi.class);
    }

    @Provides
    SearchApi searchApi(@AceQualifier Retrofit retrofit){
        return retrofit.create(SearchApi.class);
    }

    @Provides
    CategoryApi categoryApi(@MojitoQualifier Retrofit retrofit){
        return retrofit.create(CategoryApi.class);
    }
}
