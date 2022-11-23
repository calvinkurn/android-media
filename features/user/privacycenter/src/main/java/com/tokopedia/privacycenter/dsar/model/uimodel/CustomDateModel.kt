package com.tokopedia.privacycenter.dsar.model.uimodel

import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toString

data class CustomDateModel(
    var startDate: String = DateUtil.getCurrentDate().toString(DateUtil.DEFAULT_VIEW_FORMAT),
    var endDate: String = DateUtil.getCurrentDate().toString(DateUtil.DEFAULT_VIEW_FORMAT)
)
