package com.tokopedia.flashsale.management.product.view.presenter

import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.campaignlist.Criteria
import com.tokopedia.flashsale.management.data.seller_status.SellerStatus
import com.tokopedia.flashsale.management.product.data.FlashSaleCategoryListGQL
import com.tokopedia.flashsale.management.product.data.FlashSaleProductGQL
import com.tokopedia.flashsale.management.product.data.FlashSaleProductHeader
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleCategoryListUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleProductUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleTncUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.SubmitProductUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import kotlinx.coroutines.experimental.*
import java.lang.NullPointerException
import javax.inject.Inject
import javax.inject.Named

class FlashSaleProductListPresenter @Inject constructor(val getFlashSaleProductUseCase: GetFlashSaleProductUseCase,
                                                        val getFlashSaleCategoryListUseCase: GetFlashSaleCategoryListUseCase,
                                                        @Named(FlashSaleConstant.NAMED_REQUEST_SELLER_STATUS)
                                                        private val sellerStatusUseCase: GraphqlUseCase<SellerStatus.Response>,
                                                        val getFlashSaleTncUseCase: GetFlashSaleTncUseCase,
                                                        val submitProductUseCase: SubmitProductUseCase,
                                                        val userSession: UserSession) {

    protected var getProductListJob: Job = Job()

    fun getEligibleProductList(campaignId: Int, campaignSlug: String, offset: Int, rows: Int, q: String,
                               filterId: Int,
                               onSuccess: (FlashSaleProductHeader) -> Unit, onError: (Throwable) -> Unit) {
        getProductListJob.cancel()
        getProductListJob = Job()
        val handler = CoroutineExceptionHandler { _, ex ->
            GlobalScope.launch(Dispatchers.Main) {
                onError(ex)
            }
        }
        GlobalScope.launch(Dispatchers.Main + getProductListJob + handler) {
            val shopId = userSession.shopId.toInt()
            getFlashSaleProductUseCase.setParams(campaignId, offset, rows, q,
                    shopId, filterId)
            val flashSaleProductJob = GlobalScope.async(Dispatchers.Default + handler) {
                getFlashSaleProductUseCase.executeOnBackground()
            }

            getFlashSaleCategoryListUseCase.setParams(campaignSlug, shopId)
            val flashSaleCategoryJob = GlobalScope.async(Dispatchers.Default + handler) {
                getFlashSaleCategoryListUseCase.executeOnBackground()
            }
            onSuccess(mergeResult(flashSaleProductJob.await(), flashSaleCategoryJob.await()))
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

    fun submitSubmission(campaignId: Int, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        //TODO send the correct message
        submitProductUseCase.setParams(campaignId, userSession.shopId.toInt())
        submitProductUseCase.execute(
                {
                    if ( it.flashSaleSubmitProduct.isSuccess()) {
                        onSuccess(it.flashSaleSubmitProduct.message)
                    } else {
                        onError(MessageErrorException(it.flashSaleSubmitProduct.message))
                    }
                }, onError)
    }

    fun getFlashSaleTnc(campaignSlug: String,
                        onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        getFlashSaleTncUseCase.setParams(campaignSlug, userSession.shopId.toInt())
        getFlashSaleTncUseCase.execute(
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
        getProductListJob.cancel()
        sellerStatusUseCase.unsubscribe()
        getFlashSaleTncUseCase.unsubscribe()
    }
}