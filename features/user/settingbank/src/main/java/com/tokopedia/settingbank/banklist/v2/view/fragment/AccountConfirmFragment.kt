package com.tokopedia.settingbank.banklist.v2.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType
import com.tokopedia.imagepicker.picker.main.builder.*
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.domain.AddBankRequest
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.domain.UploadDocumentPojo
import com.tokopedia.settingbank.banklist.v2.util.AccountConfirmationType
import com.tokopedia.settingbank.banklist.v2.util.ImageUtils
import com.tokopedia.settingbank.banklist.v2.view.viewModel.UploadDocumentViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewState.DocumentUploadEnd
import com.tokopedia.settingbank.banklist.v2.view.viewState.DocumentUploadError
import com.tokopedia.settingbank.banklist.v2.view.viewState.DocumentUploadStarted
import com.tokopedia.settingbank.banklist.v2.view.viewState.DocumentUploaded
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_confirm_bank_account.*
import javax.inject.Inject

const val ARG_BANK_ACCOUNT_DATA = "arg_bank_account_data"
const val ARG_ACCOUNT_TYPE = "arg_account_type"
const val ARG_KYC_NAME = "arg_kyc_name"


class AccountConfirmFragment : BaseDaggerFragment() {


    override fun getScreenName(): String? = null

    private val REQUEST_CODE_IMAGE: Int = 101

    private val DOC_TYPE_IMAGE = 1
    private val DOC_TYPE_NAME = 3

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
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
        val uploadDocumentPojo = getUploadDocumentPojo()
        uploadDocumentPojo?.let {
            uploadDocumentViewModel.uploadDocument(it)
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
            val builder = ImagePickerBuilder(pickerTitle,
                    intArrayOf(ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA),
                    GalleryType.IMAGE_ONLY, 2048,
                    ImagePickerBuilder.DEFAULT_MIN_RESOLUTION, ImageRatioTypeDef.ORIGINAL, true,
                    ImagePickerEditorBuilder(
                            intArrayOf(ImageEditActionTypeDef.ACTION_BRIGHTNESS, ImageEditActionTypeDef.ACTION_CONTRAST,
                                    ImageEditActionTypeDef.ACTION_CROP, ImageEditActionTypeDef.ACTION_ROTATE),
                            false, null),
                    ImagePickerMultipleSelectionBuilder(
                            null, null, -1, 1
                    ))
            val intent = ImagePickerActivity.getIntent(it, builder)
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_IMAGE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage = data.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
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
        uploadDocumentViewModel.uploadDocumentStatus.observe(this, Observer {
            when (it) {
                is DocumentUploadStarted -> progressBar.visible()
                is DocumentUploadEnd -> progressBar.gone()
                is DocumentUploaded -> {
                    showTickerMessage(it.message)
                    activity?.finish()
                }
                is DocumentUploadError -> {
                    showErrorOnUI(it.errorMessage, null)
                }
            }
        })
    }

    private fun showTickerMessage(message: String?) {
        message?.let {
            view?.let { view ->
                Toaster.make(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun showErrorOnUI(errorMessage: String?, retry: (() -> Unit)?) {
        errorMessage?.let {
            view?.let { view ->
                retry?.let {
                    Toaster.make(view, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                            getString(R.string.sbank_promo_coba_lagi), View.OnClickListener { retry.invoke() })
                } ?: run {
                    Toaster.make(view, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }

            }
        }
    }

    private fun getUploadDocumentPojo(): UploadDocumentPojo? {
        val docType = if (accountConfirmationType == AccountConfirmationType.OTHER) DOC_TYPE_NAME else DOC_TYPE_IMAGE
        if (docType == DOC_TYPE_IMAGE && selectedFilePath == null) {
            showErrorOnUI("Silakan pilih foto dokumen", null)
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
                document_mime = ImageUtils.getMimeType(context!!, selectedFilePath))
    }

}
