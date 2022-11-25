package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse

interface AffiliateEducationLearnClickInterface {
    fun onKamusClick()
    fun onBantuanClick()
}

interface AffiliateEducationEventArticleClickInterface {
    fun onSeeMoreClick(pageType: String, categoryId: String)
    fun onDetailClick(pageType: String, slug: String)
}

interface AffiliateEducationTopicTutorialClickInterface {
    fun onCardClick(pageType: String, categoryId: String)
}

interface AffiliateEducationSeeAllCardClickInterface {
    fun onCardClick(pageType: String, slug: String)
}

interface AffiliateEducationSocialCTAClickInterface {
    fun onSocialClick(channel: String, url: String)
}

interface AffiliateEducationBannerClickInterface {
    fun onBannerClick(url: String)
}

interface AffiliateEduCategoryChipClick {
    fun onChipClick(type: AffiliateEducationArticleCardsResponse.CardsArticle.Data.CardsItem.Article.CategoriesItem?)
}
