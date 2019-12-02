package com.tokopedia.digital.productV2.presentation.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.common.topupbills.widget.TopupBillsInputFieldWidget
import com.tokopedia.digital.R
import com.tokopedia.digital.productV2.di.DigitalProductComponent
import com.tokopedia.digital.productV2.model.DigitalProductOperatorCluster
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapter
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapterFactory
import com.tokopedia.digital.productV2.presentation.adapter.viewholder.OnInputListener
import com.tokopedia.digital.productV2.presentation.viewmodel.DigitalProductViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_digital_product.*
import javax.inject.Inject

class DigitalProductFragment: BaseTopupBillsFragment(), OnInputListener, DigitalProductAdapter.LoaderListener {

    //    @Inject
//    lateinit var trackingUtil: DigitalHomeTrackingUtil
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: DigitalProductViewModel

    lateinit var adapter: DigitalProductAdapter

    private var inputData: MutableMap<Int, String> = mutableMapOf()
    private var inputDataSize = 0

    private var menuId: Int = 0
    private var categoryId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_product, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalProductViewModel::class.java)

            adapter = DigitalProductAdapter(it, DigitalProductAdapterFactory(this), this)
        }

        arguments?.let {
            menuId = it.getInt(EXTRA_PARAM_MENU_ID)
            categoryId = it.getString(EXTRA_PARAM_CATEGORY_ID, "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_digital_product.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_digital_product.adapter = adapter
        while (rv_digital_product.itemDecorationCount > 0) rv_digital_product.removeItemDecorationAt(0)
        rv_digital_product.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                // Add offset to all items except the last one
                if (parent.getChildAdapterPosition(view) < adapter.dataSize - 1) {
                    context?.resources?.getDimension(ITEM_DECORATOR_SIZE)?.toInt()?.let { dimen -> outRect.bottom = dimen }
                }
            }
        })

        loadData()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(DigitalProductComponent::class.java).inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.operatorCluster.observe(this, Observer {
            when(it) {
                is Success -> {
//                    (activity as BaseSimpleActivity).updateTitle("")
                    renderOperatorCluster(it.data)
//                    trackSearchResultCategories(it.data)
                }
                is Fail -> {
//                    showGetListError(it.throwable)
                }
            }
        })

        viewModel.productList.observe(this, Observer {
            when(it) {
                is Success -> {
                    adapter.hideLoading()
                    val dataList: MutableList<Visitable<DigitalProductAdapterFactory>> = mutableListOf()
                    dataList.addAll(it.data.enquiryFields)
                    dataList.add(it.data.product)

                    inputDataSize = it.data.enquiryFields.size + 1
                    adapter.renderList(dataList)
//                    trackSearchResultCategories(it.data)
                }
                is Fail -> {
//                    showGetListError(it.throwable)
                }
            }
        })
    }

    private fun renderOperatorCluster(cluster: DigitalProductOperatorCluster) {
        if (cluster.operatorGroups.size == 1) {
            operator_cluster_select.hide()
            renderOperatorList(cluster.operatorGroups[0])
        } else if (cluster.operatorGroups.size > 1) {
            operator_cluster_select.setLabel(cluster.text)
            operator_cluster_select.setHint("")
            operator_cluster_select.setActionListener(object : TopupBillsInputFieldWidget.ActionListener {
                override fun onFinishInput(input: String) {
                    cluster.operatorGroups.find { it.name == input }?.let {
                        renderOperatorList(it)
                    }
                }
            })

            // Setup dropdown bottom sheet data
            val dropdownData = cluster.operatorGroups.map { TopupBillsInputDropdownData(it.name) }
            operator_cluster_select.setupDropdownBottomSheet(dropdownData)
            operator_cluster_select.show()
        }
    }

    private fun renderOperatorList(operatorGroup: DigitalProductOperatorCluster.CatalogOperatorGroup) {
        if (operatorGroup.operators.size == 1) {
            operator_select.hide()
            adapter.showLoading()
            getProductList(menuId, operatorGroup.operators[0].id.toString())
        } else if (operatorGroup.operators.size > 1) {
            operator_select.setLabel(operatorGroup.name)
            operator_select.setHint("")
            operator_select.setActionListener(object : TopupBillsInputFieldWidget.ActionListener {
                override fun onFinishInput(input: String) {
                    operatorGroup.operators.find { it.attributes.name == input }?.let {
                        adapter.showLoading()
                        getProductList(menuId, it.id.toString())
                    }
                }
            })

            // Setup dropdown bottom sheet data
            val dropdownData = operatorGroup.operators.map { TopupBillsInputDropdownData(it.attributes.name) }
            operator_select.setupDropdownBottomSheet(dropdownData)
            operator_select.show()
        }
    }

    override fun loadData() {
        getMenuDetail(menuId)
        getOperatorCluster(menuId)
    }

    private fun getOperatorCluster(menuId: Int) {
        viewModel.getOperatorCluster(GraphqlHelper.loadRawString(resources, R.raw.query_catalog_operator_select_group), viewModel.createParams(menuId))
    }

    private fun getProductList(menuId: Int, operator: String) {
        viewModel.getProductList(GraphqlHelper.loadRawString(resources, com.tokopedia.common.topupbills.R.raw.query_catalog_product_input), viewModel.createParams(menuId, operator))
    }

    override fun processEnquiry(data: TelcoEnquiryData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun processMenuDetail(data: TopupBillsMenuDetail) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFinishInput(input: String, position: Int) {
        updateInputData(input, position)
    }

    private fun updateInputData(input: String, position: Int) {
        inputData[position] = input
        if (inputData.size == inputDataSize) {
            // TODO: Enable enquiry button
        }
    }

    companion object {
        const val EXTRA_PARAM_MENU_ID = "EXTRA_PARAM_MENU_ID"
        const val EXTRA_PARAM_CATEGORY_ID = "EXTRA_PARAM_CATEGORY_ID"

        const val INPUT_DATA_SIZE = "INPUT_DATA_SIZE"

        val ITEM_DECORATOR_SIZE = com.tokopedia.design.R.dimen.dp_8

        fun newInstance(menuId: Int, categoryId: String): DigitalProductFragment {
            val fragment = DigitalProductFragment()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PARAM_MENU_ID, menuId)
            bundle.putString(EXTRA_PARAM_CATEGORY_ID, categoryId)
            fragment.arguments = bundle
            return fragment
        }
    }
}