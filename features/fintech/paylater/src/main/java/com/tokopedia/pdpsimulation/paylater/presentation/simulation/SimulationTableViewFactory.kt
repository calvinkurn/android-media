package com.tokopedia.pdpsimulation.paylater.presentation.simulation

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.core.content.ContextCompat
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.pdpsimulation.paylater.domain.model.SimulationItemDetail
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterSimulationTenureType
import com.tokopedia.pdpsimulation.paylater.presentation.simulation.widgets.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class SimulationTableViewFactory {

    fun <T : Any> create(context: Context, modelClass: Class<T>, layoutParams: ViewGroup.LayoutParams): T {
        when {
            modelClass.isAssignableFrom(BlankViewTableRowHeader::class.java) ->
                return BlankViewTableRowHeader(context, layoutParams) as T

            modelClass.isAssignableFrom(NoRecommendationViewTableRowHeader::class.java) ->
                return NoRecommendationViewTableRowHeader(context, layoutParams) as T

            modelClass.isAssignableFrom(RecommendationViewTableRowHeader::class.java) ->
                return RecommendationViewTableRowHeader(context, layoutParams) as T

            modelClass.isAssignableFrom(InstallmentViewTableColumnHeader::class.java) ->
                return InstallmentViewTableColumnHeader(context, layoutParams) as T

            modelClass.isAssignableFrom(InstallmentViewTableContent::class.java) ->
                return InstallmentViewTableContent(context, layoutParams) as T

            else -> throw IllegalArgumentException("unknown model class " + modelClass)
        }
    }

    private fun getInstallmentView(
            context: Context,
            contentLayoutParam: ViewGroup.LayoutParams,
            installmentMap: HashMap<PayLaterSimulationTenureType, SimulationItemDetail>,
            tenureList: Array<Int>,
            isRecommendedOption: Boolean,
            col: Int,
    ): View {
        val installmentView = create(context, InstallmentViewTableContent::class.java, contentLayoutParam)
        return installmentView.initUI(installmentMap, tenureList, isRecommendedOption, col)
    }

    private fun getColumnHeader(context: Context, contentLayoutParam: ViewGroup.LayoutParams, position: Int): View {
        val installmentColumnHeader = create(context, InstallmentViewTableColumnHeader::class.java, contentLayoutParam)
        return installmentColumnHeader.initUI(position)
    }

    private fun setRowBackground(
            context: Context,
            isRecommendedInstallment: Boolean,
            showBackGround: Boolean,
            contentRow: TableRow,
    ) {
        if (isRecommendedInstallment) {
            if (showBackGround) contentRow.background = ContextCompat.getDrawable(context, R.drawable.ic_paylater_bg_grey_installment_green_border)
            else contentRow.background = ContextCompat.getDrawable(context, R.drawable.ic_paylater_bg_white_installment_green_border)
        } else {
            if (showBackGround)
                contentRow.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N50))
            else contentRow.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
    }


    fun getBlankView(context: Context, layoutParam: ViewGroup.LayoutParams): View {
        val blankSimulationTableHeading = create(context, BlankViewTableRowHeader::class.java, layoutParam)
        return blankSimulationTableHeading.initUI()
    }

    fun getPayLaterOption(
            context: Context,
            layoutParam: ViewGroup.LayoutParams,
            simulationDataItem: PayLaterSimulationGatewayItem,
            position: Int,
    ): View {
        val showBackGround = showBackground(position)
        return if (simulationDataItem.isRecommended) {
            val recommendationView = create(context, RecommendationViewTableRowHeader::class.java, layoutParam)
            recommendationView.initUI(simulationDataItem, showBackGround)
        } else {
            val noRecommendationViewSimulationTable = create(context, NoRecommendationViewTableRowHeader::class.java, layoutParam)
            noRecommendationViewSimulationTable.initUI(simulationDataItem, showBackGround)
        }
    }

    fun getTableContentView(
            context: Context,
            simulationDataList: ArrayList<PayLaterSimulationGatewayItem>,
            tenureList: Array<Int>,
            rowIdx: Int,
            contentLayoutParam: TableRow.LayoutParams
    ): View {
        val contentRow = TableRow(context)
        val showBackGround = showBackground(rowIdx)
        for (columnIdx in 0 until MAX_INSTALLMENT_COLUMN_COUNT) {
            when (rowIdx) {
                0 -> contentRow.addView(getColumnHeader(context, contentLayoutParam, columnIdx), contentLayoutParam)
                else -> {
                    val isRecommendedOption = simulationDataList[rowIdx - 1].isRecommended
                    setRowBackground(context, isRecommendedOption, showBackGround, contentRow)
                    contentRow.addView(getInstallmentView(context, contentLayoutParam, simulationDataList[rowIdx - 1].installmentMap, tenureList, isRecommendedOption, columnIdx), contentLayoutParam)
                }
            }
        }
        return contentRow
    }

    private fun showBackground(position: Int) = position % 2 == 0

    companion object {
        const val MAX_INSTALLMENT_COLUMN_COUNT = 5

    }

}