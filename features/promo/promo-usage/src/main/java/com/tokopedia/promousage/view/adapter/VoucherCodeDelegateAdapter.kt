package com.tokopedia.promousage.view.adapter

import android.annotation.SuppressLint
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
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

    inner class ViewHolder(private val binding: PromoUsageItemVoucherCodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            setupCta()
            binding.tauVoucherCode.isClearable = false
            binding.tauVoucherCode.textInputLayout.editText?.maxLines = SINGLE_LINE
            binding.tauVoucherCode.textInputLayout.editText?.inputType = InputType.TYPE_CLASS_TEXT
            binding.tauVoucherCode.editText.doOnTextChanged { _, _, _, count ->
                if (count.isMoreThanZero()) {
                    //binding.tauVoucherCode.clearIconView.gone()
                    //binding.tauVoucherCode.icon2.visible()
                    //binding.tauVoucherCode.icon2.setImageResource(R.drawable.promo_usage_ic_use_voucher)
                } else {
                    //binding.tauVoucherCode.icon2.gone()
                }
            }

            binding.tauVoucherCode.clearIconView.setOnClickListener { onVoucherCodeClearIconClick() }
        }

        fun bind(item: VoucherCode) {
            val isVoucherCodeValid = item.voucher != null
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


                tauVoucherCode.isClearable = false
            }
        }

        private fun handleVoucherError(errorMessage: String) {
            binding.run {
                userInputVoucherView.gone()

                //hide pakai CTA


                tauVoucherCode.isInputError = errorMessage.isNotEmpty()
                tauVoucherCode.setMessage(errorMessage)
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setupCta() {
            binding.tauVoucherCode.iconContainer.post {
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

                binding.tauVoucherCode.editText.setOnTouchListener { view, motionEvent ->

                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        if (motionEvent.rawX >= (binding.tauVoucherCode.editText.getRight() - binding.tauVoucherCode.editText.getCompoundDrawables()[2].getBounds().width())) {
                            val voucherCode = binding.tauVoucherCode.editText.text.toString()
                            onApplyVoucherCodeCtaClick(voucherCode)
                            return@setOnTouchListener  true
                        }
                    }

                    return@setOnTouchListener false
                }
            }
        }

    }


}
