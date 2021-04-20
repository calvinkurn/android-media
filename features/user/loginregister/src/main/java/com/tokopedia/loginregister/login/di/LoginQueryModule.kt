package com.tokopedia.loginregister.login.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.loginregister.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created by Ade Fulki on 2019-10-09.
 * ade.hadian@tokopedia.com
 */

@Module
class LoginQueryModule {

    @LoginScope
    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.QUERY_STATUS_PIN)
    fun provideRawQueryStatusPin(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_status_pin)

    @LoginScope
    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.MUTATION_REGISTER_CHECK)
    fun provideRawMutationRegisterCheck(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_register_check)

    @LoginScope
    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.QUERY_VERIFY_FINGERPRINT)
    fun provideRawQueryVerifyFingerprint(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_verify_fingerprint)

    @LoginScope
    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.QUERY_REGISTER_PUSH_NOTIF)
    fun provideRawQueryRegisterPushNotif(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_register_push_notif)

}