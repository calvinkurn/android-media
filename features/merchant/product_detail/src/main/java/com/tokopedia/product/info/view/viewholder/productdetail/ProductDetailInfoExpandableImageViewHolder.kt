package com.tokopedia.product.info.view.viewholder.productdetail

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.widget.ExpandableAnimation
import kotlinx.android.synthetic.main.bs_item_product_detail_expandable_image.view.*

/**
 * Created by Yehezkiel on 14/10/20
 */
class ProductDetailInfoExpandableImageViewHolder(private val view: View, private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoExpandableImageDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_expandable_image
    }

    override fun bind(element: ProductDetailInfoExpandableImageDataModel) {
        view.expandable_title_chevron?.titleText = "Panduan Ukuran"
        setupExpandableItem(element)
    }

    private fun setupExpandableItem(element: ProductDetailInfoExpandableImageDataModel) = with(view) {
        expandable_image?.loadImage("https://ecs7-p.tokopedia.net/img/cache/700/product-1/2020/7/28/17211033/17211033_a30b5d37-0bf9-41da-8273-b32731d2553b_1180_1180")
        expandable_image?.showWithCondition(element.isShowable)
        expandable_title_chevron?.isExpand = element.isShowable

        setOnClickListener {
            expandable_title_chevron?.isExpand = expandable_title_chevron?.isExpand != true
            listener.closeAllExpand(element.uniqueIdentifier(), expandable_title_chevron?.isExpand
                    ?: false)
        }
    }

    override fun bind(element: ProductDetailInfoExpandableImageDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey("toggle")) {
                val toggle = bundle.getBoolean("toggle")
                if (toggle) {
                    ExpandableAnimation.expand(view.expandable_image)
                } else {
                    ExpandableAnimation.collapse(view.expandable_image)
                }
                view.expandable_title_chevron?.isExpand = toggle
            }
        }
    }
}