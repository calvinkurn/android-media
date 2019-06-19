package com.tokopedia.age_restriction.viewcontroller

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import com.tokopedia.age_restriction.R
import com.tokopedia.age_restriction.viewmodel.ARHomeViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.tradein_common.IAccessRequestListener
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import com.tokopedia.unifycomponents.Toaster

class AgeRestrictionHomeActivity : BaseARActivity<ARHomeViewModel>(), IAccessRequestListener {

    private lateinit var arHomeViewModel: ARHomeViewModel
    private var notAdult = 11
    private var notVerified = 22
    private var notFilledDob = 33
    private var notLogin = 44
    private var selection = 0

    override fun clickAccept() {
        when (selection) {
            notLogin -> {
                navigateToActivityRequest(RouteManager.getIntent(this, ApplinkConst.LOGIN), LOGIN_REQUEST)
                selection = 0
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - login",
                        "before login - {origin url/external} - {destination url}")
            }

            notAdult -> {
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - kembali",
                        "not eligible - {origin url/external} - {destination url}")
                startActivity(RouteManager.getIntent(this, ApplinkConst.HOME)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra("VERIFICATION_SUCCESS", getString(R.string.ar_text_age_not_adult)))
                selection = 0
            }

            notVerified, notFilledDob -> {
                if (selection == notFilledDob) {
                    navigateToActivityRequest(Intent(this, VerifyDOBActivity::class.java), VERIFICATION_REQUEST)
                    sendGeneralEvent(eventClick,
                            event,
                            "click - adult pop up - verifikasi tanggal lahir",
                            "not yet verified - empty DOB - {origin url/external} - {destination url}")
                } else {
                    navigateToActivityRequest(Intent(this, VerifyDOBActivity::class.java)
                            .putExtra(PARAM_EXTRA_DOB, arHomeViewModel.notVerified.value), VERIFICATION_REQUEST)
                    sendGeneralEvent(eventClick,
                            event,
                            "click - adult pop up - benar lanjutkan",
                            "not yet verified - "
                                    + arHomeViewModel.notVerified.value + " - {origin url/external} - {destination url}")
                }
                selection = 0
            }
        }

    }

    override fun clickDeny() {
        when (selection) {
            notLogin -> {
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - kembali",
                        "before login - {origin url/external} - {destination url}")
                finish()
            }
            notAdult -> {
                finish()
            }

            notVerified -> {
                navigateToActivityRequest(Intent(this, VerifyDOBActivity::class.java), VERIFICATION_REQUEST)
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - ubah tanggal lahir",
                        "not yet verified - "
                                + arHomeViewModel.notVerified.value + " - {origin url/external} - {destination url}")
            }

            notFilledDob -> {
                sendGeneralEvent(eventClick,
                        event,
                        "click - adult pop up - kembali",
                        "not yet verified - empty DOB - {origin url/external} - {destination url}")
                finish()
            }
        }

    }

    override fun getViewModelType(): Class<ARHomeViewModel> {
        return ARHomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel?) {
        arHomeViewModel = viewModel as ARHomeViewModel
        arHomeViewModel.getAskUserLogin().observe(this, Observer<Int> {
            selection = notLogin
            sendGeneralEvent(eventView,
                    event,
                    "view - adult pop up - before login",
                    "view - adult pop up - before login")
            showDialogFragment(R.layout.age_restriction_verifcation_dialog, "",
                    getString(R.string.ar_text_login_first),
                    getString(R.string.label_login_button),
                    getString(R.string.ar_label_back))
        })

    }

    override fun getMenuRes(): Int {
        return 0
    }

    override fun getLayoutRes(): Int {
        return R.layout.age_restriction_home_activity
    }

    override fun getTncFragmentInstance(TncResId: Int): Fragment? {
        return null
    }

    override fun getBottomSheetLayoutRes(): Int {
        return 0
    }

    override fun doNeedReattach(): Boolean {
        return false
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun initView() {
        arHomeViewModel.notAdult.observe(this, Observer<Int> {
            selection = notAdult
            showDialogFragment(R.layout.age_restriction_verifcation_dialog, "",
                    getString(R.string.ar_text_not_adult),
                    getString(R.string.ar_text_goto_home),
                    null)
            sendGeneralEvent(eventView,
                    event,
                    "view - adult pop up - not eligible",
                    "not eligible - {origin url/external} - {destination url}")
        })

        arHomeViewModel.userAdult.observe(this, Observer {
            Toaster.showNormalWithAction(this,
                    getString(R.string.ar_text_verification_success),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.general_label_ok), View.OnClickListener { })
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

    override fun onStart() {
        super.onStart()
        arHomeViewModel.notFilled.observe(this, Observer<Int> {
            selection = notFilledDob
            showDialogFragment(R.layout.age_restriction_verifcation_dialog, "",
                    getString(R.string.ar_text_dob_verify),
                    getString(R.string.ar_text_verify_dob),
                    getString(R.string.ar_label_back))
            sendGeneralEvent(eventView,
                    event,
                    "view - adult pop up - not yet verified",
                    "not yet verified - {origin url} - {destination url}")
        })

        arHomeViewModel.notVerified.observe(this, Observer<String> {
            selection = notVerified
            showDialogFragment(R.layout.age_restriction_verifcation_dialog, "",
                    String.format(getString(R.string.ar_text_filled_not_verfied), it),
                    getString(R.string.ar_text_yes_continue),
                    getString(R.string.ar_text_no_update))
            sendGeneralEvent(eventView,
                    event,
                    "view - adult pop up - not yet verified",
                    "not yet verified - {origin url} - {destination url}")
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGIN_REQUEST -> if (resultCode == Activity.RESULT_OK) {
                arHomeViewModel.fetchUserDOB()
            }
            VERIFICATION_REQUEST -> if (resultCode == RESULT_IS_ADULT) {
                setResult(RESULT_IS_ADULT, Intent().putExtra("VERIFICATION_SUCCESS", getString(R.string.ar_text_verification_success)))
                Toaster.showNormalWithAction(this,
                        getString(R.string.ar_text_verification_success),
                        Snackbar.LENGTH_INDEFINITE,
                        getString(R.string.general_label_ok), View.OnClickListener { })
                finish()
            } else if (resultCode == RESULT_IS_NOT_ADULT) {
                selection = notAdult
                clickAccept()
            }
        }
    }
}