package com.tokopedia.payment.setting.list.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.media.loader.loadImage
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.list.model.SettingBannerModel
import com.tokopedia.payment.setting.list.model.SettingListAddCardModel
import kotlinx.android.synthetic.main.item_setting_banner_view_holder.view.*

class SettingBannerViewHolder(itemView : View?)
    : AbstractViewHolder<SettingBannerModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_setting_banner_view_holder
    }

    override fun bind(element: SettingBannerModel) {
        itemView.settingBanner.loadImage(element.assets)
    }

}
