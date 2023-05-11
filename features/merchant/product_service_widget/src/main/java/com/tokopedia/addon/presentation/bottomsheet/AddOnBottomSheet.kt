package com.tokopedia.addon.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.addon.presentation.fragment.AddOnFragment
import com.tokopedia.product_service_widget.R
import com.tokopedia.product_service_widget.databinding.BottomsheetAddonBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AddOnBottomSheet(private val addOnId: String) : BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetAddonBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        overlayClickDismiss = true
        binding = BottomsheetAddonBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.gifting_title_bottomsheet))
        childFragmentManager.beginTransaction()
            .replace(R.id.parent_view,
                AddOnFragment(), "")
            .commit()
    }
}
