package com.tokopedia.product.detail.data.util

import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel

internal typealias OnImageReviewClick = (List<ImageReviewItem>, Int, ComponentTrackDataModel?) -> Unit
internal typealias OnSeeAllReviewClick = ((ComponentTrackDataModel?) -> Unit)
internal typealias OnImageReviewClicked = ((List<String>, Int, String?, ComponentTrackDataModel?) -> Unit)
internal typealias OnErrorLog = ((Throwable) -> Unit)