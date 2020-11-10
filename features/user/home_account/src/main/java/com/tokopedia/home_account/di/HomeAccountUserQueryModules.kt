package com.tokopedia.home_account.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-10-09.
 * ade.hadian@tokopedia.com
 */

@HomeAccountUserScope
@Module
class HomeAccountUserQueryModules {

//    @HomeAccountUserScope
//    @Provides
//    @IntoMap
//    @StringKey(AdditionalCheckConstants.QUERY_CHECK_BOTTOM_SHEET)
//    fun provideRawQueryStatusPin(@AdditionalCheckContext context: Context): String =
//            GraphqlHelper.loadRawString(context.resources, R.raw.query_show_interrupt)

    @HomeAccountUserScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.NEW_QUERY_BUYER_ACCOUNT_HOME)
    fun provideRawQueryUserData(@HomeAccountUserContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_account_buyer)

    @HomeAccountUserScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.QUERY_USER_REWARDSHORCUT)
    fun provideRawQueryShortcut(@HomeAccountUserContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_account_shortcut)

    @HomeAccountUserScope
    @Provides
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(@HomeAccountUserContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_recommendation_widget)
    }

}