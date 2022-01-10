package com.tokopedia.pdp.fintech.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter
import com.tokopedia.pdp.fintech.di.components.DaggerFintechWidgetComponent
import com.tokopedia.pdp.fintech.domain.datamodel.WidgetDetail
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp.fintech.viewmodel.FintechWidgetViewModel
import com.tokopedia.pdp_fintech.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class PdpFintechWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
) : BaseCustomView(context, attrs, defStyleAttr) {


    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private lateinit var baseView: View
    private lateinit var loader: LoaderUnify
    private lateinit var fintechWidgetAdapter : FintechWidgetAdapter
    private lateinit var instanceProductUpdateListner: ProductUpdateListner
    private lateinit var fintechWidgetViewModel: FintechWidgetViewModel


    init {
        initInjector()
        initView()
        initRecycler()
    }


    fun updateBaseFragmentContext(  parentViewModelStore: ViewModelStore, parentLifeCycleOwner: LifecycleOwner)
    {
         fintechWidgetViewModel  =
            ViewModelProvider(parentViewModelStore, viewModelFactory.get()).get(FintechWidgetViewModel::class.java)
        observeProductInfo(parentLifeCycleOwner)
        observeWidgetInfo(parentLifeCycleOwner)

    }

    private fun observeWidgetInfo(parentLifeCycleOwner: LifecycleOwner) {
        fintechWidgetViewModel.widgetDetailLiveData.observe(parentLifeCycleOwner, {
            when (it) {
                is Success -> {
                    updateWidget(it.data)
                }
                is Fail -> {

                }
            }
        })
    }

    private fun updateWidget(data: WidgetDetail) {
       fintechWidgetAdapter.setData(data.list[0].chips)
    }

    private fun observeProductInfo(parentLifeCycleOwner: LifecycleOwner) {
        fintechWidgetViewModel.productDetailLiveData.observe(parentLifeCycleOwner, {
            when (it) {
                is Success -> {
                    loader.visibility = GONE
                    fintechWidgetViewModel.getWidgetData()
                }
                is Fail -> {
                    instanceProductUpdateListner.removeWidget()
                }
            }
        })
    }

    private fun initInjector() {
        DaggerFintechWidgetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initRecycler() {
        val recyclerView = baseView.findViewById<RecyclerView>(R.id.recycler_items)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        fintechWidgetAdapter = FintechWidgetAdapter(object :WidgetClickListner{
            override fun clickedWidget(position: Int) {
                if(position == 0)
                    instanceProductUpdateListner.showWebview()
                else
                    instanceProductUpdateListner.showBottomSheet(position)
            }



        })
        recyclerView.adapter = fintechWidgetAdapter
    }

    private fun initView() {
        baseView = inflate(context, R.layout.pdp_fintech_widget_layout, this)
        loader = baseView.findViewById(R.id.widgetShimmer)
    }

     fun updateProductId(
         productID: String,
         fintechWidgetViewHolder: ProductUpdateListner
     ) {
         this.instanceProductUpdateListner = fintechWidgetViewHolder
         loader.visibility = View.VISIBLE
         fintechWidgetViewModel.getProductDetail(productID)


    }




}