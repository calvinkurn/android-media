package com.tokopedia.product.addedit.variant.presentation.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.databinding.AddEditProductCustomVariantManageBottomSheetContentBinding
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeSelectedAdapter
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.CUSTOM_VARIANT_TYPE_ID
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CustomVariantManageBottomSheet(
    private val selectedVariantDetails: List<VariantDetail>? = null,
    private val variantDetails: List<VariantDetail>? = null
) : BottomSheetUnify() {

    private var binding by autoClearedNullable<AddEditProductCustomVariantManageBottomSheetContentBinding>()
    private var onVariantTypeEditedListener: ((variantDetail: VariantDetail) -> Unit)? = null
    private var onVariantTypeDeletedListener: ((variantDetail: VariantDetail) -> Unit)? = null

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        adapter.setData(selectedVariantDetails?.map {
            Pair(it.name, it.variantID == CUSTOM_VARIANT_TYPE_ID)
        }.orEmpty())
        adapter.setOnEditButtonClickedListener {
            selectedVariantDetails?.getOrNull(it)?.let { variantDetail ->
                showEditConfDialog(variantDetail)
            }
            dismiss()
        }
        adapter.setOnDeleteButtonClickedListener {
            selectedVariantDetails?.getOrNull(it)?.let { variantDetail ->
                showDeleteConfDialog(variantDetail)
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
        DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.label_cvt_edit_conf_title, variantDetail.name))
            setDescription(getString(R.string.label_cvt_edit_conf_desc))
            setPrimaryCTAText(getString(R.string.action_variant_delete_all_negative))
            setSecondaryCTAText(getString(R.string.action_variant_delete_all_positive))
            setPrimaryCTAClickListener {
                dismiss()
            }
            setSecondaryCTAClickListener {
                dismiss()
                onVariantTypeDeletedListener?.invoke(variantDetail)
            }
        }.show()
    }

    private fun showEditConfDialog(variantDetail: VariantDetail) {
        val bottomSheet = CustomVariantInputBottomSheet(variantDetail.name, variantDetails.orEmpty())
        bottomSheet.setOnCustomVariantTypeSubmitted { newVariantName ->
            onVariantTypeEditedListener?.invoke(
                variantDetail.apply { name = newVariantName }
            )
        }
        bottomSheet.setOnPredefinedVariantTypeSubmitted {
            Log.e("okhttp", it.toString())
        }
        bottomSheet.show(requireActivity().supportFragmentManager)
    }

    fun setOnVariantTypeEditedListener(listener: (variantDetail: VariantDetail) -> Unit) {
        onVariantTypeEditedListener = listener
    }

    fun setOnVariantTypeDeletedListener(listener: (variantDetail: VariantDetail) -> Unit) {
        onVariantTypeDeletedListener = listener
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this, this@CustomVariantManageBottomSheet.javaClass.simpleName)
        }
    }
}