package com.tokopedia.tokopoints.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.util.CommonConstant
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class TokopointsQueryModule {

    @Provides
    fun getContext(@ApplicationContext context: Context) = context

    @Provides
    fun getRepository() = Interactor.getInstance().graphqlRepository

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_CURRENT_POINTS)
    fun getGQLCurrentPoint(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_current_points)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_HISTORY_POINTS)
    fun getGQLHistoryPoint(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_point_history)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_APPLY_COUPON)
    fun getGQLTokopointApplyCoupon(context: Context) : String{
        return GraphqlHelper.loadRawString(context.getResources(),
                R.raw.tp_gql_tokopoint_apply_coupon)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_COUPON_DETAIL)
    fun getGQLCouponDetail(context: Context) : String {
        return GraphqlHelper.loadRawString(context.getResources(),
                R.raw.tp_gql_coupon_detail)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_REFETCH_REAL_CODE)
    fun getGQLRefetchRealCode(context: Context) : String {
        return GraphqlHelper.loadRawString(context.getResources(),
                R.raw.tp_gql_refetch_real_code)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_SWIPE_COUPON)
    fun getGQLSwipeCoupon(context: Context) : String {
        return GraphqlHelper.loadRawString(context.getResources(),
                R.raw.tp_gql_swipe_coupon)
    }
}