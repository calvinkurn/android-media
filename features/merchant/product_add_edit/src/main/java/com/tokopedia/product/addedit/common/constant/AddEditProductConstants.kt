package com.tokopedia.product.addedit.common.constant

import com.tokopedia.imageassets.ImageUrl

object AddEditProductConstants {
    const val EXTRA_CACHE_MANAGER_ID = "CACHE_MANAGER_ID"
    const val EXTRA_IS_EDIT_MODE = "EXTRA_IS_EDIT_MODE"
    const val BROADCAST_ADD_PRODUCT = "BROADCAST_ADD_PRODUCT"
    const val KEY_SAVE_INSTANCE_PREVIEW = "KEY_SAVE_INSTANCE_STATE_PREVIEW"
    const val KEY_SAVE_INSTANCE_INPUT_MODEL = "KEY_SAVE_INSTANCE_INPUT_MODEL"
    const val KEY_SAVE_INSTANCE_ISADDING = "KEY_SAVE_INSTANCE_STATE_ISADDING"
    const val KEY_SAVE_INSTANCE_ISEDITING = "KEY_SAVE_INSTANCE_STATE_ISEDITING"
    const val KEY_SAVE_INSTANCE_ISDRAFTING = "KEY_SAVE_INSTANCE_STATE_ISDRAFTING"
    const val KEY_SAVE_INSTANCE_ISFIRSTMOVED = "KEY_SAVE_INSTANCE_STATE_ISFIRSTMOVED"

    const val HTTP_PREFIX = "http"
    const val KEY_YOUTUBE_VIDEO_ID = "v"
    const val WEB_PREFIX_HTTPS = "https://"
    const val PREFIX_CACHE = "PickerImageUrl_"
    const val GQL_ERROR_SUBSTRING = "gql.tokopedia.com"
    const val YOUTU_BE_URL = "youtu.be"
    const val YOUTUBE_URL = "youtube.com"
    const val FULL_YOUTUBE_URL = "www.youtube.com"
    const val TEMP_IMAGE_EXTENSION = ".0"
    const val KEY_OPEN_BOTTOMSHEET = "tokopedia://bottomsheet_product_limitation"
    const val EXT_JPG = ".jpg"
    const val EXT_JPEG = ".jpeg"

    const val PHOTO_TIPS_URL_1 = ImageUrl.PHOTO_TIPS_URL_1
    const val PHOTO_TIPS_URL_2 = ImageUrl.PHOTO_TIPS_URL_2
    const val PHOTO_TIPS_URL_3 = ImageUrl.PHOTO_TIPS_URL_3

    const val PHOTO_NEW_USER_SPECIFICATION = ImageUrl.PHOTO_NEW_USER_SPECIFICATION
    const val PHOTO_SIGNAL_STATUS_SPECIFICATION = ImageUrl.PHOTO_SIGNAL_STATUS_SPECIFICATION
    const val PHOTO_TITLE_VALIDATION_SUCCESS = ImageUrl.PHOTO_TITLE_VALIDATION_SUCCESS
    const val PHOTO_TITLE_VALIDATION_ERROR = ImageUrl.PHOTO_TITLE_VALIDATION_ERROR

    const val LIGHT_BULB_ICON = ImageUrl.LIGHT_BULB_ICON
    const val ROUND_GREEN_CHECK_MARK_ICON = ImageUrl.ROUND_GREEN_CHECK_MARK_ICON
    const val INFORMATION_ICON = ImageUrl.INFORMATION_ICON

    const val MAX_PRODUCT_IMAGE_SIZE_IN_MB = 30
    const val MAX_PRODUCT_IMAGE_SIZE_IN_KB = MAX_PRODUCT_IMAGE_SIZE_IN_MB * 1024
    const val TOP_SCROLL_MARGIN = 16

    const val DELAY_MILLIS = 1000L

    const val FIRST_CATEGORY_SELECTED = 0
    const val DOUBLE_ZERO = 0.0

    const val SERVICE_FEE_LIMIT = 100

    // only applied to CE (Commission Engine) service
    const val GET_COMMISSION_ENGINE_REGULAR_MERCHANT = 999
}
