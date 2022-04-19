package com.tokopedia.chatbot

object ChatbotConstant {

    const val TOKOPEDIA_ATTACH_INVOICE_REQ_CODE = 114
    const val REQUEST_CODE_CHAT_IMAGE = 115
    const val REQUEST_SUBMIT_FEEDBACK = 909
    const val REQUEST_SUBMIT_CSAT = 911
    const val ONE_SECOND_IN_MILLISECONDS = 1000

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
    }

    object ChatbotUnification{
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
}
