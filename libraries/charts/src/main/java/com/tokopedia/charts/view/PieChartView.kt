package com.tokopedia.charts.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.tokopedia.charts.R
import com.tokopedia.charts.config.PieChartConfig
import com.tokopedia.charts.model.PieChartConfigModel
import com.tokopedia.charts.model.PieChartEntry
import com.tokopedia.charts.view.adapter.PieChartLegendAdapter
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.view_pie_chart.view.*

/**
 * Created By @ilhamsuaib on 07/07/20
 */

class PieChartView(
        context: Context,
        attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    companion object {
        const val SIZE_UNDEFINED = -3
    }

    var config: PieChartConfigModel = PieChartConfig.getDefaultConfig()
        private set

    private val legendAdapter by lazy {
        PieChartLegendAdapter()
    }

    init {
        View.inflate(context, R.layout.view_pie_chart, this)
    }

    fun init(config: PieChartConfigModel? = null) {
        config?.let {
            this.config = it
        }

        setupPieChart()
        setupLegend()
    }

    fun setData(chartEntries: List<PieChartEntry>) {
        val chartColors = mutableListOf<Int>()
        val entries: List<PieEntry> = chartEntries.map {
            chartColors.add(Color.parseColor(it.hexColor))
            return@map PieEntry(it.value.toFloat(), it.legend)
        }

        val pieDataSet = PieDataSet(entries, "Pie Data Set")
        with(pieDataSet) {
            setDrawIcons(false)
            sliceSpace = config.sliceSpaceWidth
            colors = chartColors
            selectionShift = 0f
        }

        val mData = PieData(pieDataSet)
        with(mData) {
            setValueTextSize(config.entryLabelTextSize)
            setValueTextColor(config.entryLabelColor)
        }

        mData.dataSets.forEach {
            it.setDrawValues(config.showXValueEnabled)
        }

        pieChart.data = mData

        if (config.legendEnabled) {
            rvPieChartLegend.visible()
            legendAdapter.setLegends(chartEntries)
        } else {
            rvPieChartLegend.gone()
        }

        setOnEmpty(chartEntries)
    }

    private fun setOnEmpty(chartEntries: List<PieChartEntry>) {
        val isEmpty = chartEntries.sumBy { it.value } == 0
        if (isEmpty) {
            pieChart.gone()
            vPieChartEmpty.visible()

            val emptyDrawableRes: Drawable? = context.getResDrawable(R.drawable.shape_charts_pie_empty)
            emptyDrawableRes?.let {
                vPieChartEmpty.background = it
            }
        } else {
            pieChart.visible()
            vPieChartEmpty.gone()
        }
    }

    fun invalidateChart() {
        pieChart.invalidate()
    }

    private fun setupLegend() {
        rvPieChartLegend.run {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = legendAdapter
        }
    }

    private fun setupPieChart() = with(pieChart) {
        description.isEnabled = false
        setDrawCenterText(false)
        setDrawEntryLabels(config.showYValueEnabled)

        isRotationEnabled = config.rotationEnabled
        isHighlightPerTapEnabled = config.highlightPerTapEnabled

        if (config.pieChartWidth != SIZE_UNDEFINED) {
            layoutParams.width = config.pieChartWidth
        }
        if (config.pieChartHeight != SIZE_UNDEFINED) {
            layoutParams.height = config.pieChartHeight
        }
        setTouchEnabled(config.touchEnabled)

        setEntryLabelColor(config.entryLabelColor)
        setEntryLabelTextSize(config.entryLabelTextSize)

        legend.isEnabled = false

        if (config.isHalfChart) {
            maxAngle = 180f
            rotationAngle = 180f
        }

        if (config.animationEnabled) {
            setupAnimation()
        }

        setupDonutChart()
    }

    private fun setupDonutChart() = with(pieChart) {
        if (config.donutStyleConfig.isEnabled) {
            isDrawHoleEnabled = true

            if (config.donutStyleConfig.isCurveEnabled) {
                setDrawSlicesUnderHole(false)
                setDrawRoundedSlices(true)
            }

            holeRadius = config.donutStyleConfig.holeRadius
        } else {
            isDrawHoleEnabled = false
        }
    }

    private fun setupAnimation() = with(pieChart) {
        when {
            (config.xAnimationDuration > 0 && config.yAnimationDuration > 0) -> {
                animateXY(config.xAnimationDuration, config.yAnimationDuration)
            }
            config.xAnimationDuration > 0 -> {
                animateX(config.xAnimationDuration)
            }
            config.yAnimationDuration > 0 -> {
                animateX(config.yAnimationDuration)
            }
        }
    }
}