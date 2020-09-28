package com.tokopedia.topads.debit.autotopup.view.sheet

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.CreditResponse
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsAutoTopUpChipsAdapter
import com.tokopedia.topads.debit.autotopup.view.fragment.TopAdsEditAutoTopUpFragment.Companion.CREDIT_AMOUNT
import com.tokopedia.topads.debit.autotopup.view.fragment.TopAdsEditAutoTopUpFragment.Companion.MIN_CREDIT_25
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_dash_choose_auto_topup_sheet_layout.*
import kotlinx.android.synthetic.main.topads_dash_choose_topup_layout.*

/**
 * Created by Pika on 23/9/20.
 */

class TopAdsChooseTopUpAmountSheet : BottomSheetUnify() {

    private lateinit var data: CreditResponse
    var onCancel: (() -> Unit)? = null
    var onSaved: ((positionSelected: Int) -> Unit)? = null
    var bonus: Int = 0

    companion object {
        private const val TOPADS_BOTTOM_SHEET_TAG = "CHOOSE_AUTO_TOPUP"
        fun newInstance(): TopAdsChooseTopUpAmountSheet = TopAdsChooseTopUpAmountSheet()
    }

    private val adapter: TopAdsAutoTopUpChipsAdapter? by lazy {
        context?.run { TopAdsAutoTopUpChipsAdapter() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val childView = View.inflate(context, R.layout.topads_dash_choose_auto_topup_sheet_layout, null)
        setChild(childView)
        setTitle(getString(R.string.topads_dash_topads_auto_topup_bottomsheet_title))
        showCloseIcon = false
    }

    fun show(fragmentManager: FragmentManager,
             data: CreditResponse, bonus: Int) {
        this.data = data
        this.bonus = bonus
        show(fragmentManager, TOPADS_BOTTOM_SHEET_TAG)
    }


    private fun setinitialState() {
        dedAmount?.text = MIN_CREDIT_25
        deductionDesc?.visibility = View.GONE
    }

    private fun calculateBonus(productPrice: String): Int {
        val price = productPrice.removeCommaRawString()
        var result = 0
        if (price.isNotEmpty())
            result = (price.toInt() * bonus) / 100
        return result

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (data.credit.firstOrNull()?.productPrice == CREDIT_AMOUNT) {
            setinitialState()
        }
        bonusTxt.text = Html.fromHtml(String.format(getString(R.string.topads_dash_auto_topup_bonus_amount), Utils.convertToCurrency(calculateBonus(data.credit.firstOrNull()?.productPrice
                ?: "1").toLong())))

        adapter?.setChipData(data)
        creditOptionsRV?.adapter = adapter
        creditOptionsRV?.layoutManager = GridLayoutManager(context, 2)

        cancelBtn?.setOnClickListener {
            dismiss()
            onCancel?.invoke()
        }
        saveBtn?.setOnClickListener {
            dismiss()
            onSaved?.invoke(adapter?.getSelected() ?: 0)

        }
        adapter?.setListener(object : TopAdsAutoTopUpChipsAdapter.OnCreditOptionItemClicked {
            override fun onItemClicked(position: Int) {
                bonusTxt.text = Html.fromHtml(String.format(getString(R.string.topads_dash_auto_topup_bonus_amount), Utils.convertToCurrency(calculateBonus(data.credit[position].productPrice).toLong())))
                if (position == 0 && data.credit[position].productPrice == CREDIT_AMOUNT) {
                    setinitialState()
                } else {
                    dedAmount?.text = data.credit[position].minCredit
                    deductionDesc?.visibility = View.VISIBLE
                }
            }
        })
    }
}