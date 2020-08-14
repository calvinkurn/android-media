package com.tokopedia.charts.config

import com.tokopedia.charts.config.annotation.PieChartDsl
import com.tokopedia.charts.model.DonutStyleConfigModel

/**
 * Created By @ilhamsuaib on 15/07/20
 */

@PieChartDsl
class DonutStyleConfig {

    companion object {
        fun getDefault(): DonutStyleConfigModel = create { }

        fun create(lambda: DonutStyleConfig.() -> Unit): DonutStyleConfigModel {
            return DonutStyleConfig().apply(lambda).build()
        }
    }

    private var isEnabled: Boolean = false
    private var isCurveEnabled: Boolean = false
    private var holeRadius: Float = 50f

    internal fun build(): DonutStyleConfigModel {
        return DonutStyleConfigModel(
                isEnabled = isEnabled,
                isCurveEnabled = isCurveEnabled,
                holeRadius = holeRadius
        )
    }

    /**
     * set this to true to draw the pie center empty. Default false
     */
    fun enabled(lambda: () -> Boolean) {
        this.isEnabled = lambda()
    }

    /**
     * sets whether to draw slices in a curved fashion. Default false
     */
    fun curveEnabled(lambda: () -> Boolean) {
        this.isCurveEnabled = lambda()
    }

    /**
     * sets the radius of the hole in the center of the piechart in percent of
     * the maximum radius (max = the radius of the whole chart), default 50%.
     */
    fun holeRadiusPercent(lambda: () -> Float) {
        this.holeRadius = lambda()
    }
}