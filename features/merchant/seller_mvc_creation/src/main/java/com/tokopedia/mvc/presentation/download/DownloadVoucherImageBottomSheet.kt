package com.tokopedia.mvc.presentation.download

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.extension.applyPaddingToLastItem
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetDownloadVoucherImageBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageEffect
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageEvent
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageUiModel
import com.tokopedia.mvc.presentation.download.uimodel.VoucherImageUiState
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.tracker.DownloadVoucherImageTracker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DownloadVoucherImageBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BANNER_URL = "banner_url"
        private const val SQUARE_URL = "square_url"
        private const val PORTRAIT_URL = "portrait_url"


        @JvmStatic
        fun newInstance(
            voucherId: Long,
            bannerUrl: String,
            squareUrl: String,
            portraitUrl: String
        ): DownloadVoucherImageBottomSheet {
            return DownloadVoucherImageBottomSheet().apply {
                arguments = Bundle().apply {
                    putLong(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
                    putString(BANNER_URL, bannerUrl)
                    putString(SQUARE_URL, squareUrl)
                    putString(PORTRAIT_URL, portraitUrl)
                }
            }
        }
    }

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    private val voucherImageAdapter by lazy { DownloadVoucherImageAdapter() }
    private val voucherId by lazy { arguments?.getLong(BundleConstant.BUNDLE_VOUCHER_ID).orZero() }
    private val bannerUrl by lazy { arguments?.getString(BANNER_URL).toBlankOrString() }
    private val squareUrl by lazy { arguments?.getString(SQUARE_URL).toBlankOrString() }
    private val portraitUrl by lazy { arguments?.getString(PORTRAIT_URL).toBlankOrString() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var tracker: DownloadVoucherImageTracker

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(DownloadVoucherImageViewModel::class.java) }

    private var binding by autoClearedNullable<SmvcBottomsheetDownloadVoucherImageBinding>()
    private var onDownloadSuccess: () -> Unit = {}
    private var onDownloadError : () -> Unit = {}

    private fun setupDependencyInjection() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        setCloseClickListener {
            tracker.sendClickCancelEvent()
            dismiss()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tracker.sendBottomSheetVisibleImpression()

        val images = populateImages()
        setupView(images)
        applyUnifyBackgroundColor()
        observeUiEffect()
        observeUiState()
        viewModel.processEvent(VoucherImageEvent.PopulateInitialData(voucherId, images))
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SmvcBottomsheetDownloadVoucherImageBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(getString(R.string.smvc_select_voucher_image_ratio))
    }

    private fun setupView(images: List<VoucherImageUiModel>) {
        setupClickListener()
        setupList()
        binding?.tvMvcDownloadDescription?.text =
            context?.getString(R.string.smvc_placeholder_download_voucher_description, images.size)
    }

    private fun setupClickListener() {
        binding?.btnDownload?.setOnClickListener {
            viewModel.processEvent(VoucherImageEvent.TapDownloadButton)
        }
    }

    private fun setupList() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity ?: return, LinearLayoutManager.VERTICAL, false)
            applyPaddingToLastItem()
            voucherImageAdapter.setOnCheckboxClick { index, isChecked ->
                val selectedImage = voucherImageAdapter.snapshot()[index]

                if (isChecked) {
                    viewModel.processEvent(VoucherImageEvent.AddImageToSelection(selectedImage.imageUrl))
                } else {
                    viewModel.processEvent(VoucherImageEvent.RemoveImageFromSelection(selectedImage.imageUrl))
                }
            }
            voucherImageAdapter.setOnDropdownClick { index ->
                val selectedImage = voucherImageAdapter.snapshot()[index]
                viewModel.processEvent(VoucherImageEvent.TapDropdownIcon(selectedImage.imageUrl))
            }
            adapter = voucherImageAdapter
        }
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


    private fun handleUiState(uiState: VoucherImageUiState) {
        renderList(uiState.voucherImages)
        renderButton(uiState.selectedImageUrls.count())
    }

    private fun handleEffect(effect: VoucherImageEffect) {
        when (effect) {
            is VoucherImageEffect.DownloadImages -> {
                checkDownloadPermission(effect.selectedImageUrls)
                sendDownloadVoucherTracker(effect.voucherId, effect.selectedImageUrls)
            }
        }
    }

    private fun sendDownloadVoucherTracker(voucherId: Long, selectedImages: List<VoucherImageUiModel>) {
        tracker.sendClickDownloadButtonEvent(voucherId, selectedImages)
    }

    private fun renderButton(selectedImageCount: Int) {
        binding?.btnDownload?.isEnabled = selectedImageCount.isMoreThanZero()
    }

    private fun renderList(voucherImages : List<VoucherImageUiModel>) {
        voucherImageAdapter.submit(voucherImages)
    }

    fun setOnDownloadSuccess(onDownloadSuccess: () -> Unit) {
        this.onDownloadSuccess = onDownloadSuccess
    }

    fun setOnDownloadError(onDownloadError: () -> Unit) {
        this.onDownloadError = onDownloadError
    }

    private fun checkDownloadPermission(imageUrls: List<VoucherImageUiModel>) {
        val listener = object : PermissionCheckerHelper.PermissionCheckListener {
            override fun onPermissionDenied(permissionText: String) {
                permissionCheckerHelper.onPermissionDenied(requireActivity(), permissionText)
                binding?.btnDownload.showToasterError(getString(R.string.smvc_storage_permission_enabled_needed))
            }

            override fun onNeverAskAgain(permissionText: String) {
                permissionCheckerHelper.onNeverAskAgain(requireActivity(), permissionText)
            }

            override fun onPermissionGranted() {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    imageUrls.forEach { downloadFile(it.imageUrl) }
                }
            }
        }

        permissionCheckerHelper.checkPermission(
            fragment = this,
            permission = PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
            listener = listener
        )

    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun downloadFile(uri: String) {
        val downloadCompleteListener = object : DownloadHelper.DownloadHelperListener {
            override fun onDownloadComplete() {
                onDownloadSuccess()
                dismiss()
            }
        }

        try {
            val helper = DownloadHelper(
                context = requireActivity(),
                uri = uri,
                filename = System.currentTimeMillis().toString() + ".jpg",
                listener = downloadCompleteListener
            )
            helper.downloadFile { true }
        } catch (e: Exception) {
            onDownloadError()
            dismiss()
        }
    }

    private fun populateImages(): List<VoucherImageUiModel> {
        val voucherImages = mutableListOf<VoucherImageUiModel>()
        if (squareUrl.isNotEmpty()) {
            voucherImages.add(
                VoucherImageUiModel(
                    ratio = getString(R.string.smvc_square),
                    description = getString(R.string.smvc_square_description),
                    isSelected = true,
                    isExpanded = false,
                    imageRatio = ImageRatio.SQUARE,
                    imageUrl = squareUrl
                )
            )
        }

        if (portraitUrl.isNotEmpty()) {
            voucherImages.add(
                VoucherImageUiModel(
                    ratio = getString(R.string.smvc_vertical),
                    description = getString(R.string.smvc_vertical_description),
                    isSelected = true,
                    isExpanded = false,
                    imageRatio = ImageRatio.VERTICAL,
                    imageUrl = portraitUrl
                )
            )
        }

        if (bannerUrl.isNotEmpty()) {
            voucherImages.add(
                VoucherImageUiModel(
                    ratio = getString(R.string.smvc_horizontal),
                    description = getString(R.string.smvc_horizontal_description),
                    isSelected = true,
                    isExpanded = false,
                    imageRatio = ImageRatio.HORIZONTAL,
                    imageUrl = bannerUrl
                )
            )
        }

        return voucherImages
    }

}
