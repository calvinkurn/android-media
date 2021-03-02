package com.tokopedia.managename.view.fragment

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.managename.R
import com.tokopedia.managename.di.ManageNameComponent
import com.tokopedia.managename.view.ViewUtil
import com.tokopedia.managename.viewmodel.ManageNameViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_manage_name.*
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-06-03.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class AddNameFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = ""
    private var isError = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(ManageNameViewModel::class.java) }

    override fun initInjector() {
        getComponent(ManageNameComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manage_name, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
        setViewListener()
        disableNextButton()
    }

    private fun initObserver(){
        viewModel.updateNameLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Success -> onSuccessAddName()
                is Fail -> onErrorRegister(it.throwable)
            }
        })
    }

    private fun initViews(){
        initTermPrivacyView()
        ViewUtil.stripUnderlines(bottom_info)
    }

    private fun setViewListener() {
        et_name.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isNotEmpty()) {
                    enableNextButton()
                } else {
                    disableNextButton()
                }
                if (isError) {
                    hideValidationError()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        btn_continue.setOnClickListener { onContinueClick() }
        btn_continue.setOnEditorActionListener(OnEditorActionListener { v, id, event ->
            if (id == R.id.btn_continue || id == EditorInfo.IME_NULL) {
                onContinueClick()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun initTermPrivacyView() {
        context?.let {
            val textTermPrivacy1 = "${getString(R.string.text_term_and_privacy_1)} "
            val textTermPrivacy2 = " ${getString(R.string.text_term_and_privacy_2)} "
            val textTermCondition = getString(R.string.text_term_condition)
            val textPrivacyPolicy = getString(R.string.text_privacy_policy)
            val termPrivacy = SpannableStringBuilder()
            termPrivacy.append(textTermPrivacy1)
            termPrivacy.append(textTermCondition)
            termPrivacy.setSpan(termConditionClickAction(), termPrivacy.length - textTermCondition.length, termPrivacy.length, 0)
            termPrivacy.append(textTermPrivacy2)
            termPrivacy.append(textPrivacyPolicy)
            termPrivacy.setSpan(privacyClickAction(), termPrivacy.length - textPrivacyPolicy.length, termPrivacy.length, 0)

            bottom_info?.run {
                movementMethod = LinkMovementMethod.getInstance()
                isSelected = false
                text = termPrivacy
            }
        }
    }

    private fun termConditionClickAction(): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                context?.let {
                    startActivity(RouteManager.getIntent(it, ApplinkConstInternalGlobal.TERM_PRIVACY, ApplinkConstInternalGlobal.PAGE_TERM_AND_CONDITION))
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400)
            }
        }
    }

    private fun privacyClickAction(): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                context?.let {
                    startActivity(RouteManager.getIntent(it, ApplinkConstInternalGlobal.TERM_PRIVACY, ApplinkConstInternalGlobal.PAGE_PRIVACY_POLICY))
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_G400)
            }
        }
    }

    fun enableNextButton() {
        btn_continue?.isEnabled = true
    }

    fun disableNextButton(){
        btn_continue?.isEnabled = false
    }

    fun showValidationError(error: String?) {
        isError = true
        et_name?.setError(true)
        et_name?.setMessage(error ?: "")
    }

    fun hideValidationError() {
        isError = false
        et_name?.setError(false)
        et_name?.setMessage("")
    }

    fun onSuccessAddName() {
        activity?.run {
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
    }

    fun showLoading(){
        btn_continue?.isEnabled = false
        add_name_linear_layout?.alpha = 0.5F
        add_name_progressbar?.show()
    }

    fun dismissLoading(){
        btn_continue?.isEnabled = true
        add_name_linear_layout?.alpha = 1.0F
        add_name_progressbar?.hide()
    }

    fun onErrorRegister(error: Throwable){
        dismissLoading()
        showValidationError(ErrorHandler.getErrorMessage(activity, error))
    }

    private fun onContinueClick() {
        showLoading()
        KeyboardHandler.DropKeyboard(activity, view)
        viewModel.updateName(et_name.textFieldInput.text.toString())
    }


    companion object {
        fun newInstance(bundle: Bundle?): AddNameFragment {
            val fragment = AddNameFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
