package com.tokopedia.loginregister.login.dagger

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.di.LoginQueryConstant
import com.tokopedia.loginregister.login.di.LoginScope
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created by Yoris Prayogo on 09/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
@Module
class MockLoginQueryModule {

    @LoginScope
    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.QUERY_STATUS_PIN)
    fun provideRawQueryStatusPin(@ApplicationContext context: Context): String = ""

    @LoginScope
    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.MUTATION_REGISTER_CHECK)
    fun provideRawMutationRegisterCheck(@ApplicationContext context: Context): String = ""

    @LoginScope
    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.QUERY_VERIFY_FINGERPRINT)
    fun provideRawQueryVerifyFingerprint(@ApplicationContext context: Context): String = ""

}