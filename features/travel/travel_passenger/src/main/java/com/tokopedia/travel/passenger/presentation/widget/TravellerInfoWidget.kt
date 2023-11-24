package com.tokopedia.travel.passenger.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.travel.passenger.databinding.LayoutWidgetTravellerInfoBinding
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * @author by jessica on 2019-09-13
 */

class TravellerInfoWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private var listener: TravellerInfoWidgetListener? = null
    var phoneNum: String = ""
    var phoneCode: Int = 0
    var phoneCountry: String = ""

    private val binding = LayoutWidgetTravellerInfoBinding.inflate(LayoutInflater.from(context), this, true)

    fun setContactName(name: String) {
        binding.tvTravelContactName.text = name
    }

    fun setContactEmail(email: String) {
        binding.tvTravelContactEmail.text = email
    }

    fun setContactPhoneNum(phoneNum: String) {
        this.phoneNum = phoneNum
        binding.tvTravelContactPhoneNumber.text = phoneNum
    }

    fun setContactPhoneNum(phoneCode: Int, phoneNum: String) {
        this.phoneNum = phoneNum
        this.phoneCode = phoneCode
        binding.tvTravelContactPhoneNumber.text = "+$phoneCode $phoneNum"
    }

    fun setContactPhoneCountry(phoneCountry: String) {
        this.phoneCountry = phoneCountry
    }

    fun getContactPhoneCountry(): String = if (phoneCountry.isNotEmpty()) phoneCountry else "ID"

    fun getContactName(): String = binding.tvTravelContactName.text.toString().trim()

    fun getContactEmail(): String = binding.tvTravelContactEmail.text.toString().trim()

    fun getContactPhoneNum(): String = phoneNum

    fun getContactPhoneCode(): Int = phoneCode

    fun showLoadingBar() {
        binding.loadingBar.visibility = View.VISIBLE
    }

    fun hideLoadingBar() {
        binding.loadingBar.visibility = View.GONE
    }

    fun setListener(listener: TravellerInfoWidgetListener) {
        this.listener = listener
        binding.userContactInfo.setOnClickListener { listener.onClickEdit() }
    }

    interface TravellerInfoWidgetListener {
        fun onClickEdit()
    }
}
