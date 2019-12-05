package com.tokopedia.product.detail.data.util

import com.tokopedia.gallery.viewmodel.ImageReviewItem

internal typealias OnImageReviewClick = (List<ImageReviewItem>, Int) -> Unit
internal typealias OnSeeAllReviewClick = (() -> Unit)
internal typealias OnImageReviewClicked = ((List<String>, Int, String?) -> Unit)