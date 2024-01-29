package com.tokopedia.catalogcommon.listener

import com.tokopedia.catalogcommon.uimodel.ExpertReviewUiModel

interface VideoExpertListener {

    fun onClickVideoExpert()

    fun onVideoExpertImpression(itemHasSaw: ExpertReviewUiModel)

}
