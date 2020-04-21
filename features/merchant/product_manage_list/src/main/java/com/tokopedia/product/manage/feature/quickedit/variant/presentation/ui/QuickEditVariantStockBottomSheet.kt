package com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.ProductVariantStockAdapter
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantStockAdapterFactoryImpl
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantStockViewHolder.*
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState.HideProgressBar
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState.ShowProgressBar
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.result.EditVariantResult
import com.tokopedia.product.manage.feature.quickedit.variant.di.DaggerQuickEditVariantComponent
import com.tokopedia.product.manage.feature.quickedit.variant.di.QuickEditVariantComponent
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel.QuickEditVariantViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_product_manage_quick_edit_variant.*
import javax.inject.Inject

class QuickEditVariantStockBottomSheet(
    private val onSaveVariantsStock: (EditVariantResult) -> Unit
): BottomSheetUnify(), HasComponent<QuickEditVariantComponent>, ProductVariantStockListener {

    companion object {
        private const val EXTRA_PRODUCT_ID = "extra_product_id"
        val TAG: String = QuickEditVariantStockBottomSheet::class.java.simpleName

        fun createInstance(
            context: Context,
            productId: String,
            onSaveVariantsStock: (EditVariantResult) -> Unit
        ): QuickEditVariantStockBottomSheet {
            return QuickEditVariantStockBottomSheet(onSaveVariantsStock).apply {
                val bundle = Bundle()
                bundle.putString(EXTRA_PRODUCT_ID, productId)
                arguments = bundle
                initView(context)
            }
        }

        private fun QuickEditVariantStockBottomSheet.initView(context: Context) {
            val itemView = View.inflate(context,
                R.layout.bottom_sheet_product_manage_quick_edit_variant, null)
            val title = itemView.context.getString(R.string.product_manage_variant)

            isFullpage = false
            setTitle(title)
            setChild(itemView)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }
    }

    @Inject
    lateinit var viewModel: QuickEditVariantViewModel

    private var adapter: ProductVariantStockAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = arguments?.getString(EXTRA_PRODUCT_ID).orEmpty()

        setupVariantList()
        setupBottomSheet()

        observeGetVariant()
        observeEditVariant()
        observeViewState()

        getProductVariants(productId)
    }

    override fun onStockChanged(variantId: String, stock: Int) {
        viewModel.updateVariantStock(variantId, stock)
    }

    override fun onStatusChanged(variantId: String, status: ProductStatus) {
        viewModel.updateVariantStatus(variantId, status)
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    override fun onDestroy() {
        removeObservers(viewModel.getProductVariantsResult)
        super.onDestroy()
    }

    override fun getComponent(): QuickEditVariantComponent? {
        return activity?.run {
            DaggerQuickEditVariantComponent.builder()
                .productManageComponent(ProductManageInstance.getComponent(application))
                .build()
        }
    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun setupVariantList() {
        adapter = ProductVariantStockAdapter(ProductVariantStockAdapterFactoryImpl(this))
        variantList.apply {
            adapter = this@QuickEditVariantStockBottomSheet.adapter
            layoutManager = LinearLayoutManager(this@QuickEditVariantStockBottomSheet.context)
        }
    }

    private fun setupBottomSheet() {
        val padding = context?.resources?.getDimensionPixelSize(R.dimen.spacing_lvl4).orZero()
        bottomSheetHeader.setPadding(padding, padding, padding, padding)
        bottomSheetWrapper.setPadding(0, 0, 0, 0)
    }

    private fun setupSaveBtn(result: EditVariantResult) {
        btnSave.setOnClickListener {
            onSaveVariantsStock(result)
            dismiss()
        }
    }

    private fun getProductVariants(productId: String) {
        viewModel.getProductVariants(productId)
    }

    private fun observeGetVariant() {
        observe(viewModel.getProductVariantsResult) {
            when(it) {
                is Success -> {
                    val variants = it.data.variants
                    adapter?.addElement(variants)
                    showHideSaveBtn(variants)
                }
                is Fail -> {
                    showErrorToast()
                    dismiss()
                }
            }
        }
    }

    private fun observeEditVariant() {
        observe(viewModel.editVariantResult) {
            setupSaveBtn(it)
        }
    }

    private fun observeViewState() {
        observe(viewModel.viewState) {
            when(it) {
                is ShowProgressBar -> progressBar.show()
                is HideProgressBar -> progressBar.hide()
            }
        }
    }

    private fun showHideSaveBtn(variants: List<ProductVariant>) {
        btnSave.showWithCondition(variants.isNotEmpty())
    }

    private fun showErrorToast(
        message: String = getString(R.string.product_manage_snack_bar_fail),
        actionLabel: String = getString(R.string.product_manage_close),
        onClickActionLabel: () -> Unit = {}
    ) {
        Toaster.make(container, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, actionLabel,
            View.OnClickListener { onClickActionLabel.invoke() })
    }
}