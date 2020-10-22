package com.tokopedia.product.info.view.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.joinToStringWithLast
import com.tokopedia.product.detail.R
import com.tokopedia.product.info.model.specification.ProductSpecificationDataModel
import com.tokopedia.product.info.model.specification.Specification
import com.tokopedia.product.info.model.specification.SpecificationBodyDataModel
import com.tokopedia.product.info.model.specification.SpecificationTitleDataModel
import com.tokopedia.product.info.view.adapter.ProductSpecificationAdapter
import com.tokopedia.product.info.view.adapter.ProductSpecificationFactoryImpl
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bs_product_specification.view.*

/**
 * Created by Yehezkiel on 21/10/20
 */
class ProductSpecificationBottomSheet : BottomSheetUnify() {

    private val adapter: ProductSpecificationAdapter = ProductSpecificationAdapter(ProductSpecificationFactoryImpl())
    private var specificationData: List<Specification> = listOf()

    fun getData(listOfData: List<Specification>) {
        this.specificationData = listOfData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun mapToDataModel(data: List<Specification>): MutableList<ProductSpecificationDataModel> {
        val productSpecificationDataModel: MutableList<ProductSpecificationDataModel> = mutableListOf()

        data.forEach { it ->
            productSpecificationDataModel.add(SpecificationTitleDataModel(it.name))
            it.row.forEach {
                val fusionValueText = it.value.joinToStringWithLast(separator = "\n")
                productSpecificationDataModel.add(SpecificationBodyDataModel(it.key, fusionValueText))
            }
        }

        return productSpecificationDataModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val visitable = mapToDataModel(specificationData)
        adapter.addElement(visitable)
    }

    private fun initView() {
        setTitle(getString(R.string.label_spec))
        val childView = View.inflate(requireContext(), R.layout.bs_product_specification, null)
        setupRecyclerView(childView)
        setChild(childView)
    }

    private fun setupRecyclerView(childView: View) {
        childView.rv_product_specification_bs.adapter = adapter
    }
}