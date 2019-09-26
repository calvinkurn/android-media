package com.tokopedia.officialstore.presentation

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.presentation.adapter.viewmodel.OfficialBannerViewModel
import com.tokopedia.officialstore.presentation.di.DaggerOfficialHomeComponent
import javax.inject.Inject

class OfficialHomeFragment : BaseDaggerFragment(), OfficialHomeView {

    companion object {

        val BUNDLE_CATEGORY = "category_os"

        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeFragment().apply { arguments = bundle }
    }

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null

    private var adapter: OfficialHomeAdapter? = null

    @Inject lateinit var presenter: OfficialHomePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_official_home_child, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)


        layoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = layoutManager

        val adapterTypeFactory = OfficialHomeAdapterTypeFactory()
        adapter = OfficialHomeAdapter(adapterTypeFactory)
        recyclerView?.adapter = adapter

        adapter?.addElement(dummy())

        setListener()
    }

    private fun setListener() {
        swipeRefreshLayout?.setOnRefreshListener {
            swipeRefreshLayout?.isRefreshing = false
        }

        if (parentFragment is RecyclerViewScrollListener) {
            val scrollListener = parentFragment as RecyclerViewScrollListener
            layoutManager?.let {
                var firstVisibleInListview = it.findFirstVisibleItemPosition()
                recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val currentFirstVisible = it.findFirstVisibleItemPosition()

                        // scroll up
                        if (currentFirstVisible > firstVisibleInListview) {
                            scrollListener.onScrollUp()
                        } else { // scroll down
                            scrollListener.onScrollDown()
                        }
                        firstVisibleInListview = currentFirstVisible
                    }
                })
            }
        }

    }

    fun dummy(): List<Visitable<OfficialHomeAdapterTypeFactory>> {
        val dummy = ArrayList<Visitable<OfficialHomeAdapterTypeFactory>>()
        dummy.add(OfficialBannerViewModel())
        return dummy
    }

    private fun loadDataProduct() {
        // Get Product Recommendation

    }

    private fun onErrorGetRecommendation(errorMessage: String?) {
        // Show error
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerOfficialHomeComponent.builder()
                .baseAppComponent((activity!!.applicationContext as BaseMainApplication)
                        .baseAppComponent)
                .build()
                .inject(this)
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}