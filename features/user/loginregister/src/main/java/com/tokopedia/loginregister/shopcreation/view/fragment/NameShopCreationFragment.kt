package com.tokopedia.loginregister.shopcreation.view.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics.Companion.SCREEN_OPEN_SHOP_CREATION
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.viewmodel.ShopCreationViewModel
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

    private lateinit var toolbarShopCreation: Toolbar
    private lateinit var container: View
    private lateinit var phone: String
    private lateinit var buttonContinue: UnifyButton
    private lateinit var textFieldName: TextFieldUnify

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var shopCreationAnalytics: ShopCreationAnalytics
    @Inject
    lateinit var registerAnalytics: RegisterAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val shopCreationViewModel by lazy {
        viewModelProvider.get(ShopCreationViewModel::class.java)
    }

    override fun getScreenName(): String = SCREEN_OPEN_SHOP_CREATION

    override fun initInjector() = getComponent(ShopCreationComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_name_shop_creation, container, false)
        toolbarShopCreation = view.findViewById(R.id.toolbar_shop_creation)
        this.container = view.findViewById(R.id.container)
        buttonContinue = view.findViewById(R.id.btn_continue)
        textFieldName = view.findViewById(R.id.text_field_name)
        return view
    }

    override fun onStart() {
        super.onStart()
        shopCreationAnalytics.trackScreen(screenName)
        textFieldName.textFieldInput.let {
            it.isFocusableInTouchMode = true
            it.isFocusable = true
        }
    }

    override fun onPause() {
        super.onPause()
        textFieldName.textFieldInput.let {
            it.isFocusableInTouchMode = false
            it.isFocusable = false
        }
    }

    override fun getToolbar(): Toolbar = toolbarShopCreation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVar()
        initView()
        initObserver()
    }

    override fun onBackPressed(): Boolean {
        shopCreationAnalytics.eventClickBackNameShopCreation()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        shopCreationViewModel.addNameResponse.removeObservers(this)
        shopCreationViewModel.registerPhoneAndName.removeObservers(this)
        shopCreationViewModel.flush()
    }

    private fun initVar() {
        val phone = arguments?.getString(ApplinkConstInternalGlobal.PARAM_PHONE, "")
        if (phone != null)
            this.phone = phone
    }

    private fun initView() {
        textFieldName.textFieldInput.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        context?.let {
            textFieldName.textFieldWrapper.setHelperTextColor(ContextCompat.getColorStateList(it, com.tokopedia.unifycomponents.R.color.Unify_N700_68))
        }
        textFieldName.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    when {
                        it.length < MINIMUM_LENGTH -> {
                            textFieldName.setMessage(getString(R.string.error_minimal_name))
                            textFieldName.setError(true)
                            buttonContinue.isEnabled = false
                        }
                        it.length > MAXIMUM_LENGTH -> {
                            textFieldName.setMessage(getString(R.string.error_maximal_name))
                            textFieldName.setError(true)
                            buttonContinue.isEnabled = false
                        }
                        else -> {
                            textFieldName.setMessage(getString(R.string.desc_name_shop_creation))
                            textFieldName.setError(false)
                            buttonContinue.isEnabled = true
                        }
                    }
                }
            }
        })

        textFieldName.textFieldInput.setOnClickListener {
            it.isFocusableInTouchMode = true
            it.isFocusable = true
        }

        textFieldName.textFieldInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                LetUtil.ifLet(context, v) { (context, view) ->
                    showKeyboardFrom(context as Context, view as View)
                }
            }
        }

        container.setOnClickListener {
            textFieldName.textFieldInput.let {
                it.isFocusableInTouchMode = false
                it.isFocusable = false
            }

            LetUtil.ifLet(context, view?.parent) { (context, view) ->
                hideKeyboardFrom(context as Context, view as View)
            }
        }

        if (phone.isEmpty()) {
            buttonContinue.setOnClickListener {
                val name = textFieldName.textFieldInput.text.toString()
                if (name.isNotEmpty()) {
                    buttonContinue.isLoading = true
                    shopCreationViewModel.addName(name)
                } else {
                    emptyStatePhoneField()
                }
            }
        } else {
            buttonContinue.setOnClickListener {
                val name = textFieldName.textFieldInput.text.toString()
                if (name.isNotEmpty()) {
                    buttonContinue.isLoading = true
                    shopCreationViewModel.registerPhoneAndName(phone, name)
                } else {
                    emptyStatePhoneField()
                }
            }
        }
    }

    private fun initObserver() {
        shopCreationViewModel.addNameResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onSuccessAddName()
                }
                is Fail -> {
                    onFailedAddName(it.throwable)
                }
            }
        })
        shopCreationViewModel.registerPhoneAndName.observe(viewLifecycleOwner, Observer {
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

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    private fun onSuccessAddName() {
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
        successRegisterTracking()
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

    private fun successRegisterTracking() {
        registerAnalytics.trackSuccessRegister(
                userSession.loginMethod,
                userSession.userId,
                userSession.name,
                userSession.email,
                userSession.phoneNumber,
                userSession.isGoldMerchant,
                userSession.shopId,
                userSession.shopName
        )
    }

    private fun emptyStatePhoneField() {
        setMessageFieldPhone(getString(R.string.please_fill_name))
    }

    private fun setMessageFieldPhone(message: String) {
        textFieldName.setMessage(message)
    }

    private fun toastError(throwable: Throwable) {
        throwable.message?.let { toastError(it) }
    }

    private fun toastError(message: String) {
        view?.run {
            Toaster.make(this, message, Toaster.toasterLength, Toaster.TYPE_ERROR)
        }
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