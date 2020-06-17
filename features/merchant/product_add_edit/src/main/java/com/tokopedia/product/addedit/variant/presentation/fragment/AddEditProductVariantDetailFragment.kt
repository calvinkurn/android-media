package com.tokopedia.product.addedit.variant.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailFieldsAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantDetailInputTypeFactoryImpl
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantDetailHeaderViewHolder
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.dialog.MultipleVariantEditSelectBottomSheet
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantDetailViewModel
import kotlinx.android.synthetic.main.fragment_add_edit_product_variant_detail.*
import javax.inject.Inject


class AddEditProductVariantDetailFragment : BaseDaggerFragment(),
        VariantDetailHeaderViewHolder.OnCollapsibleHeaderClickListener, MultipleVariantEditSelectBottomSheet.MultipleVariantEditListener {

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_variant_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSave.isEnabled = false

        variantDetailFieldsAdapter = VariantDetailFieldsAdapter(VariantDetailInputTypeFactoryImpl(this))
        recyclerViewVariantDetailFields.adapter = variantDetailFieldsAdapter
        recyclerViewVariantDetailFields.layoutManager = LinearLayoutManager(context)

        switchUnifySku.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) variantDetailFieldsAdapter?.updateAllField(viewModel.hideSkuFields())
            else variantDetailFieldsAdapter?.updateAllField(viewModel.showSkuFields())
        }

        val multipleVariantEditSelectBottomSheet = MultipleVariantEditSelectBottomSheet(this)
        val variantInputModel = viewModel.productInputModel.value?.variantInputModel
        multipleVariantEditSelectBottomSheet.setData(variantInputModel)
        multipleVariantEditSelectBottomSheet.show(fragmentManager)

        observeSelectedVariantSize()
    }

    override fun onHeaderClicked(adapterPosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        // get target fields (indexes)
        // remove indexes from visitables
    }

    private fun observeSelectedVariantSize() {
        viewModel.selectedVariantSize.observe(this, Observer { size ->
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

    private fun setupVariantDetailFields(selectedUnitValues: List<OptionInputModel>) {
        // without variant unit values combination
        selectedUnitValues.forEach { unitValue ->
            val variantDetailInputModel = VariantDetailInputLayoutModel(unitValueLabel = unitValue.value)
            val fieldAdapterPosition = variantDetailFieldsAdapter?.addVariantDetailField(variantDetailInputModel)
            fieldAdapterPosition?.let { viewModel.updateVariantDetailInputMap(fieldAdapterPosition, variantDetailInputModel) }
        }
    }

    private fun setupVariantDetailCombinationFields(selectedVariants: List<SelectionInputModel>) {
        // variant level 1 properties
        val selectedVariantLevel1 = selectedVariants[VARIANT_VALUE_LEVEL_ONE_POSITION]
        val unitValueLevel1 = selectedVariantLevel1.options
        // variant level 2 properties
        val selectedVariantLevel2 = selectedVariants[VARIANT_VALUE_LEVEL_TWO_POSITION]
        val unitValueLevel2 = selectedVariantLevel2.options
        // start rendering
        unitValueLevel1.forEach { level1Value ->
            // render collapsible header
            val headerAdapterPosition = variantDetailFieldsAdapter?.addUnitValueHeader(level1Value.value)
            // render variant unit value fields
            unitValueLevel2.forEach { level2Value ->
                val variantDetailInputModel = VariantDetailInputLayoutModel(unitValueLabel = level2Value.value)
                val fieldAdapterPosition = variantDetailFieldsAdapter?.addVariantDetailField(variantDetailInputModel)
                fieldAdapterPosition?.let { viewModel.updateVariantDetailInputMap(fieldAdapterPosition, variantDetailInputModel) }
            }
        }
    }

    override fun onMultipleEditFinished(multipleVariantEditInputModel: MultipleVariantEditInputModel) {
        println("$multipleVariantEditInputModel")
    }

    fun onBackPressed() {
        activity?.finish()
    }
}
