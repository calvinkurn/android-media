package com.tokopedia.expresscheckout.view.variant

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.expresscheckout.data.entity.ExpressCheckoutResponse
import com.tokopedia.expresscheckout.view.variant.mapper.DataMapper
import com.tokopedia.expresscheckout.view.variant.mapper.ViewModelMapper
import com.tokopedia.expresscheckout.R

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantPresenter : BaseDaggerPresenter<CheckoutVariantContract.View>(), CheckoutVariantContract.Presenter {

    override fun attachView(view: CheckoutVariantContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
    }

    override fun loadData() {
        // Todo : load dataModel using usecase
        var json = FileUtils().readRawTextFile(view.getActivityContext(), R.raw.response_ok)
        var response: ExpressCheckoutResponse = Gson().fromJson(json, ExpressCheckoutResponse::class.java)
        val dataMapper: DataMapper = ViewModelMapper()

        view.showData(dataMapper.convertToViewModels(response.data))
    }

}