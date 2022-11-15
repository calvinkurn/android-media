package com.tokopedia.content.common.util

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on November 14, 2022
 */

fun ContentAccountUiModel?.orUnknown(): ContentAccountUiModel = this ?: ContentAccountUiModel.Empty
