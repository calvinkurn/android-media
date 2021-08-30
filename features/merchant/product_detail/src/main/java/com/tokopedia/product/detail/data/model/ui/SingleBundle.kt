package com.tokopedia.product.detail.data.model.ui

import android.graphics.Paint
import android.view.View
import android.view.ViewStub
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.bundleinfo.BundleInfo
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * UI Model for ProductBundlingViewHolder
 */
class SingleBundle(parent: View) {
    private val viewStub: ViewStub = parent.findViewById(R.id.product_bundling_stub_single)
    private val view: View = viewStub.inflate()

    private val image: ImageUnify = view.findViewById(R.id.product_bundling_single_image)
    private val label: Label = view.findViewById(R.id.product_bundling_single_label)
    private val name: Typography = view.findViewById(R.id.product_bundling_single_name)
    private val price: Typography = view.findViewById(R.id.product_bundling_single_price)
    private val discount: Label = view.findViewById(R.id.product_bundling_single_discount)
    private val slash: Typography = view.findViewById(R.id.product_bundling_single_slash)

    fun process(item: BundleInfo.BundleItem, bundleName: String) {
        view.show()

        image.urlSrc = item.picURL
        name.text = item.name
        price.text = item.bundlePrice

        label.shouldShowWithAction(bundleName.isNotBlank()) {
            label.text = bundleName
        }

        val itemDiscount = item.discountPercentage
        if (itemDiscount.isBlank()) {
            slash.hide()
            discount.hide()
            price.text = item.originalPrice
        } else {
            slash.show()
            discount.show()
            discount.text = itemDiscount
            price.text = item.bundlePrice
            slash.apply {
                text = item.originalPrice
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }

    fun hide() = view.hide()
}