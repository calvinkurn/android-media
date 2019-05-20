package com.tokopedia.topupbills.telco.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoProductComponentData
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoProductViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoProductWidget
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductFragment : BaseDaggerFragment() {

    private lateinit var telcoTelcoProductView: DigitalTelcoProductWidget
    private lateinit var sharedModel: SharedProductTelcoViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productViewModel: DigitalTelcoProductViewModel

    override fun onStart() {
        context?.let {
            GraphqlClient.init(it)
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            productViewModel = viewModelProvider.get(DigitalTelcoProductViewModel::class.java)
            sharedModel = viewModelProvider.get(SharedProductTelcoViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModel.productItem.observe(this, Observer {
            it?.run {
                telcoTelcoProductView.notifyProductItemChanges(it.key)
            }
        })
    }

    override fun getScreenName(): String {
        return DigitalTelcoProductFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_product, container, false)
        telcoTelcoProductView = view.findViewById(R.id.telco_product_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val productType = it.getInt(PRODUCT_TYPE)
            val operatorId = it.getString(OPERATOR_ID)
            val componentId = it.getInt(COMPONENT_TYPE)

            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put("componentID", componentId)
            mapParam.put("operatorID", operatorId)
            productViewModel.getProductCollections(GraphqlHelper.loadRawString(resources, R.raw.query_product_digital_telco),
                    mapParam, productType, this::onSuccessProductList, this::onErrorProductList)
        }

        telcoTelcoProductView.setListener(object : DigitalTelcoProductWidget.ActionListener {
            override fun onClickProduct(itemProduct: TelcoProductDataCollection) {
                sharedModel.setProductSelected(itemProduct)
            }
        })
    }

    fun onSuccessProductList(productData: TelcoProductComponentData) {
        telcoTelcoProductView.renderGridProductList(productData.productType, productData.rechargeProductData.productDataCollections)
    }

    fun onErrorProductList(throwable: Throwable) {
        Toast.makeText(activity, "product " + throwable.message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        val PRODUCT_TYPE = "product_type"
        val COMPONENT_TYPE = "component_type"
        val OPERATOR_ID = "operator_Id"

        fun newInstance(componentType: Int, operatorId: String, productType: Int): Fragment {
            val fragment = DigitalTelcoProductFragment()
            val bundle = Bundle()
            bundle.putInt(PRODUCT_TYPE, productType)
            bundle.putInt(COMPONENT_TYPE, componentType)
            bundle.putString(OPERATOR_ID, operatorId)
            fragment.arguments = bundle
            return fragment
        }
    }
}