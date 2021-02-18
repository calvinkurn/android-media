package com.tokopedia.play.broadcaster.view.partial

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.IdRes
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.TextFieldUnify

/**
 * Created by jegul on 18/02/21
 */
class TextFieldAddTagViewComponent(
        container: ViewGroup,
        @IdRes idRes: Int,
        listener: Listener,
) : ViewComponent(container, idRes) {

    private val textField: TextFieldUnify = rootView as TextFieldUnify

    init {
        textField.textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
        textField.textFieldInput.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE
                    || event.action == KeyEvent.ACTION_DOWN
                    && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                listener.onTagSubmitted(this, view.text.toString())
                view.text = ""
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    interface Listener {

        fun onTagSubmitted(view: TextFieldAddTagViewComponent, tag: String)
    }
}