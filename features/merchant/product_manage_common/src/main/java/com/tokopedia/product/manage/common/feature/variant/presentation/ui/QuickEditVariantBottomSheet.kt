package com.tokopedia.product.manage.common.feature.variant.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.manage.common.ProductManageCommonInstance
import com.tokopedia.product.manage.common.R
import com.tokopedia.product.manage.common.feature.variant.di.DaggerQuickEditVariantComponent
import com.tokopedia.product.manage.common.feature.variant.di.QuickEditVariantComponent
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.viewmodel.QuickEditVariantViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage_quick_edit_variant.*
import javax.inject.Inject

abstract class QuickEditVariantBottomSheet: BottomSheetUnify(), HasComponent<QuickEditVariantComponent> {

    protected companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }

    @Inject
    lateinit var viewModel: QuickEditVariantViewModel

    private var adapter: BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemView = View.inflate(context,
            R.layout.bottom_sheet_product_manage_quick_edit_variant, null)

        setChild(itemView)
        setTitle(getTitle())
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = arguments?.getString(EXTRA_PRODUCT_ID).orEmpty()

        setupSaveBtn()
        setupVariantList()
        setupBottomSheet()
        setupErrorView(productId)

        observeGetVariant()
        observeClickSaveBtn()
        observeViewState()

        getData(productId)
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
        variantList.apply {
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

    private fun setupErrorView(productId: String) {
        errorView.setType(GlobalError.NO_CONNECTION)
        errorView.setActionClickListener { getData(productId) }
        errorView.errorDescription.text = getString(R.string.product_manage_error_description)
        errorView.errorTitle.hide()
    }

    private fun setupSaveBtn() {
        btnSave.setOnClickListener {
            viewModel.saveVariants()
        }
    }

    private fun getData(productId: String) {
        viewModel.getData(productId)
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
            progressBar.showWithCondition(showProgressBar)
        }

        observe(viewModel.showErrorView) { showErrorView ->
            if(showErrorView) {
                expandBottomSheet()
                errorViewContainer.show()
            } else {
                errorViewContainer.hide()
            }
        }

        observe(viewModel.showSaveBtn) {
            btnSave.showWithCondition(it)
        }
    }

    private fun expandBottomSheet() {
        (view?.parent as? FrameLayout)?.apply {
            val params = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            container.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
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