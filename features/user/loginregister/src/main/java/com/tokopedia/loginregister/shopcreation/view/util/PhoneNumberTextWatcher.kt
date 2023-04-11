package com.tokopedia.loginregister.shopcreation.view.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


/**
 * Created by Ade Fulki on 2020-01-23.
 * ade.hadian@tokopedia.com
 */

open class PhoneNumberTextWatcher(private val editText: EditText) : TextWatcher {

    override fun onTextChanged(
        s: CharSequence, cursorPosition: Int, before: Int,
        count: Int,
    ) {
        var cursor = cursorPosition

        if (before == 0 && count == 1) {  //Entering values

            var phone: String? = s.toString()
            var tempSubstring1: String? = ""
            var tempSubstring2: String? = ""
            var tempSubstring3: String? = ""
            if (phone != null && phone.isNotEmpty()) {
                phone = phone.replace("-", "")
                if (phone.length >= 3) {
                    tempSubstring1 = phone.substring(0, 3)
                } else if (phone.length < 3) {
                    tempSubstring1 = phone.substring(0, phone.length)
                }
                if (phone.length >= 7) {
                    tempSubstring2 = phone.substring(3, 7)
                    tempSubstring3 = phone.substring(7, phone.length)
                } else if (phone.length in 4..6) {
                    tempSubstring2 = phone.substring(3, phone.length)
                }
                val stringBuffer = StringBuffer()
                if (tempSubstring1 != null && tempSubstring1.isNotEmpty()) {
                    stringBuffer.append(tempSubstring1)

                }
                if (tempSubstring2 != null && tempSubstring2.isNotEmpty()) {
                    stringBuffer.append("-")
                    stringBuffer.append(tempSubstring2)

                }
                if (tempSubstring3 != null && tempSubstring3.isNotEmpty()) {
                    stringBuffer.append("-")
                    stringBuffer.append(tempSubstring3)
                }
                editText.removeTextChangedListener(this)
                editText.setText(stringBuffer.toString())
                cursor += if (cursor == 3 || cursor == 8) {
                    2
                } else {
                    1
                }
                if (cursor <= editText.text.toString().length) {
                    editText.setSelection(cursor)
                } else {
                    editText.setSelection(editText.text.toString().length)
                }
                editText.addTextChangedListener(this)
            } else {
                editText.removeTextChangedListener(this)
                editText.setText("")
                editText.addTextChangedListener(this)
            }

        }

        if (before == 1 && count == 0) {

            var phone: String? = s.toString()
            var tempSubstring1: String? = ""
            var tempSubstring2: String? = ""
            var tempSubstring3: String? = ""

            if (phone != null && phone.isNotEmpty()) {
                phone = phone.replace("-", "")
                if (cursor == 3) {
                    phone = removeCharAt(phone, cursor - 1, s.toString().length - 1)
                } else if (cursor == 8) {
                    phone = removeCharAt(phone, cursor - 2, s.toString().length - 2)
                }
                if (phone.length >= 3) {
                    tempSubstring1 = phone.substring(0, 3)
                } else if (phone.length < 3) {
                    tempSubstring1 = phone.substring(0, phone.length)
                }
                if (phone.length >= 7) {
                    tempSubstring2 = phone.substring(3, 7)
                    tempSubstring3 = phone.substring(7, phone.length)
                } else if (phone.length in 4..6) {
                    tempSubstring2 = phone.substring(3, phone.length)
                }
                val stringBuffer = StringBuffer()
                if (tempSubstring1 != null && tempSubstring1.isNotEmpty()) {
                    stringBuffer.append(tempSubstring1)

                }
                if (tempSubstring2 != null && tempSubstring2.isNotEmpty()) {
                    stringBuffer.append("-")
                    stringBuffer.append(tempSubstring2)

                }
                if (tempSubstring3 != null && tempSubstring3.isNotEmpty()) {
                    stringBuffer.append("-")
                    stringBuffer.append(tempSubstring3)
                }
                editText.removeTextChangedListener(this)
                editText.setText(stringBuffer.toString())
                if (cursor == 3 || cursor == 8) {
                    cursor -= 1
                }
                if (cursor <= editText.text.toString().length) {
                    editText.setSelection(cursor)
                } else {
                    editText.setSelection(editText.text.toString().length)
                }
                editText.addTextChangedListener(this)
            } else {
                editText.removeTextChangedListener(this)
                editText.setText("")
                editText.addTextChangedListener(this)
            }

        }


    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {}

    companion object {

        fun removeCharAt(s: String, pos: Int, length: Int): String {

            var value = ""
            if (length > pos) {
                value = s.substring(pos + 1)
            }
            return s.substring(0, pos) + value
        }
    }
}