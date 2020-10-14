package com.tokopedia.product.info.view.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.info.model.productdetail.uidata.*
import com.tokopedia.product.info.view.BsProductDetailInfoViewModel
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.BsProductDetailInfoAdapter
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactoryImpl
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.concurrent.Executors
import javax.inject.Inject


/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoBottomSheet : BottomSheetUnify(), ProductDetailInfoListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(BsProductDetailInfoViewModel::class.java)
    }

    private var productDetailComponent: ProductDetailComponent? = null
    private var rvBsProductDetail: RecyclerView? = null
    private var currentList: List<ProductDetailInfoVisitable>? = null

    private var productId: String = ""
    private var shopId: String = ""

    private val productDetailInfoAdapter by lazy {
        BsProductDetailInfoAdapter(AsyncDifferConfig.Builder(ProductDetailInfoDiffUtil())
                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
                .build(), adapterTypeFactory)
    }
    private val adapterTypeFactory by lazy {
        ProductDetailInfoAdapterFactoryImpl(this)
    }

    fun setDaggerComponent(productId: String, shopId: String, daggerProductDetailComponent: ProductDetailComponent?) {
        this.productDetailComponent = daggerProductDetailComponent
        this.productId = productId
        this.shopId = shopId
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewLifecycleOwner.observe(viewModel.bottomSheetDetailData) { data ->
            data.doSuccessOrFail({
                Log.e("successs", "value " + it.data)
            }) {
                Log.e("successs", "fail " + it.message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        productDetailComponent?.inject(this)
        super.onCreate(savedInstanceState)
        viewModel.setParams(productId, shopId)
        initView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productDetailInfoAdapter.submitList(listOf(ProductDetailInfoLoadingDataModel()))
    }

    override fun onLoadingClick() {
        currentList = listOf(ProductDetailInfoExpandableDataModel(componentName = 1, isShowable = true), ProductDetailInfoExpandableListDataModel(componentName = 2), ProductDetailInfoExpandableImageDataModel(componentName = 3))
        productDetailInfoAdapter.submitList(currentList)
    }

    override fun closeAllExpand(uniqueIdentifier: Int, toggle: Boolean) {
        productDetailInfoAdapter.closeAllExpanded(uniqueIdentifier, toggle, currentList ?: listOf())
    }

    private fun initView() {
        setTitle(getString(R.string.merchant_product_detail_label_product_detail))
        val childView = View.inflate(requireContext(), R.layout.bottom_sheet_product_detail_info, null)

        setupRecyclerView(childView)
        setChild(childView)
    }

    private fun setupRecyclerView(childView: View) {
        rvBsProductDetail = childView.findViewById(R.id.bs_product_info_rv)
        rvBsProductDetail?.itemAnimator = null
        rvBsProductDetail?.adapter = productDetailInfoAdapter
    }

}