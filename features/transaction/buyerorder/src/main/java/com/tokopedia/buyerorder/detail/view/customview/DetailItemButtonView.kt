package com.tokopedia.buyerorder.detail.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.buyerorder.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class DetailItemButtonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    var titleTextView: Typography
    var descTextView: Typography
    var button: UnifyButton

    var listener: Listener? = null
    set(value) {
        field = value
        if (value != null) {
            button.setOnClickListener { value.onButtonClick() }
        }
    }

    init {
        val view = View.inflate(context, getLayout(), this)
        titleTextView = view.findViewById(R.id.detail_item_title)
        descTextView = view.findViewById(R.id.detail_item_desc)
        button = view.findViewById(R.id.detail_item_button)
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setDescription(desc: String) {
        descTextView.text = desc
    }

    open fun getLayout(): Int {
        return R.layout.order_list_detail_item_button
    }

    interface Listener {
        fun onButtonClick()
    }
}