package com.tokopedia.digital_product_detail.presentation.delegate

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.common_digital.common.util.DigitalKeyboardWatcher
import java.lang.ref.WeakReference

class DigitalKeyboardDelegateImpl: DigitalKeyboardDelegate, LifecycleObserver {

    private val keyboardWatcher = DigitalKeyboardWatcher()
    private var mView: WeakReference<View>? = null

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun registerKeyboard(view: WeakReference<View>) {
        mView = view
        mView?.get()?.let {
            keyboardWatcher.listen(it, object : DigitalKeyboardWatcher.Listener {
                override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                    // do nothing
                }

                override fun onKeyboardHidden() {
                    // do nothing
                }
            })
        }
    }

    override fun isSoftKeyboardShown(): Boolean {
        return keyboardWatcher.isKeyboardOpened
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun unregisterListener() {
        mView?.get()?.run {
            keyboardWatcher.unlisten(this)
        }
    }
}