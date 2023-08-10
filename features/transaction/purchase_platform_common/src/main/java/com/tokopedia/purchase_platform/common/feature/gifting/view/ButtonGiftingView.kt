package com.tokopedia.purchase_platform.common.feature.gifting.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.databinding.ItemGiftingViewBinding
import com.tokopedia.unifycomponents.BaseCustomView

class ButtonGiftingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: ItemGiftingViewBinding? =
        ItemGiftingViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun showActive(title: String, desc: String) {
        binding?.run {
            titleInactiveAddon.gone()
            descInactiveAddon.gone()
            titleAddon.visible()
            titleAddon.text = title
            titleAddon.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))

            if (desc.isEmpty()) {
                descAddon.gone()
            } else {
                descAddon.visible()
                descAddon.text = desc
                titleAddon.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
            }
        }
    }

    fun showEmptyState(title: String, desc: String) {
        binding?.run {
            titleAddon.gone()
            descAddon.gone()
            titleInactiveAddon.visible()
            titleInactiveAddon.text = title

            if (desc.isEmpty()) {
                descInactiveAddon.gone()
            } else {
                descInactiveAddon.visible()
                descInactiveAddon.text = desc
            }
        }
    }

    fun showInactive(title: String, desc: String) {
        binding?.run {
            titleInactiveAddon.gone()
            descInactiveAddon.gone()
            titleAddon.visible()
            titleAddon.text = title
            titleAddon.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))

            if (desc.isEmpty()) {
                descAddon.gone()
            } else {
                descAddon.visible()
                descAddon.text = desc
                descAddon.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
            }
        }
    }
}
