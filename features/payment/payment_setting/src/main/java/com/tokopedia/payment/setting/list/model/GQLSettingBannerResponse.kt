package com.tokopedia.payment.setting.list.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.payment.setting.list.view.adapter.SettingListPaymentAdapterTypeFactory

data class GQLSettingBannerResponse(
    @SerializedName("cc_cobrand_getccsettingbanner")
    @Expose
    var settingBannerResponse: SettingBannerModel = SettingBannerModel()
)

data class SettingBannerModel(
    @SerializedName("id")
    @Expose
    var id: Long = 0,

    @SerializedName("code")
    @Expose
    var code: String = "",

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("title_body_message")
    @Expose
    var titleBodyMessage: String = "",

    @SerializedName("assets")
    @Expose
    var assets: String = "",

    @SerializedName("button_name")
    @Expose
    var buttonName: String = "",

    @SerializedName("button_redirect_url")
    @Expose
    var buttonRedirectUrl: String = "",
) : Visitable<SettingListPaymentAdapterTypeFactory> {

    override fun type(typeFactory: SettingListPaymentAdapterTypeFactory?): Int {

        return typeFactory?.type(this)?:0
    }
}
