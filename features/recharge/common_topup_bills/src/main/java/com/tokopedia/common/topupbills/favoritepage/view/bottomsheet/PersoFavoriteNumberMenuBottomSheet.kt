package com.tokopedia.common.topupbills.favoritepage.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import com.tokopedia.common.topupbills.databinding.BottomSheetSeamlessFavoriteNumberMenuBinding
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsPersoFavNumberDataView
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify

class PersoFavoriteNumberMenuBottomSheet: BottomSheetUnify() {

    // reuse seamless layout
    private lateinit var binding: BottomSheetSeamlessFavoriteNumberMenuBinding

    private var isShowDelete: Boolean? = null
    private var favNumberItem: TopupBillsPersoFavNumberDataView? = null
    private var listener: PersoFavoriteNumberMenuListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { arguments ->
            isShowDelete = arguments.getBoolean(IS_SHOW_DELETE_EXTRA, false)
            favNumberItem = arguments.getParcelable(FAV_NUMBER_ITEM_EXTRA)
        }
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
            isShowDelete?.let { isShowDelete ->
                commonTopupBillsFavoriteNumberDelete.showWithCondition(isShowDelete)
            }
        }
    }

    private fun initListener() {
        with(binding) {
            favNumberItem?.let { favNumberItem ->
                commonTopupbillsFavoriteNumberChangeName.setOnClickListener {
                    listener?.onChangeNameMenuClicked(favNumberItem)
                    dismiss()
                }

                commonTopupBillsFavoriteNumberDelete.setOnClickListener {
                    listener?.onDeleteContactClicked(favNumberItem)
                    dismiss()
                }
            }
        }
    }

    fun setListener(listener: PersoFavoriteNumberMenuListener) {
        this.listener = listener
    }

    interface PersoFavoriteNumberMenuListener {
        fun onChangeNameMenuClicked(favNumberItem: TopupBillsPersoFavNumberDataView)
        fun onDeleteContactClicked(favNumberItem: TopupBillsPersoFavNumberDataView)
    }

    companion object {
        private const val FAV_NUMBER_ITEM_EXTRA = "FAV_NUMBER_ITEM_EXTRA"
        private const val IS_SHOW_DELETE_EXTRA = "IS_SHOW_DELETE_EXTRA"
        fun newInstance(
            favNumberItem: TopupBillsPersoFavNumberDataView,
            isShowDelete: Boolean
        ): PersoFavoriteNumberMenuBottomSheet {
            val bottomSheet = PersoFavoriteNumberMenuBottomSheet()
            val bundle = Bundle()
            bundle.putParcelable(FAV_NUMBER_ITEM_EXTRA, favNumberItem)
            bundle.putBoolean(IS_SHOW_DELETE_EXTRA, isShowDelete)
            bottomSheet.arguments = bundle
            return bottomSheet
        }
    }
}
