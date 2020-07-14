package com.tokopedia.product.manage.item.video.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.util.BaseTextPickerDialogFragment
import com.tokopedia.product.manage.item.utils.YoutubeUtil

class ProductAddVideoDialogFragment : BaseTextPickerDialogFragment() {

    private var isErrorReturn: Boolean = false
    private val youtubeUtil : YoutubeUtil by lazy { YoutubeUtil() }
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
        if (s.isEmpty()) {
            setErrorTextInputLayout(getString(R.string.product_error_no_video_url_name))
            return
        }

        if(youtubeUtil.isValidYoutubeUrl(s.toString())){
            videoID = youtubeUtil.getVideoID()
        } else {
            setErrorTextInputLayout(getString(R.string.product_invalid_video_url))
            return
        }

        textInputLayout.isErrorEnabled = false
        textInputLayout.error = null
        isErrorReturn = false
    }


    private fun setErrorTextInputLayout(text: String){
        textInputLayout.isErrorEnabled = true
        textInputLayout.error = text

        isErrorReturn = true
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
