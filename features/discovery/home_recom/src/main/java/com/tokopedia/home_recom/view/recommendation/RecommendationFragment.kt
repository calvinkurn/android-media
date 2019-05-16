package com.tokopedia.home_recom.view.recommendation

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.dataModel.BaseHomeRecommendationDataModel
import com.tokopedia.home_recom.view.adapter.HomeRecommendationAdapter
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactoryImpl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class RecommendationFragment: BaseListFragment<BaseHomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl>(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    private val adapterFactory by lazy { HomeRecommendationTypeFactoryImpl() }
    private val adapter by lazy { HomeRecommendationAdapter(adapterTypeFactory) }
    private val viewModel by lazy { viewModelProvider.get(RecommendationViewModel::class.java) }

    companion object{
        fun newInstance() = RecommendationFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disableLoadMore()
        viewModel.data.observe(this, Observer {
            if(it is Success) renderList(listOf(it.data))
            else if(it is Fail) showGetListError(it.throwable)
        })
    }

    override fun getAdapterTypeFactory(): HomeRecommendationTypeFactoryImpl {
        return adapterFactory
    }

    override fun createAdapterInstance(): BaseListAdapter<BaseHomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl> {
        return adapter
    }

    override fun onItemClicked(t: BaseHomeRecommendationDataModel) {
        //Do nothing
    }

    override fun getScreenName(): String = ""


    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        //Do nothing
    }
}