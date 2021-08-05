package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.FundsAndInvestmentItemWalletBinding
import com.tokopedia.home_account.view.adapter.uiview.WalletUiView
import com.tokopedia.home_account.view.listener.WalletListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.view.binding.viewBinding

class FundsAndInvestmentViewHolder(
    private val walletListener: WalletListener,
    view: View
) : AbstractViewHolder<WalletUiView>(view) {

    private val binding: FundsAndInvestmentItemWalletBinding? by viewBinding()

    override fun bind(element: WalletUiView?) {
        setImage(element?.urlImage)
        setTitleText(element?.title)
        setSubtitleText(element?.subtitle)
        setAction(element?.actionText, element?.isShowActionImage)
        setClickLitener(walletListener, element?.type)
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

    private fun setAction(text: String?, isButtonShown: Boolean? = true) {
        binding?.imageAction?.gone()
        binding?.textAction?.gone()
        if (isButtonShown == true) {
            binding?.imageAction?.visible()
            binding?.imageAction?.setImageResource(R.drawable.ic_reload)
        } else if (!text.isNullOrEmpty()) {
            binding?.textAction?.visible()
            binding?.textAction?.text = text
        } else {
            binding?.imageAction?.visible()
            binding?.imageAction?.setImageResource(R.drawable.ic_chevron_right)
        }
    }

    private fun setClickLitener(listener: WalletListener, type: String?) {
        binding?.container?.setOnClickListener {
            if (!type.isNullOrEmpty()) {
                listener.onClickWallet(type)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.funds_and_investment_item_wallet
    }
}