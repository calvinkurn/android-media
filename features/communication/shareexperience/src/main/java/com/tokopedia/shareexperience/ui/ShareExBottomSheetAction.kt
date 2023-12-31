package com.tokopedia.shareexperience.ui

sealed interface ShareExBottomSheetAction {
    object InitializePage : ShareExBottomSheetAction
    data class UpdateShareBody(val position: Int) : ShareExBottomSheetAction
    data class UpdateShareImage(val imageUrl: String) : ShareExBottomSheetAction
}
