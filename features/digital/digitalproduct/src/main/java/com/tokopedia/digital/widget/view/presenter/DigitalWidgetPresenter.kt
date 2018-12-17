package com.tokopedia.digital.widget.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.digital.widget.domain.interactor.DigitalWidgetUseCase
import com.tokopedia.digital.widget.view.model.category.Category
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

/**
 * Created by Rizky on 19/11/18.
 */
class DigitalWidgetPresenter(private val digitalWidgetUseCase: DigitalWidgetUseCase) :
        BaseDaggerPresenter<DigitalWidgetContract.View>(), DigitalWidgetContract.Presenter {

    override fun fetchDataRechargeCategory() {
        digitalWidgetUseCase.execute(RequestParams.EMPTY, object : Subscriber<List<Category>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.renderErrorNetwork(e.message)
            }

            override fun onNext(categories: List<Category>) {
                view.renderDataRechargeCategory(categories)
            }
        })
    }

}