package com.tokopedia.product.info.view.viewholder.productdetail

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableImageDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil.Companion.DIFFUTIL_PAYLOAD_TOGGLE
import com.tokopedia.product.info.widget.ExpandableAnimation
import kotlinx.android.synthetic.main.bs_item_product_detail_expandable_image.view.*

/**
 * Created by Yehezkiel on 14/10/20
 */
class ProductDetailInfoExpandableImageViewHolder(private val view: View,
                                                 private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoExpandableImageDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_expandable_image
    }

    override fun bind(element: ProductDetailInfoExpandableImageDataModel) {
        view.expandable_title_chevron?.titleText = element.title
        setupExpandableItem(element)
    }

    private fun setupExpandableItem(element: ProductDetailInfoExpandableImageDataModel) = with(itemView) {
        expandable_image?.loadImage(element.imageUrl)

        expandable_image?.showWithCondition(element.isShowable)
        expandable_image?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                listener.goToImagePreview(element.imageUrl)
            }

        })
        expandable_title_chevron?.isExpand = element.isShowable

        expandable_title_chevron?.setOnClickListener {
            expandable_title_chevron?.isExpand = expandable_title_chevron?.isExpand != true
            listener.closeAllExpand(element.uniqueIdentifier(), expandable_title_chevron?.isExpand
                    ?: false)
        }
    }

    override fun bind(element: ProductDetailInfoExpandableImageDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey(DIFFUTIL_PAYLOAD_TOGGLE)) {
                val toggle = bundle.getBoolean(DIFFUTIL_PAYLOAD_TOGGLE)
                if (toggle) {
                    ExpandableAnimation.expand(view.expandable_image, customHeight = view.resources.displayMetrics.widthPixels) {
                        view.expandable_image.loadImage(element.imageUrl)
                    }
                } else {
                    ExpandableAnimation.collapse(view.expandable_image)
                }
                view.expandable_title_chevron?.isExpand = toggle
            }
        }
    }
}