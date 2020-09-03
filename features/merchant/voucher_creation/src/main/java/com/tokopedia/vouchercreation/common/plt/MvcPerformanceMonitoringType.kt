package com.tokopedia.vouchercreation.common.plt

enum class MvcPerformanceMonitoringType(val prepareMetric: String,
                                        val networkMetric: String,
                                        val renderMetric: String,
                                        val trace: String) {

    Detail(
            MvcPerformanceMonitoringConst.Metrics.Prepare.VOUCHER_DETAIL,
            MvcPerformanceMonitoringConst.Metrics.Network.VOUCHER_DETAIL,
            MvcPerformanceMonitoringConst.Metrics.Render.VOUCHER_DETAIL,
            MvcPerformanceMonitoringConst.Traces.VOUCHER_DETAIL),
    List(
            MvcPerformanceMonitoringConst.Metrics.Prepare.VOUCHER_LIST,
            MvcPerformanceMonitoringConst.Metrics.Network.VOUCHER_LIST,
            MvcPerformanceMonitoringConst.Metrics.Render.VOUCHER_LIST,
            MvcPerformanceMonitoringConst.Traces.VOUCHER_LIST),
    Create(
            MvcPerformanceMonitoringConst.Metrics.Prepare.VOUCHER_CREATE,
            MvcPerformanceMonitoringConst.Metrics.Network.VOUCHER_CREATE,
            MvcPerformanceMonitoringConst.Metrics.Render.VOUCHER_CREATE,
            MvcPerformanceMonitoringConst.Traces.VOUCHER_CREATE
    )
}