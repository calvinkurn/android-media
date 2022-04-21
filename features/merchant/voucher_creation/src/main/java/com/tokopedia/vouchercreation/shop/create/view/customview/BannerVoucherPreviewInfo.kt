package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Key
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.shop.create.view.enums.getScaledValuePair
import kotlinx.android.synthetic.main.mvc_voucher_image_preview.view.*
import java.security.MessageDigest

class BannerVoucherPreviewInfo @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        @LayoutRes layoutResource: Int = R.layout.mvc_voucher_image_preview)
    : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes, layoutResource) {

    inner class BannerVoucherKey: Key {
        override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
    }

    override fun setupAttributes() {}

    override fun setupView() {}

    fun setPreviewInfo(voucherImageType: VoucherImageType, labelUrl: String, secondaryLabelUrl: String? = null) {
        when(voucherImageType) {
            is VoucherImageType.FreeDelivery -> {
                hideTopBottomValue()
                loadPreviewInfo(labelUrl, voucherImageType.value)
                showMiddleValue()
            }
            is VoucherImageType.Rupiah -> {
                hideTopBottomValue()
                loadPreviewInfo(labelUrl, voucherImageType.value)
                showMiddleValue()
            }
            is VoucherImageType.Percentage -> {
                hideMiddleValue()
                secondaryLabelUrl?.let { bottomLabelUrl ->
                    loadPreviewInfo(labelUrl, bottomLabelUrl, voucherImageType.percentage, voucherImageType.value)
                }
                showTopBottomValue()
            }
        }
    }

    fun setPromoName(promoName: String) {
        bannerPromoName?.text = promoName
    }

    private fun loadPreviewInfo(labelUrl: String, value: Int) {
        Glide.with(context)
                .load(labelUrl)
                .signature(BannerVoucherKey())
                .into(middleLabel)
        getScaledValuePair(context, value).run {
            middleValueAmount?.text = first
            middleValueScale?.text = second
        }
    }

    private fun loadPreviewInfo(topLabelUrl: String, bottomLabelUrl: String, topValue: Int, bottomValue: Int) {
        Glide.with(context)
                .load(topLabelUrl)
                .signature(BannerVoucherKey())
                .into(topLabel)
        getScaledValuePair(context, topValue).run {
            topValueAmount?.text = first
        }
        Glide.with(context)
                .load(bottomLabelUrl)
                .signature(BannerVoucherKey())
                .into(bottomLabel)
        getScaledValuePair(context, bottomValue).run {
            bottomValueAmount?.text = first
            bottomValueScale?.text = second
        }
    }

    private fun hideTopBottomValue() {
        topLabel?.gone()
        topValue?.gone()
        bottomLabel?.gone()
        bottomValue?.gone()
    }

    private fun hideMiddleValue() {
        middleLabel?.gone()
        middleValue?.gone()
    }

    private fun showTopBottomValue() {
        topLabel?.visible()
        topValue?.visible()
        bottomLabel?.visible()
        bottomValue?.visible()
    }

    private fun showMiddleValue() {
        middleLabel?.visible()
        middleValue?.visible()
    }
}