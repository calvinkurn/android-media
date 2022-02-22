package com.tokopedia.profilecompletion.profileinfo.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.databinding.ProfileItemViewBinding
import com.tokopedia.profilecompletion.profileinfo.view.uimodel.ProfileInfoItemUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProfileInfoItemViewHolder(val view: View,
			      private val listener: ProfileInfoItemInterface) : AbstractViewHolder<ProfileInfoItemUiModel>(view) {

    private var binding: ProfileItemViewBinding? by viewBinding()

    override fun bind(element: ProfileInfoItemUiModel?) {
	if(element?.isEnable == true) {
	    binding?.root?.setOnClickListener {
		element.action.invoke()
		listener.onItemClicked(element)
	    }
	}
	binding?.fragmentProfileItemTitle?.text = element?.title
	binding?.fragmentProfileItemValue?.text = element?.itemValue
	binding?.fragmentProfileItemIcon?.setImage(element?.rightIcon)
    }

    companion object {
	@LayoutRes
	val LAYOUT_RES = R.layout.profile_item_view
    }

    interface ProfileInfoItemInterface {
	fun onItemClicked(item: ProfileInfoItemUiModel?)
    }
}