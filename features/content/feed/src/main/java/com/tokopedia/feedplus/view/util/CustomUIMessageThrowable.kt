package com.tokopedia.feedplus.view.util

import androidx.annotation.StringRes

class CustomUiMessageThrowable(
    @StringRes val errorMessageId: Int
): Throwable()