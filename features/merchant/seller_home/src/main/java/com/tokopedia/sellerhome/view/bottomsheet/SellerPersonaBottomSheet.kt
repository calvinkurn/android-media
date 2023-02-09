package com.tokopedia.sellerhome.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.BottomSheetSellerPersonaBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Created by @ilhamsuaib on 10/02/23.
 */

class SellerPersonaBottomSheet : BottomSheetUnify() {

    companion object {
        fun newInstance(): SellerPersonaBottomSheet {
            return SellerPersonaBottomSheet()
        }

        private const val IMG_SELLER_PERSONA_ENTRY_POINT =
            "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_entry_point-min.png"
    }

    private var binding by autoClearedNullable<BottomSheetSellerPersonaBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSellerPersonaBinding.inflate(inflater, container, false).apply {
            setChild(root)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    fun show(fm: FragmentManager) {
        if (fm.isStateSaved || isAdded) return
        show(fm, this::class.java.canonicalName)
    }

    private fun setupView() {
        context?.let {
            setTitle(it.getString(R.string.sah_persona_bottom_sheet_title))
        }

        binding?.run {
            imgSahPersonaEntryPoint.loadImage(IMG_SELLER_PERSONA_ENTRY_POINT)
            btnSahPersonaBottomSheet.setOnClickListener {
                dismiss()
            }
        }
    }
}