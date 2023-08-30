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
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemPromoAttemptBinding
import com.tokopedia.promousage.domain.entity.list.PromoAttemptItem
import com.tokopedia.promousage.view.custom.TextDrawable
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx

internal class PromoAttemptCodeDelegateAdapter(
    private val onAttemptPromoCode: (String) -> Unit
) : DelegateAdapter<PromoAttemptItem, PromoAttemptCodeDelegateAdapter.ViewHolder>(
    PromoAttemptItem::class.java
) {

    companion object {
        private const val SINGLE_LINE = 1
        private const val RIGHT_DRAWABLE_INDEX = 2
        private const val NO_ERROR_MESSAGE = ""

        private const val PADDING_TOP_IN_DP = 16
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemPromoAttemptBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoAttemptItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    internal inner class ViewHolder(
        private val binding: PromoUsageItemPromoAttemptBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            setupPromoCodeTextField()
        }

        fun bind(item: PromoAttemptItem) {
            with(binding) {
                if (item.hasOtherSection) {
                    binding.root.setPadding(0, PADDING_TOP_IN_DP.toPx(), 0, 0)
                } else {
                    binding.root.setPadding(0, 0, 0, 0)
                }
                if (item.label.isNotBlank()) {
                    tauVoucherCode.labelText.text =
                        HtmlLinkHelper(tauVoucherCode.context, item.label).spannedString
                }
                if (item.attemptedPromoCode.isNotBlank()) {
                    tauVoucherCode.editText.setText(item.attemptedPromoCode)
                }
                bottomDivider.isVisible = item.promos.isEmpty()
                if (item.errorMessage.isNotBlank()) {
                    tauVoucherCode.isInputError = true
                    tauVoucherCode.setMessage(item.errorMessage)
                    showClearIcon()
                }
                hideUsePromoCodeCta()
            }
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
