package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.view.adapter.product.ProductListAdapter
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.product.viewmodel.ProductItemViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.TopadsCreateProductListKeywordListSheetBinding

class CreateProductListBottomSheet : BottomSheetUnify() {

    private var binding: TopadsCreateProductListKeywordListSheetBinding? = null
    private var productList: MutableList<TopAdsProductModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val viewBinding = TopadsCreateProductListKeywordListSheetBinding.inflate(inflater, container, false)
        binding = viewBinding
        isHideable = true
        showCloseIcon = true
        setChild(viewBinding.root)
        setTitle((String.format(getString(R.string.top_ads_create_bottom_sheet_title),
            productList.count().toString())))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val productListAdapter = ProductListAdapter(ProductListAdapterTypeFactoryImpl {})
        binding?.createProductListRv?.apply {
            adapter = productListAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(object : DividerItemDecoration(context, VERTICAL) {
                override fun shouldDrawOnLastItem(): Boolean {
                    return false
                }
            })
        }
        productListAdapter.items = productList.map { ProductItemViewModel(it).apply { isCompact = true } }.toMutableList()

    }

    fun show(
        fragmentManager: FragmentManager,
    ) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "PRODUCT_LIST_BOTTOM_SHEET_TAG"
        fun newInstance(productList: MutableList<TopAdsProductModel>): CreateProductListBottomSheet =
            CreateProductListBottomSheet().apply {
                this.productList = productList
            }
    }
}
