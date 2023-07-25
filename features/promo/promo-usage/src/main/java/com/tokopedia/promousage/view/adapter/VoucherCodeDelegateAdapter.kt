package com.tokopedia.promousage.view.adapter

import android.annotation.SuppressLint
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherCodeBinding
import com.tokopedia.promousage.domain.entity.list.VoucherCode
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.R
import com.tokopedia.promousage.domain.entity.list.Voucher
import com.tokopedia.promousage.util.TextDrawable

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

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(private val binding: PromoUsageItemVoucherCodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            setupCtaClickListener()
            binding.tauVoucherCode.editText.doOnTextChanged { text , _, _, _ ->
                if (text?.length.isZero()) {
                    hideUseVoucherCodeCta()
                } else {
                    showUseVoucherCodeCta()
                    binding.tauVoucherCode.icon2.gone()
                    binding.tauVoucherCode.isInputError = false
                    binding.tauVoucherCode.setMessage("")
                }
            }
            binding.tauVoucherCode.textInputLayout.editText?.maxLines = SINGLE_LINE
            binding.tauVoucherCode.textInputLayout.editText?.inputType = InputType.TYPE_CLASS_TEXT
        }

        fun bind(item: VoucherCode) {
            val isVoucherCodeValid = item.voucher != null

            binding.tauVoucherCode.editText.setText(item.userInputVoucherCode)

            if (isVoucherCodeValid) {
                handleVoucherSuccess(item.voucher)
            } else {
                handleVoucherError(item.errorMessage)
            }


            if (item.errorMessage.isNotEmpty()) {
                showClearIcon()
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
                tauVoucherCode.isInputError = errorMessage.isNotEmpty()
                tauVoucherCode.setMessage(errorMessage)
            }
        }

        private fun setupCtaClickListener() {
            binding.tauVoucherCode.editText.setOnTouchListener { _, motionEvent ->

                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    if (motionEvent.rawX >= (binding.tauVoucherCode.editText.right - binding.tauVoucherCode.editText.compoundDrawables[2].bounds.width())) {
                        val voucherCode = binding.tauVoucherCode.editText.text.toString()
                        onApplyVoucherCodeCtaClick(voucherCode)
                        return@setOnTouchListener true
                    }
                }

                return@setOnTouchListener false
            }

            binding.tauVoucherCode.clearIconView.setOnClickListener { onVoucherCodeClearIconClick() }
        }


        private fun showUseVoucherCodeCta() {
            val rightDrawable = TextDrawable(
                binding.tauVoucherCode.context,
                binding.tauVoucherCode.context.getString(R.string.promo_voucher_use)
            )
            binding.tauVoucherCode.editText.setCompoundDrawables(
                null,
                null,
                rightDrawable,
                null
            )
        }

        private fun hideUseVoucherCodeCta() {
            binding.tauVoucherCode.editText.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
        }

        private fun showClearIcon() {
            binding.tauVoucherCode.clearIconView.visible()
        }
    }


}
