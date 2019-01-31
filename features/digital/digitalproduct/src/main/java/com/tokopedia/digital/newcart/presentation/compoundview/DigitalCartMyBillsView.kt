package com.tokopedia.digital.newcart.presentation.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.digital.R

class DigitalCartMyBillsView : BaseCustomView {

    private lateinit var headerTitleTextView: TextViewCompat;
    private lateinit var subscriptionCheckbox: CheckBox;
    private lateinit var descriptionTextView: TextViewCompat;

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_digital_cart_my_bills_holder, this, true);
        headerTitleTextView = view.findViewById(R.id.tv_header_title)
        subscriptionCheckbox = view.findViewById(R.id.cb_subscription)
        descriptionTextView = view.findViewById(R.id.tv_description)
    }

    fun setDescription(description: String) {
        descriptionTextView.text = MethodChecker.fromHtml(description)
    }

    fun setChecked(checked: Boolean) {
        subscriptionCheckbox.isChecked = checked
    }

    fun setHeaderTitle(title : String) {
        headerTitleTextView.text = title
    }

    fun isChecked() : Boolean = subscriptionCheckbox.isChecked
}