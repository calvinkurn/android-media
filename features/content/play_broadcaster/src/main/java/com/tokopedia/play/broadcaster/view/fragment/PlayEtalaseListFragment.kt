package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.EtalaseLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder
import com.tokopedia.play.broadcaster.util.error.DefaultNetworkThrowable
import com.tokopedia.play.broadcaster.view.adapter.PlayEtalaseAdapter
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseEtalaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import com.tokopedia.play_common.util.extension.doOnPreDraw
import com.tokopedia.play_common.util.scroll.StopFlingScrollListener
import javax.inject.Inject

class PlayEtalaseListFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory,
        private val analytic: PlayBroadcastAnalytic
) : PlayBaseEtalaseSetupFragment() {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var rvEtalase: RecyclerView

    private val etalaseAdapter = PlayEtalaseAdapter(object : PlayEtalaseViewHolder.Listener {
        override fun onEtalaseClicked(etalaseId: String, etalaseName: String, sharedElements: List<View>) {
            etalaseSetupCoordinator.openEtalaseDetail(
                    etalaseId,
                    etalaseName,
                    sharedElements
            )
        }

        override fun onEtalaseBound(etalaseId: String) {
            viewModel.loadEtalaseProductPreview(etalaseId)
        }
    })

    override fun getScreenName(): String = "Etalase List Page"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(etalaseSetupCoordinator.getParent(), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        etalaseSetupCoordinator.showBottomAction(false)
        return inflater.inflate(R.layout.fragment_play_etalase_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setupTransition()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeEtalase()
        observeEtalaseStatus()
    }

    override fun refresh() {
        etalaseAdapter.notifyDataSetChanged()
    }

    private fun initView(view: View) {
        with(view) {
            rvEtalase = findViewById(R.id.rv_etalase)
        }
    }

    private fun setupView(view: View) {
        rvEtalase.layoutManager = GridLayoutManager(rvEtalase.context, SPAN_COUNT, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                override fun getSpanSize(position: Int): Int {
                    return if (etalaseAdapter.getItem(position) == EtalaseLoadingUiModel) SPAN_COUNT
                    else 1
                }
            }
        }
        rvEtalase.adapter = etalaseAdapter
        rvEtalase.addItemDecoration(PlayGridTwoItemDecoration(requireContext()))
        rvEtalase.addOnScrollListener(StopFlingScrollListener())
    }

    /**
     * Observe
     */
    private fun observeEtalase() {
        viewModel.observableEtalase.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                PageResultState.Loading -> {
                    etalaseSetupCoordinator.hideGlobalError()
                    etalaseAdapter.setItemsAndAnimateChanges(listOf(EtalaseLoadingUiModel))
                }
                is PageResultState.Success -> {
                    etalaseSetupCoordinator.hideGlobalError()
                    etalaseAdapter.setItemsAndAnimateChanges(it.currentValue)
                    startPostponedTransition()
                }
                is PageResultState.Fail -> {
                    etalaseSetupCoordinator.showGlobalError(if (it.state.error is DefaultNetworkThrowable) GlobalError.NO_CONNECTION else GlobalError.SERVER_ERROR) {
                        viewModel.loadEtalaseList()
                    }
                    startPostponedTransition()
                }
            }

        })
    }

    private fun observeEtalaseStatus() {
        viewModel.observableEtalaseProductState.observe(viewLifecycleOwner, Observer {
            if (it.state is PageResultState.Fail) {
                analytic.viewEtalaseError(it.state.error.localizedMessage)
            }
        })
    }

    /**
     * Transition
     */
    private fun setupTransition() {
        setupExitTransition()
        setupReenterTransition()
    }

    private fun setupExitTransition() {
        exitTransition = Slide(Gravity.BOTTOM)
                .setStartDelay(150)
                .setDuration(300)
    }

    private fun setupReenterTransition() {
        reenterTransition = Slide(Gravity.BOTTOM)
                .setStartDelay(150)
                .setDuration(300)
    }

    private fun startPostponedTransition() {
        requireView().doOnPreDraw {
            etalaseSetupCoordinator.startPostponedEnterTransition()
        }
    }

    companion object {

        private const val SPAN_COUNT = 2
    }
}