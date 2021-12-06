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
        private const val ID_CATEGORY = 26
        private const val ID_TOP_UP_AND_BILL = 47
        private const val ID_TRAVEL_AND_ENTERTAINMENT = 50
        private const val ID_FINANCE = 49
        private const val ID_HALAL_CORNER = 48
        private const val ID_OTHER_SERVICES = 51
    }

    /*
        This function used for hardcode all category icon to using unify icon
     */
    private fun setAllCategoryIconToUnify() {

    }

    override fun bind(element: HomeNavMenuDataModel) {
        binding?.menuTitle?.text = element.itemTitle
        binding?.menuTitle?.tag = element.id
//        setAllCategoryIcon()
        if (element.srcIconId != null) {
            binding?.menuImage?.visible()
            binding?.menuImageUnify?.gone()

            binding?.menuImage?.setImage(newIconId = element.srcIconId)
        } else {
            binding?.menuImage?.gone()
            binding?.menuImageUnify?.visible()

            binding?.menuImageUnify?.loadImage(element.srcImage, com.tokopedia.homenav.R.drawable.grey_button_rounded)

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