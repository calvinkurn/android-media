package com.tokopedia.catalogcommon.listener

import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel
import com.tokopedia.catalogcommon.uimodel.TrustMakerUiModel

interface VideoExpertListener {

    fun onClickVideoExpert()

    fun onVideoExpertImpression(itemHasSaw:List<ExpertReviewUiModel.ItemExpertReviewUiModel>)

}
