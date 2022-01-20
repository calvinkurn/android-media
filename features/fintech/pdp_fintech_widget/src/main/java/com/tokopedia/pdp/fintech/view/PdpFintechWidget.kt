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
import com.tokopedia.pdp.fintech.domain.datamodel.ChipsData
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
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


    private var idToPriceMap = HashMap<String, String>()
    private var priceToChip = HashMap<String, ArrayList<ChipsData>>()
    private var listOfPrice: ArrayList<Double> = ArrayList()
    private var listOfUrls: ArrayList<String?> = ArrayList()
    private var categoryId: String? = null
    private lateinit var productID: String
    private lateinit var productPrice: String


    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private lateinit var baseView: View
    private lateinit var loader: LoaderUnify
    private lateinit var fintechWidgetAdapter: FintechWidgetAdapter
    private lateinit var instanceProductUpdateListner: ProductUpdateListner
    private lateinit var fintechWidgetViewModel: FintechWidgetViewModel

    private var counter = 0


    init {
        initInjector()
        initView()
        initRecycler()
    }


    fun updateBaseFragmentContext(
        parentViewModelStore: ViewModelStore,
        parentLifeCycleOwner: LifecycleOwner
    ) {
        fintechWidgetViewModel =
            ViewModelProvider(parentViewModelStore, viewModelFactory.get()).get(
                FintechWidgetViewModel::class.java
            )
        observeWidgetInfo(parentLifeCycleOwner)

    }

    private fun observeWidgetInfo(parentLifeCycleOwner: LifecycleOwner) {
        fintechWidgetViewModel.widgetDetailLiveData.observe(parentLifeCycleOwner, {
            when (it) {
                is Success -> {
                    setPriceToChipMap(it.data)
                    loader.visibility = View.GONE
                    getChipDataAndUpdate(idToPriceMap[productID])
                }
                is Fail -> {
                    instanceProductUpdateListner.removeWidget()
                }
            }
        })
    }

    private fun setPriceToChipMap(widgetDetail: WidgetDetail) {
        widgetDetail.baseWidgetResponse?.baseData?.let {
            for (i in it.list.indices) {
                priceToChip[it.list[i].price.toString()] = it.list[i].chips
            }
        }
    }


    private fun initInjector() {
        DaggerFintechWidgetComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    private fun initRecycler() {
        val recyclerView = baseView.findViewById<RecyclerView>(R.id.recycler_items)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        fintechWidgetAdapter = FintechWidgetAdapter(context, object : WidgetClickListner {
            override fun clickedWidget(cta: Int, url: String) {
                instanceProductUpdateListner.fintechRedirection(
                    FintechRedirectionWidgetDataClass(
                        cta,
                        url
                    )
                )
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
        try {
            this.productID = productID
            this.instanceProductUpdateListner = fintechWidgetViewHolder
            loader.visibility = View.VISIBLE
            if (counter == 0) {
                counter++
                categoryId?.let {
                    fintechWidgetViewModel.getWidgetData(
                        it,
                        listOfPrice,
                        listOfUrls
                    )
                }
            } else {
                if (priceToChip.size != 0 && idToPriceMap.size != 0)
                    getChipDataAndUpdate(idToPriceMap[productID])
                else
                    categoryId?.let {
                        fintechWidgetViewModel.getWidgetData(it, listOfPrice, listOfUrls)
                    }
            }

        } catch (e: Exception) {
            instanceProductUpdateListner.removeWidget()
        }
    }

    private fun getChipDataAndUpdate(productPrice: String?) {
        if (productPrice != null) {
            this.productPrice = productPrice
        }
        productPrice?.let {
            priceToChip[it]?.let { chipList ->
                loader.visibility = View.GONE
                instanceProductUpdateListner.showWidget()
                fintechWidgetAdapter.setData(chipList)
                fintechWidgetAdapter.notifyItemRangeChanged(0, fintechWidgetAdapter.itemCount)

            } ?: run {
                instanceProductUpdateListner.removeWidget()
            }
        } ?: run {
            instanceProductUpdateListner.removeWidget()
        }
    }

    fun updateidToPriceMap(
        productIdToPrice: HashMap<String, String>,
        listofProductUrl: ArrayList<String?>,
        productCategoryId: String?
    ) {
        idToPriceMap = productIdToPrice
        categoryId = productCategoryId
        listOfUrls = listofProductUrl
        productIdToPrice.values.map { it.toDouble() }.toCollection(listOfPrice)

    }

}