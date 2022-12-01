package com.tokopedia.digital_product_detail.presentation.delegate

import android.view.View
import androidx.lifecycle.LifecycleOwner
import java.lang.ref.WeakReference

interface DigitalKeyboardDelegate {
    fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner)
    fun registerKeyboard(view: WeakReference<View>)
    fun isSoftKeyboardShown(): Boolean
}