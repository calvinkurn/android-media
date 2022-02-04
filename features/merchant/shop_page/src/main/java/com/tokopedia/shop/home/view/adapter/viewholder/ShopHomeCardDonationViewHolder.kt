package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeCardDonationBinding
import com.tokopedia.shop.home.view.listener.ShopHomeCardDonationListener
import com.tokopedia.shop.home.view.model.ShopHomeCardDonationUiModel

class ShopHomeCardDonationViewHolder(
    private val binding: ItemShopHomeCardDonationBinding,
    private val listener: ShopHomeCardDonationListener
) : AbstractViewHolder<ShopHomeCardDonationUiModel>(binding.root) {

    override fun bind(element: ShopHomeCardDonationUiModel) {
        with(binding) {
            // render image
            shopHomeCardDonationImage.loadImage(element.header.cover)

            val ctaDescription = SpannableString(element.header.ctaText)
            // set cta color to green
            ctaDescription.setSpan(
                ForegroundColorSpan(
                    MethodChecker.getColor(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                ),
                0,
                ctaDescription.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // set cta typeface to bold
            ctaDescription.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                ctaDescription.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // set cta action
            ctaDescription.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        listener.onCardDonationClick(element.header.ctaLink)
                    }
                },
                0,
                ctaDescription.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // render description
            shopHomeCardDonationDescription.text =
                TextUtils.concat(element.header.title, " ", ctaDescription)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_card_donation
    }
}