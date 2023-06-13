package com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model

data class ImageUploadDataModel(
    var showImageUpload: Boolean = false,
    var text: String = "",
    var leftIconUrl: String = "",
    var checkoutId: String = "",
    var frontEndValidation: Boolean = false
)
