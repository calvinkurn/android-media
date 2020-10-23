package com.tokopedia.media.loader.target

import android.view.View

interface ViewTarget<T : View> : Target {
    val view: T
}