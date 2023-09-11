package com.tokopedia.epharmacy.ui.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.ui.fragment.EPharmacyQuantityChangeFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class EPharmacyComponentBottomSheet : BottomSheetUnify() {

    init {
        isFullpage = true
        isDragable = false
        isHideable = true
        clearContentPadding = true
        isSkipCollapseState = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChild(getContentView())
    }

    private fun getContentView(): View {
        return View.inflate(requireContext(), R.layout.epharmacy_component_bottomsheet, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTitle()
        if(savedInstanceState == null) {
            childFragmentManager.beginTransaction().replace(R.id.ep_frame_content,
                EPharmacyQuantityChangeFragment.newInstance()).commit()
        }
    }

    private fun setUpTitle() {
        setTitle("Perubahan jumlah pesanan")
        setCloseClickListener {
            activity?.finish()
        }
    }

    companion object {

        private const val COMPONENT_NAME = "ComponentName"

        fun newInstance(componentName: String): EPharmacyComponentBottomSheet {
            return EPharmacyComponentBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(COMPONENT_NAME, componentName)
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }
}
