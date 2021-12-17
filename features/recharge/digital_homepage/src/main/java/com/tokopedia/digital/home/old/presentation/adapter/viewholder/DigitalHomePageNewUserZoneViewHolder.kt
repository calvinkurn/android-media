package com.tokopedia.digital.home.old.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeNewUserZoneBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageNewUserZoneModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.NEW_USER_BANNER_CLICK
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.NEW_USER_IMPRESSION
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DigitalHomePageNewUserZoneViewHolder(itemView: View?, val onItemBindListener: OnItemBindListener) :
        AbstractViewHolder<DigitalHomePageNewUserZoneModel>(itemView) {

    override fun bind(element: DigitalHomePageNewUserZoneModel) {
        val bind = LayoutDigitalHomeNewUserZoneBinding.bind(itemView)
        if (element.isLoaded) {
            if (element.isSuccess
                    && element.data != null
                    && element.data.section.items.isNotEmpty()) {
                with (element.data.section) {
                    bind.digitalHomepageNewUserZoneShimmering.hide()
                    bind.digitalHomepageNewUserZoneContainer.show()
                    ImageHandler.loadImageWithoutFit(itemView.context, bind.digitalHomepageNewUserZoneImage, items[0].mediaUrl)
                    onItemBindListener.onSectionItemImpression(items, NEW_USER_IMPRESSION)
                    itemView.setOnClickListener { onItemBindListener.onSectionItemClicked(items[0], adapterPosition, NEW_USER_BANNER_CLICK) }
                }
            } else {
                bind.digitalHomepageNewUserZoneShimmering.hide()
                bind.digitalHomepageNewUserZoneContainer.hide()
            }
        } else {
            bind.digitalHomepageNewUserZoneShimmering.show()
            bind.digitalHomepageNewUserZoneContainer.hide()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_digital_home_new_user_zone
    }
}