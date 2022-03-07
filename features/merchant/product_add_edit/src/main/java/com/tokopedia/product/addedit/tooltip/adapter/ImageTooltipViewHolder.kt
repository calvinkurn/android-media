package com.tokopedia.product.addedit.tooltip.adapter

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.tooltip.model.ImageTooltipModel
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by faisalramd on 2020-03-09.
 */

class ImageTooltipViewHolder(val view: View?,
                             private val listener: TooltipTypeFactory.OnItemClickListener?)
    : AbstractViewHolder<ImageTooltipModel>(view) {

    val textViewContent: Typography? = view?.findViewById(R.id.textViewContent)
    val imageViewContent: ImageView? = view?.findViewById(R.id.imageViewContent)

    override fun bind(element: ImageTooltipModel) {
        textViewContent?.text = element.title
        imageViewContent?.loadImageRounded(element.imageUrl)
        itemView.setOnClickListener { listener?.onClick(element, adapterPosition) }
    }

    companion object {
        val LAYOUT = R.layout.item_tooltip_image
    }
}