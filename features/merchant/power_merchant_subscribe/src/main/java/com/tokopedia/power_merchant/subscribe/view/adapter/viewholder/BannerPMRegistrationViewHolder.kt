package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
import com.tokopedia.power_merchant.subscribe.databinding.ItemPmRegistrationBannerBinding
import com.tokopedia.power_merchant.subscribe.view.model.WidgetBannerPMRegistration
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

/**
 * Created by @ilhamsuaib on 19/04/22.
 */

class BannerPMRegistrationViewHolder(
    itemView: View
) : AbstractViewHolder<WidgetBannerPMRegistration>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_pm_registration_banner
    }

    private val binding: ItemPmRegistrationBannerBinding? by viewBinding()

    override fun bind(element: WidgetBannerPMRegistration) {
        loadImageResource()
    }

    private fun loadImageResource() {
        binding?.run {
            imgPmHeaderBackdrop.loadImage(Constant.Image.PM_BG_REGISTRATION_PM)
            imgPmHeaderImage.loadImage(Constant.Image.IMG_PM_REGISTRATION)
        }
    }
}