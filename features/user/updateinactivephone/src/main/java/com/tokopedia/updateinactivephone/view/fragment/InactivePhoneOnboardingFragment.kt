package com.tokopedia.updateinactivephone.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_PHONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.STATUS_MULTIPLE_ACCOUNT
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.STATUS_SUCCESS
import com.tokopedia.updateinactivephone.common.UserDataTemporary
import com.tokopedia.updateinactivephone.di.DaggerInactivePhoneComponent
import com.tokopedia.updateinactivephone.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.view.InactivePhoneTracker
import com.tokopedia.updateinactivephone.view.activity.InactivePhoneAccountListActivity
import com.tokopedia.updateinactivephone.view.viewmodel.InactivePhoneOnboardingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding.*
import javax.inject.Inject

class InactivePhoneOnboardingFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var userDataTemp: UserDataTemporary

    @Inject
    lateinit var tracker: InactivePhoneTracker

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(InactivePhoneOnboardingViewModel::class.java) }

    private lateinit var fragmentTransactionInterface: FragmentTransactionInterface

    private lateinit var phoneNumber: String
    private lateinit var email: String

    override fun getScreenName(): String = ""
    override fun initInjector() {
        DaggerInactivePhoneComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .inactivePhoneModule(InactivePhoneModule(requireContext()))
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
        fragmentTransactionInterface = activity as FragmentTransactionInterface

        arguments?.let {
            if (it.isEmpty) return

            phoneNumber = replaceRegionPhoneCode(it.getString(PARAM_PHONE, ""))
            email = it.getString(PARAM_EMAIL, "")

            if (email.isNotEmpty()) userDataTemp.setEmail(email)
            if (phoneNumber.isNotEmpty()) userDataTemp.setOldPhone(phoneNumber)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext?.setOnClickListener {
            tracker.clickOnNextButtonOnboarding()

            showLoading()
            viewModel.phoneValidation(userDataTemp.getOldPhone(), userDataTemp.getEmail())
        }
    }

    private fun initObserver() {
        viewModel.phoneValidation.observe(this, Observer {
            hideLoading()

            when (it) {
                is Success -> {
                    if (it.data.validation.status == STATUS_SUCCESS) {
                        userDataTemp.setIndex(1)
                        gotoOnboardingIdCardPage()
                    } else if (it.data.validation.status == STATUS_MULTIPLE_ACCOUNT) {
                        gotoAccountListPage(userDataTemp.getOldPhone())
                    }
                }
                is Fail -> {
                    view?.let { view ->
                        Toaster.make(view, it.throwable.message.toString(), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSE_ACCOUNT && resultCode == Activity.RESULT_OK) {
            gotoOnboardingIdCardPage()
        }
    }

    private fun gotoOnboardingIdCardPage() {
        fragmentTransactionInterface.replace(InactivePhoneOnboardingIdCardFragment())
    }

    private fun gotoAccountListPage(phoneNumber: String) {
        context?.let {
            val intent = InactivePhoneAccountListActivity.createIntent(it, phoneNumber)
            startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
        }
    }

    private fun showLoading() {
        loader?.show()
        btnNext.isEnabled = false
    }

    private fun hideLoading() {
        loader?.hide()
        btnNext.isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
        viewModel.phoneValidation.removeObservers(this)
    }

    private fun replaceRegionPhoneCode(phoneNumber: String): String {
        val regionRegex = Regex(REGEX_PHONE_NUMBER_REGION)
        val symbolRegex = Regex(REGEX_PHONE_NUMBER)
        var result = phoneNumber

        if (phoneNumber.contains(regionRegex)) {
            result = phoneNumber.replace(regionRegex, "0")
        }

        return result.replace(symbolRegex, "")
    }

    companion object {
        private const val REQUEST_CHOOSE_ACCOUNT = 100

        private const val REGEX_PHONE_NUMBER = """[+()\-\s]"""
        private const val REGEX_PHONE_NUMBER_REGION = "^(\\+\\d{1,2})"

        fun createInstance(bundle: Bundle): Fragment = InactivePhoneOnboardingFragment().apply {
            arguments = bundle
        }
    }
}