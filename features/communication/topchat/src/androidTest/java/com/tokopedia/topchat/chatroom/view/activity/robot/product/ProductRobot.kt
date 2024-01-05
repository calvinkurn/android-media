package com.tokopedia.topchat.chatroom.view.activity.robot.product

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.matchers.withRecyclerView

object ProductRobot {

    fun clickBuyButtonAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.tv_buy, click())
        }
    }

    fun clickATCButtonAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.tv_atc, click())
        }
    }

    fun clickWishlistButtonAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.tv_wishlist, click())
        }
    }

    fun clickProductAttachmentAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.cl_info, click())
        }
    }

    fun clickChangeStockBtnAt(position: Int) {
        generalRobot {
            doActionOnListItemAt(position, R.id.btn_update_stock, click())
        }
    }

    fun clickChangeStockBtnOnCarouselAt(position: Int) {
        onView(
            withRecyclerView(R.id.rv_product)
                .atPositionOnView(position, R.id.btn_update_stock)
        ).perform(click())
    }

    fun clickChangeStockBtnOnCarouselBroadcastAt(position: Int) {
        onView(
            withRecyclerView(R.id.rv_product_carousel)
                .atPositionOnView(position, R.id.btn_update_stock)
        ).perform(click())
    }
}
