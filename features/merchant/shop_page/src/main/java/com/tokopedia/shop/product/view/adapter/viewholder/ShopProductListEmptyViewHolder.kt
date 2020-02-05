package com.tokopedia.shop.product.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import kotlinx.android.synthetic.main.shop_product_list_empty_state.view.*

class ShopProductListEmptyViewHolder(
        val view: View,
        private val emptyProductOnClickListener: Callback?
) : BaseEmptyViewHolder<EmptyModel>(view) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.shop_product_list_empty_state
    }

    override fun bind(element: EmptyModel) {
        with(view) {
            ImageHandler.loadImage(
                    view.context,
                    no_result_image,
                    element.urlRes,
                    R.drawable.ic_loading_image
            )
            text_view_empty_title_text.text = element.title
            text_view_empty_content_text.text = element.content
            btn_result_empty.run {
                text = context.getString(R.string.shop_product_list_empty_button_text)
                setOnClickListener {
                    emptyProductOnClickListener?.onEmptyButtonClicked()
                }
            }
        }
    }
}