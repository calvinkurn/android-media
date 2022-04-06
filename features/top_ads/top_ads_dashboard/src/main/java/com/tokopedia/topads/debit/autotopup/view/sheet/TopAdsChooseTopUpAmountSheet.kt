package com.tokopedia.topads.debit.autotopup.view.sheet

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.utils.Utils.calculatePercentage
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.view.adapter.TopAdsAutoTopUpChipsAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 23/9/20.
 */

class TopAdsChooseTopUpAmountSheet : BottomSheetUnify() {

    private var saveBtn: UnifyButton? = null
    private var cancelBtn: UnifyButton? = null
    private var creditOptionsRV: RecyclerView? = null
    private var bonusTxt: Typography? = null
    private var tooltip: ImageUnify? = null
    private var dedAmount: Typography? = null

    private var data: AutoTopUpStatus? = null
    var onCancel: (() -> Unit)? = null
    var onSaved: ((positionSelected: Int) -> Unit)? = null
    var bonus: Double = 0.0
    private val defIndex = 3
    private var dismissedSaved = false

    companion object {
        private const val SPAN_COUNT = 2
        private const val TOPADS_BOTTOM_SHEET_TAG = "CHOOSE_AUTO_TOPUP"
        fun newInstance(): TopAdsChooseTopUpAmountSheet = TopAdsChooseTopUpAmountSheet()
    }

    private val adapter: TopAdsAutoTopUpChipsAdapter? by lazy {
        context?.run { TopAdsAutoTopUpChipsAdapter() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val childView =
            View.inflate(context, R.layout.topads_dash_choose_auto_topup_sheet_layout, null)
        setChild(childView)
        initView(childView)
        setTitle(getString(R.string.topads_dash_topads_auto_topup_bottomsheet_title))
        showCloseIcon = false
    }

    private fun initView(childView: View) {
        saveBtn = childView.findViewById(R.id.saveBtn)
        cancelBtn = childView.findViewById(R.id.cancelBtn)
        creditOptionsRV = childView.findViewById(R.id.creditOptionsRV)
        bonusTxt = childView.findViewById(R.id.bonusTxt)
        tooltip = childView.findViewById(R.id.tooltip)
        dedAmount = childView.findViewById(R.id.dedAmount)
    }

    fun show(fragmentManager: FragmentManager, data: AutoTopUpStatus?, bonus: Double) {
        this.data = data
        this.bonus = bonus
        show(fragmentManager, TOPADS_BOTTOM_SHEET_TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialState()
        adapter?.setChipData(data)
        creditOptionsRV?.adapter = adapter
        creditOptionsRV?.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        setOnDismissListener {
            if (!dismissedSaved) {
                dismissedSaved = false
                onCancel?.invoke()
            }
        }
        cancelBtn?.setOnClickListener {
            dismiss()
            onCancel?.invoke()
        }
        saveBtn?.setOnClickListener {
            dismissedSaved = true
            dismiss()
            onSaved?.invoke(adapter?.getSelected() ?: 0)
        }
        adapter?.setListener(object : TopAdsAutoTopUpChipsAdapter.OnCreditOptionItemClicked {
            override fun onItemClicked(position: Int) {
                data?.let {
                    bonusTxt?.text =
                        Html.fromHtml(String.format(getString(R.string.topads_dash_bonus_String_bottomsheet),
                            "$bonus%",
                            Utils.convertToCurrency(calculatePercentage(it?.availableNominals[position].priceFmt,
                                bonus).toLong())))
                    dedAmount?.text = it.availableNominals[position].minCreditFmt
                }
            }
        })

        tooltip?.setOnClickListener {
            val view1 = View.inflate(context, R.layout.topads_dash_sheet_info, null)
            val bottomSheet = BottomSheetUnify()
            bottomSheet.setTitle(getString(R.string.toapds_dash_tooltip_title))
            bottomSheet.setChild(view1)
            bottomSheet.show(childFragmentManager, "")
        }
    }

    private fun setInitialState() {
        /*def should be 200k*/
        adapter?.setSelected()
        bonusTxt?.text =
            Html.fromHtml(String.format(getString(R.string.topads_dash_bonus_String_bottomsheet),
                "$bonus%",
                Utils.convertToCurrency(calculatePercentage(data?.availableNominals?.get(defIndex)?.priceFmt
                    ?: "1", bonus)
                    .toLong())))
        dedAmount?.text = data?.availableNominals?.get(defIndex)?.minCreditFmt

    }
}