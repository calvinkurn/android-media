package com.tokopedia.loginregister.shopcreation.view.nameshop

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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics.Companion.SCREEN_OPEN_SHOP_CREATION
import com.tokopedia.loginregister.databinding.FragmentNameShopCreationBinding
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.view.base.BaseShopCreationFragment
import com.tokopedia.loginregister.shopcreation.view.ShopCreationViewModel
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-12-18.
 * ade.hadian@tokopedia.com
 */

class NameShopCreationFragment : BaseShopCreationFragment(), IOnBackPressed {

    private lateinit var phone: String
    private var validateToken: String = ""

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var shopCreationAnalytics: ShopCreationAnalytics
    @Inject
    lateinit var registerAnalytics: RegisterAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val shopCreationViewModel by lazy {
        viewModelProvider.get(ShopCreationViewModel::class.java)
    }
    
    private var viewBinding by autoClearedNullable<FragmentNameShopCreationBinding>()

    override fun getScreenName(): String = SCREEN_OPEN_SHOP_CREATION

    override fun initInjector() = getComponent(ShopCreationComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentNameShopCreationBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onStart() {
        super.onStart()
        shopCreationAnalytics.trackScreen(screenName)
        viewBinding?.textFieldName?.textFieldInput.let {
            it?.isFocusableInTouchMode = true
            it?.isFocusable = true
        }
    }

    override fun onPause() {
        super.onPause()
        viewBinding?.textFieldName?.textFieldInput.let {
            it?.isFocusableInTouchMode = false
            it?.isFocusable = false
        }
    }

    override fun getToolbar(): Toolbar? = viewBinding?.toolbarShopCreation

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
    }

    private fun initVar() {
        val phone = arguments?.getString(ApplinkConstInternalGlobal.PARAM_PHONE, "")
        if (phone != null)
            this.phone = phone
        validateToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
    }

    private fun initView() {
        viewBinding?.textFieldName?.textFieldInput?.setTextColor(
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
        )
        context?.let {
            viewBinding?.textFieldName?.textFieldWrapper?.setHelperTextColor(
                ContextCompat.getColorStateList(it, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
            )
        }
        viewBinding?.textFieldName?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    when {
                        it.length < MINIMUM_LENGTH -> {
                            viewBinding?.textFieldName?.setMessage(getString(R.string.error_minimal_name))
                            viewBinding?.textFieldName?.setError(true)
                            viewBinding?.btnContinue?.isEnabled = false
                        }
                        it.length > MAXIMUM_LENGTH -> {
                            viewBinding?.textFieldName?.setMessage(getString(R.string.error_maximal_name))
                            viewBinding?.textFieldName?.setError(true)
                            viewBinding?.btnContinue?.isEnabled = false
                        }
                        else -> {
                            viewBinding?.textFieldName?.setMessage(getString(R.string.desc_name_shop_creation))
                            viewBinding?.textFieldName?.setError(false)
                            viewBinding?.btnContinue?.isEnabled = true
                        }
                    }
                }
            }
        })

        viewBinding?.textFieldName?.textFieldInput?.setOnClickListener {
            it.isFocusableInTouchMode = true
            it.isFocusable = true
        }

        viewBinding?.textFieldName?.textFieldInput?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                LetUtil.ifLet(context, v) { (context, view) ->
                    showKeyboardFrom(context as Context, view as View)
                }
            }
        }

        viewBinding?.container?.setOnClickListener {
            viewBinding?.textFieldName?.textFieldInput.let {
                it?.isFocusableInTouchMode = false
                it?.isFocusable = false
            }

            LetUtil.ifLet(context, view?.parent) { (context, view) ->
                hideKeyboardFrom(context as Context, view as View)
            }
        }

        if (phone.isEmpty()) {
            viewBinding?.btnContinue?.setOnClickListener {
                val name = viewBinding?.textFieldName?.textFieldInput?.text.toString()
                if (name.isNotEmpty()) {
                    viewBinding?.btnContinue?.isLoading = true
                    shopCreationViewModel.addName(name, validateToken)
                } else {
                    emptyStatePhoneField()
                }
            }
        } else {
            viewBinding?.btnContinue?.setOnClickListener {
                val name = viewBinding?.textFieldName?.textFieldInput?.text.toString()
                if (name.isNotEmpty()) {
                    viewBinding?.btnContinue?.isLoading = true
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
        viewBinding?.btnContinue?.isLoading = false
        activity?.let {
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    private fun onFailedAddName(throwable: Throwable) {
        shopCreationAnalytics.eventFailedClickContinueNameShopCreation()
        toastError(throwable)
        viewBinding?.btnContinue?.isLoading = false
    }

    private fun onSuccessRegisterPhoneAndName(registerInfo: RegisterInfo) {
        shopCreationAnalytics.eventSuccessClickContinueNameShopCreation()
        successRegisterTracking()
        userSession.clearToken()
        userSession.setToken(registerInfo.accessToken, "Bearer", EncoderDecoder.Encrypt(registerInfo.refreshToken, userSession.refreshTokenIV))
        viewBinding?.btnContinue?.isLoading = false
        activity?.let {
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    private fun onFailedRegisterPhoneAndName(throwable: Throwable) {
        shopCreationAnalytics.eventFailedClickContinueNameShopCreation()
        userSession.clearToken()
        toastError(throwable)
        viewBinding?.btnContinue?.isLoading = false
    }

    private fun successRegisterTracking() {
        registerAnalytics.trackSuccessRegister(
                userSession.loginMethod,
                userSession.userId,
                userSession.isGoldMerchant,
                userSession.shopId,
                userSession.shopName
        )
    }

    private fun emptyStatePhoneField() {
        setMessageFieldPhone(getString(R.string.please_fill_name))
    }

    private fun setMessageFieldPhone(message: String) {
        viewBinding?.textFieldName?.setMessage(message)
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
