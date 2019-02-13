package com.tokopedia.iris

/**
 * @author okasurya on 10/18/18.
 */


//const val STAGING = "https://merlin-staging.tokopedia.com/"
//const val LIVE = "https://merlin.tokopedia.com/"

const val LIVE = "https://hub.tokopedia.com/"
const val STAGING = "http://hub-staging.tokopedia.com/"

const val VERSION = "iris/v1/"

const val BASE_URL ="$STAGING$VERSION"

const val SINGLE_EVENT = "track/dim-event"
const val MULTI_EVENT = "track/dim-event"

const val HEADER_CONTENT_TYPE = "Content-Type"
const val HEADER_USER_ID = "Tkpd-UserId"
const val HEADER_DEVICE = "X-Device"
const val HEADER_JSON = "application/json"
const val HEADER_ANDROID = "android-" + BuildConfig.VERSION_NAME

const val DATABASE_NAME = "iris-db"
const val TABLE_TRACKING = "tracking"

const val SHARED_PREFERENCES = "com.tokopedia.iris.SHARED_PREFERENCES"
const val DOMAIN_HASH = "android-tokopedia"
const val KEY_DOMAIN_HASH = "domain_hash"
const val KEY_USER_ID = "user_id"
const val KEY_DEVICE_ID = "device_id"
const val KEY_SESSION_ID = "session_id"
const val KEY_TIMESTAMP_PREVIOUS = "timestamp_previous"
const val KEY_UUID = "uuid"
const val KEY_INITIAL_VISIT = "initial_visit"

const val WORKER_SEND_DATA = "com.tokopedia.iris.WORKER_SEND_DATA"
const val MAX_ROW = "MAX_ROW"

const val KEY_CONTAINER = "gtm"
const val KEY_EVENT = "default_app"

const val DEFAULT_SERVICE_TIME: Long = 15
const val DEFAULT_MAX_ROW = 50