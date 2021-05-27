package com.tokopedia.logintest.login.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.logintest.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created by Ade Fulki on 2019-10-09.
 * ade.hadian@tokopedia.com
 */

@Module
class LoginTestAppQueryModule {

    @LoginTestAppScope
    @Provides
    @IntoMap
    @StringKey(LoginTestAppQueryConstant.QUERY_STATUS_PIN)
    fun provideRawQueryStatusPin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_status_pin)

    @LoginTestAppScope
    @Provides
    @IntoMap
    @StringKey(LoginTestAppQueryConstant.MUTATION_REGISTER_CHECK)
    fun provideRawMutationRegisterCheck(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_testapp_register_check)

}