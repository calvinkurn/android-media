package com.tokopedia.product.addedit.variant.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.imagepicker.common.*
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.util.AddEditProductUploadErrorHandler
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.common.util.RecyclerViewItemDecoration
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.tracking.ProductAddStepperTracking
import com.tokopedia.product.addedit.tracking.ProductAddVariantTracking
import com.tokopedia.product.addedit.tracking.ProductEditStepperTracking
import com.tokopedia.product.addedit.tracking.ProductEditVariantTracking
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.activity.AddEditProductVariantDetailActivity
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantPhotoAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantValueAdapter
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.COLOUR_VARIANT_TYPE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.CUSTOM_VARIANT_UNIT_VALUE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_SIZECHART_IMAGE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_VARIANT_DETAIL
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_VARIANT_PHOTO_IMAGE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.dialog.AddEditProductVariantSizechartDialogFragment
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantPhoto
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantViewModel
import com.tokopedia.product.addedit.variant.presentation.widget.CustomVariantUnitValueForm
import com.tokopedia.product.addedit.variant.presentation.widget.VariantDataValuePicker
import com.tokopedia.product.addedit.variant.presentation.widget.VariantUnitPicker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.add_edit_product_variant_photo_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_sizechart_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_type_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_value_level1_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_value_level2_layout.*
import kotlinx.android.synthetic.main.fragment_add_edit_product_variant.*
import javax.inject.Inject

