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
        const val MINIMUM_HEIGHT = 100
        const val MINIMUM_WIDTH = 300
        const val DEFAULT_ONE_MEGABYTE: Long = 1024
    }
}
