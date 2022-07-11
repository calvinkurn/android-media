package com.tokopedia.digital.home.presentation.customview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.widget.Toast
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
    var actionListener: ActionListener? = null

    var cardHeight: Int = 0

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

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                cardHeight = binding.root.measuredHeight
            }
        })

        setupProductImage()

        binding.root.setOnClickListener {
            actionListener?.onClick()
        }
    }

    private fun setupProductImage() {
        with(binding.tgHorizontalCardProductImage) {
            if (imageUrl.isNotEmpty()) {
                loadImage(imageUrl)
            }

            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)

                    Toast.makeText(context, "Card Height : $cardHeight", Toast.LENGTH_SHORT).show()

                    val ratio: Double =
                        layoutParams.width.toDouble() / layoutParams.height.toDouble()
                    val newWidth = cardHeight
                    val newHeight = newWidth / ratio

                    Toast.makeText(
                        context,
                        "New Width : $newWidth ... New Height : $newHeight",
                        Toast.LENGTH_SHORT
                    ).show()

                    layoutParams.width = newWidth
                    layoutParams.height = newHeight.toInt()
                    requestLayout()
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

    interface ActionListener {
        fun onClick()
    }

}