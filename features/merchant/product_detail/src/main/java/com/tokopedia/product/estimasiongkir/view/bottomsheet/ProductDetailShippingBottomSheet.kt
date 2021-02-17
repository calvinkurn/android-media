package com.tokopedia.product.estimasiongkir.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.observeOnce
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.KG
import com.tokopedia.product.detail.data.util.LABEL_GRAM
import com.tokopedia.product.detail.data.util.LABEL_KG
import com.tokopedia.product.detail.data.util.numberFormatted
import com.tokopedia.product.detail.view.util.ProductSeparatorItemDecoration
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.detail.view.util.showToasterSuccess
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductServiceDetailDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingShimmerDataModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.estimasiongkir.di.DaggerRatesEstimationComponent
import com.tokopedia.product.estimasiongkir.di.RatesEstimationModule
import com.tokopedia.product.estimasiongkir.view.adapter.ProductDetailShippingDIffutil
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingFactoryImpl
import com.tokopedia.product.estimasiongkir.view.adapter.ProductShippingVisitable
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationBoeViewModel
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationDetailViewModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.bs_product_shipping_rate_estimate.view.*
import javax.inject.Inject


/**
 * Created by Yehezkiel on 25/01/21
 */
class ProductDetailShippingBottomSheet : BottomSheetDialogFragment(), ProductDetailShippingListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewModel: RatesEstimationBoeViewModel? = null
    private var adapter: ProductDetailShippingAdapter? = null
    private var rv: RecyclerView? = null

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = View.inflate(context, R.layout.bs_product_shipping_rate_estimate, null)
        setupBottomSheet(dialog)

        dialog.run {
            setupRecyclerView(view)
            setContentView(view)
            initInjector()
            initViewModel()
            observeData()
        }
    }

    private fun setupBottomSheet(dialog: Dialog) {
        val dialogFragment = dialog as BottomSheetDialog
        dialogFragment.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialogFragment.behavior.skipCollapsed = true
    }

    override fun getTheme(): Int {
        return R.style.ProductBottomSheetDialogTheme
    }

    private fun initInjector() {
        activity?.let {
            DaggerRatesEstimationComponent.builder()
                    .baseAppComponent((activity?.application as com.tokopedia.abstraction.base.app.BaseMainApplication).baseAppComponent)
                    .ratesEstimationModule(RatesEstimationModule()).build()
                    .inject(this)
        }
    }

    private fun initViewModel() {
        if (::viewModelFactory.isInitialized) {
            viewModel = ViewModelProvider(this, viewModelFactory).get(RatesEstimationBoeViewModel::class.java)
        }
    }

    fun show(childFragmentManager: FragmentManager) {
        show(childFragmentManager, "shipping_bs")
    }

    private fun observeData() {
        val sharedViewModel = ViewModelProvider(requireActivity()).get(ProductDetailSharedViewModel::class.java)
        sharedViewModel.rateEstimateRequest.observeOnce(this) {
            viewModel?.setRatesRequest(it)
        }

        viewModel?.ratesVisitableResult?.observe(this) {
            it.doSuccessOrFail({
                adapter?.submitList(it.data)
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
                .build()

        adapter = ProductDetailShippingAdapter(asyncDiffer, ProductShippingFactoryImpl(this))

        rv?.adapter = adapter
        adapter?.submitList(listOf(ProductShippingShimmerDataModel()))
    }

    override fun onChooseAddressClicked() {
        view?.showToasterSuccess("Clicked bottom sheet")
    }
}