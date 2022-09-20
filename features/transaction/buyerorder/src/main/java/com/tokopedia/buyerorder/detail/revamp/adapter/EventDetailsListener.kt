package com.tokopedia.buyerorder.detail.revamp.adapter

import android.widget.TextView
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails

/**
 * created by @bayazidnasir on 24/8/2022
 */

interface EventDetailsListener {
    fun setEventDetails(actionButton: ActionButton, item: Items)
    fun openQRFragment(actionButton: ActionButton, item: Items)
    fun setDetailTitle(title: String)
    fun setInsuranceDetail()
    fun setPassengerEvent(item: Items)
    fun setActionButtonEvent(actionButton: ActionButton, item: Items, orderDetails: OrderDetails)
    fun setDealsBanner(metadata: MetaDataInfo)
    fun askPermission(uri: String, isDownloadable: Boolean,downloadFileName: String)
    fun sendThankYouEvent(metadata: MetaDataInfo, categoryType: Int, orderDetails: OrderDetails)
    fun sendOpenScreenDeals(isOMP: Boolean)
    fun setActionButtonGql(tapAction: List<ActionButton>, position: Int, flag: Boolean, isCalledFromAdapter: Boolean)
    fun showRetryButtonToaster(message: String)
    fun onTapActionDeals(view: TextView?, actionButton: ActionButton, item: Items, count: Int, position: Int)
}