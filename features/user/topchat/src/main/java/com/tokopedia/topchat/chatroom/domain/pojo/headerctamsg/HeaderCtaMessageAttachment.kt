package com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg


import com.google.gson.annotations.SerializedName
import com.tokopedia.attachcommon.preview.ProductPreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatProductAttachmentPreviewUiModel

data class HeaderCtaMessageAttachment(
    @SerializedName("body")
    var body: String = "",
    @SerializedName("extras")
    var extras: Extras = Extras(),
    @SerializedName("header")
    var header: String = "",
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("text_url")
    var textUrl: String = "",
    @SerializedName("type")
    var type: Int = 0,
    @SerializedName("url")
    var url: String = ""
) {

    val productName: String get() = extras.extrasProduct.getOrNull(0)?.product_name
        ?: ""

    fun generateSendableProductPreview(): List<SendablePreview> {
        return extras.extrasProduct.map { extraProduct ->
            TopchatProductAttachmentPreviewUiModel.Builder().apply {
                withProductUrl(extraProduct.url)
                withProductId(extraProduct.productId)
            }.build()
        }
    }

    fun hasVisibleCta(): Boolean {
        return type != TYPE_NO_BUTTON
    }

    fun isClickable(): Boolean {
        return status == STATUS_ENABLED
    }

    companion object {
        const val TYPE_NO_BUTTON = 0

        const val STATUS_ENABLED = 1
        const val STATUS_DISABLED = 0
    }
}