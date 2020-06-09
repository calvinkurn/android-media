package com.tokopedia.payment.setting.authenticate.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.payment.setting.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(GQL_CHECK_UPDATE_WHITE_LIST)
    fun provideWhiteListCreditCardQuery(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.whitelist_credit_card)

}