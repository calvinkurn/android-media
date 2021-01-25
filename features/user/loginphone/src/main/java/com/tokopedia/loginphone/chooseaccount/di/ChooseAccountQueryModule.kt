package com.tokopedia.loginphone.chooseaccount.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.loginphone.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created by Ade Fulki on 2019-11-18.
 * ade.hadian@tokopedia.com
 */

@Module
class ChooseAccountQueryModule{

    @Provides
    @IntoMap
    @StringKey(ChooseAccountQueryConstant.QUERY_GET_ACCOUNT_LIST)
    fun provideRawQueryGetAccountList(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_accounts_list)

}