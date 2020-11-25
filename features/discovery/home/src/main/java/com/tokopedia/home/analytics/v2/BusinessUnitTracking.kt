package com.tokopedia.home.analytics.v2

import android.annotation.SuppressLint
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel

@SuppressLint("VisibleForTests")
object BusinessUnitTracking : BaseTracking(){
    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
    }

    private object CustomAction{
        const val BU_VIEW = "impression on bu widget"
        const val BU_CLICK = "click on bu widget"
        const val TAB_BU_CLICK = "click on bu widget tab"
    }

    fun getPageSelected(header: String) = DataLayer.mapOf(
            Event.KEY, CustomEvent.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.TAB_BU_CLICK,
            Label.KEY, header
    )

    fun getBusinessUnitView(promotion: Promotion): MutableMap<String, Any> = DataLayer.mapOf(
            Event.KEY, Event.PROMO_VIEW,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.BU_VIEW,
            Label.KEY, Label.NONE,
            Ecommerce.KEY, Ecommerce.getEcommercePromoView(listOf(promotion))
    )

    fun getBusinessUnitClick(promotion: Promotion): MutableMap<String, Any> = DataLayer.mapOf(
            Event.KEY, Event.PROMO_CLICK,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.BU_CLICK,
            Label.KEY, promotion.creative,
            Ecommerce.KEY, Ecommerce.getEcommercePromoClick(listOf(promotion))
    )

    fun mapToPromotionTracker(model: BusinessUnitItemDataModel, positionWidget: Int) = Promotion(
            id = model.content.contentId.toString(),
            creative = model.content.contentName,
            name = Ecommerce.PROMOTION_NAME.format(positionWidget, "bu widget - tab ${(model.tabPosition+1)}", model.tabName),
            position = (model.itemPosition+1).toString(),
            promoCodes = Label.NONE,
            promoIds = Label.NONE)
}