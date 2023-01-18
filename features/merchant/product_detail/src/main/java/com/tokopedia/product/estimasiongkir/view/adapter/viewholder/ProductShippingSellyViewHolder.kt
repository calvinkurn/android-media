package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemProductShippingSellyBinding
import com.tokopedia.product.detail.databinding.ItemSellyDateBinding
import com.tokopedia.product.detail.databinding.ItemSellyTimeBinding
import com.tokopedia.product.estimasiongkir.data.model.shipping.Product
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingSellyDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.Service
import kotlin.reflect.KMutableProperty0

class ProductShippingSellyViewHolder(
    view: View
) : AbstractViewHolder<ProductShippingSellyDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_shipping_selly
    }

    private val binding = ItemProductShippingSellyBinding.bind(view)
    private val context = view.context

    private val layoutInflater by lazy { LayoutInflater.from(context) }
    private val grayColor by lazy {
        MethodChecker.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN400
        )
    }

    override fun bind(element: ProductShippingSellyDataModel) = with(binding) {
        val services = element.services
        element.services.forEach { service ->
            val serviceView = renderService(service)
            pdpSellyDateList.addView(serviceView)
        }
        pdpSellyTitle.showWithCondition(services.isNotEmpty())
    }

    private fun renderService(
        service: Service
    ) = ItemSellyDateBinding.inflate(layoutInflater).apply {
        val scheduledDate = service.scheduledDate
        pdpSellyDate.showIfWithBlock(scheduledDate.isNotEmpty()) {
            text = scheduledDate
        }

        val products = service.products
        if (service.isAvailable) renderAvailableDate(this, products, service::isExpanded)
        else renderNotAvailableDate(this, products)
    }.root

    /**
     * Render Available & Recommend Date
     */
    private fun renderAvailableDate(
        binding: ItemSellyDateBinding,
        products: List<Product>,
        isExpandedProperty: KMutableProperty0<Boolean>
    ) {
        renderScheduledTimes(binding, products.filter {
            if (isExpandedProperty.get()) true
            else it.isRecommend
        })

        binding.pdpSellyExpandableButton.setOnClickListener {
            toggle(binding, products, isExpandedProperty)
        }
    }

    private fun toggle(
        binding: ItemSellyDateBinding,
        products: List<Product>,
        isExpandedProperty: KMutableProperty0<Boolean>
    ) = with(binding) {
        val isExpanded = isExpandedProperty.get()
        pdpSellyTimeList.removeAllViews()

        renderScheduledTimes(binding, products.filter{
            if (isExpanded) it.isRecommend
            else true
        })
        isExpandedProperty.set(!isExpanded)

        if(isExpanded){
            pdpSellyExpandableButton.text = "Jadwal Lainnya"
            pdpSellyExpandableIcon.animate().rotation(0f).start()
        }else{
            pdpSellyExpandableButton.text = "Tampilkan lebih sedikit"
            pdpSellyExpandableIcon.animate().rotation(180f).start()
        }
    }

    private fun renderNotAvailableDate(
        binding: ItemSellyDateBinding,
        products: List<Product>
    ) = with(binding) {

        pdpSellyExpandableButton.hide()
        pdpSellyExpandableIcon.hide()
        pdpSellyDate.setTextColor(grayColor)

        val product = products.firstOrNull() ?: return@with


        pdpSellyAdditionalMessage.apply {
            root.show()
            val scheduledTime = product.scheduledTime
            pdpSellyTime.showIfWithBlock(scheduledTime.isNotEmpty()) {
                text = scheduledTime
            }

            val messageText = product.text
            pdpSellyFinalPrice.showIfWithBlock(messageText.isNotEmpty()) {
                text = messageText
            }

            listOf(
                pdpSellyTime,
                pdpSellyFinalPrice
            ).forEach { it.setTextColor(grayColor) }
        }
    }

    private fun renderScheduledTimes(
        binding: ItemSellyDateBinding,
        products: List<Product>
    ) = with(binding) {
        products.forEach { product ->
            val childView = renderChildView(product)
            pdpSellyTimeList.addView(childView)
        }
    }

    private fun renderChildView(
        product: Product
    ) = ItemSellyTimeBinding.inflate(layoutInflater).apply {
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
            text = context.getString(R.string.location_dot_builder, scheduledTime)
        }

        val messageText = product.text
        pdpSellyFinalPrice.showIfWithBlock(messageText.isNotEmpty()) {
            text = messageText
        }

        val typographies = listOf(
            pdpSellyTime,
            pdpSellyFinalPrice,
            pdpSellyRealPrice
        )

        typographies.forEach { typography ->
            typography.setTextColor(grayColor)
        }
    }
}
