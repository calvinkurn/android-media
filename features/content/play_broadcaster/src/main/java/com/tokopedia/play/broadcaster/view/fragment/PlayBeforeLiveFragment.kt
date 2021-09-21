package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.SerializableHydraSetupData
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherErrorType
import com.tokopedia.play.broadcaster.util.extension.setLoading
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.custom.PlayShareFollowerView
import com.tokopedia.play.broadcaster.view.custom.PlayStartStreamingButton
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.edit.CoverEditFragment
import com.tokopedia.play.broadcaster.view.fragment.edit.ProductEditFragment
import com.tokopedia.play.broadcaster.view.fragment.edit.SetupBroadcastScheduleBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.edit.TitleAndTagsEditBottomSheet
import com.tokopedia.play.broadcaster.view.partial.ActionBarViewComponent
import com.tokopedia.play.broadcaster.view.partial.BroadcastScheduleViewComponent
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.PlayLiveViewState
import com.tokopedia.play.broadcaster.view.viewmodel.BroadcastScheduleViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 11/06/20
 */
class PlayBeforeLiveFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val dispatcher: CoroutineDispatchers,
        private val analytic: PlayBroadcastAnalytic
) : PlayBaseBroadcastFragment() {

    private lateinit var ivImagePreview: ImageView
    private lateinit var tvChannelTitle: TextView
    private lateinit var llSelectedProduct: LinearLayout
    private lateinit var tvSelectedProduct: TextView
    private lateinit var btnStartLive: PlayStartStreamingButton
    private lateinit var followerView: PlayShareFollowerView
    private lateinit var ivShareLink: ImageView
    private lateinit var flEdit: FrameLayout

    private val actionBarView by viewComponent {
        ActionBarViewComponent(it, object : ActionBarViewComponent.Listener {
            override fun onCameraIconClicked() {
                parentViewModel.switchCamera()
                analytic.clickSwitchCameraOnFinalSetupPage()
            }

            override fun onCloseIconClicked() {
                activity?.onBackPressed()
            }
        })
    }

    private val broadcastScheduleView by viewComponent {
        BroadcastScheduleViewComponent(it, R.id.view_play_broadcast_schedule, object : BroadcastScheduleViewComponent.Listener {
            override fun onAddBroadcastSchedule(view: BroadcastScheduleViewComponent) {
                analytic.clickAddEditScheduleOnFinalSetupPage(isEdit = false)
                openSetupBroadcastSchedulePage()
            }

            override fun onEditBroadcastSchedule(view: BroadcastScheduleViewComponent) {
                analytic.clickAddEditScheduleOnFinalSetupPage(isEdit = true)
                openSetupBroadcastSchedulePage()
            }

            override fun onDeleteBroadcastSchedule(view: BroadcastScheduleViewComponent) {
                analytic.clickDeleteScheduleOnFinalSetupPage()
                getDeleteScheduleDialog()
                        .show()
                analytic.viewDialogConfirmDeleteOnFinalSetupPage()
            }
        })
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.immediate)

    private lateinit var earlyLiveStreamDialog: DialogUnify
    private lateinit var deleteScheduleDialog: DialogUnify

    private lateinit var prepareViewModel: PlayBroadcastPrepareViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel
    private lateinit var scheduleViewModel: BroadcastScheduleViewModel

    private var toasterBottomMargin = 0

    private val setupResultListener = object : SetupResultListener {
        override fun onSetupCanceled() {
        }

        override suspend fun onSetupCompletedWithData(bottomSheet: BottomSheetDialogFragment, dataStore: PlayBroadcastSetupDataStore): Throwable? {
            prepareViewModel.setDataFromSetupDataStore(dataStore)

            if (bottomSheet is SetupBroadcastScheduleBottomSheet) {
                showToaster(
                        message = getString(R.string.play_broadcast_schedule_set_success),
                        type = Toaster.TYPE_NORMAL
                )
            }

            return parentViewModel.getChannelDetail()
        }
    }

    private var isDeleting: Boolean = false

    override fun getScreenName(): String = "Play Before Live Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastPrepareViewModel::class.java)
        parentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
        scheduleViewModel = ViewModelProvider(this, viewModelFactory).get(BroadcastScheduleViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_before_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupObserve()
        setupInsets(view)

        if (savedInstanceState != null) populateSavedData(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        requireView().requestApplyInsetsWhenAttached()
        analytic.openFinalSetupPage()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductEditFragment -> childFragment.setListener(setupResultListener)
            is CoverEditFragment -> childFragment.setListener(setupResultListener)
            is SetupBroadcastScheduleBottomSheet -> childFragment.setListener(setupResultListener)
            is TitleAndTagsEditBottomSheet -> childFragment.setListener(setupResultListener)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SETUP_DATA, GsonSingleton.instance.toJson(parentViewModel.getHydraSetupData()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

    private fun initView(view: View) {
        with(view) {
            ivImagePreview = findViewById(R.id.iv_image_preview)
            tvChannelTitle = findViewById(R.id.tv_channel_title)
            tvSelectedProduct = findViewById(R.id.tv_selected_product)
            llSelectedProduct = findViewById(R.id.ll_selected_product)
            btnStartLive = findViewById(R.id.btn_start_live)
            followerView = findViewById(R.id.follower_view)
            ivShareLink = findViewById(R.id.iv_share_link)
            flEdit = findViewById(R.id.fl_edit)
        }
    }

    private fun setupView(view: View) {
        actionBarView.setTitle(getString(R.string.play_action_bar_prepare_final_title))
        btnStartLive.setOnClickListener {
            startStreaming()
        }
        llSelectedProduct.setOnClickListener {
            openEditProductPage()
            analytic.clickEditProductTaggingOnFinalSetupPage()
        }
        tvChannelTitle.setOnClickListener {
            openEditTitleAndTagsPage()
            analytic.clickEditTitleOnFinalSetupPage()
        }
        ivImagePreview.setOnClickListener {
            openEditCoverImagePage()
            analytic.clickEditCoverOnFinalSetupPage()
        }

        btnStartLive.setMaxDurationDescription(prepareViewModel.maxDurationDesc)
        ivShareLink.setOnClickListener {
            doCopyShareLink()
            analytic.clickShareIconOnFinalSetupPage()
        }
    }

    private fun setupObserve() {
        observeFollowers()
        observeCreateChannel()
        observeProductList()
        observeCover()
        observeTitle()
        observeLiveInfo()
        observeBroadcastSchedule()
        observeDeleteBroadcastSchedule()
        observeChannelInfo()
    }

    private fun setupInsets(view: View) {
        view.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop, bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
    }

    //region observe
    /**
     * Observe
     */
    private fun observeFollowers() {
        prepareViewModel.observableFollowers.observe(viewLifecycleOwner, Observer {
            followerView.setFollowersModel(it)
        })
    }

    private fun observeProductList() {
        parentViewModel.observableProductList.observe(viewLifecycleOwner, Observer {
            tvSelectedProduct.text = getString(R.string.play_before_live_selected_product, it.size)
        })
    }

    private fun observeCover() {
        parentViewModel.observableCover.observe(viewLifecycleOwner, Observer {
            when (val croppedCover = it.croppedCover) {
                is CoverSetupState.Cropped -> ivImagePreview.loadImageRounded(croppedCover.coverImage.toString())
                is CoverSetupState.Cropping.Image -> ivImagePreview.loadImageRounded(croppedCover.coverImage.toString())
                else -> ivImagePreview.setImageDrawable(null)
            }
        })
    }

    private fun observeTitle() {
        parentViewModel.observableTitle.observe(viewLifecycleOwner, Observer {
            tvChannelTitle.text = it.title
        })
    }

    private fun observeCreateChannel() {
        prepareViewModel.observableCreateLiveStream.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkResult.Loading -> btnStartLive.setLoading(true)
                is NetworkResult.Success -> parentViewModel.startLiveStream(withTimer = false)
                is NetworkResult.Fail -> {
                    showErrorToaster(it.error)
                    btnStartLive.setLoading(false)
                    analytic.viewErrorOnFinalSetupPage(getProperErrorMessage(it.error))
                }
            }
        })
    }

    private fun observeLiveInfo() {
        parentViewModel.observableLiveViewState.observe(viewLifecycleOwner, Observer(::handleLiveInfoState))
    }

    private fun observeBroadcastSchedule() {
        parentViewModel.observableBroadcastSchedule.observe(viewLifecycleOwner, Observer {
            broadcastScheduleView.setSchedule(it)
        })
    }

    private fun observeDeleteBroadcastSchedule() {
        scheduleViewModel.observableDeleteBroadcastSchedule.observe(viewLifecycleOwner) {
            when (it) {
                NetworkResult.Loading -> {
                    setDeleteScheduleDialogLoading(isLoading = true)
                }
                is NetworkResult.Fail -> {
                    getDeleteScheduleDialog().dismiss()
                    showErrorToaster(it.error)
                }
                is NetworkResult.Success -> {
                    scope.launch {
                        isDeleting = true
                        parentViewModel.getChannelDetail()
                    }
                }
            }
        }
    }

    private fun observeChannelInfo() {
        parentViewModel.observableChannelInfo.observe(viewLifecycleOwner) {
            setDeleteScheduleDialogLoading(isLoading = false)

            if (isDeleting && it != NetworkResult.Loading) {
                getDeleteScheduleDialog()
                        .dismiss()

                if (it is NetworkResult.Fail) {
                    showErrorToaster(it.error)
                } else if (it is NetworkResult.Success) {
                    showToaster(
                            message = getString(R.string.play_broadcast_schedule_deleted)
                    )
                }

                isDeleting = false
            }

        }
    }
    //endregion

    private fun populateSavedData(savedInstanceState: Bundle) {
        val setupDataString = savedInstanceState.getString(KEY_SETUP_DATA)
        val setupData = GsonSingleton.instance.fromJson<SerializableHydraSetupData>(setupDataString, SerializableHydraSetupData::class.java)
        setupData?.let { parentViewModel.setHydraSetupData(setupData) }
    }

    private fun handleLiveInfoState(state: PlayLiveViewState) {
        if (!isVisible) return
        when (state) {
            is PlayLiveViewState.Started -> {
                openBroadcastLivePage()
                btnStartLive.setLoading(false)
                parentViewModel.setFirstTimeLiveStreaming()
            }
            is PlayLiveViewState.Error -> {
                btnStartLive.setLoading(false)
                handleLivePushError(state)
            }
        }
    }

    private fun handleLivePushError(state: PlayLiveViewState.Error) {
        when(state.error.type) {
            PlayLivePusherErrorType.ConnectFailed -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_live_broadcast_connect_fail),
                actionLabel = getString(R.string.play_broadcast_try_again),
                actionListener = { parentViewModel.reconnectLiveStream() }
            )
            PlayLivePusherErrorType.SystemError -> showErrorToaster(
                err = state.error,
                customErrMessage = getString(R.string.play_dialog_unsupported_device_desc),
                actionLabel = getString(R.string.play_ok),
                actionListener = { parentViewModel.stopLiveStream(shouldNavigate = true) }
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

    private fun openBroadcastLivePage() {
        broadcastCoordinator.navigateToFragment(PlayBroadcastUserInteractionFragment::class.java,
                Bundle().apply {
                    putBoolean(PlayBroadcastUserInteractionFragment.KEY_START_COUNTDOWN, true)
                })
        analytic.openBroadcastScreen(parentViewModel.channelId)
    }

    private fun openEditCoverImagePage() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val editCoverFragment = fragmentFactory.instantiate(requireContext().classLoader, CoverEditFragment::class.java.name) as CoverEditFragment
        childFragmentManager.beginTransaction()
                .replace(flEdit.id, editCoverFragment, TAG_COVER_EDIT)
                .commit()
    }

    private fun openEditProductPage() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val editProductFragment = fragmentFactory.instantiate(requireContext().classLoader, ProductEditFragment::class.java.name) as ProductEditFragment
        childFragmentManager.beginTransaction()
                .replace(flEdit.id, editProductFragment, TAG_PRODUCT_EDIT)
                .commit()
    }

    private fun openEditTitleAndTagsPage() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val editTitleAndTagsBottomSheet = fragmentFactory.instantiate(requireContext().classLoader, TitleAndTagsEditBottomSheet::class.java.name) as TitleAndTagsEditBottomSheet
        editTitleAndTagsBottomSheet.show(childFragmentManager, TAG_TITLE_AND_TAGS_EDIT)
    }

    private fun openSetupBroadcastSchedulePage() {
        getSetupBroadcastScheduleBottomSheet().show(childFragmentManager)
        analytic.viewBottomSheetScheduleOnFinalSetupPage()
    }

    private fun doCopyShareLink() {
        PlayShareWrapper.copyToClipboard(requireContext(), parentViewModel.shareContents) {
            showToaster(
                    message = getString(R.string.play_live_broadcast_share_link_copied),
                    actionLabel = getString(R.string.play_ok))
        }
    }

    private fun startStreaming() {
        val schedule = scheduleViewModel.schedule
        if (schedule is BroadcastScheduleUiModel.Scheduled) {
            val currentTime = Date()
            if (currentTime.before(schedule.time)) {
                getEarlyLiveStreamDialog().show()
                analytic.viewDialogConfirmStartLiveBeforeScheduledOnFinalSetupPage()
                return
            }
        }

        createLiveStream()
    }

    private fun getEarlyLiveStreamDialog(): DialogUnify {
        if (!::earlyLiveStreamDialog.isInitialized) {
            earlyLiveStreamDialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setPrimaryCTAText(getString(R.string.play_broadcast_start_streaming_action))
                setPrimaryCTAClickListener {
                    analytic.clickStartLiveOnBeforeScheduledDialog()
                    createLiveStream()
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

    private fun getDeleteScheduleDialog(): DialogUnify {
        if (!::deleteScheduleDialog.isInitialized) {
            deleteScheduleDialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setPrimaryCTAText(getString(R.string.play_broadcast_delete_schedule_action))
                setSecondaryCTAText(getString(R.string.play_broadcast_exit))
                setTitle(getString(R.string.play_broadcast_delete_schedule_dialog_title))
                setDescription(getString(R.string.play_broadcast_delete_schedule_dialog_desc))

                setOnShowListener {
                    setDeleteScheduleDialogLoading(false)
                }
                dialogSecondaryCTA.buttonVariant = UnifyButton.Variant.TEXT_ONLY
            }
        }

        return deleteScheduleDialog
    }

    private fun setDeleteScheduleDialogLoading(isLoading: Boolean) {
        getDeleteScheduleDialog().apply {
            setLoading(isLoading)

            setPrimaryCTAClickListener {
                if (!isLoading) deleteBroadcastSchedule()
            }
            setSecondaryCTAClickListener {
                if (!isLoading) dismiss()
            }
        }
    }

    private fun createLiveStream() {
        prepareViewModel.createLiveStream()
        analytic.clickStartStreamingOnFinalSetupPage()
    }

    private fun deleteBroadcastSchedule() {
        scheduleViewModel.deleteBroadcastSchedule()
        analytic.clickDeleteScheduleOnConfirmDeleteDialog()
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
            "$customErrMessage. Kode Error: (${errCode})"
        }
        showToaster(errMessage, Toaster.TYPE_ERROR, actionLabel, actionListener)
    }

    private fun showToaster(
            message: String,
            type: Int = Toaster.TYPE_NORMAL,
            actionLabel: String = "",
            actionListener: View.OnClickListener = View.OnClickListener {  }
    ) {
        if (toasterBottomMargin == 0) {
            val offset8 = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
            val marginParams = btnStartLive.layoutParams as ViewGroup.MarginLayoutParams
            toasterBottomMargin = btnStartLive.height + marginParams.bottomMargin + offset8
        }

        view?.showToaster(
                message = message,
                actionLabel = actionLabel,
                type = type,
                bottomMargin = toasterBottomMargin,
                actionListener = actionListener
        )
    }

    private fun getSetupBroadcastScheduleBottomSheet(): SetupBroadcastScheduleBottomSheet {
        val bottomSheet = SetupBroadcastScheduleBottomSheet()
        bottomSheet.setShowListener { bottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        return bottomSheet
    }

    companion object {

        private const val KEY_SETUP_DATA = "setup_data"
        private const val TAG_COVER_EDIT = "cover_edit"
        private const val TAG_PRODUCT_EDIT = "product_edit"
        private const val TAG_TITLE_AND_TAGS_EDIT = "title_and_tags_edit"
    }
}