package com.tokopedia.home.beranda.presentation.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business.BusinessWidgetTypeFactory
import com.tokopedia.home.beranda.presentation.view.viewmodel.ItemTabBusinessViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.layout_recyclerview_business_widget.*
import javax.inject.Inject

class BusinessUnitItemFragment : BaseListFragment<HomeWidget.ContentItemTab, BusinessWidgetTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ItemTabBusinessViewModel

    lateinit var itemTab: HomeWidget.TabItem

    companion object {
        const val ITEM_EXTRAS = "ITEM_EXTRAS"

        fun newInstance(item : HomeWidget.TabItem) : Fragment {
            val fragment = BusinessUnitItemFragment()
            val bundle = Bundle()
            bundle.putParcelable(ITEM_EXTRAS, item)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            itemTab = it?.getParcelable(ITEM_EXTRAS)!!
        }
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(ItemTabBusinessViewModel::class.java)
        }
    }

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_recyclerview_business_widget, container, false);
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
        return BusinessWidgetTypeFactory()
    }

    override fun onItemClicked(t: HomeWidget.ContentItemTab?) {

    }

    override fun loadData(page: Int) {
        viewModel.getList(
                GraphqlHelper.loadRawString(activity?.resources, R.raw.query_content_tab_business_widget),
                itemTab.id
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.homeWidget.observe(
                this, Observer { when (it) {
            is Success -> onSuccessGetList(it.data)
            is Fail -> onErrorGetList(it.throwable)
        } })
    }

    private fun onSuccessGetList(data: HomeWidget) {
        renderList(data.contentItemTabList, false)
    }

    private fun onErrorGetList(throwable: Throwable) {

    }
}
