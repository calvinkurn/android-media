package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.databinding.ItemSettingBannerViewHolderBinding
import com.tokopedia.payment.setting.list.model.SettingBannerModel
import com.tokopedia.payment.setting.list.view.listener.SettingListActionListener

class SettingBannerViewHolder(
    itemView: View,
    private val listener: SettingListActionListener
) : AbstractViewHolder<SettingBannerModel>(itemView) {

    private val binding = ItemSettingBannerViewHolderBinding.bind(itemView)

    companion object {
        val LAYOUT = R.layout.item_setting_banner_view_holder
    }

    override fun bind(element: SettingBannerModel) {
        binding.settingBanner.loadImage(element.assets)
        listener.onViewBanner(element)
    }
}
