package com.tokopedia.additional_check.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.additional_check.R
import com.tokopedia.additional_check.data.pref.AdditionalCheckPreference
import com.tokopedia.additional_check.databinding.BottomSheetBiometricOfferingBinding
import com.tokopedia.additional_check.di.AdditionalCheckModules
import com.tokopedia.additional_check.di.DaggerAdditionalCheckComponents
import com.tokopedia.additional_check.internal.TwoFactorTracker
import com.tokopedia.additional_check.subscriber.TwoFactorCheckerSubscriber
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginfingerprint.view.dialog.FingerprintDialogHelper
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

class BiometricOfferingActivity: BaseActivity() {

    @Inject
    lateinit var additionalCheckPreference: AdditionalCheckPreference

    private val twoFactorTracker = TwoFactorTracker()

    private var binding: BottomSheetBiometricOfferingBinding? = null

    private fun initInjector() {
	DaggerAdditionalCheckComponents
	    .builder()
	    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
	    .additionalCheckModules(AdditionalCheckModules())
	    .build()
	    .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	initInjector()
	binding = BottomSheetBiometricOfferingBinding.inflate(layoutInflater)
	setContentView(binding?.root)

	twoFactorTracker.viewBiometricPopup()

	initOfferingViews()
    }

    fun initOfferingViews() {
	binding?.run {
	    bottomSheetBiometricOfferingImg.loadImage(FingerprintDialogHelper.BIOMETRIC_OFFERING_MAIN_IMG)
	    bottomSheetBiometricOfferingPrimaryBtn.setOnClickListener {
		twoFactorTracker.clickRegisterBiometric()
		val intent = RouteManager.getIntent(
		    this@BiometricOfferingActivity,
		    ApplinkConstInternalUserPlatform.REGISTER_BIOMETRIC
		)
		startActivityForResult(intent, REQUEST_CODE_REGISTER_BIOMETRIC)
	    }

	    bottomSheetBiometricOfferingToolbar.setOnClickListener {
	        onBackPressed()
	    }
	}

    }

    override fun onBackPressed() {
	super.onBackPressed()
	twoFactorTracker.clickCloseBiometric()
    }

    fun createBiometricOfferingSuccessDialog(activity: FragmentActivity) {
	BottomSheetUnify().apply {
	    val view = View.inflate(activity, R.layout.bottom_sheet_biometric_offering_success, null)
	    val primaryBtn = view?.findViewById<UnifyButton>(R.id.bottom_sheet_biometric_offering_success_primary_btn)
	    val mainImgView = view?.findViewById<ImageUnify>(R.id.bottom_sheet_biometric_offering_success_img)

	    mainImgView?.loadImage(FingerprintDialogHelper.BIOMETRIC_OFFERING_SUCCESS_IMG)

	    primaryBtn?.setOnClickListener {
		twoFactorTracker.clickContinueShoppingWhenSuccess()
		onFinishDialogSuccess()
	    }

	    setCloseClickListener {
		twoFactorTracker.clickCloseWhenSuccess()
		onFinishDialogSuccess()
	    }

	    setChild(view)
	    activity.supportFragmentManager.run {
		show(this, "")
	    }
	}
    }

    private fun onFinishDialogSuccess() {
	onSuccessRegister()
	finish()
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
	    if(resultCode == Activity.RESULT_OK) {
	        createBiometricOfferingSuccessDialog(this)
		twoFactorTracker.successAddBiometric()
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