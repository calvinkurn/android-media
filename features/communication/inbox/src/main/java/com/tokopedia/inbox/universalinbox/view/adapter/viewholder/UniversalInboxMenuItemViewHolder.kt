package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxMenuItemBinding
import com.tokopedia.inbox.universalinbox.util.UniversalInboxViewUtil
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxMenuListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxMenuItemViewHolder(
    itemView: View,
    private val listener: UniversalInboxMenuListener
) : AbstractViewHolder<UniversalInboxMenuUiModel>(itemView) {

    private val binding: UniversalInboxMenuItemBinding? by viewBinding()

    override fun bind(uiModel: UniversalInboxMenuUiModel) {
        bindMenu(uiModel)
        bindShopInfo(uiModel)
        bindCounter(uiModel)
        bindListener(uiModel)
    }

    private fun bindMenu(uiModel: UniversalInboxMenuUiModel) {
        binding?.inboxIconMenu?.setImage(newIconId = uiModel.icon)
        binding?.inboxTvMenu?.text = uiModel.title
    }

    private fun bindCounter(uiModel: UniversalInboxMenuUiModel) {
        val strCounter = UniversalInboxViewUtil.getStringCounter(uiModel.counter)
        binding?.inboxNotificationIcon?.showWithCondition(strCounter.isNotEmpty())
        binding?.inboxNotificationIcon?.setNotification(
            notif = strCounter,
            notificationType = NotificationUnify.COUNTER_TYPE,
            colorType = NotificationUnify.COLOR_PRIMARY
        )
    }

    private fun bindShopInfo(uiModel: UniversalInboxMenuUiModel) {
        val shopInfo = uiModel.getShopInfo()?.also {
            binding?.inboxTvShopName?.text = it.shopName
            binding?.inboxImgShopAvatar?.loadImage(it.avatar)
        }
        binding?.inboxLayoutShopInfo?.showWithCondition(shopInfo != null)
    }

    private fun bindListener(uiModel: UniversalInboxMenuUiModel) {
        binding?.inboxLayoutMenu?.setOnClickListener {
            listener.onMenuClicked(uiModel)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_menu_item
    }
}
