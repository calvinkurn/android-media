package com.tokopedia.similarsearch.viewmodel.testinstance

import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.testutils.jsonToObject

internal fun getSimilarProductModelCommon() =
    "common.json".jsonToObject<SimilarProductModel>()

internal fun getSimilarProductModelEmptyResult() =
    "empty-result.json".jsonToObject<SimilarProductModel>()

internal fun getSimilarProductModelOnePage() =
    "one-page.json".jsonToObject<SimilarProductModel>()

internal fun getSimilarProductModelTwoPage() =
    "two-page.json".jsonToObject<SimilarProductModel>()

internal fun getSimilarProductModelThreePage() =
    "three-page.json".jsonToObject<SimilarProductModel>()

internal fun getSimilarProductModelOriginalProductWishlisted() =
    "original-product-wishlisted.json".jsonToObject<SimilarProductModel>()