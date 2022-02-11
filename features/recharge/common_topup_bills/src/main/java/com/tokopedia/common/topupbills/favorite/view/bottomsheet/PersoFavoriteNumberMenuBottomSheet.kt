package com.tokopedia.common.topupbills.favorite.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.common.topupbills.databinding.BottomSheetSeamlessFavoriteNumberMenuBinding
import com.tokopedia.common.topupbills.favorite.view.model.TopupBillsPersoFavNumberDataView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify

class PersoFavoriteNumberMenuBottomSheet(
    private val favNumberItem: TopupBillsPersoFavNumberDataView,
    private val listener: PersoFavoriteNumberMenuListener,
    private val isShowDelete: Boolean
): BottomSheetUnify() {

    // reuse seamless layout
    private lateinit var binding: BottomSheetSeamlessFavoriteNumberMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
        initView()
        initListener()
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        setCloseClickListener { dismiss() }

        binding = BottomSheetSeamlessFavoriteNumberMenuBinding.inflate(LayoutInflater.from(context))
        setChild(binding.root)
    }

    private fun initView() {
        with(binding) {
            if (isShowDelete) {
                commonTopupBillsFavoriteNumberDelete.show()
            } else {
                commonTopupBillsFavoriteNumberDelete.hide()
            }
        }
    }

    private fun initListener() {
        with(binding) {
            commonTopupbillsFavoriteNumberChangeName.setOnClickListener {
                listener.onChangeNameMenuClicked(favNumberItem)
                dismiss()
            }

            commonTopupBillsFavoriteNumberDelete.setOnClickListener {
                listener.onDeleteContactClicked(favNumberItem)
                dismiss()
            }
        }
    }

    interface PersoFavoriteNumberMenuListener {
        fun onChangeNameMenuClicked(favNumberItem: TopupBillsPersoFavNumberDataView)
        fun onDeleteContactClicked(favNumberItem: TopupBillsPersoFavNumberDataView)
    }

    companion object {

        fun newInstance(
            favNumberItem: TopupBillsPersoFavNumberDataView,
            listener: PersoFavoriteNumberMenuListener,
            isShowDelete: Boolean
        ): PersoFavoriteNumberMenuBottomSheet {
            return PersoFavoriteNumberMenuBottomSheet(favNumberItem, listener, isShowDelete)
        }
    }
}