package com.tokopedia.otp.verification.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.otp.common.analytics.OTPAnalytics
import com.tokopedia.otp.verification.common.di.VerificationComponent
import com.tokopedia.otp.verification.domain.data.ModeListData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpModeListData
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.adapter.VerificationMethodAdapter
import com.tokopedia.otp.verification.view.viewbinding.VerificationMethodViewBinding
import com.tokopedia.otp.verification.viewmodel.VerificationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Ade Fulki on 02/06/20.
 */

class VerificationMethodFragment : BaseVerificationFragment() {

    @Inject
    lateinit var analytics: OTPAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var otpData: OtpData
    private lateinit var adapter: VerificationMethodAdapter

    private val viewmodel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(VerificationViewModel::class.java)
    }

    override var viewBound = VerificationMethodViewBinding()

    override fun getScreenName(): String = OTPAnalytics.Screen.SCREEN_SELECT_VERIFICATION_METHOD

    override fun initInjector() = getComponent(VerificationComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpData = arguments?.getParcelable(OtpConstant.OTP_DATA_EXTRA) ?: OtpData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initView()
        getVerificationMethod()
    }

    override fun onStart() {
        super.onStart()
        analytics.sendScreen(activity, screenName)
    }

    override fun onDestroy() {
        viewmodel.getVerificationMethodResult.removeObservers(this)
        viewmodel.flush()
        super.onDestroy()
    }

    private fun initView() {
        (activity as VerificationActivity).title = "Verifikasi"

        adapter = VerificationMethodAdapter.createInstance(object : VerificationMethodAdapter.ClickListener {
            override fun onModeListClick(modeList: ModeListData, position: Int) {
                analytics.eventClickMethodOtp(otpData.otpType, modeList.modeText)

                if(modeList.modeText == OtpConstant.OtpMode.MISCALL && otpData.otpType == OtpConstant.OtpType.REGISTER_PHONE_NUMBER) {
                    (activity as VerificationActivity).goToOnboardingMiscallPage(modeList)
                } else {
                    (activity as VerificationActivity).goToVerificationPage(modeList)
                }
            }
        })
        viewBound.methodList?.adapter = adapter
        viewBound.methodList?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,  false)
    }

    private fun getVerificationMethod() {
        showLoading()
        viewmodel.getVerificationMethod(
                otpType = otpData.otpType.toString(),
                userId = otpData.userId,
                msisdn = otpData.msisdn,
                email = otpData.email
        )
    }

    private fun initObserver() {
        viewmodel.getVerificationMethodResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetVerificationMethod().invoke(it.data)
                is Fail -> onFailedGetVerificationMethod().invoke(it.throwable)
            }
        })
    }

    private fun onSuccessGetVerificationMethod(): (OtpModeListData) -> Unit {
        return { otpModeListData ->
            if(otpModeListData.success && otpModeListData.modeList.isNotEmpty()){
                hideLoading()
                adapter.setList(otpModeListData.modeList)
            } else if(otpModeListData.errorMessage.isEmpty()){
                onFailedGetVerificationMethod().invoke(MessageErrorException(otpModeListData.errorMessage))
            } else {
                onFailedGetVerificationMethod().invoke(Throwable())
            }
        }
    }

    private fun onFailedGetVerificationMethod(): (Throwable) -> Unit {
        return { throwable ->
            throwable.printStackTrace()
            hideLoading()
            val message = ErrorHandler.getErrorMessage(context, throwable)
            NetworkErrorHelper.showEmptyState(context, viewBound.containerView, message) {
                getVerificationMethod()
            }
        }
    }

    private fun showLoading() {
        viewBound.loader?.show()
        viewBound.containerView?.hide()
    }

    private fun hideLoading() {
        viewBound.loader?.hide()
        viewBound.containerView?.show()
    }

    companion object {

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = VerificationMethodFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}