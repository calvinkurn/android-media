package com.tokopedia.hotel.destination.view.widget

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.SearchBarUnify

/**
 * @author by jessica on 13/04/20
 */

class HotelSearchInputView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0) : BaseCustomView(context, attrs, defStyleAttr) {
    var searchBarUnify: SearchBarUnify
    var searchBarEditText: EditText

    var actionListener: ActionListener? = null

    init {
        val view = View.inflate(context, R.layout.widget_hotel_search_input_view, this)
        searchBarUnify = view.findViewById(R.id.hotel_destination_search_bar)
        searchBarEditText = searchBarUnify.searchBarTextField
    }

    fun buildView() {
        searchBarUnify.isClearable = true
        searchBarUnify.showIcon = false
        searchBarUnify.searchBarTextField.hint = resources.getString(R.string.hotel_destination_edittext_hint)

        searchBarEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { /* do nothing */ }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* do nothing */ }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(searchBarEditText.text.toString())) {
                    searchBarUnify.searchBarIcon.visibility = View.GONE
                } else {
                    searchBarUnify.searchBarIcon.visibility = View.VISIBLE
                }
                actionListener?.onSearchTextChanged(searchBarEditText.text.toString())
            }
        })
    }

    interface ActionListener {
        fun onSearchTextChanged(text: String)
    }
}