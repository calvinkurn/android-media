package com.tokopedia.people.views.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel

/**
 * Created By : Jonathan Darwin on July 13, 2023
 */
class UserProfilePagerDiffUtil(
    private val oldData: List<ProfileTabUiModel.Tab>,
    private val newData: List<ProfileTabUiModel.Tab>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].key == newData[newItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }
}
