package com.tokopedia.topupbills.telco.view.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.DigitalTopupAnalytics
import com.tokopedia.topupbills.telco.data.TelcoProductComponentData
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection
import com.tokopedia.topupbills.telco.view.bottomsheet.DigitalProductBottomSheet
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.model.DigitalTrackProductTelco
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoProductViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoProductWidget
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductFragment : BaseDaggerFragment() {

    private lateinit var telcoTelcoProductView: DigitalTelcoProductWidget
    private lateinit var emptyStateProductView: LinearLayout
    private lateinit var titleEmptyState: TextView
    private lateinit var descEmptyState: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedModel: SharedProductTelcoViewModel
    private lateinit var productViewModel: DigitalTelcoProductViewModel
    private lateinit var selectedOperatorName: String

    private var titleProduct: String = ""
    private var selectedProductId: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics

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
        emptyStateProductView = view.findViewById(R.id.telco_empty_state_layout)
        titleEmptyState = view.findViewById(R.id.title_empty_product)
        descEmptyState = view.findViewById(R.id.desc_empty_product)
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            titleProduct = it.getString(COMPONENT_NAME)
            val productType = it.getInt(PRODUCT_TYPE)
            val operatorId = it.getString(OPERATOR_ID)
            val componentId = it.getInt(COMPONENT_TYPE)
            selectedProductId = it.getString(SELECTED_PRODUCT_ID)
            selectedOperatorName = it.getString(OPERATOR_NAME)

            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(KEY_COMPONENT_ID, componentId)
            mapParam.put(KEY_OPERATOR_ID, operatorId)
            productViewModel.getProductCollections(GraphqlHelper.loadRawString(resources, R.raw.query_product_digital_telco),
                    mapParam, productType, this::onLoadingProductList, this::onSuccessProductList, this::onErrorProductList)
        }

        telcoTelcoProductView.setListener(object : DigitalTelcoProductWidget.ActionListener {
            override fun onClickProduct(itemProduct: TelcoProductDataCollection, position: Int) {
                sharedModel.setProductSelected(itemProduct)
                sharedModel.setShowTotalPrice(true)
                topupAnalytics.clickEnhanceCommerceProduct(itemProduct, position, selectedOperatorName)
            }

            override fun onSeeMoreProduct(itemProduct: TelcoProductDataCollection) {
                topupAnalytics.eventClickSeeMore(itemProduct.product.attributes.categoryId)

                activity?.let {
                    val seeMoreBottomSheet = DigitalProductBottomSheet.newInstance(
                            itemProduct.product.attributes.info,
                            MethodChecker.fromHtml(itemProduct.product.attributes.detail).toString(),
                            itemProduct.product.attributes.price)
                    seeMoreBottomSheet.setDismissListener {
                        topupAnalytics.eventCloseDetailProduct(itemProduct.product.attributes.categoryId)
                    }
                    seeMoreBottomSheet.show(it.supportFragmentManager, "")
                }
            }

            override fun onTrackImpressionProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>) {
                topupAnalytics.impressionEnhanceCommerceProduct(digitalTrackProductTelcoList)
            }
        })
    }

    fun onSuccessProductList(productData: TelcoProductComponentData) {
        emptyStateProductView.visibility = View.GONE
        telcoTelcoProductView.visibility = View.VISIBLE
        var position = -1
        if (selectedProductId.isNotEmpty()) {
            for (i in 0 until productData.rechargeProductData.productDataCollections.size) {
                if (productData.rechargeProductData.productDataCollections[i].product.id == selectedProductId) {
                    productData.rechargeProductData.productDataCollections[i].product.attributes.selected = true
                    position = i
                }
            }
        }
        telcoTelcoProductView.renderProductList(productData.productType,
                productData.rechargeProductData.productDataCollections, position)
    }

    fun onErrorProductList(throwable: Throwable) {
        titleEmptyState.text = getString(R.string.title_telco_product_empty_state, titleProduct.toLowerCase())
        descEmptyState.text = getString(R.string.desc_telco_product_empty_state,
                titleProduct.toLowerCase(), titleProduct.toLowerCase())
        emptyStateProductView.visibility = View.VISIBLE
        telcoTelcoProductView.visibility = View.GONE
    }

    fun onLoadingProductList(showLoading: Boolean) {
        emptyStateProductView.visibility = View.GONE
        telcoTelcoProductView.visibility = View.GONE
        if (showLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    companion object {

        val PRODUCT_TYPE = "product_type"
        val COMPONENT_TYPE = "component_type"
        val COMPONENT_NAME = "component_name"
        val OPERATOR_ID = "operator_Id"
        val SELECTED_PRODUCT_ID = "selected_product_id"
        val OPERATOR_NAME = "operator_name"

        val KEY_COMPONENT_ID = "componentID"
        val KEY_OPERATOR_ID = "operatorID"

        fun newInstance(componentType: Int, componentName: String, operatorId: String,
                        operatorName: String, productType: Int,
                        selectedProductId: String): Fragment {
            val fragment = DigitalTelcoProductFragment()
            val bundle = Bundle()
            bundle.putInt(PRODUCT_TYPE, productType)
            bundle.putInt(COMPONENT_TYPE, componentType)
            bundle.putString(OPERATOR_ID, operatorId)
            bundle.putString(COMPONENT_NAME, componentName)
            bundle.putString(SELECTED_PRODUCT_ID, selectedProductId)
            bundle.putString(OPERATOR_NAME, operatorName)
            fragment.arguments = bundle
            return fragment
        }
    }
}