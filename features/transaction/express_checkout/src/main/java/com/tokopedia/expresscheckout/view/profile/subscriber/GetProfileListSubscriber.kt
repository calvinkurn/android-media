package com.tokopedia.expresscheckout.view.profile.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.expresscheckout.data.entity.response.profile.ProfileListGqlResponse
import com.tokopedia.expresscheckout.domain.mapper.profile.ProfileDataMapper
import com.tokopedia.expresscheckout.domain.mapper.profile.ProfileDomainModelMapper
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileContract
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 18/12/19.
 */

class GetProfileListSubscriber(val view: CheckoutProfileContract.View?, val presenter: CheckoutProfileContract.Presenter) : Subscriber<GraphqlResponse>() {

    private lateinit var domainModelMapper: ProfileDataMapper

    companion object {
        const val STATUS_OK = "OK"
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        view?.showErrorPage(ErrorHandler.getErrorMessage(view.getActivityContext(), e))
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideLoading()
        val profileResponse = response.getData<ProfileListGqlResponse>(ProfileListGqlResponse::class.java)
        if (profileResponse.data.status == STATUS_OK) {
            domainModelMapper = ProfileDomainModelMapper()
            val profileResponseModel = domainModelMapper.convertToDomainModel(profileResponse.data)
            presenter.prepareViewModel(profileResponseModel)
        } else {
            view?.showErrorPage(profileResponse.data.errorMessage.joinToString { " " })
        }
    }

}