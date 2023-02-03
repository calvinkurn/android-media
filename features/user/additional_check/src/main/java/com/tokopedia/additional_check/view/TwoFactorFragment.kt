package com.tokopedia.additional_check.view

import com.tokopedia.imageassets.TokopediaImageUrl

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.additional_check.R
import com.tokopedia.additional_check.common.ADD_PHONE_NUMBER_PAGE
import com.tokopedia.additional_check.common.ADD_PIN_PAGE
import com.tokopedia.additional_check.common.ActivePageListener
import com.tokopedia.additional_check.data.TwoFactorResult
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.databinding.FragmentTwoFactorBinding
import com.tokopedia.additional_check.di.AdditionalCheckModules
import com.tokopedia.additional_check.di.DaggerAdditionalCheckComponents
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.POPUP_TYPE_BOTH
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.POPUP_TYPE_PHONE
import com.tokopedia.additional_check.internal.AdditionalCheckConstants.POPUP_TYPE_PIN
import com.tokopedia.additional_check.internal.TwoFactorTracker
import com.tokopedia.additional_check.subscriber.TwoFactorCheckerSubscriber
import com.tokopedia.additional_check.view.activity.TwoFactorActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.remoteconfig.RemoteConfigInstance
import javax.inject.Inject

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

    @Inject
    lateinit var additionalCheckPreference: AdditionalCheckPreference

    override fun getScreenName(): String = "twoFactorFragment"

    override fun initInjector() {
        DaggerAdditionalCheckComponents
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .additionalCheckModules(AdditionalCheckModules())
            .build()
            .inject(this)
    }

    private var _binding: FragmentTwoFactorBinding? = null

    private val binding get() = _binding!!

    fun setActiveListener(mActivePageListener: ActivePageListener){
        this.activePageListener = mActivePageListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = arguments?.getParcelable(RESULT_POJO_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTwoFactorBinding.inflate(inflater, container, false)
        notifyActivity()
        renderViewByType()
        return binding.root
    }

    private fun notifyActivity(){
        activity?.run {
            if(this is TwoFactorActivity){
                onFragmentCreated()
            }
        }
    }

    private fun renderViewByType(){
        when(model?.popupType){
            POPUP_TYPE_PIN -> renderPinView()
            POPUP_TYPE_PHONE -> renderPhoneView()
            POPUP_TYPE_BOTH -> renderPhoneView()
        }
    }

    private fun renderPinView(){
        context?.run {
            activePageListener?.currentPage(ADD_PIN_PAGE)
            twoFactorTracker.viewPageOnboardingAddPin()
            binding.titleTwoFactor.text = getString(R.string.add_pin_heading)
            binding.bodyTwoFactor.text = getString(R.string.add_pin_body)
            binding.btnTwoFactor.text = getString(R.string.add_pin_button_title)
            binding.imgViewTwoFactor.run {
                ImageHandler.LoadImage(this, PIN_ONBOARDING_IMG)
            }
            binding.btnTwoFactor.setOnClickListener {
                twoFactorTracker.clickButtonPageAddPin()
                goToAddPin(validateToken)
            }
        }
    }

    private fun renderPhoneView(){
        if (isImprove2FA()) {
            goToNewAddPhone()
            return
        }

        context?.run {
            activePageListener?.currentPage(ADD_PHONE_NUMBER_PAGE)

            binding.titleTwoFactor.text = getString(R.string.add_phone_heading)
            binding.bodyTwoFactor.text = getString(R.string.add_phone_body)
            binding.btnTwoFactor.text = getString(R.string.add_phone_button_title)
            binding.imgViewTwoFactor.run {
                ImageHandler.LoadImage(this, PHONE_ONBOARDING_IMG)
            }
            binding.btnTwoFactor.setOnClickListener {
                twoFactorTracker.clickButtonPageAddPhoneNumber()
                goToAddPhone()
            }
        }
    }

    private fun renderSuccessPin(){
        context?.run {
            binding.titleTwoFactor.text = getString(R.string.add_pin_success_heading)
            binding.bodyTwoFactor.text = getString(R.string.add_pin_success_body)
            binding.btnTwoFactor.text = getString(R.string.add_pin_success_button_title)
            binding.imgViewTwoFactor.run {
                ImageHandler.LoadImage(this, PIN_SUCCESS_IMG)
            }
            binding.btnTwoFactor.setOnClickListener {
                if(additionalCheckPreference.getNextOffer().isNotEmpty() && activity != null) {
                    val nextIntent = TwoFactorCheckerSubscriber.mapStringToOfferData(additionalCheckPreference, requireActivity(), additionalCheckPreference.getNextOffer())
                    nextIntent?.run {
                        startActivity(this)
                    }
                }
                activity?.finish()
            }
        }
    }

    private fun goToAddPin(validateToken: String){
        context?.run {
            val i = RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.ADD_PIN_FROM_2FA)
            i.putExtras(Bundle().apply {
                putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
                putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
            })
            startActivityForResult(i, ADD_PIN_REQ_CODE)
        }
    }

    private fun goToAddPhone(){
        context?.run {
            val i = RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.ADD_PHONE)
            i.putExtras(Bundle().apply {
                putBoolean(IS_FROM_2FA, arguments?.getBoolean(IS_FROM_2FA, false) ?: false)
            })
            startActivityForResult(i, ADD_PHONE_REQ_CODE)
        }
    }

    private fun goToNewAddPhone() {
        context?.run {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.NEW_ADD_PHONE)
            startActivityForResult(intent, ADD_PHONE_REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            ADD_PIN_REQ_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    renderSuccessPin()
                } else {
                    renderPinView()
                }
            }
            ADD_PHONE_REQ_CODE -> {
                if(resultCode == Activity.RESULT_OK) {
                    if(additionalCheckPreference.getNextOffer().isNotEmpty()) {
                        additionalCheckPreference.clearNextOffer()
                    }
                    validateToken = data?.getStringExtra(ApplinkConstInternalGlobal.PARAM_TOKEN).toString()
                    goToAddPin(validateToken)
                } else {
                    if (isImprove2FA()) {
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun isImprove2FA(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(ROLLENCE_IMPROVE_2FA).isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ROLLENCE_IMPROVE_2FA = "2fa_phone_improve_2"
        const val RESULT_POJO_KEY = "modelKey"
        const val IS_FROM_2FA = "is_from_2fa_checker"

        private const val PIN_ONBOARDING_IMG = TokopediaImageUrl.PIN_ONBOARDING_IMG
        private const val PHONE_ONBOARDING_IMG = TokopediaImageUrl.PHONE_ONBOARDING_IMG
        private const val PIN_SUCCESS_IMG = TokopediaImageUrl.PIN_SUCCESS_IMG

        fun newInstance(bundle: Bundle?): Fragment{
            return TwoFactorFragment().apply {
                arguments = bundle
            }
        }
    }
}
