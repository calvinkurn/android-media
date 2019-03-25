package com.tokopedia.home.beranda.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home.IHomeRouter
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business.BusinessWidgetTypeFactory
import com.tokopedia.home.beranda.presentation.view.viewmodel.ItemTabBusinessViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.layout_recyclerview_business_widget.*
import javax.inject.Inject


class BusinessUnitItemFragment : BaseListFragment<HomeWidget.ContentItemTab, BusinessWidgetTypeFactory>(),
    BusinessUnitItemView {

    @Inject
    lateinit var viewModel: ItemTabBusinessViewModel

    private lateinit var itemTab: HomeWidget.TabItem
    private lateinit var trackingQueue: TrackingQueue
    private var positionWidget: Int = 0
    private var nameTab: String = ""

    companion object {
        const val ITEM_EXTRAS = "ITEM_EXTRAS"
        const val ITEM_POSITION = "ITEM_POSITION"
        const val ITEM_NAME = "ITEM_NAME"

        fun newInstance(item: HomeWidget.TabItem, position: Int, name: String) : Fragment {
            val fragment = BusinessUnitItemFragment()
            val bundle = Bundle()
            bundle.putParcelable(ITEM_EXTRAS, item)
            bundle.putInt(ITEM_POSITION, position)
            bundle.putString(ITEM_NAME, name)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.let {
            trackingQueue = TrackingQueue(it!!)
        }
        arguments.let {
            itemTab = it?.getParcelable(ITEM_EXTRAS)!!
            nameTab = it?.getString(ITEM_NAME)!!
            positionWidget = it.getInt(ITEM_POSITION)
        }
    }

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_recyclerview_business_widget, container, false)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return recyclerView
    }

    override fun initInjector() {
        val component = DaggerBerandaComponent.builder().baseAppComponent((activity!!.application as BaseMainApplication)
                .baseAppComponent)
                .build()
        component.inject(this)
    }

    override fun getScreenName(): String {
        return BusinessUnitItemFragment::class.java.simpleName
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun getAdapterTypeFactory(): BusinessWidgetTypeFactory {
        return BusinessWidgetTypeFactory(this)
    }

    override fun onItemClicked(element: HomeWidget.ContentItemTab) {
        (activity?.applicationContext as IHomeRouter).goToApplinkActivity(activity, element.applink)
        HomePageTracking.eventEnhancedClickHomeWidget(
                activity,
                element.id.toString(),
                String.format("/ - p%d - bu widget - %s", positionWidget.toString(), nameTab.toLowerCase()),
                element.name,
                element.imageUrl,
                adapter.data.indexOf(element).toString(),
                ""
        )
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && view != null) {
            requestListData()
        }
    }

    override fun loadData(page: Int) {
        if (userVisibleHint) {
            requestListData()
        }
    }

    private fun requestListData() {
        if (!adapter.isContainData) {
            viewModel.getList(
                    GraphqlHelper.loadRawString(
                            activity?.resources,
                            R.raw.query_content_tab_business_widget
                    ),
                    itemTab.id,
                    this
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view).addItemDecoration(
                SpacingItemDecoration(
                        convertDpToPixel(8.toFloat(), activity),
                        SpacingItemDecoration.HORIZONTAL)
        )
    }

    private fun convertDpToPixel(i: Float, context: Context?): Int {
        val r = context?.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r?.displayMetrics).toInt()
    }

    override fun onReloadButtonClick() {
        onRetryClicked()
    }

    override fun onSuccessGetData(data: HomeWidget) {
        renderList(data.contentItemTabList, false)
    }

    override fun onErrorGetData(throwable: Throwable) {
        onGetListErrorWithEmptyData(throwable)
    }

    override fun onImpressed(element: HomeWidget.ContentItemTab, position: Int) {
        HomePageTracking.eventEnhancedImpressionHomeWidget(
                trackingQueue,
                element.id.toString(),
                String.format("/ - p%d - bu widget - %s", positionWidget.toString(), nameTab.toLowerCase()),
                element.name,
                element.imageUrl,
                position.toString(),
                ""
        )
    }
}

interface BusinessUnitItemView {
    fun onReloadButtonClick()
    fun onSuccessGetData(data: HomeWidget)
    fun onErrorGetData(throwable: Throwable)
    fun onImpressed(element: HomeWidget.ContentItemTab, position: Int)
}
