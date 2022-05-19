package com.tokopedia.affiliate.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.affiliate.model.response.FeeDetailData
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.affiliate_saldo_withdrawal_detail_item_view.view.*

class AffiliateWithdrawalDetailsList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val spacing4 by lazy {
        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4).toDp().toInt()
    }

    init {
        this.orientation = VERTICAL
    }

    fun setData(feeDetailData: ArrayList<FeeDetailData>, title: String?) {
        inflateTitle(title)
        inflateDetailList(feeDetailData)
        inflateDivider()
        inflateAmount(feeDetailData.getOrNull(feeDetailData.size - 1))
    }

    private fun inflateDivider() {
        addView(getLayout(VIEW_TYPE_SEPARATOR))
    }

    private fun inflateTitle(title: String?) {
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = spacing4.toPx()
        layoutParams.bottomMargin = spacing4.toPx()
        val heading = Typography(context)
        heading.setType(Typography.HEADING_4)
        heading.setWeight(Typography.BOLD)
        heading.text = title ?: ""
        heading.layoutParams = layoutParams
        addView(heading)
    }

    private fun inflateAmount(amountData: FeeDetailData?) {
        amountData?.let {
            val view = getLayout()
            setWithdrawalText(view.tvDetailTitle, amountData.feeType)
            setWithdrawalText(view.tvDetailAmount, amountData.amountFormatted ?: "")
            addView(view)
        }
    }

    private fun inflateDetailList(feeDetailData: ArrayList<FeeDetailData>) {
        for (i in 0 until feeDetailData.size - 1) {
            val view = getLayout()
            view.tvDetailTitle.text = feeDetailData[i].feeType
            view.tvDetailAmount.text = feeDetailData[i].amountFormatted ?: ""
            view.tvDetailAmount.visible()
            addView(view)
        }
    }

    fun getLayout(viewType: Int = 1): View {
        val inflater = LayoutInflater.from(context)
        val view = when (viewType) {
            VIEW_TYPE_SEPARATOR -> R.layout.affiliate_saldo_divider_item_view
            else -> R.layout.affiliate_saldo_withdrawal_detail_item_view
        }
        return inflater.inflate(view, this, false)
    }

    private fun setWithdrawalText(textView: Typography, text: String) {
        textView.text = text
        textView.setWeight(Typography.BOLD)
        textView.setType(Typography.BODY_2)
        textView.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        textView.visible()
    }

    companion object {
        const val VIEW_TYPE_DETAIL = 1
        const val VIEW_TYPE_SEPARATOR = 2
    }

}