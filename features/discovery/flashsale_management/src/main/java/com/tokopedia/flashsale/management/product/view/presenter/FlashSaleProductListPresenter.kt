package com.tokopedia.flashsale.management.product.view.presenter

import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.campaignlist.Criteria
import com.tokopedia.flashsale.management.common.data.SellerStatus
import com.tokopedia.flashsale.management.product.data.*
import com.tokopedia.flashsale.management.product.domain.usecase.*
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import kotlinx.coroutines.experimental.*
import java.lang.NullPointerException
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Named

class FlashSaleProductListPresenter @Inject constructor(val getSubmissionFlashSaleProductUseCase: GetSubmissionFlashSaleProductUseCase,
                                                        val getPostFlashSaleProductUseCase: GetPostFlashSaleProductUseCase,
                                                        val getFlashSaleCategoryListUseCase: GetFlashSaleCategoryListUseCase,
                                                        @Named(FlashSaleConstant.NAMED_REQUEST_SELLER_STATUS)
                                                        private val sellerStatusUseCase: GraphqlUseCase<SellerStatus.Response>,
                                                        val getFlashSaleTncUseCase: GetFlashSaleTncUseCase,
                                                        val submitProductUseCase: SubmitProductUseCase,
                                                        val userSession: UserSessionInterface) {

    protected var getProductListJob: Job = Job()

    fun getEligibleProductList(campaignId: Int, campaignSlug: String, offset: Int, rows: Int, q: String,
                               filterId: Int,
                               onSuccess: (FlashSaleSubmissionProductData) -> Unit, onError: (Throwable) -> Unit) {
        getProductListJob.cancel()
        getProductListJob = Job()
        val handler = CoroutineExceptionHandler { _, ex ->
            CoroutineScope(Dispatchers.Main).launch {
                onError(ex)
            }
        }
        CoroutineScope(Dispatchers.Main + getProductListJob + handler).launch() {
            val shopId = userSession.shopId.toInt()
            getSubmissionFlashSaleProductUseCase.setParams(campaignId, offset, rows, q,
                    shopId, filterId)
            val flashSaleProductJob = async(Dispatchers.Default + handler) {
                getSubmissionFlashSaleProductUseCase.executeOnBackground()
            }

            getFlashSaleCategoryListUseCase.setParams(campaignSlug, shopId)
            val flashSaleCategoryJob = async(Dispatchers.Default + handler) {
                getFlashSaleCategoryListUseCase.executeOnBackground()
            }
            onSuccess(mergeResult(flashSaleProductJob.await(), flashSaleCategoryJob.await()))
        }
    }

    fun getPostProductList(campaignId: Int, campaignSlug: String, start: Int, rows: Int, q: String,
                           statusId: Int,
                           onSuccess: (GetMojitoPostProduct) -> Unit, onError: (Throwable) -> Unit) {
        getProductListJob.cancel()
        getProductListJob = Job()
        val handler = CoroutineExceptionHandler { _, ex ->
            CoroutineScope(Dispatchers.Main).launch {
                onError(ex)
            }
        }
        CoroutineScope(Dispatchers.Main + getProductListJob + handler).launch {
            val shopId = userSession.shopId.toInt()
            getPostFlashSaleProductUseCase.setParams(campaignId, start, rows, q, shopId.toString(), statusId)
            val flashSaleProductJob = async(Dispatchers.Default + handler) {
                getPostFlashSaleProductUseCase.executeOnBackground()
            }

            getFlashSaleCategoryListUseCase.setParams(campaignSlug, shopId)
            val flashSaleCategoryJob = async(Dispatchers.Default + handler) {
                getFlashSaleCategoryListUseCase.executeOnBackground()
            }
            onSuccess(mergeResult(flashSaleProductJob.await(), flashSaleCategoryJob.await()))
        }
    }

    fun mergeResult(flashSaleSubmissionProductGQLResult: FlashSaleSubmissionProductGQL,
                    flashSaleCategoryListResult: FlashSaleCategoryListGQL): FlashSaleSubmissionProductData {
        val resultProductList = flashSaleSubmissionProductGQLResult.mojitoEligibleSellerProduct.data
        if (resultProductList.flashSaleSubmissionProduct.isEmpty()) {
            return resultProductList
        }

        val categoryMap = getCategoryMap(flashSaleCategoryListResult)

        for (flashSaleProductItem in resultProductList.flashSaleSubmissionProduct) {
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

    fun getCategoryMap(flashSaleCategoryListResult: FlashSaleCategoryListGQL): HashMap<Long, String> {
        val resultCategoryList: List<Criteria> = flashSaleCategoryListResult.flashSaleCategoryListGQLData.flashSaleCategoryListGQLContent.criteriaList

        val categoryMap = HashMap<Long, String>()
        for (criteria in resultCategoryList) {
            for (category in criteria.categories) {
                categoryMap.put(category.depId, category.depName)
            }
        }
        return categoryMap
    }

    fun mergeResult(flashSalePostProductGQL: FlashSalePostProductGQL,
                    flashSaleCategoryListResult: FlashSaleCategoryListGQL): GetMojitoPostProduct {
        val resultProductList = flashSalePostProductGQL.getMojitoPostProduct.data
        if (resultProductList.flashSalePostProductList.isEmpty()) {
            return flashSalePostProductGQL.getMojitoPostProduct
        }

        val categoryMap = getCategoryMap(flashSaleCategoryListResult)

        for (flashSaleProductItem in resultProductList.flashSalePostProductList) {
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
        return flashSalePostProductGQL.getMojitoPostProduct
    }

    fun getSellerStatus(rawQuery: String, campaignSlug: String,
                        onSuccess: (SellerStatus) -> Unit, onError: (Throwable) -> Unit) {
        sellerStatusUseCase.setGraphqlQuery(rawQuery)
        val params = mapOf(FlashSaleConstant.PARAM_SHOP_ID to userSession.shopId.toInt(),
                FlashSaleConstant.PARAM_SLUG to campaignSlug)
        sellerStatusUseCase.setRequestParams(params)
        sellerStatusUseCase.execute({ onSuccess(it.getCampaignSellerStatus.sellerStatus) }, onError)
    }

    fun submitSubmission(campaignId: Int, onSuccess: (FlashSaleDataContainer) -> Unit, onError: (Throwable) -> Unit) {
        submitProductUseCase.setParams(campaignId, userSession.shopId.toInt())
        submitProductUseCase.execute(
                {
                    if (it.flashSaleDataContainer.isSuccess()) {
                        onSuccess(it.flashSaleDataContainer)
                    } else {
                        onError(RuntimeException(it.flashSaleDataContainer.statusCode.toString()))
                    }
                }, onError)
    }

    fun getFlashSaleInfoAndTnc(campaignSlug: String,
                               onSuccess: (FlashSaleTncContent) -> Unit, onError: (Throwable) -> Unit) {
        getFlashSaleTncUseCase.setParams(campaignSlug, userSession.shopId.toInt())
        getFlashSaleTncUseCase.execute(
                {
                    val tnc = it.flashSaleTncGQLData.flashSaleTncContent.tnc
                    if (tnc.isEmpty()) {
                        onError(NullPointerException())
                    } else {
                        onSuccess(it.flashSaleTncGQLData.flashSaleTncContent)
                    }
                }, onError)
    }

    fun clearCache() {
        getFlashSaleCategoryListUseCase.clearCache()
        getFlashSaleTncUseCase.clearCache()
    }

    fun detachView() {
        getProductListJob.cancel()
        sellerStatusUseCase.cancelJobs()
        getFlashSaleTncUseCase.cancelJobs()
    }
}