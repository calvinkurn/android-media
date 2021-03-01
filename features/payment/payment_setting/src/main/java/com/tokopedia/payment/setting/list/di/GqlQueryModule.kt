package com.tokopedia.payment.setting.list.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.payment.setting.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(GQL_GET_CREDIT_CARD_LIST)
    fun provideGetCreditCardList(context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.credit_card_list_query)


}