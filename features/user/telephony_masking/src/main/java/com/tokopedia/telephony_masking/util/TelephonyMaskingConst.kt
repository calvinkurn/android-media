package com.tokopedia.telephony_masking.util

object TelephonyMaskingConst {
    const val CONTACT_NAME = "Tokopedia Care"
    const val TELEPHONY_MASKING_KEY = "android_telephony_masking_number_list"
    const val CONTACT_NUMBERS_DEFAULT = """
        021-50917008, 021-50917000, 021-25091708, 021-30030316, 021-30034080
    """

    const val SAVE_EXTRA = "finishActivityOnSaveCompleted"

    const val RESULT_NOT_SAVED = -2
    const val RESULT_SKIP = -3
    const val RESULT_ALREADY_EXIST = -4

    const val PREFERENCE_NAME = "CACHE_TELEPHONY_MASKING"
    const val KEY_LOCAL_NUMBERS = "KEY_LOCAL_NUMBERS_TELEPHONY"

    const val REDIRECT_WEB = "https://www.tokopedia.com/"
}