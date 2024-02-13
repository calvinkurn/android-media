package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionProductBinding
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.utils.R as utilsR

class OrderProductViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OrderProductModel>(itemView) {
    private var binding: HolderTransactionProductBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_product
    }

    override fun bind(element: OrderProductModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(productModel: OrderProductModel) {
        val context = itemView.context

        itemView.addOnImpressionListener(productModel)  {
            mainNavListener.onOrderCardImpressed(
                productModel.navProductModel.statusText,
                productModel.navProductModel.id,
                productModel.position
            )
        }
        //title
        binding?.orderProductName?.text = productModel.navProductModel.productNameText

        //image
        if (productModel.navProductModel.imageUrl.isNotEmpty()) {
            val imageView = binding?.orderProductImage
            val shimmer = binding?.orderProductImageShimmer

            productModel.navProductModel.imageUrl.getBitmapImageUrl(
                itemView.context,
                properties = {
                    setPlaceHolder(utilsR.drawable.ic_loading_placeholder)
                    setErrorDrawable(utilsR.drawable.ic_loading_placeholder)
                    fitCenter()
                },
                target = MediaBitmapEmptyTarget(
                onReady = {
                    imageView?.setImageDrawable(it.toDrawable(itemView.resources))
                    shimmer?.gone()
                },
                onLoadStarted = {
                    shimmer?.visible()
                },
                onCleared = {
                    shimmer?.gone()
                },
                onFailed = {
                    shimmer?.gone()
                }
            ))
        }

        //description
        binding?.orderProductDescription?.text = productModel.navProductModel.descriptionText
        if (productModel.navProductModel.descriptionTextColor.isNotEmpty()) {
            binding?.orderProductDescription?.setTextColor(
                   Color.parseColor(productModel.navProductModel.descriptionTextColor)
            )
        }

        //status
        binding?.orderProductStatus?.text = productModel.navProductModel.statusText
        binding?.orderProductStatus?.setTextColor(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN400)
        )

        //more than 1 product
        if (productModel.navProductModel.additionalProductCount != 0) {
            binding?.orderProductImageLayer?.visibility = View.VISIBLE
            binding?.orderProductCount?.visibility = View.VISIBLE
            binding?.orderProductCount?.text = String.format(
                    context.getString(R.string.transaction_item_total_product),
                    productModel.navProductModel.additionalProductCount
            )
        } else {
            binding?.orderProductImageLayer?.visibility = View.GONE
            binding?.orderProductCount?.visibility = View.GONE
        }

        itemView.setOnClickListener {
            mainNavListener.onOrderCardClicked(
                productModel.navProductModel.applink,
                productModel.navProductModel.statusText,
                productModel.navProductModel.id,
                productModel.position
            )
        }
    }
}
