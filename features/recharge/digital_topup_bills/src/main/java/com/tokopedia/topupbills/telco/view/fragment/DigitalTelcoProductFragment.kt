package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.DigitalTopupAnalytics
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection
import com.tokopedia.topupbills.telco.view.bottomsheet.DigitalProductBottomSheet
import com.tokopedia.topupbills.telco.view.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.view.model.DigitalTrackProductTelco
import com.tokopedia.topupbills.telco.view.viewmodel.DigitalTelcoProductViewModel
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoProductWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductFragment : BaseDaggerFragment() {

    private lateinit var telcoTelcoProductView: DigitalTelcoProductWidget
    private lateinit var emptyStateProductView: RelativeLayout
    private lateinit var titleEmptyState: TextView
    private lateinit var descEmptyState: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedModel: SharedProductTelcoViewModel
    private lateinit var productViewModel: DigitalTelcoProductViewModel
    private lateinit var selectedOperatorName: String

    private var titleProduct: String = ""
    private var selectedProductId: Int = 0

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
        getComponent(DigitalTopupComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_product, container, false)
        telcoTelcoProductView = view.findViewById(R.id.telco_product_view)
        emptyStateProductView = view.findViewById(com.tokopedia.common.topupbills.R.id.telco_empty_state_layout)
        titleEmptyState = view.findViewById(com.tokopedia.common.topupbills.R.id.title_empty_product)
        descEmptyState = view.findViewById(com.tokopedia.common.topupbills.R.id.desc_empty_product)
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            titleProduct = it.getString(COMPONENT_NAME)
            selectedProductId = it.getInt(SELECTED_PRODUCT_ID)
            selectedOperatorName = it.getString(OPERATOR_NAME)

            sharedModel.productList.observe(this, Observer {
                when (it) {
                    is Success -> onSuccessProductList()
                    is Fail -> onErrorProductList()
                }
            })

            sharedModel.loadingProductList.observe(this, Observer {
                onLoadingProductList(it)
            })

        }

        telcoTelcoProductView.setListener(object : DigitalTelcoProductWidget.ActionListener {
            override fun onClickProduct(itemProduct: TelcoProductDataCollection, position: Int) {
                sharedModel.setProductSelected(itemProduct)
                sharedModel.setShowTotalPrice(true)
                if (::selectedOperatorName.isInitialized) {
                    topupAnalytics.clickEnhanceCommerceProduct(itemProduct, position, selectedOperatorName)
                }
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

    private fun onSuccessProductList() {
        val productDataList = (sharedModel.productList.value as Success).data
        emptyStateProductView.visibility = View.GONE
        telcoTelcoProductView.visibility = View.VISIBLE
        productDataList.map {
            if (it.key == titleProduct) {
                if (it.value.rechargeProductData.productDataCollections.isNotEmpty()) {
                    var position = -1
                    if (selectedProductId > 0) {
                        for (i in it.value.rechargeProductData.productDataCollections.indices) {
                            if (it.value.rechargeProductData.productDataCollections[i].product.id == selectedProductId.toString()) {
                                it.value.rechargeProductData.productDataCollections[i].product.attributes.selected = true
                                position = i
                            }
                        }
                    }
                    telcoTelcoProductView.renderProductList(it.value.productType,
                            it.value.rechargeProductData.productDataCollections, position)
                } else {
                    onErrorProductList()
                }
            }
        }
    }

    private fun onErrorProductList() {
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

        val COMPONENT_NAME = "component_name"
        val SELECTED_PRODUCT_ID = "selected_product_id"
        val OPERATOR_NAME = "operator_name"

        val KEY_COMPONENT_ID = "componentID"
        val KEY_OPERATOR_ID = "operatorID"

        fun newInstance(componentName: String, operatorName: String, selectedProductId: Int): Fragment {
            val fragment = DigitalTelcoProductFragment()
            val bundle = Bundle()
            bundle.putString(COMPONENT_NAME, componentName)
            bundle.putInt(SELECTED_PRODUCT_ID, selectedProductId)
            bundle.putString(OPERATOR_NAME, operatorName)
            fragment.arguments = bundle
            return fragment
        }
    }
}