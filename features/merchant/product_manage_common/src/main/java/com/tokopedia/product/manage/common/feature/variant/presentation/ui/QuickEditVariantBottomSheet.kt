package com.tokopedia.product.manage.common.feature.variant.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.common.ProductManageCommonInstance
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.databinding.BottomSheetProductManageQuickEditVariantBinding
import com.tokopedia.product.manage.common.feature.variant.di.DaggerQuickEditVariantComponent
import com.tokopedia.product.manage.common.feature.variant.di.QuickEditVariantComponent
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.viewmodel.QuickEditVariantViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

abstract class QuickEditVariantBottomSheet: BottomSheetUnify(), HasComponent<QuickEditVariantComponent> {

    protected companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
        const val EXTRA_IS_BUNDLING = "extra_is_bundling"
        const val EXTRA_IS_MULTILOCATION = "extra_is_multilocation"
    }

    @Inject
    lateinit var viewModel: QuickEditVariantViewModel

    private var adapter: BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>? = null

    private var binding by autoClearedNullable<BottomSheetProductManageQuickEditVariantBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getTitle())
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageQuickEditVariantBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = arguments?.getString(EXTRA_PRODUCT_ID).orEmpty()
        val isBundling = arguments?.getBoolean(EXTRA_IS_BUNDLING).orFalse()
        val isMultiLocation = arguments?.getBoolean(EXTRA_IS_MULTILOCATION).orFalse()

        setupTicker(isMultiLocation)
        setupSaveBtn()
        setupVariantList()
        setupBottomSheet()
        setupErrorView(productId, isBundling)

        observeGetVariant()
        observeClickSaveBtn()
        observeViewState()

        getData(productId, isBundling)
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onDestroy() {
        removeObservers(viewModel.getProductVariantsResult)
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun getComponent(): QuickEditVariantComponent? {
        return activity?.run {
            DaggerQuickEditVariantComponent.builder()
                .productManageCommonComponent(ProductManageCommonInstance.getComponent(application))
                .build()
        }
    }

    abstract fun getTitle(): String

    abstract fun createAdapter(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>

    abstract fun onSaveButtonClicked(result: EditVariantResult)

    private fun initInjector() {
        component?.inject(this)
    }

    private fun setupVariantList() {
        adapter = createAdapter()
        binding?.variantList?.run {
            adapter = this@QuickEditVariantBottomSheet.adapter
            layoutManager = LinearLayoutManager(this@QuickEditVariantBottomSheet.context)
            itemAnimator = null
        }
    }

    private fun setupBottomSheet() {
        val horizontalSpacing = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()
        val topSpacing = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()
        val bottomSpacing = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).orZero()
        bottomSheetHeader.setMargin(horizontalSpacing, topSpacing, horizontalSpacing, bottomSpacing)
        bottomSheetWrapper.setPadding(0, 0, 0, 0)
    }

    private fun setupErrorView(productId: String, isBundle: Boolean) {
        binding?.errorView?.run {
            setType(GlobalError.NO_CONNECTION)
            setActionClickListener { getData(productId, isBundle) }
            errorDescription.text = getString(R.string.product_manage_error_description)
            errorTitle.hide()
        }
    }

    private fun setupSaveBtn() {
        binding?.btnSave?.setOnClickListener {
            viewModel.saveVariants()
        }
    }

    private fun setupTicker(isMultiLocation: Boolean) {
        binding?.tickerProductManageEditPriceVariantMultiloc?.showWithCondition(isMultiLocation)
    }

    private fun getData(productId: String, isBundle: Boolean) {
        viewModel.getData(productId, isBundle)
    }

    private fun observeGetVariant() {
        observe(viewModel.getProductVariantsResult) {
            val variants = it.variants
            adapter?.addElement(variants)
            collapseBottomSheet()
        }
    }

    private fun observeClickSaveBtn() {
        observe(viewModel.onClickSaveButton) {
            onSaveButtonClicked(it)
        }
    }

    private fun observeViewState() {
        observe(viewModel.showProgressBar) { showProgressBar ->
            binding?.progressBar?.showWithCondition(showProgressBar)
        }

        observe(viewModel.showErrorView) { showErrorView ->
            if(showErrorView) {
                expandBottomSheet()
                binding?.errorViewContainer?.show()
            } else {
                binding?.errorViewContainer?.hide()
            }
        }

        observe(viewModel.showSaveBtn) {
            binding?.btnSave?.showWithCondition(it)
        }
    }

    private fun expandBottomSheet() {
        (view?.parent as? FrameLayout)?.apply {
            val params = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            binding?.container?.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            layoutParams = params
        }
    }

    private fun collapseBottomSheet() {
        (view?.parent as? FrameLayout)?.apply {
            val params = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                gravity = Gravity.BOTTOM
            }
            layoutParams = params
        }
    }
}