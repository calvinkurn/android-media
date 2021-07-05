package com.tokopedia.loginregister.registerinitial.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.loginregister.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created by Ade Fulki on 2019-10-17.
 * ade.hadian@tokopedia.com
 */

@Module
class RegisterInitialQueryModule{

    @Provides
    @IntoMap
    @StringKey(RegisterInitialQueryConstant.MUTATION_REGISTER_CHECK)
    fun provideRawMutationRegisterCheck(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_register_check)

    @Provides
    @IntoMap
    @StringKey(RegisterInitialQueryConstant.MUTATION_REGISTER_REQUEST)
    fun provideRawMutationRegisterRequest(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_register_request)

    @Provides
    @IntoMap
    @StringKey(RegisterInitialQueryConstant.MUTATION_ACTIVATE_USER)
    fun provideRawMutationActivateUser(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_activate_user)
}