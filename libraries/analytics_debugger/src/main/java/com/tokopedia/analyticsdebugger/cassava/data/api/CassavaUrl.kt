package com.tokopedia.analyticsdebugger.cassava.data.api

/**
 * @author by furqan on 07/04/2021
 */
object CassavaUrl {
    const val TOKEN = "eI63F_ZFRdeSE0hKMGVOSMx-WRNxh_-7gV-qZqo30Kg="

    const val DATA_TRACKER_PATH = "data-tracker"

    const val GET_QUERY_LIST = "$DATA_TRACKER_PATH/v1/datalayers"
    const val POST_VALIDATION_RESULT = "$DATA_TRACKER_PATH/v1/test-result/journey/{journeyId}"
}