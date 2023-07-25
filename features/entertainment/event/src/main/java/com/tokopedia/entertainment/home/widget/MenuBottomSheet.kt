package com.tokopedia.entertainment.home.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.entertainment.databinding.EntLayoutBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MenuBottomSheet: BottomSheetUnify() {

    init {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
        clearContentPadding = true
    }

    private var itemClickListener: ItemClickListener? = null
    private var binding by autoClearedNullable<EntLayoutBottomSheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EntLayoutBottomSheetBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.txtPromo?.setOnClickListener {
            itemClickListener?.onMenuPromoClick()
        }

        binding?.txtHelp?.setOnClickListener {
            itemClickListener?.onMenuHelpClick()
        }

        binding?.txtTransaction?.setOnClickListener {
            itemClickListener?.onMenuTransactionListClick()
        }
    }

    fun setListener(listener: ItemClickListener) {
        this.itemClickListener = listener
    }

    companion object {
        fun newInstance(): MenuBottomSheet {
            return MenuBottomSheet()
        }
    }

    interface ItemClickListener {
        fun onMenuPromoClick()
        fun onMenuHelpClick()
        fun onMenuTransactionListClick()
    }
}
