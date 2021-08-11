package com.tokopedia.product_bundle.single.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.di.ProductBundleComponentBuilder
import com.tokopedia.product_bundle.common.di.ProductBundleModule
import com.tokopedia.product_bundle.common.extension.setSubtitleText
import com.tokopedia.product_bundle.common.extension.setTitleText
import com.tokopedia.product_bundle.common.util.AtcVariantNavigation
import com.tokopedia.product_bundle.single.di.DaggerSingleProductBundleComponent
import com.tokopedia.product_bundle.single.presentation.adapter.BundleItemListener
import com.tokopedia.product_bundle.single.presentation.adapter.SingleProductBundleAdapter
import com.tokopedia.product_bundle.single.presentation.viewmodel.SingleProductBundleViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class SingleProductBundleFragment(
    private var bundleInfo: BundleInfo
) : BaseDaggerFragment(), BundleItemListener {

    @Inject
    lateinit var viewModel: SingleProductBundleViewModel

    private var tvBundleSold: Typography? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var totalAmount: TotalAmount? = null
    private var adapter = SingleProductBundleAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setBundleInfo(requireContext(), bundleInfo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_single_product_bundle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTotalSold(view)
        setupRecyclerViewItems(view)
        setupTotalAmount(view)

        observeSingleProductBundleUiModel()
        observeTotalAmountUiModel()
        observeToasterError()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AtcVariantHelper.onActivityResultAtcVariant(requireContext(), requestCode, data) {
            val selectedProductVariant = adapter.getSelectedProductVariant() ?: ProductVariant()
           adapter.setSelectedVariant(selectedProductId, viewModel.getVariantText(selectedProductVariant, selectedProductId))
        }
    }

    override fun getScreenName() = SingleProductBundleFragment::class.java.simpleName

    override fun initInjector() {
        DaggerSingleProductBundleComponent.builder()
                .productBundleComponent(ProductBundleComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
                .productBundleModule(ProductBundleModule())
                .build()
                .inject(this)
    }

    override fun onVariantSpinnerClicked(selectedVariant: ProductVariant?) {
        selectedVariant?.let {
            AtcVariantNavigation.showVariantBottomSheet(this, it)
        }
    }

    override fun onBundleItemSelected(originalPrice: Double, discountedPrice: Double, quantity: Int) {
        viewModel.updateTotalAmount(originalPrice, discountedPrice, quantity)
    }

    private fun observeSingleProductBundleUiModel() {
        viewModel.singleProductBundleUiModel.observe(viewLifecycleOwner, {
            swipeRefreshLayout?.isRefreshing = false
            updateTotalPO(it.preorderDurationWording)
            adapter.setData(it.items, it.selectedItems)
        })
    }

    private fun observeTotalAmountUiModel() {
        viewModel.totalAmountUiModel.observe(viewLifecycleOwner, {
            updateTotalAmount(it.price, it.discount, it.slashPrice, it.priceGap)
        })
    }

    private fun observeToasterError() {
        viewModel.toasterError.observe(viewLifecycleOwner, { throwable ->
            throwable.message?.let {
                Toaster.build(requireView(), it, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        })
    }

    private fun setupTotalSold(view: View) {
        tvBundleSold = view.findViewById(R.id.tv_bundle_sold)
        updateTotalPO(null)
    }

    private fun setupRecyclerViewItems(view: View) {
        val rvBundleItems: RecyclerView = view.findViewById(R.id.rv_bundle_items)
        rvBundleItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvBundleItems.adapter = adapter

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout?.isEnabled = false
    }

    private fun setupTotalAmount(view: View) {
        totalAmount = view.findViewById(R.id.total_amount)
        totalAmount?.apply {
            setLabelOrder(TotalAmount.Order.TITLE, TotalAmount.Order.AMOUNT, TotalAmount.Order.SUBTITLE)
            updateTotalAmount("Rp0", 0, "Rp0", "Rp0")
            amountCtaView.setOnClickListener {
                viewModel.checkout(adapter.getSelectedData())
            }
        }
    }

    private fun updateTotalPO(totalPOWording: String?) {
        tvBundleSold?.isVisible = totalPOWording != null
        tvBundleSold?.text = "PO $totalPOWording"
    }

    private fun updateTotalAmount(price: String, discount: Int, slashPrice: String, priceGap: String) {
        totalAmount?.apply {
            amountView.text = price
            setTitleText("$discount%", slashPrice)
            setSubtitleText("Hemat", priceGap)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundleInfo: BundleInfo) = SingleProductBundleFragment(bundleInfo)
    }
}