package com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemParameterProtectedSectionBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ProtectedParameterListener
import com.tokopedia.shop.score.performance.presentation.model.tablet.ProtectedParameterTabletUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProtectedParameterTabletViewHolder(
    view: View,
    private val protectedParameterListener: ProtectedParameterListener
) : AbstractViewHolder<ProtectedParameterTabletUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_parameter_protected_section
    }

    private val binding: ItemParameterProtectedSectionBinding? by viewBinding()

    override fun bind(element: ProtectedParameterTabletUiModel?) {
        binding?.run {
            parameterProtectedWidget.setData(element, protectedParameterListener) {
                setCardItemProtectedBackground()
            }
        }
    }

    private fun ItemParameterProtectedSectionBinding.setCardItemProtectedBackground() {
        root.context?.let {
            parameterProtectedWidget.binding?.root?.setBackgroundResource(
                R.drawable.corner_rounded_performance_list
            )
        }
    }
}
