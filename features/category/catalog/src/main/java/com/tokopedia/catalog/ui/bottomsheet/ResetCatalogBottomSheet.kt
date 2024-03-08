package com.tokopedia.catalog.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.catalog.R
import com.tokopedia.catalog.databinding.BottomSheetResetSwitchingCatalogBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ResetCatalogBottomSheet : BottomSheetUnify() {

    private var onClickReset: (() -> Unit)? = null
    private var onClickRemoveAll: (() -> Unit)? = null

    companion object {
        private val TAG: String = ResetCatalogBottomSheet::class.java.simpleName

        fun createInstance(): ResetCatalogBottomSheet {
            return ResetCatalogBottomSheet()
        }
    }

    private var binding by autoClearedNullable<BottomSheetResetSwitchingCatalogBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetResetSwitchingCatalogBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@ResetCatalogBottomSheet)?.commit()
        }
    }

    fun show(
        fm: FragmentManager,
    ) {
        show(fm, TAG)
    }

    fun setOnClickReset(onClickReset: () -> Unit) {
        this.onClickReset = onClickReset
    }

    fun setOnClickRemove(onClickRemoveAll: () -> Unit) {
        this.onClickRemoveAll = onClickRemoveAll
    }
    private fun setupView() {
        val title = context?.getString(R.string.catalog_title_bottom_sheet_reset).orEmpty()
        setTitle(title)
        binding?.btnReset?.setOnClickListener {
            onClickReset?.invoke()
            dismiss()
        }

        binding?.btnDeleteComparison?.setOnClickListener {
            onClickRemoveAll?.invoke()
            dismiss()
        }
    }
}
