package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetErrorBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetLoadingBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetSuccessBinding
import com.tokopedia.sellerhomecommon.presentation.model.UnificationWidgetUiModel

/**
 * Created by @ilhamsuaib on 06/07/22.
 */

class UnificationViewHolder(
    itemView: View
) : AbstractViewHolder<UnificationWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_unification_widget
    }

    private val binding by lazy { ShcUnificationWidgetBinding.bind(itemView) }
    private val loadingStateBinding by lazy {
        val view = binding.stubShcUnificationLoading.inflate()
        ShcUnificationWidgetLoadingBinding.bind(view)
    }
    private val errorStateBinding by lazy {
        val view = binding.stubShcUnificationError.inflate()
        ShcUnificationWidgetErrorBinding.bind(view)
    }
    private val commonErrorStateBinding by lazy {
        errorStateBinding.containerShcUnificationError
    }
    private val successStateBinding by lazy {
        val view = binding.stubShcUnificationSuccess.inflate()
        ShcUnificationWidgetSuccessBinding.bind(view)
    }

    override fun bind(element: UnificationWidgetUiModel) {
        setTitle(element.title)
        observeState(element)
    }

    private fun setTitle(title: String) {
        binding.tvShcUnificationTitle.text = title.parseAsHtml()
    }

    private fun observeState(element: UnificationWidgetUiModel) {
        val data = element.data
        when {
            data == null || element.showLoadingState -> showLoadingState()
            data.error.isNotEmpty() -> {
                showErrorState(element)
            }
            else -> showSuccessState(element)
        }
    }

    private fun showLoadingState() {
        loadingStateBinding.containerShcRecommendationLoading.visible()

    }

    private fun showErrorState(element: UnificationWidgetUiModel) {
        errorStateBinding.containerShcUnificationError.visible()
    }

    private fun showSuccessState(element: UnificationWidgetUiModel) {
        successStateBinding.containerShcUnificationSuccess.visible()

    }
}