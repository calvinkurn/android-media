package com.tokopedia.privacycenter.dsar

import com.tokopedia.privacycenter.common.utils.getSimpleDateFormat
import com.tokopedia.privacycenter.dsar.model.ItemRangeModel
import java.util.*

object DsarUtils {

    fun getInitialRangeItem(): ArrayList<ItemRangeModel> {
        return arrayListOf(
            ItemRangeModel(
                DsarConstants.DATE_RANGE_YEARS, DsarConstants.LABEL_RANGE_YEARS, transactionDate = calculateTransactionDate(
                    DsarConstants.DATE_RANGE_YEARS
                )),
            ItemRangeModel(
                DsarConstants.DATE_RANGE_3_YEARS, DsarConstants.LABEL_RANGE_3_YEARS, transactionDate = calculateTransactionDate(
                    DsarConstants.DATE_RANGE_3_YEARS
                )),
            ItemRangeModel(
                DsarConstants.DATE_RANGE_3_MONTHS, DsarConstants.LABEL_RANGE_3_MONTHS, transactionDate = calculateTransactionDate(
                    DsarConstants.DATE_RANGE_3_MONTHS
                )),
            ItemRangeModel(
                DsarConstants.DATE_RANGE_30_DAYS, DsarConstants.LABEL_RANGE_30_DAYS, transactionDate = calculateTransactionDate(
                    DsarConstants.DATE_RANGE_30_DAYS
                )),
            ItemRangeModel(
                DsarConstants.DATE_RANGE_WEEKLY, DsarConstants.LABEL_RANGE_WEEKLY, transactionDate = calculateTransactionDate(
                    DsarConstants.DATE_RANGE_WEEKLY
                )),
            ItemRangeModel(
                DsarConstants.DATE_RANGE_CUSTOM, DsarConstants.LABEL_RANGE_CUSTOM, transactionDate = calculateTransactionDate(
                    DsarConstants.DATE_RANGE_CUSTOM
                ))
        )
    }

    fun calculateTransactionDate(selectedId: Int): String {
        val maxDate = GregorianCalendar(Locale.getDefault())
        val minDate = GregorianCalendar(Locale.getDefault())
        when(selectedId) {
            DsarConstants.DATE_RANGE_YEARS -> {
                minDate.apply {
                    set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY)
                    set(GregorianCalendar.DAY_OF_MONTH, 1)
                }
            }
            DsarConstants.DATE_RANGE_3_YEARS -> {
                minDate.apply {
                    add(Calendar.YEAR, -3)
                }
            }
            DsarConstants.DATE_RANGE_3_MONTHS -> {
                minDate.apply {
                    add(Calendar.MONTH, -3)
                }
            }
            DsarConstants.DATE_RANGE_30_DAYS -> {
                minDate.apply {
                    add(Calendar.DAY_OF_MONTH, -30)
                }
            }
            DsarConstants.DATE_RANGE_WEEKLY -> {
                minDate.apply {
                    add(Calendar.DAY_OF_MONTH, -7)
                }
            }
        }
        val formatter = getSimpleDateFormat(DsarConstants.ONE_TRUST_FORMAT_1)
        val formattedMaxDate = formatter.format(maxDate.time)
        val formattedMinDate = formatter.format(minDate.time)
        return "${DsarConstants.TRANSACTION_HISTORY_PREFIX}_${formattedMinDate}_${formattedMaxDate}"
    }
}
