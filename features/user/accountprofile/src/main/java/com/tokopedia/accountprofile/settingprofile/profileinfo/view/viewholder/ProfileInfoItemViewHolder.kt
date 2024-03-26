package com.tokopedia.accountprofile.settingprofile.profileinfo.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.databinding.ProfileItemViewBinding
import com.tokopedia.accountprofile.settingprofile.profileinfo.data.ProfileInfoConstants
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.uimodel.ProfileInfoItemUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProfileInfoItemViewHolder(
    val view: View,
    private val listener: ProfileInfoItemInterface
) : AbstractViewHolder<ProfileInfoItemUiModel>(view) {

    var binding: ProfileItemViewBinding? by viewBinding()

    override fun bind(element: ProfileInfoItemUiModel?) {
        if (element?.isEnable == true) {
            binding?.containerClick?.setOnClickListener {
                element.action.invoke()
            }
        }
        binding?.fragmentProfileItemIcon?.setOnClickListener {
            listener.onRightIconClicked(element)
            if (element?.id != ProfileInfoConstants.USER_ID) {
                element?.action?.invoke()
            }
        }
        if (element?.showVerifiedTag == true) binding?.tvVerification?.visible()
        else binding?.tvVerification?.gone()
        binding?.fragmentProfileItemTitle?.text = element?.title
        binding?.fragmentProfileItemValue?.text =
            if (element?.itemValue.isNullOrBlank()) element?.placeholder else element?.itemValue
        if (element?.itemValue.isNullOrBlank()) {
            binding?.fragmentProfileItemValue?.setTextColor(
                MethodChecker.getColor(
                    view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        } else {
            binding?.fragmentProfileItemValue?.setTextColor(
                MethodChecker.getColor(
                    view.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN950
                )
            )
        }
        if (element?.rightIcon != -1) {
            binding?.fragmentProfileItemIcon?.setImage(element?.rightIcon)
        } else {
            binding?.fragmentProfileItemIcon?.hide()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.profile_item_view
    }

    interface ProfileInfoItemInterface {
        fun onRightIconClicked(item: ProfileInfoItemUiModel?)
    }
}
