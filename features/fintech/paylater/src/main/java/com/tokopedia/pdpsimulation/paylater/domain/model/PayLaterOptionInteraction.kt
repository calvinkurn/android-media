package com.tokopedia.pdpsimulation.paylater.domain.model

import com.tokopedia.pdpsimulation.common.analytics.PayLaterAnalyticsBase

data class PayLaterOptionInteraction(
        val onCtaClicked: (Detail) -> Unit,
        val installementDetails: (Detail) -> Unit,
        val seeMoreOptions: (Int) -> Unit,
        val invokeAnalytics: (PayLaterAnalyticsBase) -> Unit,
        val retryLoading: () -> Unit,
)