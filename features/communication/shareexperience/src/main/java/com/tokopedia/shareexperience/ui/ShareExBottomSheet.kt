package com.tokopedia.shareexperience.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.data.di.DaggerShareExComponent
import com.tokopedia.shareexperience.databinding.ShareexperienceBottomSheetBinding
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
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
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ShareExBottomSheet :
    BottomSheetUnify(),
    ShareExChipsListener,
    ShareExImageGeneratorListener,
    ShareExAffiliateRegistrationListener,
    ShareExChannelListener,
    ShareExErrorListener {

    @Inject
    lateinit var viewModel: ShareExViewModel

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
        val baseMainApplication = activity?.applicationContext as? BaseMainApplication
        baseMainApplication?.let {
            DaggerShareExComponent
                .builder()
                .baseAppComponent(it.baseAppComponent)
                .build()
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
        viewModel.bottomSheetArgs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(BOTTOM_SHEET_DATA_KEY, ShareExBottomSheetArg::class.java)
        } else {
            arguments?.getParcelable(BOTTOM_SHEET_DATA_KEY)
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
             * If error, show toaster or do nothing
             * If channel copy link, copy text & show toaster
             * If channel others, show native chooser
             * If show toaster is true and error message not null/empty, show toaster
             */
            if (!it.isLoading) {
                if (it.error != null) {
                    if (it.error.message?.contains("affiliate", ignoreCase = true) == true) {
                        showFailGenerateAffiliateLinkToaster(it.message, it.error.message.toString())
                    }
                } else {
                    when(it.channelEnum) {
                        ShareExChannelEnum.COPY_LINK -> {
                            val isSuccessCopy = copyTextToClipboard(it.message)
                            if (isSuccessCopy) {
                                dismiss()
                                listener?.onSuccessCopyLink()
                            }
                        }
                        ShareExChannelEnum.OTHERS -> {
                            // Show intent chooser
                        }
                        else -> {
                            it.intent?.let { intent ->
                                if (intent.type == ShareExMimeTypeEnum.IMAGE.textType) {
                                    copyTextToClipboard(it.message)
                                }
                                navigateWithIntent(intent)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showToaster(
        text: String,
        type: Int,
        duration: Int,
        actionText: String = "",
        clickListener: View.OnClickListener? = null
    ) {
        view?.let {
            if (actionText.isNotBlank() && clickListener != null) {
                Toaster.build(
                    view = it.rootView,
                    text = text,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = clickListener
                ).show()
            } else {
                Toaster.build(
                    view = it.rootView,
                    text = text,
                    duration = duration,
                    type = type
                ).show()
            }
        }
    }

    private fun showFailGenerateAffiliateLinkToaster(
        message: String,
        toasterMessage: String
    ) {
        showToaster(toasterMessage, Toaster.TYPE_ERROR, Toaster.LENGTH_LONG,
            getString(R.string.shareex_action_copy_link)
        ) {
            val isSuccessCopy = copyTextToClipboard(message)
            if (isSuccessCopy) {
                dismiss()
                listener?.onSuccessCopyLink()
            }
        }
    }

    override fun onChipClicked(position: Int, text: String) {
        viewModel.processAction(ShareExAction.UpdateShareBody(position, text))
    }

    override fun onImageChanged(imageUrl: String) {
        viewModel.processAction(ShareExAction.UpdateShareImage(imageUrl))
    }

    override fun onAffiliateRegistrationCardClicked(appLink: String) {
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
        }
    }

    private fun copyTextToClipboard(text: String): Boolean {
        return try {
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(getString(R.string.shareex_action_copy_link), text)
            clipboard?.setPrimaryClip(clip)
            true
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            false
        }
    }

    override fun onDestroyView() {
        listener = null
        super.onDestroyView()
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
