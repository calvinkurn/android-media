package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProgressiveInfoUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartProgressiveInfoBinding

class MiniCartProgressiveInfoViewHolder (
    private val viewBinding: ItemMiniCartProgressiveInfoBinding,
) : AbstractViewHolder<MiniCartProgressiveInfoUiModel>(viewBinding.root) {
    companion object {
        val LAYOUT = R.layout.item_mini_cart_progressive_info
    }

    override fun bind(element: MiniCartProgressiveInfoUiModel) {
        viewBinding.apply {
            iuIcon.loadImage(element.icon)
            tpProgressiveInfo.text = MethodChecker.fromHtml(element.message)
            icuChevron.showIfWithBlock(element.appLink.isNotBlank()) {
                setOnClickListener {
                    RouteManager.route(icuChevron.context, element.appLink)
                }
            }
        }
    }
}
