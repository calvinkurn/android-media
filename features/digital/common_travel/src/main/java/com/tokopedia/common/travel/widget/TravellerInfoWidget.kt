package com.tokopedia.common.travel.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.travel.R
import com.tokopedia.design.base.BaseCustomView
import kotlinx.android.synthetic.main.layout_widget_traveller_info.view.*

/**
 * @author by jessica on 2019-09-13
 */

class TravellerInfoWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        BaseCustomView(context, attrs, defStyleAttr) {

    private var listener: TravellerInfoWidgetListener? = null
    var phoneNum: String = ""
    var phoneCode: Int = 0
    var phoneCountry: String = ""

    init {
        View.inflate(context, R.layout.layout_widget_traveller_info, this)
    }

    fun setContactName(name: String) { tv_contact_name.text = name }

    fun setContactEmail(email: String) { tv_contact_email.text = email }

    fun setContactPhoneNum (phoneNum: String) {
        this.phoneNum = phoneNum
        tv_contact_phone_number.text = phoneNum
    }

    fun setContactPhoneNum (phoneCode: Int, phoneNum: String) {
        this.phoneNum = phoneNum
        this.phoneCode = phoneCode
        tv_contact_phone_number.text = "+${phoneCode} ${phoneNum}"
    }

    fun setContactPhoneCountry(phoneCountry: String) {
        this.phoneCountry = phoneCountry
    }

    fun getContactPhoneCountry(): String = phoneCountry

    fun getContactName(): String = tv_contact_name.text.toString().trim()

    fun getContactEmail(): String = tv_contact_email.text.toString().trim()

    fun getContactPhoneNum(): String = phoneNum

    fun getContactPhoneCode(): Int = phoneCode

    fun showLoadingBar() {
        loading_bar.visibility = View.VISIBLE
    }

    fun hideLoadingBar() {
        loading_bar.visibility = View.GONE
    }

    fun setListener(listener: TravellerInfoWidgetListener) {
        this.listener = listener
        ic_edit_contact.setOnClickListener { listener.onClickEdit() }
    }

    interface TravellerInfoWidgetListener {
        fun onClickEdit()
    }
}