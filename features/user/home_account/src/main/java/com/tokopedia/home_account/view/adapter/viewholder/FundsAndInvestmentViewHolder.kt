package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FundsAndInvestmentItemWalletBinding
import com.tokopedia.home_account.view.adapter.uimodel.WalletUiModel
import com.tokopedia.home_account.view.listener.WalletListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.view.binding.viewBinding

class FundsAndInvestmentViewHolder(
    private val walletListener: WalletListener,
    view: View
) : BaseViewHolder(view) {

    private val binding: FundsAndInvestmentItemWalletBinding? by viewBinding()

    fun bind(item: WalletUiModel?) {
        setImage(item?.urlImage)
        setTitleText(item?.title)
        setSubtitleText(item?.subtitle)
        setAction(item?.isFailed.orFalse(), item?.isActive.orTrue(), item?.isVertical.orFalse())
        setClickLitener(item?.id,
            item?.applink,
            item?.isFailed.orFalse(),
            item?.isActive.orTrue(),
            walletListener
        )
    }

    private fun setImage(url: String?) {
        url?.let { binding?.image?.setImageUrl(it) }
    }

    private fun setTitleText(text: String? = "") {
        binding?.title?.text = text
    }

    private fun setSubtitleText(text: String?) {
        if (text.isNullOrEmpty()) {
            binding?.subtitle?.gone()
        } else {
            binding?.subtitle?.text = text
        }
    }

    private fun setAction(isFailed: Boolean, isActive: Boolean, isVertical: Boolean) {
        binding?.imageAction?.gone()
        binding?.textAction?.gone()
        when {
            isFailed -> {
                binding?.imageAction?.visible()
                binding?.imageAction?.context?.let {
                    val colorGreen = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                    binding?.imageAction?.setImage(IconUnify.RELOAD, colorGreen, colorGreen)
                }
            }
            !isActive && isVertical -> {
                binding?.subtitle?.gone()
                binding?.textAction?.visible()
                binding?.textAction?.text = getString(R.string.funds_and_investment_actiivate)
            }
            else -> {
                binding?.imageAction?.visible()
                binding?.imageAction?.context?.let {
                    val colorNeutral = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Neutral_N700)
                    binding?.imageAction?.setImage(IconUnify.CHEVRON_RIGHT, colorNeutral, colorNeutral)
                }
            }
        }
    }

    private fun setClickLitener(id: String?,
                                applink: String?,
                                isFailed: Boolean,
                                isActive: Boolean,
                                listener: WalletListener
    ) {
        binding?.container?.setOnClickListener {
            id?.let { id -> listener.onClickWallet(id, applink, isFailed, isActive) }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.funds_and_investment_item_wallet
    }
}