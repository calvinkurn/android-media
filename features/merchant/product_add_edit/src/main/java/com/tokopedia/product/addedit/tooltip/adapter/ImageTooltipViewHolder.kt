package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import kotlinx.android.synthetic.main.item_tooltip_image.view.*

/**
 * Created by faisalramd on 2020-03-09.
 */

class ImageTooltipViewHolder(val view: View?,
                             private val listener: TooltipTypeFactory.OnItemClickListener?)
    : AbstractViewHolder<ImageTooltipModel>(view) {
    override fun bind(element: ImageTooltipModel) {
        itemView.textViewContent.text = element.title
        itemView.imageViewContent.loadImageRounded(element.imageUrl)
        itemView.setOnClickListener { listener?.onClick(element, adapterPosition) }
    }

    companion object {
        val LAYOUT = R.layout.item_tooltip_image
    }
}