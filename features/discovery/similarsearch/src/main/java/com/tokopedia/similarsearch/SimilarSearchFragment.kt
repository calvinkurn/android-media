package com.tokopedia.similarsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct
import kotlinx.android.synthetic.main.similar_search_fragment_layout.*

internal class SimilarSearchFragment: TkpdBaseV4Fragment() {

    companion object {
        fun getInstance(): SimilarSearchFragment {
            return SimilarSearchFragment()
        }
    }

    private var similarSearchViewModel: SimilarSearchViewModel? = null
    private var swipeRefreshLayoutSimilarSearch: SwipeRefreshLayout? = null
    private var similarSearchAdapter: SimilarSearchAdapter? = null
//    private var recyclerViewSimilarSearch: RecyclerView? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    override fun getScreenName(): String {
        return "/searchproduct - product"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.similar_search_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            initViewModel(it)
            initViews(it)
        }

        similarSearchViewModel?.onViewCreated()
    }

    private fun initViewModel(activity: FragmentActivity) {
        similarSearchViewModel = ViewModelProviders.of(activity).get(SimilarSearchViewModel::class.java)
    }

    private fun initViews(activity: FragmentActivity) {
        bindSelectedProductView()
        bindRecyclerView()
    }

    private fun bindSelectedProductView() {
        view?.let { view ->
            val selectedProductViewListener = object : SimilarSearchSelectedProductViewListener {
                override fun getSelectedProduct(): SimilarSearchSelectedProduct? {
                    return similarSearchViewModel?.similarSearchSelectedProduct
                }

                override fun getFragmentView(): View {
                    return view
                }
            }

            SimilarSearchSelectedProductView(selectedProductViewListener).bindSelectedProductView()
        }
    }

    private fun bindRecyclerView() {
        similarSearchAdapter = SimilarSearchAdapter()

        recyclerViewSimilarSearch?.adapter = similarSearchAdapter
        recyclerViewSimilarSearch?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).also {
            it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }

        similarSearchAdapter?.updateList(mutableListOf<Any>().also {
            it.add(DividerViewModel())
        })
    }
}