package com.tokopedia.loginregister.shopcreation.view.fragment

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics.Companion.SCREEN_OPEN_SHOP_CREATION
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.viewmodel.ShopCreationViewModel
import com.tokopedia.profilecommon.domain.pojo.UserProfileUpdate
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-12-18.
 * ade.hadian@tokopedia.com
 */

class NameShopCreationFragment : BaseShopCreationFragment(), IOnBackPressed {

    lateinit var toolbarShopCreation: Toolbar
    lateinit var phone: String
    lateinit var buttonContinue: UnifyButton
    lateinit var textFieldName: TextFieldUnify

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var shopCreationAnalytics: ShopCreationAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val shopCreationViewModel by lazy { viewModelProvider.get(ShopCreationViewModel::class.java) }

    override fun getScreenName(): String = SCREEN_OPEN_SHOP_CREATION

    override fun initInjector() = getComponent(ShopCreationComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_name_shop_creation, container, false)
        toolbarShopCreation = view.findViewById(R.id.toolbar_shop_creation)
        buttonContinue = view.findViewById(R.id.btn_continue)
        textFieldName = view.findViewById(R.id.text_field_name)
        return view
    }

    override fun onStart() {
        super.onStart()
        shopCreationAnalytics.trackScreen(screenName)
    }

    override fun getToolbar(): Toolbar = toolbarShopCreation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
        initView()
        initObserver()
    }

    private fun initVar() {
        val phone = arguments?.getString(ApplinkConstInternalGlobal.PARAM_PHONE, "")
        if(phone != null)
            this.phone = phone
    }

    private fun initView() {
        textFieldName.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    buttonContinue.isEnabled = it.length in MINIMUM_LENGTH..MAXIMUM_LENGTH
                }
            }
        })

        if(phone.isEmpty()) {
            buttonContinue.setOnClickListener {
                val name = textFieldName.textFieldInput.text.toString()
                if(name.isNotEmpty()) {
                    buttonContinue.isLoading = true
                    shopCreationViewModel.addName(name)
                } else {
                    emptyStatePhoneField()
                }
            }
        } else {
            buttonContinue.setOnClickListener {
                val name = textFieldName.textFieldInput.text.toString()
                if(name.isNotEmpty()) {
                    buttonContinue.isLoading = true
                    shopCreationViewModel.registerPhoneAndName(phone, name)
                } else {
                    emptyStatePhoneField()
                }
            }
        }
    }

    private fun initObserver() {
        shopCreationViewModel.addNameResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessAddName(it.data)
                }
                is Fail -> {
                    onFailedAddName(it.throwable)
                }
            }
        })
        shopCreationViewModel.registerPhoneAndName.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessRegisterPhoneAndName(it.data)
                }
                is Fail -> {
                    onFailedRegisterPhoneAndName(it.throwable)
                }
            }
        })
    }

    private fun onSuccessAddName(userProfileUpdate: UserProfileUpdate) {
        shopCreationAnalytics.eventSuccessClickContinueNameShopCreation()
        buttonContinue.isLoading = false
        activity?.let {
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    private fun onFailedAddName(throwable: Throwable) {
        shopCreationAnalytics.eventFailedClickContinueNameShopCreation()
        toastError(throwable)
        buttonContinue.isLoading = false
    }

    private fun onSuccessRegisterPhoneAndName(registerInfo: RegisterInfo) {
        shopCreationAnalytics.eventSuccessClickContinueNameShopCreation()
        userSession.clearToken()
        userSession.setToken(registerInfo.accessToken, "Bearer", registerInfo.refreshToken)
        buttonContinue.isLoading = false
        activity?.let {
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    private fun onFailedRegisterPhoneAndName(throwable: Throwable) {
        shopCreationAnalytics.eventFailedClickContinueNameShopCreation()
        userSession.clearToken()
        toastError(throwable)
        buttonContinue.isLoading = false
    }

    private fun emptyStatePhoneField() {
        setMessageFieldPhone(getString(R.string.please_fill_name))
    }

    private fun setMessageFieldPhone(message: String) {
        textFieldName.setMessage(message)
    }

    private fun clearMessageFieldPhone() {
        textFieldName.setMessage("")
    }

    private fun toastError (throwable: Throwable) {
        throwable.message?.let { toastError(it) }
    }

    private fun toastError (message: String) {
        view?.run {
            Toaster.make(this, message, Toaster.toasterLength, Toaster.TYPE_ERROR)
        }
    }

    override fun onBackPressed(): Boolean {
        shopCreationAnalytics.eventClickBackNameShopCreation()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        shopCreationViewModel.addNameResponse.removeObservers(this)
        shopCreationViewModel.registerPhoneAndName.removeObservers(this)
        shopCreationViewModel.clear()
    }

    companion object {

        const val MINIMUM_LENGTH = 3
        const val MAXIMUM_LENGTH = 35

        fun createInstance(bundle: Bundle): NameShopCreationFragment {
            val fragment = NameShopCreationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}