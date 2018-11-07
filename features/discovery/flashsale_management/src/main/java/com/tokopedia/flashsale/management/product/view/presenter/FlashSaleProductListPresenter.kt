package com.tokopedia.flashsale.management.product.view.presenter

import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.campaignlist.Criteria
import com.tokopedia.flashsale.management.data.seller_status.SellerStatus
import com.tokopedia.flashsale.management.product.data.FlashSaleCategoryListGQL
import com.tokopedia.flashsale.management.product.data.FlashSaleProductGQL
import com.tokopedia.flashsale.management.product.data.FlashSaleProductHeader
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleCategoryListUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleProductUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleTnCUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import kotlinx.coroutines.experimental.*
import java.lang.NullPointerException
import javax.inject.Inject
import javax.inject.Named

class FlashSaleProductListPresenter @Inject constructor(val getFlashSaleProductUseCase: GetFlashSaleProductUseCase,
                                                        val getFlashSaleCategoryListUseCase: GetFlashSaleCategoryListUseCase,
                                                        @Named(FlashSaleConstant.NAMED_REQUEST_SELLER_STATUS)
                                                        private val sellerStatusUseCase: GraphqlUseCase<SellerStatus.Response>,
                                                        val getFlashSaleTnCUseCase: GetFlashSaleTnCUseCase,
                                                        val userSession: UserSession) {

    protected var parentJob: Job = Job()

    fun getEligibleProductList(campaignId: Int, campaignSlug: String, offset: Int, rows: Int, q: String,
                               filterId: Int,
                               onSuccess: (FlashSaleProductHeader) -> Unit, onError: (Throwable) -> Unit) {
        parentJob.cancel()
        parentJob = Job()
        GlobalScope.launch(Dispatchers.Main + parentJob) {
            try {
                val shopId = userSession.shopId.toInt()
                getFlashSaleProductUseCase.setParams(campaignId, offset, rows, q,
                        shopId, filterId)
                val flashSaleProductJob = GlobalScope.async(Dispatchers.Default) {
                    try {
                        getFlashSaleProductUseCase.executeOnBackground()
                    } catch (e: Throwable) {
                        onError(e)
                        parentJob.cancel()
                    }
                }

                getFlashSaleCategoryListUseCase.setParams(campaignSlug, shopId)
                val flashSaleCategoryJob = GlobalScope.async(Dispatchers.Default) {
                    try {
                        getFlashSaleCategoryListUseCase.executeOnBackground()
                    } catch (e: Throwable) {
                        onError(e)
                        parentJob.cancel()
                    }
                }
                onSuccess(mergeResult(flashSaleProductJob.await() as FlashSaleProductGQL,
                        flashSaleCategoryJob.await() as FlashSaleCategoryListGQL))
            } catch (throwable: Throwable) {
                onError(throwable)
            }
        }
    }

    fun mergeResult(flashSaleProductGQLResult: FlashSaleProductGQL,
                    flashSaleCategoryListResult: FlashSaleCategoryListGQL): FlashSaleProductHeader {
        val resultProductList = flashSaleProductGQLResult.flashSaleProductGQLData.data
        if (resultProductList.flashSaleProduct.isEmpty()) {
            return resultProductList
        }

        val resultCategoryList: List<Criteria> = flashSaleCategoryListResult.flashSaleCategoryListGQLData.flashSaleCategoryListGQLContent.criteriaList

        val categoryMap = HashMap<Long, String>()
        for (criteria in resultCategoryList) {
            for (category in criteria.categories) {
                categoryMap.put(category.depId, category.depName)
            }
        }

        for (flashSaleProductItem in resultProductList.flashSaleProduct) {
            val departmentNameList = mutableListOf<String>()
            for (departmentId in flashSaleProductItem.departmentId) {
                if (categoryMap.containsKey(departmentId.toLong())) {
                    val departmentName = categoryMap.get(departmentId.toLong())
                    departmentName?.run {
                        departmentNameList.add(departmentName)
                    }
                }
            }
            flashSaleProductItem.departmentName = departmentNameList
        }
        return resultProductList
    }

    fun getSellerStatus(rawQuery: String, campaignSlug: String,
                        onSuccess: (SellerStatus) -> Unit, onError: (Throwable) -> Unit) {
        sellerStatusUseCase.setGraphqlQuery(rawQuery)
        val params = mapOf(FlashSaleConstant.PARAM_SHOP_ID to userSession.shopId.toInt(),
                FlashSaleConstant.PARAM_SLUG to campaignSlug)
        sellerStatusUseCase.setRequestParams(params)
        sellerStatusUseCase.execute({ onSuccess(it.getMojitoSellerStatus.sellerStatus) }, onError)
    }

    fun getFlashSaleTnc(campaignSlug: String,
                        onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        getFlashSaleTnCUseCase.setParams(campaignSlug, userSession.shopId.toInt())
        getFlashSaleTnCUseCase.execute(
                {
                    val tnc = it.flashSaleTncGQLData.flashSaleTncContent.tnc
                    if (tnc.isEmpty()) {
                        onError(NullPointerException())
                    } else {
                        onSuccess(it.flashSaleTncGQLData.flashSaleTncContent.tnc)
                    }
                }, onError)
    }

    fun clearCache() {
        getFlashSaleCategoryListUseCase.clearCache()
    }

    fun detachView() {
        parentJob.cancel()
        sellerStatusUseCase.unsubscribe()
    }
}