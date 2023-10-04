package com.tokopedia.common.topupbills.analytics

import android.os.Bundle
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Action.CLICK_BAYAR_LAINNYA
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Action.IMPRESS_MULTI_BUTTON
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Event.SELECT_CONTENT
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Event.VIEW_ITEM
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Label.USER_ID
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Promotion.CREATIVE_NAME
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Promotion.CREATIVE_SLOT
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Promotion.ITEM_ID
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Promotion.ITEM_NAME
import com.tokopedia.common_digital.common.constant.DigitalTrackingConst.Promotion.PROMOTION
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class CommonMultiCheckoutAnalytics {

    fun onCloseMultiCheckoutCoachmark(categoryName: String, loyaltyStatus: String) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, DigitalTrackingConst.Action.CLOSE_COACHMARK)
            putString(
                TrackAppUtils.EVENT_LABEL,
                String.format(
                    "%s_%s",
                    categoryName,
                    loyaltyStatus
                )
            )
            putString(TrackAppUtils.EVENT, DigitalTrackingConst.Event.CLICK_DIGITAL)
            putString(TrackAppUtils.EVENT_CATEGORY, RechargeAnalytics.DIGITAL_HOMEPAGE)
            putString(
                DigitalTrackingConst.Other.KEY_TRACKER_ID,
                DigitalTrackingConst.Id.CLOSE_COACHMARK_ID
            )
            putString(
                DigitalTrackingConst.Label.BUSINESS_UNIT,
                DigitalTrackingConst.Value.RECHARGE_BU
            )
            putString(
                DigitalTrackingConst.Label.CURRENTSITE,
                DigitalTrackingConst.Value.RECHARGE_SITE
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DigitalTrackingConst.Event.CLICK_DIGITAL,
            eventDataLayer
        )
    }

    fun onClickMultiCheckout(
        categoryName: String, operatorName: String, channelId: String, userId: String,
        promotionMultiCheckout: PromotionMultiCheckout
    ) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, SELECT_CONTENT)
            putString(TrackAppUtils.EVENT_ACTION, CLICK_BAYAR_LAINNYA)
            putString(TrackAppUtils.EVENT_CATEGORY, RechargeAnalytics.DIGITAL_HOMEPAGE)
            putString(
                TrackAppUtils.EVENT_LABEL, String.format(
                    "%s - %s - %s",
                    categoryName,
                    operatorName,
                    channelId
                )
            )
            putString(
                DigitalTrackingConst.Other.KEY_TRACKER_ID,
                DigitalTrackingConst.Id.CLICK_MULTICHECKOUT_BUTTON
            )
            putString(
                DigitalTrackingConst.Label.BUSINESS_UNIT,
                DigitalTrackingConst.Value.RECHARGE_BU
            )
            putString(
                DigitalTrackingConst.Label.CURRENTSITE,
                DigitalTrackingConst.Value.RECHARGE_SITE
            )
            putString(USER_ID, userId)
            putParcelableArrayList(
                PROMOTION,
                mapperPromotionMultiCheckout(promotionMultiCheckout)
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun onImpressMultiCheckoutButtons(categoryName: String, buttonType: Int, userId: String) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, VIEW_ITEM)
            putString(TrackAppUtils.EVENT_ACTION, IMPRESS_MULTI_BUTTON)
            putString(TrackAppUtils.EVENT_CATEGORY, RechargeAnalytics.DIGITAL_HOMEPAGE)
            putString(
                TrackAppUtils.EVENT_LABEL, String.format(
                    "%s_%s",
                    categoryName,
                    buttonType.toString(),
                )
            )
            putString(
                DigitalTrackingConst.Other.KEY_TRACKER_ID,
                DigitalTrackingConst.Id.IMPRESS_MULTI_CHECKOUT
            )
            putString(
                DigitalTrackingConst.Label.BUSINESS_UNIT,
                DigitalTrackingConst.Value.RECHARGE_BU
            )
            putString(
                DigitalTrackingConst.Label.CURRENTSITE,
                DigitalTrackingConst.Value.RECHARGE_SITE
            )
            putString(USER_ID, userId)
            putParcelableArrayList(
                PROMOTION,
                mapperPromotionMultiCheckoutEmpty()
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
    }

    private fun mapperPromotionMultiCheckout(promotionMultiCheckout: PromotionMultiCheckout): ArrayList<Bundle> {
        val listItems = ArrayList<Bundle>()
        promotionMultiCheckout.run {
            listItems.add(
                Bundle().apply {
                    putString(ITEM_ID, "0")
                    putString(ITEM_NAME, promotionMultiCheckout.itemName)
                    putString(CREATIVE_NAME, promotionMultiCheckout.itemName)
                    putString(CREATIVE_SLOT, promotionMultiCheckout.position.toString())
                }
            )
        }

        return listItems
    }

    private fun mapperPromotionMultiCheckoutEmpty(): ArrayList<Bundle> {
        val listItems = ArrayList<Bundle>()
        listItems.add(
            Bundle().apply {
                putString(ITEM_ID, "0")
                putString(ITEM_NAME, "")
                putString(CREATIVE_NAME, "")
                putString(CREATIVE_SLOT, "")
            }
        )
        return listItems
    }
}
