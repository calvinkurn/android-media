package com.tokopedia.settingnotif.usersetting.data.pojo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory

data class SellerSection(
        var title: Int = 0,
        var resourceIcon: Int = 0
) : BaseSetting(), Visitable<SettingFieldTypeFactory> {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun createSellerItem(): SellerSection {
            return SellerSection().apply {
                this.title = R.string.settingnotif_settings_seller_title
                this.resourceIcon = R.drawable.ic_notifsetting_seller
            }
        }
    }

}