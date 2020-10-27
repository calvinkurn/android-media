package com.tokopedia.updateinactivephone.revamp.view.fragment

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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_USER_ID
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.revamp.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.revamp.common.UserDataTemporary
import com.tokopedia.updateinactivephone.revamp.di.DaggerInactivePhoneComponent
import com.tokopedia.updateinactivephone.revamp.di.module.InactivePhoneModule
import com.tokopedia.updateinactivephone.revamp.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.revamp.view.activity.InactivePhoneAccountListActivity
import com.tokopedia.updateinactivephone.revamp.view.viewmodel.InactivePhoneOnboardingViewModel
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
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(InactivePhoneOnboardingViewModel::class.java) }

    private lateinit var fragmentTransactionInterface: FragmentTransactionInterface

    private lateinit var phoneNumber: String
    private lateinit var email: String

    @Deprecated("Delet Soon")
    private lateinit var userId: String

    override fun getScreenName(): String = ""
    override fun initInjector() {
        DaggerInactivePhoneComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .inactivePhoneModule(InactivePhoneModule(requireContext()))
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initObserver()
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentTransactionInterface = activity as FragmentTransactionInterface

        arguments?.let {
            /**- Test =========-==-=0-90909-=-0=-=-0=-0=-0=-0=0=-*/
            phoneNumber = it.getString(PARAM_PHONE, "")
            email = it.getString(PARAM_EMAIL, "")
            userId = it.getString(PARAM_USER_ID, "")
            /**-=========-==-=0-90909-=-0=-=-0=-0=-0=-0=0=-*/
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNext?.setOnClickListener {
            // need improvement on login / register page to set login method
            if (userSession.loginMethod == UserSessionInterface.LOGIN_METHOD_PHONE) {
                viewModel.phoneValidation(phoneNumber)
            } else {
                gotoOnboardingIdCardPage()
            }
        }


        /**- Test =========-==-=0-90909-=-0=-=-0=-0=-0=-0=0=-*/
        userDataTemp.setUserId(userId)
        userDataTemp.setEmail(email)
        userDataTemp.setOldPhone(phoneNumber)
        /**-=========-==-=0-90909-=-0=-=-0=-0=-0=-0=0=-*/
    }

    private fun initObserver() {
        viewModel.phoneValidation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.validation.status == 1) {
                        gotoOnboardingIdCardPage()
                        userDataTemp.setIndex(1)
                    } else if (it.data.validation.status == 2) {
                        gotoAccountListPage(phoneNumber)
                    }
                }
                is Fail -> {

                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSE_ACCOUNT && resultCode == Activity.RESULT_OK) {
            val userData = data?.extras?.getParcelable<AccountListDataModel.UserDetailDataModel>(InactivePhoneConstant.PARAM_USER_DETAIL_DATA)
            userData?.let {
                /**- Test =========-==-=0-90909-=-0=-=-0=-0=-0=-0=0=-*/
//                userDataTemp.setUserId(userData.userId)
//                userDataTemp.setEmail(email)
//                userDataTemp.setOldPhone(phoneNumber)
                /**-=========-==-=0-90909-=-0=-=-0=-0=-0=-0=0=-*/
            }
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

    companion object {
        private const val REQUEST_CHOOSE_ACCOUNT = 100

        fun createInstance(bundle: Bundle): Fragment = InactivePhoneOnboardingFragment().apply {
            arguments = bundle
        }
    }
}