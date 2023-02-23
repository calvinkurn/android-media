package com.tokopedia.payment.setting.list.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
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
) : SettingListPaymentModel() {

    companion object {
        const val CODE_REGISTER_COBRAND = "register_cobrand"
        const val CODE_ON_PROGRESS = "on_progress"
        const val CODE_ACTIVATION = "activation"
        const val CODE_SAVE_CARD = "save_card"
    }

    override fun type(typeFactory: SettingListPaymentAdapterTypeFactory?): Int {

        return typeFactory?.type(this) ?: 0
    }
}
