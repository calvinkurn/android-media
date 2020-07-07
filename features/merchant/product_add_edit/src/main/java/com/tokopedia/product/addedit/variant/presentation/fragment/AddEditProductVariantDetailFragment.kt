package com.tokopedia.product.addedit.variant.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_ACTIVE_STRING
import com.tokopedia.product.addedit.common.util.InputPriceUtil.formatProductPriceInput
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
        VariantDetailFieldsViewHolder.OnSkuInputTextChangedListener {

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

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(AddEditProductVariantComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

        cacheManagerId?.run {
            viewModel.productInputModel.value = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL,
                    ProductInputModel::class.java) ?: ProductInputModel()
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

        val multipleVariantEditSelectBottomSheet = MultipleVariantEditSelectBottomSheet(this)
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

        switchUnifySku.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateSkuVisibilityStatus(isVisible = isChecked)
            variantDetailFieldsAdapter?.updateSkuVisibilityStatus(viewModel.getAvailableFields(), isChecked)
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
    }

    override fun onHeaderClicked(adapterPosition: Int): Boolean {
        val isCollapsed = viewModel.isVariantDetailHeaderCollapsed(adapterPosition)
        if (!isCollapsed) {
            variantDetailFieldsAdapter?.collapseUnitValueHeader(adapterPosition, viewModel.getInputFieldSize())
            viewModel.increaseCollapsedFields(viewModel.getInputFieldSize())
            viewModel.updateVariantDetailHeaderMap(adapterPosition, true)
        } else {
            variantDetailFieldsAdapter?.expandDetailFields(adapterPosition, viewModel.getVariantDetailHeaderData(adapterPosition))
            recyclerViewVariantDetailFields.scrollToPosition(adapterPosition)
            viewModel.decreaseCollapsedFields(viewModel.getInputFieldSize())
            viewModel.updateVariantDetailHeaderMap(adapterPosition, false)
        }
        return viewModel.isVariantDetailHeaderCollapsed(adapterPosition)
    }

    override fun onCheckedChanged(isChecked: Boolean, adapterPosition: Int) {
        val updatedInputModel = viewModel.updateSwitchStatus(isChecked, adapterPosition)
        viewModel.editVariantDetailInputMap(adapterPosition, updatedInputModel)

        // tracking
        ProductAddVariantDetailTracking.clickVariantStatusToggle(
                if (isChecked) VARIANT_TRACKER_ON else VARIANT_TRACKER_OFF,
                userSession.shopId
        )
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
        viewModel.updatePrimaryVariant(combination)

        // tracking
        ProductAddVariantDetailTracking.saveMainVariant(
                viewModel.getPrimaryVariantTitle(combination),
                userSession.shopId)
    }

    fun onBackPressed() {
        activity?.finish()
    }

    private fun observeSelectedVariantSize() {
        viewModel.selectedVariantSize.observe(this, Observer { size ->
            // clear old elements before rendering new elements
            variantDetailFieldsAdapter?.clearAllElements()
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
        viewModel.hasWholesale.observe(this, Observer {
            variantDetailFieldsAdapter?.updatePriceEditingStatus(viewModel.getAvailableFields(), !it)
            tickerVariantWholesale.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun observeInputStatus() {
        viewModel.errorCounter.observe(this, Observer {
            buttonSave.isEnabled = it.orZero() <= 0
        })
    }

    private fun setupVariantDetailFields(selectedUnitValues: List<OptionInputModel>) {
        // without variant unit values combination
        val productVariants = viewModel.productInputModel.value?.variantInputModel?.products.orEmpty()
        selectedUnitValues.forEachIndexed { productVariantIndex, unitValue ->
            val productVariant = productVariants
                    .getOrElse(productVariantIndex) { ProductVariantInputModel() }
            val priceString = productVariant.price.toString()
            val variantDetailInputModel = VariantDetailInputLayoutModel(
                    price = formatProductPriceInput(priceString),
                    isActive = productVariant.status == STATUS_ACTIVE_STRING,
                    sku = productVariant.sku,
                    stock = productVariant.stock.toString(),
                    unitValueLabel = unitValue.value)
            val fieldAdapterPosition = variantDetailFieldsAdapter?.addVariantDetailField(variantDetailInputModel)
            fieldAdapterPosition?.let { viewModel.updateVariantDetailInputMap(fieldAdapterPosition, variantDetailInputModel) }
        }
    }

    private fun setupVariantDetailCombinationFields(selectedVariants: List<SelectionInputModel>) {
        //increment for indexing product variant
        var productVariantIndex = 0
        val productVariants = viewModel.productInputModel.value?.variantInputModel?.products.orEmpty()
        // variant level 1 properties
        val selectedVariantLevel1 = selectedVariants[VARIANT_VALUE_LEVEL_ONE_POSITION]
        val unitValueLevel1 = selectedVariantLevel1.options
        // variant level 2 properties
        val selectedVariantLevel2 = selectedVariants[VARIANT_VALUE_LEVEL_TWO_POSITION]
        val unitValueLevel2 = selectedVariantLevel2.options
        // start rendering
        unitValueLevel1.forEachIndexed { index, level1Value ->
            // render collapsible header
            val headerPosition = variantDetailFieldsAdapter?.addUnitValueHeader(level1Value.value, index)
                    ?: 0
            viewModel.updateVariantDetailHeaderMap(headerPosition, false)
            // render variant unit value fields
            unitValueLevel2.forEach { level2Value ->
                val productVariant = productVariants
                        .getOrElse(productVariantIndex) { ProductVariantInputModel() }
                val priceString = productVariant.price.toString()
                val variantDetailInputModel = VariantDetailInputLayoutModel(
                        price = formatProductPriceInput(priceString),
                        isActive = productVariant.status == STATUS_ACTIVE_STRING,
                        sku = productVariant.sku,
                        stock = productVariant.stock.toString(),
                        headerPosition = headerPosition,
                        unitValueLabel = level2Value.value)
                val fieldAdapterPosition = variantDetailFieldsAdapter?.addVariantDetailField(variantDetailInputModel)
                fieldAdapterPosition?.let { viewModel.updateVariantDetailInputMap(fieldAdapterPosition, variantDetailInputModel) }
                productVariantIndex++
            }
        }
        // set input field size
        viewModel.setInputFieldSize(unitValueLevel2.size)
    }

    private fun showMultipleEditBottomSheet() {
        val variantInputModel = viewModel.productInputModel.value?.variantInputModel
        val bottomSheet = MultipleVariantEditSelectBottomSheet(this)
        val hasWholesale = viewModel.hasWholesale.value ?: false
        bottomSheet.isKeyboardOverlap = false
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
                val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID) ?: ""
                SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, this)

                val intent = Intent().putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
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
}
