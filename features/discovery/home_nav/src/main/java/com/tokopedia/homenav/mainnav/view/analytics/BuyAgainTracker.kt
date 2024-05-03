package com.tokopedia.homenav.mainnav.view.analytics

import android.os.Bundle
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.asTrackingPageSource
import com.tokopedia.homenav.mainnav.view.interactor.listener.BuyAgainCallback
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainModel
import com.tokopedia.iris.Iris
import com.tokopedia.track.builder.util.BaseTrackerConst

object BuyAgainTracker : BaseTrackerConst() {

    private const val GLOBAL_MENU_ITEM = "/global_menu - %s"
    private const val COMPONENT_NAME = "buy_again_card"

    // @Link: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890 (row: 18)
    private const val IMPRESSION_ACTION = "product list impression"
    private const val IMPRESSION_TRACKER_ID = "49873"
    fun impression(
        irisInstance: Iris,
        userId: String,
        position: Int,
        models: List<BuyAgainModel>,
        pageDetail: BuyAgainCallback.PageDetail?,
    ) {
        val pageSource = pageDetail?.source?.asTrackingPageSource(pageDetail.path)
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.VIEW_ITEM_LIST)
            putString(Action.KEY, IMPRESSION_ACTION)
            putString(Category.KEY, MainNavTrackingConst.GLOBAL_MENU)
            putString(Label.KEY, COMPONENT_NAME)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(ItemList.KEY, GLOBAL_MENU_ITEM.format(COMPONENT_NAME))
            putString(TrackerId.KEY, IMPRESSION_TRACKER_ID)
            putString(UserId.KEY, userId)
            putParcelableArray(
                Items.KEY, models.mapIndexed { _, model ->
                    Bundle().apply {
                        putString("index", (position + 1).toString())
                        putString("dimension40", GLOBAL_MENU_ITEM.format(COMPONENT_NAME))
                        putString("dimension90", pageSource)
                        putString("item_brand", "")
                        putString("item_category", model.category)
                        putString("item_id", model.productId)
                        putString("item_name", model.productName)
                        putString("item_variant", model.variant)
                        putString("price", model.priceInt)
                    }
                }.toTypedArray()
            )
        }
        irisInstance.saveEvent(bundle)
        getTracker().sendEnhanceEcommerceEvent(Event.VIEW_ITEM_LIST, bundle)
    }

    // @Link: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890 (row: 19)
    private const val PRODUCT_CLICK_ACTION = "click product list"
    private const val PRODUCT_CLICK_TRACKER_ID = "49874"
    fun productClick(userId: String, position: Int, model: BuyAgainModel, pageDetail: BuyAgainCallback.PageDetail?) {
        val pageSource = pageDetail?.source?.asTrackingPageSource(pageDetail.path)

        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, PRODUCT_CLICK_ACTION)
            putString(Category.KEY, MainNavTrackingConst.GLOBAL_MENU)
            putString(Label.KEY, COMPONENT_NAME)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(ItemList.KEY, GLOBAL_MENU_ITEM.format(COMPONENT_NAME))
            putString(TrackerId.KEY, PRODUCT_CLICK_TRACKER_ID)
            putString(UserId.KEY, userId)
            putParcelableArrayList(
                Items.KEY, arrayListOf(
                    Bundle().apply {
                        putString("index", (position + 1).toString())
                        putString("dimension40", GLOBAL_MENU_ITEM.format(COMPONENT_NAME))
                        putString("dimension90", pageSource)
                        putString("item_brand", "")
                        putString("item_category", model.category)
                        putString("item_id", model.productId)
                        putString("item_name", model.productName)
                        putString("item_variant", model.variant)
                        putString("price", model.price)
                    }
                )
            )
        }

        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    // @Link: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890 (row: 20)
    private const val PRODUCT_ATC_CLICK_ACTION = "click add to cart on product card"
    private const val PRODUCT_ATC_CLICK_TRACKER_ID = "49875"
    fun productAtcClick(userId: String, position: Int, model: BuyAgainModel, pageDetail: BuyAgainCallback.PageDetail?) {
        val pageSource = pageDetail?.source?.asTrackingPageSource(pageDetail.path)

        val bundle = Bundle().apply {
            putString(Event.KEY, Event.PRODUCT_ADD_TO_CART)
            putString(Action.KEY, PRODUCT_ATC_CLICK_ACTION)
            putString(Category.KEY, MainNavTrackingConst.GLOBAL_MENU)
            putString(Label.KEY, COMPONENT_NAME)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(TrackerId.KEY, PRODUCT_ATC_CLICK_TRACKER_ID)
            putString(UserId.KEY, userId)
            putParcelableArrayList(
                Items.KEY, arrayListOf(
                    Bundle().apply {
                        putString("category_id", "")
                        putString("dimension40", GLOBAL_MENU_ITEM.format(COMPONENT_NAME))
                        putString("dimension45", model.cartId)
                        putString("dimension90", pageSource)
                        putString("item_brand", "")
                        putString("item_category", model.category)
                        putString("item_id", model.productId)
                        putString("item_name", model.productName)
                        putString("item_variant", model.variant)
                        putString("price", model.priceInt)
                        putString("quantity", "1")
                        putString("shop_id", model.shopId)
                        putString("shop_name", model.shopName)
                        putString("shop_type", model.shopType)
                    }
                )
            )
        }

        getTracker().sendEnhanceEcommerceEvent(Event.ADD_TO_CART, bundle)
    }
}

