package com.tokopedia.product.addedit.productlimitation.presentation.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler.loadImageFitCenter
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.productlimitation.presentation.model.ProductLimitationActionItemModel
import com.tokopedia.unifyprinciples.Typography

class ProductLimitationItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

    private val tvTitle: Typography = itemView.findViewById(R.id.tv_title)
    private val tvDescription: Typography = itemView.findViewById(R.id.tv_description)
    private val tvAction: Typography = itemView.findViewById(R.id.tv_action)
    private val ivCard: ImageView = itemView.findViewById(R.id.iv_card)

    fun bindData(data: ProductLimitationActionItemModel) {
        val drawableIcon = MethodChecker.getDrawable(itemView.context, com.tokopedia.list.R.drawable.listitem_chevron)
        loadImageFitCenter(itemView.context, ivCard, data.imageUrl)
        tvTitle.text = data.title
        tvDescription.text = data.description
        tvAction.text = data.actionText
        tvAction.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableIcon, null)
    }

    fun setOnClickListener(itemOnClick: (Int) -> Unit) {
        tvAction.setOnClickListener {
            itemOnClick.invoke(adapterPosition)
        }
    }
}