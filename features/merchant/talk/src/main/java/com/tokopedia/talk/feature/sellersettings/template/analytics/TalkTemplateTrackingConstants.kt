package com.tokopedia.talk.feature.sellersettings.template.analytics

import com.tokopedia.talk.common.analytics.TalkTrackingConstants

object TalkTemplateTrackingConstants {
    const val EVENT_CATEGORY_TEMPLATE_LIST = "inbox talk - template"
    const val EVENT_CATEGORY_TEMPLATE_BOTTOM_SHEET = "inbox talk - template"
    const val EVENT_ACTION_ADD_TEMPLATE = "${TalkTrackingConstants.EVENT_ACTION_CLICK} tambah template on template balasan"
    const val EVENT_ACTION_SAVE_TEMPLATE = "${TalkTrackingConstants.EVENT_ACTION_CLICK} simpan on template balasan"
    const val EVENT_ACTION_ACTIVATE_TEMPLATE = "${TalkTrackingConstants.EVENT_ACTION_CLICK} activate template balasan"
}