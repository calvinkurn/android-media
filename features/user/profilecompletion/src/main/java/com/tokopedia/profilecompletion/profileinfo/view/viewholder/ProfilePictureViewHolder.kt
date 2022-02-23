package com.tokopedia.profilecompletion.profileinfo.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.DividerProfileUiModel

class ProfilePictureViewHolder(val view: View, val listener: ProfilePictureInterface) : AbstractViewHolder<DividerProfileUiModel>(view) {

    override fun bind(element: DividerProfileUiModel?) {

    }

    companion object {
	@LayoutRes
	val LAYOUT_RES = R.layout.divider_profile_item
    }

    interface ProfilePictureInterface {
        fun onChangeProfilePictureClicked()
    }
}