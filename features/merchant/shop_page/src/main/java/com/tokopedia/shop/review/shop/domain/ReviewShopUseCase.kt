package com.tokopedia.shop.review.shop.domain

import com.tokopedia.shop.review.product.data.model.reviewlist.DataResponseReviewShop
import com.tokopedia.shop.review.shop.domain.model.GetLikeDislikeReviewDomain
import com.tokopedia.shop.review.shop.domain.repository.ReputationRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 1/19/18.
 */
class ReviewShopUseCase @Inject constructor(private val reputationRepository: ReputationRepository, private val getLikeDislikeReviewUseCase: GetLikeDislikeReviewUseCase) : UseCase<DataResponseReviewShop>() {
    override fun createObservable(requestParams: RequestParams): Observable<DataResponseReviewShop> {
        return reputationRepository.getReviewShopList(requestParams.paramsAllValueInString)
                .flatMap(Func1<DataResponseReviewShop?, Observable<DataResponseReviewShop>> { dataResponseReviewShop: DataResponseReviewShop? ->
                    if (dataResponseReviewShop!!.list != null && dataResponseReviewShop.list!!.size > 0) {
                        return@Func1 getLikeDislikeReviewUseCase.createObservable(
                                GetLikeDislikeReviewUseCase.Companion.getParam(createReviewIds(dataResponseReviewShop), requestParams.getString(USER_ID, "")))
                                .map { getLikeDislikeReviewDomain: GetLikeDislikeReviewDomain? -> mapLikeModelToReviewModel(getLikeDislikeReviewDomain, dataResponseReviewShop) }
                    } else {
                        return@Func1 Observable.just(dataResponseReviewShop)
                    }
                })
    }

    private fun mapLikeModelToReviewModel(getLikeDislikeReviewDomain: GetLikeDislikeReviewDomain?,
                                          dataResponseReviewShop: DataResponseReviewShop?): DataResponseReviewShop? {
        for (review in dataResponseReviewShop!!.list!!) {
            for (likeDislikeListDomain in getLikeDislikeReviewDomain!!.list!!) {
                if (likeDislikeListDomain.reviewId == review.reviewId) {
                    review.totalLike = likeDislikeListDomain.totalLike
                    review.likeStatus = likeDislikeListDomain.likeStatus
                    break
                }
            }
        }
        return dataResponseReviewShop
    }

    private fun createReviewIds(dataResponseReviewShop: DataResponseReviewShop?): String {
        val listIds: MutableList<String> = ArrayList()
        for (review in dataResponseReviewShop!!.list!!) {
            listIds.add(java.lang.String.valueOf(review.reviewId))
        }
        return listIds.joinToString("~")
    }

    fun createRequestParams(shopDomain: String?, shopId: String?, page: String?, userId: String?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(SHOP_DOMAIN, shopDomain)
        requestParams.putString(SHOP_ID, shopId)
        requestParams.putString(PAGE, page)
        requestParams.putString(PER_PAGE, DEFAULT_PER_PAGE)
        requestParams.putString(USER_ID, userId)
        return requestParams
    }

    companion object {
        const val SHOP_DOMAIN = "shop_domain"
        const val SHOP_ID = "shop_id"
        const val PAGE = "page"
        const val PER_PAGE = "per_page"
        const val DEFAULT_PER_PAGE = "12"
        private const val USER_ID = "user_id"
    }

}