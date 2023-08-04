package com.tokopedia.promousage.view.adapter

import android.annotation.SuppressLint
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherCodeBinding
import com.tokopedia.promousage.domain.entity.list.PromoAttemptItem
import com.tokopedia.promousage.util.TextDrawable
import com.tokopedia.promousage.util.composite.DelegateAdapter

internal class PromoAttemptCodeDelegateAdapter(
    private val onAttemptPromoCode: (String) -> Unit
) : DelegateAdapter<PromoAttemptItem, PromoAttemptCodeDelegateAdapter.ViewHolder>(
    PromoAttemptItem::class.java
) {

    companion object {
        private const val SINGLE_LINE = 1
        private const val RIGHT_DRAWABLE_INDEX = 2
        private const val NO_ERROR_MESSAGE = ""
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherCodeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoAttemptItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    internal inner class ViewHolder(
        private val binding: PromoUsageItemVoucherCodeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            setupPromoCodeTextField()
        }

        fun bind(item: PromoAttemptItem) {
            if (item.label.isNotBlank()) {
                binding.tauVoucherCode.setLabel(item.label)
            }
            if (item.attemptedPromoCode.isNotBlank()) {
                binding.tauVoucherCode.editText.setText(item.attemptedPromoCode)
            }
            if (item.promo != null && item.errorMessage.isBlank()) {
                binding.userInputVoucherView.bind(item.promo)
                binding.userInputVoucherView.visible()
            } else {
                binding.userInputVoucherView.gone()
            }
            if (item.errorMessage.isNotBlank()) {
                binding.tauVoucherCode.isInputError = true
                binding.tauVoucherCode.setMessage(item.errorMessage)
                showClearIcon()
            }
            hideUsePromoCodeCta()
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setupPromoCodeTextField() {
            binding.tauVoucherCode.editText.doOnTextChanged { text, start, before, count ->
                hideClearIcon()
                if (text?.length.isZero()) {
                    hideUsePromoCodeCta()
                } else {
                    showUsePromoCodeCta()
                    binding.tauVoucherCode.isInputError = false
                    binding.tauVoucherCode.setMessage(NO_ERROR_MESSAGE)
                }
            }

            binding.tauVoucherCode.editText.maxLines = SINGLE_LINE
            binding.tauVoucherCode.editText.inputType = InputType.TYPE_CLASS_TEXT
            binding.tauVoucherCode.editText.filters = arrayOf(InputFilter.AllCaps())
            binding.tauVoucherCode.editText.setOnTouchListener { _ , motionEvent ->
                val drawables = binding.tauVoucherCode.editText.compoundDrawables
                val rightDrawable = drawables.getOrNull(RIGHT_DRAWABLE_INDEX)
                val hasUseVoucherCodeCta = rightDrawable != null

                if (hasUseVoucherCodeCta && motionEvent.action == MotionEvent.ACTION_UP) {
                    if (motionEvent.rawX >= (binding.tauVoucherCode.editText.right - rightDrawable?.bounds?.width().orZero())) {
                        val attemptedPromoCode = binding.tauVoucherCode.editText.text.toString()
                        onAttemptPromoCode(attemptedPromoCode)
                        return@setOnTouchListener true
                    }
                }

                return@setOnTouchListener false
            }

            binding.tauVoucherCode.clearIconView.setOnClickListener {
                binding.tauVoucherCode.editText.setText("")
            }
        }

        private fun showUsePromoCodeCta() {
            val rightDrawable = TextDrawable(
                binding.tauVoucherCode.context,
                binding.tauVoucherCode.context.getString(R.string.promo_voucher_use)
            )
            binding.tauVoucherCode.editText
                .setCompoundDrawables(null, null, rightDrawable, null)
        }

        private fun hideUsePromoCodeCta() {
            binding.tauVoucherCode.editText
                .setCompoundDrawables(null, null, null, null)
        }

        private fun showClearIcon() {
            binding.tauVoucherCode.clearIconView.visible()
        }

        private fun hideClearIcon() {
            binding.tauVoucherCode.clearIconView.gone()
        }
    }
}
