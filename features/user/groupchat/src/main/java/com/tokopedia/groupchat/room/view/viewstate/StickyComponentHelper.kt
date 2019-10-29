package com.tokopedia.groupchat.room.view.viewstate

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.groupchat.chatroom.domain.pojo.AttributeStickyComponentData
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.StickyComponentAdapter
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.common.analytics.GroupChatAnalytics
import com.tokopedia.groupchat.room.view.activity.PlayActivity
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentsViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 25/02/19.
 */
class StickyComponentHelper(
        var stickyComponentView: RecyclerView,
        var userSession: UserSessionInterface,
        var analytics: GroupChatAnalytics,
        viewModel: ChannelInfoViewModel?,
        var openLink: (String) -> Unit,
        var activity: FragmentActivity
) : PlayBaseHelper(viewModel) {

    private val stickyComponentAdapter = StickyComponentAdapter(eventClickStickyComponent(), eventViewStickyComponent(), eventGoToAtc())

    init {
        stickyComponentView.layoutManager = LinearLayoutManager(
                stickyComponentView.context,
                LinearLayoutManager.HORIZONTAL,
                false)
        stickyComponentView.adapter = stickyComponentAdapter
    }


    fun assignModel(list: StickyComponentsViewModel?) {
        hide()
        stickyComponentAdapter.clearList()
        list?.list?.run {
            if(this.isNotEmpty()) {
                stickyComponentAdapter.setList(this)
                show()
            }
        }
    }

    fun show() {
        stickyComponentView.show()
    }

    fun hide() {
        stickyComponentView.hide()
    }

    private fun eventViewStickyComponent(): (item: StickyComponentViewModel) -> Unit {
        return {item ->
            viewModel?.let {
                analytics.eventShowStickyComponent(item, it)
            }
        }
    }

    private fun eventClickStickyComponent(): (item: StickyComponentViewModel) -> Unit {
        return {item ->
            viewModel?.let {
                analytics.eventClickStickyComponent(item, it)
                val applink = RouteManager.routeWithAttribution(stickyComponentView.context, item.redirectUrl,
                        GroupChatAnalytics.generateTrackerAttribution(
                                GroupChatAnalytics.ATTRIBUTE_PROMINENT_BUTTON,
                                it.channelUrl,
                                it.title
                        ))
                openLink.invoke(applink)
            }
        }
    }

    private fun eventGoToAtc(): (item: AttributeStickyComponentData, productName: String, productPrice: String) -> Unit {
        return { item: AttributeStickyComponentData, productName: String, productPrice: String ->

            val productId = item.productId
            val quantity = item.minQuantity
            val atcAndBuyAction = "1"
            val needRefresh = true
            val shopName = item.shopName
            var intent = RouteManager.getIntent(stickyComponentView.context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
                putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, item.shopId)
                putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, productId)
                putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity)
                putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, item.productId)
                putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcAndBuyAction)
                putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, shopName)
                putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
                putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needRefresh)
            }

            activity.startActivityForResult(intent, 102)


            analytics.eventClickATC(productName, productId, productPrice, quantity, item.shopId, shopName)
        }
    }
}