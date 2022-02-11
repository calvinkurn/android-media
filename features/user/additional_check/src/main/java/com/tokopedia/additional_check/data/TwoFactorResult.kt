package com.tokopedia.additional_check.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

@Parcelize
data class TwoFactorResult(
        var showSkipButton: Boolean = false,
        var popupType: Int = 0
): Parcelable