package com.tokopedia.digital.productV2.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.digital.R
import com.tokopedia.digital.productV2.di.DigitalProductComponent
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapter
import com.tokopedia.digital.productV2.presentation.adapter.DigitalProductAdapterFactory
import com.tokopedia.digital.productV2.presentation.adapter.viewholder.OnInputListener
import com.tokopedia.digital.productV2.presentation.viewmodel.DigitalProductViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_digital_product.*
import javax.inject.Inject

class DigitalProductFragment: BaseTopupBillsFragment(), OnInputListener {

    //    @Inject
//    lateinit var trackingUtil: DigitalHomeTrackingUtil
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var viewModel: DigitalProductViewModel

    lateinit var adapter: DigitalProductAdapter

    private var inputData: MutableList<String> = mutableListOf()

    private var menuId: Int = 0
    private var categoryId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_digital_product, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(DigitalProductViewModel::class.java)

            adapter = DigitalProductAdapter(this, DigitalProductAdapterFactory(this@DigitalProductFragment))
        }

        arguments?.let {
            menuId = it.getInt(EXTRA_PARAM_MENU_ID)
            categoryId = it.getString(EXTRA_PARAM_CATEGORY_ID, "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = rv_digital_product as VerticalRecyclerView
        recyclerView.clearItemDecoration()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
//                    adapter.renderList(it.data)
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
                    val dataList: MutableList<Visitable<DigitalProductAdapterFactory>> = mutableListOf()
                    dataList.addAll(it.data.enquiryFields)
                    dataList.add(it.data.product)
                    adapter.renderList(dataList)
//                    trackSearchResultCategories(it.data)
                }
                is Fail -> {
//                    showGetListError(it.throwable)
                }
            }
        })
    }

    private fun getOperatorCluster(menuId: Int) {
        viewModel.getOperatorCluster(GraphqlHelper.loadRawString(resources, R.raw.query_category_operator_select_group), viewModel.createParams(menuId))
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
        inputData[position] = input
    }

    companion object {
        const val EXTRA_PARAM_MENU_ID = "EXTRA_PARAM_MENU_ID"
        const val EXTRA_PARAM_CATEGORY_ID = "EXTRA_PARAM_CATEGORY_ID"

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