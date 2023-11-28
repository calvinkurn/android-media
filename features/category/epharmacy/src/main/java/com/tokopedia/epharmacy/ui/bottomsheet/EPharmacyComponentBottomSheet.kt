package com.tokopedia.epharmacy.ui.bottomsheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_ID
import com.tokopedia.epharmacy.utils.EPHARMACY_TOKO_CONSULTATION_IDS
import com.tokopedia.epharmacy.utils.EPharmacyNavigator
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.BottomSheetUnify

class EPharmacyComponentBottomSheet : BottomSheetUnify() {

    init {
        isFullpage = false
        isHideable = true
        clearContentPadding = true
    }

    private var tConsultationIds = arrayListOf<String>()

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
        if (savedInstanceState == null) {
            when (arguments?.getString(COMPONENT_NAME)) {
                TYPE_QUANTITY_EDITOR -> {
                    tConsultationIds = arguments?.getStringArrayList(EPHARMACY_TOKO_CONSULTATION_IDS) ?: arrayListOf()
                    EPharmacyNavigator.navigateToQuantityBottomSheet(tConsultationIds, childFragmentManager)
                }
            }
        }
    }

    private fun setUpTitle() {
        setTitle("Perubahan jumlah pesanan")
        setCloseClickListener {
            activity?.finish()
        }
    }

    companion object {

        const val COMPONENT_NAME = "ComponentName"
        private const val TYPE_QUANTITY_EDITOR = "quantity-editor"

        fun newInstance(bundle: Bundle): EPharmacyComponentBottomSheet {
            return EPharmacyComponentBottomSheet().apply {
                arguments = bundle
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
    }
}
