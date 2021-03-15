package com.tokopedia.pdpsimulation.paylater.mapper

const val ONE_MONTH_TENURE = 1
const val THREE_MONTH_TENURE = 3
const val SIX_MONTH_TENURE = 6
const val NINE_MONTH_TENURE = 9
const val TWELVE_MONTH_TENURE = 12
const val EMPTY_TENURE = -1

sealed class PayLaterSimulationTenureType(val tenure: Int)
object OneMonthInstallment : PayLaterSimulationTenureType(ONE_MONTH_TENURE)
object ThreeMonthlyInstallment : PayLaterSimulationTenureType(THREE_MONTH_TENURE)
object SixMonthlyInstallment : PayLaterSimulationTenureType(SIX_MONTH_TENURE)
object NineMonthlyInstallment : PayLaterSimulationTenureType(NINE_MONTH_TENURE)
object TwelveMonthlyInstallment : PayLaterSimulationTenureType(TWELVE_MONTH_TENURE)
object EmptyInstallment : PayLaterSimulationTenureType(EMPTY_TENURE)

object PayLaterSimulationResponseMapper {

    fun getSimulationTenureType(tenure: Int?): PayLaterSimulationTenureType {
        return when (tenure) {
            1 -> OneMonthInstallment
            3 -> ThreeMonthlyInstallment
            6 -> SixMonthlyInstallment
            9 -> NineMonthlyInstallment
            12 -> TwelveMonthlyInstallment
            else -> EmptyInstallment
        }
    }
}