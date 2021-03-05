package com.tokopedia.settingbank.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.analytics.BankSettingAnalytics
import com.tokopedia.settingbank.di.SettingBankComponent
import com.tokopedia.settingbank.domain.model.AddBankRequest
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.SettingBankErrorHandler
import com.tokopedia.settingbank.domain.model.UploadDocumentPojo
import com.tokopedia.settingbank.util.AccountConfirmationType
import com.tokopedia.settingbank.util.ImageUtils
import com.tokopedia.settingbank.view.activity.SettingBankActivity
import com.tokopedia.settingbank.view.viewModel.UploadDocumentViewModel
import com.tokopedia.settingbank.view.viewState.DocumentUploadEnd
import com.tokopedia.settingbank.view.viewState.DocumentUploadError
import com.tokopedia.settingbank.view.viewState.DocumentUploadStarted
import com.tokopedia.settingbank.view.viewState.DocumentUploaded
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_confirm_bank_account.*
import javax.inject.Inject

const val ARG_BANK_ACCOUNT_DATA = "arg_bank_account_data"
const val ARG_ACCOUNT_TYPE = "arg_account_type"
const val ARG_KYC_NAME = "arg_kyc_name"


class AccountDocumentFragment : BaseDaggerFragment() {

    override fun getScreenName(): String? = null

    private val REQUEST_CODE_IMAGE: Int = 101

    private val DOC_TYPE_COMPANY = 1
    private val DOC_TYPE_FAMILY = 2
    private val DOC_TYPE_OTHER = 3

    private val MAX_FILE_SIZE = 2048

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var bankSettingAnalytics: BankSettingAnalytics


    private lateinit var uploadDocumentViewModel: UploadDocumentViewModel


    val builder: AddBankRequest.Builder = AddBankRequest.Builder()

    lateinit var bankAccount: BankAccount
    lateinit var accountConfirmationType: AccountConfirmationType
    lateinit var kYCName: String

    var selectedFilePath: String? = null

