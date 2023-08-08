package com.tokopedia.product.addedit.variant.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_DETAIL_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_DETAIL_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_DETAIL_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_DETAIL_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.SharedPreferencesUtil.getFirstTimeWeightPerVariant
import com.tokopedia.product.addedit.common.util.SharedPreferencesUtil.setFirstTimeWeightPerVariant
import com.tokopedia.product.addedit.common.util.setFragmentToUnifyBgColor
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.tracking.ProductAddVariantDetailTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantDetailTracking
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailFieldsAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailInputTypeFactoryImpl
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailFieldsUiModel
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailHeaderUiModel
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantDetailFieldsViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantDetailHeaderViewHolder
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_TRACKER_OFF
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_TRACKER_ON
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.dialog.MultipleVariantEditListener
import com.tokopedia.product.addedit.variant.presentation.dialog.MultipleVariantEditSelectBottomSheet
import com.tokopedia.product.addedit.variant.presentation.dialog.SelectVariantMainBottomSheet
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantDetailViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.math.BigInteger
import java.util.*
import javax.inject.Inject
import com.tokopedia.product.manage.common.R as productManageR

class AddEditProductVariantDetailFragment :
    BaseDaggerFragment(),
    VariantDetailHeaderViewHolder.OnCollapsibleHeaderClickListener,
    SelectVariantMainBottomSheet.SelectVariantMainListener,
    VariantDetailFieldsViewHolder.VariantDetailFieldsViewHolderListener,
    AddEditProductPerformanceMonitoringListener,
    MultipleVariantEditListener {

    companion object {
        const val SCROLLING_DELAY = 500L
        const val TRACKER_DELIMITER = " - "
        const val TRACKER_THROTTLE_TIME = 100
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
    private val firstTimeWeightPerVariant: Boolean by lazy {
        getFirstTimeWeightPerVariant(activity)
    }

    // PLT Monitoring
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    // fragment views
    private var recyclerViewVariantDetailFields: RecyclerView? = null
    private var switchUnifySku: SwitchUnify? = null
    private var tickerVariantWholesale: Ticker? = null
    private var multiLocationTicker: Ticker? = null
    private var imageViewMultipleEdit: ImageView? = null
    private var variantListButton: Typography? = null
    private var buttonSave: UnifyButton? = null
    private var lastSendTrackerDate: Date = Date()
    private var isSendingTrackerMultipleVariant = false

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
            val productInputModel = saveInstanceCacheManager.get(
                EXTRA_PRODUCT_INPUT_MODEL,
                ProductInputModel::class.java
            ) ?: ProductInputModel()
            viewModel.updateProductInputModel(productInputModel)
        }

        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_variant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendTrackerTrackScreenData()

        // set bg color programatically, to reduce overdraw
        setFragmentToUnifyBgColor()

        setupViews(view)
        viewModel.setupMultiLocationValue()

        // max stock as threshold when seller inserts stock
        viewModel.getMaxStockThreshold(userSession.shopId)

        multiLocationTicker?.showWithCondition(viewModel.isMultiLocationShop)

        val multipleVariantEditSelectBottomSheet = MultipleVariantEditSelectBottomSheet(this, viewModel.isMultiLocationShop)
        val variantInputModel = viewModel.productInputModel.value?.variantInputModel
        val hasDTStock = viewModel.productInputModel.value?.hasDTStock.orFalse()
        multipleVariantEditSelectBottomSheet.setData(variantInputModel)

        variantDetailFieldsAdapter = VariantDetailFieldsAdapter(
            VariantDetailInputTypeFactoryImpl(
                this,
                this,
                !hasDTStock
            )
        )
        recyclerViewVariantDetailFields?.adapter = variantDetailFieldsAdapter
        recyclerViewVariantDetailFields?.layoutManager = LinearLayoutManager(context)
        recyclerViewVariantDetailFields?.itemAnimator = null

        switchUnifySku?.setOnClickListener {
            val isChecked = switchUnifySku?.isChecked.orFalse()
            viewModel.updateSkuVisibilityStatus(isVisible = isChecked)
            sendTrackerClickSKUToggleData(isChecked)
        }

        imageViewMultipleEdit?.setOnClickListener {
            showMultipleEditBottomSheet()
        }

        variantListButton?.setOnClickListener {
            showSelectPrimaryBottomSheet()
        }

        observeSelectedVariantSize()
        observeHasWholesale()
        observeMaxStockThreshold()
        observeIsSingleProductVariant()

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
            viewModel.updateVariantDetailHeaderMap(headerPosition, true)
            viewModel.collapseHeader(headerPosition, currentHeaderPosition)
        } else {
            variantDetailFieldsAdapter?.expandDetailFields(currentHeaderPosition, viewModel.getVariantDetailHeaderData(headerPosition))
            val layoutManager: LinearLayoutManager = recyclerViewVariantDetailFields?.layoutManager as LinearLayoutManager
            layoutManager.scrollToPositionWithOffset(currentHeaderPosition, 0)
            viewModel.updateVariantDetailHeaderMap(headerPosition, false)
            viewModel.expandHeader(headerPosition, currentHeaderPosition)
        }
        return viewModel.isVariantDetailHeaderCollapsed(headerPosition)
    }

    override fun onStatusSwitchChanged(isChecked: Boolean, adapterPosition: Int) {
        val updatedInputModel = viewModel.updateSwitchStatus(isChecked, adapterPosition)
        viewModel.updateVariantDetailInputMap(adapterPosition, updatedInputModel)

        // tracking
        sendClickVariantStatusToggleData(isChecked)
    }

    override fun onPriceInputTextChanged(priceInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val validatedInputModel = viewModel.validateVariantPriceInput(priceInput, adapterPosition)
        viewModel.updateVariantDetailInputMap(adapterPosition, validatedInputModel)
        return validatedInputModel
    }

    override fun onStockInputTextChanged(stockInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val validatedInputModel = viewModel.validateProductVariantStockInput(stockInput.toIntOrNull(), adapterPosition)
        viewModel.updateVariantDetailInputMap(adapterPosition, validatedInputModel)
        return validatedInputModel
    }

    override fun onSkuInputTextChanged(skuInput: String, adapterPosition: Int) {
        val updatedInputModel = viewModel.updateVariantSkuInput(skuInput, adapterPosition)
        viewModel.updateVariantDetailInputMap(adapterPosition, updatedInputModel)
    }

    override fun onWeightInputTextChanged(weightInput: String, adapterPosition: Int): VariantDetailInputLayoutModel {
        val validatedInputModel = viewModel.validateProductVariantWeightInput(weightInput.toIntOrNull(), adapterPosition)
        viewModel.updateVariantDetailInputMap(adapterPosition, validatedInputModel)
        sendTrackerWpvFillWeight(validatedInputModel)
        return validatedInputModel
    }

    override fun onCoachmarkDismissed() {
        setFirstTimeWeightPerVariant(activity, false)
    }

    override fun onDisablingVariantDT(position: Int) {
        showDTNotAllowedChangeStatusDialog(position)
    }

    override fun onDisablingVariantCampaign(position: Int) {
        val toaster = Toaster.build(
            requireView(),
            getString(R.string.product_add_edit_cannot_deactivate_variant_campaign),
            actionText = getString(R.string.action_oke),
            type = Toaster.TYPE_ERROR,
            duration = Toaster.LENGTH_LONG
        ).apply {
            anchorView = buttonSave
        }
        toaster.show()
    }

    override fun onMultipleEditInputFinished(multipleVariantEditInputModel: MultipleVariantEditInputModel) {
        val headerList = variantDetailFieldsAdapter?.list?.filterIsInstance<VariantDetailHeaderUiModel>()
        headerList?.forEach {
            if (it.isCollapsed) onHeaderClicked(it.position)
        }

        variantDetailFieldsAdapter?.list?.forEachIndexed { index, visitable ->
            (visitable as? VariantDetailFieldsUiModel)?.variantDetailInputLayoutModel?.let {
                if (!multipleVariantEditInputModel.selection.contains(it.combination)) return@let

                // assign new value if input price is not empty
                if (multipleVariantEditInputModel.price.isNotEmpty()) {
                    onPriceInputTextChanged(multipleVariantEditInputModel.price, index)
                }
                // assign new value if input stock is not empty
                if (multipleVariantEditInputModel.stock.isNotEmpty()) {
                    onStockInputTextChanged(multipleVariantEditInputModel.stock, index)
                }
                // assign new value if input sku is not empty
                if (multipleVariantEditInputModel.sku.isNotEmpty()) {
                    onSkuInputTextChanged(multipleVariantEditInputModel.sku, index)
                }
                // assign new value if input weight is not empty
                if (multipleVariantEditInputModel.weight.isNotEmpty()) {
                    sendTrackerWpvFillMultipleWeight(multipleVariantEditInputModel.weight, it)
                    onWeightInputTextChanged(multipleVariantEditInputModel.weight, index)
                }

                onStatusSwitchChanged(true, index)
            }
            variantDetailFieldsAdapter?.notifyItemChanged(index)
        }
    }

    override fun onMultipleEditInputValidatePrice(price: BigInteger): String {
        return viewModel.validateVariantPriceInput(price)
    }

    override fun onMultipleEditInputValidateStock(stock: BigInteger): String {
        return viewModel.validateProductVariantStockInput(stock)
    }

    override fun onMultipleEditInputValidateWeight(weight: BigInteger): String {
        return viewModel.validateProductVariantWeightInput(weight.toInt())
    }

    override fun onSelectVariantMainFinished(combination: List<Int>) {
        val updatedFieldPosition = viewModel.updatePrimaryVariant(combination)

        // update switch status if primary variant changed (index bigger than -1)
        if (updatedFieldPosition.isMoreThanZero() || updatedFieldPosition.isZero()) {
            viewModel.updateSwitchStatus(true, updatedFieldPosition)
            variantDetailFieldsAdapter?.activateVariantStatus(combination)
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
        viewModel.selectedVariantSize.observe(viewLifecycleOwner) { size ->
            // clear old elements before rendering new elements
            variantDetailFieldsAdapter?.clearAllElements()
            // have 2 selected variant detail
            val hasVariantCombination = viewModel.hasVariantCombination(size)
            // with collapsible header
            if (hasVariantCombination) {
                val selectedVariantList =
                    viewModel.productInputModel.value?.variantInputModel?.selections
                selectedVariantList?.run { setupVariantDetailCombinationFields(selectedVariantList) }
            } else {
                val selectedVariant =
                    viewModel.productInputModel.value?.variantInputModel?.selections?.firstOrNull()
                val selectedUnitValues = selectedVariant?.options
                selectedUnitValues?.run { setupVariantDetailFields(selectedUnitValues) }
            }
        }
    }

    private fun observeHasWholesale() {
        viewModel.hasWholesale.observe(viewLifecycleOwner) {
            variantDetailFieldsAdapter?.updatePriceEditingStatus(!it)
            tickerVariantWholesale?.isVisible = it
        }
    }

    private fun observeIsSingleProductVariant() {
        viewModel.isSingleProductVariant.observe(viewLifecycleOwner) {
            if (it) {
                buttonSave?.setOnClickListener {
                    invokeFieldsValidation()
                    if (viewModel.getInputDataValidStatus()) {
                        showSingleProductVariantDialog()
                    }
                }
            } else {
                buttonSave?.setOnClickListener {
                    submitVariantInput()
                    sendTrackerSaveVariantDetailData()
                }
            }
        }
    }

    private fun observeMaxStockThreshold() {
        viewModel.maxStockThreshold.observe(viewLifecycleOwner) {
            variantDetailFieldsAdapter?.updateMaxStockThreshold(it)
        }
    }

    private fun enableSku() {
        switchUnifySku?.isChecked = true
    }

    private fun setupViews(view: View) {
        recyclerViewVariantDetailFields = view.findViewById(R.id.recyclerViewVariantDetailFields)
        switchUnifySku = view.findViewById(R.id.switchUnifySku)
        tickerVariantWholesale = view.findViewById(R.id.tickerVariantWholesale)
        imageViewMultipleEdit = view.findViewById(R.id.imageViewMultipleEdit)
        variantListButton = view.findViewById(R.id.variantListButton)
        buttonSave = view.findViewById(R.id.buttonSave)
        multiLocationTicker = view.findViewById(R.id.ticker_add_edit_variant_multi_location)
        multiLocationTicker?.setTextDescription(context?.resources?.getString(R.string.ticker_variant_only_main_location).toString())
        tickerVariantWholesale?.setTextDescription(context?.resources?.getString(R.string.label_variant_wholesale_warning).toString())
    }

    private fun setupVariantDetailFields(selectedUnitValues: List<OptionInputModel>) {
        // without variant unit values combination
        selectedUnitValues.forEachIndexed { productVariantIndex, unitValue ->
            val isSkuVisible = switchUnifySku?.isChecked.orFalse() // get last visibility
            val variantDetailInputModel = viewModel.generateVariantDetailInputModel(
                productVariantIndex,
                Int.ZERO,
                unitValue.value,
                isSkuVisible
            )
            val fieldVisitablePosition = variantDetailFieldsAdapter?.addVariantDetailField(
                variantDetailInputModel,
                firstTimeWeightPerVariant
            )
            fieldVisitablePosition?.let { viewModel.addToVariantDetailInputMap(fieldVisitablePosition, variantDetailInputModel) }
        }
    }

    private fun setupVariantDetailCombinationFields(selectedVariants: List<SelectionInputModel>) {
        // increment for indexing product variant
        var productVariantIndex = Int.ZERO
        // variant level 1 properties
        val selectedVariantLevel1 = selectedVariants[VARIANT_VALUE_LEVEL_ONE_POSITION]
        val unitValueLevel1 = selectedVariantLevel1.options
        // variant level 2 properties
        val selectedVariantLevel2 = selectedVariants[VARIANT_VALUE_LEVEL_TWO_POSITION]
        val unitValueLevel2 = selectedVariantLevel2.options
        // start rendering
        unitValueLevel1.forEach { level1Value ->
            // render collapsible header
            val headerVisitablePosition = variantDetailFieldsAdapter?.addUnitValueHeader(level1Value.value).orZero()
            viewModel.updateVariantDetailHeaderMap(headerVisitablePosition, false)
            // init header position - current header visitable position map values
            viewModel.updateCurrentHeaderPositionMap(headerVisitablePosition, headerVisitablePosition)
            // render variant unit value fields
            unitValueLevel2.forEach { level2Value ->
                val isSkuVisible = switchUnifySku?.isChecked.orFalse() // get last visibility
                val variantDetailInputModel = viewModel.generateVariantDetailInputModel(
                    productVariantIndex,
                    headerVisitablePosition,
                    level2Value.value,
                    isSkuVisible
                )
                val fieldVisitablePosition = variantDetailFieldsAdapter?.addVariantDetailField(
                    variantDetailInputModel,
                    firstTimeWeightPerVariant
                )
                fieldVisitablePosition?.let { viewModel.addToVariantDetailInputMap(fieldVisitablePosition, variantDetailInputModel) }
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
        bottomSheet.setEnableEditSku(switchUnifySku?.isChecked.orFalse())
        bottomSheet.setEnableEditPrice(!hasWholesale)
        bottomSheet.setTrackerShopId(userSession.shopId)
        bottomSheet.setTrackerIsEditMode(viewModel.isEditMode)
        bottomSheet.show(childFragmentManager)
    }

    private fun showSelectPrimaryBottomSheet() {
        val variantInputModel = viewModel.productInputModel.value?.variantInputModel
        val bottomSheet = SelectVariantMainBottomSheet(this)
        bottomSheet.setData(variantInputModel)
        bottomSheet.show(childFragmentManager)
    }

    private fun showDTNotAllowedChangeStatusDialog(position: Int) {
        val dialog = DialogUnify(context ?: return, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        val descriptionText = getString(R.string.product_add_edit_text_description_product_dt_cannot_deactivate).parseAsHtml()
        dialog.apply {
            setTitle(getString(R.string.product_add_edit_text_title_product_dt_cannot_deactivate))
            setDescription(descriptionText)
            setPrimaryCTAText(getString(productManageR.string.product_manage_confirm_inactive_dt_product_positive_button))
            setSecondaryCTAText(getString(productManageR.string.product_manage_confirm_dt_product_cancel_button))
            setPrimaryCTAClickListener {
                viewModel.updateSwitchStatus(false, position)
                variantDetailFieldsAdapter?.deactivateVariantStatus(position)
                dismiss()
            }
            setSecondaryCTAClickListener {
                dismiss()
            }
        }
        dialog.show()
    }

    private fun showSingleProductVariantDialog() {
        val dialog = DialogUnify(context ?: return, DialogUnify.HORIZONTAL_ACTION, DialogUnify.WITH_ILLUSTRATION)
        dialog.apply {
            setTitle(getString(R.string.product_add_edit_single_product_variant_dialog_title))
            setDescription(getString(R.string.product_add_edit_single_product_variant_dialog_desc))
            setPrimaryCTAText(getString(R.string.product_add_edit_single_product_variant_dialog_delete))
            setSecondaryCTAText(getString(R.string.action_back))
            setImageUrl(TokopediaImageUrl.AEP_SINGLE_VARIANT_WARNING)
            setPrimaryCTAClickListener {
                viewModel.updateProductInputModel()
                viewModel.convertToNonVariant()
                sendResultData()
                dismiss()
            }
            setSecondaryCTAClickListener {
                dismiss()
            }
        }
        dialog.show()
    }

    private fun submitVariantInput() {
        invokeFieldsValidation()
        if (viewModel.getInputDataValidStatus()) {
            viewModel.updateProductInputModel()
            sendResultData()
        }
    }

    private fun sendResultData() {
        viewModel.productInputModel.value?.apply {
            val cacheManagerId = arguments?.getString(
                AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
            ).orEmpty()
            SaveInstanceCacheManager(requireContext(), cacheManagerId)
                .put(EXTRA_PRODUCT_INPUT_MODEL, this)

            val intent = Intent().putExtra(
                AddEditProductConstants.EXTRA_CACHE_MANAGER_ID,
                cacheManagerId
            )
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun invokeFieldsValidation() {
        val headerList = variantDetailFieldsAdapter?.list?.filterIsInstance<VariantDetailHeaderUiModel>()
        headerList?.forEach {
            if (it.isCollapsed) onHeaderClicked(it.position)
        }

        variantDetailFieldsAdapter?.list?.forEachIndexed { index, visitable ->
            (visitable as? VariantDetailFieldsUiModel)?.variantDetailInputLayoutModel?.let {
                onPriceInputTextChanged(it.price.replace(".", ""), index)
                onStockInputTextChanged(it.stock?.toString().orEmpty(), index)
                onWeightInputTextChanged(it.weight?.toString().orEmpty(), index)
                onSkuInputTextChanged(it.sku, index)
            }
            variantDetailFieldsAdapter?.notifyItemChanged(index)
        }

        recyclerViewVariantDetailFields?.postDelayed({
            scrollToFirstError()
        }, SCROLLING_DELAY)
    }

    private fun scrollToFirstError() {
        try {
            val errorFieldIndex = variantDetailFieldsAdapter?.list?.indexOfFirst {
                (it as? VariantDetailFieldsUiModel)?.variantDetailInputLayoutModel?.let { inputModel ->
                    inputModel.isWeightError || inputModel.isPriceError || inputModel.isStockError
                } ?: false
            }

            errorFieldIndex?.let {
                recyclerViewVariantDetailFields?.smoothScrollToPosition(it)
            }
        } catch (scrollingException: Exception) {
            // no-op
        }
    }

    private fun sendTrackerSaveMainVariant(combination: List<Int>) {
        if (viewModel.isEditMode) {
            ProductEditVariantDetailTracking.saveMainVariant(
                viewModel.getPrimaryVariantTitle(combination),
                userSession.shopId
            )
        } else {
            ProductAddVariantDetailTracking.saveMainVariant(
                viewModel.getPrimaryVariantTitle(combination),
                userSession.shopId
            )
        }
    }

    private fun sendClickVariantStatusToggleData(isChecked: Boolean) {
        if (viewModel.isEditMode) {
            ProductEditVariantDetailTracking.clickVariantStatusToggle(
                if (isChecked) VARIANT_TRACKER_ON else VARIANT_TRACKER_OFF,
                userSession.shopId
            )
        } else {
            ProductAddVariantDetailTracking.clickVariantStatusToggle(
                if (isChecked) VARIANT_TRACKER_ON else VARIANT_TRACKER_OFF,
                userSession.shopId
            )
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
                userSession.shopId
            )
        } else {
            ProductAddVariantDetailTracking.clickSKUToggle(
                if (isChecked) VARIANT_TRACKER_ON else VARIANT_TRACKER_OFF,
                userSession.shopId
            )
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

    private fun sendTrackerWpvFillWeight(validatedInputModel: VariantDetailInputLayoutModel) {
        // throttle call to avoid doubled call
        val dateNow = Date()
        if (dateNow.time < lastSendTrackerDate.time + TRACKER_THROTTLE_TIME) return
        lastSendTrackerDate = dateNow

        // avoid sending tracker when sending other tracker
        if (isSendingTrackerMultipleVariant) return

        val header = variantDetailFieldsAdapter?.getHeaderAtPosition(
            validatedInputModel.headerPosition
        )
        val variantName = if (header == null) {
            validatedInputModel.unitValueLabel
        } else {
            header.headerTitle + TRACKER_DELIMITER + validatedInputModel.unitValueLabel
        }

        ProductAddVariantDetailTracking.clickFillBoxVariantWeight(
            variantName,
            validatedInputModel.weight.orZero(),
            userSession.shopId
        )
    }

    private fun sendTrackerWpvFillMultipleWeight(
        weight: String,
        validatedInputModel: VariantDetailInputLayoutModel
    ) {
        val header = variantDetailFieldsAdapter?.getHeaderAtPosition(
            validatedInputModel.headerPosition
        )
        val variantName = if (header == null) {
            validatedInputModel.unitValueLabel
        } else {
            header.headerTitle + TRACKER_DELIMITER + validatedInputModel.unitValueLabel
        }

        isSendingTrackerMultipleVariant = true
        ProductAddVariantDetailTracking.clickAddWeightMultipleVariant(
            variantName,
            weight.toIntOrZero(),
            userSession.shopId
        )

        view?.post { isSendingTrackerMultipleVariant = false }
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(R.id.toolbar_variant_detail)?.apply {
            headerTitle = getString(com.tokopedia.product.addedit.R.string.title_variant_activity)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }
}
