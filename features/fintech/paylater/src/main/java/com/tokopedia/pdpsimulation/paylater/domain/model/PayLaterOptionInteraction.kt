package com.tokopedia.pdpsimulation.paylater.domain.model

data class PayLaterOptionInteraction(
    val onCtaClicked: (Detail) -> Unit,
    val installementDetails : (InstallmentDetails) -> Unit,
    val seeMoreOptions: (Int) -> Unit
)