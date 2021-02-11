package com.tokopedia.tokopoints.di

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.util.CommonConstant
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Named

@Module
class TokopointsQueryModule(val activity: Activity) {

    @Provides
    fun getContext(): Context = activity

    @Provides
    fun getRepository() = Interactor.getInstance().graphqlRepository

    @Provides
    @TokoPointScope
    @Named(CommonConstant.GQLQuery.TP_GQL_CURRENT_POINTS)
    fun getGQLCurrentPoint(context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_current_points)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_APPLY_COUPON)
    fun getGQLTokopointApplyCoupon(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_tokopoint_apply_coupon)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_CATALOG_LIST)
    fun getCatalogList(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
                R.raw.tp_gql_catalog_listing)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_COUPON_DETAIL)
    fun getGQLCouponDetail(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_coupon_detail)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_REFETCH_REAL_CODE)
    fun getGQLRefetchRealCode(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_refetch_real_code)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_SWIPE_COUPON)
    fun getGQLSwipeCoupon(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_swipe_coupon)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_COUPON_FILTER)
    fun getGQLCouponFilter(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_coupon_filter)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_COUPON_LISTING_STACK)
    fun getGQLCouponListingStack(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_coupon_listing_stack)
    }


    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_COUPON_IN_STACK)
    fun getGQLCouponInStack(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_coupon_in_stack)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_USER_INFO)
    fun isPhoneVerified(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_user_info)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_REDEEM_COUPON)
    fun getGQLRedeemCoupon(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_tokopoint_redeem_coupon)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_CATLOG_STATUS)
    fun getGQLCatalogStatus(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_catalog_status)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_VALIDATE_REDEEM)
    fun getGQLTokopointValidateRedeem(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_tokopoint_validate_redeem)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_PRE_VALIDATE_REDEEM)
    fun getGQLPreValidateREdeem(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_pre_validate_redeem)
    }

    @Provides
    @IntoMap
    @StringKey(CommonConstant.GQLQuery.TP_GQL_CATALOG_DETAIL)
    fun getGQLCatalogDetail(context: Context): String {
        return GraphqlHelper.loadRawString(context.getResources(),
            R.raw.tp_gql_catalog_detail)
    }

    @Provides
    @TokoPointScope
    @Named(CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_DETAIL)
    fun getGQLTOkopointDetail(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_tokopoint_detail)
    }

    @Provides
    @TokoPointScope
    @Named(CommonConstant.GQLQuery.TP_GQL_CATALOG_FILTER)
    fun getGQLCatalogFilter(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_catalog_filter)
    }

    @Provides
    @TokoPointScope
    @Named(CommonConstant.GQLQuery.TP_GQL_LUCKY_EGG_DETAILS)
    fun getGQLLuckyEggDetail(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_lucky_egg_details)
    }

    @Provides
    @TokoPointScope
    @Named(CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_TOP_SECTION_NEW)
    fun getGQLTOkopointNewDetail(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_topsection_new)
    }

    @Provides
    @TokoPointScope
    @Named(CommonConstant.GQLQuery.TP_GQL_HOME_PAGE_SECTION)
    fun getGQLHomePageSection(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_homepage_section)
    }

    @Provides
    @TokoPointScope
    @Named(CommonConstant.GQLQuery.TP_GQL_REWARD_INTRO)
    fun getGQLRewardIntro(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_reward_intro)
    }

    @Provides
    @TokoPointScope
    @Named(CommonConstant.GQLQuery.TP_GQL_REWARD_USESAVING)
    fun getGQLRewardUserSaving(context: Context) : String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.tp_gql_usersaving)
    }
}