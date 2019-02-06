package com.tokopedia.topads.dashboard.view.fragment


import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.db.williamchart.base.BaseWilliamChartConfig
import com.db.williamchart.base.BaseWilliamChartModel
import com.db.williamchart.config.GrossGraphDataSetConfig
import com.db.williamchart.model.TooltipModel
import com.db.williamchart.renderer.XRenderer
import com.db.williamchart.tooltip.TooltipWithDynamicPointer
import com.db.williamchart.util.TopAdsBaseWilliamChartConfig
import com.db.williamchart.util.TopAdsTooltipConfiguration
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Cell
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.Summary
import kotlinx.android.synthetic.main.partial_statistics_graph_fragment.*

import java.text.SimpleDateFormat

/**
 * Created by hadi.putra on 26/04/18.
 */

abstract class TopAdsDashboardStatisticFragment : TkpdBaseV4Fragment() {
    private val topAdsBaseWilliamChartConfig: TopAdsBaseWilliamChartConfig? by lazy {
        TopAdsBaseWilliamChartConfig()
    }
    private val baseWilliamChartConfig: BaseWilliamChartConfig by lazy {
        BaseWilliamChartConfig()
    }
    internal var summary: Summary = Summary()
    private var cells: List<Cell> = listOf()
    private var mLabels: Array<String> = arrayOf()
    private var mLabelDisplay: ArrayList<TooltipModel> = ArrayList()
    private var mValues: FloatArray = floatArrayOf()

    protected abstract val titleGraph: String

    override fun getScreenName(): String = javaClass.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.partial_statistics_graph_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        content_title_graph.text = titleGraph
    }

    private fun generateLineChart() {
        try {
            content_graph.dismissAllTooltips()
            val indexToDisplay = calculateIndexToDisplay()

            val baseWilliamChartModel = BaseWilliamChartModel(mLabels, mValues)

            content_graph.addDataDisplayDots(mLabelDisplay)
            val tooltip = TooltipWithDynamicPointer(activity,
                    R.layout.item_tooltip_topads, R.id.tooltip_value, R.id.tooltip_title, R.id.tooltip_pointer)
            baseWilliamChartConfig
                    .reset()
                    .addBaseWilliamChartModels(baseWilliamChartModel, GrossGraphDataSetConfig())
                    .setBasicGraphConfiguration(topAdsBaseWilliamChartConfig)
                    .setTooltip(tooltip, TopAdsTooltipConfiguration())
                    .setxRendererListener(XRenderer.XRendererListener { i ->
                        if (mValues.isEmpty()) return@XRendererListener true

                        if (i == 0 || mValues.size - 1 == i)
                            return@XRendererListener true

                        if (mValues.size <= 10) {
                            true
                        } else indexToDisplay.contains(i)

                    })
                    .setDotDrawable(ContextCompat.getDrawable(activity!!, R.drawable.oval_2_copy_6))
                    .buildChart(content_graph)
        } catch (e: Exception) {
        }

    }

    private fun calculateIndexToDisplay(): List<Int> {
        //filter display dot at graph to avoid tight display graph
        val indexToDisplay = ArrayList<Int>()
        val divider = if (mValues.size > 50) 10 else 5
        val divided = mValues.size / divider
        for (j in 1 until divided) {
            indexToDisplay.add(j * divider - 1)
        }
        return indexToDisplay
    }

    fun updateDataStatistic(dataStatistic: DataStatistic?) {
        if (dataStatistic == null) {
            return
        }
        this.summary = dataStatistic.summary
        this.cells = dataStatistic.cells
        mLabels = generateLabels()
        mValues = generateValues()
        mLabelDisplay = generateLabelDisplay()
        generateLineChart()
    }

    protected fun generateLabels(): Array<String> {
        return cells.map { getDate(it) }.toTypedArray()
    }

    private fun getDate(cell: Cell): String {
        val formatterLabel = SimpleDateFormat("dd MMM")
        return formatterLabel.format(cell.date)
    }

    protected fun generateValues(): FloatArray {
        return cells.map { getValueData(it) }.toFloatArray()
    }


    private fun generateLabelDisplay(): ArrayList<TooltipModel> {
        return ArrayList(cells.map { TooltipModel(getDate(it), getValueDisplay(it)) })
    }

    protected abstract fun getValueDisplay(cell: Cell): String

    protected abstract fun getValueData(cell: Cell): Float
}
