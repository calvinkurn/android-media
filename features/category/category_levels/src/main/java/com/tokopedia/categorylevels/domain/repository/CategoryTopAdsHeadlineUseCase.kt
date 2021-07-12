package com.tokopedia.categorylevels.domain.repository

import com.tokopedia.discovery2.repository.topads.TopAdsHeadlineRepository
import com.tokopedia.topads.sdk.utils.*
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val TOPADS_HEADLINE_VALUE_SRC = "directory"
const val PARAM_DEP_ID = "dep_id"

class CategoryTopAdsHeadlineUseCase @Inject constructor(): TopAdsHeadlineRepository {

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun getHeadlineAdsParams(depId: String, paramsMobile : String): String {
        return UrlParamHelper.generateUrlParamString(mutableMapOf(
                PARAM_DEVICE to VALUE_DEVICE,
                PARAM_PAGE to 0,
                PARAM_DEP_ID to depId,
                PARAM_EP to VALUE_EP,
                PARAM_HEADLINE_PRODUCT_COUNT to VALUE_HEADLINE_PRODUCT_COUNT,
                PARAM_ITEM to VALUE_ITEM,
                PARAM_SRC to TOPADS_HEADLINE_VALUE_SRC,
                PARAM_TEMPLATE_ID to VALUE_TEMPLATE_ID,
                PARAM_USER_ID to userSession.userId
        ))
    }
}