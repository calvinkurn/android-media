package com.tokopedia.product.addedit.variant.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.imagepicker.common.util.FileUtils
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.imagepicker.view.activity.SizechartPickerAddProductActivity
import com.tokopedia.product.addedit.imagepicker.view.activity.SizechartPickerEditPhotoActivity
import com.tokopedia.product.addedit.imagepicker.view.activity.VariantPhotoPickerActivity
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.tracking.ProductAddVariantTracking
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
        VariantUnitPicker.OnVariantUnitPickListener, VariantDataValuePicker.OnVariantUnitPickerClickListener, VariantPhotoAdapter.OnItemClickListener, VariantDataValuePicker.OnVariantUnitValuePickListener {

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
    private var variantValuePicker: BottomSheetUnify? = null
    private var variantUnitPicker: BottomSheetUnify? = null
    private var customVariantValueInputForm: BottomSheetUnify? = null
    private var cancellationDialog: DialogUnify? = null
    private var tvDeleteAll: TextView? = null

    private var userSession: UserSessionInterface? = null
    private var isLoggedin = ""
    private var userId = ""
    private var shopId = ""


    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(AddEditProductVariantComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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

        variantTypeAdapter = VariantTypeAdapter(this)
        variantValueAdapterLevel1 = VariantValueAdapter(this, VARIANT_VALUE_LEVEL_ONE_POSITION)
        variantValueAdapterLevel2 = VariantValueAdapter(this, VARIANT_VALUE_LEVEL_TWO_POSITION)
        variantPhotoAdapter = VariantPhotoAdapter(this)

        recyclerViewVariantType.adapter = variantTypeAdapter
        recyclerViewVariantValueLevel1.adapter = variantValueAdapterLevel1
        recyclerViewVariantValueLevel2.adapter = variantValueAdapterLevel2
        recyclerViewVariantPhoto.adapter = variantPhotoAdapter
        setRecyclerViewToFlex(recyclerViewVariantType)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel1)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel2)
        setRecyclerViewToHorizontal(recyclerViewVariantPhoto)

        // setup base cancellation dialog
        context?.run {
            cancellationDialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            cancellationDialog?.setPrimaryCTAText(getString(R.string.action_cancel_cancellation))
            cancellationDialog?.setSecondaryCTAText(getString(R.string.action_confirm_cancellation))
            cancellationDialog?.setPrimaryCTAClickListener {
                cancellationDialog?.dismiss()
            }
        }

        observeSizechartUrl()
        observeGetCategoryVariantCombinationResult()
        observeProductInputModel()
        observeInputStatus()
        observeSizechartVisibility()
        observeVariantPhotosVisibility()
        observeIsEditMode()
        observeIsSelectedVariantUnitValuesEmpty()

        cardSizechart.setOnClickListener {
            onSizechartClicked()
        }

        // button "tambah" variant values level 1 on click listener
        linkAddVariantValueLevel1.setOnClickListener {
            val variantDetail: VariantDetail = it.getTag(R.id.variant_detail) as VariantDetail
            val selectedVariantUnitValues = mutableListOf<UnitValue>()
            if (variantDetail.units.isEmpty()) showCustomVariantInputForm(VARIANT_VALUE_LEVEL_ONE_POSITION, variantDetail, Unit(), listOf(), selectedVariantUnitValues)
            else {
                selectedVariantUnitValues.addAll((viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_ONE_POSITION)))
                showVariantValuePicker(variantDetail, VARIANT_VALUE_LEVEL_ONE_POSITION, viewModel.getSelectedVariantUnit(VARIANT_VALUE_LEVEL_ONE_POSITION), selectedVariantUnitValues)
            }
            viewModel.isEditMode.value?.let { isEditMode ->
                val variantTypeName = variantDetail.name
                trackAddingVariantDetailValueEvent(isEditMode, variantTypeName, shopId)
            }
        }

        // button "tambah" variant values level 2 on click listener
        linkAddVariantValueLevel2.setOnClickListener {
            val variantDetail: VariantDetail = it.getTag(R.id.variant_detail) as VariantDetail
            val selectedVariantUnitValues = mutableListOf<UnitValue>()
            if (variantDetail.units.isEmpty()) showCustomVariantInputForm(VARIANT_VALUE_LEVEL_TWO_POSITION, variantDetail, Unit(), listOf(), selectedVariantUnitValues)
            else {
                selectedVariantUnitValues.addAll((viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_TWO_POSITION)))
                showVariantValuePicker(variantDetail, VARIANT_VALUE_LEVEL_TWO_POSITION, viewModel.getSelectedVariantUnit(VARIANT_VALUE_LEVEL_TWO_POSITION), selectedVariantUnitValues)
            }
            viewModel.isEditMode.value?.let { isEditMode ->
                val variantTypeName = variantDetail.name
                trackAddingVariantDetailValueEvent(isEditMode, variantTypeName, shopId)
            }
        }

        // button save on click listener
        buttonSave.setOnClickListener {
            val variantPhotos = variantPhotoAdapter?.getData().orEmpty()
            val selectedVariantDetails = variantTypeAdapter?.getSelectedItems().orEmpty()
            viewModel.updateVariantInputModel(selectedVariantDetails, variantPhotos)
            startAddEditProductVariantDetailActivity()
        }

        setupToolbarActions()
    }

    override fun onVariantTypeSelected(adapterPosition: Int, variantDetail: VariantDetail) {

        // clear photo data if have new level structure
        variantPhotoAdapter?.clearImageData()

        // track selected variant type
        viewModel.isEditMode.value?.let { isEditMode ->
            val variantTypeName = variantDetail.name
            if (isEditMode) ProductEditVariantTracking.selectVariantType(variantTypeName, shopId)
            else ProductAddVariantTracking.selectVariantType(variantTypeName, shopId)
        }

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
    }

    override fun onVariantTypeDeselected(adapterPosition: Int, variantDetail: VariantDetail): Boolean {
        // clear photo data if have new level structure
        variantPhotoAdapter?.clearImageData()

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
        // if removing all variants then save the changes
        if (viewModel.isSelectedVariantUnitValuesEmpty.value == true) {
            submitVariantInput()
        } else {
            showExitConfirmationDialog()
        }
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
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                viewModel.updateSelectedVariantUnitValuesLevel1(mutableListOf())
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                viewModel.updateSelectedVariantUnitValuesLevel2(mutableListOf())
            }
        }
        viewModel.hideProductVariantPhotos(variantDetail)
        viewModel.updateSizechartFieldVisibility(variantDetail, false)
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
                linkAddVariantValueLevel1.setTag(R.id.variant_detail, variantTypeDetail)
                viewModel.updateVariantDataMap(VARIANT_VALUE_LEVEL_ONE_POSITION, variantTypeDetail)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                variantValueLevel2Layout.show()
                variantValueAdapterLevel2?.setData(selectedVariantUnitValues)
                typographyVariantValueLevel2Title.text = variantTypeDetail.name
                linkAddVariantValueLevel2.setTag(R.id.variant_detail, variantTypeDetail)
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

        variantValuePicker?.dismiss()

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

    override fun onVariantUnitPicked(selectedVariantUnit: Unit, layoutPosition: Int) {
        variantUnitPicker?.dismiss()
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel1.getTag(R.id.variant_detail) as VariantDetail
                val selectedVariantUnitValues = mutableListOf<UnitValue>()
                showVariantValuePicker(variantDetail, layoutPosition, selectedVariantUnit, selectedVariantUnitValues)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel2.getTag(R.id.variant_detail) as VariantDetail
                val selectedVariantUnitValues = mutableListOf<UnitValue>()
                showVariantValuePicker(variantDetail, layoutPosition, selectedVariantUnit, selectedVariantUnitValues)
            }
        }
    }

    override fun onVariantUnitPickerClicked(layoutPosition: Int, selectedVariantUnit: Unit) {
        variantValuePicker?.dismiss()
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel1.getTag(R.id.variant_detail) as VariantDetail
                showVariantUnitPicker(selectedVariantUnit, variantDetail, layoutPosition)
                // track selecting variant unit event
                viewModel.isEditMode.value?.let { isEditMode ->
                    val variantTypeName = variantDetail.name
                    trackSelectingVariantUnitEvent(isEditMode, variantTypeName, shopId)
                }
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel2.getTag(R.id.variant_detail) as VariantDetail
                showVariantUnitPicker(selectedVariantUnit, variantDetail, layoutPosition)
                // track selecting variant unit event
                viewModel.isEditMode.value?.let { isEditMode ->
                    val variantTypeName = variantDetail.name
                    trackSelectingVariantUnitEvent(isEditMode, variantTypeName, shopId)
                }
            }
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
        variantValuePicker?.dismiss()
        val variantData = viewModel.getVariantData(layoutPosition)
        showCustomVariantInputForm(layoutPosition, variantData, selectedVariantUnit, variantUnitValues, selectedVariantUnitValues)
    }

    override fun onCustomVariantUnitValueAdded(layoutPosition: Int,
                                               selectedVariantUnit: Unit,
                                               customVariantUnitValue: UnitValue,
                                               currentSelectedVariantUnitValues: MutableList<UnitValue>) {
        // close the custom variant value input form
        customVariantValueInputForm?.dismiss()
        // add the added custom value to variant data and selected variant unit values
        viewModel.addCustomVariantUnitValue(layoutPosition, selectedVariantUnit, customVariantUnitValue)
        // get the variant data for tracking and showing the variant data picker
        val variantData = viewModel.getVariantData(layoutPosition)
        // track trackSaveCustomVariantUnitValueEvent
        val label = variantData.name + " - " + customVariantUnitValue.value
        viewModel.isEditMode.value?.let { isEditMode ->
            trackSaveCustomVariantUnitValueEvent(isEditMode, label, shopId)
        }
        // original selected variant unit values
        val selectedVariantUnitValues = mutableListOf<UnitValue>()
        selectedVariantUnitValues.addAll((viewModel.getSelectedVariantUnitValues(layoutPosition)))
        // unconfirmed selection during the selection process
        val unConfirmedSelection = currentSelectedVariantUnitValues.minus(selectedVariantUnitValues)
        // show the variant data picker again
        showVariantValuePicker(variantData, layoutPosition, selectedVariantUnit, selectedVariantUnitValues, customVariantUnitValue, unConfirmedSelection)
    }

    override fun onRemoveButtonClicked(position: Int, layoutPosition: Int, removedUnitValue: UnitValue) {

        viewModel.removeSelectedVariantUnitValue(layoutPosition, removedUnitValue)

        var variantId = 0

        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel1.getTag(R.id.variant_detail) as VariantDetail
                variantId = variantDetail.variantID
                // track remove variant unit value event
                val typeName = variantDetail.name
                val unitValueName = removedUnitValue.value
                val label = "$typeName - $unitValueName"
                viewModel.isEditMode.value?.let { isEditMode ->
                    trackRemoveVariantUnitValueEvent(isEditMode, label, shopId)
                }
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel2.getTag(R.id.variant_detail) as VariantDetail
                variantId = variantDetail.variantID
                // track remove variant unit value event
                val typeName = variantDetail.name
                val unitValueName = removedUnitValue.value
                val label = "$typeName - $unitValueName"
                viewModel.isEditMode.value?.let { isEditMode ->
                    trackRemoveVariantUnitValueEvent(isEditMode, label, shopId)
                }
            }
        }

        if (variantId == COLOUR_VARIANT_TYPE_ID) {
            variantPhotoAdapter?.removeData(position)
            if (variantPhotoAdapter?.getData()?.size == 0) variantPhotoLayout.hide()
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
                    val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    imageUrlOrPathList.forEach {
                        viewModel.updateSizechart(it)
                    }
                }
                REQUEST_CODE_VARIANT_PHOTO_IMAGE -> {
                    val imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    if (imageUrlOrPathList.isNotEmpty()) {
                        val imageUrlOrPath = imageUrlOrPathList.first()
                        val position = viewModel.clickedVariantPhotoItemPosition ?: 0
                        variantPhotoAdapter?.updateImageData(imageUrlOrPath, position)
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

    fun submitVariantInput() {
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

    private fun showVariantValuePicker(variantData: VariantDetail,
                                       layoutPosition: Int,
                                       selectedVariantUnit: Unit,
                                       selectedVariantUnitValues: MutableList<UnitValue>,
                                       addedCustomVariantUnitValue: UnitValue = UnitValue(),
                                       unConfirmedSelection: List<UnitValue> = listOf()) {
        variantValuePicker = BottomSheetUnify()
        variantValuePicker?.setTitle("Pilih " + variantData.name)
        variantValuePicker?.showCloseIcon = false
        variantValuePicker?.clearContentPadding = true
        variantValuePicker?.showKnob = true
        // set the bottom sheet to full screen
        variantValuePicker?.setShowListener {
            variantValuePicker?.bottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        }
        val variantValuePickerLayout = VariantDataValuePicker(requireContext(), layoutPosition, variantData, this, this, this, this)
        variantValuePickerLayout.setupVariantDataValuesPicker(selectedVariantUnit, selectedVariantUnitValues, addedCustomVariantUnitValue, unConfirmedSelection)
        variantValuePicker?.setChild(variantValuePickerLayout)
        variantValuePicker?.show(this@AddEditProductVariantFragment.childFragmentManager, TAG_VARIANT_UNIT_VALUE_PICKER)
    }

    private fun showVariantUnitPicker(selectedVariantUnit: Unit, variantDetail: VariantDetail, layoutPosition: Int) {
        variantUnitPicker = BottomSheetUnify()
        variantUnitPicker?.setTitle("Pilih " + variantDetail.name)
        variantUnitPicker?.showCloseIcon = true
        variantUnitPicker?.clearContentPadding = true
        val variantUnitPickerLayout = VariantUnitPicker(context)
        variantUnitPickerLayout.setLayoutPosition(layoutPosition)
        variantUnitPickerLayout.setSelectedVariantUnit(selectedVariantUnit)
        variantUnitPickerLayout.setOnVariantUnitPickListener(this)
        variantUnitPickerLayout.setupVariantUnitPicker(variantDetail.units)
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
        customVariantValueInputForm?.isKeyboardOverlap = false
        val customVariantValueInputLayout = CustomVariantUnitValueForm(requireContext(), layoutPosition, variantUnitValues, this)
        customVariantValueInputLayout.setupVariantCustomInputLayout(selectedVariantUnit, selectedVariantUnitValues)
        customVariantValueInputForm?.setChild(customVariantValueInputLayout)
        customVariantValueInputForm?.show(this@AddEditProductVariantFragment.childFragmentManager, TAG_CUSTOM_VARIANT_UNIT_VALUE_INPUT_FORM)
    }

    private fun showPhotoVariantPicker() {
        context?.apply {
            val isEditMode = viewModel.isEditMode.value ?: false
            val intent = VariantPhotoPickerActivity.getIntent(this, isEditMode)
            startActivityForResult(intent, REQUEST_CODE_VARIANT_PHOTO_IMAGE)
        }
    }

    private fun onSizechartClicked() {
        if (viewModel.variantSizechart.value?.filePath.isNullOrEmpty()) {
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

    private fun observeGetCategoryVariantCombinationResult() {
        viewModel.getCategoryVariantCombinationResult.observe(this, Observer { result ->
            // clear adapter before rendering
            variantTypeAdapter?.setData(emptyList())
            when (result) {
                is Success -> {
                    // master data from back end
                    val variantDataList = result.data.getCategoryVariantCombination.data.variantDetails
                    // selected variant details
                    val selectedVariantDetails = viewModel.getSelectedVariantDetails()
                    // setup the page
                    setupAddEditVariantPage(variantDataList, selectedVariantDetails)
                }
                is Fail -> {
                    context?.let {
                        showGetCategoryVariantCombinationErrorToast(
                                ErrorHandler.getErrorMessage(it, result.throwable))
                    }
                }
            }
        })
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(this, Observer { productInputModel ->
            // TODO - change flow, able to causing race condition
            // extract selected variant details
            val selectedVariantDetails = viewModel.extractSelectedVariantDetails(productInputModel)
            // set selected variant details
            viewModel.setSelectedVariantDetails(selectedVariantDetails)
            // get all variant details
            val categoryId = productInputModel.detailInputModel.categoryId
            viewModel.getCategoryVariantCombination(categoryId)
        })
    }

    private fun observeSizechartUrl() {
        viewModel.variantSizechart.observe(this, Observer {
            if (it.filePath.isEmpty()) {
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
            if (it.urlThumbnail.isNotEmpty()) {
                ivSizechart.setImage(it.urlThumbnail, 0F)
            } else {
                ivSizechart.setImage(it.filePath, 0F)
            }
        })
    }

    private fun observeInputStatus() {
        viewModel.isInputValid.observe(this, Observer {
            buttonSave.isEnabled = it
        })
    }

    private fun observeIsEditMode() {
        viewModel.isEditMode.observe(this, Observer { isEditMode ->
            // track the screen
            if (isEditMode) ProductEditVariantTracking.trackScreen(isLoggedin, userId)
            else ProductAddVariantTracking.trackScreen(isLoggedin, userId)
        })
    }

    private fun observeIsSelectedVariantUnitValuesEmpty() {
        viewModel.isSelectedVariantUnitValuesEmpty.observe(this, Observer { isSelectedVariantUnitValuesEmpty ->
            // hide reset button if selected variant unit values exist
            tvDeleteAll?.visibility = if (!isSelectedVariantUnitValuesEmpty) View.VISIBLE else View.GONE
        })
    }

    private fun observeVariantPhotosVisibility() {
        viewModel.isVariantPhotosVisible.observe(this, Observer { isVisible ->
            if (isVisible) variantPhotoLayout.show()
            else variantPhotoLayout.hide()
        })
    }

    private fun observeSizechartVisibility() {
        viewModel.isVariantSizechartVisible.observe(this, Observer {
            layoutSizechart.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun setupAddEditVariantPage(variantDataList: List<VariantDetail>, selectedVariantDetails: List<VariantDetail>) {
        // setup variant type section view
        variantTypeAdapter?.setData(variantDataList)
        variantTypeAdapter?.setMaxSelectedItems(MAX_SELECTED_VARIANT_TYPE)
        // set selected variant types
        variantTypeAdapter?.setSelectedItems(selectedVariantDetails)
        // update variant selection state
        if (selectedVariantDetails.size == 1) viewModel.isSingleVariantTypeIsSelected = true
        // set selected variant unit and values
        selectedVariantDetails.forEachIndexed { index, variantDetail ->

            val selectedVariantUnit = variantDetail.units.firstOrNull()
                    ?: Unit()
            val selectedVariantUnitValues = variantDetail.units.firstOrNull()?.unitValues
                    ?: mutableListOf()
            val selectedVariantData = variantDataList.first {
                it.variantID == variantDetail.variantID
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
        val url = viewModel.variantSizechart.value?.filePath.orEmpty()
        viewModel.updateSizechart("")
        FileUtils.deleteFileInTokopediaFolder(url)
    }

    private fun showSizechartPicker() {
        context?.apply {
            val isEditMode = viewModel.isEditMode.value ?: false
            val intent = SizechartPickerAddProductActivity.getIntent(this, isEditMode)
            startActivityForResult(intent, REQUEST_CODE_SIZECHART_IMAGE)
        }
    }

    private fun showEditorSizechartPicker() {
        val urlOrPath = viewModel.variantSizechart.value?.run {
            if (urlOriginal.isNotEmpty()) {
                // if sizechart image is from server, then use image url
                urlOriginal
            } else {
                // if sizechart image is from device, then use file path
                filePath
            }
        }.orEmpty()

        context?.apply {
            val isEditMode = viewModel.isEditMode.value ?: false
            val editorIntent = SizechartPickerEditPhotoActivity.createIntent(this, urlOrPath, isEditMode)
            startActivityForResult(editorIntent, REQUEST_CODE_SIZECHART_IMAGE)
        }
    }

    private fun showGetCategoryVariantCombinationErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(R.string.title_try_again),
                    duration = Snackbar.LENGTH_INDEFINITE,
                    clickListener = View.OnClickListener {
                        val categoryId = viewModel.productInputModel.value?.detailInputModel?.categoryId
                        categoryId?.let { id ->
                            viewModel.getCategoryVariantCombination(id)
                        }
                    })
        }
    }

    private fun setRecyclerViewToFlex(recyclerView: RecyclerView) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        recyclerView.apply {
            layoutManager = flexboxLayoutManager
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_lvl3)))
        }
    }

    private fun setRecyclerViewToHorizontal(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_lvl3)))
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
        }
    }
}
