package com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemParameterProtectedSectionTabletBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ProtectedParameterListener
import com.tokopedia.shop.score.performance.presentation.model.tablet.ProtectedParameterTabletUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProtectedParameterTabletViewHolder(
    view: View,
    private val protectedParameterListener: ProtectedParameterListener
) : AbstractViewHolder<ProtectedParameterTabletUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_parameter_protected_section_tablet
    }

    private val binding: ItemParameterProtectedSectionTabletBinding? by viewBinding()

    override fun bind(element: ProtectedParameterTabletUiModel?) {
        binding?.run {
            parameterProtectedWidgetTablet.setData(element, protectedParameterListener) {
                setCardItemProtectedBackground()
            }
        }
    }

    private fun ItemParameterProtectedSectionTabletBinding.setCardItemProtectedBackground() {
        root.context?.let {
            parameterProtectedWidgetTablet.binding?.root?.setBackgroundResource(
                R.drawable.corner_rounded_performance_list
            )
        }
    }
}