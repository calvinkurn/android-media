package com.tokopedia.loginregister.seamlesslogin.di

import dagger.Module

/**
 * Created by Ade Fulki on 2019-10-17.
 * ade.hadian@tokopedia.com
 */

@SeamlessLoginScope
@Module
class SeamlessLoginQueryModule{

//    @Provides
//    @IntoMap
//    @StringKey(SeamlessLoginQueryConstant.MUTATION_REGISTER_CHECK)
//    fun provideRawMutationRegisterCheck(@ApplicationContext context: Context): String =
//            GraphqlHelper.loadRawString(context.resources, R.raw.mutation_register_check)
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