package com.tokopedia.product.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.kotlin.extensions.view.joinToStringWithLast
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.spesification.ProductSpecificationDataModel
import com.tokopedia.product.detail.data.model.spesification.Specification
import com.tokopedia.product.detail.data.model.spesification.SpecificationBodyDataModel
import com.tokopedia.product.detail.data.model.spesification.SpecificationTitleDataModel
import com.tokopedia.product.detail.view.adapter.factory.ProductSpecificationFactoryImpl

class ProductSpecificationFragment : BaseListFragment<ProductSpecificationDataModel, ProductSpecificationFactoryImpl>() {


    lateinit var data: List<Specification>

    private val adapterFactory by lazy {
        ProductSpecificationFactoryImpl()
    }

    override fun getAdapterTypeFactory(): ProductSpecificationFactoryImpl = adapterFactory

    override fun onItemClicked(t: ProductSpecificationDataModel?) {
        // NO OP
    }

    override fun loadData(page: Int) {
        renderList(mapToDataModel(data))
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

    companion object {
        const val SPECIFICATION_DATA = "specification_data"
        fun createInstance(data: ArrayList<Specification>): ProductSpecificationFragment = ProductSpecificationFragment().apply {
            arguments = Bundle().also {
                it.putParcelableArrayList(SPECIFICATION_DATA, data)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelableArrayList(SPECIFICATION_DATA) ?: arrayListOf()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_specification, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}
}