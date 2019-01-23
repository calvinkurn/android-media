package com.tokopedia.expresscheckout.view.profile

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.data.entity.response.profile.ProfileListGqlResponse
import com.tokopedia.expresscheckout.data.entity.response.profile.ProfileResponse
import com.tokopedia.expresscheckout.domain.model.profile.ProfileResponseModel
import com.tokopedia.expresscheckout.view.profile.mapper.ViewModelMapper
import com.tokopedia.expresscheckout.view.profile.subscriber.GetProfileListSubscriber
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class CheckoutProfilePresenter : BaseDaggerPresenter<CheckoutProfileContract.View>(), CheckoutProfileContract.Presenter {

    private val getProfileListUseCase = GraphqlUseCase()
    private lateinit var viewModelMapper: ViewModelMapper

    override fun attachView(view: CheckoutProfileContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
    }

    override fun loadData() {
        view?.showLoading()

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view.getActivityContext()?.resources,
                R.raw.query_profile_list), ProfileListGqlResponse::class.java, null)
        getProfileListUseCase.clearRequest()
        getProfileListUseCase.addRequest(graphqlRequest)
        getProfileListUseCase.execute(RequestParams.create(), GetProfileListSubscriber(view, this))

    }

    override fun prepareViewModel(profileResponseModel: ProfileResponseModel) {
        viewModelMapper = ViewModelMapper()
        view?.setData(viewModelMapper.convertToViewModels(profileResponseModel))
    }

}