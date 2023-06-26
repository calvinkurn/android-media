package com.tokopedia.product.addedit.variant.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.databinding.AddEditProductCustomVariantManageBottomSheetContentBinding
import com.tokopedia.product.addedit.tracking.CustomVariantTypeTracking
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.di.DaggerAddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeSelectedAdapter
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.CUSTOM_VARIANT_TYPE_ID
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CustomVariantManageBottomSheet(
        private val selectedVariantDetails: List<VariantDetail>? = null,
        private val customVariantDetails: List<VariantDetail>? = null,
        private val variantDetails: List<VariantDetail>? = null
) : BottomSheetUnify() {

    @Inject
    lateinit var userSession: UserSessionInterface
    private var binding by autoClearedNullable<AddEditProductCustomVariantManageBottomSheetContentBinding>()
    private var hasDTStock: Boolean = false
    private var onVariantTypeEditedListener: ((editedIndex: Int, variantDetail: VariantDetail) -> Unit)? = null
    private var onVariantTypeDeletedListener: ((deletedIndex: Int, variantDetail: VariantDetail) -> Unit)? = null
    private var onHasDTStockListener: (() -> Unit)? = null

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    private fun initInjector() {
        DaggerAddEditProductVariantComponent.builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(
                        context?.applicationContext as BaseMainApplication))
                .build()
                .inject(this)
    }

    private fun getVariantDetailPosition(variantDetail: VariantDetail): Int? {
        return variantDetails?.indexOfFirst {
            it.variantID == variantDetail.variantID && it.name == variantDetail.name
        }
    }

    private fun initChildLayout() {
        setTitle(getString(R.string.label_variant_custom_manage_bottom_sheet_title))
        binding = AddEditProductCustomVariantManageBottomSheetContentBinding
            .inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        clearContentPadding = true
    }

    private fun setupRecyclerView() {
        val adapter = VariantTypeSelectedAdapter()
        binding?.recyclerViewVariantSelected?.adapter = adapter
        binding?.recyclerViewVariantSelected?.layoutManager = LinearLayoutManager(context)
        adapter.setData(customVariantDetails?.map {
            Pair(it.name, it.variantID == CUSTOM_VARIANT_TYPE_ID)
        }.orEmpty())
        adapter.setOnEditButtonClickedListener { index ->
            customVariantDetails?.getOrNull(index)?.let { variantDetail ->
                showEditVariantBottomSheet(variantDetail)

                CustomVariantTypeTracking.clickEditVariant(userSession.shopId, variantDetail.name)
            }
            dismiss()
        }
        adapter.setOnDeleteButtonClickedListener {
            customVariantDetails?.getOrNull(it)?.let { variantDetail ->
                showDeleteConfDialog(variantDetail)

                CustomVariantTypeTracking.clickDeleteVariant(userSession.shopId, variantDetail.name)
            }
            dismiss()
        }
        adapter.setOnWarningListener {
            showToaster(it)
        }
    }

    private fun showToaster(text: String) {
        val snackbarTxt: TextView? = binding?.layoutToaster?.
            findViewById(com.tokopedia.unifycomponents.R.id.snackbar_txt)
        val snackbarBtn: TextView? = binding?.layoutToaster?.
            findViewById(com.tokopedia.unifycomponents.R.id.snackbar_btn)
        val constraintLayoutToaster: ViewGroup? = binding?.layoutToaster?.
            findViewById(com.tokopedia.unifycomponents.R.id.constraintLayoutToaster)

        snackbarTxt?.text = text
        snackbarBtn?.text = getString(R.string.action_oke)
        constraintLayoutToaster?.background = MethodChecker.getDrawable(requireContext(),
            com.tokopedia.unifycomponents.R.drawable.toaster_bg_normal)

        snackbarBtn?.show()
        binding?.layoutToaster?.show()

        snackbarBtn?.setOnClickListener {
            binding?.layoutToaster?.gone()
        }
    }

    private fun showDeleteConfDialog(variantDetail: VariantDetail) {
        if (hasDTStock) {
            onHasDTStockListener?.invoke()
        } else {
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.label_cvt_delete_conf_title, variantDetail.name))
                setDescription(getString(R.string.label_cvt_delete_conf_desc))
                setPrimaryCTAText(getString(R.string.action_cancel))
                setSecondaryCTAText(getString(R.string.action_variant_delete_all_positive))
                setPrimaryCTAClickListener {
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                    val position = getVariantDetailPosition(variantDetail)
                        ?: return@setSecondaryCTAClickListener
                    onVariantTypeDeletedListener?.invoke(position, variantDetail)
                }
            }.show()
        }
    }

    private fun showEditConfDialog(
            context: Context,
            oldVariantDetail: VariantDetail,
            newVariantDetail: VariantDetail,
            onCorfirmed: () -> Unit
    ) {
        DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(context.getString(R.string.label_cvt_edit_conf_title))
            setDescription(context.getString(
                    R.string.label_cvt_edit_conf_desc,
                    oldVariantDetail.name,
                    newVariantDetail.name,
                    oldVariantDetail.name
            ))
            setPrimaryCTAText(context.getString(R.string.action_cancel))
            setSecondaryCTAText(context.getString(R.string.action_confirm_replace_variant_type))
            setPrimaryCTAClickListener {
                dismiss()
            }
            setSecondaryCTAClickListener {
                dismiss()
                onCorfirmed.invoke()
            }
        }.show()
    }

    private fun showEditVariantBottomSheet(oldVariantDetail: VariantDetail) {
        val bottomSheet = CustomVariantInputBottomSheet(
            oldVariantDetail.name,
            selectedVariantDetails.orEmpty(),
            customVariantDetails.orEmpty()
        )
        bottomSheet.setOnCustomVariantTypeSubmitted { newVariantName ->
            val position = getVariantDetailPosition(oldVariantDetail) ?: return@setOnCustomVariantTypeSubmitted
            val newVariantDetail = oldVariantDetail.copy().apply { name = newVariantName }
            onVariantTypeEditedListener?.invoke(position, newVariantDetail)
        }
        bottomSheet.setOnPredefinedVariantTypeSubmitted { newVariantDetail ->
            showEditConfDialog(bottomSheet.requireContext(), oldVariantDetail, newVariantDetail) {
                getVariantDetailPosition(oldVariantDetail)?.let { position ->
                    onVariantTypeEditedListener?.invoke(position, newVariantDetail)
                }
            }
        }
        bottomSheet.show(requireActivity().supportFragmentManager)
    }

    fun setOnVariantTypeEditedListener(
        listener: (editedIndex: Int, variantDetail: VariantDetail) -> Unit
    ) {
        onVariantTypeEditedListener = listener
    }

    fun setOnVariantTypeDeletedListener(
        listener: (deletedIndex: Int, variantDetail: VariantDetail) -> Unit
    ) {
        onVariantTypeDeletedListener = listener
    }

    fun setOnHasDTStockListener(listener: () -> Unit) {
        onHasDTStockListener = listener
    }

    fun setHasDTStock(hasDTStock: Boolean) {
        this.hasDTStock = hasDTStock
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, this@CustomVariantManageBottomSheet.javaClass.simpleName)
        }
    }
}
