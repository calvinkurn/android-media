package com.tokopedia.saldodetails.saldoDetail.domain.data

data class SaldoInstructionUIModel(
    val instructionList: ArrayList<SaldoInstructionItem>,
    val heading: String,
    val optionText: String
)
data class SaldoInstructionItem(
    val iconId: Int,
    val instructionText: String
)