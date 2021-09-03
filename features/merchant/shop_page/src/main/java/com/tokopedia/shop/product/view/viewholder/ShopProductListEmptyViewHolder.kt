package com.tokopedia.shop.product.view.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ShopProductListEmptyViewHolder(
        val view: View,
        private val emptyProductOnClickListener: Callback?
) : BaseEmptyViewHolder<EmptyModel>(view) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.shop_product_list_empty_state
    }

    private val noResultImage: ImageView? = itemView.findViewById(R.id.no_result_image)
    private val textViewEmptyTitleText: Typography? = itemView.findViewById(R.id.text_view_empty_title_text)
    private val textViewEmptyContentText: Typography? = itemView.findViewById(R.id.text_view_empty_content_text)
    private val btnResultEmpty: UnifyButton? = itemView.findViewById(R.id.btn_result_empty)

    override fun bind(element: EmptyModel) {
        noResultImage?.loadImage(element.urlRes) {
            setPlaceHolder(R.drawable.ic_shop_page_loading_image)
        }
        textViewEmptyTitleText?.text = element.title
        textViewEmptyContentText?.text = element.content
        btnResultEmpty?.run {
            text = context.getString(R.string.shop_product_list_empty_button_text)
            setOnClickListener {
                emptyProductOnClickListener?.onEmptyButtonClicked()
            }
        }
    }
}