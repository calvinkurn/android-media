package com.tokopedia.digital.home.presentation.customview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.home.databinding.LayoutDigitalHorizontalProductCardBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BaseCustomView

class DigitalHorizontalProductCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    val binding: LayoutDigitalHorizontalProductCardBinding =
        LayoutDigitalHorizontalProductCardBinding
            .inflate(LayoutInflater.from(context), this, true)

    var imageUrl: String = ""
    var productCategory: String = ""
    var productDetail: String = ""
    var productPrice: String = ""
    var productSlashPrice: String = ""

    init {
        with(binding.tgHorizontalCardProductSlashPrice) {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    fun buildView() {
        setupProductImage()
        setupProductCategory()
        setupProductDetail()
        setupProductPrice()
        setupProductSlashPrice()
    }

    private fun setupProductImage() {
        with(binding.tgHorizontalCardProductImage) {
            if (imageUrl.isNotEmpty()) {
                loadImage(imageUrl)
            }
        }
    }

    private fun setupProductCategory() {
        with(binding.tgHorizontalCardProductCategory) {
            if (productCategory.isNotEmpty()) {
                text = productCategory
                show()
            } else {
                hide()
            }
        }
    }

    private fun setupProductDetail() {
        with(binding.tgHorizontalCardProductDetail) {
            if (productDetail.isNotEmpty()) {
                text = MethodChecker.fromHtml(productDetail)
                show()
            } else {
                hide()
            }
        }
    }

    private fun setupProductPrice() {
        with(binding.tgHorizontalCardProductPrice) {
            if (productPrice.isNotEmpty()) {
                text = productPrice
                show()
            } else {
                hide()
            }
        }
    }

    private fun setupProductSlashPrice() {
        with(binding.tgHorizontalCardProductSlashPrice) {
            if (productSlashPrice.isNotEmpty()) {
                text = productSlashPrice
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                show()
            } else {
                hide()
            }
        }
    }

}