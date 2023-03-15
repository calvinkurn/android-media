package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailHeaderUiModel

class VariantDetailHeaderViewHolder(itemView: View?, listener: OnCollapsibleHeaderClickListener) : AbstractViewHolder<VariantDetailHeaderUiModel>(itemView) {

    private var unitValueHeader: AppCompatTextView? = null
    private var accordionIndicator: AppCompatImageView? = null
    private var divTop: View? = null
    private var headerPosition = 0

    init {
        unitValueHeader = itemView?.findViewById(R.id.tv_unit_value_header)
        accordionIndicator = itemView?.findViewById(R.id.iv_accordion_indicator)
        divTop = itemView?.findViewById(R.id.div_top_item)

        itemView?.setOnClickListener {
            val isCollapsed = listener.onHeaderClicked(headerPosition)
            changeIndicatorIcon(isCollapsed)
        }
    }

    override fun bind(element: VariantDetailHeaderUiModel?) {
        element?.run {
            headerPosition = element.position
            unitValueHeader?.text = element.headerTitle
            changeIndicatorIcon(element.isCollapsed)
            divTop?.isVisible = adapterPosition != Int.ZERO
        }
    }

    private fun changeIndicatorIcon(isCollapsed: Boolean) {
        accordionIndicator?.apply {
            val iconRes = if (isCollapsed)
                getIconUnifyDrawable(context, IconUnify.CHEVRON_DOWN, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
            else
                getIconUnifyDrawable(context, IconUnify.CHEVRON_UP, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
            loadImage(iconRes)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.product_variant_detail_header_layout
    }

    interface OnCollapsibleHeaderClickListener {
        fun onHeaderClicked(headerPosition:Int): Boolean
    }
}
