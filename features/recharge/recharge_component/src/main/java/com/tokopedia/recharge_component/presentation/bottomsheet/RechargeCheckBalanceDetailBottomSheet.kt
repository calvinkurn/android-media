package com.tokopedia.recharge_component.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.recharge_component.databinding.BottomsheetRechargeCheckBalanceDetailBinding
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceDetailModel
import com.tokopedia.recharge_component.presentation.adapter.RechargeCheckBalanceDetailAdapter
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeCheckBalanceDetailViewHolder
import com.tokopedia.recharge_component.presentation.util.CustomDividerItemDecorator
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class RechargeCheckBalanceDetailBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<BottomsheetRechargeCheckBalanceDetailBinding>()
    private var checkBalanceDetailAdapter: RechargeCheckBalanceDetailAdapter? = null
    private lateinit var checkBalanceDetailModels: List<RechargeCheckBalanceDetailModel>
    private var checkBalanceDetailBottomSheetListener: RechargeCheckBalanceDetailBottomSheetListener? = null
    private var checkBalanceDetailViewHolderListener: RechargeCheckBalanceDetailViewHolder.RechargeCheckBalanceDetailViewHolderListener? = null
    private var productListName: String = ""

    fun setCheckBalanceDetailBottomSheetListener(listener: RechargeCheckBalanceDetailBottomSheetListener) {
        checkBalanceDetailBottomSheetListener = listener
    }

    fun setCheckBalanceDetailViewHolderListener(listener: RechargeCheckBalanceDetailViewHolder.RechargeCheckBalanceDetailViewHolderListener) {
        checkBalanceDetailViewHolderListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG_CHECK_BALANCE_DETAIL_BOTTOM_SHEET)
    }

    fun setBottomSheetTitle(title: String) {
        productListName = title
        setTitle(title)
    }

    fun setCheckBalanceDetailModels(models: List<RechargeCheckBalanceDetailModel>) {
        checkBalanceDetailModels = models
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet() {
        isFullpage = false
        isDragable = false
        showCloseIcon = true
        clearContentPadding = true

        binding = BottomsheetRechargeCheckBalanceDetailBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setCloseClickListener {
            checkBalanceDetailBottomSheetListener?.onCloseCheckBalanceDetailBottomSheet()
            dismiss()
        }

        renderBottomSheet()
    }

    private fun renderBottomSheet() {
        checkBalanceDetailAdapter = RechargeCheckBalanceDetailAdapter().apply {
            setCheckBalanceDetails(checkBalanceDetailModels)
            setCheckBalanceDetailViewHolderListener(object : RechargeCheckBalanceDetailViewHolder.RechargeCheckBalanceDetailViewHolderListener {
                override fun onRenderCheckBalanceDetailBuyButton(
                    model: RechargeCheckBalanceDetailModel,
                    position: Int,
                    bottomSheetTitle: String
                ) {
                    checkBalanceDetailViewHolderListener?.onRenderCheckBalanceDetailBuyButton(
                        model,
                        position,
                        productListName
                    )
                }

                override fun onClickCheckBalanceDetailBuyButton(
                    model: RechargeCheckBalanceDetailModel,
                    position: Int,
                    bottomSheetTitle: String
                ) {
                    checkBalanceDetailViewHolderListener?.onClickCheckBalanceDetailBuyButton(
                        model,
                        position,
                        productListName
                    )
                }
            })
        }
        binding?.rechargeCheckBalanceDetailRv?.run {
            adapter = checkBalanceDetailAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(CustomDividerItemDecorator(context, DividerItemDecoration.VERTICAL))
        }
        checkBalanceDetailBottomSheetListener?.onRenderCheckBalanceDetailBottomSheet()
    }

    interface RechargeCheckBalanceDetailBottomSheetListener {
        fun onRenderCheckBalanceDetailBottomSheet()
        fun onCloseCheckBalanceDetailBottomSheet()
    }

    companion object {
        private const val TAG_CHECK_BALANCE_DETAIL_BOTTOM_SHEET = "check_balance_detail_bottom_sheet"
    }
}
