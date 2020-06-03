package com.tokopedia.payment.setting.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.di.GQL_GET_CREDIT_CARD_LIST
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GqlQueryModule {

    @Provides
    @Named(GQL_DELETE_CREDIT_CARD_LIST)
    fun provideDeleteCredit(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.delete_credit_card_query)


}