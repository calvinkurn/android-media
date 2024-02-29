package com.tokopedia.shareexperience.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shareexperience.data.analytic.ShareExAnalytics
import com.tokopedia.shareexperience.data.di.component.ShareExComponentFactoryProvider
import com.tokopedia.shareexperience.databinding.ShareexperienceBottomSheetBinding
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.util.ShareExConstants
import com.tokopedia.shareexperience.domain.util.ShareExConstants.DefaultValue.DEFAULT_TITLE
import com.tokopedia.shareexperience.domain.util.ShareExLogger
import com.tokopedia.shareexperience.ui.adapter.ShareExBottomSheetAdapter
import com.tokopedia.shareexperience.ui.adapter.decoration.ShareExBottomSheetSpacingItemDecoration
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactoryImpl
import com.tokopedia.shareexperience.ui.listener.ShareExAffiliateRegistrationListener
import com.tokopedia.shareexperience.ui.listener.ShareExBottomSheetListener
import com.tokopedia.shareexperience.ui.listener.ShareExChannelListener
import com.tokopedia.shareexperience.ui.listener.ShareExChipsListener
import com.tokopedia.shareexperience.ui.listener.ShareExErrorListener
import com.tokopedia.shareexperience.ui.listener.ShareExImageGeneratorListener
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.uistate.ShareExChannelIntentUiState
import com.tokopedia.shareexperience.ui.util.ShareExIntentErrorEnum
import com.tokopedia.shareexperience.ui.util.ShareExMediaCleanupStorageWorker
import com.tokopedia.shareexperience.ui.util.copyTextToClipboard
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Use [com.tokopedia.shareexperience.ui.util.ShareExInitializer] to open this bottom sheet
 */
