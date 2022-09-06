package com.tokopedia.product.info.view.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.detail.databinding.BsProductSpecificationBinding
import com.tokopedia.product.info.model.productdetail.uidata.AnnotationValueDataModel
import com.tokopedia.product.info.view.adapter.ProductAnnotationAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by Yehezkiel on 21/10/20
 */
class ProductAnnotationBottomSheet : BottomSheetUnify() {

    private val adapter: ProductAnnotationAdapter = ProductAnnotationAdapter()
    private var annotation: List<ProductDetailInfoContent> = listOf()

    private var binding by autoClearedNullable<BsProductSpecificationBinding>()

    fun getData(listOfData: List<ProductDetailInfoContent>) {
        this.annotation = listOfData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun mapToDataModel(data: List<ProductDetailInfoContent>): List<AnnotationValueDataModel> {
        return data.filter {
            it.title.isNotEmpty() && it.subtitle.isNotEmpty()
        }.map {
            AnnotationValueDataModel(it.title, it.subtitle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setData(mapToDataModel(annotation))
    }

    private fun initView() {
        setTitle(getString(R.string.merchant_product_detail_label_specification_product))
        val childView = View.inflate(requireContext(), R.layout.bs_product_specification, null)
        binding = BsProductSpecificationBinding.bind(childView)
        setupRecyclerView()
        setChild(childView)
    }

    private fun setupRecyclerView() {
        binding?.rvProductSpecificationBs?.adapter = adapter
    }
}