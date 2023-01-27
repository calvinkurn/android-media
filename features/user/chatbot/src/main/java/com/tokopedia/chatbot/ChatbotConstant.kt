package com.tokopedia.chatbot

import com.tokopedia.url.TokopediaUrl
import java.util.*

object ChatbotConstant {

    const val TOKOPEDIA_ATTACH_INVOICE_REQ_CODE = 114
    const val REQUEST_CODE_CHAT_IMAGE = 115
    const val REQUEST_CODE_CHAT_VIDEO = 116
    const val REQUEST_SUBMIT_FEEDBACK = 909
    const val REQUEST_SUBMIT_CSAT = 911
    const val REQUEST_CODE_CHATBOT_ONBOARDING = 117
    const val ONE_SECOND_IN_MILLISECONDS = 1000
    const val MODE_AGENT = "agent"
    const val MODE_BOT = "bot"
    const val TOKOPEDIA_CARE = "Tokopedia Care"
    const val TANYA = "Tanya"
    const val RENDER_INVOICE_LIST_AND_BUTTON_ACTION = 1
    const val SOURCE_CHATBOT = "chatbot"

    object CsatRating {
        const val RATING_ONE = 1
        const val RATING_TWO = 2
        const val RATING_THREE = 3
        const val RATING_FOUR = 4
        const val RATING_FIVE = 5
    }

    object ImageUpload {
        const val MAX_FILE_SIZE = 5120
        const val MAX_FILE_SIZE_UPLOAD_SECURE = 9216
        const val MINIMUM_HEIGHT = 100
        const val MINIMUM_WIDTH = 300
        const val DEFAULT_ONE_MEGABYTE: Long = 1024
    }

    object SecureImageUpload {
        const val POST = "POST"
        const val CONTENT_TYPE = "contentTypeHeader"
        const val DATE_FORMAT = "dd MMM yy HH:mm ZZZ"
        const val AUTHORIZATION = "Authorization"
        const val TKPD_USERID = "Tkpd-UserId"
        const val X_USER_ID = "X-User-ID"
        const val X_APP_VERSION = "X-App-Version"
        const val X_DEVICE = "X-Device"
    }

    object AttachmentType {
        const val TYPE_SECURE_IMAGE_UPLOAD = "26"
        const val TYPE_VIDEO_UPLOAD = "30"
        const val SESSION_CHANGE = "31"
        const val TYPE_REPLY_BUBBLE = "28"
    }

    object ReplyBoxType {
        const val DYNAMIC_ATTACHMENT = "34"
        const val TYPE_BIG_REPLY_BOX = 100
        const val REPLY_BOX_TOGGLE_VALUE = 101
        val ALLOWED_DYNAMIC_ATTACHMENT_TYPE = listOf<Int>(TYPE_BIG_REPLY_BOX, REPLY_BOX_TOGGLE_VALUE)
    }

    object ChatbotUnification {
        const val ARTICLE_ID = "articleId"
        const val ARTICLE_TITLE = "articleTitle"
        const val CODE = "code"
        const val CREATE_TIME = "create_time"
        const val DESCRIPTION = "description"
        const val EVENT = "event"
        const val ID = "id"
        const val IMAGE_URL = "image_url"
        const val IS_ATTACHED = "is_attached"
        const val STATUS = "status"
        const val STATUS_COLOR = "status_color"
        const val STATUS_ID = "status_id"
        const val TITLE = "title"
        const val TOTAL_AMOUNT = "total_amount"
        const val USED_BY = "used_by"
        const val ARTICLE_ENTRY = "article_entry"
        const val FALSE = "false"
    }

    object NewRelic {
        // New Relic Key For Logging
        const val KEY_CHATBOT_ERROR = "CHATBOT_ANDROID_ERROR"

        // Keys for each GQL
        const val KEY_SECURE_UPLOAD = "CHATBOT_SECURE_UPLOAD"
        const val KEY_CHATBOT_SECURE_UPLOAD_AVAILABILITY = "CHATBOT_SECURE_UPLOAD_AVAILABILITY"
        const val KEY_CHATBOT_GET_CHATLIST_RATING = "CHATBOT_GET_CHATLIST_RATING"
        const val KEY_CHATBOT_SUBMIT_CHAT_CSAT = "CHATBOT_SUBMIT_CHAT_CSAT"
        const val KEY_CHATBOT_SUBMIT_HELPFULL_QUESTION = "CHATBOT_SUBMIT_HELPFULL_QUESTION"
        const val KEY_CHATBOT_GET_EXISTING_CHAT_FIRST_TIME = "CHATBOT_GET_EXISTING_CHAT_FIRST_TIME"
        const val KEY_CHATBOT_GET_EXISTING_CHAT_TOP = "CHATBOT_GET_EXISTING_CHAT_TOP"
        const val KEY_CHATBOT_GET_EXISTING_CHAT_BOTTOM = "CHATBOT_GET_EXISTING_CHAT_BOTTOM"
        const val KEY_CHATBOT_GET_LINK_FOR_REDIRECTION = "CHATBOT_GET_LINK_FOR_REDIRECTION"
        const val KEY_CHATBOT_TICKER = "CHATBOT_TICKER"
        const val KEY_CHATBOT_NEW_SESSION = "CHATBOT_NEW_SESSION"
        const val KEY_CHATBOT_SEND_RATING = "CHATBOT_SEND_RATING"
        const val KEY_CHATBOT_CSAT_RATING = "CHATBOT_CSAT_RATING"
        const val KEY_CHATBOT_SOCKET_EXCEPTION = "CHATBOT_SOCKET_EXCEPTION"
    }

    const val CONTACT_US_APPLINK = "tokopedia-android-internal://customercare-inbox-list"
    const val VIDEO_URL = "chatbot-video-url"

    object VideoUpload {
        const val SOURCE_ID_FOR_VIDEO_UPLOAD = "fLapDZ"
        const val MAX_VIDEO_COUNT = 5
        const val MAX_IMAGE_COUNT = 3
        const val MAX_MEDIA_ITEM_COUNT = 0
        const val MAX_DURATION_FOR_VIDEO = 300000
    }

    object SecureImageUploadUrl {

        private val BASE_URL = TokopediaUrl.getInstance().CHAT

        private const val UPLOAD_SECURE_PATH = "/tc/v1/upload_secure"

        fun getUploadSecureUrl(): String {
            return BASE_URL + UPLOAD_SECURE_PATH
        }
    }

    object ChatbotUrl {
        fun getPathWebsocket(deviceId: String?, userId: String?): String {
            return String.format(
                Locale.getDefault(),
                "%s%s?os_type=1&device_id=%s&user_id=%s&source=chatbot",
                WEBSOCKET_URL,
                CONNECT_WEBSOCKET,
                deviceId,
                userId
            )
        }

        private const val CONNECT_WEBSOCKET = "/connect"
        val WEBSOCKET_URL = TokopediaUrl.getInstance().WS_CHAT
    }
}
