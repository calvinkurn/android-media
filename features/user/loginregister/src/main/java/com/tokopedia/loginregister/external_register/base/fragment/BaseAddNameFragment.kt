package com.tokopedia.loginregister.external_register.base.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.external_register.base.constant.ExternalRegisterConstants
import com.tokopedia.loginregister.external_register.base.listener.BaseAddNameListener
import kotlinx.android.synthetic.main.fragment_base_add_name.*
import kotlinx.android.synthetic.main.fragment_base_add_name.view.*


/**
 * Created by Yoris Prayogo on 16/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
abstract class BaseAddNameFragment: BaseDaggerFragment() {

    var baseAddNameListener: BaseAddNameListener? = null

    abstract fun initObserver()

    override fun getScreenName(): String = ExternalRegisterConstants.ADD_NAME_SCREEN

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_add_name, container, false)
    }

    fun startButtonLoading(){
        base_add_name_button_next?.isLoading = true
    }

    fun stopButtonLoading(){
        base_add_name_button_next?.isLoading = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(context?.getString(R.string.title_external_register_add_name) ?: "")
        setBottomText()
        base_add_name_button_next?.setOnClickListener {
            if(view.base_add_name_textfield?.textFieldInput?.text?.toString()?.isNotEmpty() == true) {
                baseAddNameListener?.onNextButtonClicked()
            }
            else {
                view.base_add_name_textfield?.setMessage(getString(R.string.base_add_name_error_empty))
                view.base_add_name_textfield?.setError(true)
            }
        }

        view.base_add_name_textfield?.textFieldInput?.afterTextChanged {
            if(it.length == 1) {
                view.base_add_name_textfield?.setMessage("")
                view.base_add_name_textfield?.setError(false)
            }
        }

        initObserver()
    }

    fun getInputText(): String = view?.base_add_name_textfield?.textFieldInput?.text?.toString() ?: ""

    fun setTitle(title: String){
        base_add_name_title?.text = title
    }

    fun setBottomText() {
        val normal = "Dengan mendaftar, kamu menyetujui\n"
        val tnc: Spannable = SpannableString("Syarat dan Ketentuan.")
        val privacy: Spannable = SpannableString("Kebijakan Privasi.")

        tnc.setSpan(object: ClickableSpan() {
            override fun onClick(widget: View) {
                RouteManager.route(activity, ExternalRegisterConstants.BASE_WEBVIEW_APPLINK + ExternalRegisterConstants.BASE_MOBILE + ExternalRegisterConstants.PATH_TERM_CONDITION)
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.color = resources.getColor(R.color.Unify_G400_96)
            }
        }, 0, tnc.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        privacy.setSpan(object: ClickableSpan() {
            override fun onClick(widget: View) {
                RouteManager.route(activity, ExternalRegisterConstants.BASE_WEBVIEW_APPLINK + ExternalRegisterConstants.BASE_MOBILE + ExternalRegisterConstants.PATH_PRIVACY_POLICY)
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.color = resources.getColor(R.color.Unify_G400_96)
            }
        }, 0, privacy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        base_add_name_tnc_text?.apply {
            append(normal)
            append(tnc)
            append(" serta ")
            append(privacy)
        }
    }
}