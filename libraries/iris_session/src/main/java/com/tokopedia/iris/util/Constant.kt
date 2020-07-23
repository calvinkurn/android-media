package com.tokopedia.iris.util

/**
 * @author okasurya on 10/18/18.
 */

const val VERSION = "iris/v1/"

const val TAG = "P3IRIS-ANDROID"

const val SINGLE_EVENT = "track/dim-event"
const val MULTI_EVENT = "track/dim-event"

const val HEADER_CONTENT_TYPE = "Content-Type"
const val HEADER_USER_ID = "Tkpd-UserId"
const val HEADER_DEVICE = "X-Device"
const val HEADER_JSON = "application/json"
const val HEADER_ANDROID = "android-"

const val DATABASE_NAME = "iris-db"
const val TABLE_TRACKING = "tracking"

const val SHARED_PREFERENCES = "com.tokopedia.iris.SHARED_PREFERENCES"
const val DOMAIN_HASH = "android-tokopedia"
const val KEY_USER_ID = "user_id"
const val KEY_SESSION_ID = "session_id"
const val KEY_SESSION_IRIS = "sessionIris"
const val KEY_TIMESTAMP_LAST_ACTIVITY = "timestamp_last_activity"
const val KEY_INITIAL_VISIT = "initial_visit"

const val MAX_ROW = "MAX_ROW"

const val IRIS_ENABLED = "iris_enabled"
const val REMOTE_CONFIG_IRIS_DB_FLUSH = "android_main_app_line_iris_db_flush"
const val REMOTE_CONFIG_IRIS_DB_SEND = "android_main_app_line_iris_db_send"
const val REMOTE_CONFIG_IRIS_BATCH_SEND = "android_main_app_line_iris_batch_send"

const val KEY_CONTAINER = "gtm"
const val KEY_EVENT = "default_app"
const val KEY_EVENT_SELLERAPP = "default_sellerapp"

const val DEFAULT_CONFIG = "{\"row_limit\":25,\"interval\":2}"
const val JOB_IRIS_ID = 1500
const val DEFAULT_SERVICE_TIME: Long = 2
const val DEFAULT_MAX_ROW = 25
