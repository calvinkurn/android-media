package com.tokopedia.product_bundle.single.presentation.model

data class SingleProductBundleDialogModel (
    var title: String? = "",
    var message: String? = "",
    var type: DialogType = DialogType.DIALOG_NORMAL
) {
    enum class DialogType {
        DIALOG_NORMAL,
        DIALOG_REFRESH
    }
}