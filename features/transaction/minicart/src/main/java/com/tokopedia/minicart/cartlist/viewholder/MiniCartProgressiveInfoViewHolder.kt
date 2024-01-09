package com.tokopedia.minicart.cartlist.viewholder

import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartProgressiveInfoListener
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProgressiveInfoUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartProgressiveInfoBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class MiniCartProgressiveInfoViewHolder (
    private val viewBinding: ItemMiniCartProgressiveInfoBinding,
    private val listener: MiniCartProgressiveInfoListener? = null
) : AbstractViewHolder<MiniCartProgressiveInfoUiModel>(viewBinding.root) {
    companion object {
        val LAYOUT = R.layout.item_mini_cart_progressive_info
    }

    override fun bind(element: MiniCartProgressiveInfoUiModel) {
        viewBinding.apply {
            when(element.state) {
                MiniCartProgressiveInfoUiModel.State.LOADED -> showNormalState(element)
                MiniCartProgressiveInfoUiModel.State.LOADING -> showLoadingState()
                MiniCartProgressiveInfoUiModel.State.ERROR -> showRefreshState(element.offerId)
            }
        }
    }

    private fun ItemMiniCartProgressiveInfoBinding.showLoadingState() {
        root.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_bmgm_mini_cart_progressive_info_loading)
        shimmeringIcon.show()
        shimmeringProgressiveInfo.show()
        icuChevron.hide()
        iuIcon.hide()
        tpProgressiveInfo.hide()
    }

    private fun ItemMiniCartProgressiveInfoBinding.showRefreshState(offerId: Long) {
        root.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_bmgm_mini_cart_progressive_info_refresh)
        iuIcon.hide()
        shimmeringIcon.hide()
        shimmeringProgressiveInfo.hide()
        tpProgressiveInfo.show()
        tpProgressiveInfo.text = getString(R.string.mini_cart_progressive_info_failed_text)
        tpProgressiveInfo.setTextColor(ContextCompat.getColor(itemView.context, unifyprinciplesR.color.Unify_NN950))
        icuChevron.setImage(IconUnify.RELOAD)
        icuChevron.setOnClickListener {
            listener?.onRefreshClicked(offerId)
        }
    }

    private fun ItemMiniCartProgressiveInfoBinding.showNormalState(
        element: MiniCartProgressiveInfoUiModel
    ) {
        root.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_bmgm_mini_cart_progressive_info)
        iuIcon.show()
        shimmeringIcon.hide()
        shimmeringProgressiveInfo.hide()
        iuIcon.loadImage(element.icon)
        tpProgressiveInfo.show()
        tpProgressiveInfo.text = MethodChecker.fromHtml(element.message)
        tpProgressiveInfo.setTextColor(ContextCompat.getColor(itemView.context, unifyprinciplesR.color.Unify_TN500))
        icuChevron.setImage(IconUnify.CHEVRON_RIGHT)
        icuChevron.showIfWithBlock(element.appLink.isNotBlank()) {
            setOnClickListener {
                RouteManager.route(icuChevron.context, element.appLink)
            }
        }
    }
}
