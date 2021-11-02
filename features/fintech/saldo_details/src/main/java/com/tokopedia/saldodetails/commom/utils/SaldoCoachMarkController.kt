package com.tokopedia.saldodetails.commom.utils

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkContentPosition
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.unifycomponents.toPx

class SaldoCoachMarkController(val context: Context) {

    private val coachMark by lazy { CoachMark2(context) }
    var anchorViewList: ArrayList<View?>? = null

    fun startCoachMark(isSellerEnabled: Boolean) {
        val allCoachMarkList = buildSaldoCoach(anchorViewList)
        val showCoachMarkList = allCoachMarkList.filterNot { isSaldoCoachMarkShown(it.coachMarkKey) }.map { it.coachMarkItem }
        coachMark.showCoachMark(ArrayList(showCoachMarkList))
        // first is shown by and updated
        // reset coachmark will be updated in onStep callBack
        val indexOfFirstCoachMark = allCoachMarkList.size - showCoachMarkList.size
        val firstKey = allCoachMarkList.getOrNull(indexOfFirstCoachMark)?.coachMarkKey
        updateCoachMarkShown(firstKey)
        coachMark.setStepListener(object: CoachMark2.OnStepListener {
            override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                val key = allCoachMarkList.getOrNull(currentIndex)?.coachMarkKey
                if (isSaldoCoachMarkShown(key).not()) {
                    updateCoachMarkShown(key)
                }
            }
        })
    }

    private fun buildSaldoCoach(anchorViewList: ArrayList<View?>?) : ArrayList<SaldoCoachMark> {
        val list = arrayListOf<SaldoCoachMark>()
        anchorViewList?.getOrNull(0)?.let {
            val item = SaldoCoachMark(
                KEY_CAN_SHOW_REFUND_COACHMARK,
                CoachMark2Item(
                    it,
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_buyer),
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_balance_buyer_desc)
                ))
            list.add(item)
        }

        anchorViewList?.getOrNull(1)?.let {
            val item = SaldoCoachMark(
                KEY_CAN_SHOW_INCOME_COACHMARK,
                CoachMark2Item(
                    it,
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_total_balance_seller),
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_intro_description_seller)
                ))
            list.add(item)
        }

        anchorViewList?.getOrNull(2)?.let {
            val item = SaldoCoachMark(
                KEY_CAN_SHOW_PENJUALAN_COACHMARK,
                CoachMark2Item(
                    it,
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_penjualan_coachmark_title),
                    context.getString(com.tokopedia.saldodetails.R.string.saldo_penjualan_coachmark_desc),
                    CoachMarkContentPosition.BOTTOM.position
                ))
            list.add(item)
        }
        return list
    }

    fun updatePenjualanCoachMark() {
        if (coachMark.currentIndex == 2) {
        val xOffset = (X_OFFSET).toPx()
        val yOffset = 8.toPx()
        val tabView = anchorViewList?.getOrNull(2)
        tabView?.post {
            coachMark.update(tabView, xOffset, yOffset, -1, -1)
            }
        }
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

    companion object {
        const val KEY_CAN_SHOW_REFUND_COACHMARK = "com.tokopedia.saldodetails.refund_coach_mark"
        const val KEY_CAN_SHOW_INCOME_COACHMARK = "com.tokopedia.saldodetails.income_coach_mark"
        const val KEY_CAN_SHOW_PENJUALAN_COACHMARK = "penjualan_coach_mark"
        const val X_OFFSET = -70

    }
}

data class SaldoCoachMark(
    val coachMarkKey: String,
    val coachMarkItem: CoachMark2Item
)