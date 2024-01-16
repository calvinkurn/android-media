package com.tokopedia.shareexperience.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shareexperience.data.di.ShareExComponent
import com.tokopedia.shareexperience.databinding.ShareexperienceBottomSheetBinding
import com.tokopedia.shareexperience.domain.model.ShareExMimeTypeEnum
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.ui.adapter.ShareExBottomSheetAdapter
import com.tokopedia.shareexperience.ui.adapter.decoration.ShareExBottomSheetSpacingItemDecoration
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactoryImpl
import com.tokopedia.shareexperience.ui.listener.ShareExAffiliateRegistrationListener
import com.tokopedia.shareexperience.ui.listener.ShareExChannelListener
import com.tokopedia.shareexperience.ui.listener.ShareExChipsListener
import com.tokopedia.shareexperience.ui.listener.ShareExErrorListener
import com.tokopedia.shareexperience.ui.listener.ShareExImageGeneratorListener
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
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ShareExViewModel by activityViewModels { viewModelFactory }

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
        overlayClickDismiss = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    @Suppress("UNCHECKED_CAST")
    private fun initInjector() {
        (activity as HasComponent<ShareExComponent>).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        initObservers()
        viewModel.processAction(ShareExAction.InitializePage)
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
        viewModel.shortLinkUiState.collect {
            viewBinding?.shareexLayoutLoading?.showWithCondition(it.isLoading)
            if (!it.isLoading) {
                when {
                    (it.showToaster && !it.error?.message.isNullOrBlank()) -> {
                        showToaster(
                            it.error?.message.toString(),
                            Toaster.TYPE_ERROR,
                            Toaster.LENGTH_LONG,
                            actionText = "Salin Link"
                        ) {
                            showToaster(
                                "Link berhasil disalin!",
                                Toaster.TYPE_NORMAL,
                                Toaster.LENGTH_SHORT
                            )
                        }
                    }
                    (it.intent != null) -> {
                        if (it.intent.type == ShareExMimeTypeEnum.IMAGE.textType) {
                            copyTextToClipboard(it.message)
                        }
                        navigateWithIntent(it.intent)
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
        if (activity is ShareExLoadingActivity) {
            (activity as? ShareExLoadingActivity)?.refreshPage()
        }
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

    fun copyTextToClipboard(text: String) {
        try {
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText("Share Link", text)
            clipboard?.setPrimaryClip(clip)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }
}
