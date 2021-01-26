package com.tokopedia.product.estimasiongkir.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.KG
import com.tokopedia.product.detail.data.util.LABEL_GRAM
import com.tokopedia.product.detail.data.util.LABEL_KG
import com.tokopedia.product.detail.data.util.numberFormatted
import com.tokopedia.product.detail.view.util.ProductSeparatorItemDecoration
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductServiceDetailDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.estimasiongkir.di.DaggerRatesEstimationComponent
import com.tokopedia.product.estimasiongkir.di.RatesEstimationModule
import com.tokopedia.product.estimasiongkir.view.adapter.ProductDetailShippingDIffutil
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactoryImpl
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationDetailViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductDetailShippingBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: RatesEstimationDetailViewModel? = null
    private var adapter: ProductDetailShippingAdapter? = null
    private var rv: RecyclerView? = null

    fun show(childFragmentManager: FragmentManager) {
        show(childFragmentManager, "shipping_bs")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.let {
            DaggerRatesEstimationComponent.builder()
                    .baseAppComponent((activity?.application as com.tokopedia.abstraction.base.app.BaseMainApplication).baseAppComponent)
                    .ratesEstimationModule(RatesEstimationModule()).build()
                    .inject(this)
        }
        super.onCreate(savedInstanceState)
        if (::viewModelFactory.isInitialized) {
            viewModel = ViewModelProvider(this, viewModelFactory).get(RatesEstimationDetailViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedViewModel = ViewModelProvider(requireActivity()).get(ProductDetailSharedViewModel::class.java)
        sharedViewModel.rateEstimateRequest.observeOnce(viewLifecycleOwner) {
            viewModel?.getCostEstimation(it.getWeightRequest(), it.shopDomain, it.origin, it.shopId, it.productId)
        }

        viewModel?.rateEstResp?.observe(viewLifecycleOwner) {
            it.doSuccessOrFail({
                val address = it.data.address
                val shop = it.data.shop
                val sharedViewModelData = sharedViewModel.rateEstimateRequest.value
                val weightString = context?.getString(R.string.double_string_builder, sharedViewModelData?.productWeight?.numberFormatted(), if (sharedViewModelData?.productWeightUnit?.toLowerCase() == KG)
                    LABEL_KG else LABEL_GRAM) ?: ""
                val productShippingHeader = ProductShippingHeaderDataModel(
                        id = 1,
                        shippingTo = shop.cityName,
                        shippingFrom = "${address.districtName}, ${address.provinceName}",
                        weight = weightString,
                        isFreeOngkir = sharedViewModelData?.isFreeOngkir ?: false,
                        freeOngkirEstimation = ""
                )
                val productServiceData: MutableList<ProductShippingVisitable> = mapToServicesData(it.data.rates)
                productServiceData.add(0, productShippingHeader)

                adapter?.submitList(productServiceData.toList())
            }) {

            }
        }
    }

    private fun setupRecyclerView(view: View) {
        rv = view.findViewById(R.id.rv_product_shipping)
        if (rv?.itemDecorationCount == 0 && context != null) {
            rv?.addItemDecoration(ProductSeparatorItemDecoration(requireContext()))
        }
        val asyncDiffer = AsyncDifferConfig.Builder(ProductDetailShippingDIffutil())
                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
                .build()
        adapter = ProductDetailShippingAdapter(asyncDiffer, ProductShippingFactoryImpl())

        rv?.adapter = adapter
    }

    private fun init() {
        context?.let {
            val view = View.inflate(it, R.layout.bs_product_shipping_rate_estimate, null)

            setTitle(it.getString(R.string.merchant_product_detail_shipping))
            setChild(view)
            clearContentPadding = true
            setupRecyclerView(view)
        }
    }

    private fun mapToServicesData(rates: RatesModel): MutableList<ProductShippingVisitable> {
        return rates.services.map { service ->
            val servicesDetail = service.products.map {
                ProductServiceDetailDataModel(it.name, it.texts.etd, it.price.priceFmt, it.cod.isCodAvailable == 1)
            }
            ProductShippingServiceDataModel(service.id.toLong(), service.name, servicesDetail)
        }.toMutableList()
    }

}