class AddEditProductVariantFragment :
        BaseDaggerFragment(),
        VariantTypeAdapter.OnVariantTypeClickListener,
        VariantDataValuePicker.OnButtonSaveClickListener,
        VariantValueAdapter.OnRemoveButtonClickListener,
        CustomVariantUnitValueForm.OnCustomVariantUnitAddListener,
        VariantDataValuePicker.OnAddCustomVariantUnitValueListener,
        VariantUnitPicker.OnVariantUnitPickListener,
        VariantDataValuePicker.OnVariantUnitPickerClickListener,
        VariantPhotoAdapter.OnItemClickListener,
        VariantDataValuePicker.OnVariantUnitValuePickListener,
        AddEditProductPerformanceMonitoringListener {

    companion object {
        private const val TAG_VARIANT_UNIT_PICKER = "VARIANT_UNIT_PICKER"
        private const val TAG_VARIANT_UNIT_VALUE_PICKER = "VARIANT_UNIT_VALUE_PICKER"
        private const val TAG_CUSTOM_VARIANT_UNIT_VALUE_INPUT_FORM = "VARIANT_UNIT_CUSTOM_VALUE_INPUT_FORM"

        fun createInstance(cacheManagerId: String?): Fragment {
            return AddEditProductVariantFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: AddEditProductVariantViewModel
    private var variantTypeAdapter: VariantTypeAdapter? = null
    private var variantValueAdapterLevel1: VariantValueAdapter? = null
    private var variantValueAdapterLevel2: VariantValueAdapter? = null
    private var variantPhotoAdapter: VariantPhotoAdapter? = null
    private var variantDataValuePicker: BottomSheetUnify? = null
    private var variantUnitPicker: BottomSheetUnify? = null
    private var customVariantValueInputForm: BottomSheetUnify? = null
    private var cancellationDialog: DialogUnify? = null
    private var tvDeleteAll: TextView? = null

    private var userSession: UserSessionInterface? = null
    private var isLoggedin = ""
    private var userId = ""
    private var shopId = ""

    // start PLT monitoring
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(AddEditProductVariantComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // start PLT monitoring
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)

        val cacheManagerId = arguments?.getString(EXTRA_CACHE_MANAGER_ID)
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

        cacheManagerId?.run {
            viewModel.productInputModel.value = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL,
                    ProductInputModel::class.java) ?: ProductInputModel()
        }

        // get user Id and shop Id
        userSession = UserSession(requireContext())
        userSession?.let { session ->
            this.isLoggedin = session.isLoggedIn.toString()
            this.userId = session.userId
            this.shopId = session.shopId
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_variant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set bg color programatically, to reduce overdraw
        context?.let { activity?.window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0)) }

        variantTypeAdapter = VariantTypeAdapter(this)
        variantValueAdapterLevel1 = VariantValueAdapter(this, VARIANT_VALUE_LEVEL_ONE_POSITION)
        variantValueAdapterLevel2 = VariantValueAdapter(this, VARIANT_VALUE_LEVEL_TWO_POSITION)
        variantPhotoAdapter = VariantPhotoAdapter(this)

        recyclerViewVariantType.adapter = variantTypeAdapter
        recyclerViewVariantValueLevel1.adapter = variantValueAdapterLevel1
        recyclerViewVariantValueLevel2.adapter = variantValueAdapterLevel2
        recyclerViewVariantPhoto.addItemDecoration(RecyclerViewItemDecoration(requireContext()))
        recyclerViewVariantPhoto.adapter = variantPhotoAdapter
        setRecyclerViewToFlex(recyclerViewVariantType)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel1)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel2)
        setRecyclerViewToHorizontal(recyclerViewVariantPhoto)

        // setup base cancellation dialog
        context?.run {
            cancellationDialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            cancellationDialog?.setPrimaryCTAText(getString(com.tokopedia.product.addedit.R.string.action_cancel_cancellation))
            cancellationDialog?.setSecondaryCTAText(getString(com.tokopedia.product.addedit.R.string.action_confirm_cancellation))
            cancellationDialog?.setPrimaryCTAClickListener {
                cancellationDialog?.dismiss()
            }
        }

        observeIsEditMode()
        observeSizechartUrl()
        observeGetVariantCategoryCombinationResult()
        observeProductInputModel()
        observeInputStatus()
        observeSizechartVisibility()
        observeVariantPhotosVisibility()
        observeIsEditMode()
        observeisRemovingVariant()

        cardSizechart.setOnClickListener {
            onSizechartClicked()
        }

        // button "tambah" variant values level 1 on click listener
        linkAddVariantValueLevel1.setOnClickListener {
            if (variantDataValuePicker?.isVisible == true) return@setOnClickListener
            val variantData: VariantDetail = viewModel.getVariantData(VARIANT_VALUE_LEVEL_ONE_POSITION)
            val selectedVariantUnitValues = mutableListOf<UnitValue>()
            if (variantData.units.isEmpty()) showCustomVariantInputForm(VARIANT_VALUE_LEVEL_ONE_POSITION, variantData, Unit(), listOf(), selectedVariantUnitValues)
            else {
                selectedVariantUnitValues.addAll((viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_ONE_POSITION)))
                showVariantDataValuePicker(variantData, VARIANT_VALUE_LEVEL_ONE_POSITION, viewModel.getSelectedVariantUnit(VARIANT_VALUE_LEVEL_ONE_POSITION), selectedVariantUnitValues)
            }
            viewModel.isEditMode.value?.let { isEditMode ->
                val variantTypeName = variantData.name
                trackAddingVariantDetailValueEvent(isEditMode, variantTypeName, shopId)
            }
        }

        // button "tambah" variant values level 2 on click listener
        linkAddVariantValueLevel2.setOnClickListener {
            if (variantDataValuePicker?.isVisible == true) return@setOnClickListener
            val variantData: VariantDetail = viewModel.getVariantData(VARIANT_VALUE_LEVEL_TWO_POSITION)
            val selectedVariantUnitValues = mutableListOf<UnitValue>()
            if (variantData.units.isEmpty()) showCustomVariantInputForm(VARIANT_VALUE_LEVEL_TWO_POSITION, variantData, Unit(), listOf(), selectedVariantUnitValues)
            else {
                selectedVariantUnitValues.addAll((viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_TWO_POSITION)))
                showVariantDataValuePicker(variantData, VARIANT_VALUE_LEVEL_TWO_POSITION, viewModel.getSelectedVariantUnit(VARIANT_VALUE_LEVEL_TWO_POSITION), selectedVariantUnitValues)
            }
            viewModel.isEditMode.value?.let { isEditMode ->
                val variantTypeName = variantData.name
                trackAddingVariantDetailValueEvent(isEditMode, variantTypeName, shopId)
            }
        }

        // button save on click listener
        buttonSave.setOnClickListener {
            // track click action on continue button
            viewModel.isEditMode.value?.let { isEditMode ->
                if (isEditMode) ProductEditVariantTracking.continueToVariantDetailPage(shopId)
                else ProductAddVariantTracking.continueToVariantDetailPage(shopId)
            }
            // perform the save button function
            if (viewModel.isRemovingVariant.value == true) {
                submitVariantInput()
            } else {
                val variantPhotos = variantPhotoAdapter?.getData().orEmpty()
                viewModel.updateVariantInputModel(variantPhotos)
                startAddEditProductVariantDetailActivity()
            }
        }

        setupToolbarActions()
        // stop PLT prepare monitoring
        stopPreparePagePerformanceMonitoring()
    }

    override fun onVariantTypeSelected(adapterPosition: Int, variantDetail: VariantDetail) {
        // track selected variant type
        viewModel.isEditMode.value?.let { isEditMode ->
            val variantTypeName = variantDetail.name
            if (isEditMode) ProductEditVariantTracking.selectVariantType(variantTypeName, shopId)
            else ProductAddVariantTracking.selectVariantType(variantTypeName, shopId)
        }

        // disable removing variant state, means it's back to add/edit-ing state
        viewModel.disableRemovingVariant()

        if (viewModel.isVariantUnitValuesLayoutEmpty()) {
            // get selected variant unit values for variant level 1
            val selectedVariantUnitValuesLevel1 = viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_ONE_POSITION)
            // setup variant unit level 1 section
            setupVariantValueSection(VARIANT_VALUE_LEVEL_ONE_POSITION, variantDetail, selectedVariantUnitValuesLevel1)
            // update adapter - layout position map
            viewModel.updateVariantValuesLayoutMap(adapterPosition, VARIANT_VALUE_LEVEL_ONE_POSITION)
            // save selected level 1 variant unit values
            viewModel.updateSelectedVariantUnitValuesLevel1(selectedVariantUnitValuesLevel1)
            // update layout - selected unit values map
            viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, selectedVariantUnitValuesLevel1)
            // set single selection flag to true
            viewModel.isSingleVariantTypeIsSelected = true
        } else {
            viewModel.isSingleVariantTypeIsSelected = false

            val renderedAdapterPosition = viewModel.getRenderedLayoutAdapterPosition()

            if (adapterPosition < renderedAdapterPosition) {

                // render the new variant type values in level one position
                // move the rendered to level two position

                // get rendered variant detail
                val renderedVariantDetail = variantTypeAdapter?.getItem(renderedAdapterPosition)
                // get rendered layout position
                val layoutPosition = viewModel.getVariantValuesLayoutPosition(renderedAdapterPosition)

                // get rendered selected variant unit values by layout position
                val renderedSelectedVariantUnitValues = viewModel.getSelectedVariantUnitValues(layoutPosition)

                // move the rendered variant information to level two position
                renderedVariantDetail?.run { setupVariantValueSection(VARIANT_VALUE_LEVEL_TWO_POSITION, this, renderedSelectedVariantUnitValues) }
                // update adapter - layout mapping
                viewModel.updateVariantValuesLayoutMap(renderedAdapterPosition, VARIANT_VALUE_LEVEL_TWO_POSITION)
                // update selected variant unit values level 2
                viewModel.updateSelectedVariantUnitValuesLevel2(renderedSelectedVariantUnitValues)
                // get rendered selected variant unit by layout position
                val renderedSelectedVariantUnit = viewModel.getSelectedVariantUnit(layoutPosition)
                // update layout - selected unit mapping
                viewModel.updateSelectedVariantUnitMap(VARIANT_VALUE_LEVEL_TWO_POSITION, renderedSelectedVariantUnit)
                // update layout - selected unit values mapping
                viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_TWO_POSITION, renderedSelectedVariantUnitValues)

                // render the new value type section in variant level one section
                val selectedVariantUnitValuesLevel1 = mutableListOf<UnitValue>()
                setupVariantValueSection(VARIANT_VALUE_LEVEL_ONE_POSITION, variantDetail, selectedVariantUnitValuesLevel1)
                // update adapter - layout mapping
                viewModel.updateVariantValuesLayoutMap(adapterPosition, VARIANT_VALUE_LEVEL_ONE_POSITION)
                // update selected variant unit values level 1
                viewModel.updateSelectedVariantUnitValuesLevel1(selectedVariantUnitValuesLevel1)
                // update layout - selected unit mapping
                viewModel.updateSelectedVariantUnitMap(VARIANT_VALUE_LEVEL_ONE_POSITION, Unit())
                // update layout - selected unit values mapping
                viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, selectedVariantUnitValuesLevel1)

            } else {

                // render the new variant type values in level two position
                // get rendered layout position
                val layoutPosition = viewModel.getVariantValuesLayoutPosition(renderedAdapterPosition)
                val renderedVariantDetail = variantTypeAdapter?.getItem(renderedAdapterPosition)
                val renderedSelectedVariantUnitValues = viewModel.getSelectedVariantUnitValues(layoutPosition)
                renderedVariantDetail?.run { setupVariantValueSection(VARIANT_VALUE_LEVEL_ONE_POSITION, this, renderedSelectedVariantUnitValues) }
                viewModel.updateVariantValuesLayoutMap(renderedAdapterPosition, VARIANT_VALUE_LEVEL_ONE_POSITION)
                viewModel.updateSelectedVariantUnitValuesLevel1(renderedSelectedVariantUnitValues)
                val renderedSelectedVariantUnit = viewModel.getSelectedVariantUnit(layoutPosition)
                viewModel.updateSelectedVariantUnitMap(VARIANT_VALUE_LEVEL_ONE_POSITION, renderedSelectedVariantUnit)
                viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, renderedSelectedVariantUnitValues)

                // render the new value type section in variant level two section
                val selectedVariantUnitValuesLevel2 = mutableListOf<UnitValue>()
                setupVariantValueSection(VARIANT_VALUE_LEVEL_TWO_POSITION, variantDetail, selectedVariantUnitValuesLevel2)
                viewModel.updateVariantValuesLayoutMap(adapterPosition, VARIANT_VALUE_LEVEL_TWO_POSITION)
                viewModel.updateSelectedVariantUnitValuesLevel2(selectedVariantUnitValuesLevel2)
                viewModel.updateSelectedVariantUnitMap(VARIANT_VALUE_LEVEL_TWO_POSITION, Unit())
                viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_TWO_POSITION, selectedVariantUnitValuesLevel2)
            }
        }

        // update sizechart visibility based on variant selected type
        viewModel.updateSizechartFieldVisibility(variantTypeAdapter?.getSelectedItems().orEmpty())

        // update viewmodel's variant details
        viewModel.setSelectedVariantDetails(variantTypeAdapter?.getSelectedItems().orEmpty())
    }

    override fun onVariantTypeDeselected(adapterPosition: Int, variantDetail: VariantDetail): Boolean {

        viewModel.isSingleVariantTypeIsSelected = true
        val layoutPosition = viewModel.getVariantValuesLayoutPosition(adapterPosition)
        // if deselected variant type contain unit values show confirmation dialog
        val isVariantUnitValuesEmpty = viewModel.isVariantUnitValuesEmpty(layoutPosition)
        return if (!isVariantUnitValuesEmpty) {
            setupCancellationDialog(layoutPosition, adapterPosition, variantDetail)
            cancellationDialog?.show()
            false
        } else {
            deselectVariantType(layoutPosition, adapterPosition, variantDetail)
            true
        }
    }

    fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun deselectVariantType(layoutPosition: Int, adapterPosition: Int, variantDetail: VariantDetail) {
        // hide section
        resetVariantValueSection(layoutPosition)
        // remove adapter - layout map entry
        viewModel.removeVariantValueLayoutMapEntry(adapterPosition)
        // update layout - selected unit map
        viewModel.updateSelectedVariantUnitMap(layoutPosition, Unit())
        // update layout - selected unit values map
        viewModel.updateSelectedVariantUnitValuesMap(layoutPosition, mutableListOf())
        // clear old variant price/ stock
        viewModel.clearProductVariant()
        // remove viewmodel's variant details
        viewModel.removeSelectedVariantDetails(variantDetail)
        // remove all photo adapter data
        if (variantDetail.variantID == COLOUR_VARIANT_TYPE_ID) {
            variantPhotoAdapter?.setData(emptyList())
        }
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                viewModel.updateSelectedVariantUnitValuesLevel1(mutableListOf())
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                viewModel.updateSelectedVariantUnitValuesLevel2(mutableListOf())
            }
        }
        viewModel.hideProductVariantPhotos(variantDetail)
        viewModel.updateSizechartFieldVisibility()
    }

    private fun setupCancellationDialog(layoutPosition: Int, adapterPosition: Int, variantDetail: VariantDetail) {
        val variantTypeName = variantDetail.name
        val title = getString(R.string.label_cancel) + " " + variantTypeName + getString(R.string.label_question_mark)
        val message = getString(R.string.label_data) + " " + variantTypeName + " " + getString(R.string.label_variant_cancellation_message)
        cancellationDialog?.setTitle(title)
        cancellationDialog?.setDescription(message)
        cancellationDialog?.setSecondaryCTAClickListener {
            deselectVariantType(layoutPosition, adapterPosition, variantDetail)
            variantTypeAdapter?.deselectItem(adapterPosition)
            cancellationDialog?.dismiss()
            // track variant type cancellation
            viewModel.isEditMode.value?.let { isEditMode ->
                if (isEditMode) ProductEditVariantTracking.confirmVariantTypeCancellation(variantTypeName, shopId)
                else ProductAddVariantTracking.confirmVariantTypeCancellation(variantTypeName, shopId)
            }
        }
    }

    private fun setupVariantValueSection(layoutPosition: Int, variantTypeDetail: VariantDetail, selectedVariantUnitValues: List<UnitValue>) {
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                variantValueLevel1Layout.show()
                variantValueAdapterLevel1?.setData(selectedVariantUnitValues)
                typographyVariantValueLevel1Title.text = variantTypeDetail.name
                viewModel.updateVariantDataMap(VARIANT_VALUE_LEVEL_ONE_POSITION, variantTypeDetail)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                variantValueLevel2Layout.show()
                variantValueAdapterLevel2?.setData(selectedVariantUnitValues)
                typographyVariantValueLevel2Title.text = variantTypeDetail.name
                viewModel.updateVariantDataMap(VARIANT_VALUE_LEVEL_TWO_POSITION, variantTypeDetail)
            }
        }
    }

    override fun onVariantUnitValueSaveButtonClicked(selectedVariantUnit: Unit, selectedVariantUnitValues: MutableList<UnitValue>, layoutPosition: Int) {
        val variantData = viewModel.getVariantData(layoutPosition)
        // tracking save variant unit values event
        val selectedCount = selectedVariantUnitValues.size
        val eventLabel = "${variantData.name} - $selectedCount"
        viewModel.isEditMode.value?.let { isEditMode ->
            trackSaveVariantUnitValueEvent(isEditMode, eventLabel, shopId)
        }

        variantDataValuePicker?.dismiss()

        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                viewModel.updateSelectedVariantUnitValuesLevel1(selectedVariantUnitValues)
                viewModel.updateSelectedVariantUnitMap(layoutPosition, selectedVariantUnit)
                viewModel.updateSelectedVariantUnitValuesMap(layoutPosition, selectedVariantUnitValues.toMutableList())
                variantValueAdapterLevel1?.setData(selectedVariantUnitValues)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                viewModel.updateSelectedVariantUnitValuesLevel2(selectedVariantUnitValues)
                viewModel.updateSelectedVariantUnitMap(layoutPosition, selectedVariantUnit)
                viewModel.updateSelectedVariantUnitValuesMap(layoutPosition, selectedVariantUnitValues.toMutableList())
                variantValueAdapterLevel2?.setData(selectedVariantUnitValues)
            }
        }

        if (variantData.variantID == COLOUR_VARIANT_TYPE_ID) {
            variantPhotoLayout.show()
            selectedVariantUnitValues.forEach {
                variantPhotoAdapter?.addDataIfNotExist(VariantPhoto(it.value, ""))
            }
        }

        // update sizechart visibility based on variant selected type
        viewModel.updateSizechartFieldVisibility(variantTypeAdapter?.getSelectedItems().orEmpty())
    }

    override fun onVariantUnitPicked(selectedVariantUnit: Unit,
                                     currentVariantUnit: Unit,
                                     layoutPosition: Int,
                                     hasSelectedValues: Boolean) {
        val variantData: VariantDetail = viewModel.getVariantData(layoutPosition)
        variantUnitPicker?.dismiss()
        // show the current variant data picker when the user select the same unit
        if (selectedVariantUnit.variantUnitID == currentVariantUnit.variantUnitID) {
            // show picker dialog to retain the UI states
            variantDataValuePicker?.dialog?.show()
        } else {
            showReplaceVariantUnitDialog(selectedVariantUnit.unitName, currentVariantUnit, variantData, layoutPosition)
        }
    }

    override fun onVariantUnitPickerClicked(layoutPosition: Int,
                                            selectedVariantUnit: Unit,
                                            selectedVariantUnitValues: MutableList<UnitValue>) {
        // hide picker dialog to retain ui states
        variantDataValuePicker?.dialog?.hide()
        val variantData = viewModel.getVariantData(layoutPosition)
        val hasSelectedValues = selectedVariantUnitValues.isNotEmpty()
        showVariantUnitPicker(variantData, layoutPosition, selectedVariantUnit, hasSelectedValues)
        // track selecting variant unit event
        viewModel.isEditMode.value?.let { isEditMode ->
            val variantTypeName = variantData.name
            trackSelectingVariantUnitEvent(isEditMode, variantTypeName, shopId)
        }
    }

    override fun onVariantUnitValuePickListener(variantType: String, variantUnitValue: String) {
        val label = "$variantType - $variantUnitValue"
        viewModel.isEditMode.value?.let { isEditMode ->
            trackSelectVariantUnitValueEvent(isEditMode, label, shopId)
        }
    }

    override fun onAddCustomButtonClicked(layoutPosition: Int,
                                          selectedVariantUnit: Unit,
                                          variantUnitValues: List<UnitValue>,
                                          selectedVariantUnitValues: MutableList<UnitValue>) {
        variantDataValuePicker?.dismiss()
        val variantData = viewModel.getVariantData(layoutPosition)
        showCustomVariantInputForm(layoutPosition, variantData, selectedVariantUnit, variantUnitValues, selectedVariantUnitValues)
    }

    override fun onCustomVariantUnitValueAdded(layoutPosition: Int,
                                               currentSelectedVariantUnit: Unit,
                                               customVariantUnitValue: UnitValue,
                                               currentSelectedVariantUnitValues: MutableList<UnitValue>) {
        // close the custom variant value input form
        customVariantValueInputForm?.dismiss()
        // add the added custom value to variant data and selected variant unit values
        viewModel.addCustomVariantUnitValue(layoutPosition, currentSelectedVariantUnit, customVariantUnitValue)
        // get the variant data for tracking and showing the variant data picker
        val variantData = viewModel.getVariantData(layoutPosition)
        // track trackSaveCustomVariantUnitValueEvent
        val label = variantData.name + " - " + customVariantUnitValue.value
        viewModel.isEditMode.value?.let { isEditMode ->
            trackSaveCustomVariantUnitValueEvent(isEditMode, label, shopId)
        }
        val selectedVariantUnit = viewModel.getSelectedVariantUnit(layoutPosition)
        val selectedVariantUnitValues = mutableListOf<UnitValue>()
        // if the user does not change the variant unit
        if (currentSelectedVariantUnit.variantUnitID == selectedVariantUnit.variantUnitID) {
            // retrieve all selected variant unit values inside the view model
            selectedVariantUnitValues.addAll((viewModel.getSelectedVariantUnitValues(layoutPosition)))
        }
        // unconfirmed selection during the selection process
        val unConfirmedSelection = currentSelectedVariantUnitValues.minus(selectedVariantUnitValues)
        // show the variant data picker again
        showVariantDataValuePicker(variantData, layoutPosition, currentSelectedVariantUnit, selectedVariantUnitValues, customVariantUnitValue, unConfirmedSelection)
    }

    override fun onRemoveButtonClicked(position: Int, layoutPosition: Int, removedUnitValue: UnitValue) {

        viewModel.removeSelectedVariantUnitValue(layoutPosition, removedUnitValue)

        val variantData = viewModel.getVariantData(layoutPosition)

        // get variant id to manage section visibility status
        val variantId = variantData.variantID

        // track remove variant unit value event
        val typeName = variantData.name
        val unitValueName = removedUnitValue.value
        val label = "$typeName - $unitValueName"
        viewModel.isEditMode.value?.let { isEditMode ->
            trackRemoveVariantUnitValueEvent(isEditMode, label, shopId)
        }

        // update photo section state
        if (variantId == COLOUR_VARIANT_TYPE_ID) {
            variantPhotoAdapter?.removeData(position)
            if (variantPhotoAdapter?.getData()?.size == 0) {
                variantPhotoLayout.hide()
                variantPhotoAdapter?.setData(emptyList())
            }
        }

        // update sizechart visibility based on variant selected type
        viewModel.updateSizechartFieldVisibility(variantTypeAdapter?.getSelectedItems().orEmpty())
    }

    override fun onItemClicked(position: Int) {
        viewModel.clickedVariantPhotoItemPosition = position
        showPhotoVariantPicker()
    }

    private fun resetVariantValueSection(layoutPosition: Int) {
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                variantValueLevel1Layout.hide()
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                variantValueLevel2Layout.hide()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_SIZECHART_IMAGE -> {
                    val imageUrlOrPathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
                    imageUrlOrPathList.forEach {
                        viewModel.updateSizechart(it)
                    }
                }
                REQUEST_CODE_VARIANT_PHOTO_IMAGE -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS).orEmpty()
                    imageUrlOrPathList.firstOrNull()?.let {
                        val position = viewModel.clickedVariantPhotoItemPosition.orZero()
                        variantPhotoAdapter?.updateImageData(it, position)
                    }
                }
                REQUEST_CODE_VARIANT_DETAIL -> {
                    val cacheManagerId = data.getStringExtra(EXTRA_CACHE_MANAGER_ID)
                    val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)
                    cacheManagerId?.let {
                        viewModel.productInputModel.value = saveInstanceCacheManager
                                .get(EXTRA_PRODUCT_INPUT_MODEL, ProductInputModel::class.java)
                    }
                    submitVariantInput()
                }
            }
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ADD_EDIT_PRODUCT_VARIANT_PLT_PREPARE_METRICS,
                ADD_EDIT_PRODUCT_VARIANT_PLT_NETWORK_METRICS,
                ADD_EDIT_PRODUCT_VARIANT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(ADD_EDIT_PRODUCT_VARIANT_TRACE)
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
        recyclerViewVariantType.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                stopRenderPerformanceMonitoring()
                stopPerformanceMonitoring()
                recyclerViewVariantType.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    private fun submitVariantInput() {
        val productInputModel = viewModel.productInputModel.value
        productInputModel?.apply {
            val cacheManagerId = arguments?.getString(EXTRA_CACHE_MANAGER_ID)
                    ?: ""
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, this)

            val intent = Intent().putExtra(EXTRA_CACHE_MANAGER_ID, cacheManagerId)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun showVariantDataValuePicker(variantData: VariantDetail,
                                           layoutPosition: Int,
                                           selectedVariantUnit: Unit,
                                           selectedVariantUnitValues: MutableList<UnitValue>,
                                           addedCustomVariantUnitValue: UnitValue = UnitValue(),
                                           unConfirmedSelection: List<UnitValue> = listOf()) {
        variantDataValuePicker = BottomSheetUnify()
        variantDataValuePicker?.setTitle(getString(R.string.label_variant_choose) + " " + variantData.name)
        variantDataValuePicker?.overlayClickDismiss = false
        variantDataValuePicker?.showCloseIcon = true
        variantDataValuePicker?.clearContentPadding = true
        variantDataValuePicker?.setShowListener {
            // set the bottom sheet to full screen
            variantDataValuePicker?.bottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        }
        val variantDataValuePickerLayout = VariantDataValuePicker(requireContext(), layoutPosition, variantData, this, this, this, this)
        variantDataValuePickerLayout.setupVariantDataValuePicker(selectedVariantUnit, selectedVariantUnitValues, addedCustomVariantUnitValue, unConfirmedSelection)
        variantDataValuePicker?.setChild(variantDataValuePickerLayout)
        variantDataValuePicker?.show(this@AddEditProductVariantFragment.childFragmentManager, TAG_VARIANT_UNIT_VALUE_PICKER)
    }

    private fun showVariantUnitPicker(variantData: VariantDetail,
                                      layoutPosition: Int,
                                      selectedVariantUnit: Unit,
                                      hasSelectedValues: Boolean = false) {
        variantUnitPicker = BottomSheetUnify()
        variantUnitPicker?.setTitle(getString(R.string.label_variant_choose) + " " + variantData.name)
        variantUnitPicker?.overlayClickDismiss = false
        variantUnitPicker?.showCloseIcon = true
        variantUnitPicker?.clearContentPadding = true
        val variantUnitPickerLayout = VariantUnitPicker(context)
        variantUnitPickerLayout.setLayoutPosition(layoutPosition)
        variantUnitPickerLayout.setSelectedVariantUnit(selectedVariantUnit)
        variantUnitPickerLayout.setOnVariantUnitPickListener(this)
        variantUnitPickerLayout.setupVariantUnitPicker(variantData.units)
        variantUnitPickerLayout.setHasSelectedValues(hasSelectedValues)
        variantUnitPicker?.setCloseClickListener {
            variantUnitPicker?.dismiss()
            variantDataValuePicker?.dialog?.show()
        }
        variantUnitPicker?.setChild(variantUnitPickerLayout)
        variantUnitPicker?.show(this@AddEditProductVariantFragment.childFragmentManager, TAG_VARIANT_UNIT_PICKER)
    }

    private fun showCustomVariantInputForm(layoutPosition: Int,
                                           variantData: VariantDetail,
                                           selectedVariantUnit: Unit,
                                           variantUnitValues: List<UnitValue>,
                                           selectedVariantUnitValues: MutableList<UnitValue>) {
        customVariantValueInputForm = BottomSheetUnify()
        customVariantValueInputForm?.setTitle(getString(R.string.action_variant_add) + " " + variantData.name)
        customVariantValueInputForm?.overlayClickDismiss = false
        customVariantValueInputForm?.isKeyboardOverlap = false
        val customVariantValueInputLayout = CustomVariantUnitValueForm(requireContext(), layoutPosition, variantUnitValues, this)
        customVariantValueInputLayout.setupVariantCustomInputLayout(selectedVariantUnit, selectedVariantUnitValues)
        customVariantValueInputForm?.setChild(customVariantValueInputLayout)
        customVariantValueInputForm?.show(this@AddEditProductVariantFragment.childFragmentManager, TAG_CUSTOM_VARIANT_UNIT_VALUE_INPUT_FORM)
    }

    private fun showPhotoVariantPicker() {
        val ctx = context ?: return
        val isEditMode = viewModel.isEditMode.value ?: false
        val builder = ImagePickerBuilder.getSquareImageBuilder(ctx)
                .withSimpleEditor()
        builder.imagePickerEditorBuilder?.convertToWebp = true
        ImagePickerGlobalSettings.onImageEditorContinue = ImagePickerCallback(ctx) { it, _ ->
            val shopId = UserSession(it).shopId ?: ""
            if (isEditMode) ProductEditVariantTracking.pickProductVariantPhotos(shopId)
            else ProductAddVariantTracking.pickProductVariantPhotos(shopId)
        }
        val intent = RouteManager.getIntent(ctx, ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(builder);
        startActivityForResult(intent, REQUEST_CODE_VARIANT_PHOTO_IMAGE)
    }

    private fun onSizechartClicked() {
        if (viewModel.variantSizechart.value?.urlOriginal.isNullOrEmpty()) {
            showSizechartPicker()
        } else {
            val fm = this@AddEditProductVariantFragment.childFragmentManager
            val dialogFragment = AddEditProductVariantSizechartDialogFragment.newInstance()
            dialogFragment.show(fm, AddEditProductVariantSizechartDialogFragment.FRAGMENT_TAG)
            dialogFragment.setOnImageEditListener(object :
                    AddEditProductVariantSizechartDialogFragment.OnImageEditListener {
                override fun clickImageEditor() {
                    showEditorSizechartPicker()
                }

                override fun clickRemoveImage() {
                    removeSizechart()
                }

                override fun clickChangeImagePath() {
                    showSizechartPicker()
                }
            })
        }
    }

    private fun observeGetVariantCategoryCombinationResult() {
        // start network PLT monitoring
        startNetworkRequestPerformanceMonitoring()
        viewModel.getVariantCategoryCombinationResult.observe(viewLifecycleOwner, Observer { result ->
            // clear adapter before rendering
            variantTypeAdapter?.setData(emptyList())
            when (result) {
                is Success -> {
                    // master data from back end
                    val variantDataList = result.data.getVariantCategoryCombination.data.variantDetails
                    // selected variant details
                    val selectedVariantDetails = viewModel.getSelectedVariantDetails()
                    // setup the page
                    setupAddEditVariantPage(variantDataList, selectedVariantDetails)

                    // continue to render PLT monitoring if success
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                }
                is Fail -> {
                    // end monitoring if failed
                    stopPerformanceMonitoring()
                    context?.let {
                        showGetVariantCategoryCombinationErrorToast(
                                ErrorHandler.getErrorMessage(it, result.throwable))
                        viewModel.isEditMode.value?.let { isEditMode ->
                            trackOopsConnectionPageScreen(isEditMode, result.throwable, it)
                        }
                    }
                }
            }
        })
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(viewLifecycleOwner, Observer { productInputModel ->
            // extract selected variant details
            val selectedVariantDetails = viewModel.extractSelectedVariantDetails(productInputModel)
            // set selected variant details
            viewModel.setSelectedVariantDetails(selectedVariantDetails)
            // get all variant details
            val categoryId = productInputModel.detailInputModel.categoryId.toIntOrNull()
            val selections = productInputModel.variantInputModel.selections
            categoryId?.run { viewModel.getVariantCategoryCombination(this, selections) }
        })
    }

    private fun observeSizechartUrl() {
        viewModel.variantSizechart.observe(viewLifecycleOwner, Observer {
            if (it.urlOriginal.isEmpty()) {
                ivSizechartAddSign.visible()
                ivSizechartEditSign.gone()
                ivSizechart.gone()
                typographySizechartDescription.text = getString(R.string.label_variant_sizechart_description)
            } else {
                ivSizechartAddSign.gone()
                ivSizechartEditSign.visible()
                ivSizechart.visible()
                typographySizechartDescription.text = getString(R.string.label_variant_sizechart_edit_description)
            }

            // display sizechart image (use server image if exist)
            ivSizechart.setImage(it.urlOriginal, 0F)
        })
    }

    private fun observeInputStatus() {
        viewModel.isInputValid.observe(viewLifecycleOwner, Observer {
            tvDeleteAll?.isEnabled = it
            if (viewModel.isRemovingVariant.value == true) {
                buttonSave.isEnabled = true // always enable save button if removing variant activated
            } else {
                buttonSave.isEnabled = it
            }
        })
    }

    private fun observeIsEditMode() {
        viewModel.isEditMode.observe(viewLifecycleOwner, Observer { isEditMode ->
            // track the screen
            if (isEditMode) ProductEditVariantTracking.trackScreen(isLoggedin, userId)
            else ProductAddVariantTracking.trackScreen(isLoggedin, userId)
        })
    }

    private fun observeVariantPhotosVisibility() {
        viewModel.isVariantPhotosVisible.observe(viewLifecycleOwner, Observer { isVisible ->
            if (isVisible) variantPhotoLayout.show()
            else variantPhotoLayout.hide()
        })
    }

    private fun observeSizechartVisibility() {
        viewModel.isVariantSizechartVisible.observe(viewLifecycleOwner, Observer {
            layoutSizechart.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun observeisRemovingVariant() {
        viewModel.isRemovingVariant.observe(viewLifecycleOwner, Observer {
            buttonSave.text = if (it) {
                getString(com.tokopedia.product.addedit.R.string.action_variant_save)
            } else {
                getString(com.tokopedia.product.addedit.R.string.action_variant_next)
            }
        })
    }

    private fun setupAddEditVariantPage(variantDataList: List<VariantDetail>, selectedVariantDetails: List<VariantDetail>) {
        // setup variant type section view
        variantTypeAdapter?.setData(variantDataList)
        variantTypeAdapter?.setMaxSelectedItems(MAX_SELECTED_VARIANT_TYPE)
        // set selected variant types
        variantTypeAdapter?.setSelectedItems(selectedVariantDetails)
        // if editing old variant data (given data is reversed) then you should reverse
        // selectedVariantDetails data first
        viewModel.updateIsOldVariantData(variantTypeAdapter?.getSelectedItems().orEmpty(), selectedVariantDetails)
        val displayedVariantDetail = if (viewModel.isOldVariantData) {
            selectedVariantDetails.reversed()
        } else {
            selectedVariantDetails
        }
        // update variant selection state
        if (selectedVariantDetails.size == 1) viewModel.isSingleVariantTypeIsSelected = true
        // set selected variant unit and values
        displayedVariantDetail.forEachIndexed { index, variantDetail ->

            val selectedVariantUnit = variantDetail.units.firstOrNull()
                    ?: Unit()
            val selectedVariantUnitValues = variantDetail.units.firstOrNull()?.unitValues
                    ?: mutableListOf()
            val selectedVariantData = variantDataList.first {
                it.variantID == variantDetail.variantID
            }

            // add custom unit values to variant data
            val selectedCustomVariantUnitValues = selectedVariantUnitValues.filter {
                it.variantUnitValueID == CUSTOM_VARIANT_UNIT_VALUE_ID
            }
            // add custom variant unit values to variant data
            if (selectedCustomVariantUnitValues.isNotEmpty()) {
                // find the unit and add the values
                selectedVariantData.units.find {
                    it.variantUnitID == selectedVariantUnit.variantUnitID
                }?.unitValues?.addAll(selectedCustomVariantUnitValues)
            }

            val adapterPosition = variantDataList.indexOfFirst {
                it.variantID == variantDetail.variantID
            }

            when (index) {
                VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                    setupVariantValueSection(VARIANT_VALUE_LEVEL_ONE_POSITION, selectedVariantData, selectedVariantUnitValues)
                    // update adapter - layout position map
                    viewModel.updateVariantValuesLayoutMap(adapterPosition, VARIANT_VALUE_LEVEL_ONE_POSITION)
                    // update view model selected variant unit level1
                    viewModel.updateSelectedVariantUnitMap(VARIANT_VALUE_LEVEL_ONE_POSITION, selectedVariantUnit)
                    // update view model selected variant unit values level 1
                    viewModel.updateSelectedVariantUnitValuesLevel1(selectedVariantUnitValues)
                    // update layout position - selected variant unit values
                    viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, selectedVariantUnitValues.toMutableList())
                }
                VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                    setupVariantValueSection(VARIANT_VALUE_LEVEL_TWO_POSITION, selectedVariantData, selectedVariantUnitValues)
                    // update adapter - layout position map
                    viewModel.updateVariantValuesLayoutMap(adapterPosition, VARIANT_VALUE_LEVEL_TWO_POSITION)
                    // update view model selected variant unit level2
                    viewModel.updateSelectedVariantUnitMap(VARIANT_VALUE_LEVEL_TWO_POSITION, selectedVariantUnit)
                    // update view model selected variant unit values level 2
                    viewModel.updateSelectedVariantUnitValuesLevel2(selectedVariantUnitValues)
                    // update layout position - selected variant unit values
                    viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_TWO_POSITION, selectedVariantUnitValues.toMutableList())
                }
            }
            // show the variant photos
            viewModel.showProductVariantPhotos(variantDetail)
        }

        // set product variant images
        viewModel.productInputModel.value?.run {
            val variantPhotos = viewModel.getProductVariantPhotos(this)
            if (variantPhotos.isNotEmpty()) {
                // populate the adapter with photos
                variantPhotoAdapter?.setData(variantPhotos)
            }
        }

        // set product variant size chart
        val sizechart = viewModel.productInputModel.value?.variantInputModel?.sizecharts
                ?: PictureVariantInputModel()
        viewModel.updateSizechart(sizechart)

        // update sizechart visibility based on variant selected type
        viewModel.updateSizechartFieldVisibility(variantTypeAdapter?.getSelectedItems().orEmpty())

    }

    private fun showExitConfirmationDialog() {
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(getString(R.string.label_variant_exit_dialog_title))
            setDescription(getString(R.string.label_variant_exit_dialog_desc))
            setPrimaryCTAText(getString(R.string.action_cancel_exit))
            setPrimaryCTAClickListener {
                dialog.dismiss()
            }
            setSecondaryCTAText(getString(R.string.action_confirm_exit))
            setSecondaryCTAClickListener {
                dialog.dismiss()
                activity?.finish()
            }
        }
        dialog.show()
    }

    private fun showRemoveVariantDialog() {
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(getString(R.string.label_variant_delete_all_title))
            setDescription(getString(R.string.label_variant_delete_all_description))
            setPrimaryCTAText(getString(R.string.action_variant_delete_all_negative))
            setPrimaryCTAClickListener {
                dialog.dismiss()
            }
            setSecondaryCTAText(getString(R.string.action_variant_delete_all_positive))
            setSecondaryCTAClickListener {
                dialog.dismiss()
                removeVariant()
                // track product variant reset
                viewModel.isEditMode.value?.let { isEditMode ->
                    if (isEditMode) ProductEditVariantTracking.confirmProductVariantReset(shopId)
                    else ProductAddVariantTracking.confirmProductVariantReset(shopId)
                }
            }
        }
        dialog.show()
    }

    private fun showReplaceVariantUnitDialog(oldUnitName: String, newUnit: Unit, variantData: VariantDetail, layoutPosition: Int) {
        val dialogTitle = getString(R.string.label_variant_replace) +
                " " + oldUnitName + " " + getString(R.string.label_variant_to) +
                " " + newUnit.unitName + getString(R.string.label_question_mark)
        val dialogDesc = getString(R.string.label_variant_data) +
                " " + oldUnitName +
                " " + getString(R.string.label_variant_replacement_notice) +
                " " + newUnit.unitName
        val dialog = DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(dialogTitle)
            setDescription(dialogDesc)
            setPrimaryCTAText(getString(R.string.action_cancel_replacement))
            setPrimaryCTAClickListener {
                dialog.dismiss()
                // show picker dialog to retain the UI states
                variantDataValuePicker?.dialog?.show()
            }
            setSecondaryCTAText(getString(R.string.action_confirm_replacement))
            setSecondaryCTAClickListener {
                dialog.dismiss()
                // remove the previous dialog fragment
                variantDataValuePicker?.dismiss()
                // remove all the previous variant selected values
                showVariantDataValuePicker(variantData, layoutPosition, newUnit, mutableListOf())
            }
        }
        dialog.show()
    }

    private fun removeVariant() {
        viewModel.removeVariant()
        variantValueAdapterLevel1?.setData(emptyList())
        variantValueAdapterLevel2?.setData(emptyList())
        variantPhotoAdapter?.setData(emptyList())
        variantValueLevel1Layout.hide()
        variantValueLevel2Layout.hide()
        removeSizechart()
        layoutSizechart.hide()
    }

    private fun removeSizechart() {
        val url = viewModel.variantSizechart.value?.urlOriginal.orEmpty()
        viewModel.updateSizechart("")
    }

    private fun showSizechartPicker() {
        val ctx = context ?: return
        val isEditMode = viewModel.isEditMode.value ?: false
        val builder = ImagePickerBuilder.getSquareImageBuilder(ctx)
                .withSimpleEditor()
        builder.imagePickerEditorBuilder?.convertToWebp = true
        ImagePickerGlobalSettings.onImageEditorContinue = onImagePickerEditContinue(ctx, isEditMode)
        val intent = RouteManager.getIntent(ctx, ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(builder)
        startActivityForResult(intent, REQUEST_CODE_SIZECHART_IMAGE)
    }

    private fun onImagePickerEditContinue(context: Context, isInEditMode: Boolean): ImagePickerCallback {
        return ImagePickerCallback(context) { it, _ ->
            val shopId = UserSession(it).shopId.orEmpty()
            if (isInEditMode)
                ProductEditVariantTracking.pickSizeChartImage(shopId)
            else
                ProductAddVariantTracking.pickSizeChartImage(shopId)
        }
    }

    private fun showEditorSizechartPicker() {
        val ctx = context ?: return
        val urlOrPath = viewModel.variantSizechart.value?.urlOriginal ?: ""

        val isEditMode = viewModel.isEditMode.value ?: false
        val builder = ImageEditorBuilder(
                imageUrls = arrayListOf(urlOrPath),
                defaultRatio = ImageRatioType.RATIO_1_1,
                convertToWebp = true
        )
        val intent = RouteManager.getIntent(ctx, ApplinkConstInternalGlobal.IMAGE_EDITOR)
        intent.putImageEditorBuilder(builder)
        ImagePickerGlobalSettings.onImageEditorContinue = onImagePickerEditContinue(ctx, isEditMode)
        startActivityForResult(intent, REQUEST_CODE_SIZECHART_IMAGE)
    }

    private fun showGetVariantCategoryCombinationErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(com.tokopedia.abstraction.R.string.title_try_again),
                    duration = Snackbar.LENGTH_INDEFINITE,
                    clickListener = View.OnClickListener {
                        val categoryId = viewModel.productInputModel.value?.detailInputModel?.categoryId
                        val selections = viewModel.productInputModel.value?.variantInputModel?.selections
                                ?: listOf()
                        categoryId?.let { id ->
                            val paramId = id.toIntOrNull()
                            paramId?.run { viewModel.getVariantCategoryCombination(this, selections) }
                        }
                    })
        }
    }

    private fun setRecyclerViewToFlex(recyclerView: RecyclerView) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        recyclerView.apply {
            layoutManager = flexboxLayoutManager
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)))
        }
    }

    private fun setRecyclerViewToHorizontal(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)))
        }
    }

    private fun startAddEditProductVariantDetailActivity() {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel.value)
            }
            val intent = AddEditProductVariantDetailActivity.createInstance(context, cacheManager.id)
            startActivityForResult(intent, REQUEST_CODE_VARIANT_DETAIL)
        }
    }

    private fun trackAddingVariantDetailValueEvent(isEditMode: Boolean, variantTypeName: String, shopId: String) {
        if (isEditMode) ProductEditVariantTracking.addingVariantDetailValue(variantTypeName, shopId)
        else ProductAddVariantTracking.addingVariantDetailValue(variantTypeName, shopId)
    }

    private fun trackSelectingVariantUnitEvent(isEditMode: Boolean, variantTypeName: String, shopId: String) {
        if (isEditMode) ProductEditVariantTracking.selectingVariantUnit(variantTypeName, shopId)
        else ProductAddVariantTracking.selectingVariantUnit(variantTypeName, shopId)
    }

    private fun trackSelectVariantUnitValueEvent(isEditMode: Boolean, eventLabel: String, shopId: String) {
        if (isEditMode) ProductEditVariantTracking.selectVariantUnitValue(eventLabel, shopId)
        else ProductAddVariantTracking.selectVariantUnitValue(eventLabel, shopId)
    }

    private fun trackSaveVariantUnitValueEvent(isEditMode: Boolean, eventLabel: String, shopId: String) {
        if (isEditMode) ProductEditVariantTracking.saveVariantUnitValues(eventLabel, shopId)
        else ProductAddVariantTracking.saveVariantUnitValues(eventLabel, shopId)
    }

    private fun trackSaveCustomVariantUnitValueEvent(isEditMode: Boolean, eventLabel: String, shopId: String) {
        if (isEditMode) ProductEditVariantTracking.saveCustomVariantUnitValue(eventLabel, shopId)
        else ProductAddVariantTracking.saveCustomVariantUnitValue(eventLabel, shopId)
    }

    private fun trackRemoveVariantUnitValueEvent(isEditMode: Boolean, eventLabel: String, shopId: String) {
        if (isEditMode) ProductEditVariantTracking.removeVariantUnitValue(eventLabel, shopId)
        else ProductAddVariantTracking.removeVariantUnitValue(eventLabel, shopId)
    }

    private fun trackOopsConnectionPageScreen(isEditMode: Boolean, throwable: Throwable, context: Context) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        val errorName = AddEditProductUploadErrorHandler.getErrorName(throwable)
        if (isEditMode) {
            ProductEditStepperTracking.oopsConnectionPageScreen(
                    userId,
                    errorMessage,
                    errorName)
        } else {
            ProductAddStepperTracking.oopsConnectionPageScreen(
                    userId,
                    errorMessage,
                    errorName)
        }
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(R.id.toolbar_variant)?.apply {
            headerTitle = getString(R.string.title_variant_activity)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            actionTextView?.setOnClickListener {
                showRemoveVariantDialog()
            }
            actionTextView?.text = getString(R.string.action_variant_delete)
            tvDeleteAll = actionTextView
            tvDeleteAll?.isEnabled = false
            tvDeleteAll?.visible()
        }
    }
}
