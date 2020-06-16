package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.itemdecoration.PlayGridTwoItemDecoration
import com.tokopedia.play.broadcaster.ui.model.EtalaseLoadingUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder
import com.tokopedia.play.broadcaster.util.doOnPreDraw
import com.tokopedia.play.broadcaster.util.scroll.StopFlingScrollListener
import com.tokopedia.play.broadcaster.view.adapter.PlayEtalaseAdapter
import com.tokopedia.play.broadcaster.view.bottomsheet.PlayBroadcastSetupBottomSheet
import com.tokopedia.play.broadcaster.view.custom.PlaySearchBar
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseEtalaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import javax.inject.Inject

class PlayEtalaseListFragment @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : PlayBaseEtalaseSetupFragment() {

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var rvEtalase: RecyclerView
    private lateinit var psbSearch: PlaySearchBar

    private val etalaseAdapter = PlayEtalaseAdapter(object : PlayEtalaseViewHolder.Listener {
        override fun onEtalaseClicked(etalaseId: String, sharedElements: List<View>) {
            etalaseSetupCoordinator.openEtalaseDetail(
                    etalaseId,
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
        viewModel = ViewModelProviders.of(getParentFragmentByClass(PlayBroadcastSetupBottomSheet::class.java), viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        etalaseSetupCoordinator.postponeEnterTransition()
        return inflater.inflate(R.layout.fragment_play_etalase_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeEtalase()
    }

    private fun initView(view: View) {
        with(view) {
            rvEtalase = findViewById(R.id.rv_etalase)
            psbSearch = findViewById(R.id.psb_search)
        }
    }

    private fun setupView(view: View) {
        psbSearch.setOnClickListener {
            etalaseSetupCoordinator.openSearchPage("", emptyList())
        }

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
                    etalaseAdapter.setItemsAndAnimateChanges(listOf(EtalaseLoadingUiModel))
                }
                is PageResultState.Success -> {
                    etalaseAdapter.setItemsAndAnimateChanges(it.currentValue)
                    startPostponedTransition()
                }
                is PageResultState.Fail -> {
                    startPostponedTransition()
                }
            }

        })
    }

    private fun startPostponedTransition() {
        requireView().doOnPreDraw {
            etalaseSetupCoordinator.startPostponedEnterTransition()
        }
    }

    private fun getParentFragmentByClass(clazz: Class<out Fragment>): Fragment {
        var parent = parentFragment
        while (parent != null && clazz != parent::class.java) {
            parent = parent.parentFragment
        }
        return parent ?: throw IllegalArgumentException("No parent found with class : $clazz")
    }

    companion object {

        private const val SPAN_COUNT = 2
    }
}