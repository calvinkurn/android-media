package com.tokopedia.product.addedit.variant.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
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
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantViewModel
import com.tokopedia.product.addedit.variant.presentation.widget.CustomVariantUnitValueForm
import com.tokopedia.product.addedit.variant.presentation.widget.VariantDetailValuesPicker
import com.tokopedia.product.addedit.variant.presentation.widget.VariantUnitPicker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
        VariantDetailValuesPicker.OnButtonSaveClickListener,
        VariantValueAdapter.OnRemoveButtonClickListener, CustomVariantUnitValueForm.OnCustomVariantUnitAddListener,
        VariantDetailValuesPicker.OnAddCustomVariantUnitValueListener,
        VariantUnitPicker.OnVariantUnitPickListener, VariantDetailValuesPicker.OnVariantUnitPickerClickListener, VariantPhotoAdapter.OnItemClickListener {

    companion object {
        private const val TAG_VARIANT_UNIT_PICKER = "VARIANT_UNIT_PICKER"
        private const val TAG_VARIANT_UNIT_VALUE_PICKER = "VARIANT_UNIT_VALUE_PICKER"
        private const val TAG_VARIANT_UNIT_CUSTOM_VALUE_INPUT_FORM = "VARIANT_UNIT_CUSTOM_VALUE_INPUT_FORM"

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
    private var variantPhotoAdapterAdapter: VariantPhotoAdapter? = null

    private var variantValuePicker: BottomSheetUnify? = null
    private var variantUnitPicker: BottomSheetUnify? = null
    private var variantCustomValueInputForm: BottomSheetUnify? = null

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
        variantPhotoAdapterAdapter = VariantPhotoAdapter(this)

        recyclerViewVariantType.adapter = variantTypeAdapter
        recyclerViewVariantValueLevel1.adapter = variantValueAdapterLevel1
        recyclerViewVariantValueLevel2.adapter = variantValueAdapterLevel2
        recyclerViewVariantPhoto.adapter = variantPhotoAdapterAdapter
        setRecyclerViewToFlex(recyclerViewVariantType)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel1)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel2)
        setRecyclerViewToHorizontal(recyclerViewVariantPhoto)

        observeSizechartUrl()
        observeGetCategoryVariantCombinationResult()
        observeProductInputModel()

        cardSizechart.setOnClickListener {
            onSizechartClicked()
        }

        linkAddVariantValueLevel1.setOnClickListener {
            val variantDetail: VariantDetail = it.getTag(R.id.variant_detail) as VariantDetail
            if (variantDetail.units.isEmpty()) showVariantCustomInputForm(VARIANT_VALUE_LEVEL_ONE_POSITION, variantDetail.variantID, variantDetail.name)
            else showVariantValuePicker(variantDetail, VARIANT_VALUE_LEVEL_ONE_POSITION, null)
        }

        linkAddVariantValueLevel2.setOnClickListener {
            val variantDetail: VariantDetail = it.getTag(R.id.variant_detail) as VariantDetail
            if (variantDetail.units.isEmpty()) showVariantCustomInputForm(VARIANT_VALUE_LEVEL_ONE_POSITION, variantDetail.variantID, variantDetail.name)
            showVariantValuePicker(variantDetail, VARIANT_VALUE_LEVEL_TWO_POSITION, null)
        }
      
        buttonSave.setOnClickListener {
            val variantDetails = variantTypeAdapter?.getItems().orEmpty()
            viewModel.updateVariantInputModel(variantDetails)
            startAddEditProductVariantDetailActivity()
        }
    }

    override fun onVariantTypeSelected(adapterPosition: Int, variantDetail: VariantDetail) {


        if (viewModel.isVariantUnitValuesLayoutEmpty()) {

            val selectedVariantUnitValuesLevel1 = viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_ONE_POSITION)
            setupVariantValueSection(VARIANT_VALUE_LEVEL_ONE_POSITION, variantDetail, selectedVariantUnitValuesLevel1)
            viewModel.updateVariantValuesLayoutMap(adapterPosition, VARIANT_VALUE_LEVEL_ONE_POSITION)
            viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, selectedVariantUnitValuesLevel1)


        } else {

            val renderedAdapterPosition = viewModel.getRenderedLayoutAdapterPosition()

            if (adapterPosition < renderedAdapterPosition) {

                val renderedVariantDetail = variantTypeAdapter?.getItem(renderedAdapterPosition)
                val layoutPosition = viewModel.getVariantValuesLayoutPosition(renderedAdapterPosition)
                val renderedSelectedVariantUnitValues = viewModel.getSelectedVariantUnitValues(layoutPosition)
                renderedVariantDetail?.run { setupVariantValueSection(VARIANT_VALUE_LEVEL_TWO_POSITION, this, renderedSelectedVariantUnitValues) }
                viewModel.updateVariantValuesLayoutMap(renderedAdapterPosition, VARIANT_VALUE_LEVEL_TWO_POSITION)
                viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_TWO_POSITION, renderedSelectedVariantUnitValues)


                val selectedVariantUnitValuesLevel1 = mutableListOf<UnitValue>()
                setupVariantValueSection(VARIANT_VALUE_LEVEL_ONE_POSITION, variantDetail, selectedVariantUnitValuesLevel1)
                viewModel.updateVariantValuesLayoutMap(adapterPosition, VARIANT_VALUE_LEVEL_ONE_POSITION)
                viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, selectedVariantUnitValuesLevel1)


            } else {

                val renderedVariantDetail = variantTypeAdapter?.getItem(renderedAdapterPosition)
                val renderedSelectedVariantUnitValues = viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_ONE_POSITION)
                renderedVariantDetail?.run { setupVariantValueSection(VARIANT_VALUE_LEVEL_ONE_POSITION, this, renderedSelectedVariantUnitValues) }
                viewModel.updateVariantValuesLayoutMap(renderedAdapterPosition, VARIANT_VALUE_LEVEL_ONE_POSITION)
                viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_ONE_POSITION, renderedSelectedVariantUnitValues)

                val selectedVariantUnitValues = viewModel.getSelectedVariantUnitValues(VARIANT_VALUE_LEVEL_TWO_POSITION)
                setupVariantValueSection(VARIANT_VALUE_LEVEL_TWO_POSITION, variantDetail, selectedVariantUnitValues)
                viewModel.updateVariantValuesLayoutMap(adapterPosition, VARIANT_VALUE_LEVEL_TWO_POSITION)
                viewModel.updateSelectedVariantUnitValuesMap(VARIANT_VALUE_LEVEL_TWO_POSITION, selectedVariantUnitValues)
            }
        }
    }

    override fun onVariantTypeDeselected(adapterPosition: Int) {
        val layoutPosition = viewModel.getVariantValuesLayoutPosition(adapterPosition)
        resetVariantValueSection(layoutPosition)
        viewModel.removeVariantValueLayoutMapEntry(adapterPosition)
        viewModel.updateSelectedVariantUnitValuesMap(layoutPosition, mutableListOf())
    }

    private fun setupVariantValueSection(layoutPosition: Int, variantTypeDetail: VariantDetail, selectedVariantUnitValues: List<UnitValue>) {
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                variantValueLevel1Layout.show()
                variantValueAdapterLevel1?.setData(selectedVariantUnitValues)
                typographyVariantValueLevel1Title.text = variantTypeDetail.name
                linkAddVariantValueLevel1.setTag(R.id.variant_detail, variantTypeDetail)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                variantValueLevel2Layout.show()
                variantValueAdapterLevel2?.setData(selectedVariantUnitValues)
                typographyVariantValueLevel2Title.text = variantTypeDetail.name
                linkAddVariantValueLevel2.setTag(R.id.variant_detail, variantTypeDetail)
            }
        }
    }

    override fun onVariantUnitValueSaveButtonClicked(selectedVariantUnitValues: List<UnitValue>, layoutPosition: Int, variantId: Int) {

        variantValuePicker?.dismiss()

        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                viewModel.updateSelectedVariantUnitValuesMap(layoutPosition, selectedVariantUnitValues.toMutableList())
                variantValueAdapterLevel1?.setData(selectedVariantUnitValues)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                viewModel.updateSelectedVariantUnitValuesMap(layoutPosition, selectedVariantUnitValues.toMutableList())
                variantValueAdapterLevel2?.setData(selectedVariantUnitValues)
            }
        }

        if (variantId == COLOUR_VARIANT_TYPE_ID) {
            variantPhotoLayout.show()
            variantPhotoAdapterAdapter?.setData(selectedVariantUnitValues)
        }
    }

    override fun onVariantUnitPicked(selectedVariantUnit: Unit, layoutPosition: Int) {
        variantUnitPicker?.dismiss()
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel1.getTag(R.id.variant_detail) as VariantDetail
                showVariantValuePicker(variantDetail, layoutPosition, selectedVariantUnit)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel2.getTag(R.id.variant_detail) as VariantDetail
                showVariantValuePicker(variantDetail, layoutPosition, selectedVariantUnit)
            }
        }
    }

    override fun onVariantUnitPickerClicked(selectedVariantUnit: Unit, layoutPosition: Int) {
        variantValuePicker?.dismiss()
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel1.getTag(R.id.variant_detail) as VariantDetail
                showVariantUnitPicker(selectedVariantUnit, variantDetail, layoutPosition)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                val variantDetail: VariantDetail = linkAddVariantValueLevel2.getTag(R.id.variant_detail) as VariantDetail
                showVariantUnitPicker(selectedVariantUnit, variantDetail, layoutPosition)
            }
        }
    }

    //
    override fun onAddButtonClicked(layoutPosition: Int, variantId: Int, unitName: String) {
        variantValuePicker?.dismiss()
        showVariantCustomInputForm(layoutPosition, variantId, unitName)
    }

    // custom value save
    override fun onCustomVariantUnitAdded(customVariantUnitValue: UnitValue, layoutPosition: Int, variantId: Int) {
        variantCustomValueInputForm?.dismiss()
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                viewModel.addCustomVariantUnitValue(layoutPosition, customVariantUnitValue)
                variantValueAdapterLevel1?.addData(customVariantUnitValue)
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                viewModel.addCustomVariantUnitValue(layoutPosition, customVariantUnitValue)
                variantValueAdapterLevel2?.addData(customVariantUnitValue)
            }
        }

        if (variantId == COLOUR_VARIANT_TYPE_ID) {
            variantPhotoLayout.show()
            variantPhotoAdapterAdapter?.addData(customVariantUnitValue)
        }
    }

    override fun onRemoveButtonClicked(position: Int, layoutPosition: Int) {
        viewModel.removeSelectedVariantUnitValue(layoutPosition, position)
    }

    override fun onItemClicked(position: Int) {
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
                        viewModel.variantSizechartUrl.value = it
                    }
                }
            }
        }
    }

    private fun showVariantValuePicker(variantDetail: VariantDetail, layoutPosition: Int, selectedVariantUnit: Unit?) {

        val selectedVariantUnitValues = viewModel.getSelectedVariantUnitValues(layoutPosition)

        variantDetail.let {
            variantValuePicker = BottomSheetUnify()
            variantValuePicker?.setTitle("Pilih " + variantDetail.name)
            variantValuePicker?.showCloseIcon = true
            val variantValuePickerLayout = VariantDetailValuesPicker(context)
            variantValuePickerLayout.setLayoutPosition(layoutPosition)

            selectedVariantUnit?.run { variantValuePickerLayout.setSelectedVariantUnit(this) }

            variantValuePickerLayout.setOnButtonSaveClickListener(this)
            variantValuePickerLayout.setOnVariantUnitPickerClickListener(this)
            variantValuePickerLayout.setOnAddCustomVariantUnitValueListener(this)
            variantValuePickerLayout.setSelectedVariantUnitValues(selectedVariantUnitValues)
            variantValuePickerLayout.setupVariantDetailValuesPicker(variantDetail)
            variantValuePicker?.setChild(variantValuePickerLayout)
            variantValuePicker?.show(this@AddEditProductVariantFragment.childFragmentManager, TAG_VARIANT_UNIT_VALUE_PICKER)
        }
    }

    private fun showVariantUnitPicker(selectedVariantUnit: Unit, variantDetail: VariantDetail, layoutPosition: Int) {
        variantUnitPicker = BottomSheetUnify()
        variantUnitPicker?.setTitle("Pilih " + variantDetail.name)
        variantUnitPicker?.showCloseIcon = true
        val variantUnitPickerLayout = VariantUnitPicker(context)
        variantUnitPickerLayout.setLayoutPosition(layoutPosition)
        variantUnitPickerLayout.setSelectedVariantUnit(selectedVariantUnit)
        variantUnitPickerLayout.setOnVariantUnitPickListener(this)
        variantUnitPickerLayout.setupVariantUnitPicker(variantDetail.units)
        variantUnitPicker?.setChild(variantUnitPickerLayout)
        variantUnitPicker?.show(this@AddEditProductVariantFragment.childFragmentManager, TAG_VARIANT_UNIT_PICKER)
    }

    private fun showVariantCustomInputForm(layoutPosition: Int, variantId: Int, unitName: String) {
        variantCustomValueInputForm = BottomSheetUnify()
        variantCustomValueInputForm?.setTitle("Pilih $unitName")
        val variantCustomValueInputLayout = CustomVariantUnitValueForm(context)
        variantCustomValueInputLayout.setLayoutPosition(layoutPosition)
        variantCustomValueInputLayout.setOnButtonSaveClickListener(this)
        variantCustomValueInputLayout.setupVariantCustomInputLayout(layoutPosition, variantId)
        variantCustomValueInputForm?.setChild(variantCustomValueInputLayout)
        variantCustomValueInputForm?.show(this@AddEditProductVariantFragment.childFragmentManager, TAG_VARIANT_UNIT_CUSTOM_VALUE_INPUT_FORM)
    }

    private fun showPhotoVariantPicker() {
        context?.apply {
            val intent = VariantPhotoPickerActivity.getIntent(this)
            startActivityForResult(intent, REQUEST_CODE_VARIANT_PHOTO_IMAGE)
        }
    }

    private fun onSizechartClicked() {
        if (viewModel.variantSizechartUrl.value.isNullOrEmpty()) {
            showSizechartPicker()
        } else {
            val fm = activity!!.supportFragmentManager
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
            when (result) {
                is Success -> {
                    val variantDetails =
                            result.data.getCategoryVariantCombination.data.variantDetails
                    variantTypeAdapter?.setData(variantDetails)
                    variantTypeAdapter?.setMaxSelectedItems(MAX_SELECTED_VARIANT_TYPE)
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
            val categoryId = productInputModel.detailInputModel.categoryId
            viewModel.getCategoryVariantCombination(categoryId)
        })
    }

    private fun observeSizechartUrl() {
        viewModel.variantSizechartUrl.observe(this, Observer {
            if (it.isEmpty()) {
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
            ivSizechart.setImage(it, 0F)
        })
    }

    private fun removeSizechart() {
        val url = viewModel.variantSizechartUrl.value.orEmpty()
        viewModel.variantSizechartUrl.value = ""
        FileUtils.deleteFileInTokopediaFolder(url)
    }

    private fun showSizechartPicker() {
        context?.apply {
            val intent = SizechartPickerAddProductActivity.getIntent(this)
            startActivityForResult(intent, REQUEST_CODE_SIZECHART_IMAGE)
        }
    }

    private fun showEditorSizechartPicker() {
        val url = viewModel.variantSizechartUrl.value.orEmpty()
        context?.apply {
            val editorIntent = SizechartPickerEditPhotoActivity.createIntent(this, url)
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

    fun onBackPressed() {
        activity?.finish()
    }
}
