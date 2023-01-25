package com.tokopedia.logisticaddaddress.utils

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormViewModel

object TextInputUtil {

    fun setWrapperWatcher(
        wrapper: TextInputLayout,
        text: String?,
        errorMessage: String?
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, text)
                } else {
                    setWrapperError(wrapper, errorMessage.orEmpty())
                }
            }

            override fun afterTextChanged(text: Editable) {
                if (text.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }
        }
    }

    fun setNotesWrapperWatcher(wrapper: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                wrapper.error = null
                wrapper.setErrorEnabled(false)
            }

            override fun afterTextChanged(text: Editable) {
                wrapper.error = null
                wrapper.setErrorEnabled(false)
            }
        }
    }

    fun setWrapperWatcherPhone(
        wrapper: TextInputLayout,
        textWatcher: String?,
        isEdit: Boolean,
        errorMessage: String?
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length < AddressFormViewModel.MIN_CHAR_PHONE_NUMBER) {
                    setWrapperError(wrapper, textWatcher)
                } else if (s.isEmpty() && isEdit) {
                    setWrapperError(wrapper, errorMessage.orEmpty())
                } else {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
            }
        }
    }

    fun setWrapperError(wrapper: TextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.setErrorEnabled(false)
        } else {
            wrapper.setErrorEnabled(true)
            wrapper.error = s
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun AutoCompleteTextView.setOnTouchLabelAddress(
        showListLabelAlamat: () -> Unit,
        rvChips: RecyclerView?,
        onAfterTextChanged: (String, RecyclerView?) -> Unit
    ) {
        this.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showListLabelAlamat.invoke()
                } else {
                    rvChips?.gone()
                }
            }
            setOnClickListener {
                showListLabelAlamat.invoke()
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // no op
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    // no op
                }

                override fun afterTextChanged(s: Editable) {
                    onAfterTextChanged(s.toString(), rvChips)
                }
            })
            setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
        }
    }
}
