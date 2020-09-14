package com.tokopedia.product.addedit.variant.presentation.model

data class VariantPhoto(
        var variantUnitValueName: String,
        var imageUrlOrPath: String,
        var picID: String = "",
        var description: String = "",
        var fileName: String = "",
        var filePath: String = "",
        var width: Long = 0,
        var height: Long = 0,
        var isFromIG: String = "",
        var uploadId: String = ""
)