package com.tokopedia.digital.home.presentation.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital.home.databinding.LayoutDigitalHorizontalProductCardBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.util.extension.marginLp
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
    var actionListener: ActionListener? = null

    var imageRatio: Double = 0.0

    init {
        with(binding.tgHorizontalCardProductSlashPrice) {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    fun buildView() {
        setupProductCategory()
        setupProductDetail()
        setupProductPrice()
        setupProductSlashPrice()
        setupProductImage()

        binding.root.setOnClickListener {
            actionListener?.onClick()
        }
    }

    private fun setupProductImage() {
        with(binding.tgHorizontalCardProductImage) {
            if (imageUrl.isNotEmpty()) {
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            imageRatio = resource.width.toDouble() / resource.height.toDouble()

                            binding.tgHorizontalCardProductImage.setImageBitmap(resource)

                            val newWidth = measureContentHeight()
                            val newHeight = newWidth / imageRatio

                            layoutParams.width = newWidth
                            layoutParams.height = newHeight.toInt()
                            requestLayout()
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            }

            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)

                    if (imageRatio > 0.0) {
                        val newWidth = measureContentHeight()
                        val newHeight = newWidth / imageRatio

                        layoutParams.width = newWidth
                        layoutParams.height = newHeight.toInt()
                        requestLayout()
                    }
                }
            })
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

    private fun measureContentHeight(): Int {
        var contentHeight = 0

        contentHeight += binding.tgHorizontalCardProductCategory.measuredHeight
        contentHeight += binding.tgHorizontalCardProductCategory.marginLp.topMargin
        contentHeight += binding.tgHorizontalCardProductCategory.marginLp.bottomMargin

        contentHeight += binding.tgHorizontalCardProductDetail.measuredHeight
        contentHeight += binding.tgHorizontalCardProductDetail.marginLp.topMargin
        contentHeight += binding.tgHorizontalCardProductDetail.marginLp.bottomMargin

        contentHeight += binding.tgHorizontalCardProductPrice.measuredHeight
        contentHeight += binding.tgHorizontalCardProductPrice.marginLp.topMargin
        contentHeight += binding.tgHorizontalCardProductPrice.marginLp.bottomMargin

        return contentHeight
    }

    interface ActionListener {
        fun onClick()
    }

}