package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.cloud.model.GoldGetPmOsStatus
import com.tokopedia.power_merchant.subscribe.data.model.GoldCancellationsQuestionaireResponse

class PMCancellationQuestionnaireData(
        val expiredDate: String,
        val listQuestion: MutableList<PMCancellationQuestionnaireQuestionModel>
)