package com.tokopedia.accountprofile.settingprofile.profileinfo.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.databinding.ProfileItemTitleViewBinding
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.uimodel.ProfileInfoTitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProfileInfoTitleViewHolder(
    val view: View,
    private val listener: ProfileInfoTitleInterface
) : AbstractViewHolder<ProfileInfoTitleUiModel>(view) {

    private var binding: ProfileItemTitleViewBinding? by viewBinding()

    override fun bind(element: ProfileInfoTitleUiModel?) {
        binding?.profileItemTitleIcon?.setOnClickListener { listener.onSectionIconClicked(element?.id) }
        binding?.profileItemTitle?.text = element?.title
        binding?.profileItemTitleIcon?.setImage(element?.infoIcon)
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.profile_item_title_view
    }

    interface ProfileInfoTitleInterface {
        fun onSectionIconClicked(id: String?)
    }
}
