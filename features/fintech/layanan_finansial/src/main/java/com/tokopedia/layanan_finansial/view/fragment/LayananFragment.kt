package com.tokopedia.layanan_finansial.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.di.LayananComponent
import com.tokopedia.layanan_finansial.view.adapter.LayananViewHolderFactory
import com.tokopedia.layanan_finansial.view.customview.LayananSectionView
import com.tokopedia.layanan_finansial.view.models.LayananFinansialModel
import com.tokopedia.layanan_finansial.view.models.LayananSectionModel
import com.tokopedia.layanan_finansial.view.viewModel.LayananFinansialViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class LayananFragment : BaseListFragment<LayananSectionModel,LayananViewHolderFactory>() {


    @Inject
    lateinit var factory: ViewModelFactory
    val viewModel by lazy { ViewModelProviders.of(this,factory)[LayananFinansialViewModel::class.java] }
    private val performanceInterface by lazy { PageLoadTimePerformanceCallback(LAYANAN_PLT_PREPARE_METRICS, LAYANAN_PLT_NETWORK_METRICS, LAYANAN_PLT_RENDER_METRICS) as PageLoadTimePerformanceInterface }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performanceInterface.startMonitoring(LAYANAN_PLT)
        performanceInterface.startPreparePagePerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    private fun addObserver()  = viewModel.liveData.observe(this, Observer {
        it?.let {
            when(it){
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

    private fun render(data: LayananFinansialModel) {
        renderList(data.sectionList ?: mutableListOf())
    }

    override fun getScreenName(): String = this.javaClass.name

    override fun initInjector() {
     getComponent(LayananComponent::class.java).inject(this)
    }

    override fun onItemClicked(t: LayananSectionModel) {

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

    companion object{
       private const val LAYANAN_PLT = "layanan_plt"
       private const val LAYANAN_PLT_PREPARE_METRICS = "layanan_plt_prepare_metrics"
       private const val LAYANAN_PLT_NETWORK_METRICS = "layanan_plt_network_metrics"
       private const val LAYANAN_PLT_RENDER_METRICS = "layanan_plt_render_metrics"

    }
}