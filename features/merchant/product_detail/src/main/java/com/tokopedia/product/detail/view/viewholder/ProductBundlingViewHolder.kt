package com.tokopedia.product.detail.view.viewholder

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.bundleinfo.BundleInfo
import com.tokopedia.product.detail.data.model.datamodel.ProductBundlingDataModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import java.lang.ref.WeakReference

class ProductBundlingViewHolder(
    view: View
) : AbstractViewHolder<ProductBundlingDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_bundling

        private const val BUNDLE_TYPE_SINGLE = "SINGLE"
        private const val BUNDLE_TYPE_MULTIPLE = "MULTIPLE"
    }

    private val weakContext: WeakReference<Context> = WeakReference(view.context)

    private val title: Typography = view.findViewById(R.id.product_bundling_title)
    private val info: Typography = view.findViewById(R.id.product_bundling_info)
    private val slash: Typography = view.findViewById(R.id.product_bundling_total_slash)
    private val saving: Typography = view.findViewById(R.id.product_bundling_total_saving)
    private val price: Typography = view.findViewById(R.id.product_bundling_total_price)
    private val quantity: Typography = view.findViewById(R.id.product_bundling_total_quantity)

    private val singleView: View = view.findViewById(R.id.product_bundling_content_single)
    private val singleLabel: Label by lazy { view.findViewById(R.id.product_bundling_single_label) }
    private val singleName: Typography by lazy { view.findViewById(R.id.product_bundling_single_name) }
    private val singlePrice: Typography by lazy { view.findViewById(R.id.product_bundling_single_price) }
    private val singleDiscount: Label by lazy { view.findViewById(R.id.product_bundling_single_discount) }
    private val singleSlash: Typography by lazy { view.findViewById(R.id.product_bundling_single_label) }

    private val multiView = view.findViewById<View>(R.id.product_bundling_content_multi)
    private val multiImage1: ImageUnify by lazy { view.findViewById(R.id.product_bundling_image_1) }
    private val multiImage2: ImageUnify by lazy { view.findViewById(R.id.product_bundling_image_2) }
    private val multiImage3: ImageUnify by lazy { view.findViewById(R.id.product_bundling_image_3) }
    private val multiPrice1: Typography by lazy { view.findViewById(R.id.product_bundling_price_1) }
    private val multiPrice2: Typography by lazy { view.findViewById(R.id.product_bundling_price_2) }
    private val multiPrice3: Typography by lazy { view.findViewById(R.id.product_bundling_price_3) }
    private val multiDiscount1: Label by lazy { view.findViewById(R.id.product_bundling_discount_1) }
    private val multiDiscount2: Label by lazy { view.findViewById(R.id.product_bundling_discount_2) }
    private val multiDiscount3: Label by lazy { view.findViewById(R.id.product_bundling_discount_3) }
    private val multiSlash1: Typography by lazy { view.findViewById(R.id.product_bundling_slash_1) }
    private val multiSlash2: Typography by lazy { view.findViewById(R.id.product_bundling_slash_2) }
    private val multiSlash3: Typography by lazy { view.findViewById(R.id.product_bundling_slash_3) }
    private val multiGroup1: Group by lazy { view.findViewById(R.id.product_bundling_group_1) }
    private val multiGroup2: Group by lazy { view.findViewById(R.id.product_bundling_group_2) }
    private val multiGroup3: Group by lazy { view.findViewById(R.id.product_bundling_group_3) }

    override fun bind(element: ProductBundlingDataModel) {

        val bundle = element.bundleInfo
        val bundleItems = bundle.bundleItems

        when (bundle.type) {
            BUNDLE_TYPE_SINGLE -> showSingleBundle(bundleItems.firstOrNull())
            BUNDLE_TYPE_MULTIPLE -> showMultiBundle(bundleItems)
            else -> return
        }

        val context = weakContext.get()

        val quantityText = context?.getString(
            R.string.pdp_bundling_quantity,
            bundleItems.size.toString()
        ) ?: bundleItems.size.toString()

        title.text = bundle.titleComponent
        info.text = bundle.preorderString
        slash.apply {
            text = bundle.originalPriceBundling
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        saving.text = bundle.savingPriceBundling
        price.text = bundle.finalPriceBundling
        quantity.text = quantityText
    }

    private fun showSingleBundle(item: BundleInfo.BundleItem?) {
        if (item == null) return

        singleView.show()
        multiView.hide()

        singleLabel.text = item.quantity
        singleName.text = item.name
        singlePrice.text = item.bundlePrice
        singleDiscount.text = item.discountPercentage
        singleSlash.apply {
            text = item.originalPrice
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun showMultiBundle(items: List<BundleInfo.BundleItem>) {
        singleView.hide()
        multiView.show()

        val viewImages = listOf(multiImage1, multiImage2, multiImage3)
        val viewPrices = listOf(multiPrice1, multiPrice2, multiPrice3)
        val viewDiscounts = listOf(multiDiscount1, multiDiscount2, multiDiscount3)
        val viewSlashes = listOf(multiSlash1, multiSlash2, multiSlash3)
        val viewGroups = listOf(multiGroup1, multiGroup2, multiGroup3)

        val unusedGroups = viewGroups.toMutableList()
        items.forEachIndexed { index, item ->
            viewGroups[index].apply {
                unusedGroups -= this
                show()
            }
            viewImages[index].urlSrc = item.picURL
            viewPrices[index].text = item.bundlePrice
            viewDiscounts[index].text = item.discountPercentage
            viewSlashes[index].apply {
                text = item.originalPrice
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

        unusedGroups.forEach { group ->
            group.invisible()
        }
    }

}