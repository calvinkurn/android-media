package com.tokopedia.topads.dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.data.internal.ParamObject.KEY_AD_TYPE
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.TopAdsDeletedAdsResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.DeletedGroupItemsListAdapter
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.DeletedItemsAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsEmptyModel
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsItemModel
import com.tokopedia.topads.dashboard.view.presenter.TopAdsDashboardPresenter
import com.tokopedia.unifycomponents.LoaderUnify
import javax.inject.Inject

class TopAdsDashDeletedGroupFragment : BaseDaggerFragment() {

    private lateinit var adapter: DeletedGroupItemsListAdapter
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var totalCount = 0
    private var totalPage = 0
    private var currentPageNum = 1
    private lateinit var loader: LoaderUnify
    private val adType: String? by lazy { arguments?.getString(KEY_AD_TYPE) }


    @Inject
    lateinit var topAdsDashboardPresenter: TopAdsDashboardPresenter

    companion object {
        fun createInstance(bundle: Bundle): TopAdsDashDeletedGroupFragment {
            val fragment = TopAdsDashDeletedGroupFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return TopAdsDashDeletedGroupFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            resources.getLayout(R.layout.topads_dash_fragment_deleted_group_list),
            container,
            false
        )
        recyclerView = view.findViewById(R.id.deletedGroupList)
        setAdapter()
        return view
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.isNestedScrollingEnabled = false
        adapter = DeletedGroupItemsListAdapter(
            DeletedItemsAdapterTypeFactoryImpl()
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
    }

    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (currentPageNum < totalPage) {
                    currentPageNum++
                    adType?.let { fetchNextPage(currentPageNum, it) }

                }
            }
        }
    }

    private fun fetchNextPage(page: Int, adType: String) {
        val startDate =
            Utils.format.format((parentFragment as TopAdsBaseTabFragment).startDate)
        val endDate = Utils.format.format((parentFragment as TopAdsBaseTabFragment).endDate)
        topAdsDashboardPresenter.getDeletedAds(
            page, adType,
            startDate, endDate, ::onSuccessResult, ::onEmptyResult
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        adType?.let { fetchFirstPage(it) }
    }

    private fun initViews(view: View) {
        loader = view.findViewById(R.id.loader)
        recyclerView = view.findViewById(R.id.deletedGroupList)
    }


    fun fetchFirstPage(adType: String) {
        currentPageNum = 1
        adapter.clearList()
        loader.show()
        val startDate =
            Utils.format.format((parentFragment as TopAdsBaseTabFragment).startDate)
        val endDate = Utils.format.format((parentFragment as TopAdsBaseTabFragment).endDate)
        topAdsDashboardPresenter.getDeletedAds(
            currentPageNum, adType,
            startDate, endDate, ::onSuccessResult, ::onEmptyResult
        )
    }

    private fun onSuccessResult(topAdsDeletedAdsResponse: TopAdsDeletedAdsResponse) {
        setPaginationFlow(topAdsDeletedAdsResponse.topAdsDeletedAds.page)
        val list = topAdsDeletedAdsResponse.topAdsDeletedAds.topAdsDeletedAdsItemList
        adapter.submitList(ArrayList(getList(list)))
        loader.hide()
    }

    private fun setPaginationFlow(page: TopAdsDeletedAdsResponse.TopadsDeletedAds.Page) {
        totalCount = page.total
        totalPage = if (totalCount % page.perPage == 0) {
            totalCount / page.perPage
        } else
            (totalCount / page.perPage) + 1
        recyclerviewScrollListener.updateStateAfterGetData()
        (parentFragment as TopAdsBaseTabFragment).setDeletedGroupCount(totalCount)
    }

    private fun getList(list: List<TopAdsDeletedAdsResponse.TopadsDeletedAds.TopAdsDeletedAdsItem>): MutableList<DeletedGroupItemsItemModel> {
        val itemList = ArrayList<DeletedGroupItemsItemModel>()
        list.forEach {
            it.AdType = adType ?: ""
            val model = DeletedGroupItemsItemModel(it)
            itemList.add(model)
        }
        return itemList

    }

    private fun onEmptyResult() {
        if (adapter.itemCount > 0) return
        (parentFragment as TopAdsBaseTabFragment).setDeletedGroupCount(adapter.itemCount)
        adapter.submitList(arrayListOf(DeletedGroupItemsEmptyModel()))
        loader.hide()
    }
}