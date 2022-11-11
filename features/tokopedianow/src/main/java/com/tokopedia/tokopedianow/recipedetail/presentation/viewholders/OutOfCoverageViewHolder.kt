package com.tokopedia.tokopedianow.recipedetail.presentation.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantUrl.LEARN_MORE_TOKOPEDIA_NOW_URL
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecipeOutOfCoverageBinding
import com.tokopedia.tokopedianow.recipedetail.constant.RecipeImageUrl
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.OutOfCoverageUiModel
import com.tokopedia.utils.view.binding.viewBinding

class OutOfCoverageViewHolder(
    itemView: View,
    private val listener: OutOfCoverageListener?
) : AbstractViewHolder<OutOfCoverageUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_tokopedianow_recipe_out_of_coverage
    }

    private var binding: ItemTokopedianowRecipeOutOfCoverageBinding? by viewBinding()

    override fun bind(item: OutOfCoverageUiModel) {
        addImpressionListener(item)
        renderImageIllustration()
        renderChangeAddressBtn()
        renderTextLearnMore()
    }

    private fun renderImageIllustration() {
        binding?.imageIllustration?.loadImage(RecipeImageUrl.ILLUSTRATION_OUT_OF_COVERAGE)
    }

    private fun renderChangeAddressBtn() {
        binding?.btnChangeAddress?.setOnClickListener {
            listener?.onClickChangeAddress()
        }
    }

    private fun renderTextLearnMore() {
        val text = itemView.context.getString(R.string.tokopedianow_recipe_learn_more)
        binding?.textLearnMore?.text = MethodChecker.fromHtml(text)
        binding?.textLearnMore?.setOnClickListener {
            RouteManager.route(itemView.context, ApplinkConst.WEBVIEW, LEARN_MORE_TOKOPEDIA_NOW_URL)
            listener?.onClickLearnMore()
        }
    }

    private fun addImpressionListener(item: OutOfCoverageUiModel) {
        itemView.addOnImpressionListener(item) {
            listener?.onImpressOutOfCoverage()
        }
    }

    interface OutOfCoverageListener {
        fun onClickChangeAddress()
        fun onClickLearnMore()
        fun onImpressOutOfCoverage()
    }
}