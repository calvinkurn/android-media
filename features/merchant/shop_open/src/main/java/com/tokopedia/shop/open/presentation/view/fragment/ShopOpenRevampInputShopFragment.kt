package com.tokopedia.shop.open.presentation.view.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.header.HeaderUnify
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainSuggestionResult
import com.tokopedia.shop.open.R
import com.tokopedia.shop.open.analytic.ShopOpenRevampTracking
import com.tokopedia.shop.open.common.*
import com.tokopedia.shop.open.common.TermsAndConditionsLink.URL_PRIVACY_POLICY
import com.tokopedia.shop.open.common.TermsAndConditionsLink.URL_TNC
import com.tokopedia.shop.open.di.DaggerShopOpenRevampComponent
import com.tokopedia.shop.open.di.ShopOpenRevampComponent
import com.tokopedia.shop.open.di.ShopOpenRevampModule
import com.tokopedia.shop.open.listener.FragmentNavigationInterface
import com.tokopedia.shop.open.listener.InputShopInterface
import com.tokopedia.shop.open.presentation.adapter.ShopOpenRevampShopsSuggestionAdapter
import com.tokopedia.shop.open.presentation.view.watcher.AfterTextWatcher
import com.tokopedia.shop.open.presentation.viewmodel.ShopOpenRevampViewModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ShopOpenRevampInputShopFragment : BaseDaggerFragment(),
        InputShopInterface, HasComponent<ShopOpenRevampComponent>, CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val userSession: UserSessionInterface by lazy {
        UserSession(activity)
    }

    private val MIN_SHOP_NAME_LENGTH = 3
    @Inject
    lateinit var viewModel: ShopOpenRevampViewModel
    private lateinit var txtInputShopName: TextFieldUnify
    private lateinit var txtInputDomainName: TextFieldUnify
    private lateinit var txtTermsAndConditions: TextView
    private lateinit var btnShopRegistration: UnifyButton
    private var recyclerView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var adapter: ShopOpenRevampShopsSuggestionAdapter? = null
    private var shopOpenRevampTracking: ShopOpenRevampTracking? = null
    private var fragmentNavigationInterface: FragmentNavigationInterface? = null
    private var isValidShopName = false
    private var isValidDomainName = false
    private var shopNameValue = ""
    private var domainNameValue = ""
    private var shopNameSuggestionList = ValidateShopDomainSuggestionResult()

    companion object {
        const val FIRST_FRAGMENT_TAG = "first"
        const val DEFAULT_SELECTED_POSITION = -1
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentNavigationInterface = context as FragmentNavigationInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shop_open_revamp_input_shop, container, false)
        setupToolbarActions(view)
        txtTermsAndConditions = view.findViewById(R.id.txt_shop_open_revamp_tnc)
        txtInputShopName = view.findViewById(R.id.text_input_shop_open_revamp_shop_name)
        txtInputDomainName = view.findViewById(R.id.text_input_shop_open_revamp_domain_name)
        btnShopRegistration = view.findViewById(R.id.shop_registration_button)
        recyclerView = view.findViewById(R.id.recycler_view_shop_suggestions)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.layoutManager = layoutManager
        adapter = ShopOpenRevampShopsSuggestionAdapter(this)
        recyclerView?.adapter = adapter

        btnShopRegistration.isEnabled = false
        setupTncShopOpenRevamp()

        txtInputShopName.textFieldInput.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length < MIN_SHOP_NAME_LENGTH) {
                    shopNameValue = s.toString()
                    validateShopName(true, getString(R.string.open_shop_revamp_error_shop_name_too_short))
                } else if (s.toString().length >= MIN_SHOP_NAME_LENGTH && s.isNotEmpty()) {
                    shopNameValue = s.toString()
                    viewModel.checkShopName(shopNameValue)
                }
            }
        })

        txtInputDomainName.textFieldInput.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val domainInputStr = txtInputDomainName.getEditableValue()
                txtInputDomainName.setError(false)
                adapter?.selectedPosition = DEFAULT_SELECTED_POSITION
                adapter?.notifyDataSetChanged()
                if (domainInputStr.length < MIN_SHOP_NAME_LENGTH) {
                    domainNameValue = domainInputStr.toString()
                    validateDomainName(true, getString(R.string.open_shop_revamp_error_domain_too_short))
                } else if (domainInputStr.isNotEmpty() && domainInputStr.length >= MIN_SHOP_NAME_LENGTH) {
                    txtInputDomainName.setMessage("")
                    domainNameValue = domainInputStr.toString()
                    reselectChipSuggestionDomainName()
                    viewModel.checkDomainName(domainNameValue)
                }
            }
        })

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            shopOpenRevampTracking = ShopOpenRevampTracking(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopOpenRevampTracking?.sendScreenNameTracker(ScreenNameTracker.SCREEN_SHOP_REGISTRATION)
        txtInputShopName.setMessage(getString(R.string.open_shop_revamp_default_hint_input_shop))
        btnShopRegistration.setOnClickListener {
            activity?.let {
                val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            if (domainNameValue.isNotEmpty()
                    && shopNameValue.isNotEmpty()
                    && isValidShopName
                    && isValidDomainName) {
                EspressoIdlingResource.increment()
                viewModel.createShop(domainNameValue, shopNameValue)
            }
        }
        observeShopNameValidationData()
        observeDomainNameValidationData()
        observeDomainShopNameSuggestions()
        observeCreateShopData()
    }

    override fun getComponent(): ShopOpenRevampComponent {
        return activity.run {
            DaggerShopOpenRevampComponent
                    .builder()
                    .shopOpenRevampModule(ShopOpenRevampModule())
                    .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    override fun onDestroy() {
        viewModel.checkShopNameResponse.removeObservers(this)
        viewModel.checkDomainNameResponse.removeObservers(this)
        viewModel.domainShopNameSuggestionsResponse.removeObservers(this)
        viewModel.createShopOpenResponse.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun onClickedSuggestion(domainName: String, position: Int) {
        if (domainName.isNotEmpty()) {
            shopOpenRevampTracking?.clickShopDomainSuggestion(domainName)
            txtInputDomainName.textFieldInput.setText(domainName)
        }
        adapter?.selectedPosition = position
        adapter?.notifyDataSetChanged()
    }

    private fun setupToolbarActions(view: View?) {
        view?.findViewById<HeaderUnify>(R.id.toolbar_input_shop)?.apply {
            transparentMode = context.isDarkMode()
            isShowShadow = false
            setNavigationOnClickListener {
                shopOpenRevampTracking?.clickBackButtonFromInputShopPage()
                fragmentNavigationInterface?.showExitDialog()
            }
        }
    }

    private fun reselectChipSuggestionDomainName() {
        if (domainNameValue.isNotEmpty()) {
            shopNameSuggestionList.shopDomains.forEachIndexed { index, item ->
                if (domainNameValue == item) {
                    adapter?.selectedPosition = index
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun observeShopNameValidationData() {
        viewModel.checkShopNameResponse.observe(viewLifecycleOwner, Observer {
            EspressoIdlingResource.decrement()
            when (it) {
                is Success -> {
                    if (!it.data.validateDomainShopName.isValid) {
                        var errorMessage = it.data.validateDomainShopName.error.message
                        if (shopNameValue.length < MIN_SHOP_NAME_LENGTH) {
                            errorMessage = getString(R.string.open_shop_revamp_error_shop_name_too_short)
                            validateShopName(
                                    isError = true,
                                    hintMessage = errorMessage
                            )
                        } else {
                            validateShopName(
                                    isError = true,
                                    hintMessage = errorMessage
                            )
                        }
                        ShopOpenRevampErrorHandler.logMessage(
                                title = ErrorConstant.ERROR_CHECK_SHOP_NAME,
                                userId = userSession.userId,
                                message = errorMessage
                        )
                    } else {
                        validateShopName(
                                isError = false,
                                hintMessage = getString(R.string.open_shop_revamp_default_hint_input_shop)
                        )
                        viewModel.getDomainShopNameSuggestions(shopNameValue)
                        EspressoIdlingResource.increment()
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    validateShopName(
                            isError = true,
                            hintMessage = errorMessage
                    )
                    ShopOpenRevampErrorHandler.logMessage(
                            title = ErrorConstant.ERROR_CHECK_SHOP_NAME,
                            userId = userSession.userId,
                            message = errorMessage
                    )
                    ShopOpenRevampErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun observeDomainNameValidationData() {
        viewModel.checkDomainNameResponse.observe(viewLifecycleOwner, Observer {
            EspressoIdlingResource.decrement()
            when (it) {
                is Success -> {
                    if (!it.data.validateDomainShopName.isValid) {
                        val errorMessage = it.data.validateDomainShopName.error.message
                        validateDomainName(
                                isError = true,
                                hintMessage = errorMessage
                        )
                        ShopOpenRevampErrorHandler.logMessage(
                                title = ErrorConstant.ERROR_CHECK_DOMAIN_NAME,
                                userId = userSession.userId,
                                message = errorMessage
                        )
                    } else {
                        isValidDomainName = true
                        validateDomainName(
                                isError = false,
                                hintMessage = ""
                        )
                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    validateDomainName(
                            isError = true,
                            hintMessage = errorMessage
                    )
                    ShopOpenRevampErrorHandler.logMessage(
                            title = ErrorConstant.ERROR_CHECK_DOMAIN_NAME,
                            userId = userSession.userId,
                            message = errorMessage
                    )
                    ShopOpenRevampErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun observeCreateShopData() {
        viewModel.createShopOpenResponse.observe(viewLifecycleOwner, Observer {
            EspressoIdlingResource.decrement()
            when (it) {
                is Success -> {
                    val _shopId = it.data.createShop.createdId
                    val _isSuccess = it.data.createShop.success
                    var _message = it.data.createShop.message
                    var isSuccess = false
                    if (_shopId.isNotEmpty() && _isSuccess) {
                        isSuccess = true
                        userSession.shopId = _shopId
                        userSession.shopName = shopNameValue
                        EspressoIdlingResource.increment()
                        fragmentNavigationInterface?.navigateToNextPage(PageNameConstant.SPLASH_SCREEN_PAGE, FIRST_FRAGMENT_TAG)
                        shopOpenRevampTracking?.clickCreateShop(
                                isSuccess = isSuccess,
                                shopDomainName = shopNameValue
                        )
                    } else {
                        isSuccess = false
                        shopOpenRevampTracking?.clickCreateShop(
                                isSuccess = isSuccess,
                                shopDomainName = shopNameValue
                        )
                        if (_message.isNotEmpty()) {
                            showErrorResponse(_message)
                        } else {
                            _message = getString(R.string.open_shop_revamp_error_retry)
                            showErrorResponse(_message)
                        }

                        ShopOpenRevampErrorHandler.logMessage(
                                title = ErrorConstant.ERROR_CREATE_SHOP,
                                userId = userSession.userId,
                                message = _message
                        )

                    }
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showErrorNetwork(errorMessage)
                    shopOpenRevampTracking?.clickCreateShop(
                            isSuccess = false,
                            shopDomainName = shopNameValue
                    )
                    ShopOpenRevampErrorHandler.logMessage(
                            title = ErrorConstant.ERROR_CREATE_SHOP,
                            userId = userSession.userId,
                            message = errorMessage
                    )
                    ShopOpenRevampErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun observeDomainShopNameSuggestions() {
        viewModel.domainShopNameSuggestionsResponse.observe(viewLifecycleOwner, Observer {
            EspressoIdlingResource.decrement()
            when (it) {
                is Success -> {
                    txtInputDomainName.textFieldInput.setText(it.data.shopDomainSuggestion.result.shopDomain.toString())
                    btnShopRegistration.isEnabled
                    shopNameSuggestionList = it.data.shopDomainSuggestion.result
                    adapter?.updateDataShopSuggestions(shopNameSuggestionList)
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    showErrorNetwork(errorMessage)
                    ShopOpenRevampErrorHandler.logMessage(
                            title = ErrorConstant.ERROR_DOMAIN_NAME_SUGGESTIONS,
                            userId = userSession.userId,
                            message = errorMessage
                    )
                    ShopOpenRevampErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun showErrorNetwork(errorMessage: String) {
        view?.let {
            Toaster.showError(
                    it,
                    errorMessage,
                    Snackbar.LENGTH_LONG
            )
        }
    }

    private fun validateShopName(isError: Boolean, hintMessage: String) {
        if (isError) {
            isValidShopName = false
            txtInputShopName.setError(true)
            txtInputShopName.setMessage(hintMessage)
            btnShopRegistration.isEnabled = false
        } else {
            isValidShopName = true
            txtInputShopName.setError(false)
            txtInputShopName.setMessage(hintMessage)
            validateButtonShopRegistration()
        }
    }

    private fun validateDomainName(isError: Boolean, hintMessage: String) {
        if (isError) {
            isValidDomainName = false
            txtInputDomainName.setError(true)
            txtInputDomainName.setMessage(hintMessage)
            btnShopRegistration.isEnabled = false
        } else {
            isValidDomainName = true
            txtInputDomainName.setError(false)
            txtInputDomainName.setMessage("")
            validateButtonShopRegistration()
        }
    }

    private fun validateButtonShopRegistration() {
        if (shopNameValue.isEmpty() || domainNameValue.isEmpty()) return
        val shopNameLastCharacter = shopNameValue.get(shopNameValue.length - 1)
        val domainNameLastCharacter = domainNameValue.get(domainNameValue.length - 1)
        if (isValidShopName &&
                isValidDomainName &&
                !shopNameLastCharacter.equals(" ") &&
                !domainNameLastCharacter.equals(" ")) {
            btnShopRegistration.isEnabled = true
        }
    }

    private fun showErrorResponse(message: String) {
        view?.let {
            Toaster.showError(it, message, Snackbar.LENGTH_LONG)
        }
    }

    private fun setupTncShopOpenRevamp() {
        val textTnc = SpannableString(getString(R.string.open_shop_revamp_tnc_open_shop))
        val tncClickableSpan = setupClickableSpan(URL_TNC, getString(R.string.open_shop_revamp_tnc_webview_title))
        val tncUnclickableAreaSpan = setupClickableSpan("", getString(R.string.open_shop_revamp_tnc_unclickable))
        val privacyPolicyClickableSpan = setupClickableSpan(URL_PRIVACY_POLICY, getString(R.string.open_shop_revamp_tnc_privacy_policy_webview_title))

        textTnc.setSpan(tncClickableSpan, 0, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textTnc.setSpan(StyleSpan(Typeface.BOLD), 0, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textTnc.setSpan(ForegroundColorSpan(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)), 0, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textTnc.setSpan(tncUnclickableAreaSpan, 21, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textTnc.setSpan(StyleSpan(Typeface.NORMAL), 21, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textTnc.setSpan(ForegroundColorSpan(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N400)), 21, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textTnc.setSpan(privacyPolicyClickableSpan, 25, textTnc.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textTnc.setSpan(StyleSpan(Typeface.BOLD), 25, textTnc.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textTnc.setSpan(ForegroundColorSpan(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)), 25, textTnc.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        txtTermsAndConditions.setText(textTnc)
        txtTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance())
    }

    private fun setupClickableSpan(url: String, title: String): ClickableSpan {
        return object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

            override fun onClick(textView: View) {
                if (activity != null) {
                    if (url == URL_TNC) {
                        shopOpenRevampTracking?.clickTextTermsAndConditions()
                    } else {
                        shopOpenRevampTracking?.clickTextPrivacyPolicy()
                    }
                    RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW_TITLE, title, url)
                }
            }
        }
    }

}
