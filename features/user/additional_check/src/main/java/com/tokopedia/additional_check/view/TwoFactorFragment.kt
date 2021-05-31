package com.tokopedia.additional_check.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.additional_check.R
import com.tokopedia.additional_check.common.ADD_PHONE_NUMBER_PAGE
import com.tokopedia.additional_check.common.ADD_PIN_PAGE
import com.tokopedia.additional_check.common.ActivePageListener
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.POPUP_TYPE_BOTH
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.POPUP_TYPE_PHONE
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.POPUP_TYPE_PIN
import com.tokopedia.additional_check.internal.TwoFactorTracker
import com.tokopedia.additional_check.view.activity.TwoFactorActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import kotlinx.android.synthetic.main.fragment_two_factor.view.*

/**
 * Created by Yoris Prayogo on 10/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class TwoFactorFragment: BaseDaggerFragment() {

    private var activePageListener: ActivePageListener? = null
    private val ADD_PHONE_REQ_CODE = 1
    private val ADD_PIN_REQ_CODE = 2

    private val twoFactorTracker = TwoFactorTracker()
    private var validateToken: String = ""

    var model: TwoFactorResult? = TwoFactorResult()

    override fun getScreenName(): String = "twoFactorFragment"
    override fun initInjector() {}

    fun setActiveListener(mActivePageListener: ActivePageListener){
        this.activePageListener = mActivePageListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = arguments?.getParcelable(RESULT_POJO_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_two_factor, container, false)
        notifyActivity()
        renderViewByType(mView)
        return mView
    }

    private fun notifyActivity(){
        activity?.run {
            if(this is TwoFactorActivity){
                onFragmentCreated()
            }
        }
    }

    private fun renderViewByType(mView: View?){
        when(model?.popupType){
            POPUP_TYPE_PIN -> renderPinView(mView)
            POPUP_TYPE_PHONE or POPUP_TYPE_BOTH -> renderPhoneView(mView)
        }
    }

    private fun renderPinView(mView: View?){
        context?.run {
            activePageListener?.currentPage(ADD_PIN_PAGE)
            twoFactorTracker.viewPageOnboardingAddPin()

            mView?.title_two_factor?.text = getString(R.string.add_pin_heading)
            mView?.body_two_factor?.text = getString(R.string.add_pin_body)
            mView?.btn_two_factor?.text = getString(R.string.add_pin_button_title)
            mView?.img_view_two_factor?.run {
                ImageHandler.LoadImage(this, PIN_ONBOARDING_IMG)
            }
            mView?.btn_two_factor?.setOnClickListener {
                twoFactorTracker.clickButtonPageAddPin()
                goToAddPin(validateToken)
            }
        }
    }

    private fun renderPhoneView(mView: View?){
        context?.run {
            activePageListener?.currentPage(ADD_PHONE_NUMBER_PAGE)

            mView?.title_two_factor?.text = getString(R.string.add_phone_heading)
            mView?.body_two_factor?.text = getString(R.string.add_phone_body)
            mView?.btn_two_factor?.text = getString(R.string.add_phone_button_title)
            mView?.img_view_two_factor?.run {
                ImageHandler.LoadImage(this, PHONE_ONBOARDING_IMG)
            }
            mView?.btn_two_factor?.setOnClickListener {
                twoFactorTracker.clickButtonPageAddPhoneNumber()
                goToAddPhone()
            }
        }
    }

    private fun renderSuccessPin(mView: View?){
        context?.run {
            mView?.title_two_factor?.text = getString(R.string.add_pin_success_heading)
            mView?.body_two_factor?.text = getString(R.string.add_pin_success_body)
            mView?.btn_two_factor?.text = getString(R.string.add_pin_success_button_title)
            mView?.img_view_two_factor?.run {
                ImageHandler.LoadImage(this, PIN_SUCCESS_IMG)
            }
            mView?.btn_two_factor?.setOnClickListener { activity?.finish() }
        }
    }

    private fun goToAddPin(validateToken: String){
        context?.run {
            val i = RouteManager.getIntent(this, ApplinkConstInternalGlobal.ADD_PIN_FROM_2FA)
            i.putExtras(Bundle().apply {
                putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
                putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
            })
            startActivityForResult(i, ADD_PIN_REQ_CODE)
        }
    }

    private fun goToAddPhone(){
        context?.run {
            val i = RouteManager.getIntent(this, ApplinkConstInternalGlobal.ADD_PHONE)
            startActivityForResult(i, ADD_PHONE_REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            ADD_PIN_REQ_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    renderSuccessPin(view)
                }
            }
            ADD_PHONE_REQ_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    validateToken = data?.getStringExtra(ApplinkConstInternalGlobal.PARAM_TOKEN).toString()
                    goToAddPin(validateToken)
                }
            }
        }
    }

    companion object {
        const val RESULT_POJO_KEY = "modelKey"

        private const val PIN_ONBOARDING_IMG = "https://ecs7.tokopedia.net/android/user/image_pin_two_factor.png"
        private const val PHONE_ONBOARDING_IMG = "https://ecs7.tokopedia.net/android/user/image_phone_two_factor.png"
        private const val PIN_SUCCESS_IMG = "https://ecs7.tokopedia.net/android/user/image_pin_success_two_factor.png"

        fun newInstance(bundle: Bundle?): Fragment{
            return TwoFactorFragment().apply {
                arguments = bundle
            }
        }
    }
}