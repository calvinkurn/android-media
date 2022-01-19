package com.tokopedia.pdpsimulation.paylater.domain.model

data class PayLaterOptionInteraction(
    val onCtaClicked: (Cta) -> Unit,
    val installementDetails : (InstallmentDetails) -> Unit
)