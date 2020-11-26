package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.SerializableHydraSetupData
import com.tokopedia.play.broadcaster.ui.model.LiveStreamInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.custom.PlayShareFollowerView
import com.tokopedia.play.broadcaster.view.custom.PlayStartStreamingButton
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.edit.CoverEditFragment
import com.tokopedia.play.broadcaster.view.fragment.edit.EditCoverTitleBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.edit.ProductEditFragment
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by jegul on 11/06/20
 */
class PlayBeforeLiveFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val analytic: PlayBroadcastAnalytic
) : PlayBaseBroadcastFragment() {

    private lateinit var ivImagePreview: ImageView
    private lateinit var tvCoverTitle: TextView
    private lateinit var llSelectedProduct: LinearLayout
    private lateinit var tvSelectedProduct: TextView
    private lateinit var btnStartLive: PlayStartStreamingButton
    private lateinit var followerView: PlayShareFollowerView
    private lateinit var ivShareLink: ImageView
    private lateinit var flEdit: FrameLayout

    private lateinit var prepareViewModel: PlayBroadcastPrepareViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var exitDialog: DialogUnify

    private var toasterBottomMargin = 0

    private val setupResultListener = object : SetupResultListener {
        override fun onSetupCanceled() {
        }

        override suspend fun onSetupCompletedWithData(bottomSheet: BottomSheetDialogFragment, dataStore: PlayBroadcastSetupDataStore): Throwable? {
            prepareViewModel.setDataFromSetupDataStore(dataStore)
            return parentViewModel.getChannelDetail()
        }
    }

    override fun getScreenName(): String = "Play Before Live Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastPrepareViewModel::class.java)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_before_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupInsets(view)

        if (savedInstanceState != null) populateSavedData(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeFollowers()
        observeCreateChannel()
        observeProductList()
        observeCover()
    }

    override fun onStart() {
        super.onStart()
        requireView().requestApplyInsetsWhenAttached()
        analytic.openFinalSetupPage()
    }

    override fun onBackPressed(): Boolean {
        showDialogWhenActionClose()
        return true
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is ProductEditFragment -> childFragment.setListener(setupResultListener)
            is EditCoverTitleBottomSheet -> childFragment.setListener(setupResultListener)
            is CoverEditFragment -> childFragment.setListener(setupResultListener)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SETUP_DATA, GsonSingleton.instance.toJson(parentViewModel.getHydraSetupData()))
    }

    private fun initView(view: View) {
        with(view) {
            ivImagePreview = findViewById(R.id.iv_image_preview)
            tvCoverTitle = findViewById(R.id.tv_cover_title)
            tvSelectedProduct = findViewById(R.id.tv_selected_product)
            llSelectedProduct = findViewById(R.id.ll_selected_product)
            btnStartLive = findViewById(R.id.btn_start_live)
            followerView = findViewById(R.id.follower_view)
            ivShareLink = findViewById(R.id.iv_share_link)
            flEdit = findViewById(R.id.fl_edit)
        }
    }

    private fun setupView(view: View) {
        broadcastCoordinator.setupTitle(getString(R.string.play_action_bar_prepare_final_title))
        btnStartLive.setOnClickListener {
            startStreaming()
            analytic.clickStartStreamingOnFinalSetupPage()
        }
        llSelectedProduct.setOnClickListener {
            openEditProductPage()
            analytic.clickEditProductTaggingOnFinalSetupPage()
        }
        tvCoverTitle.setOnClickListener {
            openEditCoverTitlePage()
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

            tvCoverTitle.text = it.title
        })
    }

    private fun observeCreateChannel() {
        prepareViewModel.observableCreateLiveStream.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkResult.Loading -> btnStartLive.setLoading(true)
                is NetworkResult.Success -> {
                    openBroadcastLivePage(it.data)
                    btnStartLive.setLoading(false)
                    parentViewModel.startCountDown()
                }
                is NetworkResult.Fail -> {
                    showToaster(
                            message = it.error.localizedMessage,
                            type = Toaster.TYPE_ERROR
                    )
                    btnStartLive.setLoading(false)
                    analytic.viewErrorOnFinalSetupPage(it.error.localizedMessage)
                }
            }
        })
    }
    //endregion

    private fun populateSavedData(savedInstanceState: Bundle) {
        val setupDataString = savedInstanceState.getString(KEY_SETUP_DATA)
        val setupData = GsonSingleton.instance.fromJson<SerializableHydraSetupData>(setupDataString, SerializableHydraSetupData::class.java)
        setupData?.let { parentViewModel.setHydraSetupData(setupData) }
    }

    private fun openBroadcastLivePage(liveStreamInfo: LiveStreamInfoUiModel) {
        broadcastCoordinator.navigateToFragment(PlayBroadcastUserInteractionFragment::class.java,
                Bundle().apply {
                    putString(PlayBroadcastUserInteractionFragment.KEY_INGEST_URL, liveStreamInfo.ingestUrl)
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

    private fun openEditCoverTitlePage() {
        getEditTitleBottomSheet().show(childFragmentManager)
    }

    private fun doCopyShareLink() {
        PlayShareWrapper.copyToClipboard(requireContext(), parentViewModel.shareContents) {
            showToaster(
                    message = getString(R.string.play_live_broadcast_share_link_copied),
                    actionLabel = getString(R.string.play_ok))
        }
    }

    private fun startStreaming() {
        prepareViewModel.createLiveStream()
    }

    private fun getExitDialog(): DialogUnify {
        if (!::exitDialog.isInitialized) {
            exitDialog = requireContext().getDialog(
                    actionType = DialogUnify.HORIZONTAL_ACTION,
                    title = getString(R.string.play_prepare_broadcast_dialog_end_title),
                    desc = getString(R.string.play_prepare_broadcast_dialog_end_desc),
                    primaryCta = getString(R.string.play_prepare_broadcast_dialog_end_primary),
                    primaryListener = { dialog -> dialog.dismiss() },
                    secondaryCta = getString(R.string.play_broadcast_exit),
                    secondaryListener = { _ ->
                        analytic.clickExitOnDialogFinalSetupPage()
                        activity?.finish()
                    }
            )
        }
        return exitDialog
    }

    private fun showDialogWhenActionClose() {
        getExitDialog().show()
        analytic.viewExitDialogOnFinalSetupPage()
    }

    private fun showToaster(
            message: String,
            type: Int = Toaster.TYPE_NORMAL,
            actionLabel: String = ""
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
                bottomMargin = toasterBottomMargin
        )
    }

    private fun getEditTitleBottomSheet(): EditCoverTitleBottomSheet {
        val editTitleBottomSheet = EditCoverTitleBottomSheet()
        editTitleBottomSheet.setShowListener { editTitleBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        return editTitleBottomSheet
    }

    companion object {

        private const val KEY_SETUP_DATA = "setup_data"
        private const val TAG_COVER_EDIT = "cover_edit"
        private const val TAG_PRODUCT_EDIT = "product_edit"
    }
}