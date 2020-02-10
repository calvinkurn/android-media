package com.tokopedia.product.detail.imagepreview.data

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class ImagePreviewTracking {

    private val tracker = TrackApp.getInstance().gtm

    fun onSuccessAdd() {
        tracker.sendGeneralEvent(EVENT.CLICK_PDP, CATEGORY.PDP, ACTION.ADD_WISLIST_LOGIN, LABEL.EMPTY)
    }

    fun onSuccessRemove() {
        tracker.sendGeneralEvent(EVENT.CLICK_PDP, CATEGORY.PDP, ACTION.REMOVE_WISLIST_LOGIN, LABEL.EMPTY)
    }

    fun onAddWishlistNonLogin() {
        tracker.sendGeneralEvent(EVENT.CLICK_PDP, CATEGORY.PDP, ACTION.ADD_WISLIST_NON_LOGIN, LABEL.EMPTY)
    }

    fun onImageSwipe(productId: String, swipeDirection: String) {
        val mapData = TrackAppUtils.gtmData(
                EVENT.CLICK_PDP,
                CATEGORY.PDP,
                ACTION.SWIPE_PICTURE,
                "$swipeDirection - "+LABEL.OVERLAY
        )
        mapData[KEY.KEY_PRODUCT_ID] = productId
        tracker.sendGeneralEvent(mapData)
    }

    companion object {

        object KEY {
            const val KEY_PRODUCT_ID = "productId"
        }

        object EVENT {
            const val CLICK_PDP = "clickPDP"
        }

        object CATEGORY {
            const val PDP = "product detail page"
        }

        object ACTION {
            const val ADD_WISLIST_NON_LOGIN = "add wishlist - product image - nonlogin"
            const val ADD_WISLIST_LOGIN = "add wishlist - product image - login"
            const val REMOVE_WISLIST_LOGIN = "remove wishlist - product image - login"
            const val SWIPE_PICTURE = "click - swipe product picture"
        }

        object LABEL {
            const val EMPTY = ""
            const val OVERLAY = "overlay"
        }

    }
}