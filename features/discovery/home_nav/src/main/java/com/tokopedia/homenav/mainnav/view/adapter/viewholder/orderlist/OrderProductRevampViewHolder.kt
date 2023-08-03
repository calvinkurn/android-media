package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionProductRevampBinding
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductRevampModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

@MePage(MePage.Widget.TRANSACTION)
class OrderProductRevampViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OrderProductRevampModel>(itemView) {
    private var binding: HolderTransactionProductRevampBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_product_revamp
    }

    override fun bind(element: OrderProductRevampModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setLayoutFullWidth(element: OrderProductRevampModel) {
        val layoutParams = binding?.orderProductCard?.layoutParams
        if (element.navProductModel.fullWidth) {
            layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            layoutParams?.width =
                itemView.resources.getDimension(com.tokopedia.homenav.R.dimen.nav_card_me_page_size).toInt()
        }
        binding?.orderProductCard?.layoutParams = layoutParams
    }

    override fun bind(productRevampModel: OrderProductRevampModel) {
        val context = itemView.context
        setLayoutFullWidth(productRevampModel)
        itemView.addOnImpressionListener(productRevampModel)  {
            mainNavListener.onOrderCardImpressed(
                productRevampModel.navProductModel.statusText,
                productRevampModel.navProductModel.id,
                productRevampModel.position
            )
        }
        //title
        binding?.orderProductName?.text = productRevampModel.navProductModel.productNameText

        //image
        if (productRevampModel.navProductModel.imageUrl.isNotEmpty()) {
            binding?.orderProductImage?.setImageUrl(
                productRevampModel.navProductModel.imageUrl
            )
        }

        //description
        binding?.orderProductDescription?.text = productRevampModel.navProductModel.estimatedArrival
        if (productRevampModel.navProductModel.descriptionTextColor.isNotEmpty()) {
            binding?.orderProductDescription?.setTextColor(
                   Color.parseColor(productRevampModel.navProductModel.descriptionTextColor)
            )
        }

        //status
        binding?.orderProductStatus?.text = productRevampModel.navProductModel.statusText
        var productStatusColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN500)
        if (productRevampModel.navProductModel.statusTextColor.isNotEmpty()) {
            productStatusColor = Color.parseColor(productRevampModel.navProductModel.statusTextColor)
        }
        binding?.orderProductStatus?.setTextColor(productStatusColor)

        //more than 1 product
        if (productRevampModel.navProductModel.additionalProductCount != 0) {
            binding?.orderProductImageLayer?.visibility = View.VISIBLE
            binding?.orderProductCount?.visibility = View.VISIBLE
            binding?.orderProductCount?.text = String.format(
                    context.getString(R.string.transaction_item_total_product),
                    productRevampModel.navProductModel.additionalProductCount
            )
        } else {
            binding?.orderProductImageLayer?.visibility = View.GONE
            binding?.orderProductCount?.visibility = View.GONE
        }

        binding?.orderProductContainer?.setOnClickListener {
            mainNavListener.onOrderCardClicked(
                productRevampModel.navProductModel.applink,
                productRevampModel.navProductModel.statusText
            )
        }
    }
}
