package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.LiveStreamInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.PlayShareWrapper
import com.tokopedia.play.broadcaster.util.getDialog
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastEditTitleBottomSheet
import com.tokopedia.play.broadcaster.view.contract.SetupResultListener
import com.tokopedia.play.broadcaster.view.custom.PlayShareFollowerView
import com.tokopedia.play.broadcaster.view.custom.PlayStartStreamingButton
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.edit.CoverEditFragment
import com.tokopedia.play.broadcaster.view.fragment.edit.ProductEditFragment
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by jegul on 11/06/20
 */
class PlayBeforeLiveFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
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

    private lateinit var editTitleBottomSheet: PlayBroadcastEditTitleBottomSheet

    private lateinit var exitDialog: DialogUnify

    private val setupResultListener = object : SetupResultListener {
        override fun onSetupCanceled() {
        }

        override fun onSetupCompletedWithData(dataStore: PlayBroadcastSetupDataStore) {
            prepareViewModel.setDataFromSetupDataStore(dataStore)
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeFollowers()
        observeSetupChannel()
        observeCreateChannel()
    }

    override fun onBackPressed(): Boolean {
        showDialogWhenActionClose()
        return true
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
        btnStartLive.setOnClickListener { startStreaming() }
        llSelectedProduct.setOnClickListener { openEditProductPage() }
        tvCoverTitle.setOnClickListener { openEditCoverTitlePage() }
        ivImagePreview.setOnClickListener { openEditCoverImagePage() }

        btnStartLive.setMaxStreamingDuration(30)
        ivShareLink.setOnClickListener { doCopyShareLink() }
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

    private fun observeSetupChannel() {
        prepareViewModel.observableSetupChannel.observe(viewLifecycleOwner, Observer {
            tvSelectedProduct.text = getString(R.string.play_before_live_selected_product, it.selectedProductList.size)
            when (val croppedCover = it.cover.croppedCover) {
                is CoverSetupState.Cropped -> ivImagePreview.loadImageRounded(croppedCover.coverImage.toString())
                is CoverSetupState.Cropping.Image -> ivImagePreview.loadImageRounded(croppedCover.coverImage.toString())
                else -> ivImagePreview.setImageDrawable(null)
            }

            tvCoverTitle.text = it.cover.title
        })
    }

    private fun observeCreateChannel() {
        prepareViewModel.observableCreateLiveStream.observe(viewLifecycleOwner, Observer {
            when (it) {
                NetworkResult.Loading -> btnStartLive.setLoading(true)
                is NetworkResult.Success -> {
                    openBroadcastLivePage(it.data)
                    btnStartLive.setLoading(false)
                }
                is NetworkResult.Fail -> {
                    showToaster(it.error.localizedMessage, Toaster.TYPE_ERROR)
                    btnStartLive.setLoading(false)
                }
            }
        })
    }
    //endregion

    private fun openBroadcastLivePage(liveStreamInfo: LiveStreamInfoUiModel) {
        broadcastCoordinator.navigateToFragment(PlayBroadcastUserInteractionFragment::class.java,
                Bundle().apply {
                    putString(PlayBroadcastUserInteractionFragment.KEY_INGEST_URL, liveStreamInfo.ingestUrl)
                })
    }

    private fun openEditCoverImagePage() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val editCoverFragment = fragmentFactory.instantiate(requireContext().classLoader, CoverEditFragment::class.java.name) as CoverEditFragment
        editCoverFragment.setListener(setupResultListener)
        childFragmentManager.beginTransaction()
                .replace(flEdit.id, editCoverFragment)
                .commit()
    }

    private fun openEditProductPage() {
        val fragmentFactory = childFragmentManager.fragmentFactory
        val editProductFragment = fragmentFactory.instantiate(requireContext().classLoader, ProductEditFragment::class.java.name) as ProductEditFragment
        editProductFragment.setListener(setupResultListener)
        childFragmentManager.beginTransaction()
                .replace(flEdit.id, editProductFragment)
                .commit()
    }

    private fun openEditCoverTitlePage() {
        getEditTitleBottomSheet().apply {
            setCoverTitle(prepareViewModel.title)
        }.show(childFragmentManager)
    }

    private fun doCopyShareLink() {
        parentViewModel.shareInfo?.let { shareInfo ->
            PlayShareWrapper.doCopyShareLink(requireContext(), shareInfo) {
                showToaster(message = getString(R.string.play_live_broadcast_share_link_copied),
                        toasterType = Toaster.TYPE_NORMAL,
                        actionLabel = getString(R.string.play_ok))
            }
        }
    }

    private fun showToaster(message: String, toasterType: Int, actionLabel: String = "") {
        Toaster.make(requireView(),
                text = message,
                type = toasterType,
                actionText = actionLabel)
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
                    secondaryListener = { dialog -> activity?.finish() }
            )
        }
        return exitDialog
    }

    private fun showDialogWhenActionClose() {
        getExitDialog().show()
    }

    private fun getEditTitleBottomSheet(): PlayBroadcastEditTitleBottomSheet {
        if (!::editTitleBottomSheet.isInitialized) {
            editTitleBottomSheet = PlayBroadcastEditTitleBottomSheet()
            editTitleBottomSheet.setListener(object : PlayBroadcastEditTitleBottomSheet.Listener {
                override fun onSaveEditedTitle(title: String) {
                    prepareViewModel.title = title
                }
            })
            editTitleBottomSheet.setShowListener { editTitleBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        }
        return editTitleBottomSheet
    }
}