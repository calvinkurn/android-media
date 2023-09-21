package com.tokopedia.notifcenter.data.uimodel.affiliate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.affiliate.AffiliateEducationArticleResponse
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationTypeFactory

class NotificationAffiliateEducationUiModel(
    val data: AffiliateEducationArticleResponse.CardsArticle.Data.CardsItem
) :
    Visitable<NotificationTypeFactory> {

    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
