package com.tokopedia.common.topupbills.view.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberMenuListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_seamless_favorite_number_menu.*
import kotlinx.android.synthetic.main.bottom_sheet_seamless_favorite_number_menu.view.*

class FavoriteNumberMenuBottomSheet(
        private val favNumberItem: TopupBillsSeamlessFavNumberItem,
        private val listener: FavoriteNumberMenuListener,
        private val isShowDelete: Boolean
): BottomSheetUnify() {

    private lateinit var childView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
        initView()
        initListener()
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        setCloseClickListener { dismiss() }

        childView = View.inflate(context, R.layout.bottom_sheet_seamless_favorite_number_menu, null)
        setChild(childView)
    }

    private fun initView() {
        with(childView) {
            if (isShowDelete) {
                common_topup_bills_favorite_number_delete.show()
            } else {
                common_topup_bills_favorite_number_delete.hide()
            }
        }
    }

    private fun initListener() {
        with(childView) {
            common_topupbills_favorite_number_change_name.setOnClickListener {
                listener.onChangeNameMenuClicked(favNumberItem)
                dismiss()
            }

            common_topup_bills_favorite_number_delete.setOnClickListener {
                listener.onDeleteContactClicked(favNumberItem)
                dismiss()
            }
        }
    }

    companion object {

        fun newInstance(
            favNumberItem: TopupBillsSeamlessFavNumberItem,
            listener: FavoriteNumberMenuListener,
            isShowDelete: Boolean
        ): FavoriteNumberMenuBottomSheet {
            return FavoriteNumberMenuBottomSheet(favNumberItem, listener, isShowDelete)
        }
    }
}