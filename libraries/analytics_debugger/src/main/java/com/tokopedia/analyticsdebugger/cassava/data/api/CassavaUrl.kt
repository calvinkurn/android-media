package com.tokopedia.analyticsdebugger.cassava.data.api

/**
 * @author by furqan on 07/04/2021
 */
object CassavaUrl {
    const val TOKEN = "yQrDWdx97HRurBY8nvGt_Q6B4zMncJ22DGByVmzUBCI="

    const val DATA_TRACKER_PATH = "data-tracker"

    const val GET_QUERY_LIST = "$DATA_TRACKER_PATH/v1/datalayers"
    const val POST_VALIDATION_RESULT = "$DATA_TRACKER_PATH/v1/test-result/journey/{journeyId}"
}