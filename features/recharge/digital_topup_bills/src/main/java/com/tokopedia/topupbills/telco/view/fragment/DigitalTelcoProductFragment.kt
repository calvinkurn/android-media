package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.DigitalTopupAnalytics
import com.tokopedia.topupbills.telco.data.TelcoCatalogProductInput
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.view.bottomsheet.DigitalProductBottomSheet
import com.tokopedia.topupbills.telco.view.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.view.model.DigitalTrackProductTelco
import com.tokopedia.topupbills.telco.view.viewmodel.SharedProductTelcoViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoProductWidget
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductFragment : BaseDaggerFragment() {

    private lateinit var telcoTelcoProductView: DigitalTelcoProductWidget
    private lateinit var emptyStateProductView: ConstraintLayout
    private lateinit var titleEmptyState: TextView
    private lateinit var descEmptyState: TextView
    private lateinit var sharedModel: SharedProductTelcoViewModel
    private lateinit var selectedOperatorName: String
    private lateinit var shimmeringGridLayout: LinearLayout
    private lateinit var shimmeringListLayout: LinearLayout

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
            sharedModel = viewModelProvider.get(SharedProductTelcoViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModel.productCatalogItem.observe(this, Observer {
            it?.run {
                telcoTelcoProductView.notifyProductItemChanges(it.id)
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
        emptyStateProductView = view.findViewById(R.id.telco_empty_state_layout)
        titleEmptyState = view.findViewById(R.id.title_empty_product)
        descEmptyState = view.findViewById(R.id.desc_empty_product)
        shimmeringGridLayout = view.findViewById(R.id.shimmering_product_grid)
        shimmeringListLayout = view.findViewById(R.id.shimmering_product_list)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            titleProduct = it.getString(TITLE_PAGE)
            val productType = it.getInt(PRODUCT_TYPE)
            selectedProductId = it.getInt(SELECTED_PRODUCT_ID)
            selectedOperatorName = it.getString(OPERATOR_NAME)

            sharedModel.productList.observe(this, Observer { productList ->
                productList.map { productInput ->
                    if (productInput.label == titleProduct) {
                        onSuccessProductList(productInput, productType)
                    }
                }
            })

            sharedModel.loadingProductList.observe(this, Observer { loadingShow ->
                if (loadingShow) showShimmering()
                else hideShimmering()
            })

            sharedModel.errorProductList.observe(this, Observer {
                onErrorProductList()
            })
        }

        telcoTelcoProductView.setListener(object : DigitalTelcoProductWidget.ActionListener {
            override fun onClickProduct(itemProduct: TelcoProduct, position: Int) {
                sharedModel.setProductCatalogSelected(itemProduct)
                sharedModel.setShowTotalPrice(true)
                if (::selectedOperatorName.isInitialized) {
                    topupAnalytics.clickEnhanceCommerceProduct(itemProduct, position, selectedOperatorName)
                }
            }

            override fun onSeeMoreProduct(itemProduct: TelcoProduct) {
                topupAnalytics.eventClickSeeMore(itemProduct.attributes.categoryId)

                activity?.let {
                    val seeMoreBottomSheet = DigitalProductBottomSheet.newInstance(
                            itemProduct.attributes.desc,
                            MethodChecker.fromHtml(itemProduct.attributes.detail).toString(),
                            itemProduct.attributes.price)
                    seeMoreBottomSheet.setOnDismissListener {
                        topupAnalytics.eventCloseDetailProduct(itemProduct.attributes.categoryId)
                    }
                    seeMoreBottomSheet.show(it.supportFragmentManager, "")

                }
            }

            override fun onTrackImpressionProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>) {
                topupAnalytics.impressionEnhanceCommerceProduct(digitalTrackProductTelcoList)
            }
        })
    }

    private fun onSuccessProductList(productInput: TelcoCatalogProductInput, productType: Int) {
        emptyStateProductView.visibility = View.GONE
        telcoTelcoProductView.visibility = View.VISIBLE

        val dataCollections = wrapDataCollections(productInput)

        //set true on selected product datacollection list
        var position = -1
        if (selectedProductId > 0) {
            for (i in dataCollections.indices) {
                if (dataCollections[i].id == selectedProductId.toString()) {
                    dataCollections[i].attributes.selected = true
                    position = i
                }
            }
        }
        telcoTelcoProductView.renderProductList(productType, dataCollections, position)
    }

    private fun wrapDataCollections(productInput: TelcoCatalogProductInput): List<TelcoProduct> {
        val dataCollections = mutableListOf<TelcoProduct>()
        productInput.product.dataCollections.map {
            if (productInput.product.dataCollections.size > 1 && productInput.label != TelcoComponentName.PRODUCT_PULSA) {
                if (it.name.isNotEmpty()) {
                    dataCollections.add(TelcoProduct(titleSection = it.name, isTitle = true))
                } else {
                    dataCollections.add(TelcoProduct(titleSection = getString(R.string.telco_other_recommendation), isTitle = true))
                }
            }
            dataCollections.addAll(it.products)
        }
        return dataCollections;
    }

    private fun onErrorProductList() {
        titleEmptyState.text = getString(R.string.title_telco_product_empty_state, titleProduct)
        descEmptyState.text = getString(R.string.desc_telco_product_empty_state, titleProduct)
        emptyStateProductView.visibility = View.VISIBLE
        telcoTelcoProductView.visibility = View.GONE
    }

    private fun showShimmering() {
        emptyStateProductView.visibility = View.GONE
        telcoTelcoProductView.visibility = View.GONE
        arguments?.let {
            titleProduct = it.getString(TITLE_PAGE)
            if (titleProduct == TelcoComponentName.PRODUCT_PULSA) {
                shimmeringGridLayout.visibility = View.VISIBLE
            } else {
                shimmeringListLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun hideShimmering() {
        shimmeringGridLayout.visibility = View.GONE
        shimmeringListLayout.visibility = View.GONE
    }

    companion object {

        val PRODUCT_TYPE = "product_type"
        val TITLE_PAGE = "title_page"
        val SELECTED_PRODUCT_ID = "selected_product_id"
        val OPERATOR_NAME = "operator_name"

        fun newInstance(titlePage: String, operatorName: String, productType: Int,
                        selectedProductId: Int): Fragment {
            val fragment = DigitalTelcoProductFragment()
            val bundle = Bundle()
            bundle.putString(TITLE_PAGE, titlePage)
            bundle.putInt(PRODUCT_TYPE, productType)
            bundle.putInt(SELECTED_PRODUCT_ID, selectedProductId)
            bundle.putString(OPERATOR_NAME, operatorName)
            fragment.arguments = bundle
            return fragment
        }
    }
}