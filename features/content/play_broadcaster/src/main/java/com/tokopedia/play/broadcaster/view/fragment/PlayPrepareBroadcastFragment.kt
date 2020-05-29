package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayFollowerItemDecoration
import com.tokopedia.play.broadcaster.view.adapter.PlayFollowersAdapter
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.uimodel.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayPrepareBroadcastViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayPrepareBroadcastFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val fragmentFactory: FragmentFactory
) : PlayBaseBroadcastFragment() {

    private lateinit var viewModel: PlayPrepareBroadcastViewModel
    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var btnSetup: UnifyButton
    private lateinit var rvFollowers: RecyclerView

    private val followersAdapter = PlayFollowersAdapter()

    override fun getScreenName(): String = "Play Prepare Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayPrepareBroadcastViewModel::class.java)
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
        btnSetup.setOnClickListener {
            // openBroadcastSetupPage()
            // TODO("for testing live")
            doCreateChannel()
        }

        rvFollowers.adapter = followersAdapter
        rvFollowers.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (rvFollowers.itemDecorationCount == 0) rvFollowers.addItemDecoration(PlayFollowerItemDecoration())
                rvFollowers.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
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
        broadcastCoordinator.navigateToFragment(PlayLiveBroadcastFragment::class.java,
                Bundle().apply {
                    putString(PlayLiveBroadcastFragment.KEY_CHANNEL_ID, channelInfo.channelId)
                    putString(PlayLiveBroadcastFragment.KEY_INGEST_URL, channelInfo.ingestUrl)
                })
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