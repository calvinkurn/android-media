package com.tokopedia.saldodetails.saldoDetail.coachmark

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkContentPosition
import com.tokopedia.saldodetails.R

data class SaldoCoachMark(
    val coachMarkKey: String,
    val coachMarkItem: CoachMark2Item
) {
    companion object {
        const val KEY_CAN_SHOW_REFUND_COACHMARK = "com.tokopedia.saldodetails.refund_coach_mark"
        const val KEY_CAN_SHOW_INCOME_COACHMARK = "com.tokopedia.saldodetails.income_coach_mark"
        const val KEY_CAN_SHOW_PENJUALAN_COACHMARK = "penjualan_coach_mark"

        fun buildSaldoCoachMarkListByKey(
            context: Context,
            anchorViewList: ArrayList<View?>
        ): ArrayList<SaldoCoachMark> {
            val list = arrayListOf<SaldoCoachMark>()
            anchorViewList.getOrNull(0)?.let {
                val item = SaldoCoachMark(
                    KEY_CAN_SHOW_REFUND_COACHMARK,
                    CoachMark2Item(
                        it,
                        context.getString(R.string.saldo_total_balance_buyer),
                        context.getString(R.string.saldo_balance_buyer_desc)
                    )
                )
                list.add(item)
            }

            anchorViewList.getOrNull(1)?.let {
                val item = SaldoCoachMark(
                    KEY_CAN_SHOW_INCOME_COACHMARK,
                    CoachMark2Item(
                        it,
                        context.getString(R.string.saldo_total_balance_seller),
                        context.getString(R.string.saldo_intro_description_seller)
                    )
                )
                list.add(item)
            }

            anchorViewList.getOrNull(2)?.let {
                val item = SaldoCoachMark(
                    KEY_CAN_SHOW_PENJUALAN_COACHMARK,
                    CoachMark2Item(
                        it,
                        context.getString(R.string.saldo_penjualan_coachmark_title),
                        context.getString(R.string.saldo_penjualan_coachmark_desc),
                        CoachMarkContentPosition.BOTTOM.position
                    )
                )
                list.add(item)
            }
            return list
        }
    }

}