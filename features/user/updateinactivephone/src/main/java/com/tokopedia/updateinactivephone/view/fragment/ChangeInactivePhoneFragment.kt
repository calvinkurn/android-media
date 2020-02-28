package com.tokopedia.updateinactivephone.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.tkpd.library.ui.utilities.TkpdProgressDialog
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.core.analytics.ScreenTracking
import com.tokopedia.core.app.MainApplication
import com.tokopedia.core.base.di.component.AppComponent
import com.tokopedia.design.component.Dialog
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.view.activity.ChangeInactiveFormRequestActivity
import com.tokopedia.updateinactivephone.view.activity.ChangeInactivePhoneRequestSubmittedActivity
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.IS_DUPLICATE_REQUEST
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.OLD_PHONE
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.QUERY_CONSTANTS.Companion.USER_ID
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventConstants
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventTracking
import com.tokopedia.updateinactivephone.viewmodel.presenter.ChangeInactivePhonePresenter
import com.tokopedia.updateinactivephone.view.ChangeInactivePhone
import com.tokopedia.updateinactivephone.di.component.DaggerUpdateInactivePhoneComponent

import javax.inject.Inject

class ChangeInactivePhoneFragment : BaseDaggerFragment(), ChangeInactivePhone.View {
    private var inputMobileNumber: EditText? = null
    private var buttonContinue: Button? = null
    private var errorText: TextView? = null
    private var phoneHintTextView: TextView? = null

    private var tkpdProgressDialog: TkpdProgressDialog? = null

    @Inject
    lateinit var presenter: ChangeInactivePhonePresenter

    override fun initInjector() {
        val appComponent = getComponent(AppComponent::class.java)

        val daggerUpdateInactivePhoneComponent = DaggerUpdateInactivePhoneComponent.builder()
                .appComponent(appComponent)
                .build() as DaggerUpdateInactivePhoneComponent

        daggerUpdateInactivePhoneComponent.inject(this)
    }

    override fun getScreenName(): String {
        return UpdateInactivePhoneEventConstants.Screen.INPUT_OLD_PHONE_SCREEN
    }

    override fun onStart() {
        super.onStart()
        ScreenTracking.screen(MainApplication.getAppContext(), screenName)
    }


    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_change_inactive_phone, parent, false)

        inputMobileNumber = view.findViewById(R.id.phone_number)
        buttonContinue = view.findViewById(R.id.button_continue)
        phoneHintTextView = view.findViewById(R.id.phone_hint_text_view)
        errorText = view.findViewById(R.id.error)
        presenter.attachView(this)
        prepareView()
        return view
    }

    private fun prepareView() {

        inputMobileNumber?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                setErrorText("")
                if (charSequence.toString().isEmpty()) {
                    buttonContinue?.isEnabled = false
                    buttonContinue?.isClickable = false
                    buttonContinue?.setTextColor(resources.getColor(R.color.black_26))
                } else {
                    buttonContinue?.isEnabled = true
                    buttonContinue?.isClickable = true
                    buttonContinue?.setTextColor(Color.WHITE)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        inputMobileNumber?.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                handled = true
            }
            handled
        }

        buttonContinue?.setOnClickListener { v ->
            setErrorText("")
            presenter.checkPhoneNumberStatus(inputMobileNumber?.text.toString())
            hideKeyboard(v)
            UpdateInactivePhoneEventTracking.eventInactivePhoneClick(v.context)
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setErrorText(text: String) {
        if (TextUtils.isEmpty(text)) {
            errorText?.visibility = View.GONE
            phoneHintTextView?.visibility = View.VISIBLE
        } else {
            errorText?.text = text
            errorText?.visibility = View.VISIBLE
            phoneHintTextView?.visibility = View.GONE
        }
    }

    override fun showErrorPhoneNumber(resId: Int) {
        setErrorText(getString(resId))
    }

    override fun showErrorPhoneNumber(errorMessage: String) {
        setErrorText(errorMessage)
    }

    override fun dismissLoading() {
        tkpdProgressDialog?.dismiss()
    }

    override fun showLoading() {
        if (tkpdProgressDialog == null){
            tkpdProgressDialog = TkpdProgressDialog(activity, TkpdProgressDialog.NORMAL_PROGRESS)
        }
        tkpdProgressDialog?.showDialog()
    }

    override fun onForbidden() {}

    override fun onPhoneStatusSuccess(userid: String) {
        setErrorText("")
        val bundle = Bundle()
        bundle.putString(USER_ID, userid)
        bundle.putString(OLD_PHONE, inputMobileNumber?.text.toString())
        startActivity(context?.let { ChangeInactiveFormRequestActivity.createIntent(it, bundle) })
    }

    override fun onPhoneRegisteredWithEmail() {
        val dialog = Dialog(activity, Dialog.Type.PROMINANCE)
        dialog.setTitle(getString(R.string.registered_email_dialog_title))
        dialog.setDesc(getString(R.string.registered_email_dialog_message))
        dialog.setBtnOk(getString(R.string.drawer_title_login))
        dialog.setOnOkClickListener { v ->
            UpdateInactivePhoneEventTracking.eventLoginDialogClick(v.context)
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
        dialog.setBtnCancel(getString(R.string.title_cancel))
        dialog.setOnCancelClickListener { v ->
            UpdateInactivePhoneEventTracking.eventCancelDialogClick(v.context)
            dialog.dismiss()
        }
        dialog.show()

        dialog.btnCancel.setTextColor(resources.getColor(R.color.black_54))
        dialog.btnOk.setTextColor(resources.getColor(R.color.tkpd_main_green))
    }

    override fun onPhoneDuplicateRequest() {
        setErrorText("")
        val bundle = Bundle()
        bundle.putBoolean(IS_DUPLICATE_REQUEST, true)
        val intent = context?.let { ChangeInactivePhoneRequestSubmittedActivity.createNewIntent(it, bundle) }
        startActivity(intent)
    }

    override fun onPhoneServerError() {
        setErrorText("")
    }

    override fun onPhoneBlackListed() {
        setErrorText(getString(R.string.phone_blacklisted))
    }

    override fun onPhoneInvalid() {
        setErrorText(getString(R.string.error_invalid_phone_number))
    }

    override fun onPhoneNotRegistered() {
        setErrorText(getString(R.string.phone_not_registered))
    }

    override fun onPhoneTooShort() {
        setErrorText(getString(R.string.phone_number_invalid_min_8))
    }

    override fun onPhoneTooLong() {
        setErrorText(getString(R.string.phone_number_invalid_max_15))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    companion object {

        val instance: Fragment
            get() = ChangeInactivePhoneFragment()
    }
}
