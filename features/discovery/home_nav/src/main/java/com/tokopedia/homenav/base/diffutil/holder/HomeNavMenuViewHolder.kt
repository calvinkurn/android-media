package com.tokopedia.homenav.base.diffutil.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.databinding.HolderHomeNavMenuBinding
import com.tokopedia.iconunify.IconUnify
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
        This function used for hardcode all category icon to using unify icon by element id
    */
    private fun setAllCategoryIconToUnify(element: HomeNavMenuDataModel) {
        when(element.id) {
            ID_CATEGORY -> element.srcIconId = IconUnify.DISCOVERY_BELANJA
            ID_TOP_UP_AND_BILL -> element.srcIconId = IconUnify.DISCOVERY_TOPUP_TAGIHAN
            ID_TRAVEL_AND_ENTERTAINMENT -> element.srcIconId = IconUnify.DISCOVERY_TRAVEL_ENTERTAINMENT
            ID_FINANCE -> element.srcIconId = IconUnify.DISCOVERY_KEUANGAN
            ID_HALAL_CORNER -> element.srcIconId = IconUnify.DISCOVERY_HALAL_CORNER
            ID_OTHER_SERVICES -> element.srcIconId = IconUnify.DISCOVERY_LAINNYA
        }
    }

    private fun setImageByIconUnify(element: HomeNavMenuDataModel) {
        binding?.menuImage?.visible()
        binding?.menuImageUnify?.gone()

        binding?.menuImage?.setImage(newIconId = element.srcIconId)
    }

    private fun setImageByImageSource(element: HomeNavMenuDataModel) {
        binding?.menuImage?.gone()
        binding?.menuImageUnify?.visible()

        binding?.menuImageUnify?.loadImage(element.srcImage, com.tokopedia.homenav.R.drawable.grey_button_rounded)
    }

    private fun setImageByMappingToIconUnify(element: HomeNavMenuDataModel) {
        setAllCategoryIconToUnify(element)
        if (element.srcIconId != null) {
            setImageByIconUnify(element)
        } else {
            setImageByImageSource(element)
        }
    }

    override fun bind(element: HomeNavMenuDataModel) {
        binding?.menuTitle?.text = element.itemTitle
        binding?.menuTitle?.tag = element.id

        if (element.srcIconId != null) {
            setImageByIconUnify(element)
        } else {
            setImageByMappingToIconUnify(element)
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