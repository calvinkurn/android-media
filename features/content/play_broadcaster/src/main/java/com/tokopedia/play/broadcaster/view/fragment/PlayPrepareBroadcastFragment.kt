package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayFollowerItemDecoration
import com.tokopedia.play.broadcaster.view.adapter.PlayFollowersAdapter
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayPrepareBroadcastCreatePromoBottomSheet
import com.tokopedia.play.broadcaster.view.viewmodel.PlayPrepareBroadcastViewModel
import javax.inject.Inject

/**
 * Created by jegul on 20/05/20
 */
class PlayPrepareBroadcastFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var parentViewModel: PlayPrepareBroadcastViewModel

    private lateinit var rvFollowers: RecyclerView

    private val followersAdapter = PlayFollowersAdapter()

    override fun getScreenName(): String = "Play Prepare Page"

    override fun initInjector() {
        DaggerPlayBroadcasterComponent.create()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayPrepareBroadcastViewModel::class.java)
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
    }

    private fun initView(view: View) {
        with(view) {
            rvFollowers = findViewById(R.id.rv_followers)
        }
    }

    private fun setupView(view: View) {
        rvFollowers.adapter = followersAdapter
        rvFollowers.viewTreeObserver.addOnPreDrawListener {
            if (rvFollowers.itemDecorationCount == 0)
                rvFollowers.addItemDecoration(PlayFollowerItemDecoration())

            true
        }
    }

    //region observe
    /**
     * Observe
     */

    private fun observeFollowers() {
        parentViewModel.observableFollowers.observe(viewLifecycleOwner, Observer {
            followersAdapter.setItemsAndAnimateChanges(it)
        })
    }
    //endregion

    companion object {

        fun newInstance(): PlayPrepareBroadcastFragment {
            return PlayPrepareBroadcastFragment()
        }
    }
}