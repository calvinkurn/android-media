package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBannerUiModel
import com.tokopedia.topchat.databinding.ItemTopchatRoomSettingBannerBinding
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding

class RoomSettingBannerViewHolder(itemView: View?) : AbstractViewHolder<RoomSettingBannerUiModel>(itemView) {

    private val binding: ItemTopchatRoomSettingBannerBinding? by viewBinding()

    override fun bind(element: RoomSettingBannerUiModel?) {
        if (element == null) return
        bindBannerType(element)
        bindBannerText(element)
    }

    private fun bindBannerType(element: RoomSettingBannerUiModel) {
        val bannerType = when (element.typeString) {
            RoomSettingBannerUiModel.TYPE_INFO -> Ticker.TYPE_ANNOUNCEMENT
            RoomSettingBannerUiModel.TYPE_ERROR -> Ticker.TYPE_ERROR
            RoomSettingBannerUiModel.TYPE_WARNING -> Ticker.TYPE_WARNING
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
        binding?.tkrBanner?.tickerType = bannerType
    }

    private fun bindBannerText(element: RoomSettingBannerUiModel) {
        binding?.tkrBanner?.setHtmlDescription(element.text)
        binding?.tkrBanner?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(itemView.context, linkUrl.toString())
            }

            override fun onDismiss() { }
        })

        // Workaround for ticker not wrapping multiline content correctly
        binding?.tkrBanner?.post {
            binding?.tkrBanner?.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            binding?.tkrBanner?.requestLayout()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_room_setting_banner
    }
}
