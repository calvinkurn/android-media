package com.tokopedia.saldodetails.commom.utils

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.saldoDetail.coachmark.SaldoCoachMark
import com.tokopedia.unifycomponents.toPx

class SaldoCoachMarkController(private val context: Context, private val expandAppBar: () -> Unit) : CoachMark2.OnStepListener {

    private val coachMark by lazy(LazyThreadSafetyMode.NONE) { CoachMark2(context) }
    private val toolBarHeight by lazy(LazyThreadSafetyMode.NONE) { NavToolbarExt.getFullToolbarHeight(context) }
    private var anchorViewList: ArrayList<View?> = ArrayList()
    private var eligibleSaldoCoachMarkList: List<SaldoCoachMark> = ArrayList()
    private var isSaldoBalanceWidgetReady = false
    private var isSalesTabWidgetReady = false
    private var isBalancePreConditionsFulfilled = false

    private val balanceTitleList = listOf(
        context.getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_buyer),
        context.getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_seller),
    )

    private fun checkIfCoachMarkReady() = isSaldoBalanceWidgetReady && isSalesTabWidgetReady

    fun startCoachMark() {
        if (checkIfCoachMarkReady()) {
            // do not show coach mark if balance widget is not visible
            if (!isBalancePreConditionsFulfilled) return

            eligibleSaldoCoachMarkList = SaldoCoachMark.buildSaldoCoachMarkListByKey(
                context, anchorViewList
            ).filterNot { isSaldoCoachMarkShown(it.coachMarkKey) }

            if (eligibleSaldoCoachMarkList.isEmpty()) return

            val showCoachMarkList = eligibleSaldoCoachMarkList
                .map { it.coachMarkItem }

            coachMark.showCoachMark(ArrayList(showCoachMarkList))
            // first coachmark is shown by default and updated
            // reset coachmark will be updated in onStep callBack
            val firstKey = eligibleSaldoCoachMarkList.getOrNull(0)?.coachMarkKey ?: ""
            updateCoachMarkShown(firstKey)
            coachMark.setStepListener(this)
        }
    }

    fun updateCoachMarkOnScroll(expandLayout: Boolean) {
        if (coachMark.isDismissed.not() && checkIfCoachMarkReady()) {
            val xOffset = (X_OFFSET).toPx()
            val yOffset = 8.toPx()
            val index = getIndexOfBalanceCoachMark()
            if (index in 0..1) {
                updateBalanceCoachMarkItem(xOffset, expandLayout, index)
            } else if (coachMark.currentIndex >= 0) {
                updateSalesCoachMarkItem(xOffset, yOffset)
            }
        }
    }

    private fun updateSalesCoachMarkItem(xOffset: Int, yOffset: Int) {
        val tabView = anchorViewList.getOrNull(PENJUALAN_TAB_INDEX)
        tabView?.post {
            coachMark.update(tabView, xOffset, yOffset, -1, -1)
        }
    }

    private fun updateBalanceCoachMarkItem(
        xOffset: Int,
        expandLayout: Boolean,
        balanceCoachMarkIdx: Int
    ) {
        val view = anchorViewList.getOrNull(balanceCoachMarkIdx)
        val coordinates = intArrayOf(0, 0)
        view?.getLocationOnScreen(coordinates)
        if (expandLayout && (coordinates.getOrNull(1) ?: 0) >= toolBarHeight) {
            handleCoachMarkVisibility(true)
            view?.post {
                coachMark.update(view, xOffset, 0, -1, -1)
            }
        } else handleCoachMarkVisibility(false)
    }

    private fun isSaldoCoachMarkShown(coachMarkKey: String?): Boolean {
        coachMarkKey?.let {
            return CoachMarkPreference.hasShown(context, coachMarkKey)
        } ?: kotlin.run { return false }
    }

    private fun updateCoachMarkShown(coachMarkKey: String?) {
        coachMarkKey?.let {
            CoachMarkPreference.setShown(context, coachMarkKey, true)
        }
    }

    fun handleCoachMarkVisibility(isShow: Boolean) {
        if (isShow) coachMark.contentView.visible()
        else {
            // prevent sales tab coach-mark from being hidden
            val index = getIndexOfBalanceCoachMark()
            if (index in 0..1) {
                coachMark.contentView.gone()
            }
        }
    }

    // since title text is smaller comparison is favoured on title rather than coachmark key
    private fun getIndexOfBalanceCoachMark(): Int {
        val title = coachMark.coachMarkItem.getOrNull(coachMark.currentIndex)?.title ?: ""
        return balanceTitleList.indexOf(title)
    }

    // return true when balik pressed on sales tab coach-mark
    private fun shouldExpandAppBar(key: String) =
        SaldoCoachMark.KEY_CAN_SHOW_PENJUALAN_COACHMARK != key && isSaldoCoachMarkShown(
            SaldoCoachMark.KEY_CAN_SHOW_PENJUALAN_COACHMARK
        )

    fun addBalanceAnchorsForCoachMark(isBalanceShown: Boolean, anchorViewList: List<View?>) {
        if (!isSaldoBalanceWidgetReady) {
            this.anchorViewList.add(0, anchorViewList[0])
            this.anchorViewList.add(1, anchorViewList[1])
        }
        isSaldoBalanceWidgetReady = true
        isBalancePreConditionsFulfilled = isBalanceShown
    }

    fun setSalesTabWidgetReady(anchorView: View?) {
        isSalesTabWidgetReady = true
        anchorViewList.add(anchorView)
    }

    override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
        val key = eligibleSaldoCoachMarkList.getOrNull(currentIndex)?.coachMarkKey ?: ""

        if (shouldExpandAppBar(key)) {
            expandAppBar.invoke()
        }
        if (isSaldoCoachMarkShown(key).not()) {
            updateCoachMarkShown(key)
        }
    }

    companion object {
        const val X_OFFSET = -70
        private const val PENJUALAN_TAB_INDEX = 2
    }

}