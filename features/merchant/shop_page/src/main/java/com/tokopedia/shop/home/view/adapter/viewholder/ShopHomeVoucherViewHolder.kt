package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.ImageView

import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget

import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.unifyprinciples.Typography
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
        fun onVoucherItemImpressed(parentPosition: Int, itemPosition: Int, voucher: MerchantVoucherViewModel)
        fun onVoucherSeeAllClicked()
        fun onVoucherClicked(
                parentPosition: Int,
                position: Int,
                merchantVoucherViewModel: MerchantVoucherViewModel
        )
        fun onVoucherReloaded()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_merchant_voucher
    }

    init {
        findView(itemView)
    }

    private var merchantVoucherListWidget: MerchantVoucherListWidget? = null
    private var merchantVoucherReload: CardView? = null
    private var merchantVoucherUiModel: ShopHomeVoucherUiModel? = null
    private var textReload: Typography? = null
    private var imageReload: ImageView? = null

    private fun findView(itemView: View) {
        merchantVoucherListWidget = itemView.findViewById(R.id.merchantVoucherListWidget)
        merchantVoucherReload = itemView.findViewById(R.id.merchantVoucherReload)
        textReload = itemView.findViewById(R.id.textReload)
        imageReload = itemView.findViewById(R.id.imageReload)
    }

    override fun bind(model: ShopHomeVoucherUiModel) {
        if (model.isError) {
            merchantVoucherListWidget?.hide()
            merchantVoucherReload?.show()
            textReload?.setOnClickListener {
                shopHomeVoucherViewHolderListener.onVoucherReloaded()
                merchantVoucherListWidget?.show()
                merchantVoucherReload?.hide()
            }
            imageReload?.setOnClickListener {
                shopHomeVoucherViewHolderListener.onVoucherReloaded()
                merchantVoucherListWidget?.show()
                merchantVoucherReload?.hide()
            }
        } else {
            merchantVoucherListWidget?.show()
            merchantVoucherUiModel = model
            val recyclerViewState = merchantVoucherListWidget?.recyclerView?.layoutManager?.onSaveInstanceState()

            merchantVoucherListWidget?.apply {
                setOnMerchantVoucherListWidgetListener(this@ShopHomeVoucherViewHolder)
                setData(model.data as ArrayList<MerchantVoucherViewModel>?)
                setTitle(model.header.title)
                setSeeAllText(model.header.ctaText)
                getVoucherHeaderContainer()?.let {
                    val leftPadding = it.paddingLeft
                    val topPadding = it.paddingTop
                    val rightPadding = it.paddingRight
                    val bottomPadding = 0
                    it.setPadding(
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

    override fun onVoucherItemImpressed(merchantVoucherViewModel: MerchantVoucherViewModel, voucherPosition: Int) {
        shopHomeVoucherViewHolderListener.onVoucherItemImpressed(
                adapterPosition,
                voucherPosition,
                merchantVoucherViewModel
        )
    }
}