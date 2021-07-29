package com.tokopedia.saldodetails.design

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.R
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.saldo_withdrawal_detail_item_view.view.*

class WithdrawalDetailsList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val spacing4 by lazy {
        resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_4).toDp().toInt()
    }

    val demo = arrayListOf(
        Pair("Jumlah Penarikan", "Rp600.000"),
        Pair("Biaya Penarikan", "-Rp5.000"),
        Pair("Jumlah Penarikan", "Rp600.00"),
    )

    init {
        this.orientation = VERTICAL
    }

    fun setData() {
        inflateTitle()
        inflateDetailList()
        inflateDivider()
        inflateAmount()
    }

    private fun inflateDivider() {
        addView(getLayout(VIEW_TYPE_SEPARATOR))
    }

    private fun inflateTitle() {
        val layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = spacing4.toPx() // Small hack to make custom label center
        layoutParams.bottomMargin = spacing4.toPx()
        val heading = Typography(context)
        heading.setType(Typography.HEADING_4)
        heading.setWeight(Typography.BOLD)
        heading.text = "Rincian penarikan"
        heading.layoutParams = layoutParams
        addView(heading)
    }

    private fun inflateAmount() {
        val view = getLayout()
        setWithdrawalText(view.tvDetailTitle, "Jumlah yang Ditransfer")
        setWithdrawalText(view.tvDetailAmount, "Rp595.000")
        addView(view)
    }

    private fun inflateDetailList() {
        demo.forEach {
            val view = getLayout()
            view.tvDetailTitle.text = it.first
            view.tvDetailAmount.text = it.second
            view.tvDetailAmount.visible()
            addView(view)
        }
    }

    fun getLayout(viewType: Int = 1): View {
        val inflater = LayoutInflater.from(context)
        val view = when (viewType) {
            VIEW_TYPE_SEPARATOR -> R.layout.saldo_divider_item_view
            else -> R.layout.saldo_withdrawal_detail_item_view
        }
        return inflater.inflate(view, this, false)
    }

    private fun setWithdrawalText(textView: Typography, text: String) {
        textView.text = text
        textView.setWeight(Typography.BOLD)
        textView.setType(Typography.BODY_2)
        textView.setTextColor(ContextCompat.getColor(context, R.color.Unify_N700_96))
        textView.visible()
    }
    companion object {
        const val VIEW_TYPE_DETAIL = 1
        const val VIEW_TYPE_SEPARATOR = 2
    }

}