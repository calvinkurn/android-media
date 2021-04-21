package com.tokopedia.common_digital.common

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst
import com.tokopedia.common_digital.common.presentation.model.DigitalAtcTrackingModel
import com.tokopedia.common_digital.common.presentation.model.RechargePushEventRecommendationResponseEntity
import com.tokopedia.common_digital.common.usecase.RechargePushEventRecommendationUseCase
import com.tokopedia.common_digital.common.util.BranchProductGroup
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.RechargeLinkerData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst
import rx.Subscriber
import java.util.*

class RechargeAnalytics(private val rechargePushEventRecommendationUseCase: RechargePushEventRecommendationUseCase) {

    fun eventOpenScreen(userId: String, categoryName: String, categoryId: String) {
        val stringScreenName = StringBuilder(RECHARGE_SCREEN_NAME)
        stringScreenName.append(categoryName.toLowerCase())

        val mapOpenScreen = HashMap<String, String>()
        mapOpenScreen[EVENT_NAME] = OPEN_SCREEN_EVENT
        mapOpenScreen[CATEGORY] = categoryName
        mapOpenScreen[CATEGORY_ID] = categoryId
        mapOpenScreen[USER_ID] = userId
        mapOpenScreen[IS_LOGIN_STATUS] = if (userId.isNotEmpty()) "true" else "false"
        mapOpenScreen[BUSINESS_UNIT] = BUSINESS_UNIT_RECHARGE
        mapOpenScreen[CURRENT_SITE] = CURRENT_SITE_RECHARGE

        val mapScreenLaunchData = mapOf(
                CATEGORY to categoryName,
                CATEGORY_ID_SCREEN_LAUNCH to categoryId
        )

        TrackApp.getInstance().gtm.sendScreenAuthenticated(stringScreenName.toString(), mapOpenScreen)
        TrackApp.getInstance().gtm.pushEvent(EVENT_DIGITAL_CATEGORY_SCREEN_LAUNCH, mapScreenLaunchData)

        val groupWiseCategoryId = BranchProductGroup.getGroupWiseProductID(categoryId)

        // Branch
        LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(
                LinkerConstants.EVENT_DIGITAL_SCREEN_LAUNCH, createScreenLaunchLinkerData(userId, categoryName, groupWiseCategoryId)
        ))
    }

    fun trackVisitRechargePushEventRecommendation(categoryId: Int) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParams(categoryId, ACTION_VISIT),
                getDefaultRechargePushEventRecommendationSubsriber())
    }

    fun trackVisitRechargePushEventRecommendation(categoryId: Int, subscriber: Subscriber<GraphqlResponse>) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParams(categoryId, ACTION_VISIT),
                subscriber)
    }

    fun trackAddToCartRechargePushEventRecommendation(categoryId: Int) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParams(categoryId, ACTION_ATC),
                getDefaultRechargePushEventRecommendationSubsriber())
    }

    fun trackAddToCartRechargePushEventRecommendation(categoryId: Int, subscriber: Subscriber<GraphqlResponse>) {
        rechargePushEventRecommendationUseCase.execute(rechargePushEventRecommendationUseCase.createRequestParams(categoryId, ACTION_ATC),
                subscriber)
    }

    private fun createScreenLaunchLinkerData(userId: String, categoryName: String, groupWiseCategoryId: List<String>): RechargeLinkerData {
        val rechargeLinkerData = RechargeLinkerData()
        rechargeLinkerData.linkerData = LinkerData().apply {
            productCategory = categoryName
            this.userId = userId
        }
        rechargeLinkerData.setCategoryIds(groupWiseCategoryId)
        return rechargeLinkerData
    }

    fun eventViewPdpPage(categoryName: String, userId: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(TrackAppUtils.EVENT, DigitalTrackingConst.Event.VIEW_DIGITAL_IRIS,
                        TrackAppUtils.EVENT_CATEGORY, DigitalTrackingConst.Category.DIGITAL_HOMEPAGE,
                        TrackAppUtils.EVENT_ACTION, DigitalTrackingConst.Action.VIEW_PDP_PAGE,
                        TrackAppUtils.EVENT_LABEL, categoryName,
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.USER_ID, userId,
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE
                )
        )
    }

    fun eventAddToCart(digitalAtcTrackingModel: DigitalAtcTrackingModel) {
        val productName: String = "${digitalAtcTrackingModel.operatorName.toLowerCase()} " +
                "${digitalAtcTrackingModel.priceText.toLowerCase()}"

        val products: MutableList<Any> = ArrayList()

        val eventCategory = when (digitalAtcTrackingModel.source) {
            DigitalCheckoutPassData.PARAM_WIDGET -> DigitalTrackingConst.Category.HOMEPAGE_DIGITAL_WIDGET
            else -> DigitalTrackingConst.Category.DIGITAL_NATIVE
        }

        val eventLabel = digitalAtcTrackingModel.categoryName.toLowerCase() + " - " +
                digitalAtcTrackingModel.operatorName

        products.add(constructProductEnhanceEcommerce(digitalAtcTrackingModel, productName))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(TrackAppUtils.EVENT, DigitalTrackingConst.Event.ADD_TO_CART,
                        TrackAppUtils.EVENT_CATEGORY, eventCategory,
                        TrackAppUtils.EVENT_ACTION, DigitalTrackingConst.Action.CLICK_BELI,
                        TrackAppUtils.EVENT_LABEL, eventLabel,
                        DigitalTrackingConst.Label.BUSINESS_UNIT, DigitalTrackingConst.Value.RECHARGE_BU,
                        DigitalTrackingConst.Label.USER_ID, digitalAtcTrackingModel.userId,
                        BaseTrackerConst.Ecommerce.KEY, DataLayer.mapOf(
                        DigitalTrackingConst.CurrencyCode.KEY, DigitalTrackingConst.CurrencyCode.IDR,
                        DigitalTrackingConst.Label.ADD,
                        DataLayer.mapOf(DigitalTrackingConst.Label.PRODUCTS, DataLayer.listOf(*products.toTypedArray()))),
                        DigitalTrackingConst.Label.CURRENTSITE, DigitalTrackingConst.Value.RECHARGE_SITE
                )
        )
    }

    private fun constructProductEnhanceEcommerce(digitalAtcTrackingModel: DigitalAtcTrackingModel,
                                                 productName: String)
            : Map<String?, Any?> {
        var productId = DigitalTrackingConst.Value.NONE
        if (digitalAtcTrackingModel.productId.isNotEmpty()) productId = digitalAtcTrackingModel.productId

        return DataLayer.mapOf(
                DigitalTrackingConst.Product.KEY_NAME, productName,
                DigitalTrackingConst.Product.KEY_ID, productId,
                DigitalTrackingConst.Product.KEY_PRICE, digitalAtcTrackingModel.pricePlain.toString(),
                DigitalTrackingConst.Product.KEY_BRAND, digitalAtcTrackingModel.operatorName.toLowerCase(),
                DigitalTrackingConst.Product.KEY_CATEGORY, digitalAtcTrackingModel.categoryName.toLowerCase(),
                DigitalTrackingConst.Product.KEY_VARIANT, DigitalTrackingConst.Value.NONE,
                DigitalTrackingConst.Product.KEY_QUANTITY, "1",
                DigitalTrackingConst.Product.KEY_CATEGORY_ID, digitalAtcTrackingModel.categoryId,
                DigitalTrackingConst.Product.KEY_CART_ID, digitalAtcTrackingModel.cartId,
                DigitalTrackingConst.Product.KEY_SHOP_ID, DigitalTrackingConst.Value.NONE,
                DigitalTrackingConst.Product.KEY_SHOP_NAME, DigitalTrackingConst.Value.NONE,
                DigitalTrackingConst.Product.KEY_SHOP_TYPE, DigitalTrackingConst.Value.NONE
        )
    }

    private fun getDefaultRechargePushEventRecommendationSubsriber(): Subscriber<GraphqlResponse> {
        return object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val response = graphqlResponse.getData<RechargePushEventRecommendationResponseEntity>(RechargePushEventRecommendationResponseEntity::class.java)
            }
        }
    }

    fun onClickSliceRecharge(userId: String, rechargeProductFromSlice: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT_KEY, "clickGAMain",
                EVENT_CATEGORY, "ga main app",
                EVENT_ACTION, "click item transaction",
                EVENT_LABEL, rechargeProductFromSlice,
                BUSINESS_UNIT, "recharge",
                CURRENT_SITE, "tokopediadigital",
                USER_ID, userId
        ))
    }

    fun onOpenPageFromSlice(page: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT_KEY, "openScreen",
                EVENT_SCREEN_NAME, "${page} - from voice search - mainapp"
        ))
    }

    companion object {
        const val ACTION_VISIT = "VISIT"
        const val ACTION_ATC = "ATC"

        const val DIGITAL_HOMEPAGE = "digital - homepage"

        const val EVENT_NAME = "eventName"
        const val OPEN_SCREEN_EVENT = "openScreen"
        const val USER_ID = "userId"
        const val IS_LOGIN_STATUS = "isLoggedInStatus"
        const val CATEGORY = "category"
        const val CATEGORY_ID = "digitalCategoryId"
        const val CATEGORY_ID_SCREEN_LAUNCH = "digital_category_id"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"

        const val RECHARGE_SCREEN_NAME = "/digital/"
        const val BUSINESS_UNIT_RECHARGE = "recharge"
        const val CURRENT_SITE_RECHARGE = "tokopediadigital"
        const val EVENT_DIGITAL_CATEGORY_SCREEN_LAUNCH = "Digital_Category_Screen_Launched"

        const val EVENT_KEY = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val EVENT_SCREEN_NAME = "screenName"
    }
}
