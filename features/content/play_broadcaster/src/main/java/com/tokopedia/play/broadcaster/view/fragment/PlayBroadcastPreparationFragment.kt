package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.broadcaster.revamp.util.error.BroadcasterErrorType
import com.tokopedia.broadcaster.revamp.util.error.BroadcasterException
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroadcastPreparationBinding
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.setup.schedule.util.SchedulePicker
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState
import com.tokopedia.play.broadcaster.ui.state.ScheduleUiModel
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play.broadcaster.view.analyticmanager.PreparationAnalyticManager
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayTimerLiveCountDown
import com.tokopedia.play.broadcaster.view.custom.actionbar.ActionBarView
import com.tokopedia.play.broadcaster.view.custom.preparation.CoverFormView
import com.tokopedia.play.broadcaster.view.custom.preparation.PreparationMenuView
import com.tokopedia.play.broadcaster.view.custom.preparation.TitleFormView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.loading.LoadingDialogFragment
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.viewmodel.*
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.lifecycle.whenLifecycle
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.hideKeyboard
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on January 24, 2022
 */
class PlayBroadcastPreparationFragment @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val viewModelFactory: ViewModelFactory,
    private val analytic: PlayBroadcastAnalytic,
    private val analyticManager: PreparationAnalyticManager,
) : PlayBaseBroadcastFragment(), FragmentWithDetachableView,
    ActionBarView.Listener,
    PreparationMenuView.Listener,
    TitleFormView.Listener,
    CoverFormView.Listener {

    /** ViewModel */
    private lateinit var viewModel: PlayBroadcastPrepareViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var scheduleViewModel: BroadcastScheduleViewModel

    /** View */
    private var _binding by viewBinding<FragmentPlayBroadcastPreparationBinding>()
    private val binding get() = _binding!!

    /** Others */
    private val fragmentViewContainer = FragmentViewContainer()

    private lateinit var earlyLiveStreamDialog: DialogUnify

    override fun getScreenName(): String = "Play Prepare Page"

    override fun getViewContainer(): FragmentViewContainer = fragmentViewContainer

    private val eventBus by viewLifecycleBound(
        creator = { EventBus<Event>() },
    )

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    private val schedulePicker by lifecycleBound(
        creator = { SchedulePicker(this) },
        onLifecycle = whenLifecycle {
            onPause { it.dismiss() }
        }
    )

    private val schedulePickerListener = object : SchedulePicker.Listener {
        override fun onDeleteSchedule(wrapper: SchedulePicker) {
            eventBus.emit(Event.DeleteSchedule)
        }

        override fun onSaveSchedule(wrapper: SchedulePicker, date: Date) {
            eventBus.emit(Event.SaveSchedule(date))
        }

        override fun onCancelSetupSchedule(wrapper: SchedulePicker) {
            eventBus.emit(Event.CloseSetupSchedule)
        }
    }

    private val loadingDialogFragment: LoadingDialogFragment by lazy(LazyThreadSafetyMode.NONE) {
        val setupClass = LoadingDialogFragment::class.java
        val fragmentFactory = childFragmentManager.fragmentFactory
        val fragment = fragmentFactory.instantiate(requireActivity().classLoader, setupClass.name) as LoadingDialogFragment
        fragment.setLoaderType(LoadingDialogFragment.LoaderType.CIRCULAR)
        fragment
    }

    /** Lifecycle */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastPrepareViewModel::class.java)
        parentViewModel = ViewModelProvider(
            requireActivity(),
            parentViewModelFactoryCreator.create(requireActivity()),
        ).get(PlayBroadcastViewModel::class.java)
        scheduleViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(BroadcastScheduleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_broadcast_preparation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupInsets()
        setupListener()
        setupObserver()

        if(parentViewModel.channelTitle.isEmpty()) showTitleForm(true)
    }

    override fun onStart() {
        super.onStart()
        requireView().requestApplyInsetsWhenAttached()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBackPressed(): Boolean {
        return when {
            binding.formTitle.visibility == View.VISIBLE -> {
                return if(parentViewModel.channelTitle.isEmpty()) {
                    analytic.clickCloseOnSetupPage()
                    false
                }
                else {
                    showTitleForm(false)
                    true
                }
            }
            binding.formCover.visibility == View.VISIBLE -> {
                showCoverForm(false)
                true
            }
            else -> super.onBackPressed()
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductSetupFragment -> {
                childFragment.setDataSource(object : ProductSetupFragment.DataSource {
                    override fun getProductSectionList(): List<ProductTagSectionUiModel> {
                        //TODO("Use uiState directly when uiState already return StateFlow")
                        return if (::parentViewModel.isInitialized) parentViewModel.productSectionList
                        else emptyList()
                    }

                    override fun isEligibleForPin(): Boolean = false
                })
            }
            is PlayBroadcastSetupBottomSheet -> {
                childFragment.setListener(object: PlayBroadcastSetupBottomSheet.Listener {
                    override fun onCoverChanged(cover: PlayCoverUiModel) {
                        parentViewModel.submitAction(
                            PlayBroadcastAction.SetCover(cover)
                        )
                    }
                })
                childFragment.setDataSource(object : PlayBroadcastSetupBottomSheet.DataSource {
                    override fun getProductList(): List<ProductUiModel> {
                        return if (::parentViewModel.isInitialized) {
                            parentViewModel.productSectionList.flatMap { it.products }
                        }
                        else emptyList()
                    }

                    override fun getChannelId(): String {
                        return if (::parentViewModel.isInitialized) parentViewModel.channelId
                        else ""
                    }
                })
            }
        }
    }

    /** Setup */
    private fun setupView() {
        binding.viewActionBar.setShopName(parentViewModel.getShopName())
        binding.viewActionBar.setShopIcon(parentViewModel.getShopIconUrl())
        binding.formTitle.setMaxCharacter(viewModel.maxTitleChars)
    }

    private fun setupInsets() {
        binding.viewActionBar.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }

        binding.flBroStartLivestream.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.parent.requestLayout()
            }
        }

        binding.viewPreparationMenu.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun setupListener() {
        binding.apply {
            viewActionBar.setListener(this@PlayBroadcastPreparationFragment)
            viewPreparationMenu.setListener(this@PlayBroadcastPreparationFragment)
            formTitle.setListener(this@PlayBroadcastPreparationFragment)
            formCover.setListener(this@PlayBroadcastPreparationFragment)

            flBroStartLivestream.setOnClickListener {
                analytic.clickStartStreaming(parentViewModel.channelId)

                val schedule = parentViewModel.uiState.value.schedule.schedule
                if (schedule is BroadcastScheduleUiModel.Scheduled) {
                    val currentTime = Date()
                    if (currentTime.before(schedule.time)) {
                        getEarlyLiveStreamDialog().show()
                        analytic.viewDialogConfirmDeleteSchedule()
                        return@setOnClickListener
                    }
                }

                validateAndStartLive()
            }

            icBroPreparationSwitchCamera.setOnClickListener {
                analytic.clickSwitchCameraOnPreparation()
                broadcaster.flip()
            }
        }
    }

    private fun requireCover(ifCoverSet: () -> Unit) {
        if(viewModel.isCoverAvailable()) ifCoverSet()
        else {
            val errorMessage = getString(R.string.play_bro_cover_empty_error)
            toaster.showError(
                err = MessageErrorException(errorMessage),
                customErrMessage = errorMessage,
            )
            showCoverForm(true)
        }
    }

    private fun validateAndStartLive() {
        requireCover { startCountDown() }
    }

    private fun setupObserver() {
        observeTitle()
        observeCover()
        observeCreateLiveStream()

        observeUiState()
        observeUiEvent()
        observeViewEvent()
    }

    private fun observeTitle() {
        parentViewModel.observableTitle.observe(viewLifecycleOwner) {
            binding.viewPreparationMenu.isSetTitleChecked(true)
        }

        viewModel.observableUploadTitleEvent.observe(viewLifecycleOwner) {
            when (val content = it.peekContent()) {
                is NetworkResult.Fail -> {
                    binding.formTitle.setLoading(false)
                    toaster.showError(content.error, content.error.message)
                }
                is NetworkResult.Success -> {
                    if (!it.hasBeenHandled) {
                        binding.formTitle.setLoading(false)
                        showTitleForm(false)
                    }
                }
            }
        }
    }

    private fun observeCover() {
        parentViewModel.observableCover.observe(viewLifecycleOwner) {
            when (val croppedCover = it.croppedCover) {
                is CoverSetupState.Cropped.Uploaded -> {
                    if(croppedCover.coverImage.toString().isNotEmpty() &&
                        croppedCover.coverImage.toString().contains("http")) {
                        binding.viewPreparationMenu.isSetCoverChecked(true)
                        binding.formCover.setCover(croppedCover.coverImage.toString())
                    } else if (!croppedCover.localImage?.toString().isNullOrEmpty()){
                        binding.viewPreparationMenu.isSetCoverChecked(true)
                        binding.formCover.setCover(croppedCover.localImage.toString())
                    }
                }
            }
        }
    }

    private fun observeCreateLiveStream() {
        viewModel.observableCreateLiveStream.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> startBroadcast(it.data.ingestUrl)
                is NetworkResult.Fail -> {
                    showLoading(false)
                    toaster.showError(
                        err = it.error,
                        customErrMessage = it.error.message
                    )
                    analytic.viewErrorOnFinalSetupPage(getProperErrorMessage(it.error))
                }
                NetworkResult.Loading -> showLoading(true)
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            parentViewModel.uiState.withCache().collectLatest { (prevState, state) ->
                renderProductMenu(prevState?.selectedProduct, state.selectedProduct)
                renderScheduleMenu(state.schedule)
                renderSchedulePicker(prevState?.schedule, state.schedule)
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            parentViewModel.uiEvent.collect { event ->
                when (event) {
                    is PlayBroadcastEvent.ShowScheduleError -> {
                        val toasterContainer = schedulePicker.getToasterContainer() ?: return@collect
                        toaster.showErrorInView(
                            toasterContainer,
                            event.error,
                            bottomMargin = requireContext().resources.getDimensionPixelOffset(
                                unifyR.dimen.spacing_lvl3
                            )
                        )
                    }
                    is PlayBroadcastEvent.SetScheduleSuccess -> {
                        toaster.showToaster(
                            message = getString(
                                if (!event.isEdit) R.string.play_broadcast_schedule_set_success
                                else R.string.play_broadcast_schedule_edit_success
                            )
                        )
                    }
                    PlayBroadcastEvent.DeleteScheduleSuccess -> {
                        toaster.showToaster(
                            message = getString(R.string.play_broadcast_schedule_deleted)
                        )
                    }
                    is PlayBroadcastEvent.ShowError -> {
                        showLoading(false)
                        if (event.onRetry == null) toaster.showError(event.error)
                        else {
                            toaster.showError(event.error,
                                duration = Toaster.LENGTH_INDEFINITE,
                                actionLabel = getString(R.string.play_broadcast_try_again),
                                actionListener = {
                                    showLoading(true)
                                    event.onRetry.invoke()
                                })
                        }
                    }
                    PlayBroadcastEvent.BroadcastStarted -> {
                        showLoading(false)
                        openBroadcastLivePage()
                        parentViewModel.startTimer()
                    }
                    is PlayBroadcastEvent.ShowBroadcastError -> {
                        showLoading(false)
                        handleBroadcastError(event.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeViewEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            eventBus.subscribe().collect { event ->
                when (event) {
                    Event.ClickSetSchedule -> {
                        requireCover { showScheduleBottomSheet() }
                    }
                    is Event.SaveSchedule -> {
                        parentViewModel.submitAction(
                            PlayBroadcastAction.SetSchedule(event.date)
                        )
                    }
                    Event.DeleteSchedule -> {
                        parentViewModel.submitAction(PlayBroadcastAction.DeleteSchedule)
                    }
                    else -> {}
                }
            }
        }

        analyticManager.observe(
            viewLifecycleOwner.lifecycleScope,
            eventBus,
        )
    }

    private fun renderProductMenu(
        prevState: List<ProductTagSectionUiModel>?,
        state: List<ProductTagSectionUiModel>
    ) {
        if (prevState != state) {
            binding.viewPreparationMenu.isSetProductChecked(
                state.any { it.products.isNotEmpty() }
            )
        }
    }

    private fun renderScheduleMenu(
        state: ScheduleUiModel
    ) {
        binding.viewPreparationMenu.showScheduleMenu(state.canSchedule)

        binding.viewPreparationMenu.isSetScheduleChecked(
            state.schedule is BroadcastScheduleUiModel.Scheduled
        )
    }

    private fun renderSchedulePicker(
        prevState: ScheduleUiModel?,
        state: ScheduleUiModel,
    ) {
        if (prevState == state) return

        schedulePicker.updateState {
            it.copy(
                isLoading = state.state == NetworkState.Loading,
                hasSchedule = state.schedule is BroadcastScheduleUiModel.Scheduled
            )
        }

        if (prevState?.state != state.state && state.state == NetworkState.Success) {
            schedulePicker.dismiss()
        }
    }

    /** Form */
    private fun showTitleForm(isShow: Boolean) {
        if(isShow) {
            showMainComponent(false)

            binding.formTitle.setTitle(parentViewModel.channelTitle)
            binding.formTitle.setLoading(false)
            binding.formTitle.visibility = View.VISIBLE
        }
        else {
            showMainComponent(true)

            binding.formTitle.visibility = View.GONE
        }
    }

    private fun showCoverForm(isShow: Boolean) {
        if(isShow) {
            showMainComponent(false)

            binding.formCover.setTitle(parentViewModel.channelTitle)
            binding.formCover.setShopName(parentViewModel.getShopName())
            binding.formCover.visibility = View.VISIBLE
        }
        else {
            showMainComponent(true)

            binding.formCover.visibility = View.GONE
        }
    }

    private fun showScheduleBottomSheet() {
        val schedule = parentViewModel.uiState.value.schedule
        schedulePicker.show(
            minDate = GregorianCalendar().apply {
                time = schedule.config.minDate
            },
            maxDate = GregorianCalendar().apply {
                time = schedule.config.maxDate
            },
            defaultDate = GregorianCalendar().apply {
                time = if (schedule.schedule is BroadcastScheduleUiModel.Scheduled) {
                    schedule.schedule.time
                } else schedule.config.defaultDate
            },
            selectedDate = if (schedule.schedule is BroadcastScheduleUiModel.Scheduled) {
                GregorianCalendar().apply {
                    time = schedule.schedule.time
                }
            } else null,
            listener = schedulePickerListener,
        )
    }

    /** Callback Action Bar */
    override fun onClickClosePreparation() {
        analytic.clickCloseOnPreparation()

        activity?.onBackPressed()
    }

    /** Callback Preparation Menu */
    override fun onClickSetTitle() {
        analytic.clickSetupTitleMenu()

        showTitleForm(true)
    }

    override fun onClickSetCover() {
        analytic.clickSetupCoverMenu()

        showCoverForm(true)
    }

    override fun onClickSetProduct() {
        analytic.clickSetupProductMenu()
        
        childFragmentManager.beginTransaction()
            .add(ProductSetupFragment::class.java, null, null)
            .commit()
    }

    override fun onClickSetSchedule() {
        eventBus.emit(Event.ClickSetSchedule)
    }

    /** Callback Title Form */
    override fun onCloseTitleForm(view: TitleFormView) {
        hideKeyboard()
        activity?.onBackPressed()
    }

    override fun onTitleSaved(view: TitleFormView, title: String) {
        analytic.clickSubmitTitle()

        hideKeyboard()
        binding.formTitle.setLoading(true)
        viewModel.uploadTitle(title)
    }

    /** Callback Cover Form */
    override fun onCloseCoverForm() {
        activity?.onBackPressed()
    }

    override fun onClickCoverPreview(isEditCover: Boolean) {
        if(isEditCover) analytic.clickEditCover() else analytic.clickAddNewCover()
        openCoverSetupFragment()
    }

    /** Others */
    private fun showMainComponent(isShow: Boolean) {
        binding.groupPreparationMain.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    private fun getProperErrorMessage(err: Throwable): String {
        return ErrorHandler.getErrorMessage(context, err)
    }

    private fun handleBroadcastError(error: Throwable) {
        analytic.viewErrorOnFinalSetupPage(error.localizedMessage)
        if (error is BroadcasterException) {
            when(error.errorType) {
                BroadcasterErrorType.InternetUnavailable,
                BroadcasterErrorType.StreamFailed -> {
                    toaster.showError(error, getString(R.string.play_live_broadcast_connect_fail),
                        duration = Toaster.LENGTH_INDEFINITE,
                        actionLabel = getString(R.string.play_broadcast_try_again),
                        actionListener = {
                            broadcaster.start()
                        })
                }
                BroadcasterErrorType.AuthFailed,
                BroadcasterErrorType.UrlEmpty -> {
                    toaster.showError(error, getString(R.string.play_live_broadcast_connect_fail),
                        duration = Toaster.LENGTH_INDEFINITE,
                        actionLabel = getString(R.string.play_broadcast_try_again),
                        actionListener = {
                            viewModel.createLiveStream()
                        })
                }
                BroadcasterErrorType.ServiceNotReady,
                BroadcasterErrorType.StartFailed -> {
                    toaster.showError(error, getString(R.string.play_broadcaster_default_error),
                        duration = Toaster.LENGTH_INDEFINITE,
                        actionLabel = getString(R.string.play_broadcast_try_again),
                        actionListener = {
                            broadcaster.start()
                        })
                }
                else -> {
                    toaster.showError(error, getString(R.string.play_live_broadcast_unrecoverable_error),
                        duration = Toaster.LENGTH_INDEFINITE,
                        actionLabel = getString(R.string.play_ok),
                        actionListener = {
                            activity?.finish()
                        })
                }
            }
        }
    }

    private fun startCountDown() {
        showCountdown(true)

        val animationProperty = PlayTimerLiveCountDown.AnimationProperty.Builder()
            .setTextCountDownInterval(TIMER_TEXT_COUNTDOWN_INTERVAL)
            .setTotalCount(parentViewModel.getBeforeLiveCountDownDuration())
            .build()

        binding.playPreparationCountdownTimer.startCountDown(animationProperty, object : PlayTimerLiveCountDown.Listener {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                showCountdown(false)
                viewModel.createLiveStream()
            }

            override fun onCancelLiveStream() {
                analytic.clickCancelStreaming(parentViewModel.channelId, parentViewModel.channelTitle)

                showCountdown(false)
            }
        })
    }

    private fun showCountdown(isShow: Boolean) {
        if(isShow) {
            showMainComponent(false)
            binding.playPreparationCountdownTimer.visibility = View.VISIBLE
        }
        else {
            showMainComponent(true)
            binding.playPreparationCountdownTimer.visibility = View.GONE
        }
    }

    private fun openCoverSetupFragment() {
        val setupClass = PlayBroadcastSetupBottomSheet::class.java
        val fragmentFactory = childFragmentManager.fragmentFactory
        val setupFragment = fragmentFactory.instantiate(requireContext().classLoader, setupClass.name) as PlayBroadcastSetupBottomSheet
        setupFragment.show(childFragmentManager)
    }

    private fun openBroadcastLivePage() {
        broadcastCoordinator.navigateToFragment(PlayBroadcastUserInteractionFragment::class.java)
        analytic.openBroadcastScreen(parentViewModel.channelId)
    }

    private fun getEarlyLiveStreamDialog(): DialogUnify {
        if (!::earlyLiveStreamDialog.isInitialized) {
            earlyLiveStreamDialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setPrimaryCTAText(getString(R.string.play_broadcast_start_streaming_action))
                setPrimaryCTAClickListener {
                    analytic.clickStartLiveBeforeScheduleTime()
                    validateAndStartLive()
                    dismiss()
                }
                setSecondaryCTAText(getString(R.string.play_broadcast_cancel_streaming_action))
                setSecondaryCTAClickListener { dismiss() }
                setTitle(getString(R.string.play_broadcast_early_streaming_dialog_title))
                setDescription(getString(R.string.play_broadcast_early_streaming_dialog_desc))
            }
        }
        return earlyLiveStreamDialog
    }

    private fun showLoading(isShow: Boolean) {
        if(isShow) {
            if(!isLoadingDialogVisible())
                loadingDialogFragment.show(childFragmentManager)
        } else if(loadingDialogFragment.isAdded) {
            loadingDialogFragment.dismiss()
        }
    }

    private fun isLoadingDialogVisible(): Boolean {
        return loadingDialogFragment.isVisible
    }

    private fun startBroadcast(ingestUrl: String) {
        broadcaster.start(ingestUrl)
    }

    companion object {
        private const val TIMER_TEXT_COUNTDOWN_INTERVAL = 1000L
    }

    sealed interface Event {

        object ClickSetSchedule : Event
        data class SaveSchedule(val date: Date) : Event
        object DeleteSchedule : Event
        object CloseSetupSchedule : Event
    }
}