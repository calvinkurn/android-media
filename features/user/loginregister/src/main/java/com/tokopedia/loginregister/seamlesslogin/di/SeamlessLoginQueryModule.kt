package com.tokopedia.loginregister.seamlesslogin.di

import android.content.Context
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

@SeamlessLoginScope
@Module
class SeamlessLoginQueryModule{

    @Provides
    @IntoMap
    @StringKey(SeamlessLoginQueryConstant.QUERY_GET_KEY)
    fun provideRawMutationRegisterCheck(@SeamlessLoginContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_generate_dummy_key)
//
//    @Provides
//    @IntoMap
//    @StringKey(SeamlessLoginQueryConstant.MUTATION_REGISTER_REQUEST)
//    fun provideRawMutationRegisterRequest(@ApplicationContext context: Context): String =
//            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_register_request)
//
//    @Provides
//    @IntoMap
//    @StringKey(SeamlessLoginQueryConstant.MUTATION_ACTIVATE_USER)
//    fun provideRawMutationActivateUser(@ApplicationContext context: Context): String =
//            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_activate_user)
}