package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePageNewUserZoneModel
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.NEW_USER_BANNER_CLICK
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.NEW_USER_IMPRESSION
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.layout_digital_home_new_user_zone.view.*

class DigitalHomePageNewUserZoneViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageNewUserZoneModel>(itemView) {

    override fun bind(element: DigitalHomePageNewUserZoneModel) {
        if (element.isLoaded) {
            if (element.isSuccess) {
                element.data?.section?.run {
                    itemView.digital_homepage_new_user_zone_shimmering.hide()
                    itemView.digital_homepage_new_user_zone_container.show()
                    ImageHandler.loadImageWithoutFit(itemView.context, itemView.digital_homepage_new_user_zone_image, items[0].mediaUrl)
                    onItemBindListener.onSectionItemImpression(items, NEW_USER_IMPRESSION, true)
                    itemView.setOnClickListener { onItemBindListener.onSectionItemClicked(items[0], adapterPosition, NEW_USER_BANNER_CLICK) }
                }
            } else {
                itemView.digital_homepage_new_user_zone_shimmering.hide()
                itemView.digital_homepage_new_user_zone_container.hide()
            }
        } else {
            itemView.digital_homepage_new_user_zone_shimmering.show()
            itemView.digital_homepage_new_user_zone_container.hide()
            onItemBindListener.onNewUserZoneItemDigitalBind(element.isLoadFromCloud)
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_new_user_zone
    }
}