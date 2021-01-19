package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.views.MvcView

import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopHomeVoucherViewHolder(
        itemView: View,
        override val isOwner: Boolean,
        private val shopHomeVoucherViewHolderListener: ShopHomeVoucherViewHolderListener
) : AbstractViewHolder<ShopHomeVoucherUiModel>(itemView), MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener {

    interface ShopHomeVoucherViewHolderListener {
        fun onVoucherItemImpressed(parentPosition: Int, itemPosition: Int, voucher: MerchantVoucherViewModel)
        fun onVoucherSeeAllClicked()
        fun onVoucherClicked(
                parentPosition: Int,
                position: Int,
                merchantVoucherViewModel: MerchantVoucherViewModel
        )
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
    private var merchantVoucherShimmering: LinearLayout? = null
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
                merchantVoucherShimmering?.hide()
                merchantVoucherWidget?.show()
                merchantVoucherReload?.hide()
                merchantVoucherUiModel = model

                merchantVoucherWidget?.setData(MvcData(
                        title = MethodChecker.fromHtml(model.data.titles?.firstOrNull()?.text ?: "").toString(),
                        subTitle = model.data.subTitle ?: "",
                        imageUrl = model.data.imageURL ?: ""
                ))
                merchantVoucherWidget?.shopId = model.data.shopId.toIntOrZero()
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

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {}

    override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {}

    override fun onSeeAllClicked() {
        shopHomeVoucherViewHolderListener.onVoucherSeeAllClicked()
    }

    override fun onVoucherItemImpressed(merchantVoucherViewModel: MerchantVoucherViewModel, voucherPosition: Int) {
        shopHomeVoucherViewHolderListener.onVoucherItemImpressed(
                adapterPosition,
                voucherPosition,
                merchantVoucherViewModel
        )
    }
}