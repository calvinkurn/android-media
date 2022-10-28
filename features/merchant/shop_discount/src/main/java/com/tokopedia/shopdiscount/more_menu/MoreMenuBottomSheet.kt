package com.tokopedia.shopdiscount.more_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.BottomsheetMoreMenuBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable


class MoreMenuBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun newInstance(): MoreMenuBottomSheet {
            return MoreMenuBottomSheet()
        }
    }

    private var binding by autoClearedNullable<BottomsheetMoreMenuBinding>()

    private var onDeleteMenuClicked: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BottomsheetMoreMenuBinding.inflate(inflater, container, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
        setTitle(getString(R.string.sd_manage))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    fun setOnDeleteMenuClicked(onDeleteMenuClicked: () -> Unit = {}) {
        this.onDeleteMenuClicked = onDeleteMenuClicked
    }

    private fun setupView() {
        binding?.run {
            tpgDelete.setOnClickListener {
                onDeleteMenuClicked()
                dismiss()
            }
        }
    }

}
