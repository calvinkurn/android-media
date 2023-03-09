package com.tokopedia.mvcwidget.multishopmvc.verticallist

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.mvcwidget.GqlUseCaseWrapper
import com.tokopedia.mvcwidget.TP_CATALOG_MULTISHOP_MVC_LIST_QUERY
import com.tokopedia.mvcwidget.multishopmvc.data.MerchantCouponResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@GqlQuery("TpMvcCatalogList", TP_CATALOG_MULTISHOP_MVC_LIST_QUERY)
class MerchantCouponUsecase @Inject constructor(var gqlWrapper: GqlUseCaseWrapper?)  {

     suspend fun getResponse(map: HashMap<String, Any>) = withContext(Dispatchers.IO)  {
         gqlWrapper?.getResponse(MerchantCouponResponse::class.java, TpMvcCatalogList.GQL_QUERY,map)
    }

    fun getQueryParams(page: Int): HashMap<String, Any> {
        val variables = HashMap<String, Any>()
        variables[MerchantCouponUsecaseParams.PAGE] = page
        variables[MerchantCouponUsecaseParams.PAGESIZE] = 10
        return variables
    }
}

object MerchantCouponUsecaseParams{
    const val PAGE = "page"
    const val PAGESIZE = "pageSize"
}
