package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

/**
 * @author : Steven 26/09/18
 * Copied from tkpddesign
 */
class TopupBillsBackEditText(context: Context?, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    var keyImeChangeListener: KeyImeChange? = null

    interface KeyImeChange {
        fun onKeyIme(keyCode: Int, event: KeyEvent)
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        keyImeChangeListener?.run {
            onKeyIme(keyCode, event)
        }
        return false
    }
}