package com.tokopedia.product.edit.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import com.tokopedia.abstraction.base.view.dialog.BaseTextPickerDialogFragment
import com.tokopedia.product.edit.util.YoutubeUtil

class ProductAddVideoDialogFragment : BaseTextPickerDialogFragment() {

    private var isErrorReturn: Boolean = false
    private val youtubeUtil : YoutubeUtil by lazy { YoutubeUtil(context!!) }
    private var videoID : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        textInput.maxLines = 1
        textInput.setSingleLine(true)
        stringPickerTitle.setText(R.string.product_add_url_video)
        textInputLayout.hint = getString(R.string.product_url_video)
        textInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validateUrl(s)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        return view
    }

    fun validateUrl(s: CharSequence) {
        try {
            youtubeUtil.setYoutubeUrl(s.toString())
        } catch (iae: IllegalArgumentException) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = iae.message

            isErrorReturn = true
            return
        }

        try {
            videoID = youtubeUtil.saveVideoID()
        } catch (iae: IllegalArgumentException) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = iae.message

            isErrorReturn = true
            return
        }

        textInputLayout.isErrorEnabled = false
        textInputLayout.error = null
        isErrorReturn = false
    }

    override fun onTextSubmitted(text: String) {
        validateFirstTime()

        if (isErrorReturn) {
            return
        }

        super.onTextSubmitted(videoID)
    }


    private fun validateFirstTime() {
        validateUrl(textInput.text.toString())
    }

    companion object {

        const val TAG = "YoutubeAddUrlDialog"
    }
}
