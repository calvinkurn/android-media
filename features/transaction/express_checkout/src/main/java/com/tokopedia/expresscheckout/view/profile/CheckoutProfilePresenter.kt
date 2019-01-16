package com.tokopedia.expresscheckout.view.profile

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.expresscheckout.view.variant.FileUtils
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.data.entity.response.profile.ProfileResponse

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class CheckoutProfilePresenter : BaseDaggerPresenter<CheckoutProfileContract.View>(), CheckoutProfileContract.Presenter {

    override fun attachView(view: CheckoutProfileContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
    }

    override fun loadData() {
        view?.showLoading()
        var json = FileUtils().readRawTextFile(view.getActivityContext(), R.raw.profile_response_ok)
        var response: ProfileResponse = Gson().fromJson(json, ProfileResponse::class.java)
//        val dataMapper: DataMapper = ViewModelMapper()
//        view.showData(dataMapper.convertToViewModels(response.data))

    }

}