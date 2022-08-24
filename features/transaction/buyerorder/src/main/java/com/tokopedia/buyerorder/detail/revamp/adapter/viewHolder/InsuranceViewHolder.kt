package com.tokopedia.buyerorder.detail.revamp.adapter.viewHolder

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorder.R
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR
import com.tokopedia.buyerorder.databinding.VoucherItemInsuranceBinding
import com.tokopedia.buyerorder.detail.data.Items
import com.tokopedia.buyerorder.detail.data.ItemsInsurance
import com.tokopedia.buyerorder.detail.data.MetaDataInfo
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.revamp.adapter.EventDetailsListener
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.utils.view.DoubleTextView

/**
 * created by @bayazidnasir on 23/8/2022
 */
class InsuranceViewHolder(
    itemView: View,
    private val eventDetailsListener: EventDetailsListener
): AbstractViewHolder<ItemsInsurance>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.voucher_item_insurance

        private const val CATEGORY_PRODUCT = "Kategori Produk"
        private const val TEXT_STYLE_BOLD = "bold"
        private const val TEXT_SIZE_LARGE = 14F
    }

    override fun bind(element: ItemsInsurance) {
        val binding = VoucherItemInsuranceBinding.bind(itemView)
        val metadata = getMetadata(element.item)

        renderProduct(binding, metadata, element.item, element.orderDetails)
    }

    private fun renderProduct(
        binding: VoucherItemInsuranceBinding,
        metadata: MetaDataInfo,
        item: Items,
        orderDetails: OrderDetails,
    ){
        if (metadata.productImage.isEmpty()) {
            binding.imgProduct.loadImageCircle(item.imageUrl)
        } else {
            binding.imgProduct.loadImageCircle(metadata.productImage)
        }

        if (metadata.productName.isEmpty()) {
            binding.txtProductName.text = item.title
        } else {
            binding.txtProductName.text = metadata.productName
        }

        eventDetailsListener.setDetailTitle(getString(R.string.insurance_detail_label))

        binding.txtQuantity.text = metadata.productQuantity
        binding.txtItemPrice.text = metadata.productPrice

        val productCategory = orderDetails.title.single { it.label.equals(CATEGORY_PRODUCT, true)  }.value

        val map = linkedMapOf<String, String>(
            getString(R.string.product_category) to productCategory,
            getString(R.string.insurance_type) to metadata.insuranceType,
            getString(R.string.insurance_quantity) to metadata.productQuantity,
            getString(R.string.insurance_length) to metadata.insuranceLength,
            getString(R.string.insurance_price) to metadata.premiumPrice,
        )

        setProductDetails(binding, map)
        eventDetailsListener.setInsuranceDetail()
    }

    private fun setProductDetails(
        binding: VoucherItemInsuranceBinding,
        map: Map<String, String>
    ){
        map.entries.forEach {
            val textView = DoubleTextView(itemView.context, LinearLayout.HORIZONTAL).apply {
                setTopText(it.key)
                setBottomText(it.value)
                setTopTextColor(MethodChecker.getColor(itemView.context, unifyPrinciplesR.color.Unify_N700_44))
                setBottomTextColor(MethodChecker.getColor(itemView.context, unifyPrinciplesR.color.Unify_N700_68))
                setBottomTextStyle(TEXT_STYLE_BOLD)
                setBottomTextSize(TEXT_SIZE_LARGE)
            }
            binding.statusDetail.addView(textView)
        }
    }

    private fun getMetadata(item: Items): MetaDataInfo{
        val gson = Gson()
        return gson.fromJson(item.metaData, MetaDataInfo::class.java)
    }
}