package com.tokopedia.promousage.view.adapter

import android.annotation.SuppressLint
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
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
            binding.tauVoucherCode.textInputLayout.editText?.maxLines = SINGLE_LINE
            binding.tauVoucherCode.textInputLayout.editText?.inputType = InputType.TYPE_CLASS_TEXT

        }

        fun bind(item: VoucherCode) {
            val isVoucherCodeValid = item.voucher != null

            binding.tauVoucherCode.editText.setText(item.userInputVoucherCode)
            handleCtaClickListener(item.errorMessage)

            if (isVoucherCodeValid) {
                handleVoucherSuccess(item.voucher)
            } else {
                handleVoucherError(item.errorMessage)
            }

            binding.tauVoucherCode.editText.doOnTextChanged { text , _, _, _ ->
                when {
                    item.errorMessage.isEmpty() && text?.length.isZero() -> {
                        hideCta()
                    }
                    item.errorMessage.isEmpty() && text?.length.isMoreThanZero()-> {
                        showCta()
                        binding.tauVoucherCode.isInputError = false
                        binding.tauVoucherCode.setMessage("")
                    }
                    item.errorMessage.isNotEmpty() ->{
                        showClearIcon()
                        binding.tauVoucherCode.isLoading  = false
                    }
                }
            }

            if (item.errorMessage.isNotEmpty()) {
                showClearIcon()
                binding.tauVoucherCode.isLoading  = false
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

        private fun handleCtaClickListener(errorMessage: String) {
            binding.tauVoucherCode.editText.setOnTouchListener { _, motionEvent ->

                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    if (motionEvent.rawX >= (binding.tauVoucherCode.editText.right - binding.tauVoucherCode.editText.compoundDrawables[2].bounds.width())) {
                        val voucherCode = binding.tauVoucherCode.editText.text.toString()

                        if (errorMessage.isNotEmpty()) {
                            onVoucherCodeClearIconClick()
                        } else {
                            onApplyVoucherCodeCtaClick(voucherCode)
                        }


                        return@setOnTouchListener true
                    }
                }

                return@setOnTouchListener false
            }
        }


        private fun showCta() {
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

        private fun hideCta() {
            binding.tauVoucherCode.editText.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                0,
                0
            )
        }

        private fun showClearIcon() {
            binding.tauVoucherCode.icon2.setImageResource(R.drawable.promo_usage_ic_close_black)
            binding.tauVoucherCode.         clearIconView.visibility = View.VISIBLE
        }
    }


}
