package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroadcastPreparationBinding
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleFormState
import com.tokopedia.play.broadcaster.ui.state.PlayTitleFormUiState
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherErrorType
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlayTimerLiveCountDown
import com.tokopedia.play.broadcaster.view.custom.actionbar.ActionBarView
import com.tokopedia.play.broadcaster.view.custom.preparation.CoverFormView
import com.tokopedia.play.broadcaster.view.custom.preparation.PreparationMenuView
import com.tokopedia.play.broadcaster.view.custom.preparation.TitleFormView
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.PlayLiveViewState
import com.tokopedia.play.broadcaster.view.viewmodel.*
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.detachableview.FragmentViewContainer
import com.tokopedia.play_common.detachableview.FragmentWithDetachableView
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.extension.hideKeyboard
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_play_broadcast_preparation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on January 24, 2022
 */
class PlayBroadcastPreparationFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private val analytic: PlayBroadcastAnalytic,
) : PlayBaseBroadcastFragment(), FragmentWithDetachableView,
    ActionBarView.Listener,
    PreparationMenuView.Listener,
    TitleFormView.Listener,
    CoverFormView.Listener {

    /** ViewModel */
    private lateinit var viewModel: PlayBroadcastPrepareViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var titleSetupViewModel: PlayTitleAndTagsSetupViewModel
    private lateinit var coverSetupViewModel: PlayCoverSetupViewModel
    private lateinit var scheduleViewModel: BroadcastScheduleViewModel

    /** View */
    private var _binding: FragmentPlayBroadcastPreparationBinding? = null
    private val binding get() = _binding!!

    /** Others */
    private val fragmentViewContainer = FragmentViewContainer()

    override fun getScreenName(): String = "Play Prepare Page"

    override fun getViewContainer(): FragmentViewContainer = fragmentViewContainer

    /** Lifecycle */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastPrepareViewModel::class.java)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        titleSetupViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayTitleAndTagsSetupViewModel::class.java)
        coverSetupViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayCoverSetupViewModel::class.java)
        scheduleViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(BroadcastScheduleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBroadcastPreparationBinding.inflate(
            LayoutInflater.from(requireContext()),
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupInsets(view)
        setupListener()
        setupObserver()

        if(parentViewModel.channelTitle.isEmpty()) showTitleForm(true)
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

    /** Setup */
    private fun setupView() {
        binding.viewActionBar.setShopName(parentViewModel.getShopName())
        binding.viewActionBar.setShopIcon(parentViewModel.getShopIconUrl())
        binding.formTitle.setMaxCharacter(titleSetupViewModel.maxTitleChars)
    }

    private fun setupInsets(view: View) {
        view.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop, bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
    }

    private fun setupListener() {
        binding.apply {
            viewActionBar.setListener(this@PlayBroadcastPreparationFragment)
            viewPreparationMenu.setListener(this@PlayBroadcastPreparationFragment)
            formTitle.setListener(this@PlayBroadcastPreparationFragment)
            formCover.setListener(this@PlayBroadcastPreparationFragment)

            flBroStartLivestream.setOnClickListener {
                analytic.clickStartStreamingOnFinalSetupPage()

                /** TODO: comment this first because we havent revamped the schedule functionality yet */
//                val schedule = scheduleViewModel.schedule
//                if (schedule is BroadcastScheduleUiModel.Scheduled) {
//                    val currentTime = Date()
//                    if (currentTime.before(schedule.time)) {
//                        getEarlyLiveStreamDialog().show()
//                        analytic.viewDialogConfirmStartLiveBeforeScheduledOnFinalSetupPage()
//                        return@setOnClickListener
//                    }
//                }

                startCountDown()
            }

            icBroPreparationSwitchCamera.setOnClickListener {
                parentViewModel.switchCamera()
                analytic.clickSwitchCameraOnSetupPage()
            }
        }
    }

    private fun setupObserver() {
        observeTitle()
        observeCover()
        observeCreateLiveStream()
        observeLiveStreamState()
    }

    private fun observeTitle() {
        parentViewModel.observableTitle.observe(viewLifecycleOwner) {
            binding.viewPreparationMenu.isSetTitleChecked(true)
        }

        titleSetupViewModel.observableUploadEvent.observe(viewLifecycleOwner) {
            when (val content = it.peekContent()) {
                is NetworkResult.Fail -> {
                    binding.formTitle.setLoading(false)
                    showErrorToaster(content.error)
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
                    binding.viewPreparationMenu.isSetCoverChecked(true)

                    if(croppedCover.coverImage.toString().isNotEmpty() &&
                        croppedCover.coverImage.toString().contains("http")) {
                        binding.formCover.setCover(croppedCover.coverImage.toString())
                    }
                }
            }
        }

        coverSetupViewModel.observableUploadCoverEvent.observe(viewLifecycleOwner) {
            when(it) {
                /** TODO: handle if error upload cover */
                is NetworkResult.Success -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        parentViewModel.getChannelDetail()
                    }
                }
            }
        }
    }

    private fun observeCreateLiveStream() {
        viewModel.observableCreateLiveStream.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> parentViewModel.startLiveStream(withTimer = false)
                is NetworkResult.Fail -> {
                    showCountdown(false)
                    showErrorToaster(it.error)
                    analytic.viewErrorOnFinalSetupPage(getProperErrorMessage(it.error))
                }
            }
        }
    }

    private fun observeLiveStreamState(){
        parentViewModel.observableLiveViewState.observe(viewLifecycleOwner) {
            if (!isVisible) return@observe

            when (it) {
                is PlayLiveViewState.Started -> {
                    openBroadcastLivePage()
                    parentViewModel.setFirstTimeLiveStreaming()
                }
                is PlayLiveViewState.Error -> {
                    showCountdown(false)
                    handleLivePushError(it)
                }
            }
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

            /** TODO: set cover if exists */
            binding.formCover.setTitle(parentViewModel.channelTitle)
            binding.formCover.setShopName(parentViewModel.getShopName())
            binding.formCover.visibility = View.VISIBLE
        }
        else {
            showMainComponent(true)

            binding.formCover.visibility = View.GONE
        }
    }

    /** Callback Action Bar */
    override fun onClickClosePreparation() {
        analytic.clickCloseOnSetupPage()
        activity?.onBackPressed()
    }

    /** Callback Preparation Menu */
    override fun onClickSetTitle() {
        showTitleForm(true)
    }

    override fun onClickSetCover() {
        showCoverForm(true)
    }

    override fun onClickSetProduct() {
        TODO("Not yet implemented")
    }

    /** Callback Title Form */
    override fun onCloseTitleForm(view: TitleFormView) {
        hideKeyboard()
        activity?.onBackPressed()
    }

    override fun onTitleSaved(view: TitleFormView, title: String) {
        hideKeyboard()
        binding.formTitle.setLoading(true)
        titleSetupViewModel.uploadTitle(title)
    }

    /** Callback Cover Form */
    override fun onCloseCoverForm() {
        activity?.onBackPressed()
    }

    override fun onClickCoverPreview() {
        openCoverSetupFragment()
    }

    /** Others */
    private fun showMainComponent(isShow: Boolean) {
        binding.groupPreparationMain.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    private fun getProperErrorMessage(err: Throwable): String {
        return ErrorHandler.getErrorMessage(context, err)
    }

    private fun showErrorToaster(
        err: Throwable,
        customErrMessage: String? = null,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener {  }
    ) {
        val errMessage = if (customErrMessage == null) {
            ErrorHandler.getErrorMessage(
                context, err, ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
        } else {
            val (_, errCode) = ErrorHandler.getErrorMessagePair(
                context, err, ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            getString(
                com.tokopedia.play_common.R.string.play_custom_error_handler_msg,
                customErrMessage,
                errCode
            )
        }
        showToaster(errMessage, Toaster.TYPE_ERROR, actionLabel, actionListener)
    }

    private fun showToaster(
        message: String,
        type: Int = Toaster.TYPE_NORMAL,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener {  }
    ) {
        view?.showToaster(
            message = message,
            actionLabel = actionLabel,
            type = type,
            actionListener = actionListener
        )
    }

    private fun handleLivePushError(state: PlayLiveViewState.Error) {
        when(state.error.type) {
            PlayLivePusherErrorType.ConnectFailed -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_live_broadcast_connect_fail),
                actionLabel = getString(R.string.play_broadcast_try_again),
                actionListener = {
                    showCountdown(true)
                    parentViewModel.reconnectLiveStream()
                }
            )
            PlayLivePusherErrorType.SystemError -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_dialog_unsupported_device_desc),
                actionLabel = getString(R.string.play_ok),
                actionListener = { parentViewModel.stopLiveStream(shouldNavigate = true) }
            )
            PlayLivePusherErrorType.NetworkLoss,
            PlayLivePusherErrorType.NetworkPoor -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_bro_error_network_problem),
                actionLabel = getString(R.string.play_ok)
            )
            else -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_broadcaster_default_error),
                actionLabel = getString(R.string.play_ok)
            )
        }
        analytic.viewErrorOnFinalSetupPage(state.error.reason)
        if (GlobalConfig.DEBUG) {
            Toast.makeText(
                requireContext(),
                "reason: ${state.error.reason} \n\n(Important! this message only appears in debug mode)",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startCountDown() {
        showCountdown(true)

        val animationProperty = PlayTimerLiveCountDown.AnimationProperty.Builder()
            .setTextCountDownInterval(TIMER_TEXT_COUNTDOWN_INTERVAL)
            .setTotalCount(parentViewModel.getBeforeLiveCountDownDuration())
            .build()

        binding.playPreparationCountdownTimer.startCountDown(animationProperty, object : PlayTimerLiveCountDown.Listener {
            override fun onTick(milisUntilFinished: Long) {}

            override fun onFinish() {
                viewModel.createLiveStream()
            }

            override fun onCancelLiveStream() {
                showCountdown(false)
                analytic.clickCancelOnCountDown(parentViewModel.channelId, parentViewModel.channelTitle)
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

    companion object {
        private const val TIMER_TEXT_COUNTDOWN_INTERVAL = 1000L
    }
}