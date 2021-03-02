package com.tokopedia.product.addedit.variant.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_DETAIL_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_DETAIL_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_DETAIL_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_DETAIL_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.tracking.ProductAddVariantDetailTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantDetailTracking
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailFieldsAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailInputTypeFactoryImpl
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantDetailFieldsViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantDetailHeaderViewHolder
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_TRACKER_OFF
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_TRACKER_ON
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.dialog.MultipleVariantEditSelectBottomSheet
import com.tokopedia.product.addedit.variant.presentation.dialog.SelectVariantMainBottomSheet
import com.tokopedia.product.addedit.variant.presentation.model.*
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantDetailViewModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_add_edit_product_variant_detail.*
import java.math.BigInteger
import javax.inject.Inject

class AddEditProductVariantDetailFragment : BaseDaggerFragment(),
        VariantDetailHeaderViewHolder.OnCollapsibleHeaderClickListener,
        VariantDetailFieldsViewHolder.OnStatusSwitchCheckedChangeListener,
        VariantDetailFieldsViewHolder.OnPriceInputTextChangedListener,
        VariantDetailFieldsViewHolder.OnStockInputTextChangedListener,
        MultipleVariantEditSelectBottomSheet.MultipleVariantEditListener,
        SelectVariantMainBottomSheet.SelectVariantMainListener,
        VariantDetailFieldsViewHolder.OnSkuInputTextChangedListener,
        AddEditProductPerformanceMonitoringListener
{

    companion object {
        fun createInstance(cacheManagerId: String): Fragment {
            return AddEditProductVariantDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: AddEditProductVariantDetailViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var variantDetailFieldsAdapter: VariantDetailFieldsAdapter? = null
    // PLT Monitoring
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    private var multiLocationTicker: Ticker? = null

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(AddEditProductVariantComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // start PLT Monitoring
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)

        val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

        cacheManagerId?.run {
            val productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL,
                    ProductInputModel::class.java) ?: ProductInputModel()
            viewModel.updateProductInputModel(productInputModel)
        }

        activity?.window?.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_variant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendTrackerTrackScreenData()

        // set bg color programatically, to reduce overdraw
        context?.let { activity?.window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0)) }

        viewModel.setupMultiLocationValue()

        multiLocationTicker = view.findViewById(R.id.ticker_add_edit_variant_multi_location)
        multiLocationTicker?.showWithCondition(viewModel.isMultiLocationShop)

        val multipleVariantEditSelectBottomSheet = MultipleVariantEditSelectBottomSheet(this, viewModel.isMultiLocationShop)
        val variantInputModel = viewModel.productInputModel.value?.variantInputModel
        multipleVariantEditSelectBottomSheet.setData(variantInputModel)

        variantDetailFieldsAdapter = VariantDetailFieldsAdapter(VariantDetailInputTypeFactoryImpl(
                this,
                this,
                this,
                this,
                this))
        recyclerViewVariantDetailFields.adapter = variantDetailFieldsAdapter
        recyclerViewVariantDetailFields.layoutManager = LinearLayoutManager(context)

        switchUnifySku.setOnClickListener {
            val isChecked = switchUnifySku.isChecked
            viewModel.updateSkuVisibilityStatus(isVisible = isChecked)
            sendTrackerClickSKUToggleData(isChecked)
        }

        imageViewMultipleEdit.setOnClickListener {
            showMultipleEditBottomSheet()
        }

        variantListButton.setOnClickListener {
            showSelectPrimaryBottomSheet()
        }

        buttonSave.setOnClickListener {
            submitVariantInput()
            sendTrackerSaveVariantDetailData()
        }

        observeSelectedVariantSize()
        observeInputStatus()
        observeHasWholesale()

        enableSku()
        setupToolbarActions()

        // stop PLT monitoring when all view prepared/ no network call
        stopPreparePagePerformanceMonitoring()
        stopPerformanceMonitoring()
    }

    override fun onHeaderClicked(headerPosition: Int): Boolean {
        val currentHeaderPosition = viewModel.getCurrentHeaderPosition(headerPosition)
        val isCollapsed = viewModel.isVariantDetailHeaderCollapsed(headerPosition)
        if (!isCollapsed) {
            variantDetailFieldsAdapter?.collapseUnitValueHeader(currentHeaderPosition, viewModel.getInputFieldSize())
            viewModel.increaseCollapsedFields(viewModel.getInputFieldSize())
            viewModel.updateVariantDetailHeaderMap(headerPosition, true)
            viewModel.collapseHeader(headerPosition, currentHeaderPosition)
        } else {
            variantDetailFieldsAdapter?.expandDetailFields(currentHeaderPosition, viewModel.getVariantDetailHeaderData(headerPosition))
            val layoutManager: LinearLayoutManager = recyclerViewVariantDetailFields?.layoutManager as LinearLayoutManager
            layoutManager.scrollToPositionWithOffset(currentHeaderPosition, 0)
            viewModel.decreaseCollapsedFields(viewModel.getInputFieldSize())
            viewModel.updateVariantDetailHeaderMap(headerPosition, false)
            viewModel.expandHeader(headerPosition, currentHeaderPosition)
        }
        return viewModel.isVariantDetailHeaderCollapsed(headerPosition)
    }

    override fun onCheckedChanged(isChecked: Boolean, adapterPosition: Int) {
        val updatedInputModel = viewModel.updateSwitchStatus(isChecked, adapterPosition)
        viewModel.editVariantDetailInputMap(adapterPosition, updatedInputModel)

        // tracking
        sendClickVariantStatusToggleData(isChecked)
    }

    override fun onPriceInputTextChanged(priceInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val validatedInputModel = viewModel.validateVariantPriceInput(priceInput, adapterPosition)
        viewModel.editVariantDetailInputMap(adapterPosition, validatedInputModel)
        return validatedInputModel
    }

    override fun onStockInputTextChanged(stockInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val validatedInputModel = viewModel.validateProductVariantStockInput(stockInput, adapterPosition)
        viewModel.editVariantDetailInputMap(adapterPosition, validatedInputModel)
        return validatedInputModel
    }

    override fun onSkuInputTextChanged(skuInput: String, adapterPosition: Int) {
        val updatedInputModel = viewModel.updateVariantSkuInput(skuInput, adapterPosition)
        viewModel.editVariantDetailInputMap(adapterPosition, updatedInputModel)
    }

    override fun onMultipleEditFinished(multipleVariantEditInputModel: MultipleVariantEditInputModel) {
        viewModel.updateProductInputModel(multipleVariantEditInputModel)
    }

    override fun onMultipleEditInputValidatePrice(price: BigInteger): String {
        return viewModel.validateVariantPriceInput(price)
    }

    override fun onMultipleEditInputValidateStock(stock: BigInteger): String {
        return viewModel.validateProductVariantStockInput(stock)
    }

    override fun onSelectVariantMainFinished(combination: List<Int>) {
        val updatedFieldPosition = viewModel.updatePrimaryVariant(combination)

        // update switch status if primary variant changed (index bigger than -1)
        if (updatedFieldPosition > -1) {
            val updatedInputModel = viewModel.updateSwitchStatus(true, updatedFieldPosition)
            variantDetailFieldsAdapter?.updateDetailInputField(updatedFieldPosition, updatedInputModel)
        }

        // tracking
        sendTrackerSaveMainVariant(combination)
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ADD_EDIT_PRODUCT_VARIANT_DETAIL_PLT_PREPARE_METRICS,
                ADD_EDIT_PRODUCT_VARIANT_DETAIL_PLT_NETWORK_METRICS,
                ADD_EDIT_PRODUCT_VARIANT_DETAIL_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(ADD_EDIT_PRODUCT_VARIANT_DETAIL_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    private fun observeSelectedVariantSize() {
        viewModel.selectedVariantSize.observe(viewLifecycleOwner, Observer { size ->
            // clear old elements before rendering new elements
            variantDetailFieldsAdapter?.clearAllElements()
            // reset the collapsed fields counter
            viewModel.resetCollapsedFields()
            // have 2 selected variant detail
            val hasVariantCombination = viewModel.hasVariantCombination(size)
            // with collapsible header
            if (hasVariantCombination) {
                val selectedVariantList = viewModel.productInputModel.value?.variantInputModel?.selections
                selectedVariantList?.run { setupVariantDetailCombinationFields(selectedVariantList) }
            } else {
                val selectedVariant = viewModel.productInputModel.value?.variantInputModel?.selections?.firstOrNull()
                val selectedUnitValues = selectedVariant?.options
                selectedUnitValues?.run { setupVariantDetailFields(selectedUnitValues) }
            }
        })
    }

    private fun observeHasWholesale() {
        viewModel.hasWholesale.observe(viewLifecycleOwner, Observer {
            variantDetailFieldsAdapter?.updatePriceEditingStatus(viewModel.getAvailableFields(), !it)
            tickerVariantWholesale.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun observeInputStatus() {
        viewModel.errorCounter.observe(viewLifecycleOwner, Observer {
            buttonSave.isEnabled = it.orZero() <= 0
        })
    }

    private fun enableSku() {
        switchUnifySku.isChecked = true
    }

    private fun setupVariantDetailFields(selectedUnitValues: List<OptionInputModel>) {
        // without variant unit values combination
        selectedUnitValues.forEachIndexed { productVariantIndex, unitValue ->
            val isSkuVisible = switchUnifySku.isChecked // get last visibility
            val variantDetailInputModel = viewModel.generateVariantDetailInputModel(
                    productVariantIndex, 0, unitValue.value, isSkuVisible)
            val fieldVisitablePosition = variantDetailFieldsAdapter?.addVariantDetailField(variantDetailInputModel)
            fieldVisitablePosition?.let { viewModel.updateVariantDetailInputMap(fieldVisitablePosition, variantDetailInputModel) }
        }
    }

    private fun setupVariantDetailCombinationFields(selectedVariants: List<SelectionInputModel>) {
        //increment for indexing product variant
        var productVariantIndex = 0
        // variant level 1 properties
        val selectedVariantLevel1 = selectedVariants[VARIANT_VALUE_LEVEL_ONE_POSITION]
        val unitValueLevel1 = selectedVariantLevel1.options
        // variant level 2 properties
        val selectedVariantLevel2 = selectedVariants[VARIANT_VALUE_LEVEL_TWO_POSITION]
        val unitValueLevel2 = selectedVariantLevel2.options
        // start rendering
        unitValueLevel1.forEach { level1Value ->
            // render collapsible header
            val headerVisitablePosition = variantDetailFieldsAdapter?.addUnitValueHeader(level1Value.value)
                    ?: 0
            viewModel.updateVariantDetailHeaderMap(headerVisitablePosition, false)
            // init header position - current header visitable position map values
            viewModel.updateCurrentHeaderPositionMap(headerVisitablePosition, headerVisitablePosition)
            // render variant unit value fields
            unitValueLevel2.forEach { level2Value ->
                val isSkuVisible = switchUnifySku.isChecked // get last visibility
                val variantDetailInputModel = viewModel.generateVariantDetailInputModel(
                        productVariantIndex, headerVisitablePosition, level2Value.value, isSkuVisible)
                val fieldVisitablePosition = variantDetailFieldsAdapter?.addVariantDetailField(variantDetailInputModel)
                fieldVisitablePosition?.let { viewModel.updateVariantDetailInputMap(fieldVisitablePosition, variantDetailInputModel) }
                productVariantIndex++
            }
        }
        // set input field size
        viewModel.setInputFieldSize(unitValueLevel2.size)
    }

    private fun showMultipleEditBottomSheet() {
        val variantInputModel = viewModel.productInputModel.value?.variantInputModel
        val bottomSheet = MultipleVariantEditSelectBottomSheet(this, viewModel.isMultiLocationShop)
        val hasWholesale = viewModel.hasWholesale.value ?: false
        bottomSheet.setData(variantInputModel)
        bottomSheet.setEnableEditSku(switchUnifySku.isChecked)
        bottomSheet.setEnableEditPrice(!hasWholesale)
        bottomSheet.setTrackerShopId(userSession.shopId)
        bottomSheet.setTrackerIsEditMode(viewModel.isEditMode)
        bottomSheet.show(fragmentManager)
    }

    private fun showSelectPrimaryBottomSheet() {
        val variantInputModel = viewModel.productInputModel.value?.variantInputModel
        val bottomSheet = SelectVariantMainBottomSheet(this)
        bottomSheet.setData(variantInputModel)
        bottomSheet.show(fragmentManager)
    }

    private fun submitVariantInput() {
        val detailList = variantDetailFieldsAdapter?.getDetailInputLayoutList().orEmpty()
        val isError = viewModel.validateSubmitDetailField(detailList)

        if (isError) {
            variantDetailFieldsAdapter?.notifyDataSetChanged()
        } else {
            viewModel.updateProductInputModel()
            viewModel.productInputModel.value?.apply {
                val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
                        ?: ""
                SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, this)

                val intent = Intent().putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
    }

    private fun sendTrackerSaveMainVariant(combination: List<Int>) {
        if (viewModel.isEditMode) {
            ProductEditVariantDetailTracking.saveMainVariant(
                    viewModel.getPrimaryVariantTitle(combination),
                    userSession.shopId)
        } else {
            ProductAddVariantDetailTracking.saveMainVariant(
                    viewModel.getPrimaryVariantTitle(combination),
                    userSession.shopId)
        }
    }

    private fun sendClickVariantStatusToggleData(isChecked: Boolean) {
        if (viewModel.isEditMode) {
            ProductEditVariantDetailTracking.clickVariantStatusToggle(
                    if (isChecked) VARIANT_TRACKER_ON else VARIANT_TRACKER_OFF,
                    userSession.shopId)
        } else {
            ProductAddVariantDetailTracking.clickVariantStatusToggle(
                    if (isChecked) VARIANT_TRACKER_ON else VARIANT_TRACKER_OFF,
                    userSession.shopId)
        }
    }

    private fun sendTrackerSaveVariantDetailData() {
        if (viewModel.isEditMode) {
            ProductEditVariantDetailTracking.saveVariantDetail(userSession.shopId)
        } else {
            ProductAddVariantDetailTracking.saveVariantDetail(userSession.shopId)
        }
    }

    private fun sendTrackerClickSKUToggleData(isChecked: Boolean) {
        if (viewModel.isEditMode) {
            ProductEditVariantDetailTracking.clickSKUToggle(
                    if (isChecked) VARIANT_TRACKER_ON else VARIANT_TRACKER_OFF,
                    userSession.shopId)
        } else {
            ProductAddVariantDetailTracking.clickSKUToggle(
                    if (isChecked) VARIANT_TRACKER_ON else VARIANT_TRACKER_OFF,
                    userSession.shopId)
        }
    }

    private fun sendTrackerTrackScreenData() {
        if (viewModel.isEditMode) {
            ProductEditVariantDetailTracking.trackScreen(
                    userSession.isLoggedIn.toString(),
                    userSession.userId
            )
        } else {
            ProductAddVariantDetailTracking.trackScreen(
                    userSession.isLoggedIn.toString(),
                    userSession.userId
            )
        }
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(com.tokopedia.product.addedit.R.id.toolbar_variant_detail)?.apply {
            headerTitle = getString(com.tokopedia.product.addedit.R.string.title_variant_activity)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }
}
