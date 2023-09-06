package com.tokopedia.layanan_finansial.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.layanan_finansial.di.LayananComponent
import com.tokopedia.layanan_finansial.view.models.LayananFinansialModel
import com.tokopedia.layanan_finansial.view.models.TopAdsImageModel
import com.tokopedia.layanan_finansial.view.viewHolder.LayananViewHolderFactory
import com.tokopedia.layanan_finansial.view.viewModel.LayananFinansialViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class LayananFragment : BaseListFragment<Visitable<*>, LayananViewHolderFactory>() {


    @Inject
    lateinit var factory: ViewModelFactory
    val viewModel by lazy {
        ViewModelProviders.of(
            this,
            factory
        )[LayananFinansialViewModel::class.java]
    }
    private val performanceInterface by lazy {
        PageLoadTimePerformanceCallback(
            LAYANAN_PLT_PREPARE_METRICS,
            LAYANAN_PLT_NETWORK_METRICS,
            LAYANAN_PLT_RENDER_METRICS
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performanceInterface.startMonitoring(LAYANAN_PLT)
        performanceInterface.startPreparePagePerformanceMonitoring()
        super.onCreate(savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                androidx.core.content.ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
    }

    private fun addObserver() = viewModel.liveData.observe(this, Observer {
        it?.let {
            when (it) {
                is Success -> {
                    performanceInterface.stopNetworkRequestPerformanceMonitoring()
                    performanceInterface.startRenderPerformanceMonitoring()
                    render(it.data)
                    performanceInterface.stopRenderPerformanceMonitoring()
                }
                is Fail -> {
                    performanceInterface.stopNetworkRequestPerformanceMonitoring()
                    performanceInterface.startRenderPerformanceMonitoring()
                    showGetListError(it.throwable)
                    performanceInterface.stopRenderPerformanceMonitoring()
                }
            }
            performanceInterface.stopMonitoring()
        }
    })

    /**
     * Added a empty list of DataType TopAdsImageModel to notify recycler view of the BaseListFragment about addition of a new view type for ads
     */
    private fun render(data: LayananFinansialModel) {
        renderList(data.sectionList ?: mutableListOf())
        val adList: ArrayList<TopAdsImageModel> = ArrayList()
        adList.add(TopAdsImageModel())
        renderList(adList as List<Visitable<*>>)
    }

    override fun getScreenName(): String = this.javaClass.name

    override fun initInjector() {
        getComponent(LayananComponent::class.java).inject(this)
    }


    override fun loadData(page: Int) {
        performanceInterface.stopPreparePagePerformanceMonitoring()
        performanceInterface.startNetworkRequestPerformanceMonitoring()
        viewModel.getDetail()
    }

    override fun getAdapterTypeFactory(): LayananViewHolderFactory {
        return LayananViewHolderFactory()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    companion object {
        private const val LAYANAN_PLT = "layanan_plt"
        private const val LAYANAN_PLT_PREPARE_METRICS = "layanan_plt_prepare_metrics"
        private const val LAYANAN_PLT_NETWORK_METRICS = "layanan_plt_network_metrics"
        private const val LAYANAN_PLT_RENDER_METRICS = "layanan_plt_render_metrics"

    }

    override fun onItemClicked(t: Visitable<*>?) {
    }
}