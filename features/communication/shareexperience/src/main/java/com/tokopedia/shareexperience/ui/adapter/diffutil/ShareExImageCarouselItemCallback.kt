package com.tokopedia.shareexperience.ui.adapter.diffutil

import android.annotation.SuppressLint
import com.tokopedia.shareexperience.ui.model.image.ShareExImageUiModel

class ShareExImageCarouselItemCallback: ShareExBaseItemCallback<ShareExImageUiModel>() {

    override fun areItemsTheSame(
        oldItem: ShareExImageUiModel,
        newItem: ShareExImageUiModel
    ): Boolean {
        return oldItem.imageUrl == newItem.imageUrl &&
            oldItem.isSelected == newItem.isSelected
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ShareExImageUiModel,
        newItem: ShareExImageUiModel
    ): Boolean {
        return oldItem.imageUrl == newItem.imageUrl &&
            oldItem.isSelected == newItem.isSelected
    }
}
