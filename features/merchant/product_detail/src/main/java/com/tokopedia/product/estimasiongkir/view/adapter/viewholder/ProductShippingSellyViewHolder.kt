package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemProductShippingSellyBinding
import com.tokopedia.product.detail.databinding.ItemSellyTimeBinding
import com.tokopedia.product.estimasiongkir.data.model.shipping.Product
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingSellyDataModel

class ProductShippingSellyViewHolder(
    view: View
) : AbstractViewHolder<ProductShippingSellyDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_shipping_selly
    }

    private val binding = ItemProductShippingSellyBinding.bind(view)
    private val context = view.context


    override fun bind(element: ProductShippingSellyDataModel) = with(binding) {

        val title = element.title
        pdpSellyTitle.showIfWithBlock(title.isNotEmpty()) {
            text = title
        }

        val scheduledDate = element.scheduledDate
        pdpSellyDate.showIfWithBlock(scheduledDate.isNotEmpty()) {
            text = scheduledDate
        }

        val products = element.products
        if (element.isAvailable) renderAvailableDate(products)
        else renderNotAvailableDate(this, products)
    }

    /**
     * Render Available & Recommend Date
     */
    private fun renderAvailableDate(products: List<Product>) {
        renderScheduledTimes(products.filter { it.isRecommend })
    }

    private fun renderNotAvailableDate(
        binding: ItemProductShippingSellyBinding,
        products: List<Product>
    ) = with(binding) {

        pdpSellyExpandableButton.hide()

        val grayColor = com.tokopedia.unifyprinciples.R.color.Unify_NN400
        val color = MethodChecker.getColor(context, grayColor)
        pdpSellyTitle.setTextColor(color)

        val product = products.firstOrNull() ?: return
        renderScheduledTimes(listOf(product))
    }

    private fun renderScheduledTimes(products: List<Product>) {
        products.forEach { product ->
            val childView = renderChildView(product)
            binding.pdpSellyTimeList.addView(childView)
        }
    }

    private fun renderChildView(product: Product): View = ItemSellyTimeBinding.inflate(
        LayoutInflater.from(context)
    ).apply {
        if (product.isAvailable) renderAvailableTime(this, product)
        else renderNotAvailableTime(this, product)
    }.root

    private fun renderAvailableTime(
        binding: ItemSellyTimeBinding,
        product: Product
    ) = with(binding) {

        val scheduledTime = product.scheduledTime
        pdpSellyTime.showIfWithBlock(scheduledTime.isNotEmpty()) {
            text = context.getString(R.string.location_dot_builder, scheduledTime)
        }

        val finalPrice = product.finalPrice
        pdpSellyFinalPrice.showIfWithBlock(finalPrice.isNotEmpty()) {
            text = finalPrice
        }

        val realPrice = product.realPrice
        pdpSellyRealPrice.showIfWithBlock(realPrice.isNotEmpty()) {
            text = realPrice
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun renderNotAvailableTime(
        binding: ItemSellyTimeBinding,
        product: Product
    ) = with(binding) {

        val scheduledTime = product.scheduledTime
        pdpSellyTime.showIfWithBlock(scheduledTime.isNotEmpty()) {
            text = scheduledTime
        }

        val messageText = product.text
        pdpSellyFinalPrice.showIfWithBlock(messageText.isNotEmpty()) {
            text = messageText
        }

        val grayColor = com.tokopedia.unifyprinciples.R.color.Unify_NN400
        val color = MethodChecker.getColor(context, grayColor)

        val typographies = listOf(
            pdpSellyTime,
            pdpSellyFinalPrice,
            pdpSellyRealPrice
        )

        typographies.forEach { typography ->
            typography.setTextColor(color)
        }
    }
}
