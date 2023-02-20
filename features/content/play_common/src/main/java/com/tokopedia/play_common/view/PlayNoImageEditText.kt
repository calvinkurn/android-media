package com.tokopedia.play_common.view

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat

/**
 * Created by jegul on 07/08/20
 */
class PlayNoImageEditText : AppCompatEditText {

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    private val forbiddenMimeTypes = arrayOf("image/*", "image/png", "image/gif", "image/jpeg")

    private var mOnErrorListener: OnErrorListener? = null

    override fun onCreateInputConnection(editorInfo: EditorInfo): InputConnection? {
        val inputConnection: InputConnection? = super.onCreateInputConnection(editorInfo)
        EditorInfoCompat.setContentMimeTypes(editorInfo, forbiddenMimeTypes)

        return if (inputConnection != null) {
            InputConnectionCompat.createWrapper(inputConnection, editorInfo) { _, _, _ ->
                mOnErrorListener?.onForbiddenImage(this@PlayNoImageEditText)
                true
            }
        } else inputConnection
    }

    fun setOnErrorListener(onErrorListener: OnErrorListener) {
        mOnErrorListener = onErrorListener
    }

    interface OnErrorListener {

        fun onForbiddenImage(view: PlayNoImageEditText)
    }
}
