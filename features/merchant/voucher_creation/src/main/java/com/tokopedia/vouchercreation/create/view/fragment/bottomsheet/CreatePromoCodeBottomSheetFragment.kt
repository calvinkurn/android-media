package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.viewmodel.CreatePromoCodeViewModel
import kotlinx.android.synthetic.main.mvc_create_promo_code_bottom_sheet_view.*
import javax.inject.Inject


class CreatePromoCodeBottomSheetFragment(bottomSheetContext: Context?,
                                         val onNextClick: (String) -> Unit = {},
                                         val getPromoCode: () -> String,
                                         val getPromoCodePrefix: () -> String) : BottomSheetUnify(), VoucherBottomView {

    companion object {
        private val TEXFIELD_ALERT_MINIMUM = R.string.mvc_create_alert_minimum
        private val TEXTFIELD_MESSAGE_EASY_REMEMBER = R.string.mvc_create_promo_code_message_easy_remember

        private const val MIN_TEXTFIELD_LENGTH = 5
        private const val MAX_TEXTFIELD_LENGTH = 10

        fun createInstance(context: Context?,
                           onNextClick: (String) -> Unit,
                           getPromoCode: () -> String = {""},
                           getPromoCodePrefix: () -> String) : CreatePromoCodeBottomSheetFragment {
            return CreatePromoCodeBottomSheetFragment(context, onNextClick, getPromoCode, getPromoCodePrefix).apply {
                context?.run {
                    val view = View.inflate(this, R.layout.mvc_create_promo_code_bottom_sheet_view, null)
                    setChild(view)
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                }
            }
        }
    }

    override var bottomSheetViewTitle: String? = bottomSheetContext?.resources?.getString(R.string.mvc_create_target_create_promo_code_bottomsheet_title)

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(CreatePromoCodeViewModel::class.java)
    }

    private var alertMinimumMessage = bottomSheetContext?.resources?.getString(TEXFIELD_ALERT_MINIMUM).toBlankOrString()
    private var easyRememberMessage = bottomSheetContext?.resources?.getString(TEXTFIELD_MESSAGE_EASY_REMEMBER).toBlankOrString()

    private var isWaitingForValidation = false

    private var promoCodePrefix = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded) {
            observeLiveData()
            setupView()
        }
    }

    override fun onResume() {
        super.onResume()
        createPromoCodeTextField?.textFieldInput?.run {
            setText(getPromoCode().replace(promoCodePrefix, ""))
            selectAll()
        }
    }

    override fun onDestroy() {
        viewModel.flush()
        super.onDestroy()
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    private fun observeLiveData() {
        viewModel.promoCodeValidationLiveData.observe(viewLifecycleOwner, Observer { result ->
            createPromoCodeSaveButton?.isLoading = false
            when(result) {
                is Success -> {
                    val errorMessage = result.data.promoCodeError
                    errorMessage.run {
                        if (isBlank() && isWaitingForValidation) {
                            onNextClick(createPromoCodeTextField?.textFieldInput?.text?.toString().toBlankOrString())
                        } else {
                            createPromoCodeTextField?.setTextFieldError(this)
                        }
                    }
                }
                is Fail -> {
                    val error = result.throwable.message.toBlankOrString()
                    showErrorToaster(error)
                }
            }
            isWaitingForValidation = false
        })
    }

    private fun setupView() {
        createPromoCodeTextField?.run {
            getPromoCodePrefix().run {
                promoCodePrefix = if (isNotBlank()) {
                    this
                } else {
                    context?.getString(R.string.mvc_create_promo_code_prefix).toBlankOrString()
                }
                prependText(promoCodePrefix)
            }
            textFieldInput.run {
                filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(MAX_TEXTFIELD_LENGTH))
                setOnFocusChangeListener { _, hasFocus ->
                    activity?.run {
                        if (hasFocus) {
                            showKeyboard()
                        } else {
                            hideKeyboard()
                        }
                    }
                }
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        //No op
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        //No op
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        when {
                            s.isEmpty() -> {
                                setError(false)
                                setMessage(easyRememberMessage)
                            }
                            s.length < MIN_TEXTFIELD_LENGTH -> {
                                setTextFieldError(alertMinimumMessage)
                            }
                            else -> {
                                setError(false)
                                setMessage("")
                            }
                        }
                    }
                })
                requestFocus()
            }

            createPromoCodeSaveButton?.run {
                setOnClickListener {
                    val promoCode = createPromoCodeTextField?.textFieldInput?.text?.toString().toBlankOrString()
                    val canValidateCode = promoCode.length in MIN_TEXTFIELD_LENGTH..MAX_TEXTFIELD_LENGTH && !isLoading
                    if (canValidateCode) {
                        isLoading = true
                        isWaitingForValidation = true
                        viewModel.validatePromoCode(promoCode)
                    }
                    VoucherCreationTracking.sendCreateVoucherClickTracking(
                            step = VoucherCreationStep.TARGET,
                            action = VoucherCreationAnalyticConstant.EventAction.Click.SAVE_PRIVATE,
                            userId = userSession.userId
                    )
                }
            }
        }
    }

    private fun TextFieldUnify.setTextFieldError(errorMessage: String) {
        setError(true)
        setMessage(errorMessage)
    }

    private fun showErrorToaster(errorMessage: String) {
        view?.run {
            Toaster.make(this,
                    errorMessage,
                    Snackbar.LENGTH_SHORT,
                    Toaster.TYPE_ERROR)
        }
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun Context.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

}