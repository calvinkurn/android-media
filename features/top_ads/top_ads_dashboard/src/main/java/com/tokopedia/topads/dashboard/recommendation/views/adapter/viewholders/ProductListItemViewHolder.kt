package com.tokopedia.topads.dashboard.recommendation.views.adapter.viewholders

import android.view.View
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify


class ProductListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val title: com.tokopedia.unifyprinciples.Typography = view.findViewById(R.id.title)
    private val description: com.tokopedia.unifyprinciples.Typography =
        view.findViewById(R.id.description)
    private val image: ImageUnify = view.findViewById(R.id.image)
    private val checkboxUnify: CheckboxUnify = view.findViewById(R.id.checkbox)

    fun bind(
        item: ProductItemUiModel,
        onItemCheckedChangeListener: (() -> Unit)?
    ) {
        title.text = item.productName
        description.text = HtmlCompat.fromHtml(
            String.format(
                view.context.getString(R.string.topads_insight_centre_product_item_description),
                item.searchCount
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        image.urlSrc = item.imgUrl
        checkboxUnify.isChecked = item.isSelected

        checkboxUnify.setOnClickListener {
            item.isSelected = checkboxUnify.isChecked
            onItemCheckedChangeListener?.invoke()
        }
    }
}
