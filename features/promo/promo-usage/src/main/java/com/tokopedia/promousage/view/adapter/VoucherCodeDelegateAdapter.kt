package com.tokopedia.promousage.view.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherCodeBinding
import com.tokopedia.promousage.domain.entity.list.VoucherCode
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.R
import com.tokopedia.promousage.domain.entity.list.Voucher
import com.tokopedia.unifyprinciples.Typography

class VoucherCodeDelegateAdapter(
    private val onApplyVoucherCodeCtaClick: (String) -> Unit,
    private val onVoucherCodeClearIconClick: () -> Unit
) : DelegateAdapter<VoucherCode, VoucherCodeDelegateAdapter.ViewHolder>(VoucherCode::class.java) {

    companion object {
        private const val SINGLE_LINE = 1
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherCodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: VoucherCode, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: PromoUsageItemVoucherCodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tauVoucherCode.textInputLayout.editText?.maxLines = SINGLE_LINE
            binding.tauVoucherCode.textInputLayout.editText?.inputType = InputType.TYPE_CLASS_TEXT
            binding.tauVoucherCode.icon2.setImageResource(R.drawable.promo_usage_ic_use_voucher)
            binding.tauVoucherCode.icon2.setOnClickListener {
                val userInputVoucherCode = binding.tauVoucherCode.editText.text.toString()
                onApplyVoucherCodeCtaClick(userInputVoucherCode)
            }
            binding.tauVoucherCode.isClearable = true
            binding.tauVoucherCode.editText.doOnTextChanged { _, _, _, count ->
                if (count.isMoreThanZero()) {
                    binding.tauVoucherCode.clearIconView.gone()
                    binding.tauVoucherCode.icon2.visible()
                    binding.tauVoucherCode.icon2.setImageResource(R.drawable.promo_usage_ic_use_voucher)
                } else {
                    binding.tauVoucherCode.icon2.gone()
                }
            }

            binding.tauVoucherCode.clearIconView.setOnClickListener { onVoucherCodeClearIconClick() }
        }

        fun bind(item: VoucherCode) {
            val isVoucherCodeValid = item.success && item.voucher != null
            if (isVoucherCodeValid) {
                handleVoucherSuccess(item.voucher)
            } else {
                handleVoucherError(item.errorMessage)
            }
        }

        private fun handleVoucherSuccess(voucher: Voucher?) {
            binding.run {
                if (voucher != null) {
                    userInputVoucherView.visible()
                    userInputVoucherView.bind(voucher)
                }
                tauVoucherCode.isInputError = false
                tauVoucherCode.setMessage("")
            }
        }

        private fun handleVoucherError(errorMessage: String) {
            binding.run {
                userInputVoucherView.gone()

                //hide pakai CTA

                tauVoucherCode.clearIconView.visible()
                tauVoucherCode.isInputError = true
                tauVoucherCode.setMessage(errorMessage)
            }
        }

        fun createDrawableFromString(context: Context, text: String): Drawable {
            // Set up the paint for drawing text
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            paint.textSize = 14f
            paint.typeface = Typography.getFontType(context, true, Typography.PARAGRAPH_2)
            paint.isAntiAlias = true

            // Calculate the width and height of the text based on the paint settings
            val width = paint.measureText(text).toInt()
            val height = paint.fontMetrics.bottom.toInt()

            // Create a bitmap with the calculated width and height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            // Create a canvas from the bitmap
            val canvas = Canvas(bitmap)

            // Draw the text on the canvas
            canvas.drawText(text, 0f, height.toFloat(), paint)

            // Create a BitmapDrawable from the bitmap and return it
            return BitmapDrawable(context.resources, bitmap)
        }

    }


}
