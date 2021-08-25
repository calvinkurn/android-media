package com.tokopedia.product.detail.data.model.ui

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.view.ViewStub
import androidx.constraintlayout.widget.Group
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.bundleinfo.BundleInfo
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import java.lang.ref.WeakReference

/**
 * UI Model for ProductBundlingViewHolder
 */
class MultiBundle(parent: View) {

    private val viewStub: ViewStub = parent.findViewById(R.id.product_bundling_stub_multi)
    private val view: View = viewStub.inflate()

    private val weakContext: WeakReference<Context> = WeakReference(view.context)

    private val image1: ImageUnify = view.findViewById(R.id.product_bundling_image_1)
    private val image2: ImageUnify = view.findViewById(R.id.product_bundling_image_2)
    private val image3: ImageUnify = view.findViewById(R.id.product_bundling_image_3)
    private val price1: Typography = view.findViewById(R.id.product_bundling_price_1)
    private val price2: Typography = view.findViewById(R.id.product_bundling_price_2)
    private val price3: Typography = view.findViewById(R.id.product_bundling_price_3)
    private val discount1: Label = view.findViewById(R.id.product_bundling_discount_1)
    private val discount2: Label = view.findViewById(R.id.product_bundling_discount_2)
    private val discount3: Label = view.findViewById(R.id.product_bundling_discount_3)
    private val slash1: Typography = view.findViewById(R.id.product_bundling_slash_1)
    private val slash2: Typography = view.findViewById(R.id.product_bundling_slash_2)
    private val slash3: Typography = view.findViewById(R.id.product_bundling_slash_3)
    private val group1: Group = view.findViewById(R.id.product_bundling_group_1)
    private val group2: Group = view.findViewById(R.id.product_bundling_group_2)
    private val group3: Group = view.findViewById(R.id.product_bundling_group_3)

    private val quantity: Typography = parent.findViewById(R.id.product_bundling_total_quantity)

    private val images = listOf(image1, image2, image3)
    private val prices = listOf(price1, price2, price3)
    private val discounts = listOf(discount1, discount2, discount3)
    private val slashes = listOf(slash1, slash2, slash3)
    private val groups = listOf(group1, group2, group3)

    fun process(
        bundle: BundleInfo,
        setOnClickItem: (productId: String) -> Unit
    ) {
        view.show()

        val items = bundle.bundleItems
        val unusedGroups = groups.toMutableList()

        items.forEachIndexed { index, item ->
            val viewImage = images[index]
            val viewPrice = prices[index]
            val viewDiscount = discounts[index]
            val viewSlash = slashes[index]

            groups[index].apply {
                unusedGroups -= this
                show()
            }

            viewImage.urlSrc = item.picURL

            val discount = item.discountPercentage
            if (discount.isBlank()) {
                viewSlash.hide()
                viewDiscount.hide()
                viewPrice.text = item.originalPrice
            } else {
                viewDiscount.show()
                viewSlash.show()
                viewPrice.text = item.bundlePrice
                viewDiscount.text = discount
                viewSlash.apply {
                    text = item.originalPrice
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
            }

            val itemProductId = item.productId
            if (itemProductId != bundle.productId) {
                setItemClickListener(listOf(viewImage, viewPrice, viewDiscount, viewSlash)) {
                    setOnClickItem(itemProductId)
                }
            }
        }

        unusedGroups.forEach { group ->
            group.hide()
        }

        val quantityText = weakContext.get()?.getString(
            R.string.pdp_bundling_quantity,
            items.size.toString()
        ) ?: items.size.toString()
        quantity.text = quantityText
        quantity.show()
    }

    fun hide() {
        view.hide()
        quantity.hide()
    }

    private fun setItemClickListener(views: List<View>, onClick: () -> Unit) {
        views.forEach { it.setOnClickListener { onClick() } }
    }
}