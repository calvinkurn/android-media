package com.tokopedia.abstraction.common.di.module.net;

import dagger.Module;

/**
 * Created by ricoharisin on 3/23/17.
 */

@Module(includes = {OkHttpClientModule.class, RetrofitModule.class})
public class NetModule {

}