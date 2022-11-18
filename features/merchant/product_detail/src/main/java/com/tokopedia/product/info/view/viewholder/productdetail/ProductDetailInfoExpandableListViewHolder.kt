package com.tokopedia.product.info.view.viewholder.productdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.BsItemProductDetailExpandableListBinding
import com.tokopedia.product.info.data.response.ShopNotesData
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil.Companion.DIFFUTIL_PAYLOAD_TOGGLE
import com.tokopedia.product.info.view.models.ProductDetailInfoExpandableListDataModel
import com.tokopedia.product.info.widget.ExpandableAnimation
import com.tokopedia.product.share.ekstensions.layoutInflater
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Yehezkiel on 14/10/20
 */
class ProductDetailInfoExpandableListViewHolder(
    private val view: View,
    private val listener: ProductDetailInfoListener
) : AbstractViewHolder<ProductDetailInfoExpandableListDataModel>(view) {

    companion object {

        val LAYOUT = R.layout.bs_item_product_detail_expandable_list
    }

    private val binding = BsItemProductDetailExpandableListBinding.bind(view)

    override fun bind(element: ProductDetailInfoExpandableListDataModel) {
        binding.expandableTitleChevron.titleText = element.title.ifBlank {
            view.context.getString(R.string.merchant_product_detail_shop_notes_title)
        }
        setupExpandableItem(element)
    }

    private fun setupExpandableItem(element: ProductDetailInfoExpandableListDataModel) =
        with(binding) {
            val inflater: LayoutInflater = view.context.layoutInflater

            expandableContainer.removeViews(1, expandableContainer.childCount - 1)

            element.shopNotes.forEach {
                val layoutValuePoint = inflater.inflate(R.layout.partial_item_value_point, null)
                setupPartialView(layoutValuePoint, it)
                expandableContainer.addView(layoutValuePoint)
            }

            expandableTitleChevron.isExpand = element.isShowable
            expandableContainer.showWithCondition(element.isShowable)

            view.setOnClickListener {
                expandableTitleChevron.isExpand = expandableTitleChevron.isExpand != true
                listener.closeAllExpand(element.uniqueIdentifier(), expandableTitleChevron.isExpand)
            }
        }

    private fun setupPartialView(
        rootView: View,
        shopNotesData: ShopNotesData
    ) = with(view) {
        val title: Typography = rootView.findViewById(R.id.point_title)
        val action: Typography = rootView.findViewById(R.id.point_action)
        action.setOnClickListener {
            listener.goToShopNotes(shopNotesData = shopNotesData)
        }
        title.text = shopNotesData.title
    }

    override fun bind(
        element: ProductDetailInfoExpandableListDataModel,
        payloads: MutableList<Any>
    ) {
        super.bind(element, payloads)
        if (payloads.isNotEmpty()) {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey(DIFFUTIL_PAYLOAD_TOGGLE)) {
                val toggle = bundle.getBoolean(DIFFUTIL_PAYLOAD_TOGGLE)
                if (toggle) {
                    ExpandableAnimation.expand(binding.expandableContainer)
                } else {
                    ExpandableAnimation.collapse(binding.expandableContainer)
                }
                binding.expandableTitleChevron.isExpand = toggle
            }
        }
    }
}
