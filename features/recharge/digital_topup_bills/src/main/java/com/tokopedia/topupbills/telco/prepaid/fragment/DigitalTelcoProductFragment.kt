package com.tokopedia.topupbills.telco.prepaid.fragment

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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.topupbills.data.constant.TelcoComponentName
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent
import com.tokopedia.topupbills.telco.data.FilterTagDataCollection
import com.tokopedia.topupbills.telco.data.TelcoFilterTagComponent
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.topupbills.telco.prepaid.bottomsheet.DigitalProductBottomSheet
import com.tokopedia.topupbills.telco.prepaid.bottomsheet.DigitalTelcoFilterBottomSheet
import com.tokopedia.topupbills.telco.prepaid.model.DigitalTrackProductTelco
import com.tokopedia.topupbills.telco.prepaid.model.TelcoFilterData
import com.tokopedia.topupbills.telco.prepaid.viewmodel.SharedTelcoPrepaidViewModel
import com.tokopedia.topupbills.telco.prepaid.widget.DigitalTelcoProductWidget
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductFragment : BaseDaggerFragment(), DigitalTelcoProductWidget.ActionListener {

    private lateinit var telcoTelcoProductView: DigitalTelcoProductWidget
    private lateinit var emptyStateProductView: ConstraintLayout
    private lateinit var titleEmptyState: TextView
    private lateinit var descEmptyState: TextView
    private lateinit var sharedModelPrepaid: SharedTelcoPrepaidViewModel
    private lateinit var selectedOperatorName: String
    private lateinit var shimmeringGridLayout: LinearLayout
    private lateinit var shimmeringListLayout: LinearLayout
    private lateinit var sortFilter: SortFilter
    private lateinit var titleFilterResult: TextView

    private var titleProduct: String = ""
    private var categoryId: Int = 0
    private var telcoFilterData: TelcoFilterData = TelcoFilterData()
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
            val viewModelProvider = ViewModelProvider(it, viewModelFactory)
            sharedModelPrepaid = viewModelProvider.get(SharedTelcoPrepaidViewModel::class.java)
        }
    }

    override fun getScreenName(): String {
        return DigitalTelcoProductFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(DigitalTelcoComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_digital_telco_product, container, false)
        telcoTelcoProductView = view.findViewById(R.id.telco_product_view)
        emptyStateProductView = view.findViewById(R.id.telco_empty_state_layout)
        titleEmptyState = view.findViewById(R.id.telco_title_empty_product)
        descEmptyState = view.findViewById(R.id.telco_desc_empty_product)
        shimmeringGridLayout = view.findViewById(R.id.telco_shimmering_product_grid)
        shimmeringListLayout = view.findViewById(R.id.telco_shimmering_product_list)
        sortFilter = view.findViewById(R.id.telco_sort_filter)
        titleFilterResult = view.findViewById(R.id.telco_title_filter_result)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { it ->
            titleProduct = it.getString(TITLE_PAGE) ?: ""
            productType = it.getInt(PRODUCT_TYPE)
            selectedOperatorName = it.getString(OPERATOR_NAME) ?: ""
            categoryId = it.getInt(CATEGORY_ID)

            sharedModelPrepaid.productList.observe(viewLifecycleOwner, Observer {
                clearFilterIfTabNotActive()
                if (telcoFilterData.isFilterSelected()) titleFilterResult.show() else titleFilterResult.hide()
                hideShimmering()
                when (it) {
                    is Success -> onSuccessProductList()
                    is Fail -> onErrorProductList(it.throwable)
                }
            })

            sharedModelPrepaid.productCatalogItem.observe(viewLifecycleOwner, Observer {
                if (it.id == DigitalTelcoPrepaidFragment.ID_PRODUCT_EMPTY) {
                    telcoTelcoProductView.resetSelectedProductItem()
                }
            })

            sharedModelPrepaid.loadingProductList.observe(viewLifecycleOwner, Observer {
                if (it) {
                    showShimmering()
                    titleFilterResult.hide()
                } else {
                    hideShimmering()
                }
            })

            sharedModelPrepaid.positionScrollItem.observe(viewLifecycleOwner, Observer {
                telcoTelcoProductView.scrollToPosition(it)
            })

            sharedModelPrepaid.selectedCategoryViewPager.observe(viewLifecycleOwner, Observer {
                if (sharedModelPrepaid.productList.value is Success) {
                    val productList = (sharedModelPrepaid.productList.value as Success).data
                    productList.map { list ->
                        if (list.label == titleProduct && it == titleProduct && list.product.dataCollections.isNotEmpty()) {
                            telcoTelcoProductView.calculateProductItemVisibleItemTracking(telcoTelcoProductView.adapter.data)
                            telcoTelcoProductView.trackFirstMccmVisibleItemToUser()

                            if (list.filterTagComponents.isNotEmpty()) {
                                topupAnalytics.impressionFilterCluster(categoryId, userSession.userId)
                            }
                        }
                    }
                }
            })

            sharedModelPrepaid.selectedProductById.observe(viewLifecycleOwner, {
                val activeCategory = sharedModelPrepaid.selectedCategoryViewPager.value
                if (activeCategory == titleProduct) {
                    telcoTelcoProductView.selectProductById(it.toString())
                }
            })
        }

        telcoTelcoProductView.setListener(this)
    }

    override fun onClickProduct(itemProduct: TelcoProduct, position: Int, labelList: String) {
        sharedModelPrepaid.setProductCatalogSelected(itemProduct)
        sharedModelPrepaid.setInputWidgetFocus(false)
        telcoTelcoProductView.selectProductItem(position)
        if (::selectedOperatorName.isInitialized) {
            topupAnalytics.clickEnhanceCommerceProduct(itemProduct, position, selectedOperatorName,
                    userSession.userId, labelList)
            if (itemProduct.isSpecialProductPromo())
                topupAnalytics.clickSpecialPromoProduct(itemProduct, position,
                        selectedOperatorName, userSession.userId)

        }
    }

    override fun onClickMccmProduct(itemProduct: TelcoProduct, position: Int) {
        topupAnalytics.clickOnMccmProduct(itemProduct, selectedOperatorName,
                position, userSession.userId, titleProduct)

        sharedModelPrepaid.setProductCatalogSelected(itemProduct)
        sharedModelPrepaid.setInputWidgetFocus(false)
        telcoTelcoProductView.selectMccmProductItem(position)
    }

    override fun onSeeMoreProduct(itemProduct: TelcoProduct, position: Int) {
        topupAnalytics.eventClickSeeMore(itemProduct.attributes.categoryId)
        renderSeeMoreBottomSheet(itemProduct, object : DigitalProductBottomSheet.ActionListener {
            override fun onClickOnProduct(isSpecialProduct: Boolean) {
                activity?.run {
                    telcoTelcoProductView.selectProductItem(position)
                    sharedModelPrepaid.setProductCatalogSelected(itemProduct)
                    sharedModelPrepaid.setProductAutoCheckout(itemProduct)
                    sharedModelPrepaid.setInputWidgetFocus(false)
                    topupAnalytics.pickProductDetail(itemProduct, selectedOperatorName,
                            userSession.userId, isSpecialProduct)
                }
            }
        })
    }

    override fun onSeeMoreMccmProduct(itemProduct: TelcoProduct, position: Int) {
        topupAnalytics.clickSeeMoreOnMccmProductItem(titleProduct, selectedOperatorName, userSession.userId)
        renderSeeMoreBottomSheet(itemProduct, object : DigitalProductBottomSheet.ActionListener {
            override fun onClickOnProduct(isSpecialProduct: Boolean) {
                activity?.run {
                    telcoTelcoProductView.selectMccmProductItem(position)
                    sharedModelPrepaid.setProductCatalogSelected(itemProduct)
                    sharedModelPrepaid.setProductAutoCheckout(itemProduct)
                    sharedModelPrepaid.setInputWidgetFocus(false)
                    topupAnalytics.pickProductDetail(itemProduct, selectedOperatorName, userSession.userId,
                            isSpecialProduct, TelcoComponentName.SPECIAL_PROMO_MCCM)
                }
            }
        })
    }

    override fun onTrackImpressionProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>) {
        topupAnalytics.impressionEnhanceCommerceProduct(digitalTrackProductTelcoList, selectedOperatorName,
                userSession.userId)
    }

    override fun onTrackImpressionMccmProductsList(digitalTrackProductTelcoList: List<DigitalTrackProductTelco>) {
        topupAnalytics.impressionViewMccmProduct(digitalTrackProductTelcoList, selectedOperatorName, userSession.userId, titleProduct)
    }

    override fun onScrollToPositionItem(position: Int) {
        sharedModelPrepaid.setPositionScrollToItem(position)
    }

    override fun onFailedAutoSelectById() {
        sharedModelPrepaid.resetSelectedProduct()
    }

    private fun renderSeeMoreBottomSheet(itemProduct: TelcoProduct, bottomSheetListener: DigitalProductBottomSheet.ActionListener) {
        activity?.let {
            val seeMoreBottomSheet = DigitalProductBottomSheet.newInstance(
                    itemProduct.attributes.desc,
                    MethodChecker.fromHtml(itemProduct.attributes.detail).toString(),
                    itemProduct.attributes.price,
                    itemProduct.attributes.productPromo?.newPrice,
                    itemProduct.isSpecialProductPromo(),
                    bottomSheetListener
            )
            seeMoreBottomSheet.setOnDismissListener {
                topupAnalytics.eventCloseDetailProduct(itemProduct.attributes.categoryId)
            }
            seeMoreBottomSheet.show(it.supportFragmentManager, "bottom_sheet_product_telco")
        }
    }

    override fun onTrackImpressionSpecialProduct(itemProduct: TelcoProduct, position: Int) {
        topupAnalytics.impressionSpecialPromoProduct(itemProduct, position,
                selectedOperatorName, userSession.userId)
    }

    private fun renderSortFilter(componentId: Int, filters: List<TelcoFilterTagComponent>) {
        if (filters.isEmpty()) {
            sortFilter.hide()
            telcoTelcoProductView.removePaddingTop(false)
        } else {
            sortFilter.show()
            telcoTelcoProductView.removePaddingTop(true)
        }

        if (telcoFilterData.getFilterTags().isEmpty()) {
            telcoFilterData.setFilterTags(filters)

            val filterData = arrayListOf<SortFilterItem>()
            telcoFilterData.getFilterTags().map { filterTag ->
                val sortFilterItem = SortFilterItem(filterTag.text)
                sortFilterItem.listener = { showBottomSheetFilter(filterTag, componentId, sortFilterItem) }
                filterData.add(sortFilterItem)
            }
            sortFilter.addItem(filterData)
            sortFilter.chipItems?.map {
                it.refChipUnify.setChevronClickListener {}
            }
            sortFilter.filterType = SortFilter.TYPE_QUICK
            sortFilter.filterRelationship = SortFilter.RELATIONSHIP_AND
            sortFilter.dismissListener = {
                telcoFilterData.clearAllFilter()
                sharedModelPrepaid.setSelectedFilter(telcoFilterData.getAllFilter())
                topupAnalytics.eventClickResetFilterCluster(categoryId, userSession.userId)
            }

            topupAnalytics.impressionFilterCluster(categoryId, userSession.userId)
        }
    }

    private fun showBottomSheetFilter(filterTag: TelcoFilterTagComponent, componentId: Int,
                                      sortFilterItem: SortFilterItem) {
        topupAnalytics.eventClickQuickFilter(categoryId, filterTag.text, userSession.userId)
        val filterBottomSheet = DigitalTelcoFilterBottomSheet.newInstance(filterTag.text,
                filterTag.paramName, filterTag.filterTagDataCollections as ArrayList<FilterTagDataCollection>)
        filterBottomSheet.setListener(object : DigitalTelcoFilterBottomSheet.ActionListener {
            override fun onTelcoFilterSaved(keysFilter: ArrayList<String>, valuesFilter: String) {
                topupAnalytics.eventClickSaveFilter(categoryId, filterTag.text, valuesFilter, userSession.userId)

                telcoFilterData.addFilter(componentId, filterTag.paramName, keysFilter)
                telcoTelcoProductView.resetSelectedProductItem()
                sharedModelPrepaid.setVisibilityTotalPrice(false)
                sharedModelPrepaid.setSelectedFilter(telcoFilterData.getAllFilter())

                if (telcoFilterData.isFilterSelectedByParamName(filterTag.paramName)) {
                    sortFilterItem.type = ChipsUnify.TYPE_SELECTED
                } else {
                    sortFilterItem.type = ChipsUnify.TYPE_NORMAL
                }
            }

            override fun getFilterSelected(): ArrayList<String> {
                return telcoFilterData.getFilterSelectedByParamName(filterTag.paramName)
            }

            override fun resetFilter() {
                topupAnalytics.eventClickResetFilter(categoryId, filterTag.text, userSession.userId)
            }
        })
        filterBottomSheet.setShowListener { filterBottomSheet.bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED }
        filterBottomSheet.show(childFragmentManager, "filter telco")
    }

    private fun onSuccessProductList() {
        val productInputList = (sharedModelPrepaid.productList.value as Success).data

        productInputList.map {
            if (it.label == titleProduct) {
                if (it.product.dataCollections.isNotEmpty() && it.product.dataCollections[0].products.isNotEmpty()) {
                    emptyStateProductView.hide()
                    telcoTelcoProductView.show()

                    val showTitle = !telcoFilterData.isFilterSelected()

                    renderSortFilter(it.product.id, it.filterTagComponents)
                    telcoTelcoProductView.renderProductList(productType, showTitle,
                            it.product.dataCollections, sharedModelPrepaid.selectedCategoryViewPager.value == titleProduct)
                } else {
                    onErrorProductList()
                }
            } else {
                //do nothing
            }
        }
    }

    private fun clearFilterIfTabNotActive() {
        if (sharedModelPrepaid.selectedCategoryViewPager.value != null &&
                sharedModelPrepaid.selectedCategoryViewPager.value != titleProduct) {
            telcoFilterData.clearAllFilter()
            sortFilter.chipItems?.map {
                it.type = ChipsUnify.TYPE_NORMAL
            }
        }
    }

    private fun onErrorProductList(error: Throwable? = null) {
        titleEmptyState.text = getString(R.string.title_telco_product_empty_state, titleProduct)
        descEmptyState.text = getString(R.string.desc_telco_product_empty_state, titleProduct)
        emptyStateProductView.show()
        telcoTelcoProductView.hide()
        error?.let {
            val (errorMessage, _) = ErrorHandler.getErrorMessagePair(
                requireContext(),
                error,
                ErrorHandler.Builder()
                    .className(this::class.java.simpleName)
                    .build()
            )
            if (!it.message.isNullOrEmpty()) {
                Toaster.build(requireView(), errorMessage.orEmpty(), Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR).show()
            }
        }
    }

    private fun showShimmering() {
        emptyStateProductView.hide()
        telcoTelcoProductView.hide()
        arguments?.let {
            titleProduct = it.getString(TITLE_PAGE) ?: ""
            if (titleProduct == TelcoComponentName.PRODUCT_PULSA) {
                shimmeringGridLayout.show()
            } else {
                shimmeringListLayout.show()
            }
        }
    }

    override fun onDestroy() {
        sortFilter.removeAllViews()
        super.onDestroy()
    }

    private fun hideShimmering() {
        shimmeringGridLayout.hide()
        shimmeringListLayout.hide()
    }

    companion object {

        const val PRODUCT_TYPE = "product_type"
        const val TITLE_PAGE = "title_page"
        const val OPERATOR_NAME = "operator_name"
        const val CATEGORY_ID = "category_id"

        fun newInstance(bundle: Bundle): Fragment {
            val fragment = DigitalTelcoProductFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}