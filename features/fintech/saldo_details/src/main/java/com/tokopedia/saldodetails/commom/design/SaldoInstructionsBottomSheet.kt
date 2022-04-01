package com.tokopedia.saldodetails.commom.design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.saldoDetail.domain.data.SaldoInstructionItem
import com.tokopedia.saldodetails.saldoDetail.domain.data.SaldoInstructionUIModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.saldo_instructions_bottom_sheet.*

class SaldoInstructionsBottomSheet : BottomSheetUnify() {

    private val childLayoutRes = com.tokopedia.saldodetails.R.layout.saldo_instructions_bottom_sheet
    private var isSeller = false
    private var sheetTitle: CharSequence = ""

    private val refundOptions = """
        1. COD (Bayar di Tempat)
        2. Debit instan
        3. Transfer virtual account
        4. Transfer bank manual
        5. Pembayaran instan (KlikBCA, Jenius, dsb) 
        6. Tunai di gerai retail (Indomaret, Alfamart, dsb)""".trimIndent()
    private val incomeOptions = """
        1. Hasil penjualan produk (untuk Seller)
        2. Penjualan hasil investasi seperti reksa dana dan emas
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun getArgumentData() {
        arguments?.let {
            sheetTitle = it.getString(TITLE_TEXT)?.parseAsHtml() ?: ""
        isSeller = it.getBoolean(SALDO_TYPE)
        }
    }


    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uiModel = if (isSeller) populateIncomeInstructions() else populateRefundInstructions()
        uiModel?.let { instructionUiModel ->
            for (item in instructionUiModel.instructionList) {
                val layout = layoutInflater.inflate(R.layout.saldo_instruction_item_layout, null)
                layout.findViewById<IconUnify>(R.id.instructionIcon).setImage(item.iconId,
                MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900))
                layout.findViewById<Typography>(R.id.saldoInstructionText).text = item.instructionText.parseAsHtml()
                saldoInstructionLL.addView(layout)
            }

            saldoInstructionLL.addView(getHeading(uiModel.heading))
            saldoInstructionLL.addView(getBulletList(uiModel.optionText))

        } ?: kotlin.run { dismiss() }
    }

    private fun populateRefundInstructions(): SaldoInstructionUIModel? {
        context?.let {
            return SaldoInstructionUIModel(
                instructionList = arrayListOf(
                    SaldoInstructionItem(IconUnify.REFUND, it.getString(R.string.saldo_refund_info_description_1)),
                    SaldoInstructionItem(IconUnify.CART, it.getString(R.string.saldo_refund_info_description_2)),
                    SaldoInstructionItem(IconUnify.WALLET, it.getString(R.string.saldo_refund_info_description_3)),
                ),
                it.getString(R.string.saldo_refund_info_title),
                refundOptions
            )
        }
        return null
    }

    private fun populateIncomeInstructions(): SaldoInstructionUIModel? {
        context?.let {
            return SaldoInstructionUIModel(
                instructionList = arrayListOf(
                    SaldoInstructionItem(IconUnify.SALDO, it.getString(R.string.saldo_income_info_description_1)),
                    SaldoInstructionItem(IconUnify.SHOP, it.getString(R.string.saldo_income_info_description_2)),
                    SaldoInstructionItem(IconUnify.WALLET, it.getString(R.string.saldo_refund_info_description_3)),
                ),
                it.getString(R.string.saldo_income_info_title),
                incomeOptions
            )
        }
        return null
    }

    private fun getHeading(headingText: String): Typography {
        return Typography(requireContext()).apply {
            this.setWeight(Typography.BOLD)
            this.setType(Typography.BODY_2)
            this.text = headingText
            this.setTextColor(MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            this.setPadding(0,
                resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                0,
                0
            )
        }
    }

    private fun getBulletList(infoItemList: String): Typography {
        return Typography(requireContext()).apply {
            this.setWeight(Typography.REGULAR)
            this.setType(Typography.BODY_2)
            this.text = infoItemList
            this.setTextColor(MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            this.setPadding(0,
                resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_4),
                0,
                0
            )
        }
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = false
        showHeader = true
        showKnob = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
        setTitle(sheetTitle.toString())
    }

    companion object {
        const val TAG = "SaldoInstruction Tag"
        const val TITLE_TEXT = "title"
        const val SALDO_TYPE = "saldo type"
        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val sheet = SaldoInstructionsBottomSheet()
            sheet.arguments = bundle
            sheet.show(childFragmentManager, TAG)
        }
    }
}