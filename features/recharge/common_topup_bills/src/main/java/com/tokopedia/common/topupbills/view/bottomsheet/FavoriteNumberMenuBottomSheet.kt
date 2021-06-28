package com.tokopedia.common.topupbills.view.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberMenuListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_seamless_favorite_number_menu.*

class FavoriteNumberMenuBottomSheet(
        private val listener: FavoriteNumberMenuListener
): BottomSheetUnify() {

    private lateinit var childView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
        initListener()
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        setCloseClickListener { dismiss() }

        childView = View.inflate(context, R.layout.bottom_sheet_seamless_favorite_number_menu, null)
        setChild(childView)
    }

    private fun initListener() {
        common_topupbills_favorite_number_change_name.setOnClickListener {
            listener.onChangeNameClicked()
            dismiss()
        }

        common_topup_bills_favorite_number_delete.setOnClickListener {
            listener.onDeleteContactClicked()
            dismiss()
        }
    }

    companion object {

        fun newInstance(listener: FavoriteNumberMenuListener): FavoriteNumberMenuBottomSheet {
            return FavoriteNumberMenuBottomSheet(listener)
        }
    }
}