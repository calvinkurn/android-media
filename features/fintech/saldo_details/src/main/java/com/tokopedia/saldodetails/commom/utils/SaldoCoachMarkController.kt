package com.tokopedia.saldodetails.commom.utils

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkContentPosition
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toPx

class SaldoCoachMarkController(val context: Context) {

    private val coachMark by lazy { CoachMark2(context) }
    private val toolBarHeight by lazy { NavToolbarExt.getFullToolbarHeight(context) }
    var anchorViewList: ArrayList<View?> = ArrayList()
    var eligibleSaldoCoachMarkList: List<SaldoCoachMark> = ArrayList()
    var isSaldoBalanceWidgetReady = false
    var isSalesTabWidgetReady = false
    var balancePreConditions = false

    private val balanceTitleList = listOf(
        context.getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_buyer),
        context.getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_seller),
    )

    private fun checkIfCoachMarkReady() =
        isSaldoBalanceWidgetReady && isSalesTabWidgetReady

    fun startCoachMark(expandAppBar: () -> Unit) {
        CoachMarkPreference.setShown(context, KEY_CAN_SHOW_PENJUALAN_COACHMARK, false)
        CoachMarkPreference.setShown(context, KEY_CAN_SHOW_INCOME_COACHMARK, false)
        CoachMarkPreference.setShown(context, KEY_CAN_SHOW_REFUND_COACHMARK, false)

        if (checkIfCoachMarkReady()) {
            val allCoachMarkList = buildSaldoCoachMarkListByKey(anchorViewList)

            // do not show coach mark if balance widget is not visible
            if (!balancePreConditions)  {
                return
            }
            eligibleSaldoCoachMarkList =
                allCoachMarkList.filterNot { isSaldoCoachMarkShown(it.coachMarkKey) }

            val showCoachMarkList = eligibleSaldoCoachMarkList
                    .map { it.coachMarkItem }
            coachMark.showCoachMark(ArrayList(showCoachMarkList))
            // first is shown by and updated
            // reset coachmark will be updated in onStep callBack
            val indexOfFirstCoachMark = allCoachMarkList.size - showCoachMarkList.size
            val firstKey = allCoachMarkList.getOrNull(indexOfFirstCoachMark)?.coachMarkKey
            updateCoachMarkShown(firstKey)
            coachMark.setStepListener(object : CoachMark2.OnStepListener {
                override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                    val key = eligibleSaldoCoachMarkList.getOrNull(currentIndex)?.coachMarkKey ?: ""

                    if (shouldExpandAppBar(key)) {
                        expandAppBar.invoke()
                    }
                    if (isSaldoCoachMarkShown(key).not()) {
                        updateCoachMarkShown(key)
                    }
                }
            })
        }
    }


    private fun buildSaldoCoachMarkListByKey(anchorViewList: ArrayList<View?>): ArrayList<SaldoCoachMark> {
        val list = arrayListOf<SaldoCoachMark>()
        anchorViewList.getOrNull(0)?.let {
            val item = SaldoCoachMark(
                KEY_CAN_SHOW_REFUND_COACHMARK,
                CoachMark2Item(
                    it,
                    balanceTitleList.getOrNull(0) ?: "",
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_balance_buyer_desc)
                )
            )
            list.add(item)
        }

        anchorViewList.getOrNull(1)?.let {
            val item = SaldoCoachMark(
                KEY_CAN_SHOW_INCOME_COACHMARK,
                CoachMark2Item(
                    it,
                    balanceTitleList.getOrNull(1) ?: "",
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_intro_description_seller)
                )
            )
            list.add(item)
        }

        anchorViewList.getOrNull(2)?.let {
            val item = SaldoCoachMark(
                KEY_CAN_SHOW_PENJUALAN_COACHMARK,
                CoachMark2Item(
                    it,
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_penjualan_coachmark_title),
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_penjualan_coachmark_desc),
                    CoachMarkContentPosition.BOTTOM.position
                )
            )
            list.add(item)
        }
        return list
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

    private fun updateBalanceCoachMarkItem(xOffset: Int, expandLayout: Boolean, balanceCoachMarkIdx: Int) {
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

    private fun getIndexOfBalanceCoachMark(): Int {
        val title = coachMark.coachMarkItem.getOrNull(coachMark.currentIndex)?.title ?: ""
        return balanceTitleList.indexOf(title)
    }
    // return true when balik pressed on sales tab coach-mark
    private fun shouldExpandAppBar(key: String) =
        KEY_CAN_SHOW_PENJUALAN_COACHMARK != key && isSaldoCoachMarkShown(
            KEY_CAN_SHOW_PENJUALAN_COACHMARK
        )

    companion object {
        const val KEY_CAN_SHOW_REFUND_COACHMARK = "com.tokopedia.saldodetails.refund_coach_mark"
        const val KEY_CAN_SHOW_INCOME_COACHMARK = "com.tokopedia.saldodetails.income_coach_mark"
        const val KEY_CAN_SHOW_PENJUALAN_COACHMARK = "penjualan_coach_mark"
        const val X_OFFSET = -70
        private const val PENJUALAN_TAB_INDEX = 2
    }
}

data class SaldoCoachMark(
    val coachMarkKey: String,
    val coachMarkItem: CoachMark2Item
)