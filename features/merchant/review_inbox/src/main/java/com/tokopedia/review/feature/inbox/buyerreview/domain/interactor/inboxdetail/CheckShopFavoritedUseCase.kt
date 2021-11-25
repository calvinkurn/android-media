package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.review.feature.inbox.buyerreview.data.repository.ReputationRepository
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.CheckShopFavoriteDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 9/26/17.
 */
class CheckShopFavoritedUseCase @Inject constructor(private val reputationRepository: ReputationRepository) :
    UseCase<CheckShopFavoriteDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<CheckShopFavoriteDomain> {
        return (reputationRepository.checkIsShopFavorited(requestParams))
    }

    companion object {
        const val PARAM_USER_ID: String = "PARAM_USER_ID"
        const val PARAM_SHOP_ID: String = "PARAM_SHOP_ID"

        fun getParam(loginID: String?, shopId: Long): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putString(PARAM_USER_ID, loginID)
            params.putString(PARAM_SHOP_ID, shopId.toString())
            return params
        }
    }
}