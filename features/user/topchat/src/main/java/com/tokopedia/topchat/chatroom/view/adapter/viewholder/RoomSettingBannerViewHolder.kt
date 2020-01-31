package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBanner
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import kotlinx.android.synthetic.main.item_topchat_room_setting_banner.view.*

class RoomSettingBannerViewHolder(itemView: View?) : AbstractViewHolder<RoomSettingBanner>(itemView) {

    override fun bind(element: RoomSettingBanner?) {
        if (element == null) return
        bindBannerType(element)
        bindBannerText(element)
    }

    private fun bindBannerType(element: RoomSettingBanner) {
        val bannerType = when (element.typeString) {
            RoomSettingBanner.TYPE_INFO -> Ticker.TYPE_ANNOUNCEMENT
            RoomSettingBanner.TYPE_ERROR -> Ticker.TYPE_ERROR
            RoomSettingBanner.TYPE_WARNING -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
        itemView.tkBanner?.tickerType = bannerType
    }

    private fun bindBannerText(element: RoomSettingBanner) {
        itemView.tkBanner?.setHtmlDescription(element.text)
        itemView.tkBanner?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(itemView.context, linkUrl.toString())
            }

            override fun onDismiss() { }
        })

        // Workaround for ticker not wrapping multiline content correctly
        itemView.tkBanner?.post {
            itemView.tkBanner?.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            itemView.tkBanner?.requestLayout()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_room_setting_banner
    }
}