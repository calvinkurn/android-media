package com.tokopedia.shareexperience.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExImageGeneratorPropertyModel(
    val sourceId: String = "",
    val args: Map<String, String> = mapOf() // Convert to map to make sure no duplicate args
): Parcelable
