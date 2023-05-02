package com.tokopedia.review.feature.bulk_write_review.presentation.analytic

object BulkWriteReviewTrackingConstant {
    const val EVENT_NAME_PROMO_VIEW = "promoView"
    const val EVENT_NAME_CLICK_PG = "clickPG"

    const val EVENT_ACTION_BULK_REVIEW_FORM_IMPRESSION = "impression - inbox review form"
    const val EVENT_ACTION_BULK_REVIEW_FORM_SUBMITTED = "click - kirim ulasan produk"
    const val EVENT_ACTION_BULK_REVIEW_FORM_DISMISS = "click - dismiss create review form"
    const val EVENT_ACTION_BULK_REVIEW_FORM_BAD_RATING_CATEGORY_IMPRESSION = "impression - bad review reason checkbox"
    const val EVENT_ACTION_BULK_REVIEW_FORM_BAD_RATING_CATEGORY_SELECTED = "click - bad review reason checkbox"
    const val EVENT_ACTION_BULK_REVIEW_FORM_SUBMIT_ERROR = "click - kirim ulasan produk - error"
    const val EVENT_ACTION_BULK_REVIEW_FORM_REMOVE_BUTTON_CLICK = "click - remove inbox review form"
    const val EVENT_ACTION_BULK_REVIEW_FORM_REMOVE_DIALOG_IMPRESSION = "impression - popup message"
    const val EVENT_ACTION_BULK_REVIEW_FORM_REMOVE_DIALOG_CANCEL_BUTTON_CLICK = "click - batal on popup message"
    const val EVENT_ACTION_BULK_REVIEW_FORM_REMOVE_DIALOG_REMOVE_BUTTON_CLICK = "click - hapus on popup message"
    const val EVENT_ACTION_BULK_REVIEW_FORM_ADD_TESTIMONY_CLICK = "click - testimoni on inbox review form"
    const val EVENT_ACTION_BULK_REVIEW_FORM_ADD_ATTACHMENT_CLICK = "click - foto/video on inbox review form"
    const val EVENT_ACTION_BULK_REVIEW_FORM_CHANGE_RATING = "click - star rating on inbox review form"

    const val EVENT_CATEGORY_BULK_REVIEW_FORM = "product review detail page - bulk review form"

    const val TRACKER_ID_BULK_REVIEW_FORM_IMPRESSION = "40298"
    const val TRACKER_ID_BULK_REVIEW_FORM_SUBMITTED = "40303"
    const val TRACKER_ID_BULK_REVIEW_FORM_DISMISS = "40306"
    const val TRACKER_ID_BULK_REVIEW_FORM_BAD_RATING_CATEGORY_IMPRESSION = "40312"
    const val TRACKER_ID_BULK_REVIEW_FORM_BAD_RATING_CATEGORY_SELECTED = "40313"
    const val TRACKER_ID_BULK_REVIEW_FORM_SUBMIT_ERROR = "40314"
    const val TRACKER_ID_BULK_REVIEW_FORM_REMOVE_BUTTON_CLICK = "40316"
    const val TRACKER_ID_BULK_REVIEW_FORM_REMOVE_DIALOG_IMPRESSION = "40317"
    const val TRACKER_ID_BULK_REVIEW_FORM_REMOVE_DIALOG_CANCEL_BUTTON_CLICK = "40318"
    const val TRACKER_ID_BULK_REVIEW_FORM_REMOVE_DIALOG_REMOVE_BUTTON_CLICK = "40319"
    const val TRACKER_ID_BULK_REVIEW_FORM_ADD_TESTIMONY_CLICK = "40320"
    const val TRACKER_ID_BULK_REVIEW_FORM_ADD_ATTACHMENT_CLICK = "40321"
    const val TRACKER_ID_BULK_REVIEW_FORM_CHANGE_RATING = "40322"

    const val EVENT_LABEL_FORMAT_REVIEW_FORM_BAD_RATING_CATEGORY_IMPRESSION = "star:%d;"
    const val EVENT_LABEL_FORMAT_REVIEW_FORM_BAD_RATING_CATEGORY_SELECTED = "star:%d;"
    const val EVENT_LABEL_FORMAT_REVIEW_FORM_SUBMIT_ERROR = "error_message:%s;"
    const val EVENT_LABEL_FORMAT_REVIEW_FORM_REMOVE_DIALOG_IMPRESSION = "title:%s;subtitle:%s;"
    const val EVENT_LABEL_FORMAT_REVIEW_FORM_REMOVE_DIALOG_BUTTON_CLICK = "title:%s;subtitle:%s;"

    const val CREATIVE_NAME_FORMAT_REVIEW_FORM_SUBMITTED = "star : %d - ulasan : %s - review_char : %d - gambar : %d - video : %d - anonim : %b - count_template_used : %d"
    const val CREATIVE_NAME_FORMAT_REVIEW_FORM_DISMISS = "star : %d - ulasan : %s - review_char : %d - gambar : %d - video : %d - anonim : %b - count_template_used : %d"
    const val CREATIVE_NAME_FORMAT_REVIEW_FORM_SUBMIT_ERROR = "star : %d - ulasan : %s - review_char : %d - gambar : %d - video : %d - anonim : %b - count_template_used : %d"
    const val CREATIVE_NAME_FORMAT_REVIEW_FORM_CHANGE_RATING = "star : %d - click_timestamp : %d"

    const val SEPARATOR_SPACED_DASH = " - "
    const val BLANK = "blank"
    const val FILLED = "filled"
}
