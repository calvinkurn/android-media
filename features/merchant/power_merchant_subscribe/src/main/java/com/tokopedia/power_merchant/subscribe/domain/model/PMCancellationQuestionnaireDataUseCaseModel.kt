package com.tokopedia.power_merchant.subscribe.domain.model

import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.power_merchant.subscribe.data.model.GoldCancellationsQuestionaireResponse

data class PMCancellationQuestionnaireDataUseCaseModel(
        val goldGetPmOsStatus: GoldGetPmOsStatus,
        val goldCancellationQuestionnaire: GoldCancellationsQuestionaireResponse
)