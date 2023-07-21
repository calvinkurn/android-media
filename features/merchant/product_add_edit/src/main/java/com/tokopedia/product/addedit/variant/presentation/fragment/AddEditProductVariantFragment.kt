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
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkContentPosition
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.imagepicker.common.ImageEditorBuilder
import com.tokopedia.imagepicker.common.ImagePickerBuilder
import com.tokopedia.imagepicker.common.ImagePickerCallback
import com.tokopedia.imagepicker.common.ImagePickerGlobalSettings
import com.tokopedia.imagepicker.common.ImagePickerPageSource
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.ImageRatioType
import com.tokopedia.imagepicker.common.PICKER_RESULT_PATHS
import com.tokopedia.imagepicker.common.putImageEditorBuilder
import com.tokopedia.imagepicker.common.putImagePickerBuilder
import com.tokopedia.imagepicker.common.putParamPageSource
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.picker.common.MediaPicker
import com.tokopedia.picker.common.PageSource
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_PLT_NETWORK_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_PLT_PREPARE_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_PLT_RENDER_METRICS
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringConstants.ADD_EDIT_PRODUCT_VARIANT_TRACE
import com.tokopedia.product.addedit.analytics.AddEditProductPerformanceMonitoringListener
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.util.AddEditProductUploadErrorHandler
import com.tokopedia.product.addedit.common.util.DTDialogUtil
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.common.util.RecyclerViewItemDecoration
import com.tokopedia.product.addedit.common.util.RemoteConfig
import com.tokopedia.product.addedit.common.util.SharedPreferencesUtil
import com.tokopedia.product.addedit.common.util.getValueOrDefault
import com.tokopedia.product.addedit.common.util.setColorToDisabled
import com.tokopedia.product.addedit.common.util.setDefaultMaxWidth
import com.tokopedia.product.addedit.common.util.setFragmentToUnifyBgColor
import com.tokopedia.product.addedit.databinding.FragmentAddEditProductVariantBinding
import com.tokopedia.product.addedit.imagepicker.ImagePickerAddEditNavigation
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.tracking.CustomVariantTypeTracking
import com.tokopedia.product.addedit.tracking.MediaImprovementTracker
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
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.CUSTOM_VARIANT_TYPE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.CUSTOM_VARIANT_UNIT_VALUE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_CUSTOM_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_EDITOR_SIZECHART_IMAGE_MEDIA_PICKER
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_SIZECHART_IMAGE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_SIZECHART_IMAGE_MEDIA_PICKER
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_VARIANT_DETAIL
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_VARIANT_PHOTO_IMAGE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_VARIANT_PHOTO_IMAGE_MEDIA_PICKER
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_COUNT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.dialog.AddEditProductVariantSizechartDialogFragment
import com.tokopedia.product.addedit.variant.presentation.dialog.CustomVariantInputBottomSheet
import com.tokopedia.product.addedit.variant.presentation.dialog.CustomVariantManageBottomSheet
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
import com.tokopedia.utils.lifecycle.autoCleared
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
        private const val TAG_CUSTOM_VARIANT_UNIT_VALUE_INPUT_FORM =
            "VARIANT_UNIT_CUSTOM_VALUE_INPUT_FORM"

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

    @Inject
    lateinit var userSession: UserSessionInterface

    private var variantTypeAdapter: VariantTypeAdapter? = null
    private var variantValueAdapterLevel1: VariantValueAdapter? = null
    private var variantValueAdapterLevel2: VariantValueAdapter? = null
    private var variantPhotoAdapter: VariantPhotoAdapter? = null
    private var variantDataValuePicker: BottomSheetUnify? = null
    private var variantUnitPicker: BottomSheetUnify? = null
    private var customVariantValueInputForm: BottomSheetUnify? = null
    private var cancellationDialog: DialogUnify? = null
    private var tvDeleteAll: TextView? = null

    private val isLoggedin by lazy { userSession.isLoggedIn.toString() }
    private val userId by lazy { userSession.userId }
    private val shopId by lazy { userSession.shopId }

    private var binding by autoCleared<FragmentAddEditProductVariantBinding>()
    private val scrollViewContent by lazy { binding.scrollViewContent }
    private val variantValueLevel1Layout by lazy { binding.variantValueLevel1Layout.root }
    private val variantValueLevel2Layout by lazy { binding.variantValueLevel2Layout.root }
    private val linkAddVariantValueLevel1 by lazy { binding.variantValueLevel1Layout.linkAddVariantValueLevel1 }
    private val linkAddVariantValueLevel2 by lazy { binding.variantValueLevel2Layout.linkAddVariantValueLevel2 }
    private val typographyVariantValueLevel1Title by lazy { binding.variantValueLevel1Layout.typographyVariantValueLevel1Title }
    private val typographyVariantValueLevel2Title by lazy { binding.variantValueLevel2Layout.typographyVariantValueLevel2Title }
    private val recyclerViewVariantValueLevel1 by lazy { binding.variantValueLevel1Layout.recyclerViewVariantValueLevel1 }
    private val recyclerViewVariantValueLevel2 by lazy { binding.variantValueLevel2Layout.recyclerViewVariantValueLevel2 }
    private val variantPhotoLayout by lazy { binding.variantPhotoLayout.root }
    private val recyclerViewVariantPhoto by lazy { binding.variantPhotoLayout.recyclerViewVariantPhoto }
    private val titleLayoutVariantType by lazy { binding.layoutVariantType.titleLayoutVariantType }
    private val buttonAddVariantType by lazy { binding.layoutVariantType.buttonAddVariantType }
    private val recyclerViewVariantType by lazy { binding.layoutVariantType.recyclerViewVariantType }
    private val layoutSizechart by lazy { binding.layoutSizechart }
    private val cardSizechart by lazy { layoutSizechart.cardSizechart }
    private val ivSizechartAddSign by lazy { layoutSizechart.ivSizechartAddSign }
    private val ivSizechartEditSign by lazy { layoutSizechart.ivSizechartEditSign }
    private val ivSizechart by lazy { layoutSizechart.ivSizechart }
    private val typographySizechartDescription by lazy { layoutSizechart.typographySizechartDescription }
    private val buttonSave by lazy { binding.buttonSave }

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
            viewModel.productInputModel.value = saveInstanceCacheManager.get(
                EXTRA_PRODUCT_INPUT_MODEL,
                ProductInputModel::class.java
            ) ?: ProductInputModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditProductVariantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set bg color programatically, to reduce overdraw
        setFragmentToUnifyBgColor()

        // setup views
        setupButtonAddVariantType()
        setupTitleLayoutVariantType()
        setupVariantRecyclerViews()
        setupBaseCancelationDialog()
        setupButtonSave()
        setupToolbarActions()

        // sizechart onclick picture listener
        cardSizechart.setOnClickListener {
            onSizechartClicked()
        }

        // button "tambah" variant values level 1 on click listener
        linkAddVariantValueLevel1.setOnClickListener {
            addVariantValueAtLevel(VARIANT_VALUE_LEVEL_ONE_POSITION)
        }

        // button "tambah" variant values level 2 on click listener
        linkAddVariantValueLevel2.setOnClickListener {
            addVariantValueAtLevel(VARIANT_VALUE_LEVEL_TWO_POSITION)

        }

        observeIsEditMode()
        observeSizechartUrl()
        observeGetVariantCategoryCombinationResult()
        observeProductInputModel()
        observeInputStatus()
        observeSizechartVisibility()
        observeVariantPhotosVisibility()
        observeIsEditMode()
        observeIsRemovingVariant()

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
            val selectedVariantUnitValuesLevel1 =
                viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_ONE_POSITION)
            // setup variant unit level 1 section
            setupVariantValueSection(
                VARIANT_VALUE_LEVEL_ONE_POSITION,
                variantDetail,
                selectedVariantUnitValuesLevel1
            )
            // update adapter - layout position map
            viewModel.updateVariantValuesLayoutMap(
                adapterPosition,
                VARIANT_VALUE_LEVEL_ONE_POSITION
            )
            // save selected level 1 variant unit values
            viewModel.updateSelectedVariantUnitValuesLevel1(selectedVariantUnitValuesLevel1)
            // update layout - selected unit values map
            viewModel.updateSelectedVariantUnitValuesMap(
                VARIANT_VALUE_LEVEL_ONE_POSITION,
                selectedVariantUnitValuesLevel1
            )
            // set single selection flag to true
            viewModel.isSingleVariantTypeIsSelected = true
        } else {
            viewModel.isSingleVariantTypeIsSelected = false

            val renderedAdapterPosition =
                variantTypeAdapter?.getSelectedAdapterPosition()?.firstOrNull {
                    it != adapterPosition
                }.orZero()

            if (adapterPosition < renderedAdapterPosition) {
                // get rendered variant detail
                val renderedVariantDetail = variantTypeAdapter?.getItem(renderedAdapterPosition)
                // get rendered layout position
                val layoutPosition =
                    viewModel.getVariantValuesLayoutPosition(renderedAdapterPosition)
                // get rendered selected variant unit values by layout position
                val renderedSelectedVariantUnitValues =
                    viewModel.getSelectedVariantUnitValues(layoutPosition)

                // move the rendered variant information to level two position
                renderedVariantDetail?.run {
                    setupVariantValueSection(
                        VARIANT_VALUE_LEVEL_TWO_POSITION,
                        this,
                        renderedSelectedVariantUnitValues
                    )
                }
                // update adapter - layout mapping
                viewModel.updateVariantValuesLayoutMap(
                    renderedAdapterPosition,
                    VARIANT_VALUE_LEVEL_TWO_POSITION
                )
                // update selected variant unit values level 2
                viewModel.updateSelectedVariantUnitValuesLevel2(renderedSelectedVariantUnitValues)
                // get rendered selected variant unit by layout position
                val renderedSelectedVariantUnit = viewModel.getSelectedVariantUnit(layoutPosition)
                // update layout - selected unit mapping
                viewModel.updateSelectedVariantUnitMap(
                    VARIANT_VALUE_LEVEL_TWO_POSITION,
                    renderedSelectedVariantUnit
                )
                // update layout - selected unit values mapping
                viewModel.updateSelectedVariantUnitValuesMap(
                    VARIANT_VALUE_LEVEL_TWO_POSITION,
                    renderedSelectedVariantUnitValues
                )

                // render the new value type section in variant level one section
                val selectedVariantUnitValuesLevel1 = mutableListOf<UnitValue>()
                setupVariantValueSection(
                    VARIANT_VALUE_LEVEL_ONE_POSITION,
                    variantDetail,
                    selectedVariantUnitValuesLevel1
                )
                // update adapter - layout mapping
                viewModel.updateVariantValuesLayoutMap(
                    adapterPosition,
                    VARIANT_VALUE_LEVEL_ONE_POSITION
                )
                // update selected variant unit values level 1
                viewModel.updateSelectedVariantUnitValuesLevel1(selectedVariantUnitValuesLevel1)
                // update layout - selected unit mapping
                viewModel.updateSelectedVariantUnitMap(VARIANT_VALUE_LEVEL_ONE_POSITION, Unit())
                // update layout - selected unit values mapping
                viewModel.updateSelectedVariantUnitValuesMap(
                    VARIANT_VALUE_LEVEL_ONE_POSITION,
                    selectedVariantUnitValuesLevel1
                )

            } else {
                // render the new variant type values in level two position
                // get rendered layout position
                val layoutPosition =
                    viewModel.getVariantValuesLayoutPosition(renderedAdapterPosition)
                val renderedVariantDetail = variantTypeAdapter?.getItem(renderedAdapterPosition)
                val renderedSelectedVariantUnitValues =
                    viewModel.getSelectedVariantUnitValues(layoutPosition)
                renderedVariantDetail?.run {
                    setupVariantValueSection(
                        VARIANT_VALUE_LEVEL_ONE_POSITION,
                        this,
                        renderedSelectedVariantUnitValues
                    )
                }
                viewModel.updateVariantValuesLayoutMap(
                    renderedAdapterPosition,
                    VARIANT_VALUE_LEVEL_ONE_POSITION
                )
                viewModel.updateSelectedVariantUnitValuesLevel1(renderedSelectedVariantUnitValues)
                val renderedSelectedVariantUnit = viewModel.getSelectedVariantUnit(layoutPosition)
                viewModel.updateSelectedVariantUnitMap(
                    VARIANT_VALUE_LEVEL_ONE_POSITION,
                    renderedSelectedVariantUnit
                )
                viewModel.updateSelectedVariantUnitValuesMap(
                    VARIANT_VALUE_LEVEL_ONE_POSITION,
                    renderedSelectedVariantUnitValues
                )

                // render the new value type section in variant level two section
                val selectedVariantUnitValuesLevel2 = mutableListOf<UnitValue>()
                setupVariantValueSection(
                    VARIANT_VALUE_LEVEL_TWO_POSITION,
                    variantDetail,
                    selectedVariantUnitValuesLevel2
                )
                viewModel.updateVariantValuesLayoutMap(
                    adapterPosition,
                    VARIANT_VALUE_LEVEL_TWO_POSITION
                )
                viewModel.updateSelectedVariantUnitValuesLevel2(selectedVariantUnitValuesLevel2)
                viewModel.updateSelectedVariantUnitMap(VARIANT_VALUE_LEVEL_TWO_POSITION, Unit())
                viewModel.updateSelectedVariantUnitValuesMap(
                    VARIANT_VALUE_LEVEL_TWO_POSITION,
                    selectedVariantUnitValuesLevel2
                )
            }
        }

        // update sizechart visibility based on variant selected type
        viewModel.updateSizechartFieldVisibility(variantTypeAdapter?.getSelectedItems().orEmpty())

        // update viewmodel's variant details
        viewModel.setSelectedVariantDetails(variantTypeAdapter?.getSelectedItems().orEmpty())
    }

    override fun onVariantTypeDeselected(
        adapterPosition: Int,
        variantDetail: VariantDetail
    ): Boolean {
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

    override fun onCustomVariantTypeCountChanged(count: Int) {
        buttonAddVariantType.isEnabled = count < MAX_CUSTOM_VARIANT_TYPE
        titleLayoutVariantType.isActionButtonVisible = count.isMoreThanZero()
    }

    override fun onVariantTypeDisabledSelected(adapterPosition: Int) {
        DTDialogUtil.showDTStockDialog(context ?: return, DTDialogUtil.UserAction.EDIT)
    }

    override fun onVariantTypeDisabledDeselected(adapterPosition: Int) {
        DTDialogUtil.showDTStockDialog(context ?: return, DTDialogUtil.UserAction.EDIT)
    }

    private fun deselectVariantType(
        layoutPosition: Int,
        adapterPosition: Int,
        variantDetail: VariantDetail
    ) {
        // hide section
        resetVariantValueSection(layoutPosition)
        // remove adapter - layout map entry
        viewModel.removeVariantValueLayoutMapEntry(adapterPosition)
        // update layout - selected unit map
        viewModel.updateSelectedVariantUnitMap(layoutPosition, Unit())
        // update layout - selected unit values map
        viewModel.updateSelectedVariantUnitValuesMap(layoutPosition, mutableListOf())
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

    private fun setupCancellationDialog(
        layoutPosition: Int,
        adapterPosition: Int,
        variantDetail: VariantDetail
    ) {
        val variantTypeName = variantDetail.name
        val title =
            getString(R.string.label_cancel_use) + " " + variantTypeName + getString(R.string.label_question_mark)
        val message = getString(R.string.label_variant_cancellation_message, variantTypeName)
        cancellationDialog?.setTitle(title)
        cancellationDialog?.setDescription(message)
        cancellationDialog?.setSecondaryCTAClickListener {
            deselectVariantType(layoutPosition, adapterPosition, variantDetail)
            variantTypeAdapter?.deselectItem(adapterPosition)
            cancellationDialog?.dismiss()
            // track variant type cancellation
            viewModel.isEditMode.value?.let { isEditMode ->
                if (isEditMode) ProductEditVariantTracking.confirmVariantTypeCancellation(
                    variantTypeName,
                    shopId
                )
                else ProductAddVariantTracking.confirmVariantTypeCancellation(
                    variantTypeName,
                    shopId
                )
            }
        }
    }

    private fun setupVariantValueSection(
        layoutPosition: Int,
        variantTypeDetail: VariantDetail,
        selectedVariantUnitValues: List<UnitValue>
    ) {
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

    override fun onVariantUnitValueSaveButtonClicked(
        selectedVariantUnit: Unit,
        selectedVariantUnitValues: MutableList<UnitValue>,
        layoutPosition: Int
    ) {
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
                viewModel.updateSelectedVariantUnitValuesMap(
                    layoutPosition,
                    selectedVariantUnitValues.toMutableList()
                )
                variantValueAdapterLevel1?.setData(selectedVariantUnitValues)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                viewModel.updateSelectedVariantUnitValuesLevel2(selectedVariantUnitValues)
                viewModel.updateSelectedVariantUnitMap(layoutPosition, selectedVariantUnit)
                viewModel.updateSelectedVariantUnitValuesMap(
                    layoutPosition,
                    selectedVariantUnitValues.toMutableList()
                )
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

    override fun onVariantUnitPicked(
        selectedVariantUnit: Unit,
        currentVariantUnit: Unit,
        layoutPosition: Int,
        hasSelectedValues: Boolean
    ) {
        val variantData: VariantDetail = viewModel.getVariantData(layoutPosition)
        variantUnitPicker?.dismiss()
        // show the current variant data picker when the user select the same unit
        if (selectedVariantUnit.variantUnitID == currentVariantUnit.variantUnitID) {
            // show picker dialog to retain the UI states
            variantDataValuePicker?.dialog?.show()
        } else {
            showReplaceVariantUnitDialog(
                selectedVariantUnit.unitName,
                currentVariantUnit,
                variantData,
                layoutPosition
            )
        }
    }

    override fun onVariantUnitPickerClicked(
        layoutPosition: Int,
        selectedVariantUnit: Unit,
        selectedVariantUnitValues: MutableList<UnitValue>
    ) {
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

    override fun onAddCustomButtonClicked(
        layoutPosition: Int,
        selectedVariantUnit: Unit,
        variantUnitValues: List<UnitValue>,
        selectedVariantUnitValues: MutableList<UnitValue>
    ) {
        variantDataValuePicker?.dismiss()
        val variantData = viewModel.getVariantData(layoutPosition)
        showCustomVariantInputForm(
            layoutPosition,
            variantData,
            selectedVariantUnit,
            variantUnitValues,
            selectedVariantUnitValues
        )
    }

    override fun onCustomVariantUnitValueAdded(
        layoutPosition: Int,
        currentSelectedVariantUnit: Unit,
        customVariantUnitValue: UnitValue,
        currentSelectedVariantUnitValues: MutableList<UnitValue>
    ) {
        // close the custom variant value input form
        customVariantValueInputForm?.dismiss()
        // add the added custom value to variant data and selected variant unit values
        viewModel.addCustomVariantUnitValue(
            layoutPosition,
            currentSelectedVariantUnit,
            customVariantUnitValue
        )
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
        showVariantDataValuePicker(
            variantData,
            layoutPosition,
            currentSelectedVariantUnit,
            selectedVariantUnitValues,
            customVariantUnitValue,
            unConfirmedSelection
        )
    }

    override fun onRemoveButtonClicked(
        position: Int,
        layoutPosition: Int,
        removedUnitValue: UnitValue
    ) {
        val product = viewModel.productInputModel.value
        val hasDTStock = product?.variantInputModel?.products?.find {
            it.combination.getOrNull(layoutPosition) == position && it.hasDTStock
        } != null

        if (hasDTStock) {
            DTDialogUtil.showDTVariantDialog(context ?: return)
        } else {
            val variantData = viewModel.getVariantData(layoutPosition)
            val variantId = variantData.variantID
            viewModel.removeSelectedVariantUnitValue(layoutPosition, removedUnitValue)
            viewModel.removeCombinations(position, layoutPosition)

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
            if (layoutPosition == VARIANT_VALUE_LEVEL_ONE_POSITION) {
                variantValueAdapterLevel1?.removeData(position)
            } else if (layoutPosition == VARIANT_VALUE_LEVEL_TWO_POSITION) {
                variantValueAdapterLevel2?.removeData(position)
            }

            // update sizechart visibility based on variant selected type
            viewModel.updateSizechartFieldVisibility(
                variantTypeAdapter?.getSelectedItems().orEmpty()
            )
        }

    }

    override fun onItemClicked(position: Int) {
        viewModel.clickedVariantPhotoItemPosition = position
        showImagePicker(position)
    }

    private fun showImagePicker(position: Int) {
        if (RemoteConfig.getImagePickerRemoteConfig(context)) {
            doTrackingVariant(position)
            showImagePickerForVariant(position)
        } else {
            showPhotoVariantPicker()
        }
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
                    val imageUrlOrPathList =
                        ImagePickerResultExtractor.extract(data).imageUrlOrPathList
                    imageUrlOrPathList.forEach {
                        viewModel.updateSizechart(it)
                    }
                }

                REQUEST_CODE_SIZECHART_IMAGE_MEDIA_PICKER -> {
                    val imageUrlOrPathList = MediaPicker.result(data)
                    viewModel.cleanUrlOrPathPicture(imageUrlOrPathList)?.let {
                        viewModel.updateSizechart(it)
                    }
                }

                REQUEST_CODE_EDITOR_SIZECHART_IMAGE_MEDIA_PICKER -> {
                    val imageUrlOrPathList = ImagePickerAddEditNavigation.resultExtrasEditor(data)
                    viewModel.cleanUrlOrPathPicture(imageUrlOrPathList)?.let {
                        viewModel.updateSizechart(it)
                    }
                }

                REQUEST_CODE_VARIANT_PHOTO_IMAGE -> {
                    val imageUrlOrPathList =
                        data.getStringArrayListExtra(PICKER_RESULT_PATHS).orEmpty()
                    imageUrlOrPathList.firstOrNull()?.let {
                        val position = viewModel.clickedVariantPhotoItemPosition.orZero()
                        variantPhotoAdapter?.updateImageData(it, position)
                    }
                }

                REQUEST_CODE_VARIANT_PHOTO_IMAGE_MEDIA_PICKER -> {
                    val imageResultPicker = MediaPicker.result(data)
                    viewModel.cleanUrlOrPathPicture(imageResultPicker)?.let {
                        val position = viewModel.clickedVariantPhotoItemPosition.orZero()
                        variantPhotoAdapter?.updateImageData(it, position)
                    }
                }

                REQUEST_CODE_VARIANT_DETAIL -> {
                    val cacheManagerId = data.getStringExtra(EXTRA_CACHE_MANAGER_ID)
                    val saveInstanceCacheManager =
                        SaveInstanceCacheManager(requireContext(), cacheManagerId)
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
        recyclerViewVariantType.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
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

    private fun setupButtonSave() {
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
    }

    private fun setupBaseCancelationDialog() {
        context?.run {
            cancellationDialog =
                DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            cancellationDialog?.setPrimaryCTAText(getString(R.string.action_cancel_cancellation))
            cancellationDialog?.setSecondaryCTAText(getString(R.string.action_confirm_cancellation))
            cancellationDialog?.setDefaultMaxWidth()
            cancellationDialog?.setPrimaryCTAClickListener {
                cancellationDialog?.dismiss()
            }
        }
    }

    private fun setupVariantRecyclerViews() {
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
    }

    private fun setupTitleLayoutVariantType() {
        titleLayoutVariantType.setActionButtonOnClickListener {
            val bottomSheet = CustomVariantManageBottomSheet(
                variantTypeAdapter?.getSelectedItems(),
                variantTypeAdapter?.getCustomVariantTypeItems(),
                variantTypeAdapter?.getItems()
            )

            bottomSheet.setOnVariantTypeEditedListener { editedIndex, variantDetail ->
                editVariantType(editedIndex, variantDetail)
            }

            bottomSheet.setHasDTStock(viewModel.hasDTStock.value.orFalse())
            bottomSheet.setOnHasDTStockListener {
                DTDialogUtil.showDTStockDialog(
                    context ?: return@setOnHasDTStockListener,
                    DTDialogUtil.UserAction.REMOVE
                )
            }

            bottomSheet.setOnVariantTypeDeletedListener { deletedIndex, variantDetail ->
                deleteVariantType(deletedIndex, variantDetail)
                showToaster(getString(R.string.label_cvt_message_variant_deleted))
            }

            bottomSheet.show(childFragmentManager)
            CustomVariantTypeTracking.clickEditExistingVariant(shopId)
        }
    }

    private fun setupButtonAddVariantType() {
        buttonAddVariantType.setOnClickListener {
            val bottomSheet = CustomVariantInputBottomSheet(
                selectedVariantDetails = variantTypeAdapter?.getSelectedItems().orEmpty(),
                customVariantDetails = variantTypeAdapter?.getCustomVariantTypeItems().orEmpty(),
                variantDetails = variantTypeAdapter?.getItems().orEmpty()
            )

            bottomSheet.setOnCustomVariantTypeSubmitted {
                addCustomVariantType(it)
                showToaster(getString(R.string.label_cvt_message_variant_added))
            }

            bottomSheet.setOnPredefinedVariantTypeSubmitted { newVariantDetail ->
                addPredefinedVariantType(newVariantDetail)
                showToaster(getString(R.string.label_cvt_message_variant_added))
            }

            bottomSheet.show(childFragmentManager)
            CustomVariantTypeTracking.clickCreateCustomVariant(shopId)
        }
    }

    private fun setupAddEditVariantPage(
        variantDataList: List<VariantDetail>,
        selectedVariantDetails: List<VariantDetail>
    ) {
        // setup variant type section view
        variantTypeAdapter?.setData(variantDataList)
        variantTypeAdapter?.setMaxSelectedItems(MAX_SELECTED_VARIANT_TYPE)

        // set selected variant types
        variantTypeAdapter?.setSelectedItems(selectedVariantDetails)

        val isCustomVariantType = variantTypeAdapter?.getCustomVariantTypeItems()?.isNotEmpty()
        val displayedVariantDetail = if (isCustomVariantType == true) {
            // if there is unordered/ unsorted selected variant details,
            // then you should reverse the adapter
            processCustomVariantTypeOrder(selectedVariantDetails)
            selectedVariantDetails
        } else {
            // if editing old variant data (given data is reversed, usually created 2019 or older),
            // then you should reverse selectedVariantDetails data first
            processOldVariantType(selectedVariantDetails)
        }

        // update variant selection state
        if (selectedVariantDetails.size == VARIANT_VALUE_LEVEL_ONE_COUNT)
            viewModel.isSingleVariantTypeIsSelected = true

        // set selected variant unit and values
        displayedVariantDetail.forEachIndexed { index, variantDetail ->
            val selectedVariantUnit = variantDetail.units.firstOrNull() ?: Unit()
            val selectedVariantUnitValues = variantDetail.units.firstOrNull()?.unitValues
                ?: mutableListOf()
            val selectedVariantData = variantTypeAdapter?.getSelectedItems()?.getOrNull(index)
                ?: return@forEachIndexed // break loop if invalid index inputted
            val adapterPosition = variantTypeAdapter?.getSelectedAdapterPosition()?.getOrNull(index)
                .orZero()
            val isPredefinedVariant =
                variantDataList.any { it.variantID == variantDetail.variantID }

            // add custom unit values to variant data
            if (isPredefinedVariant) {
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
            }

            when (index) {
                VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                    setupVariantValueSection(
                        VARIANT_VALUE_LEVEL_ONE_POSITION,
                        selectedVariantData,
                        selectedVariantUnitValues
                    )
                    // update adapter - layout position map
                    viewModel.updateVariantValuesLayoutMap(
                        adapterPosition,
                        VARIANT_VALUE_LEVEL_ONE_POSITION
                    )
                    // update view model selected variant unit level1
                    viewModel.updateSelectedVariantUnitMap(
                        VARIANT_VALUE_LEVEL_ONE_POSITION,
                        selectedVariantUnit
                    )
                    // update view model selected variant unit values level 1
                    viewModel.updateSelectedVariantUnitValuesLevel1(selectedVariantUnitValues)
                    // update layout position - selected variant unit values
                    viewModel.updateSelectedVariantUnitValuesMap(
                        VARIANT_VALUE_LEVEL_ONE_POSITION,
                        selectedVariantUnitValues.toMutableList()
                    )
                }
                VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                    setupVariantValueSection(
                        VARIANT_VALUE_LEVEL_TWO_POSITION,
                        selectedVariantData,
                        selectedVariantUnitValues
                    )
                    // update adapter - layout position map
                    viewModel.updateVariantValuesLayoutMap(
                        adapterPosition,
                        VARIANT_VALUE_LEVEL_TWO_POSITION
                    )
                    // update view model selected variant unit level2
                    viewModel.updateSelectedVariantUnitMap(
                        VARIANT_VALUE_LEVEL_TWO_POSITION,
                        selectedVariantUnit
                    )
                    // update view model selected variant unit values level 2
                    viewModel.updateSelectedVariantUnitValuesLevel2(selectedVariantUnitValues)
                    // update layout position - selected variant unit values
                    viewModel.updateSelectedVariantUnitValuesMap(
                        VARIANT_VALUE_LEVEL_TWO_POSITION,
                        selectedVariantUnitValues.toMutableList()
                    )
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

    private fun observeGetVariantCategoryCombinationResult() {
        // start network PLT monitoring
        startNetworkRequestPerformanceMonitoring()
        viewModel.getVariantCategoryCombinationResult.observe(viewLifecycleOwner) { result ->
            // highlight variant type feature
            showCoachmarkCustomVariantType()
            // clear adapter before rendering
            variantTypeAdapter?.setData(emptyList())
            when (result) {
                is Success -> {
                    // master data from back end
                    val variantDataList =
                        result.data.getVariantCategoryCombination.data.variantDetails
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
                            ErrorHandler.getErrorMessage(it, result.throwable)
                        )
                        viewModel.isEditMode.value?.let { isEditMode ->
                            trackOopsConnectionPageScreen(isEditMode, result.throwable, it)
                        }
                    }
                }
            }
            observeHasDTStock()
        }
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(viewLifecycleOwner) { productInputModel ->
            // extract selected variant details
            val selectedVariantDetails = viewModel.extractSelectedVariantDetails(productInputModel)
            // set selected variant details
            viewModel.setSelectedVariantDetails(selectedVariantDetails)
            // get all variant details
            val categoryId = productInputModel.detailInputModel.categoryId.toIntOrNull()
            val selections = productInputModel.variantInputModel.selections.filter {
                it.variantId != Int.ZERO.toString()
            }
            categoryId?.run { viewModel.getVariantCategoryCombination(this, selections) }
        }
    }

    private fun observeSizechartUrl() {
        viewModel.variantSizechart.observe(viewLifecycleOwner) {
            if (it.urlOriginal.isEmpty()) {
                ivSizechartAddSign.visible()
                ivSizechartEditSign.gone()
                ivSizechart.gone()
                typographySizechartDescription.text =
                    getString(R.string.label_variant_sizechart_description)
            } else {
                ivSizechartAddSign.gone()
                ivSizechartEditSign.visible()
                ivSizechart.visible()
                typographySizechartDescription.text =
                    getString(R.string.label_variant_sizechart_edit_description)
            }

            // display sizechart image (use server image if exist)
            ivSizechart.setImage(it.urlOriginal, 0F)
        }
    }

    private fun observeInputStatus() {
        viewModel.isInputValid.observe(viewLifecycleOwner) {
            tvDeleteAll?.isEnabled = it
            if (viewModel.isRemovingVariant.value == true) {
                buttonSave.isEnabled =
                    true // always enable save button if removing variant activated
            } else {
                buttonSave.isEnabled = it
            }
        }
    }

    private fun observeIsEditMode() {
        viewModel.isEditMode.observe(viewLifecycleOwner) { isEditMode ->
            // track the screen
            if (isEditMode) ProductEditVariantTracking.trackScreen(isLoggedin, userId)
            else ProductAddVariantTracking.trackScreen(isLoggedin, userId)
        }
    }

    private fun observeVariantPhotosVisibility() {
        viewModel.isVariantPhotosVisible.observe(viewLifecycleOwner) { isVisible ->
            if (isVisible) variantPhotoLayout.show()
            else variantPhotoLayout.hide()
        }
    }

    private fun observeSizechartVisibility() {
        viewModel.isVariantSizechartVisible.observe(viewLifecycleOwner) {
            layoutSizechart.root.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun observeIsRemovingVariant() {
        viewModel.isRemovingVariant.observe(viewLifecycleOwner) {
            buttonSave.text = if (it) {
                getString(com.tokopedia.product.addedit.R.string.action_variant_save)
            } else {
                getString(com.tokopedia.product.addedit.R.string.action_variant_next)
            }
        }
    }

    private fun observeHasDTStock() {
        viewModel.hasDTStock.observe(viewLifecycleOwner) {
            variantTypeAdapter?.setEnabledSelection(!it)
            titleLayoutVariantType.titleLayoutViewActionText?.setColorToDisabled(it)
            buttonAddVariantType.isEnabled = !it
            if (it) {
                buttonAddVariantType.setOnDisabledClickListener {
                    DTDialogUtil.showDTStockDialog(
                        context ?: return@setOnDisabledClickListener,
                        DTDialogUtil.UserAction.ADD_VARIANT_TYPE
                    )
                }
                titleLayoutVariantType.setActionButtonOnClickListener { view ->
                    DTDialogUtil.showDTStockDialog(view.context,
                        DTDialogUtil.UserAction.EDIT_VARIANT_TYPE
                    )                
                }
            } else {
                buttonAddVariantType.setOnDisabledClickListener {
                    showToaster(getString(R.string.label_cvt_message_variant_cannot_add))
                }
            }
        }
    }

    private fun showToaster(message: String) {
        Toaster.build(
            requireView(), message, Toaster.LENGTH_LONG,
            actionText = getString(R.string.action_oke)
        )
            .setAnchorView(R.id.cardViewSave)
            .show()
    }

    private fun showVariantDataValuePicker(
        variantData: VariantDetail,
        layoutPosition: Int,
        selectedVariantUnit: Unit,
        selectedVariantUnitValues: MutableList<UnitValue>,
        addedCustomVariantUnitValue: UnitValue = UnitValue(),
        unConfirmedSelection: List<UnitValue> = listOf()
    ) {
        variantDataValuePicker = BottomSheetUnify()
        variantDataValuePicker?.setTitle(getString(R.string.label_variant_choose) + " " + variantData.name)
        variantDataValuePicker?.overlayClickDismiss = false
        variantDataValuePicker?.showCloseIcon = true
        variantDataValuePicker?.clearContentPadding = true
        variantDataValuePicker?.setShowListener {
            // set the bottom sheet to full screen
            variantDataValuePicker?.bottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        }
        val variantDataValuePickerLayout = VariantDataValuePicker(
            requireContext(),
            layoutPosition,
            variantData,
            this,
            this,
            this,
            this
        )
        variantDataValuePickerLayout.setupVariantDataValuePicker(
            selectedVariantUnit,
            selectedVariantUnitValues,
            addedCustomVariantUnitValue,
            unConfirmedSelection
        )
        variantDataValuePicker?.setChild(variantDataValuePickerLayout)
        variantDataValuePicker?.show(
            this@AddEditProductVariantFragment.childFragmentManager,
            TAG_VARIANT_UNIT_VALUE_PICKER
        )
    }

    private fun showVariantUnitPicker(
        variantData: VariantDetail,
        layoutPosition: Int,
        selectedVariantUnit: Unit,
        hasSelectedValues: Boolean = false
    ) {
        variantUnitPicker = BottomSheetUnify()
        variantUnitPicker?.setTitle(getString(R.string.label_variant_choose) + " " + variantData.name)
        variantUnitPicker?.overlayClickDismiss = false
        variantUnitPicker?.showCloseIcon = true
        variantUnitPicker?.clearContentPadding = true
        val variantUnitPickerLayout = VariantUnitPicker(context)
        variantUnitPickerLayout.setLayoutPosition(layoutPosition)
        variantUnitPickerLayout.setSelectedVariantUnit(selectedVariantUnit)
        variantUnitPickerLayout.setOnVariantUnitPickListener(this)
        variantUnitPickerLayout.setupVariantUnitPicker(
            variantData.units,
            viewModel.hasDTStock.getValueOrDefault(false)
        )
        variantUnitPickerLayout.setHasSelectedValues(hasSelectedValues)
        variantUnitPicker?.setCloseClickListener {
            variantUnitPicker?.dismiss()
            variantDataValuePicker?.dialog?.show()
        }
        variantUnitPicker?.setChild(variantUnitPickerLayout)
        variantUnitPicker?.show(
            this@AddEditProductVariantFragment.childFragmentManager,
            TAG_VARIANT_UNIT_PICKER
        )
    }

    private fun showCustomVariantInputForm(
        layoutPosition: Int,
        variantData: VariantDetail,
        selectedVariantUnit: Unit,
        variantUnitValues: List<UnitValue>,
        selectedVariantUnitValues: MutableList<UnitValue>
    ) {
        customVariantValueInputForm = BottomSheetUnify()
        customVariantValueInputForm?.setTitle(getString(R.string.action_variant_add) + " " + variantData.name)
        customVariantValueInputForm?.overlayClickDismiss = false
        customVariantValueInputForm?.isKeyboardOverlap = false
        val customVariantValueInputLayout =
            CustomVariantUnitValueForm(requireContext(), layoutPosition, variantUnitValues, this)
        customVariantValueInputLayout.setupVariantCustomInputLayout(
            selectedVariantUnit,
            selectedVariantUnitValues,
            variantData.name
        )
        customVariantValueInputForm?.setChild(customVariantValueInputLayout)
        customVariantValueInputForm?.show(
            this@AddEditProductVariantFragment.childFragmentManager,
            TAG_CUSTOM_VARIANT_UNIT_VALUE_INPUT_FORM
        )
    }

    private fun showPhotoVariantPicker() {
        val ctx = context ?: return
        val isEditMode = viewModel.isEditMode.value ?: false
        val builder = ImagePickerBuilder.getSquareImageBuilder(ctx)
            .withSimpleEditor()
        ImagePickerGlobalSettings.onImageEditorContinue = ImagePickerCallback(ctx) { it, _ ->
            val shopId = UserSession(it).shopId ?: ""
            if (isEditMode) ProductEditVariantTracking.pickProductVariantPhotos(shopId)
        }
        val intent = RouteManager.getIntent(ctx, ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(builder)
        intent.putParamPageSource(ImagePickerPageSource.ADD_EDIT_PRODUCT_PAGE)
        startActivityForResult(intent, REQUEST_CODE_VARIANT_PHOTO_IMAGE)
    }

    private fun showImagePickerForVariant(position: Int) {
        val ctx = context ?: return
        val pageSourceState = variantPhotoAdapter?.getAddOrEditPickerState(position) ?: return
        val intent = ImagePickerAddEditNavigation.getIntentSinglePicker(
            ctx,
            pageSourceState
        )
        startActivityForResult(intent, REQUEST_CODE_VARIANT_PHOTO_IMAGE_MEDIA_PICKER)
    }

    private fun showExitConfirmationDialog() {
        val dialog =
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(getString(R.string.label_variant_exit_dialog_title))
            setDefaultMaxWidth()
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
        val dialog =
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(getString(R.string.label_variant_delete_all_title))
            setDefaultMaxWidth()
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

    private fun showReplaceVariantUnitDialog(
        oldUnitName: String,
        newUnit: Unit,
        variantData: VariantDetail,
        layoutPosition: Int
    ) {
        val dialogTitle = getString(R.string.label_variant_replace) +
            " " + oldUnitName + " " + getString(R.string.label_variant_to) +
            " " + newUnit.unitName + getString(R.string.label_question_mark)
        val dialogDesc = getString(R.string.label_variant_data) +
            " " + oldUnitName +
            " " + getString(R.string.label_variant_replacement_notice) +
            " " + newUnit.unitName
        val dialog =
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.apply {
            setTitle(dialogTitle)
            setDefaultMaxWidth()
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

    private fun showEditorSizechartPicker() {
        val ctx = context ?: return
        val urlOrPath = viewModel.variantSizechart.value?.urlOriginal ?: ""

        val isEditMode = viewModel.isEditMode.value ?: false
        val builder = ImageEditorBuilder(
            imageUrls = arrayListOf(urlOrPath),
            defaultRatio = ImageRatioType.RATIO_1_1
        )
        val intent = RouteManager.getIntent(ctx, ApplinkConstInternalGlobal.IMAGE_EDITOR)
        intent.putImageEditorBuilder(builder)
        intent.putParamPageSource(ImagePickerPageSource.ADD_EDIT_PRODUCT_PAGE)
        ImagePickerGlobalSettings.onImageEditorContinue = onImagePickerEditContinue(ctx, isEditMode)
        startActivityForResult(intent, REQUEST_CODE_SIZECHART_IMAGE)
    }

    private fun showEditorImagePickerSizeGuide() {
        val ctx = context ?: return
        val urlOrPath = viewModel.variantSizechart.value?.urlOriginal ?: ""
        val intent = ImagePickerAddEditNavigation.getIntentMediaEditor(
            ctx,
            arrayListOf(urlOrPath)
        )
        startActivityForResult(intent, REQUEST_CODE_EDITOR_SIZECHART_IMAGE_MEDIA_PICKER)
    }

    private fun showGetVariantCategoryCombinationErrorToast(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage,
                duration = Snackbar.LENGTH_INDEFINITE,
                type = Toaster.TYPE_ERROR,
                actionText = getString(com.tokopedia.abstraction.R.string.title_try_again),
                clickListener = View.OnClickListener {
                    val categoryId = viewModel.productInputModel.value?.detailInputModel?.categoryId
                    val selections =
                        viewModel.productInputModel.value?.variantInputModel?.selections
                            ?: listOf()
                    categoryId?.let { id ->
                        val paramId = id.toIntOrNull()
                        paramId?.run { viewModel.getVariantCategoryCombination(this, selections) }
                    }
                }).show()
        }
    }

    private fun showCoachmarkCustomVariantType() {
        val showed = SharedPreferencesUtil.getFirstTimeCustomVariantType(requireActivity())
        if (!showed) {
            val items = listOf(
                CoachMark2Item(
                    buttonAddVariantType,
                    getString(R.string.label_cvt_tips_title),
                    getString(R.string.label_cvt_tips),
                    CoachMarkContentPosition.BOTTOM.position
                )
            )
            val coachMark = CoachMark2(requireContext())
            coachMark.showCoachMark(ArrayList(items), scrollViewContent)
            hideCoachmarkWhenTouchOutside(coachMark)
            SharedPreferencesUtil.setFirstTimeCustomVariantType(requireActivity(), true)
        }
    }

    private fun hideCoachmarkWhenTouchOutside(coachMark: CoachMark2) {
        buttonAddVariantType.requestFocus()
        buttonAddVariantType.requestFocusFromTouch()
        buttonAddVariantType.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) coachMark.dismissCoachMark()
        }
        coachMark.setOnDismissListener {
            buttonAddVariantType.clearFocus()
        }
    }

    private fun addPredefinedVariantType(newVariantDetail: VariantDetail) {
        val selectedIndex = variantTypeAdapter?.getItems().orEmpty().indexOfFirst {
            it.variantID == newVariantDetail.variantID && it.name == newVariantDetail.name
        }
        val notOnAdapter = selectedIndex.isLessThanZero()
        val selectedVariantTypeCount = variantTypeAdapter?.getSelectedCount().orZero()

        if (notOnAdapter) {
            variantTypeAdapter?.addData(
                newVariantDetail,
                isSelected = selectedVariantTypeCount < MAX_SELECTED_VARIANT_TYPE
            )
        } else if (selectedVariantTypeCount < MAX_SELECTED_VARIANT_TYPE) {
            // if variant type already inside adapter, don't add variant, just invoke selected listener
            variantTypeAdapter?.onVariantTypeSelected(selectedIndex)
        }
    }

    private fun addCustomVariantType(title: String) {
        val customVariantTypeDetail = viewModel.createCustomVariantTypeModel(title)
        val selectedVariantCount = variantTypeAdapter?.getSelectedCount().orZero()

        variantTypeAdapter?.addData(
            customVariantTypeDetail,
            isSelected = selectedVariantCount < MAX_SELECTED_VARIANT_TYPE
        )
    }

    private fun editVariantType(editedIndex: Int, variantDetail: VariantDetail) {
        val oldVariantDetail = variantTypeAdapter?.getItem(editedIndex)
        val autoSelectIndex = variantTypeAdapter?.getItems().orEmpty().indexOfFirst {
            it.variantID == variantDetail.variantID && it.name == variantDetail.name
        }
        val isSelected = variantTypeAdapter?.isItemAtPositionSelected(editedIndex).orFalse()

        variantTypeAdapter?.replaceItem(editedIndex, variantDetail)
        if (!isSelected) {
            if (!autoSelectIndex.isLessThanZero()) {
                deleteVariantType(editedIndex, variantDetail)
            }
            showToaster(getString(R.string.label_cvt_message_variant_edited))
            return
        }

        val layoutPosition = viewModel.getVariantValuesLayoutPosition(editedIndex)
        if (variantDetail.variantID == CUSTOM_VARIANT_TYPE_ID) {
            val selectedVariantDetails = viewModel.getSelectedVariantDetails().toMutableList()
            selectedVariantDetails[layoutPosition] = variantDetail
            viewModel.setSelectedVariantDetails(selectedVariantDetails)
            viewModel.updateVariantDataMap(layoutPosition, variantDetail)
            when (layoutPosition) {
                VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                    typographyVariantValueLevel1Title.text = variantDetail.name
                }
                VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                    typographyVariantValueLevel2Title.text = variantDetail.name
                }
            }
        } else {
            if (autoSelectIndex.isLessThanZero() && oldVariantDetail != null) {
                viewModel.isSingleVariantTypeIsSelected = true
                deselectVariantType(layoutPosition, editedIndex, variantDetail)
                variantTypeAdapter?.deselectItem(editedIndex)
            } else {
                deleteVariantType(editedIndex, variantDetail)
                variantTypeAdapter?.onVariantTypeSelected(autoSelectIndex)
            }
            showToaster(getString(R.string.label_cvt_message_variant_edited))
        }
    }

    private fun deleteVariantType(deletedIndex: Int, variantDetail: VariantDetail) {
        val isSelected = variantTypeAdapter?.isItemAtPositionSelected(deletedIndex).orFalse()
        if (isSelected) {
            // deselect variant
            viewModel.isSingleVariantTypeIsSelected = true
            val layoutPosition = viewModel.getVariantValuesLayoutPosition(deletedIndex)
            deselectVariantType(layoutPosition, deletedIndex, variantDetail)

            // update new adapter position to viewmodel's variantValuesLayoutMap
            val renderedAdapterPosition = viewModel.getRenderedLayoutAdapterPosition()
            if (deletedIndex < renderedAdapterPosition) {
                val renderedLayoutPosition =
                    viewModel.getVariantValuesLayoutPosition(renderedAdapterPosition)
                viewModel.removeVariantValueLayoutMapEntry(renderedAdapterPosition)
                viewModel.updateVariantValuesLayoutMap(
                    renderedAdapterPosition.dec(),
                    renderedLayoutPosition
                )
            }
        }

        // delete variant at adapter
        variantTypeAdapter?.deleteItem(deletedIndex, variantDetail)
    }

    private fun addVariantValueAtLevel(level: Int) {
        if (variantDataValuePicker?.isVisible == true) return
        val variantData: VariantDetail = viewModel.getVariantData(level)
        val selectedVariantUnitValues = mutableListOf<UnitValue>()
        selectedVariantUnitValues.addAll((viewModel.getSelectedVariantUnitValues(level)))
        showVariantDataValuePicker(
            variantData,
            level,
            viewModel.getSelectedVariantUnit(level),
            selectedVariantUnitValues
        )
        viewModel.isEditMode.value?.let { isEditMode ->
            val variantTypeName = variantData.name
            trackAddingVariantDetailValueEvent(isEditMode, variantTypeName, shopId)
        }
    }

    private fun processCustomVariantTypeOrder(selectedVariantDetails: List<VariantDetail>) {
        val adapterSelectedVariantDetails = variantTypeAdapter?.getSelectedItems()
        val isUnordered = selectedVariantDetails.firstOrNull()?.variantID !=
            adapterSelectedVariantDetails?.firstOrNull()?.variantID
        if (isUnordered) {
            variantTypeAdapter?.swapSelectedItem()
            viewModel.setSelectedVariantDetails(variantTypeAdapter?.getSelectedItems().orEmpty())
        }
    }

    private fun processOldVariantType(selectedVariantDetails: List<VariantDetail>): List<VariantDetail> {
        viewModel.updateIsOldVariantData(
            variantTypeAdapter?.getSelectedItems().orEmpty(),
            selectedVariantDetails
        )
        return if (viewModel.isOldVariantData) {
            selectedVariantDetails.reversed()
        } else {
            selectedVariantDetails
        }
    }

    private fun submitVariantInput() {
        val productInputModel = viewModel.productInputModel.value
        productInputModel?.apply {
            val cacheManagerId = arguments?.getString(EXTRA_CACHE_MANAGER_ID).orEmpty()
            SaveInstanceCacheManager(
                requireContext(),
                cacheManagerId
            ).put(EXTRA_PRODUCT_INPUT_MODEL, this)
            val intent = Intent().putExtra(EXTRA_CACHE_MANAGER_ID, cacheManagerId)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun onSizechartClicked() {
        if (viewModel.variantSizechart.value?.urlOriginal.isNullOrEmpty()) {
            showImagePickerVariantSizeGuide()
        } else {
            val fm = this@AddEditProductVariantFragment.childFragmentManager
            val dialogFragment = AddEditProductVariantSizechartDialogFragment.newInstance()
            dialogFragment.show(fm, AddEditProductVariantSizechartDialogFragment.FRAGMENT_TAG)
            dialogFragment.setOnImageEditListener(object :
                AddEditProductVariantSizechartDialogFragment.OnImageEditListener {
                override fun clickImageEditor() {
                    showImageEditorVariantSizeGuide()
                }

                override fun clickRemoveImage() {
                    removeSizechart()
                }

                override fun clickChangeImagePath() {
                    showImagePickerVariantSizeGuide()
                }
            })
        }
    }

    fun showImagePickerVariantSizeGuide() {
        if (RemoteConfig.getImagePickerRemoteConfig(context)) {
            doTrackingForVariantSize()
            showImagePickerSizeGuide()
        } else {
            showSizechartPicker()
        }
    }

    fun showImageEditorVariantSizeGuide() {
        if (RemoteConfig.getImagePickerRemoteConfig(context)) {
            doTrackingForVariantSize()
            showEditorImagePickerSizeGuide()
        } else {
            showEditorSizechartPicker()
        }
    }

    private fun doTrackingVariant(position: Int) {
        val userId = UserSession(context).userId
        val shopId = UserSession(context).shopId
        val isEdit = !variantPhotoAdapter?.isPictIdIsEmpty(position = position).orTrue()
        MediaImprovementTracker.sendVariantActionTracker(isEdit, userId, shopId)
    }

    private fun doTrackingForVariantSize() {
        val userId = UserSession(context).userId
        val shopId = UserSession(context).shopId
        val isEdit = viewModel.isEditMode.value ?: false
        MediaImprovementTracker.sendVariantActionTracker(isEdit, userId, shopId)
    }

    private fun removeVariant() {
        viewModel.removeVariant()
        variantValueAdapterLevel1?.setData(emptyList())
        variantValueAdapterLevel2?.setData(emptyList())
        variantPhotoAdapter?.setData(emptyList())
        variantValueLevel1Layout.hide()
        variantValueLevel2Layout.hide()
        removeSizechart()
        layoutSizechart.root.hide()
        buttonAddVariantType.isEnabled = true
        titleLayoutVariantType.isActionButtonVisible = false
    }

    private fun removeSizechart() {
        viewModel.updateSizechart("")
    }

    private fun showSizechartPicker() {
        val ctx = context ?: return
        val isEditMode = viewModel.isEditMode.value ?: false
        val builder = ImagePickerBuilder.getSquareImageBuilder(ctx)
            .withSimpleEditor()
        ImagePickerGlobalSettings.onImageEditorContinue = onImagePickerEditContinue(ctx, isEditMode)
        val intent = RouteManager.getIntent(ctx, ApplinkConstInternalGlobal.IMAGE_PICKER)
        intent.putImagePickerBuilder(builder)
        intent.putParamPageSource(ImagePickerPageSource.ADD_EDIT_PRODUCT_PAGE)
        startActivityForResult(intent, REQUEST_CODE_SIZECHART_IMAGE)
    }

    private fun showImagePickerSizeGuide() {
        val ctx = context ?: return
        val intent = ImagePickerAddEditNavigation.getIntentSinglePicker(
            ctx,
            PageSource.EditVariant
        )
        startActivityForResult(intent, REQUEST_CODE_SIZECHART_IMAGE_MEDIA_PICKER)
    }

    private fun onImagePickerEditContinue(
        context: Context,
        isInEditMode: Boolean
    ): ImagePickerCallback {
        return ImagePickerCallback(context) { it, _ ->
            val shopId = UserSession(it).shopId.orEmpty()
            if (isInEditMode)
                ProductEditVariantTracking.pickSizeChartImage(shopId)
            else
                ProductAddVariantTracking.pickSizeChartImage(shopId)
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
            val intent =
                AddEditProductVariantDetailActivity.createInstance(context, cacheManager.id)
            startActivityForResult(intent, REQUEST_CODE_VARIANT_DETAIL)
        }
    }

    private fun trackAddingVariantDetailValueEvent(
        isEditMode: Boolean,
        variantTypeName: String,
        shopId: String
    ) {
        if (isEditMode) ProductEditVariantTracking.addingVariantDetailValue(variantTypeName, shopId)
        else ProductAddVariantTracking.addingVariantDetailValue(variantTypeName, shopId)
    }

    private fun trackSelectingVariantUnitEvent(
        isEditMode: Boolean,
        variantTypeName: String,
        shopId: String
    ) {
        if (isEditMode) ProductEditVariantTracking.selectingVariantUnit(variantTypeName, shopId)
        else ProductAddVariantTracking.selectingVariantUnit(variantTypeName, shopId)
    }

    private fun trackSelectVariantUnitValueEvent(
        isEditMode: Boolean,
        eventLabel: String,
        shopId: String
    ) {
        if (isEditMode) ProductEditVariantTracking.selectVariantUnitValue(eventLabel, shopId)
        else ProductAddVariantTracking.selectVariantUnitValue(eventLabel, shopId)
    }

    private fun trackSaveVariantUnitValueEvent(
        isEditMode: Boolean,
        eventLabel: String,
        shopId: String
    ) {
        if (isEditMode) ProductEditVariantTracking.saveVariantUnitValues(eventLabel, shopId)
        else ProductAddVariantTracking.saveVariantUnitValues(eventLabel, shopId)
    }

    private fun trackSaveCustomVariantUnitValueEvent(
        isEditMode: Boolean,
        eventLabel: String,
        shopId: String
    ) {
        if (isEditMode) ProductEditVariantTracking.saveCustomVariantUnitValue(eventLabel, shopId)
        else ProductAddVariantTracking.saveCustomVariantUnitValue(eventLabel, shopId)
    }

    private fun trackRemoveVariantUnitValueEvent(
        isEditMode: Boolean,
        eventLabel: String,
        shopId: String
    ) {
        if (isEditMode) ProductEditVariantTracking.removeVariantUnitValue(eventLabel, shopId)
        else ProductAddVariantTracking.removeVariantUnitValue(eventLabel, shopId)
    }

    private fun trackOopsConnectionPageScreen(
        isEditMode: Boolean,
        throwable: Throwable,
        context: Context
    ) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        val errorName = AddEditProductUploadErrorHandler.getErrorName(throwable)
        if (isEditMode) {
            ProductEditStepperTracking.oopsConnectionPageScreen(
                userId,
                errorMessage,
                errorName
            )
        } else {
            ProductAddStepperTracking.oopsConnectionPageScreen(
                userId,
                errorMessage,
                errorName
            )
        }
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(R.id.toolbar_variant)?.apply {
            headerTitle = getString(R.string.title_variant_activity)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            actionTextView?.setOnClickListener {
                val product = viewModel.productInputModel.value
                if (product?.hasDTStock.orFalse()) {
                    DTDialogUtil.showDTVariantDialog(context ?: return@setOnClickListener)
                } else {
                    showRemoveVariantDialog()
                }
            }
            actionTextView?.text = getString(R.string.action_variant_delete)
            tvDeleteAll = actionTextView
            tvDeleteAll?.isEnabled = false
            tvDeleteAll?.visible()
        }
    }

    fun onBackPressed() {
        showExitConfirmationDialog()
    }
}
