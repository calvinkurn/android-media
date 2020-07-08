package com.tokopedia.charts.view

import android.content.Context
import android.content.res.TypedArray
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
import com.tokopedia.charts.config.piechart.PieChartConfigBuilder
import com.tokopedia.charts.config.piechart.model.PieChartConfig
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

    var config: PieChartConfig? = null
        private set

    private val legendAdapter by lazy {
        PieChartLegendAdapter()
    }

    private var pieChartWidth: Int = context.resources.getDimensionPixelSize(R.dimen.charts_140dp)
    private var pieChartHeight: Int = context.resources.getDimensionPixelSize(R.dimen.charts_140dp)

    init {
        View.inflate(context, R.layout.view_pie_chart, this)

        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChartView)
        typedArray.let {
            pieChartWidth = it.getDimensionPixelSize(R.styleable.PieChartView_pcvChartWidth, pieChartWidth)
            pieChartHeight = it.getDimensionPixelSize(R.styleable.PieChartView_pcvChartHeight, pieChartHeight)
        }
        typedArray.recycle()

        pieChart.layoutParams.width = pieChartWidth
        pieChart.layoutParams.height = pieChartHeight
    }

    fun init(config: PieChartConfig = PieChartConfigBuilder.getDefaultConfig()) {
        if (config != this.config) {
            this.config = config
        }

        setupPieChart()
        setupLegend()
    }

    fun setData(chartEntries: List<PieChartEntry>) {
        config?.let { cfg ->
            val chartColors = mutableListOf<Int>()
            val entries: List<PieEntry> = chartEntries.map {
                chartColors.add(Color.parseColor(it.hexColor))
                return@map PieEntry(it.value.toFloat(), it.legend)
            }

            val pieDataSet = PieDataSet(entries, "Pie Data Set")
            with(pieDataSet) {
                setDrawIcons(false)
                sliceSpace = cfg.sliceSpaceWidth
                colors = chartColors
                selectionShift = 0f
            }

            val mData = PieData(pieDataSet)
            with(mData) {
                setValueTextSize(cfg.entryLabelTextSize)
                setValueTextColor(cfg.entryLabelColor)
            }

            mData.dataSets.forEach {
                it.setDrawValues(cfg.showXValueEnabled)
            }

            pieChart.data = mData

            if (cfg.legendEnabled) {
                rvPieChartLegend.visible()
                legendAdapter.setLegends(chartEntries)
            } else {
                rvPieChartLegend.gone()
            }

            setOnEmpty(chartEntries)
        }
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

    private fun setupPieChart() = config?.let { config ->
        with(pieChart) {
            description.isEnabled = false
            setDrawCenterText(false)
            setDrawEntryLabels(config.showYValueEnabled)

            isRotationEnabled = config.rotationEnabled
            isHighlightPerTapEnabled = config.highlightPerTapEnabled
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
    }

    private fun setupDonutChart() = with(pieChart) {
        config?.let { cfg ->
            if (cfg.donutStyleConfig.isEnabled) {
                isDrawHoleEnabled = true

                if (cfg.donutStyleConfig.isCurve) {
                    setDrawSlicesUnderHole(false)
                    setDrawRoundedSlices(true)
                }

                holeRadius = cfg.donutStyleConfig.holeRadius
            } else {
                isDrawHoleEnabled = false
            }
        }
    }

    private fun setupAnimation() = with(pieChart) {
        config?.let { cfg ->
            when {
                (cfg.xAnimationDuration > 0 && cfg.yAnimationDuration > 0) -> {
                    animateXY(cfg.xAnimationDuration, cfg.yAnimationDuration)
                }
                cfg.xAnimationDuration > 0 -> {
                    animateX(cfg.xAnimationDuration)
                }
                cfg.yAnimationDuration > 0 -> {
                    animateX(cfg.yAnimationDuration)
                }
            }
        }
    }
}