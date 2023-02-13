package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.core.view.inputmethod.InputConnectionCompat
import com.tokopedia.play.broadcaster.R


/**
 * Created by jegul on 16/07/20
 */
class PlayBroadcastEditText : AppCompatEditText {

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr)

    private val forbiddenMimeTypes = arrayOf("image/*", "image/png", "image/gif", "image/jpeg")
    private val errorMessageForbiddenMime = context.getString(R.string.play_error_image_edit_text)

    override fun onCreateInputConnection(editorInfo: EditorInfo): InputConnection? {
        val inputConnection: InputConnection? = super.onCreateInputConnection(editorInfo)
        EditorInfoCompat.setContentMimeTypes(editorInfo, forbiddenMimeTypes)

        return if (inputConnection != null) {
            InputConnectionCompat.createWrapper(inputConnection, editorInfo) { _, _, _ ->
                Toast.makeText(context, errorMessageForbiddenMime, Toast.LENGTH_SHORT).show()
                true
            }
        } else inputConnection
    }
}
