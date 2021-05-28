package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView

import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import com.elyeproj.loaderviewlibrary.LoaderImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.MvcSource
import com.tokopedia.mvcwidget.views.MvcView

import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopHomeVoucherViewHolder(
        itemView: View,
        private val shopHomeVoucherViewHolderListener: ShopHomeVoucherViewHolderListener
) : AbstractViewHolder<ShopHomeVoucherUiModel>(itemView) {

    interface ShopHomeVoucherViewHolderListener {
        fun onVoucherImpression()
        fun onVoucherReloaded()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_merchant_voucher
    }

    init {
        findView(itemView)
    }

    private var merchantVoucherWidget: MvcView? = null
    private var merchantVoucherReload: CardView? = null
    private var merchantVoucherUiModel: ShopHomeVoucherUiModel? = null
    private var merchantVoucherShimmering: LoaderImageView? = null
    private var textReload: Typography? = null
    private var imageReload: ImageView? = null
    private var textReloadDesc: Typography? = null

    private fun findView(itemView: View) {
        merchantVoucherWidget = itemView.findViewById(R.id.merchantVoucherWidget)
        merchantVoucherReload = itemView.findViewById(R.id.merchantVoucherReload)
        merchantVoucherShimmering = itemView.findViewById(R.id.merchantVoucherShimmering)
        textReload = itemView.findViewById(R.id.textReload)
        imageReload = itemView.findViewById(R.id.imageReload)
        textReloadDesc = itemView.findViewById(R.id.textReloadDesc)
    }

    override fun bind(model: ShopHomeVoucherUiModel) {
        if (model.isError) {
            merchantVoucherShimmering?.hide()
            merchantVoucherWidget?.hide()
            merchantVoucherReload?.show()
            textReloadDesc?.text = getReloadDesc()

            textReload?.setOnClickListener {
                shopHomeVoucherViewHolderListener.onVoucherReloaded()
                merchantVoucherShimmering?.show()
                merchantVoucherReload?.hide()

            }
            imageReload?.setOnClickListener {
                shopHomeVoucherViewHolderListener.onVoucherReloaded()
                merchantVoucherShimmering?.show()
                merchantVoucherReload?.hide()
            }
        } else {
            if (model.data != null && model.data.isShown == true) {
                shopHomeVoucherViewHolderListener.onVoucherImpression()
                merchantVoucherShimmering?.hide()
                merchantVoucherWidget?.show()
                merchantVoucherReload?.hide()
                merchantVoucherUiModel = model

                model.data.apply {
                    merchantVoucherWidget?.setData(MvcData(
                            animatedInfos = model.data.animatedInfos ?: listOf()
                    ),
                            shopId = model.data.shopId ?: "0",
                            isMainContainerSetFitsSystemWindows = false,
                            source = MvcSource.SHOP
                    )
                }
            }
        }
    }

    private fun getReloadDesc(): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(getString(R.string.shop_page_reload))
        spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD), 0, getString(R.string.shop_page_reload).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableStringBuilder()
                .append(getString(R.string.shop_page_reload_beginning_of_description))
                .append(" ")
                .append(spannableStringBuilder)
                .append(" ")
                .append(getString(R.string.shop_page_reload_end_of_description))
    }
}