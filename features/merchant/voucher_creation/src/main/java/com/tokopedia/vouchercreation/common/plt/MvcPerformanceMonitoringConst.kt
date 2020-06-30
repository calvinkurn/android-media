package com.tokopedia.vouchercreation.common.plt

object MvcPerformanceMonitoringConst {

    object Metrics {
        object Network {
            internal const val VOUCHER_LIST = "mvc_voucher_list_network_metrics"
            internal const val VOUCHER_CREATE = "mvc_voucher_create_network_metrics"
            internal const val VOUCHER_DETAIL = "mvc_voucher_detail_network_metrics"
        }

        object Render {
            internal const val VOUCHER_LIST = "mvc_voucher_list_render_metrics"
            internal const val VOUCHER_CREATE = "mvc_voucher_create_render_metrics"
            internal const val VOUCHER_DETAIL = "mvc_voucher_detail_render_metrics"
        }

        object Prepare {
            internal const val VOUCHER_LIST = "mvc_voucher_list_prepare_metrics"
            internal const val VOUCHER_CREATE = "mvc_voucher_create_prepare_metrics"
            internal const val VOUCHER_DETAIL = "mvc_voucher_detail_prepare_metrics"
        }
    }

    object Traces {
        internal const val VOUCHER_LIST = "mvc_voucher_list_trace"
        internal const val VOUCHER_CREATE = "mvc_voucher_create_trace"
        internal const val VOUCHER_DETAIL = "mvc_voucher_detail_trace"
    }

}