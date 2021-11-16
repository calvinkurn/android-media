package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.databinding.HolderHomeNavMenuBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.utils.view.binding.viewBinding

class HomeNavMenuViewHolder(
        itemView: View,
        private val listener: HomeNavListener
): AbstractViewHolder<HomeNavMenuDataModel>(itemView) {
    private var binding: HolderHomeNavMenuBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_home_nav_menu
    }

    override fun bind(element: HomeNavMenuDataModel) {
        binding?.menuTitle?.text = element.itemTitle
        binding?.menuTitle?.tag = element.id
        if (element.srcIconId != null) {
            binding?.menuImage?.visible()
            binding?.menuImageUnify?.gone()

            binding?.menuImage?.setImage(newIconId = element.srcIconId)
        } else {
            binding?.menuImage?.gone()
            binding?.menuImageUnify?.visible()

            binding?.menuImageUnify?.loadImage(element.srcImage, R.drawable.grey_button_rounded)
        }
        itemView.setOnClickListener {
            listener.onMenuClick(element)
        }

        if (element.notifCount.isNotEmpty()) {
            if(listener.getReviewCounterAbIsUnify()) {
                binding?.menuNotification?.setNotification(
                        notif = element.notifCount,
                        notificationType = NotificationUnify.COUNTER_TYPE,
                        colorType = NotificationUnify.COLOR_PRIMARY
                )
                binding?.menuNotification?.visibility = View.VISIBLE
            } else {
                binding?.menuNotificationTypography?.setNotification(element.notifCount, NotificationUnify.COUNTER_TYPE, NotificationUnify.COLOR_PRIMARY)
                binding?.menuNotificationTypography?.visibility = View.VISIBLE
            }
        } else {
            binding?.menuNotificationTypography?.visibility = View.GONE
            binding?.menuNotification?.visibility = View.GONE
        }
    }
}