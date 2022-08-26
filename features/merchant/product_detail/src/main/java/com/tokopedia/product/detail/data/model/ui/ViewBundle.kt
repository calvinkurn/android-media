package com.tokopedia.product.detail.data.model.ui

import android.graphics.Paint
import android.view.View
import android.view.ViewStub
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.bundleinfo.BundleInfo
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * UI Model for ProductBundlingViewHolder
 */
class ViewBundle(parent: View) {
    private val viewStub: ViewStub = parent.findViewById(R.id.product_bundling_stub_component)
    private val view: View = viewStub.inflate()

    private val title: Typography = view.findViewById(R.id.product_bundling_title)
    private val info: Typography = view.findViewById(R.id.product_bundling_info)
    private val slash: Typography = view.findViewById(R.id.product_bundling_total_slash)
    private val saving: Typography = view.findViewById(R.id.product_bundling_total_saving)
    private val savingView: View = view.findViewById(R.id.product_bundling_view_saving)
    private val price: Typography = view.findViewById(R.id.product_bundling_total_price)
    private val buttonCheck: UnifyButton = view.findViewById(R.id.product_bundling_button_check)

    fun process(bundle: BundleInfo, onClickCheck: () -> Unit) {
        view.show()

        title.text = bundle.titleComponent
        info.text = bundle.preorderString
        slash.apply {
            text = bundle.originalPriceBundling
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        val savingPrice = bundle.savingPriceBundling
        if (savingPrice.isBlank()) savingView.hide()
        else saving.text = savingPrice

        price.text = bundle.finalPriceBundling
        buttonCheck.setOnClickListener { onClickCheck() }
    }

    fun hide() = view.hide()
}