package com.tokopedia.product_bundle.single.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.di.ProductBundleComponentBuilder
import com.tokopedia.product_bundle.common.di.ProductBundleModule
import com.tokopedia.product_bundle.common.extension.setSubtitleText
import com.tokopedia.product_bundle.common.extension.setTitleText
import com.tokopedia.product_bundle.single.di.DaggerSingleProductBundleComponent
import com.tokopedia.product_bundle.single.presentation.adapter.SingleProductBundleAdapter
import com.tokopedia.product_bundle.single.presentation.viewmodel.SingleProductBundleViewModel
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class SingleProductBundleFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModel: SingleProductBundleViewModel

    private var tvBundleSold: Typography? = null
    private var swipeRefreshLayout: SwipeToRefresh? = null
    private var totalAmount: TotalAmount? = null
    private var adapter = SingleProductBundleAdapter()

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
    }

    override fun getScreenName() = SingleProductBundleFragment::class.java.simpleName

    override fun initInjector() {
        DaggerSingleProductBundleComponent.builder()
                .productBundleComponent(ProductBundleComponentBuilder.getComponent(requireContext().applicationContext as BaseMainApplication))
                .productBundleModule(ProductBundleModule())
                .build()
                .inject(this)
    }

    private fun observeSingleProductBundleUiModel() {
        viewModel.singleProductBundleUiModel.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout?.isRefreshing = false
            updateTotalSold(it.itemsSoldCount)
            adapter.setData(it.items)
            updateTotalAmount(it.price, it.discount, it.slashPrice, it.priceGap)
        })
    }

    private fun setupTotalSold(view: View) {
        tvBundleSold = view.findViewById(R.id.tv_bundle_sold)
        updateTotalSold(0)
    }

    private fun setupRecyclerViewItems(view: View) {
        val rvBundleItems: RecyclerView = view.findViewById(R.id.rv_bundle_items)
        rvBundleItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvBundleItems.adapter = adapter

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout?.setOnRefreshListener {
            viewModel.getBundleData()
        }
    }

    private fun setupTotalAmount(view: View) {
        totalAmount = view.findViewById(R.id.total_amount)
        totalAmount?.apply {
            setLabelOrder(TotalAmount.Order.TITLE, TotalAmount.Order.AMOUNT, TotalAmount.Order.SUBTITLE)
            updateTotalAmount("Rp0", 0, "Rp0", "Rp0")
        }
    }

    private fun updateTotalSold(totalSold: Int) {
        tvBundleSold?.text = "Terjual $totalSold paket"
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
        fun newInstance() =
                SingleProductBundleFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}