    override fun initInjector() {
        getComponent(SettingBankComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_BANK_ACCOUNT_DATA))
                arguments?.getParcelable<BankAccount>(ARG_BANK_ACCOUNT_DATA)?.let { bankAccount ->
                    this.bankAccount = bankAccount
                }
            if (it.containsKey(ARG_ACCOUNT_TYPE))
                arguments?.getInt(ARG_ACCOUNT_TYPE)?.let { accountTypeInt ->
                    when (accountTypeInt) {
                        AccountConfirmationType.COMPANY.accountType -> accountConfirmationType = AccountConfirmationType.COMPANY
                        AccountConfirmationType.FAMILY.accountType -> accountConfirmationType = AccountConfirmationType.FAMILY
                        AccountConfirmationType.OTHER.accountType -> accountConfirmationType = AccountConfirmationType.OTHER
                    }
                }
            if (it.containsKey(ARG_KYC_NAME))
                arguments?.getString(ARG_KYC_NAME)?.let { name ->
                    this.kYCName = name
                }

        }
        initViewModels()
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        uploadDocumentViewModel = viewModelProvider.get(UploadDocumentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_bank_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!::accountConfirmationType.isInitialized) {
            activity?.finish()
        }
        startObservingViewModels()
        setBankAccountData()
        setDocKycUI()
    }

    private fun setBankAccountData() {
        tvBankName.text = bankAccount.bankName
        tvAccountNumber.text = bankAccount.accNumber
        tvAccountHolderName.text = bankAccount.accName
    }

    private fun setDocKycUI() {
        if (accountConfirmationType == AccountConfirmationType.OTHER) {
            tvDocKTPNameTitle.text = getString(R.string.sbank_name_listed_on_ktp)
            tvDocumentTypeTag.text = getString(R.string.sbank_full_name)
            tvDocPathOrKTPName.text = kYCName
            tvImageConditionAndType.text = getString(R.string.sbank_name_matched_with_other)
            btn_uploadOrConfirmDocument.text = getString(R.string.sbank_yes_name_matched)
            btn_uploadOrConfirmDocument.isEnabled = true
            ivSelectFile.gone()
            btn_uploadOrConfirmDocument.setOnClickListener { uploadDocument() }
        } else {
            tvDocKTPNameTitle.text = getString(R.string.sbank_complete_document)
            if (accountConfirmationType == AccountConfirmationType.FAMILY)
                tvDocumentTypeTag.text = getString(R.string.sbank_pic_of_family_card)
            else
                tvDocumentTypeTag.text = getString(R.string.sbank_attach_image_company)
            tvDocPathOrKTPName.text = getString(R.string.sbank_select_file)
            tvImageConditionAndType.text = getString(R.string.sbank_attach_image_constraints)
            btn_uploadOrConfirmDocument.text = getString(R.string.button_next)
            btn_uploadOrConfirmDocument.isEnabled = false
            ivSelectFile.visible()
            ivSelectFile.setOnClickListener { openFilePicker() }

        }
    }

    private fun uploadDocument() {
        when (accountConfirmationType) {
            AccountConfirmationType.OTHER -> bankSettingAnalytics.eventConfirmAccountNameClick()
            AccountConfirmationType.FAMILY -> bankSettingAnalytics.eventFamilyDocumentSubmitClick()
            AccountConfirmationType.COMPANY -> bankSettingAnalytics.eventCompanyDocumentSubmitClick()
        }
        context?.let {context->
            val uploadDocumentPojo = getUploadDocumentPojo(context)
            uploadDocumentPojo?.let {
                uploadDocumentViewModel.uploadDocument(it)
            }
        }
    }

    private fun enableUploadDocument() {
        btn_uploadOrConfirmDocument.isEnabled = true
        btn_uploadOrConfirmDocument.setOnClickListener {
            uploadDocument()
        }
    }

    private fun openFilePicker() {
        val pickerTitle = if (accountConfirmationType == AccountConfirmationType.FAMILY)
            getString(R.string.sbank_pic_of_family_card)
        else getString(R.string.sbank_attach_image_company)
        context?.let {
            val builder = ImagePickerBuilder.getOriginalImageBuilder(it)
                    .withSimpleEditor()
                    .withSimpleMultipleSelection(maxPick = 1)
                    .apply {
                        maxFileSizeInKB = MAX_FILE_SIZE
                    }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalGlobal.IMAGE_PICKER)
            intent.putImagePickerBuilder(builder)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
                    setSelectedFile(selectedImage[0])
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setSelectedFile(filePath: String?) {
        filePath?.let {
            selectedFilePath = it
            tvDocPathOrKTPName.text = ImageUtils.getFileName(it)
            enableUploadDocument()
        }
    }

    private fun startObservingViewModels() {
        uploadDocumentViewModel.uploadDocumentStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DocumentUploadStarted -> progressBar.visible()
                is DocumentUploadEnd -> progressBar.gone()
                is DocumentUploaded -> {
                    setResultMessage(it.message)
                    activity?.finish()
                }
                is DocumentUploadError -> {
                    showErrorOnUI(it.throwable, null)
                }
            }
        })
    }

    private fun setResultMessage(message: String?) {
        message?.let {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putString(SettingBankActivity.UPLOAD_DOCUMENT_MESSAGE, message)
            intent.putExtras(bundle)
            activity?.setResult(Activity.RESULT_OK, intent)
        }
    }

    private fun showTickerMessage(message: String?) {
        message?.let {
            view?.let { view ->
                Toaster.make(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun showErrorOnUI(throwable: Throwable, retry: (() -> Unit)?) {
        context?.let { context ->
            view?.let { view ->
                retry?.let {
                    Toaster.make(view, SettingBankErrorHandler.getErrorMessage(context, throwable),
                            Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                            getString(R.string.sbank_promo_coba_lagi), View.OnClickListener { retry.invoke() })
                } ?: run {
                    Toaster.make(view, SettingBankErrorHandler.getErrorMessage(context, throwable),
                            Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }

            }
        }
    }

    private fun getUploadDocumentPojo(context: Context): UploadDocumentPojo? {
        val docType = when (accountConfirmationType) {
            AccountConfirmationType.OTHER -> DOC_TYPE_OTHER
            AccountConfirmationType.FAMILY -> DOC_TYPE_FAMILY
            AccountConfirmationType.COMPANY -> DOC_TYPE_COMPANY
        }
        if ((docType == DOC_TYPE_FAMILY || docType == DOC_TYPE_COMPANY) && selectedFilePath == null) {
            showErrorOnUI(Exception(context?.getString(R.string.sbank_please_select_photo)), null)
            return null
        }
        return UploadDocumentPojo(
                acc_id = bankAccount.accID,
                acc_name = bankAccount.accName,
                acc_number = bankAccount.accNumber,
                bank_id = bankAccount.bankID,
                doc_type = docType,
                document_name = ImageUtils.getFileName(selectedFilePath),
                document_base64 = ImageUtils.encodeToBase64(selectedFilePath),
                document_ext = ImageUtils.getFileExt(selectedFilePath),
                document_mime = ImageUtils.getMimeType(context, selectedFilePath))
    }

}
