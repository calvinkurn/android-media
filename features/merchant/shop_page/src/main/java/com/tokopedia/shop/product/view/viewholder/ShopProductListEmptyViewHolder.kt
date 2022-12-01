package com.tokopedia.shop.product.view.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ShopProductListEmptyStateBinding
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopProductListEmptyViewHolder(
    val view: View,
    private val emptyProductOnClickListener: Callback?
) : BaseEmptyViewHolder<EmptyModel>(view) {

    companion object {
        @JvmField
        val LAYOUT = R.layout.shop_product_list_empty_state
    }

    private val viewBinding: ShopProductListEmptyStateBinding? by viewBinding()
    private val noResultImage: ImageView? = viewBinding?.noResultImage
    private val textViewEmptyTitleText: Typography? = viewBinding?.textViewEmptyTitleText
    private val textViewEmptyContentText: Typography? = viewBinding?.textViewEmptyContentText
    private val btnResultEmpty: UnifyButton? = viewBinding?.btnResultEmpty

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
