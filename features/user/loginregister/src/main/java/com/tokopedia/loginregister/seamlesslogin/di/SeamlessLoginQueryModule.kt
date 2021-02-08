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

@Module
class SeamlessLoginQueryModule{

    @Provides
    @IntoMap
    @StringKey(SeamlessLoginQueryConstant.QUERY_GET_KEY)
    fun provideRawMutationRegisterCheck(@SeamlessLoginContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_generate_dummy_key)
}