package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.imageassets.utils.loadProductImage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.databinding.PartialNonProductBundleDetailBinding
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl

class PartialSomDetailNonProductBundleDetailViewHolder(
    private var binding: PartialNonProductBundleDetailBinding?,
    private var actionListener: SomDetailAdapterFactoryImpl.ActionListener?
) {

    private fun setupProductDetail(element: SomDetailOrder.GetSomDetail.Details.Product?) {
        binding?.run {
            if (element == null) {
                root.hide()
            } else {
                root.show()
                setupProductClickListener(element.orderDetailId.toLongOrZero())
                setupProductImage(element.thumbnail)
                setupProductName(element.name)
                setupProductDescription(element.quantity, element.priceText)
                setupProductNote(element.note)
            }
        }
    }

    private fun PartialNonProductBundleDetailBinding.setupProductClickListener(orderDetailId: Long) {
        root.setOnClickListener {
            actionListener?.onClickProduct(orderDetailId)
        }
    }

    private fun PartialNonProductBundleDetailBinding.setupProductImage(thumbnailUrl: String) {
        ivProduct.loadProductImage(
            url = thumbnailUrl,
            archivedUrl = TokopediaImageUrl.IMG_ARCHIVED_PRODUCT_SMALL
        )
    }

    private fun PartialNonProductBundleDetailBinding.setupProductName(name: String) {
        tvProductName.text = name
    }

    @SuppressLint("SetTextI18n")
    private fun PartialNonProductBundleDetailBinding.setupProductDescription(
        quantity: Int,
        priceText: String
    ) {
        tvProductDesc.text = "$quantity x $priceText"
    }

    private fun PartialNonProductBundleDetailBinding.setupProductNote(note: String) {
        if (note.isNotBlank()) {
            dividerProduct.visibility = View.VISIBLE
            tvProductNotes.visibility = View.VISIBLE
            tvProductNotes.text = MethodChecker.fromHtmlWithoutExtraSpace(
                root.context.getString(
                    R.string.som_detail_product_note_format,
                    note.replace(
                        oldValue = "\\n",
                        newValue = System.getProperty("line.separator") ?: ""
                    )
                )
            )
        } else {
            dividerProduct.visibility = View.GONE
            tvProductNotes.visibility = View.GONE
        }
    }

    fun bind(product: SomDetailOrder.GetSomDetail.Details.Product?) {
        setupProductDetail(product)
    }

    fun isShowing() = binding?.root?.isVisible == true
}
