package com.tokopedia.vouchercreation.shop.create.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Key
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.vouchercreation.common.view.VoucherCustomView
import com.tokopedia.vouchercreation.databinding.MvcVoucherImagePreviewBinding
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.shop.create.view.enums.getScaledValuePair
import java.security.MessageDigest

class BannerVoucherPreviewInfo @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : VoucherCustomView(context, attrs, defStyleAttr, defStyleRes) {

    private var binding: MvcVoucherImagePreviewBinding = MvcVoucherImagePreviewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setupLayout(binding)
    }

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
        binding.bannerPromoName.text = promoName
    }

    private fun loadPreviewInfo(labelUrl: String, value: Int) {
        binding.apply {
            Glide.with(context)
                .load(labelUrl)
                .signature(BannerVoucherKey())
                .into(middleLabel)
            getScaledValuePair(context, value).run {
                middleValueAmount.text = first
                middleValueScale.text = second
            }
        }
    }

    private fun loadPreviewInfo(topLabelUrl: String, bottomLabelUrl: String, topValue: Int, bottomValue: Int) {
        binding.apply {
            Glide.with(context)
                .load(topLabelUrl)
                .signature(BannerVoucherKey())
                .into(topLabel)
            getScaledValuePair(context, topValue).run {
                topValueAmount.text = first
            }
            Glide.with(context)
                .load(bottomLabelUrl)
                .signature(BannerVoucherKey())
                .into(bottomLabel)
            getScaledValuePair(context, bottomValue).run {
                bottomValueAmount.text = first
                bottomValueScale.text = second
            }
        }
    }

    private fun hideTopBottomValue() {
        binding.apply {
            topLabel.gone()
            topValue.gone()
            bottomLabel.gone()
            bottomValue.gone()
        }
    }

    private fun hideMiddleValue() {
        binding.apply {
            middleLabel.gone()
            middleValue.gone()
        }
    }

    private fun showTopBottomValue() {
        binding.apply {
            topLabel.visible()
            topValue.visible()
            bottomLabel.visible()
            bottomValue.visible()
        }
    }

    private fun showMiddleValue() {
        binding.apply {
            middleLabel.visible()
            middleValue.visible()
        }
    }
}