package com.tokopedia.product.detail.view.viewholder

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.merchantvoucher.common.widget.CustomVoucherView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniShopInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMiniSocialProofDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.CustomTypeSpan
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.item_dynamic_general_info.view.*
import kotlinx.android.synthetic.main.item_mini_shop_info.view.*
import kotlinx.android.synthetic.main.partial_product_detail_header.view.*

/**
 * Created by Yehezkiel on 20/05/20
 */
class ProductMiniShopInfoViewHolder(private val view: View, private val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductMiniShopInfoDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_mini_shop_info
    }

    override fun bind(element: ProductMiniShopInfoDataModel) {
        if (element.shopName.isEmpty()) {
            showLoading()
        } else {
            if (!element.isOS && !element.isGoldMerchant) {
                view.setPadding(0, 0, 0, 0)
                view.layoutParams.height = 0
            } else {
                hideLoading()
                view.setOnClickListener { listener.onMiniShopInfoClicked(getComponentTrackData(element)) }
                view.setPadding(16.toPx(), 0, 16.toPx(), 16.toPx())
                view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                showOfficialStore(element.isGoldMerchant, element.isOS, element.shopName)
            }
        }
    }

    private fun showOfficialStore(isGoldMerchant: Boolean, isOfficialStore: Boolean, shopname: String) = with(view) {
        val imageIc: ImageSpan
        val drawableSize = context.resources.getDimension(R.dimen.dp_15).toInt()

        if (isGoldMerchant && !isOfficialStore) {
            txt_mini_shop_desc.hide()

            val drawablePm = MethodChecker.getDrawable(context, R.drawable.ic_power_merchant)
            /* https://stackoverflow.com/questions/36714640/how-setbounds-of-drawable-works-in-android */
            drawablePm?.setBounds(0, 0, drawableSize, drawableSize)
            imageIc = ImageSpan(drawablePm, ImageSpan.ALIGN_BOTTOM)
            renderTxtIcon(shopname, imageIc)
        } else if (isOfficialStore) {
            txt_mini_shop_desc.show()

            val drawableOs = MethodChecker.getDrawable(context, R.drawable.ic_pdp_new_official_store)
            drawableOs?.setBounds(0, 0, drawableSize, drawableSize)
            imageIc = ImageSpan(drawableOs, ImageSpan.ALIGN_BOTTOM)
            renderTxtIcon(shopname, imageIc)
        }
    }

    private fun renderTxtIcon(labelIc: String, imageIc: ImageSpan) = with(view.txt_mini_shop_title) {
        val blackString = context.getString(R.string.label_dijual) + "  "
        val startSpan = blackString.length
        val spanText = android.text.SpannableString(blackString + "   " +
                labelIc)
        val boldTypeface = com.tokopedia.unifyprinciples.getTypeface(context, "RobotoBold.ttf")

        spanText.setSpan(imageIc, startSpan, startSpan + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(StyleSpan(Typeface.BOLD), startSpan, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanText.setSpan(CustomTypeSpan(boldTypeface), startSpan, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        setText(spanText, android.widget.TextView.BufferType.SPANNABLE)
        visible()
    }

    private fun showLoading() = with(view) {
        mini_shop_shimmering?.show()
        txt_mini_shop_desc?.hide()
        txt_mini_shop_title?.hide()
    }

    private fun hideLoading() = with(view) {
        mini_shop_shimmering?.hide()
        txt_mini_shop_desc?.show()
        txt_mini_shop_title?.show()
    }

    private fun getComponentTrackData(element: ProductMiniShopInfoDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}