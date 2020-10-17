package com.tokopedia.product.info.view.viewholder.productdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.widget.ExpandableAnimation
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.bs_item_product_detail_expandable_list.view.*

/**
 * Created by Yehezkiel on 14/10/20
 */
class ProductDetailInfoExpandableListViewHolder(private val view: View, private val listener: ProductDetailInfoListener) : AbstractViewHolder<ProductDetailInfoExpandableListDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.bs_item_product_detail_expandable_list
    }

    override fun bind(element: ProductDetailInfoExpandableListDataModel) {
        view.expandable_title_chevron?.titleText = "Catatan Toko"
        setupExpandableItem(element)
    }

    private fun setupExpandableItem(element: ProductDetailInfoExpandableListDataModel) = with(view) {
        val inflater: LayoutInflater = context.layoutInflater

        repeat(3) {
            val layoutValuePoint = inflater.inflate(R.layout.partial_item_value_point, null)
            setupPartialView(layoutValuePoint, it)
            expandable_container?.addView(layoutValuePoint)
        }

        expandable_title_chevron?.isExpand = element.isShowable
        expandable_container?.showWithCondition(element.isShowable)

        setOnClickListener {
            expandable_title_chevron?.isExpand = expandable_title_chevron?.isExpand != true
            listener.closeAllExpand(element.uniqueIdentifier(), expandable_title_chevron?.isExpand
                    ?: false)
        }
    }

    private fun setupPartialView(rootView: View, position: Int) = with(view) {
        val title: Typography = rootView.findViewById(R.id.point_title)
        val value: Typography = rootView.findViewById(R.id.point_action)

        title.text = "Catatan $position"
    }

    override fun bind(element: ProductDetailInfoExpandableListDataModel, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey("toggle")) {
                val toggle = bundle.getBoolean("toggle")
                if (toggle) {
                    ExpandableAnimation.expand(view.expandable_container)
                } else {
                    ExpandableAnimation.collapse(view.expandable_container)
                }
                view.expandable_title_chevron?.isExpand = toggle
            }
        }
    }
}