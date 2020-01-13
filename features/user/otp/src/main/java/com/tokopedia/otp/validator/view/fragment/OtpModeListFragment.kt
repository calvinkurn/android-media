package com.tokopedia.otp.validator.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.otp.R
import com.tokopedia.otp.common.analytics.OTPAnalytics
import com.tokopedia.otp.validator.data.ModeListData
import com.tokopedia.otp.validator.data.OtpConstant
import com.tokopedia.otp.validator.data.OtpParams
import com.tokopedia.otp.validator.di.ValidatorComponent
import com.tokopedia.otp.validator.view.activity.ValidatorActivity
import com.tokopedia.otp.validator.view.adapter.OtpModeListAdapter
import com.tokopedia.otp.validator.viewmodel.OtpModeListViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_cotp_choose_method.*
import javax.inject.Inject

/**
 * @author rival
 * @created on 9/12/2019
 */

class OtpModeListFragment : BaseDaggerFragment(), OtpModeListAdapter.ClickListener {

    @Inject
    lateinit var analytics: OTPAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val otpModeListViewModel by lazy { viewModelProvider.get(OtpModeListViewModel::class.java) }

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var otpParams: OtpParams
    private lateinit var otpModeListAdapter: OtpModeListAdapter

    override fun getScreenName(): String = OTPAnalytics.Screen.SCREEN_SELECT_VERIFICATION_METHOD

    override fun initInjector() {
        getComponent(ValidatorComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            arguments != null -> {
                otpParams = arguments?.getParcelable(OtpConstant.OTP_PARAMS) as OtpParams
            }
            savedInstanceState != null -> {
                otpParams = savedInstanceState.getParcelable(OtpConstant.OTP_PARAMS) as OtpParams
            }
            else -> {
                activity?.setResult(Activity.RESULT_CANCELED)
                activity?.finish()

            }
        }
    }

    override fun onStart() {
        super.onStart()
        analytics.sendScreen(activity, screenName)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(OtpConstant.OTP_PARAMS, otpParams)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cotp_choose_method, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        getOtpModeList()
        prepareView()
    }

    private fun prepareView() {
        phone_inactive?.hide()
        
        otpModeListAdapter = OtpModeListAdapter.createInstance(this)
        method_list?.adapter = otpModeListAdapter
        method_list?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private fun initObserver() {
        otpModeListViewModel.modeListResponse.observe(this, Observer {
            hideLoading()
            onSuccessGetOtpModeList(it)
        })

        otpModeListViewModel.error.observe(this, Observer {
            hideLoading()
            onError(it)
        })
    }

    private fun getOtpModeList() {
        otpModeListViewModel.getMethodList(otpParams)
        showLoading()
    }

    override fun onModeListClick(modeList: ModeListData, position: Int) {
        analytics.eventClickMethodOtp(otpParams.otpType, modeList.modeText)

        if (modeList.usingPopUp && modeList.popUpHeader.isNotEmpty() && modeList.popUpBody.isNotEmpty()) {
            showInterruptDialog(modeList)
        } else if (activity != null && activity is ValidatorActivity) {
            if (modeList.modeText == OtpConstant.OtpMode.MISCALL && otpParams.otpType == OtpConstant.OtpType.REGISTER_PHONE_NUMBER) {
                // TODO gotoOtpMiscallPage
            } else {
                (activity as ValidatorActivity).goToValidatorPage(otpParams, modeList)
            }
        }
    }

    private fun showInterruptDialog(modeList: ModeListData) {
        (activity as ValidatorActivity).goToValidatorPage(otpParams, modeList)
    }

    private fun onSuccessGetOtpModeList(modeList: MutableList<ModeListData>) {
        otpModeListAdapter.setList(modeList)
    }

    private fun onError(throwable: Throwable) {
        if (!isAdded) return

        if (throwable.message?.isNotEmpty() as Boolean) {
            NetworkErrorHelper.showEmptyState(activity, main_view, throwable.message) {
                getOtpModeList()
            }
        } else {
            NetworkErrorHelper.showEmptyState(activity, main_view) {
                getOtpModeList()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        otpModeListViewModel.clear()
    }

    private fun showLoading() {
        progress_bar?.show()
        main_view?.hide()
    }

    private fun hideLoading() {
        progress_bar?.hide()
        main_view?.show()
    }

    companion object {
        fun createInstance(otpParams: OtpParams): Fragment {
            val fragment = OtpModeListFragment()
            val bundle = Bundle()
            bundle.putParcelable(OtpConstant.OTP_PARAMS, otpParams)
            fragment.arguments = bundle
            return fragment
        }
    }
}

