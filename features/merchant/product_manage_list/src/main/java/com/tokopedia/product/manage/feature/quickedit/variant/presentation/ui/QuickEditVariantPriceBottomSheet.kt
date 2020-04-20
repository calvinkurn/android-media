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
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.ProductVariantAdapter
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.factory.ProductVariantAdapterFactoryImpl
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.feature.quickedit.variant.adapter.viewholder.ProductVariantViewHolder
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.ViewState.*
import com.tokopedia.product.manage.feature.quickedit.variant.data.model.result.EditVariantResult
import com.tokopedia.product.manage.feature.quickedit.variant.di.DaggerQuickEditVariantComponent
import com.tokopedia.product.manage.feature.quickedit.variant.di.QuickEditVariantComponent
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.viewmodel.QuickEditVariantViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_product_manage_quick_edit_variant.*
import javax.inject.Inject

class QuickEditVariantPriceBottomSheet(
    private val onSaveVariantsPrice: (EditVariantResult) -> Unit
): BottomSheetUnify(), HasComponent<QuickEditVariantComponent>, ProductVariantViewHolder.ProductVariantListener {

    companion object {
        private const val EXTRA_PRODUCT_ID = "extra_product_id"
        val TAG: String = QuickEditVariantPriceBottomSheet::class.java.simpleName

        fun createInstance(
            context: Context,
            productId: String,
            onSaveVariantsPrice: (EditVariantResult) -> Unit
        ): QuickEditVariantPriceBottomSheet {
            return QuickEditVariantPriceBottomSheet(onSaveVariantsPrice).apply {
                val bundle = Bundle()
                bundle.putString(EXTRA_PRODUCT_ID, productId)
                arguments = bundle
                initView(context)
            }
        }

        private fun QuickEditVariantPriceBottomSheet.initView(context: Context) {
            val itemView = View.inflate(context,
                R.layout.bottom_sheet_product_manage_quick_edit_variant, null)
            val title = itemView.context.getString(R.string.product_manage_menu_set_price)

            setTitle(title)
            setChild(itemView)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }
    }

    @Inject
    lateinit var viewModel: QuickEditVariantViewModel

    private var adapter: ProductVariantAdapter? = null

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

    override fun onPriceChanged(variantId: String, price: Int) {
        viewModel.updateVariantPrice(variantId, price)
    }

    private fun setupVariantList() {
        adapter = ProductVariantAdapter(ProductVariantAdapterFactoryImpl(this))
        variantList.apply {
            adapter = this@QuickEditVariantPriceBottomSheet.adapter
            layoutManager = LinearLayoutManager(this@QuickEditVariantPriceBottomSheet.context)
        }
    }

    private fun setupBottomSheet() {
        val padding = context?.resources?.getDimensionPixelSize(R.dimen.spacing_lvl4).orZero()
        bottomSheetHeader.setPadding(padding, padding, padding, padding)
        bottomSheetWrapper.setPadding(0, 0, 0, 0)
    }

    private fun setupSaveBtn(result: EditVariantResult) {
        btnSave.setOnClickListener {
            onSaveVariantsPrice(result)
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