package com.tokopedia.vouchercreation.create.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Key
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import com.tokopedia.vouchercreation.create.view.enums.CurrencyScale
import com.tokopedia.vouchercreation.create.view.enums.ValueScaleType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
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

    private fun loadPreviewInfo(labelUrl: String, value: Int) {
        Glide.with(context)
                .load(labelUrl)
                .signature(BannerVoucherKey())
                .into(middleLabel)
        getScaledValuePair(value).run {
            middleValueAmount?.text = first
            middleValueScale?.text = second
        }
    }

    private fun loadPreviewInfo(topLabelUrl: String, bottomLabelUrl: String, topValue: Int, bottomValue: Int) {
        Glide.with(context)
                .load(topLabelUrl)
                .signature(BannerVoucherKey())
                .into(topLabel)
        getScaledValuePair(topValue).run {
            topValueAmount?.text = first
        }
        Glide.with(context)
                .load(bottomLabelUrl)
                .signature(BannerVoucherKey())
                .into(bottomLabel)
        getScaledValuePair(bottomValue).run {
            bottomValueAmount?.text = first
            bottomValueScale?.text = second
        }
    }

    private fun getScaledValuePair(value: Int): Pair<String, String> {
        if (value >= CurrencyScale.THOUSAND) {
            return if (value >= CurrencyScale.MILLION) {
                if (value >= CurrencyScale.BILLION) {
                    Pair("", "")
                } else {
                    Pair((value/ CurrencyScale.MILLION).toString(), context.getString(ValueScaleType.MILLION.stringRes).toBlankOrString())
                }
            } else {
                val scaleText = context.getString(ValueScaleType.THOUSAND.stringRes).toBlankOrString()
                Pair((value/ CurrencyScale.THOUSAND).toString(), scaleText)
            }
        } else {
            return if (value > 0) {
                Pair(value.toString(), "")
            } else {
                Pair("", "")
            }
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