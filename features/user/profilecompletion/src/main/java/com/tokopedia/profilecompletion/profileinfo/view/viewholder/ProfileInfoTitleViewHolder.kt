package com.tokopedia.profilecompletion.profileinfo.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.databinding.ProfileItemTitleViewBinding
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoTitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProfileInfoTitleViewHolder(val view: View,
				 private val listener: ProfileInfoTitleInterface
): AbstractViewHolder<ProfileInfoTitleUiModel>(view) {

    private var binding: ProfileItemTitleViewBinding? by viewBinding()

    override fun bind(element: ProfileInfoTitleUiModel?) {
	binding?.profileItemTitleIcon?.setOnClickListener { listener.onIconClicked(element?.id) }
	binding?.profileItemTitle?.text = element?.title
	binding?.profileItemTitleIcon?.setImage(element?.infoIcon)
    }

    companion object {
	@LayoutRes
	val LAYOUT_RES = R.layout.profile_item_title_view
    }

    interface ProfileInfoTitleInterface {
	fun onIconClicked(id: String?)
    }
}