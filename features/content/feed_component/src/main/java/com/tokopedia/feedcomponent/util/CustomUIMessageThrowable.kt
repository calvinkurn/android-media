package com.tokopedia.feedcomponent.util

import androidx.annotation.StringRes

class CustomUiMessageThrowable(
    @StringRes val errorMessageId: Int
): Throwable()