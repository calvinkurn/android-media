package com.tokopedia.universal_sharing.view.model

import com.tokopedia.universal_sharing.model.ImageGeneratorParamModel

data class ShareWidgetParam(
    val linkProperties: LinkShareWidgetProperties,
    val affiliateInput: AffiliateInput?,
    val imageGenerator: ImageGeneratorShareWidgetParam?
)

data class ImageGeneratorShareWidgetParam(
    val sourceId: String,
    val imageGeneratorParamModel: ImageGeneratorParamModel
)
