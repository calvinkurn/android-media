package com.tokopedia.notifcenter.view.listener

import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel

interface NotificationAffiliateEduEventListener {
    fun onEducationActiveIndexChanged(
        currentIndex: Int,
        notificationAffiliateEducationUiModel: NotificationAffiliateEducationUiModel
    )
    fun onEducationItemClick(data: AffiliateEducationArticleResponse.CardsArticle.Data.CardsItem.Article)
    fun onEducationLihatSemuaClick()
}
