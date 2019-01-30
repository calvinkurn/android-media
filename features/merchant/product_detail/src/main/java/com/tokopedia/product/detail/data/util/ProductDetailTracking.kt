package com.tokopedia.product.detail.data.util

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker

class ProductDetailTracking(private val analyticTracker: AnalyticTracker?){

    fun eventTalkClicked(){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.ProductTalk.EVENT_LABEL)
    }

    fun eventReviewClicked(){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.ProductReview.EVENT_LABEL)
    }

    fun eventReportLogin(){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.Report.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Report.EVENT_LABEL)
    }

    fun eventReportNoLogin(){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.Report.EVENT,
                ProductTrackingConstant.Category.PDP,
                ProductTrackingConstant.Action.CLICK,
                ProductTrackingConstant.Report.NOT_LOGIN_EVENT_LABEL)
    }

    fun eventCartMenuClicked(variant: String){
        analyticTracker?.sendEventTracking(ProductTrackingConstant.PDP.EVENT,
                ProductTrackingConstant.Category.PDP.toLowerCase(),
                ProductTrackingConstant.Action.CLICK_CART_BUTTON_VARIANT,
                variant)
    }

}
