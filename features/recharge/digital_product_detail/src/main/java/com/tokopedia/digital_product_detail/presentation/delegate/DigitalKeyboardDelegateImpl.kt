package com.tokopedia.digital_product_detail.presentation.delegate

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.common_digital.common.util.DigitalKeyboardWatcher
import java.lang.ref.WeakReference

class DigitalKeyboardDelegateImpl : DigitalKeyboardDelegate, LifecycleObserver {

    private val keyboardWatcher = DigitalKeyboardWatcher()
    private var mView: WeakReference<View>? = null

    override fun registerLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun registerKeyboard(view: WeakReference<View>) {
        mView = view
        mView?.get()?.let {
            keyboardWatcher.listen(
                it,
                object : DigitalKeyboardWatcher.Listener {
                    override fun onKeyboardShown(estimatedKeyboardHeight: Int) {
                        // do nothing
                    }

                    override fun onKeyboardHidden() {
                        // do nothing
                    }
                }
            )
        }
    }

    override fun isSoftKeyboardShown(): Boolean {
        return keyboardWatcher.isKeyboardOpened
    }

    override fun hideKeyboard() {
        mView?.get()?.let {
            val imm: InputMethodManager = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun unregisterListener() {
        mView?.get()?.run {
            keyboardWatcher.unlisten(this)
        }
    }
}
