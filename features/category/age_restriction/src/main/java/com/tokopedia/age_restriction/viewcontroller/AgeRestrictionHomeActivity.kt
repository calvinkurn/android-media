package com.tokopedia.age_restriction.viewcontroller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.age_restriction.R
import com.tokopedia.age_restriction.viewmodel.ARHomeViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.design.dialog.AccessRequestDialogFragment
import com.tokopedia.design.dialog.IAccessRequestListener
import com.tokopedia.track.TrackApp
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.age_restriction_home_activity.*
import javax.inject.Inject

class AgeRestrictionHomeActivity : BaseARActivity<ARHomeViewModel>(), IAccessRequestListener {

    private lateinit var arHomeViewModel: ARHomeViewModel
    private var notAdult = 11
    private var notVerified = 22
    private var notFilledDob = 33
    private var notLogin = 44
    private var selection = 0
    private var isLoggedIn = false

    companion object {
        private const val EXTRA_IS_LOGIN = "is_login"

    }

    @Inject
    lateinit var viewModelProvider:  ViewModelProvider.Factory


    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().inject(this)
    }

    override fun clickAccept() {
        sendGeneralEvent(AccessRequestDialogFragment.STATUS_AGREE)
        when (selection) {
            notLogin -> {
                navigateToActivityRequest(RouteManager.getIntent(this, ApplinkConst.LOGIN), LOGIN_REQUEST)
                selection = 0
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - login",
                        "before login - $event/$destinationUrlGtm")
            }

            notAdult -> {
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - kembali",
                        "not eligible - $destinationUrlGtm - $event/$destinationUrlGtm")
                startActivity(RouteManager.getIntent(this, ApplinkConst.HOME)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                selection = 0
            }

            notVerified, notFilledDob -> {
                if (selection == notFilledDob) {
                    navigateToActivityRequest(Intent(this, VerifyDOBActivity::class.java)
                            .putExtra("DESTINATION_GTM", destinationUrlGtm)
                            .putExtra("ORIGIN", origin),
                            VERIFICATION_REQUEST)
                    sendGeneralEvent(eventClick,
                            event,
                            "click - adult pop up - verifikasi tanggal lahir",
                            "not yet verified - empty DOB - $destinationUrlGtm - $event/$destinationUrlGtm")
                } else {
                    navigateToActivityRequest(Intent(this, VerifyDOBActivity::class.java)
                            .putExtra("DESTINATION_GTM", destinationUrlGtm)
                            .putExtra("ORIGIN", origin)
                            .putExtra(PARAM_EXTRA_DOB, arHomeViewModel.notVerified.value), VERIFICATION_REQUEST)
                    sendGeneralEvent(eventClick,
                            event,
                            "click - adult pop up - benar lanjutkan",
                            "not yet verified - "
                                    + arHomeViewModel.notVerified.value + " - $destinationUrlGtm - $event/$destinationUrlGtm")
                }
                selection = 0
            }
        }

    }

    override fun clickDeny() {
        sendGeneralEvent(AccessRequestDialogFragment.STATUS_DENY)
        when (selection) {
            notLogin -> {
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - kembali",
                        "before login - $destinationUrlGtm - $event/$destinationUrlGtm")
                finish()
            }
            notAdult -> {
                finish()
            }

            notVerified -> {
                navigateToActivityRequest(Intent(this, VerifyDOBActivity::class.java)
                        .putExtra("DESTINATION_GTM", destinationUrlGtm)
                        .putExtra("ORIGIN", origin),
                        VERIFICATION_REQUEST)
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - ubah tanggal lahir",
                        "not yet verified - "
                                + arHomeViewModel.notVerified.value + " - $destinationUrlGtm - $event/$destinationUrlGtm")
            }

            notFilledDob -> {
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - kembali",
                        "not yet verified - empty DOB - $destinationUrlGtm - $event/$destinationUrlGtm")
                finish()
            }
        }

    }

    override fun getViewModelType(): Class<ARHomeViewModel> {
        return ARHomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        arHomeViewModel = viewModel as ARHomeViewModel
        arHomeViewModel.getAskUserLogin().observe(this, Observer<Int> {
            selection = notLogin
            sendGeneralEvent(eventView,
                    event,
                    "view - adult pop up - before login",
                    "before login - $event/$destinationUrlGtm")
            showDialogFragment(getString(R.string.ar_text_adult_content),
                    getString(R.string.ar_text_login_first),
                    getString(com.tokopedia.design.R.string.label_login_button),
                    getString(R.string.ar_label_back))
        })

    }

    override fun getMenuRes(): Int {
        return 0
    }

    override fun showProgressBar() {
        progress_bar_layout.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar_layout.visibility = View.GONE
    }

    override fun getLayoutRes(): Int {
        return R.layout.age_restriction_home_activity
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun initView() {
        arHomeViewModel.notAdult.observe(this, Observer<Int> {
            selection = notAdult
            showDialogFragment(getString(R.string.ar_text_adult_content),
                    getString(R.string.ar_text_not_adult),
                    getString(R.string.ar_text_goto_home),
                    null)
            sendGeneralEvent(eventView,
                    event,
                    "view - adult pop up - not eligible",
                    "not eligible - $destinationUrlGtm - $event/$destinationUrlGtm")
        })

        arHomeViewModel.userAdult.observe(this, Observer {
            val intent = Intent()
            val bundle = Bundle()
            if (isLoggedIn) {
                bundle.putBoolean(EXTRA_IS_LOGIN, true)
            }

            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        })
    }


    override fun onStart() {
        super.onStart()
        arHomeViewModel.notFilled.observe(this, Observer<Int> {
            selection = notFilledDob
            showDialogFragment(getString(R.string.ar_text_adult_content),
                    getString(R.string.ar_text_dob_verify),
                    getString(R.string.ar_text_verify_dob),
                    getString(R.string.ar_label_back), getResId())
            sendGeneralEvent(eventView,
                    event,
                    "view - adult pop up - not yet verified",
                    "not yet verified - $event/$destinationUrlGtm - $event/$destinationUrlGtm")
        })

        arHomeViewModel.notVerified.observe(this, Observer<String> {
            selection = notVerified
            showDialogFragment(getString(R.string.ar_text_adult_content),
                    String.format(getString(R.string.ar_text_filled_not_verfied), it),
                    getString(R.string.ar_text_yes_continue),
                    getString(R.string.ar_text_no_update))
            sendGeneralEvent(eventView,
                    event,
                    "view - adult pop up - not yet verified",
                    "not yet verified - $event/$destinationUrlGtm")
        })
    }

    private fun getResId() = R.layout.age_restriction_verifcation_dialog

    private fun showDialogFragment(titleText: String, bodyText: String, positiveButton: String, negativeButton: String?, layoutResId: Int = 0) {
        val fragmentManager = supportFragmentManager
        val accessDialog = AccessRequestDialogFragment.newInstance()
        accessDialog.setLayoutResId(layoutResId)
        accessDialog.setBodyText(bodyText)
        accessDialog.setTitle(titleText)
        accessDialog.setPositiveButton(positiveButton)
        accessDialog.setNegativeButton(negativeButton)
        accessDialog.show(fragmentManager, AccessRequestDialogFragment.TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGIN_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    isLoggedIn = true
                    arHomeViewModel.fetchUserDOB()
                } else {
                    selection = notLogin
                    showDialogFragment(getString(R.string.ar_text_adult_content),
                        getString(R.string.ar_text_login_first),
                        getString(com.tokopedia.design.R.string.label_login_button),
                        getString(R.string.ar_label_back))
                }
            }
            VERIFICATION_REQUEST -> if (resultCode == RESULT_IS_ADULT) {
                setResult(RESULT_IS_ADULT, Intent().putExtra(ApplinkConstInternalCategory.PARAM_EXTRA_SUCCESS, getString(R.string.ar_text_verification_success)))
                finish()
            } else if (resultCode == RESULT_IS_NOT_ADULT) {
                startActivity(RouteManager.getIntent(this, ApplinkConst.HOME)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(ApplinkConstInternalCategory.PARAM_EXTRA_SUCCESS, getString(R.string.ar_text_age_not_adult)))
            }
        }
    }

    private fun sendGeneralEvent(label: String) {
        val trackApp = TrackApp.getInstance()
        trackApp.gtm.sendGeneralEvent("clickPDP",
                "product detail page",
                "click - asking permission trade in",
                label)
    }

    private fun sendGeneralEvent(event: String, category: String, action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(event,
                category,
                action,
                label)
    }
}