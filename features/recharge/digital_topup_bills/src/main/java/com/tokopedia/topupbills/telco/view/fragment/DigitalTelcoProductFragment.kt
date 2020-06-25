package com.tokopedia.topupbills.telco.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.DigitalTopupAnalytics
import com.tokopedia.topupbills.telco.data.TelcoCatalogProductInput
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.view.bottomsheet.DigitalProductBottomSheet
import com.tokopedia.topupbills.telco.view.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.view.model.DigitalTrackProductTelco
import com.tokopedia.topupbills.telco.view.viewmodel.SharedTelcoPrepaidViewModel
import com.tokopedia.topupbills.telco.view.widget.DigitalTelcoProductWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductFragment : BaseDaggerFragment() {

    private lateinit var telcoTelcoProductView: DigitalTelcoProductWidget
    private lateinit var emptyStateProductView: ConstraintLayout
    private lateinit var titleEmptyState: TextView
    private lateinit var descEmptyState: TextView
    private lateinit var sharedModelPrepaid: SharedTelcoPrepaidViewModel
    private lateinit var selectedOperatorName: String
    private lateinit var shimmeringGridLayout: LinearLayout
    private lateinit var shimmeringListLayout: LinearLayout

    private var titleProduct: String = ""
    private var selectedProductId: Int = 0
    private var hasTitle = false
    private var productType = TelcoProductType.PRODUCT_LIST

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics
    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            sharedModelPrepaid = viewModelProvider.get(SharedTelcoPrepaidViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedModelPrepaid.productCatalogItem.observe(this, Observer {
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
            productType = it.getInt(PRODUCT_TYPE)
            selectedProductId = it.getInt(SELECTED_PRODUCT_ID)
            selectedOperatorName = it.getString(OPERATOR_NAME)

            showShimmering()
            sharedModelPrepaid.productList.observe(this, Observer {
                when (it) {
                    is Success -> onSuccessProductList()
                    is Fail -> onErrorProductList()
                }
            })
        }

        telcoTelcoProductView.setListener(object : DigitalTelcoProductWidget.ActionListener {
            override fun onClickProduct(itemProduct: TelcoProduct, position: Int, labelList: String) {
                sharedModelPrepaid.setProductCatalogSelected(itemProduct)
                sharedModelPrepaid.setShowTotalPrice(true)
                if (::selectedOperatorName.isInitialized) {
                    topupAnalytics.clickEnhanceCommerceProduct(itemProduct, position, selectedOperatorName,
                            userSession.userId, labelList)
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
                    seeMoreBottomSheet.setListener(object : DigitalProductBottomSheet.ActionListener {
                        override fun onClickOnProduct() {
                            activity?.run {
                                sharedModelPrepaid.setProductCatalogSelected(itemProduct)
                                telcoTelcoProductView.selectProductItem(itemProduct)
                                topupAnalytics.impressionPickProductDetail(itemProduct, selectedOperatorName, userSession.userId)
                            }
                        }
                    })
                    seeMoreBottomSheet.show(it.supportFragmentManager, "")

                }
            }

            override fun onTrackImpressionProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>) {
                topupAnalytics.impressionEnhanceCommerceProduct(digitalTrackProductTelcoList, selectedOperatorName,
                        userSession.userId)
            }
        })
    }

    private fun onSuccessProductList() {
        hideShimmering()
        val productInputList = (sharedModelPrepaid.productList.value as Success).data

        productInputList.map {
            if (it.label == titleProduct) {
                if (it.product.dataCollections.isNotEmpty()) {
                    emptyStateProductView.hide()
                    telcoTelcoProductView.show()

                    val dataCollections = wrapDataCollections(it)
                    if (hasTitle) {
                        setMarginProductList(MARGIN_0,  telcoTelcoProductView)
                    } else {
                        setMarginProductList(MARGIN_18, telcoTelcoProductView)
                    }

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
                } else {
                    onErrorProductList()
                }
            }
        }
    }

    private fun setMarginProductList(spaceTop: Int, view: View) {
        val params =  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT)
        params.setMargins(MARGIN_0, spaceTop, MARGIN_0, MARGIN_16)
        view.layoutParams = params
    }

    private fun wrapDataCollections(productInput: TelcoCatalogProductInput): List<TelcoProduct> {
        val dataCollections = mutableListOf<TelcoProduct>()
        hasTitle = productInput.product.dataCollections.size > 1 && productInput.label != TelcoComponentName.PRODUCT_PULSA
        productInput.product.dataCollections.map {
            if (hasTitle) {
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
        hideShimmering()
        titleEmptyState.text = getString(R.string.title_telco_product_empty_state, titleProduct)
        descEmptyState.text = getString(R.string.desc_telco_product_empty_state, titleProduct)
        emptyStateProductView.show()
        telcoTelcoProductView.hide()
    }

    private fun showShimmering() {
        emptyStateProductView.hide()
        telcoTelcoProductView.hide()
        arguments?.let {
            titleProduct = it.getString(TITLE_PAGE)
            if (titleProduct == TelcoComponentName.PRODUCT_PULSA) {
                shimmeringGridLayout.show()
            } else {
                shimmeringListLayout.show()
            }
        }
    }

    private fun hideShimmering() {
        shimmeringGridLayout.hide()
        shimmeringListLayout.hide()
    }

    override fun onDestroy() {
        sharedModelPrepaid.flush()
        super.onDestroy()
    }

    companion object {

        const val PRODUCT_TYPE = "product_type"
        const val TITLE_PAGE = "title_page"
        const val SELECTED_PRODUCT_ID = "selected_product_id"
        const val OPERATOR_NAME = "operator_name"

        const val MARGIN_18 = 18
        const val MARGIN_16 = 16
        const val MARGIN_0 = 0

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