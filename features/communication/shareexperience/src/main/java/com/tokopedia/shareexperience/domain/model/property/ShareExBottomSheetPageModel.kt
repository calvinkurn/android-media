package com.tokopedia.shareexperience.domain.model.property

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShareExBottomSheetPageModel(
    val listChip: List<String> = listOf(),
    val listShareProperty: List<ShareExPropertyModel> = listOf()
) : Parcelable
