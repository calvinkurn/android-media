package com.tokopedia.product_bundle.multiple.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.di.ProductBundleComponentBuilder
import com.tokopedia.product_bundle.common.di.ProductBundleModule
import com.tokopedia.product_bundle.common.extension.setSubtitleText
import com.tokopedia.product_bundle.common.extension.setTitleText
import com.tokopedia.product_bundle.common.util.Utility
import com.tokopedia.product_bundle.multiple.di.DaggerMultipleProductBundleComponent
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleDetailAdapter
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleDetailAdapter.ProductBundleDetailItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter.ProductBundleMasterItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.product_bundle.viewmodel.ProductBundleViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject
import kotlin.math.roundToInt

class MultipleProductBundleFragment : BaseDaggerFragment(),
        ProductBundleMasterItemClickListener,
        ProductBundleDetailItemClickListener {

    companion object {
        @JvmStatic
        fun newInstance() =
                MultipleProductBundleFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ProductBundleViewModel::class.java)
    }

    private var soldProductBundleTextView: Typography? = null

    // product bundle master components
    private var productBundleMasterView: RecyclerView? = null
    private var productBundleMasterAdapter: ProductBundleMasterAdapter? = null

    // product bundle detail components
    private var productBundleDetailView: RecyclerView? = null
    private var productBundleDetailAdapter: ProductBundleDetailAdapter? = null

    private var productBundleOverView: TotalAmount? = null
    private var errorToaster: Snackbar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_product_bundle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        soldProductBundleTextView = view.findViewById(R.id.tv_sold_product_bundle)
        // setup product bundle master
        setupProductBundleMasterView(view)
        // setup product bundle detail
        setupProductBundleDetailView(view)
        // setup product bundle overview
        setupProductBundleOverView(view)
        // simulate get product info api call and conversion from bundle to product bundle master
        val productBundleMasters = viewModel.getProductBundleMasters()
        // render product bundle master chips
        productBundleMasterAdapter?.setProductBundleMasterList(productBundleMasters)
        // simulate get the first bundle information
        val recommendedBundleId = viewModel.getRecommendedProductBundleId(viewModel.getProductBundleMasters())
        // get recommended product bundle info
        viewModel.getBundleInfo(recommendedBundleId)

        errorToaster = Toaster.build(view, "Error Message", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)

        // observe data from view model
        subscribeToProductBundleInfo()
        subscribeToErrorState()
    }

    override fun getScreenName(): String {
        return getString(R.string.product_bundle_page_title)
    }

    override fun initInjector() {
        DaggerMultipleProductBundleComponent.builder()
                .productBundleComponent(ProductBundleComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
                .productBundleModule(ProductBundleModule())
                .build()
                .inject(this)
    }

    private fun setupProductBundleMasterView(view: View) {
        productBundleMasterView = view.findViewById(R.id.rv_product_bundle_master)
        productBundleMasterAdapter = ProductBundleMasterAdapter(this)
        productBundleMasterView?.let {
            it.adapter = productBundleMasterAdapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupProductBundleDetailView(view: View) {
        productBundleDetailView = view.findViewById(R.id.rv_product_bundle_detail)
        productBundleDetailAdapter = ProductBundleDetailAdapter(this)
        productBundleDetailView?.let {
            it.adapter = productBundleDetailAdapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupProductBundleOverView(view: View) {
        productBundleOverView = view.findViewById(R.id.ta_product_bundle_sum)
        productBundleOverView?.setLabelOrder(TotalAmount.Order.TITLE, TotalAmount.Order.AMOUNT, TotalAmount.Order.SUBTITLE)
    }

    private fun updateProductBundleOverView(productBundleOverView: TotalAmount?,
                                            totalDiscount: Double,
                                            totalBundlePrice: Double,
                                            totalPrice: Double,
                                            totalSaving: Double) {
        val totalDiscountText = String.format(getString(R.string.text_discount_in_percentage), totalDiscount.roundToInt())
        val totalBundlePriceText = Utility.formatToRupiahFormat(totalBundlePrice.roundToInt())
        val totalPriceText = Utility.formatToRupiahFormat(totalPrice.roundToInt())
        val totalSavingText = Utility.formatToRupiahFormat(totalSaving.roundToInt())
        productBundleOverView?.setTitleText(totalDiscountText, totalBundlePriceText)
        productBundleOverView?.amountView?.text = totalPriceText
        productBundleOverView?.setSubtitleText(getString(R.string.text_saving), totalSavingText)
        productBundleOverView?.amountCtaView?.setOnClickListener {
            viewModel.addProductBundleToCart()
        }
    }

    private fun subscribeToProductBundleInfo() {
        viewModel.getBundleInfoResult.observe(viewLifecycleOwner, Observer { bundleInfo ->
            // render product bundle detail
            val productBundleItems = bundleInfo.bundleItems
            val productBundleDetails = viewModel.mapProductBundleItemsToProductBundleDetail(productBundleItems)
            productBundleDetailAdapter?.setProductBundleDetails(productBundleDetails)
            // render sold product bundle view
            val soldProductBundle = viewModel.getSoldProductBundle()
            soldProductBundleTextView?.text = String.format(getString(R.string.text_sold_product_bundle), soldProductBundle)
            // render product bundle overview section
            val totalPrice = viewModel.calculateTotalPrice(productBundleItems)
            val totalBundlePrice = viewModel.calculateTotalBundlePrice(productBundleItems)
            val totalDiscount = viewModel.calculateDiscountPercentage(totalPrice, totalBundlePrice)
            val totalSaving = viewModel.calculateTotalSaving(totalPrice, totalBundlePrice)
            updateProductBundleOverView(
                    productBundleOverView = productBundleOverView,
                    totalDiscount = totalDiscount,
                    totalBundlePrice = totalBundlePrice,
                    totalPrice = totalPrice,
                    totalSaving = totalSaving
            )
        })
    }

    private fun subscribeToErrorState() {
        viewModel.isError.observe(viewLifecycleOwner, Observer { isError ->
            if (isError) errorToaster?.show()
        })
    }

    override fun onProductBundleMasterItemClicked(adapterPosition: Int, productBundleMaster: ProductBundleMaster) {
        // update selected bundle state to view model
        viewModel.setSelectedProductBundleMaster(viewModel.getProductBundleMasters()[adapterPosition])
        // deselect the rest of selection except the selected one
        productBundleMasterAdapter?.deselectUnselectedItems(adapterPosition)
        // get product bundle detail
        val bundleId = productBundleMaster.bundleId
        viewModel.getBundleInfo(bundleId)
    }

    override fun onProductVariantSpinnerClicked(productBundleItem: BundleItem) {
        TODO("show product variant bottom sheet")
    }
}