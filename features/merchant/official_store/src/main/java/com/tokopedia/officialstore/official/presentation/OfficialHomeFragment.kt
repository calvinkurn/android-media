package com.tokopedia.officialstore.official.presentation

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.officialstore.BuildConfig
import com.tokopedia.officialstore.OfficialStoreInstance
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.common.RecyclerViewScrollListener
import com.tokopedia.officialstore.official.data.OfficialHomeMapper
import com.tokopedia.officialstore.official.di.DaggerOfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeComponent
import com.tokopedia.officialstore.official.di.OfficialStoreHomeModule
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapterTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.OfficialBannerViewModel
import com.tokopedia.officialstore.official.presentation.viewmodel.OfficialStoreHomeViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class OfficialHomeFragment : BaseDaggerFragment(), HasComponent<OfficialStoreHomeComponent> {

    companion object {
        const val BUNDLE_CATEGORY = "category_os"

        @JvmStatic
        fun newInstance(bundle: Bundle?) = OfficialHomeFragment().apply { arguments = bundle }
    }

    @Inject
    lateinit var viewModel: OfficialStoreHomeViewModel

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null

    private var category: Category? = null

    private var adapter: OfficialHomeAdapter? = null
    private var listOfOfficialHome: ArrayList<Visitable<OfficialHomeAdapterTypeFactory>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(BUNDLE_CATEGORY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_official_home_child, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        recyclerView = view.findViewById(R.id.recycler_view)

        layoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = layoutManager

        val adapterTypeFactory = OfficialHomeAdapterTypeFactory()
        adapter = OfficialHomeAdapter(adapterTypeFactory)
        adapter?.addElement(listOfOfficialHome)
        recyclerView?.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        observeBannerData()
        viewModel.getOfficialStoreBanners(category?.categoryId ?: "")
    }

    private fun observeBannerData() {
        viewModel.officialStoreBannersResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    OfficialHomeMapper.mappingBanners(it.data, listOfOfficialHome)
                    adapter?.notifyItemInserted(0) // banner will always first
                }
                is Fail -> {
                    if (BuildConfig.DEBUG)
                        it.throwable.printStackTrace()
                }
            }
        })
    }

    private fun setListener() {
        swipeRefreshLayout?.setOnRefreshListener {
            swipeRefreshLayout?.isRefreshing = false
            viewModel.getOfficialStoreBanners(category?.categoryId ?: "")
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
        component?.inject(this)
    }

    override fun getComponent(): OfficialStoreHomeComponent? {
        return activity?.run {
            DaggerOfficialStoreHomeComponent
                    .builder()
                    .officialStoreHomeModule(OfficialStoreHomeModule())
                    .officialStoreComponent(OfficialStoreInstance.getComponent(application))
                    .build()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}