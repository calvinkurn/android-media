package com.tokopedia.home_account.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.HomeAccountItemBalanceAndPointBinding
import com.tokopedia.home_account.view.adapter.uimodel.BalanceAndPointUiModel
import com.tokopedia.home_account.view.listener.BalanceAndPointListener
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.utils.view.binding.viewBinding

class BalanceAndPointItemViewHolder(
    private val balanceAndPointListener: BalanceAndPointListener,
    private val view: View
) : BaseViewHolder(view) {

    private val binding: HomeAccountItemBalanceAndPointBinding? by viewBinding()

    fun bind(item: BalanceAndPointUiModel?) {
        setImage(item?.urlImage)
        setTitleText(item?.isFailed.orFalse(), item?.title)
        setSubtitleText(item?.subtitle)
        setClickLitener(
            item?.id,
            item?.applink,
            item?.weblink,
            item?.isFailed.orFalse(),
            item?.isActive.orTrue(),
            balanceAndPointListener
        )
    }

    private fun setImage(url: String?) {
        url?.let { binding?.homeAccountItemBalanceAndPointIcon?.setImageUrl(it) }
    }

    private fun setTitleText(isFailed: Boolean, text: String?) {
        val title = if (isFailed) {
            getString(R.string.home_account_balance_and_point_retry)
        } else {
            text
        }

        binding?.homeAccountItemBalanceAndPointTitle?.text = title

        when (text) {
            getString(R.string.home_account_balance_and_point_activate),
            getString(R.string.home_account_balance_and_point_add_point),
            getString(R.string.home_account_balance_and_point_retry) -> {
                setTitleToGreen()
            }
        }
    }

    private fun setTitleToGreen() {
        binding?.homeAccountItemBalanceAndPointTitle?.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.Unify_G500
            )
        )
    }

    private fun setSubtitleText(text: String?) {
        binding?.homeAccountItemBalanceAndPointSubtitle?.text = text
    }

    private fun setClickLitener(
        id: String?,
        applink: String?,
        weblink: String?,
        isFailed: Boolean,
        isActive: Boolean,
        listener: BalanceAndPointListener
    ) {
        binding?.container?.setOnClickListener {
            id?.let { id -> listener.onClickBalanceAndPoint(id, applink, weblink, isFailed, isActive) }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_account_item_balance_and_point
    }
}