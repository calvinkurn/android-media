package com.tokopedia.home_account.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
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

@Module
class HomeAccountUserQueryModules {

    @ActivityScope
    @Provides
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_recommendation_widget)
    }

    @ActivityScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.QUERY_GET_BALANCE)
    fun provideRawQueryGetBalance(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_balance)

    @ActivityScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.QUERY_TOKOPOINTS_DRAWER_LIST)
    fun provideRawQueryTokopointsDrawerList(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_tokopoints_drawer_list)

    @ActivityScope
    @Provides
    @IntoMap
    @StringKey(AccountConstants.Query.QUERY_GET_USER_ASSET_CONFIG)
    fun provideRawQueryGetUserAssetConfig(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.query_get_user_asset_config)
}
