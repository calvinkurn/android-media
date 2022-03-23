package com.tokopedia.additional_check.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.additional_check.R
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.subscriber.TwoFactorCheckerSubscriber
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginfingerprint.view.dialog.FingerprintDialogHelper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

class BiometricOfferingActivity: BaseActivity() {

    lateinit var additionalCheckPreference: AdditionalCheckPreference

    private var isWaitingRegisterResult = false

    override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	additionalCheckPreference = AdditionalCheckPreference(this)
	createBiometricOfferingDialog(this)
    }

    fun createBiometricOfferingDialog(activity: FragmentActivity) {
	BottomSheetUnify().apply {
	    val view = View.inflate(activity, R.layout.bottom_sheet_biometric_offering, null)
	    val primaryBtn = view?.findViewById<UnifyButton>(R.id.bottom_sheet_biometric_offering_primary_btn)
	    val mainImgView = view?.findViewById<ImageUnify>(R.id.bottom_sheet_biometric_offering_img)

	    mainImgView?.run {
		Glide.with(activity)
		    .load(FingerprintDialogHelper.BIOMETRIC_OFFERING_MAIN_IMG)
		    .into(this)
	    }

	    primaryBtn?.setOnClickListener {
		val intent = RouteManager.getIntent(
		    requireContext(),
		    ApplinkConstInternalUserPlatform.REGISTER_BIOMETRIC
		)
		isWaitingRegisterResult = true
		activity.startActivityForResult(intent, REQUEST_CODE_REGISTER_BIOMETRIC)
		dismiss()
	    }

	    setCloseClickListener {
		dismiss()
		activity.finish()
	    }

	    setOnDismissListener {
	        if(!isWaitingRegisterResult) {
	            finish()
		}
	    }
	    setChild(view)
	    activity.supportFragmentManager.run {
		show(this, "")
	    }
	}
    }

    fun createBiometricOfferingSuccessDialog(activity: FragmentActivity) {
	BottomSheetUnify().apply {
	    val view = View.inflate(activity, R.layout.bottom_sheet_biometric_offering_success, null)
	    val primaryBtn = view?.findViewById<UnifyButton>(R.id.bottom_sheet_biometric_offering_success_primary_btn)
	    val mainImgView = view?.findViewById<ImageUnify>(R.id.bottom_sheet_biometric_offering_success_img)

	    mainImgView?.run {
		Glide.with(activity)
		    .load(FingerprintDialogHelper.BIOMETRIC_OFFERING_SUCCESS_IMG)
		    .into(this)
	    }

	    primaryBtn?.setOnClickListener {
		onSuccessRegister()
	        finish()
	    }

	    setCloseClickListener {
		dismiss()
	    }

	    setChild(view)
	    activity.supportFragmentManager.run {
		show(this, "")
	    }
	}
    }

    private fun onSuccessRegister() {
	if(additionalCheckPreference.getNextOffer().isNotEmpty()) {
	    val nextIntent = TwoFactorCheckerSubscriber.mapStringToOfferData(additionalCheckPreference, this, additionalCheckPreference.getNextOffer())
	    nextIntent?.run {
		startActivity(nextIntent)
	    }
	}
	finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
	if (requestCode == REQUEST_CODE_REGISTER_BIOMETRIC) {
	    isWaitingRegisterResult = false
	    if(resultCode == Activity.RESULT_OK) {
	        createBiometricOfferingSuccessDialog(this)
	    } else if(resultCode == Activity.RESULT_CANCELED) {
		finish()
	    }
	} else {
	    super.onActivityResult(requestCode, resultCode, data)
	}
    }

    companion object {
	const val REQUEST_CODE_REGISTER_BIOMETRIC = 303
    }
}