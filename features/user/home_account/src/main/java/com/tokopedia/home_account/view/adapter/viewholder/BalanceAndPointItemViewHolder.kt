package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.HomeAccountItemBalanceAndPointBinding
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.listener.BalanceAndPointListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.view.binding.viewBinding

class BalanceAndPointItemViewHolder(
    private val balanceAndPointListener: BalanceAndPointListener,
    view: View
) : BaseViewHolder(view) {

    private val binding: HomeAccountItemBalanceAndPointBinding? by viewBinding()

    fun bind(item: BalanceAndPointUiModel?) {
        setImage(item?.urlImage)
        setTitleText(item?.title)
        setSubtitleText(item?.subtitle)
        setClickLitener(balanceAndPointListener, item?.type)
        showShimmerView(item?.isShowShimmer)
    }

    private fun setImage(url: String?) {
        url?.let { binding?.homeAccountItemBalanceAndPointIcon?.setImageUrl(it) }
    }

    private fun setTitleText(text: String? = "") {
        binding?.homeAccountItemBalanceAndPointTitle?.text = text
    }

    private fun setSubtitleText(text: String? = "") {
        binding?.homeAccountItemBalanceAndPointSubtitle?.text = text
    }

    private fun showShimmerView(isShowView: Boolean? = true) {
        if (isShowView == false) {
            binding?.homeAccountBalanceAndPointContaints?.visible()
            binding?.homeAccountBalanceAndPointShimmer?.root?.gone()
        } else {
            binding?.homeAccountBalanceAndPointShimmer?.root?.visible()
            binding?.homeAccountBalanceAndPointContaints?.gone()
        }
    }

    private fun setClickLitener(listener: BalanceAndPointListener, type: String?) {
        binding?.homeAccountBalanceAndPointContaints?.setOnClickListener {
            if (!type.isNullOrEmpty()) {
                listener.onClickBalanceAndPoint(type)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_account_item_balance_and_point
    }
}