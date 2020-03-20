package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget

import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import java.util.ArrayList

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopHomeVoucherViewHolder(
        itemView: View,
        override val isOwner: Boolean,
        private val shopHomeVoucherViewHolderListener: ShopHomeVoucherViewHolderListener
) : AbstractViewHolder<ShopHomeVoucherUiModel>(itemView), MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener {

    interface ShopHomeVoucherViewHolderListener {
        fun onVoucherListImpression(parentPosition: Int, listVoucher: List<MerchantVoucherViewModel>)
        fun onVoucherSeeAllClicked()
        fun onVoucherClicked(
                parentPosition: Int,
                position: Int,
                merchantVoucherViewModel: MerchantVoucherViewModel
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_merchant_voucher
    }

    init {
        findView(itemView)
    }

    private var merchantVoucherListWidget: MerchantVoucherListWidget? = null
    private var merchantVoucherUiModel: ShopHomeVoucherUiModel? = null

    private fun findView(itemView: View) {
        merchantVoucherListWidget = itemView.findViewById(R.id.merchantVoucherListWidget)
    }

    override fun bind(model: ShopHomeVoucherUiModel) {
        merchantVoucherUiModel = model
        val recyclerViewState = merchantVoucherListWidget?.recyclerView?.layoutManager?.onSaveInstanceState()
        itemView.addOnImpressionListener(model) {
            model.data?.let {
                shopHomeVoucherViewHolderListener.onVoucherListImpression(adapterPosition, it)
            }
        }
        merchantVoucherListWidget?.apply {
            setData(model.data as ArrayList<MerchantVoucherViewModel>?)
            setOnMerchantVoucherListWidgetListener(this@ShopHomeVoucherViewHolder)
            setTitle(model.header.title)
            setSeeAllText(model.header.ctaText)
            getVoucherHeaderContainer()?.let {
                val leftPadding = it.paddingLeft
                val topPadding = it.paddingTop
                val rightPadding = it.paddingRight
                val bottomPadding = 0
                setVoucherHeaderPadding(
                        leftPadding,
                        topPadding,
                        rightPadding,
                        bottomPadding
                )
            }
        }
        recyclerViewState?.let {
            merchantVoucherListWidget?.recyclerView?.layoutManager?.onRestoreInstanceState(it)
        }
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {}

    override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        val position = merchantVoucherUiModel?.data?.indexOf(merchantVoucherViewModel) ?: 0
        shopHomeVoucherViewHolderListener.onVoucherClicked(
                adapterPosition,
                position,
                merchantVoucherViewModel
        )
    }

    override fun onSeeAllClicked() {
        shopHomeVoucherViewHolderListener.onVoucherSeeAllClicked()
    }
}