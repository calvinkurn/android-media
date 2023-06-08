package com.tokopedia.shop_nib.presentation.submission

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.constant.LocaleConstant
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.campaign.utils.extension.setHyperlinkText
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.campaign.utils.extension.startLoading
import com.tokopedia.campaign.utils.extension.stopLoading
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop_nib.R
import com.tokopedia.shop_nib.databinding.SsnFragmentNibSubmissionBinding
import com.tokopedia.shop_nib.di.component.DaggerShopNibComponent
import com.tokopedia.shop_nib.presentation.datepicker.NibDatePicker
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiEffect
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiEvent
import com.tokopedia.shop_nib.presentation.submission.uimodel.UiState
import com.tokopedia.shop_nib.presentation.submission_success.NibSubmissionSuccessActivity
import com.tokopedia.shop_nib.util.helper.FileHelper
import com.tokopedia.shop_nib.util.constant.UrlConstant
import com.tokopedia.shop_nib.util.extension.toMb
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class NibSubmissionFragment : BaseDaggerFragment() {

    companion object {
        private const val REQUEST_CODE_SELECT_FILE = 100
        private const val DATE_FORMAT = "dd/MM/yyyy"
        private const val IMAGE_URL_PDF_FILE_THUMBNAIL = "https://images.tokopedia.net/img/android/campaign/ssn_ic_pdf_thumbnail.png"

        @JvmStatic
        fun newInstance(): NibSubmissionFragment {
            return NibSubmissionFragment()
        }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var fileHelper: FileHelper

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider[NibSubmissionViewModel::class.java] }
    private var binding by autoClearedNullable<SsnFragmentNibSubmissionBinding>()

    override fun getScreenName(): String = NibSubmissionFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopNibComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsnFragmentNibSubmissionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.processEvent(UiEvent.RecordImpression)
        setupView()
        observeUiState()
        observeUiEffect()
        applyUnifyBackgroundColor()
    }

    private fun setupView() {
        binding?.run {
            header.setNavigationOnClickListener { activity?.finish() }
            layoutFilePickerDefault.setOnClickListener { showFilePicker() }
            layoutFilePickerError.setOnClickListener { showFilePicker() }
            iconClose.setOnClickListener { viewModel.processEvent(UiEvent.UnselectFile) }
            tpgNibBenefit.setHyperlinkText(
                fullText = context?.getString(R.string.ssn_nib_benefit).orEmpty(),
                hyperlinkSubstring = context?.getString(R.string.ssn_here).orEmpty(),
                onHyperlinkClick = { routeToUrl(UrlConstant.URL_SELLER_EDU_SHOP_NIB) }
            )

            imgFile.scaleType = ImageView.ScaleType.CENTER_CROP

            tauNibPublishedDate.editText.setOnClickListener { viewModel.processEvent(UiEvent.TapChangeDate) }
            tauNibPublishedDate.editText.inputType = InputType.TYPE_NULL
            tauNibPublishedDate.editText.isFocusable = false
            tauNibPublishedDate.icon1.setOnClickListener {
                viewModel.processEvent(UiEvent.TapChangeDate)
            }

            btnFinish.setOnClickListener {
                viewModel.processEvent(UiEvent.SubmitFile)
            }
        }

        useOpenSauceOneFont()
    }
    private fun useOpenSauceOneFont() {
        Typography.setUnifyTypographyOSO(true)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun handleUiState(uiState: UiState) {
        binding?.run {
            if (uiState.isLoading) btnFinish.startLoading() else btnFinish.stopLoading()
            btnFinish.isEnabled = uiState.isInputValid
            tauNibPublishedDate.editText.setText(uiState.selectedDate?.formatTo(DATE_FORMAT))
        }

        renderFilePreview(uiState.fileState)
    }

    private fun handleEffect(effect: UiEffect) {
        when (effect) {
            UiEffect.RedirectToSubmissionSuccess -> redirectToSubmissionSuccessPage()
            is UiEffect.ShowDatePicker -> showDatePickerBottomSheet(effect.previouslySelectedDate)
            UiEffect.ShowFilePicker -> showFilePicker()
            is UiEffect.ShowError -> {
                context?.let { context ->
                    view?.showToasterError(effect.error, context.getString(R.string.ssn_ok))
                }
            }
            is UiEffect.ShowUploadError -> {
                context?.let { context ->
                    view?.showToasterError(effect.errorMessage, context.getString(R.string.ssn_ok))
                }
            }
        }
    }


    private fun renderFilePreview(fileState: UiState.FileState) {
        when (fileState) {
            UiState.FileState.NotSelected -> {
                binding?.layoutFilePickerDefault?.visible()
                binding?.layoutFilePickerError?.gone()
                binding?.layoutFilePickerSelected?.gone()
                binding?.tpgErrorMessage?.gone()
            }
            UiState.FileState.InvalidFileExtension -> {
                binding?.layoutFilePickerDefault?.gone()
                binding?.layoutFilePickerError?.visible()
                binding?.layoutFilePickerSelected?.gone()

                binding?.tpgErrorMessage?.visible()
                binding?.tpgErrorMessage?.text =
                    context?.getString(R.string.ssn_error_message_file_extension)
            }
            UiState.FileState.ExceedMaxFileSize -> {
                binding?.layoutFilePickerDefault?.gone()
                binding?.layoutFilePickerError?.visible()
                binding?.layoutFilePickerSelected?.gone()

                binding?.tpgErrorMessage?.visible()
                binding?.tpgErrorMessage?.text =
                    context?.getString(R.string.ssn_error_message_file_size)
            }
            is UiState.FileState.Valid -> {
                binding?.layoutFilePickerDefault?.gone()
                binding?.layoutFilePickerError?.gone()
                binding?.layoutFilePickerSelected?.visible()
                binding?.tpgErrorMessage?.gone()

                renderSelectedFileThumbnail(fileState.fileUri, fileState.fileSizeKb)
            }
        }
    }

    private fun redirectToSubmissionSuccessPage() {
        activity?.finish()
        NibSubmissionSuccessActivity.start(activity ?: return)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SELECT_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) { handleSelectedFile(data) }
        }
    }


    private fun showFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/jpeg,image/png,application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(
            Intent.EXTRA_MIME_TYPES,
            arrayOf("image/png", "image/jpeg", "application/pdf")
        )
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)


        try {
            startActivityForResult(intent, REQUEST_CODE_SELECT_FILE)
        } catch (_: ActivityNotFoundException) {
        }

    }

    private fun handleSelectedFile(intent: Intent) {
        val fileUri = intent.data ?: return
        val fileSizeBytes = fileHelper.getFileSizeInBytes(fileUri)
        val fileExtension = fileHelper.getFileExtension(fileUri)

        viewModel.processEvent(UiEvent.ConfirmFile(fileUri.toString(), fileExtension, fileSizeBytes))
    }

    private fun renderSelectedFileThumbnail(uri: String, fileSizeInKb: Long) {
        val fileUri = Uri.parse(uri)
        val fileName = fileHelper.getFileName(fileUri)
        val fileExtension = fileHelper.getFileExtension(fileUri)
        val isPdf = fileExtension == "pdf"

        binding?.run {
            if (isPdf) {
                imgFile.loadImage(IMAGE_URL_PDF_FILE_THUMBNAIL)
            } else {
                imgFile.loadImage(fileUri)
            }

            tpgFileName.text = fileName

            val fileSizeInMb = fileSizeInKb.toMb()
            val roundedFileSizeInMb = String.format(LocaleConstant.INDONESIA,"%.2f", fileSizeInMb)
            tpgFileSize.text =
                context?.getString(R.string.ssn_placeholder_file_size, roundedFileSizeInMb)
        }
    }


    private fun showDatePickerBottomSheet(previouslySelectedDate: Date?) {
        if (!isAdded) return

        val param = NibDatePicker.Param(
            defaultDate = previouslySelectedDate,
            title = context?.getString(R.string.ssn_nib_publish_date).orEmpty(),
            buttonWording = context?.getString(R.string.ssn_select).orEmpty()
        )
        val datePicker = NibDatePicker(param)
        val onDateSelected: (Date) -> Unit = { selectedDate ->
            viewModel.processEvent(UiEvent.ConfirmDate(selectedDate))
        }
        val onDatePickerDismissed : () -> Unit = {
            viewModel.processEvent(UiEvent.DatePickerDismissed)
        }

        datePicker.show(
            activity ?: return,
            childFragmentManager,
            onDateSelected,
            onDatePickerDismissed
        )
    }
}
