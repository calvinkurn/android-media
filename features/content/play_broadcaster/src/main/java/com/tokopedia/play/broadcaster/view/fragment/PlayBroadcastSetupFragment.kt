package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayFollowerItemDecoration
import com.tokopedia.play.broadcaster.ui.model.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.util.doOnPreDraw
import com.tokopedia.play.broadcaster.view.adapter.PlayFollowersAdapter
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSetupViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayBroadcastSetupFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val fragmentFactory: FragmentFactory
) : PlayBaseBroadcastFragment() {

    private lateinit var viewModel: PlayBroadcastSetupViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var btnSetup: UnifyButton
    private lateinit var rvFollowers: RecyclerView

    private val followersAdapter = PlayFollowersAdapter()

    override fun getScreenName(): String = "Play Prepare Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastSetupViewModel::class.java)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_prepare_broadcast, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeFollowers()
        observeCreateChannel()
    }

    private fun initView(view: View) {
        with (view) {
            btnSetup = findViewById(R.id.btn_setup)
            rvFollowers = findViewById(R.id.rv_followers)
        }
    }

    private fun setupView(view: View) {
        broadcastCoordinator.setupTitle(getString(R.string.play_action_bar_prepare_title))
        btnSetup.setOnClickListener {
//             openBroadcastSetupPage()
            // TODO("for testing live")
            doCreateChannel()
        }

        rvFollowers.adapter = followersAdapter
        rvFollowers.doOnPreDraw {
            if (rvFollowers.itemDecorationCount == 0)
                rvFollowers.addItemDecoration(PlayFollowerItemDecoration())
        }
    }

    override fun onBackPressed(): Boolean {
        showDialogWhenActionClose()
        return true
    }

    private fun doCreateChannel() {
        viewModel.createChannel(
                shopId = 0,
                productIds = emptyArray(),
                coverUrl = "",
                title = ""
        )
    }

    private fun openBroadcastSetupPage() {
        val setupClass = PlayBroadcastSetupBottomSheet::class.java
        val setupFragment = fragmentFactory.instantiate(setupClass.classLoader!!, setupClass.name) as PlayBroadcastSetupBottomSheet
        setupFragment.show(childFragmentManager)
    }

    private fun openBroadcastLivePage(channelInfo: ChannelInfoUiModel) {
        broadcastCoordinator.navigateToFragment(PlayBroadcastUserInteractionFragment::class.java,
                Bundle().apply {
                    putString(PlayBroadcastUserInteractionFragment.KEY_CHANNEL_ID, channelInfo.channelId)
                    putString(PlayBroadcastUserInteractionFragment.KEY_INGEST_URL, channelInfo.ingestUrl)
                })
    }

    private fun showDialogWhenActionClose() {
        activity?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.play_prepare_broadcast_dialog_end_title))
                setDescription(getString(R.string.play_prepare_broadcast_dialog_end_desc))
                setPrimaryCTAText(getString(R.string.play_prepare_broadcast_dialog_end_primary))
                setSecondaryCTAText(getString(R.string.play_prepare_broadcast_dialog_end_secondary))
                setPrimaryCTAClickListener { this.dismiss() }
                setSecondaryCTAClickListener {
                    it.finish()
                }
            }.show()
        }
    }

    //region observe
    /**
     * Observe
     */

    private fun observeFollowers() {
        viewModel.observableFollowers.observe(viewLifecycleOwner, Observer {
            followersAdapter.setItemsAndAnimateChanges(it)
        })
    }

    private fun observeCreateChannel() {
        viewModel.observableCreateChannel.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    // TODO("handle: count down")
                    openBroadcastLivePage(it.data)
                }
                is Fail -> {
                    // TODO(handle: show toaster error)
                }
            }
        })
    }
    //endregion
}