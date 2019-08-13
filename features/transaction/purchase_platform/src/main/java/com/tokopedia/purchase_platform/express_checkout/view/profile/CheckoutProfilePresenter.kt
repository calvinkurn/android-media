package com.tokopedia.purchase_platform.express_checkout.view.profile

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.purchase_platform.express_checkout.domain.model.profile.ProfileResponseModel
import com.tokopedia.purchase_platform.express_checkout.domain.usecase.GetProfileListUseCase
import com.tokopedia.purchase_platform.express_checkout.view.profile.mapper.ViewModelMapper
import com.tokopedia.purchase_platform.express_checkout.view.profile.subscriber.GetProfileListSubscriber
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class CheckoutProfilePresenter @Inject constructor(val getProfileListUseCase: GetProfileListUseCase,
                                                   val viewModelMapper: ViewModelMapper) :
        BaseDaggerPresenter<CheckoutProfileContract.View>(), CheckoutProfileContract.Presenter {

    override fun detachView() {
        getProfileListUseCase.unsubscribe()
        super.detachView()
    }

    override fun loadData() {
        view?.showLoading()
        getProfileListUseCase.execute(RequestParams.create(), GetProfileListSubscriber(view, this))
    }

    override fun prepareViewModel(profileResponseModel: ProfileResponseModel) {
        view?.setData(viewModelMapper.convertToViewModels(profileResponseModel))
    }

}