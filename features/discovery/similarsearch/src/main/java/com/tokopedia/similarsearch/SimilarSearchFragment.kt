package com.tokopedia.similarsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.similar_search_fragment_layout.*
import java.lang.Error

internal class SimilarSearchFragment: TkpdBaseV4Fragment() {

    companion object {
        fun getInstance(): SimilarSearchFragment {
            return SimilarSearchFragment()
        }
    }

    private var similarSearchViewModel: SimilarSearchViewModel? = null
    private var similarSearchAdapter: SimilarSearchAdapter? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    override fun getScreenName(): String {
        return "/searchproduct - product"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.similar_search_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
        observeViewModelData()

        similarSearchViewModel?.onViewCreated()
    }

    private fun initViewModel() {
        activity?.let { activity ->
            similarSearchViewModel = ViewModelProviders.of(activity).get(SimilarSearchViewModel::class.java)
        }
    }

    private fun initViews() {
        bindSelectedProductView()
        bindRecyclerView()
        disableSwipeRefreshLayout()
    }

    private fun bindSelectedProductView() {
        view?.let { view ->
            val selectedProductViewListener = createSelectedProductViewListener(view)
            SimilarSearchSelectedProductView(selectedProductViewListener).bindSelectedProductView()
        }
    }

    private fun createSelectedProductViewListener(view: View): SimilarSearchSelectedProductViewListener {
        return object : SimilarSearchSelectedProductViewListener {
            override fun getSelectedProduct(): SimilarSearchSelectedProduct? {
                return similarSearchViewModel?.similarSearchSelectedProduct
            }

            override fun getFragmentView(): View {
                return view
            }
        }
    }

    private fun bindRecyclerView() {
        similarSearchAdapter = SimilarSearchAdapter()

        recyclerViewSimilarSearch?.adapter = similarSearchAdapter
        recyclerViewSimilarSearch?.layoutManager = createRecyclerViewSimilarSearchLayoutManager()
    }

    private fun createRecyclerViewSimilarSearchLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
            it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }

    private fun disableSwipeRefreshLayout() {
        swipeToRefreshSimilarSearch?.isEnabled = false
    }

    private fun observeViewModelData() {
        similarSearchViewModel?.getSimilarSearchLiveData()?.observe(viewLifecycleOwner, Observer {
            updateAdapter(it)
        })
    }

    private fun updateAdapter(similarSearchLiveData: State<List<Any>>) {
        when (similarSearchLiveData) {
            is State.Loading -> {
                swipeToRefreshSimilarSearch?.isRefreshing = true
                updateAdapterList(similarSearchLiveData)
            }
            is State.Success -> {
                swipeToRefreshSimilarSearch?.isRefreshing = false
                updateAdapterList(similarSearchLiveData)
            }
            is State.Error -> {

            }
        }
    }

    private fun updateAdapterList(similarSearchLiveData: State<List<Any>>) {
        similarSearchAdapter?.updateList(similarSearchLiveData.data ?: listOf())
    }
}