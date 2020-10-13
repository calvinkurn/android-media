package com.tokopedia.product.info.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoExpandableDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoLoadingDataModel
import com.tokopedia.product.info.model.productdetail.uidata.ProductDetailInfoVisitable
import com.tokopedia.product.info.view.ProductDetailInfoListener
import com.tokopedia.product.info.view.adapter.BsProductDetailInfoAdapter
import com.tokopedia.product.info.view.adapter.ProductDetailInfoAdapterFactoryImpl
import com.tokopedia.product.info.view.adapter.diffutil.ProductDetailInfoDiffUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.concurrent.Executors


/**
 * Created by Yehezkiel on 12/10/20
 */
class ProductDetailInfoBottomSheet : BottomSheetUnify(), ProductDetailInfoListener {

    private var productDetailComponent: ProductDetailComponent? = null
    private var rvBsProductDetail: RecyclerView? = null
    private var currentList: List<ProductDetailInfoVisitable>? = null

    private val productDetailInfoAdapter by lazy {
        BsProductDetailInfoAdapter(AsyncDifferConfig.Builder(ProductDetailInfoDiffUtil())
                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
                .build(), adapterTypeFactory)
    }
    private val adapterTypeFactory by lazy {
        ProductDetailInfoAdapterFactoryImpl(this)
    }

    fun setDaggerComponent(daggerProductDetailComponent: ProductDetailComponent?) {
        this.productDetailComponent = daggerProductDetailComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        productDetailComponent?.inject(this)
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productDetailInfoAdapter.submitList(listOf(ProductDetailInfoLoadingDataModel()))
    }

    override fun onLoadingClick() {
        currentList = listOf(ProductDetailInfoExpandableDataModel(componentName = 1,isShowable = true), ProductDetailInfoExpandableDataModel(componentName = 2), ProductDetailInfoExpandableDataModel(componentName = 3))
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