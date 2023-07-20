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
import com.tokopedia.utils.view.binding.viewBinding
import java.util.regex.Pattern

class BalanceAndPointItemViewHolder(
    private val balanceAndPointListener: BalanceAndPointListener,
    private val view: View
) : BaseViewHolder(view) {

    private val binding: HomeAccountItemBalanceAndPointBinding? by viewBinding()

    fun bind(item: BalanceAndPointUiModel?) {
        setImage(item?.urlImage)
        setTitleText(item?.id == AccountConstants.WALLET.CO_BRAND_CC,
            item?.isFailed.orFalse(), item?.title, item?.type)
        setSubtitleText(item)
        item?.let {
            setClickLitener(
                it,
                balanceAndPointListener
            )
        }
    }

    fun getSubTitle(): String {
        return binding?.homeAccountItemBalanceAndPointSubtitle?.text.toString()
    }

    private fun setImage(url: String?) {
        url?.let { binding?.homeAccountItemBalanceAndPointIcon?.setImageUrl(it) }
    }

    private fun setTitleText(isCobrand: Boolean, isFailed: Boolean, text: String?, type: String?) {
        val title = if (isFailed) {
            getString(R.string.home_account_balance_and_point_retry)
        } else {
            text
        }

        binding?.homeAccountItemBalanceAndPointTitle?.text = title

        if (isCobrand) {
            type.let {
                if (it == TYPE_LINK) {
                    setTitleColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                } else {
                    setTitleColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950)
                }
            }
        } else {
            if (!checkAnyNumbers(text ?: "")) {
                setTitleColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            }
        }

    }

    private fun checkAnyNumbers(text: String): Boolean {
        val pattern = Pattern.compile(NUMBER_ONLY_REGEX)
        val matcher = pattern.matcher(text)
        return matcher.find()
    }


    private fun setTitleColor(colorId: Int) {
        binding?.homeAccountItemBalanceAndPointTitle?.setTextColor(
            ContextCompat.getColor(
                view.context, colorId
            )
        )
    }

    private fun setSubtitleText(item: BalanceAndPointUiModel?) {
        binding?.homeAccountItemBalanceAndPointSubtitle?.text = if (item?.isFailed == true) {
            item.title
        } else {
            item?.subtitle
        }
        binding?.homeAccountItemBalanceAndPointSubtitle?.setTextColor(
            ContextCompat.getColor(
                view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950
            )
        )
    }

    private fun setClickLitener(
        balanceAndPointUiModel: BalanceAndPointUiModel,
        listener: BalanceAndPointListener
    ) {
        binding?.container?.setOnClickListener {
            listener.onClickBalanceAndPoint(balanceAndPointUiModel)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_account_item_balance_and_point

        private const val NUMBER_ONLY_REGEX = "\\d"
        private const val TYPE_LINK = "link"
    }
}
