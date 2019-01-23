package com.tokopedia.expresscheckout.view.profile.subscriber

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

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        // Todo : show error
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideLoading()
        val profileResponse = response.getData<ProfileListGqlResponse>(ProfileListGqlResponse::class.java)
        if (profileResponse.data.status == "OK") {
            domainModelMapper = ProfileDomainModelMapper()
            val profileResponseModel = domainModelMapper.convertToDomainModel(profileResponse.data)
            presenter.prepareViewModel(profileResponseModel)
        } else {
            // todo : show error
        }
    }

}