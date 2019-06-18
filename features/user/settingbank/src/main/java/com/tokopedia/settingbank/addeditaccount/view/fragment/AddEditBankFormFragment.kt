package com.tokopedia.settingbank.addeditaccount.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.text.TkpdHintTextInputLayout
import com.tokopedia.settingbank.BankRouter
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.addeditaccount.di.AddEditBankDependencyInjector
import com.tokopedia.settingbank.addeditaccount.view.activity.AddEditBankActivity
import com.tokopedia.settingbank.addeditaccount.view.listener.AddEditBankContract
import com.tokopedia.settingbank.addeditaccount.view.presenter.AddEditBankPresenter
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel
import com.tokopedia.settingbank.banklist.analytics.SettingBankAnalytics
import com.tokopedia.settingbank.banklist.data.SettingBankUrl
import com.tokopedia.settingbank.choosebank.view.activity.ChooseBankActivity
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_bank_form.*

/**
 * @author by nisie on 6/21/18.
 */

class AddEditBankFormFragment : AddEditBankContract.View,
        BaseDaggerFragment() {

    private val PARAM_FORM_DATA: String? = "saved_bank_data"

    private val REQUEST_CHOOSE_BANK_FIRST_TIME: Int = 101
    private val REQUEST_CHOOSE_BANK: Int = 102
    private val REQUEST_OTP: Int = 103

    lateinit var presenter: AddEditBankPresenter
    lateinit var bottomInfoDialog: BottomSheetDialog
    lateinit var alertDialog: Dialog
    lateinit var analyticTracker: SettingBankAnalytics

    private var bankFormModel = BankFormModel()

    override fun getScreenName(): String {
        return if (activity!!.intent.getStringExtra(AddEditBankActivity.Companion.PARAM_ACTION) ==
                BankFormModel.Companion.STATUS_ADD) {
            SettingBankAnalytics.SCREEN_NAME_ADD
        } else {
            SettingBankAnalytics.SCREEN_NAME_EDIT
        }
    }

    override fun initInjector() {
        presenter = AddEditBankDependencyInjector.Companion.inject(activity!!.applicationContext)
        presenter.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null && activity!!.applicationContext != null) {
            analyticTracker = SettingBankAnalytics.createInstance()
        }
    }

    override fun onStart() {
        super.onStart()
        if (activity != null) {
            analyticTracker.sendScreen(activity!!, screenName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_bank_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setMode(savedInstanceState)

        submit_button.setOnClickListener({
            setupBankFormModel()
            if (!bankFormModel.status.isBlank()) {
                presenter.validateBank(bankFormModel)
            }
        })

        bank_name_et.setOnClickListener({
            goToAddBank()
        })
    }

    override fun onSuccessValidateForm(bankFormModel: BankFormModel) {

        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        alertDialog.setTitle(getString(R.string.add_bank_account_prompt_title))
        alertDialog.setDesc(composeMakeMainDescription())
        alertDialog.setBtnCancel(getString(R.string.edit))
        alertDialog.setBtnOk(getString(R.string.yes_correct))
        alertDialog.setOnCancelClickListener({
            alertDialog.dismiss()
        })
        alertDialog.setOnOkClickListener({
            if (bankFormModel.status == BankFormModel.Companion.STATUS_ADD) {
                analyticTracker.trackConfirmYesAddBankAccount()
            } else {
                analyticTracker.trackConfirmYesEditBankAccount()
            }
            onGoToCOTP()
            alertDialog.dismiss()
        })

        alertDialog.show()
    }


    private fun composeMakeMainDescription(): String {
        return if (bankFormModel.status == BankFormModel.Companion.STATUS_ADD) {
            String.format("%s %s %s %s" +
                    " %s.",
                    getString(R.string.you_will_add_account),
                    bank_name_et.text.toString(),
                    account_number_et.text.toString(),
                    getString(R.string.under_name),
                    account_name_et.text.toString())
        } else String.format("%s %s %s %s" +
                " %s.",
                getString(R.string.you_will_change_account_into),
                bank_name_et.text.toString(),
                account_number_et.text.toString(),
                getString(R.string.under_name),
                account_name_et.text.toString())

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_info_add_bank_account, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menu_info) {
            onInfoClicked()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onInfoClicked() {
        if (!::bottomInfoDialog.isInitialized && context != null) {
            bottomInfoDialog = BottomSheetDialog(context!!)
            val bottomLayout: View = activity!!.layoutInflater.inflate(R.layout
                    .bottom_sheet_info_add_bank_account, null)
            bottomInfoDialog.setContentView(bottomLayout)

            val bankAccountImage: ImageView = bottomLayout.findViewById(R.id.bank_account_image)
            ImageHandler.LoadImage(bankAccountImage, SettingBankUrl.Companion.IMAGE_BOTTOM_DIALOG_ADD_ACCOUNT)

            val closeButton: ImageView = bottomLayout.findViewById(R.id.close_button)
            closeButton.setOnClickListener({ bottomInfoDialog.dismiss() })

        }

        bottomInfoDialog.show()
    }


    private fun goToAddBank() {
        val intentChooseBank: Intent = if (bankFormModel.bankId.isEmpty()) {
            ChooseBankActivity.createIntentChooseBank(activity!!)
        } else {
            ChooseBankActivity.createIntentChooseBank(activity!!,
                    bankFormModel.bankId)
        }
        startActivityForResult(intentChooseBank, REQUEST_CHOOSE_BANK)
    }

    private fun goToAddBankFirstTime() {
        val intentChooseBank = ChooseBankActivity.createIntentChooseBank(activity!!)
        startActivityForResult(intentChooseBank, REQUEST_CHOOSE_BANK_FIRST_TIME)
    }

    private fun setMode(savedInstanceState: Bundle?) {

        when {
            savedInstanceState != null -> {
                bankFormModel = savedInstanceState.getParcelable(PARAM_FORM_DATA)
                account_name_et.setText(bankFormModel.accountName)
                account_number_et.setText(bankFormModel.accountNumber)
                bank_name_et.setText(bankFormModel.bankName)
                checkIsValidForm()

                if (activity!!.intent.getStringExtra(AddEditBankActivity
                                .Companion.PARAM_ACTION) == BankFormModel.Companion.STATUS_EDIT)
                    (activity!! as AddEditBankActivity).setTitle(getString(R.string.title_edit_bank))

            }
            activity!!.intent.getStringExtra(AddEditBankActivity
                    .Companion.PARAM_ACTION) ==
                    BankFormModel.Companion.STATUS_ADD -> goToAddBankFirstTime()
            activity!!.intent.getStringExtra(AddEditBankActivity
                    .Companion.PARAM_ACTION) == BankFormModel.Companion.STATUS_EDIT -> {
                (activity!! as AddEditBankActivity).setTitle(getString(R.string.title_edit_bank))
                bankFormModel = activity!!.intent.getParcelableExtra(AddEditBankActivity.Companion
                        .PARAM_DATA)
                account_name_et.setText(bankFormModel.accountName)
                account_number_et.setText(bankFormModel.accountNumber)
                bank_name_et.setText(bankFormModel.bankName)
                checkIsValidForm()
            }
        }
    }


    private fun setupBankFormModel() {
        bankFormModel.accountName = account_name_et.text.toString()
        bankFormModel.accountNumber = account_number_et.text.toString()
        bankFormModel.bankName = bank_name_et.text.toString()
    }

    override fun onResume() {
        super.onResume()
        setViewListener()
    }

    private fun setViewListener() {
        account_name_et.addTextChangedListener(accountNameWatcher(wrapper_account_name))
        account_number_et.addTextChangedListener(accountNumberWatcher(wrapper_account_number))
    }

    private fun accountNameWatcher(wrapper: TkpdHintTextInputLayout): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
                checkIsValidForm()

            }
        }
    }

    private fun accountNumberWatcher(wrapper: TkpdHintTextInputLayout): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
                checkIsValidForm()

            }
        }
    }


    private fun setWrapperError(wrapper: TkpdHintTextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.setErrorEnabled(false)
        } else {
            wrapper.setErrorEnabled(true)
            wrapper.error = s
        }
    }


    private fun checkIsValidForm() {
        val accountName = account_name_et.text.toString().trim()
        val accountNumber = account_number_et.text.toString().trim()
        val bankName = bank_name_et.text.toString().trim()

        if (presenter.isValidForm(accountName, accountNumber, bankName)) {
            enableSubmitButton()
        } else {
            disableSubmitButton()
        }
    }

    private fun enableSubmitButton() {
        MethodChecker.setBackground(submit_button, MethodChecker.getDrawable(context, R.drawable
                .bg_button_green_enabled))
        submit_button.setTextColor(MethodChecker.getColor(context, R.color.white))
        submit_button.isEnabled = true
    }

    private fun disableSubmitButton() {
        MethodChecker.setBackground(submit_button, MethodChecker.getDrawable(context, R.drawable
                .bg_button_disabled))
        submit_button.setTextColor(MethodChecker.getColor(context, R.color.black_38))
        submit_button.isEnabled = false
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        main_form.visibility = View.GONE
    }

    override fun resetError() {
        setWrapperError(wrapper_account_number, "")
        setWrapperError(wrapper_account_name, "")
        setWrapperError(wrapper_bank_name, "")
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
        main_form.visibility = View.VISIBLE
    }

    override fun onSuccessAddEditBank(accountId: String) {
        val intent = Intent()
        val bundle = Bundle()
        bankFormModel.accountId = accountId
        bundle.putParcelable(AddEditBankActivity.PARAM_DATA, bankFormModel)
        intent.putExtras(bundle)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onErrorAccountNumber(errorMessage: String) {
        setWrapperError(wrapper_account_number, errorMessage)
    }

    override fun onErrorAccountName(errorMessage: String) {
        setWrapperError(wrapper_account_name, errorMessage)
    }

    override fun onErrorGeneral(errorMessage: String?) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHOOSE_BANK_FIRST_TIME -> onResultChooseBankFirstTime(resultCode, data)
            REQUEST_CHOOSE_BANK -> onResultChooseBank(resultCode, data)
            REQUEST_OTP -> onResultRequestOtp(resultCode)
        }
    }

    private fun onResultRequestOtp(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            when (bankFormModel.status) {
                BankFormModel.Companion.STATUS_ADD -> presenter.addBank(bankFormModel)
                BankFormModel.Companion.STATUS_EDIT -> presenter.editBank(bankFormModel)
            }
        }
    }

    private fun onResultChooseBankFirstTime(resultCode: Int, data: Intent?) {
        try {
            if (successRetrieveBank(resultCode, data)) {

                (activity!! as AddEditBankActivity).setTitle(getString(R.string.title_add_bank))
                bankFormModel.status = BankFormModel.Companion.STATUS_ADD
                disableSubmitButton()
                onResultChooseBank(resultCode, data)
            } else {
                activity!!.finish()
            }

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun onResultChooseBank(resultCode: Int, data: Intent?) {

        if (successRetrieveBank(resultCode, data)) {
            val selectedModel = data!!.extras.getParcelable<BankViewModel>(ChooseBankActivity
                    .PARAM_RESULT_DATA)
            bankFormModel.bankId = selectedModel.bankId!!
            bankFormModel.bankName = selectedModel.bankName!!
            bank_name_et.setText(selectedModel.bankName)
            checkIsValidForm()
        }
    }

    private fun successRetrieveBank(resultCode: Int, data: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
                && data != null
                && data.extras != null
                && data.extras.getParcelable<BankViewModel>(ChooseBankActivity.PARAM_RESULT_DATA) != null

    }

    override fun onErrorAddBank(errorMessage: String) {
        if (errorMessage.isEmpty()) {
            NetworkErrorHelper.createSnackbarWithAction(activity, {
                presenter.addBank(bankFormModel)
            }).showRetrySnackbar()
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage, {
                presenter.addBank(bankFormModel)
            }).showRetrySnackbar()
        }
    }

    override fun onErrorEditBank(errorMessage: String) {
        if (errorMessage.isEmpty()) {
            NetworkErrorHelper.createSnackbarWithAction(activity, {
                presenter.editBank(bankFormModel)
            }).showRetrySnackbar()
        } else {
            NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage, {
                presenter.editBank(bankFormModel)
            }).showRetrySnackbar()
        }
    }

    fun onGoToCOTP() {
        val intent = presenter.getCotpIntent(activity)
        startActivityForResult(intent, REQUEST_OTP)
    }

    override fun onCloseForm() {
        if (activity != null) activity!!.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(PARAM_FORM_DATA, bankFormModel)
    }
}
