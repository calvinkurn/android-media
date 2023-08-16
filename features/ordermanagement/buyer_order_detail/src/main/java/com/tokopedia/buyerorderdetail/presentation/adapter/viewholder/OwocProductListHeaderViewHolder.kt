package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.databinding.ItemOwocProductListHeaderBinding
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage

class OwocProductListHeaderViewHolder(
    view: View?,
    private val listener: Listener
) : AbstractViewHolder<OwocProductListUiModel.ProductListHeaderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_owoc_product_list_header

        const val LABEL_TYPE = "label"
    }

    private val binding = ItemOwocProductListHeaderBinding.bind(itemView)

    init {
        setupClickListener()
    }

    private var element: OwocProductListUiModel.ProductListHeaderUiModel? = null

    override fun bind(element: OwocProductListUiModel.ProductListHeaderUiModel?) {
        element?.let {
            this.element = it
            setupShopBadge(it.shopBadgeUrl)
            setupShopName(it.shopName)
            setupInvoice(it.invoiceNumber)
            setupButtonOrLabel(it.owocActionButtonUiModel)
        }
    }

    override fun bind(
        element: OwocProductListUiModel.ProductListHeaderUiModel?,
        payloads: MutableList<Any>
    ) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocProductListUiModel.ProductListHeaderUiModel && newItem is OwocProductListUiModel.ProductListHeaderUiModel) {
                    this.element = newItem
                    if (oldItem.shopBadgeUrl != newItem.shopBadgeUrl) {
                        setupShopBadge(newItem.shopBadgeUrl)
                    }
                    if (oldItem.shopName != newItem.shopName) {
                        setupShopName(newItem.shopName)
                    }
                    if (oldItem.invoiceNumber != newItem.invoiceNumber) {
                        setupInvoice(newItem.invoiceNumber)
                    }
                    if (oldItem.owocActionButtonUiModel != newItem.owocActionButtonUiModel) {
                        setupButtonOrLabel(newItem.owocActionButtonUiModel)
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun goToOtherBomDetail() {
        element?.let {
            listener.goToOtherBomDetail(it.orderId)
        }
    }

    private fun setupClickListener() {
        with(binding) {
            btnOwocMoreDetail.setOnClickListener {
                goToOtherBomDetail()
            }
        }
    }

    private fun setupButtonOrLabel(
        owocButtonActionButtonsUiModel:
            OwocProductListUiModel.ProductListHeaderUiModel.OwocActionButtonUiModel
    ) {
        val isLabelType = owocButtonActionButtonsUiModel.type == LABEL_TYPE
        setupButton(owocButtonActionButtonsUiModel, isLabelType)
        setupLabel(owocButtonActionButtonsUiModel, isLabelType)
    }

    private fun setupButton(
        owocButtonActionButtonsUiModel:
            OwocProductListUiModel.ProductListHeaderUiModel.OwocActionButtonUiModel,
        isLabelType: Boolean
    ) {
        binding.btnOwocMoreDetail.run {
            text = owocButtonActionButtonsUiModel.displayName
            buttonVariant = Utils.mapButtonVariant(owocButtonActionButtonsUiModel.variant)
            buttonType = Utils.mapButtonType(owocButtonActionButtonsUiModel.type)
            showWithCondition(!isLabelType && owocButtonActionButtonsUiModel.displayName.isNotBlank())
        }
    }

    private fun setupLabel(
        owocButtonActionButtonsUiModel: OwocProductListUiModel.ProductListHeaderUiModel.OwocActionButtonUiModel,
        isLabelType: Boolean
    ) {
        binding.labelOwoc.run {
            text = owocButtonActionButtonsUiModel.displayName
            showWithCondition(isLabelType && owocButtonActionButtonsUiModel.displayName.isNotBlank())
        }
    }

    private fun setupShopBadge(shopBadgeUrl: String) {
        if (shopBadgeUrl.isBlank()) {
            hideShopBadge()
        } else {
            showShopBadge(shopBadgeUrl)
        }
    }

    private fun setupShopName(shopName: String) {
        binding.tvOwocShopLabel.text = MethodChecker.fromHtml(shopName)
    }

    private fun setupInvoice(invoice: String) {
        binding.tvOwocOrderInvoice.text = invoice
    }

    private fun hideShopBadge() {
        binding.ivOwocShopTier.gone()
    }

    private fun showShopBadge(url: String) {
        binding.ivOwocShopTier.let {
            it.loadImage(url) {
                setErrorDrawable(com.tokopedia.utils.R.drawable.ic_loading_error)
            }
        }
    }

    interface Listener {
        fun goToOtherBomDetail(orderId: String)
    }
}
