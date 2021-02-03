package com.tokopedia.loginregister.login.dagger

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.di.LoginQueryConstant
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

    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.QUERY_STATUS_PIN)
    fun provideRawQueryStatusPin(@ApplicationContext context: Context): String = ""

    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.MUTATION_REGISTER_CHECK)
    fun provideRawMutationRegisterCheck(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_register_check)


    @Provides
    @IntoMap
    @StringKey(LoginQueryConstant.QUERY_VERIFY_FINGERPRINT)
    fun provideRawQueryVerifyFingerprint(@ApplicationContext context: Context): String = ""

}