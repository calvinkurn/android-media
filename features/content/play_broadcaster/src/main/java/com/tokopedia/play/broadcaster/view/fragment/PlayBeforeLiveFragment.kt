package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.LiveStreamInfoUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.view.custom.PlayShareFollowerView
import com.tokopedia.play.broadcaster.view.custom.PlayStartStreamingButton
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSetupViewModel
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

    private lateinit var setupViewModel: PlayBroadcastSetupViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var exitDialog: DialogUnify

    override fun getScreenName(): String = "Play Before Live Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastSetupViewModel::class.java)
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
        }
    }

    private fun setupView(view: View) {
        broadcastCoordinator.setupTitle(getString(R.string.play_action_bar_prepare_final_title))
        btnStartLive.setOnClickListener { startStreaming() }
        llSelectedProduct.setOnClickListener { openEditProductPage() }
        tvCoverTitle.setOnClickListener { openEditCoverPage() }

        btnStartLive.setMaxStreamingDuration(30)
    }

    //region observe
    /**
     * Observe
     */
    private fun observeFollowers() {
        setupViewModel.observableFollowers.observe(viewLifecycleOwner, Observer {
            followerView.setFollowersModel(it)
        })
    }

    private fun observeSetupChannel() {
        setupViewModel.observableSetupChannel.observe(viewLifecycleOwner, Observer {
            tvSelectedProduct.text = getString(R.string.play_before_live_selected_product, it.selectedProductList.size)
            ivImagePreview.loadImageRounded(it.cover.url)
            tvCoverTitle.text = it.cover.title
        })
    }

    private fun observeCreateChannel() {
        setupViewModel.observableCreateChannel.observe(viewLifecycleOwner, Observer {
            when(it) {
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
                    putString(PlayBroadcastUserInteractionFragment.KEY_CHANNEL_ID, liveStreamInfo.channelId)
                    putString(PlayBroadcastUserInteractionFragment.KEY_INGEST_URL, liveStreamInfo.ingestUrl)
                })
    }

    private fun openEditProductPage() {

    }

    private fun openEditCoverPage() {

    }

    private fun showToaster(message: String, toasterType: Int) {
        Toaster.make(requireView(),
                text = message,
                type = toasterType)
    }

    private fun startStreaming() {
        setupViewModel.createChannel()
    }

    private fun getExitDialog(): DialogUnify {
        if (!::exitDialog.isInitialized) {
            exitDialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.play_prepare_broadcast_dialog_end_title))
                setDescription(getString(R.string.play_prepare_broadcast_dialog_end_desc))
                setPrimaryCTAText(getString(R.string.play_prepare_broadcast_dialog_end_primary))
                setSecondaryCTAText(getString(R.string.play_prepare_broadcast_dialog_end_secondary))
                setPrimaryCTAClickListener { this.dismiss() }
                setSecondaryCTAClickListener {
                    activity?.finish()
                }
            }
        }
        return exitDialog
    }

    private fun showDialogWhenActionClose() {
        getExitDialog().show()
    }
}