class ShareExBottomSheet :
    BottomSheetUnify(),
    ShareExChipsListener,
    ShareExImageGeneratorListener,
    ShareExAffiliateRegistrationListener,
    ShareExChannelListener,
    ShareExErrorListener {

    @Inject
    lateinit var viewModel: ShareExViewModel

    @Inject
    lateinit var analytics: ShareExAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private var viewBinding by autoClearedNullable<ShareexperienceBottomSheetBinding>()
    private val adapter by lazy {
        ShareExBottomSheetAdapter(
            ShareExTypeFactoryImpl(
                chipsListener = this,
                imageGeneratorListener = this,
                affiliateRegistrationListener = this,
                channelListener = this,
                errorListener = this
            )
        )
    }

    private var listener: ShareExBottomSheetListener? = null

    fun setListener(listener: ShareExBottomSheetListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater) {
        viewBinding = ShareexperienceBottomSheetBinding.inflate(inflater)
        setChild(viewBinding?.root)
        clearContentPadding = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    private fun initInjector() {
        activity?.application?.let {
            ShareExComponentFactoryProvider
                .instance
                .createShareExComponent(application = it)
                .inject(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetModel()
        initializeRecyclerView()
        initObservers()
        viewModel.processAction(ShareExAction.InitializePage)
    }

    @SuppressLint("DeprecatedMethod")
    private fun setupBottomSheetModel() {
        val args = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(BOTTOM_SHEET_DATA_KEY, ShareExBottomSheetArg::class.java)
        } else {
            arguments?.getParcelable(BOTTOM_SHEET_DATA_KEY)
        }
        args?.let {
            viewModel.bottomSheetArgs = args
            analytics.trackImpressionBottomSheet(
                identifier = it.identifier,
                pageTypeEnum = it.pageTypeEnum,
                shareId = getShareId(),
                label = args.trackerArg.labelImpressionBottomSheet
            )
            setCloseClickListener { _ ->
                analytics.trackActionClickClose(
                    identifier = it.identifier,
                    pageTypeEnum = it.pageTypeEnum,
                    shareId = getShareId(),
                    label = it.trackerArg.labelActionCloseIcon
                )
                dismiss()
            }
        }
    }

    private fun initializeRecyclerView() {
        context?.let {
            viewBinding?.shareexRvBottomSheet?.adapter = adapter
            viewBinding?.shareexRvBottomSheet?.itemAnimator = null
            viewBinding?.shareexRvBottomSheet?.isNestedScrollingEnabled = false
            viewBinding?.shareexRvBottomSheet?.layoutManager = LinearLayoutManager(it)
            viewBinding?.shareexRvBottomSheet?.addItemDecoration(
                ShareExBottomSheetSpacingItemDecoration(
                    8.dpToPx(it.resources.displayMetrics),
                    16.dpToPx(it.resources.displayMetrics)
                )
            )
        }
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            observeShortLinkUiState()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeBottomSheetUiState()
                }
            }
        }
        viewModel.setupViewModelObserver()
    }

    private suspend fun observeBottomSheetUiState() {
        viewModel.bottomSheetUiState.collectLatest {
            setBottomSheetTitle(it.title)
            it.uiModelList?.let { newList ->
                setBottomSheetData(newList = newList)
            }
        }
    }

    private fun setBottomSheetTitle(title: String) {
        bottomSheetTitle.text = title
    }

    private fun setBottomSheetData(newList: List<Visitable<in ShareExTypeFactory>>) {
        adapter.updateItems(newList)
    }

    private suspend fun observeShortLinkUiState() {
        viewModel.channelIntentUiState.collect {
            viewBinding?.shareexLayoutLoading?.showWithCondition(it.isLoading)
            /**
             * If loading, then do nothing
             * If error then do logging
             ** error affiliate, continue until success and dismiss bottom sheet, show toaster, and user manually copy
             ** error branch, skip and do nothing until success
             ** error default should not be possible
             ** error image downloader or using default URL, count as success
             * If success
             ** success channel copy link, copy text & show toaster
             ** success channel SMS, check if package is empty, then use manual intent
             ** success channel others, show native chooser
             ** success channels, open the app
             */
            if (it.isLoading) return@collect

            if (it.error != null) {
                Timber.d(it.error)
                ShareExLogger.logExceptionToServerLogger(
                    it.error,
                    userSession.deviceId,
                    it.errorEnum.toString()
                )
            }

            if (it.error != null && it.errorEnum == ShareExIntentErrorEnum.AFFILIATE_ERROR) {
                dismiss()
                listener?.onFailGenerateAffiliateLink(it.shortLink)
            }

            if (it.error == null ||
                it.errorEnum == ShareExIntentErrorEnum.IMAGE_DOWNLOADER ||
                it.errorEnum == ShareExIntentErrorEnum.DEFAULT_URL_ERROR
            ) {
                trackActionClickChannel(
                    it.channelEnum,
                    it.imageType
                )
                when (it.channelEnum) {
                    ShareExChannelEnum.COPY_LINK -> {
                        val isSuccessCopy = context?.copyTextToClipboard(it.shortLink)
                        if (isSuccessCopy == true) {
                            dismiss()
                            listener?.onSuccessCopyLink()
                        }
                    }
                    ShareExChannelEnum.SMS -> {
                        openIntentSms(it)
                        dismiss()
                    }
                    ShareExChannelEnum.OTHERS -> {
                        openIntentChooser(it)
                        dismiss()
                    }
                    else -> {
                        it.intent?.let { intent ->
                            if (intent.type == ShareExMimeTypeEnum.IMAGE.textType) {
                                context?.copyTextToClipboard(it.message)
                            }
                            navigateWithIntent(intent)
                            dismiss()
                        }
                    }
                }
            }
        }
    }

    private fun trackActionClickChannel(
        channelEnum: ShareExChannelEnum?,
        imageTypeEnum: ShareExImageTypeEnum
    ) {
        viewModel.bottomSheetArgs?.let {
            if (channelEnum != null) {
                analytics.trackActionClickChannel(
                    identifier = it.identifier,
                    pageTypeEnum = it.pageTypeEnum,
                    shareId = getShareId(),
                    channel = channelEnum.trackerName,
                    imageType = imageTypeEnum.value,
                    label = it.trackerArg.labelActionClickChannel
                )
            }
        }
    }

    override fun onChipClicked(position: Int, text: String) {
        viewModel.processAction(ShareExAction.UpdateShareBody(position, text))
    }

    override fun onImageChanged(imageUrl: String) {
        viewModel.processAction(ShareExAction.UpdateShareImage(imageUrl))
    }

    override fun onImpressionAffiliateRegistrationCard() {
        viewModel.bottomSheetArgs?.let {
            analytics.trackImpressionTickerAffiliate(
                identifier = it.identifier,
                pageTypeEnum = it.pageTypeEnum,
                shareId = getShareId(),
                label = it.trackerArg.labelImpressionAffiliateRegistration
            )
        }
    }

    override fun onAffiliateRegistrationCardClicked(appLink: String) {
        viewModel.bottomSheetArgs?.let {
            analytics.trackActionClickAffiliateRegistration(
                identifier = it.identifier,
                pageTypeEnum = it.pageTypeEnum,
                shareId = getShareId(),
                label = it.trackerArg.labelActionClickAffiliateRegistration
            )
        }
        navigateToPage(appLink)
    }

    override fun onChannelClicked(element: ShareExChannelItemModel) {
        viewModel.processAction(ShareExAction.GenerateLink(element))
    }

    override fun onErrorActionClicked() {
        listener?.refreshPage()
    }

    private fun navigateToPage(appLink: String) {
        context?.let { ctx ->
            val intent = RouteManager.getIntent(ctx, appLink)
            startActivity(intent)
        }
    }

    private fun navigateWithIntent(intent: Intent) {
        try {
            startActivity(intent)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            ShareExLogger.logExceptionToServerLogger(
                throwable = throwable,
                deviceId = userSession.deviceId,
                description = ::navigateWithIntent.name
            )
        }
    }

    private fun openIntentSms(intentUiState: ShareExChannelIntentUiState) {
        val intentPackage = intentUiState.intent?.`package` ?: ""
        if (intentPackage.isBlank()) {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(ShareExConstants.SMS.URI)
                putExtra(ShareExConstants.SMS.BODY, intentUiState.message)
            }
            navigateWithIntent(intent)
        } else {
            intentUiState.intent?.let {
                navigateWithIntent(it)
            }
        }
    }

    private fun openIntentChooser(intentUiState: ShareExChannelIntentUiState) {
        val intentChooser = Intent.createChooser(intentUiState.intent, DEFAULT_TITLE)
        navigateWithIntent(intentChooser)
    }

    private fun getShareId(): String {
        return viewModel.bottomSheetArgs
            ?.bottomSheetModel
            ?.bottomSheetPage
            ?.listShareProperty
            ?.getOrNull(viewModel.bottomSheetUiState.value.chipPosition)
            ?.shareId.toString()
    }

    override fun onDestroyView() {
        cleanUp()
        super.onDestroyView()
    }

    private fun cleanUp() {
        listener = null
        cleanUpMedia()
    }

    private fun cleanUpMedia() {
        context?.let {
            ShareExMediaCleanupStorageWorker.scheduleWorker(it)
        }
    }

    companion object {
        private const val BOTTOM_SHEET_DATA_KEY = "bottom_sheet_data_key"

        fun newInstance(bottomSheetArg: ShareExBottomSheetArg): ShareExBottomSheet {
            val fragment = ShareExBottomSheet()
            val bundle = Bundle().apply {
                putParcelable(BOTTOM_SHEET_DATA_KEY, bottomSheetArg)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
