package com.tokopedia.digital.widget.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.digital.R
import com.tokopedia.digital.widget.domain.interactor.DigitalRecommendationUseCase
import com.tokopedia.digital.widget.view.model.Recommendation
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Rizky on 15/11/18.
 */
class DigitalChannelPresenter @Inject constructor(private val recommendationUseCase: DigitalRecommendationUseCase) :
        BaseDaggerPresenter<DigitalChannelContract.View>(), DigitalChannelContract.Presenter {

    override fun getRecommendationList(deviceId: Int) {
        val params = recommendationUseCase.createRequestParams(deviceId)

        recommendationUseCase.execute(params, object : Subscriber<List<Recommendation>>() {
            override fun onNext(it: List<Recommendation>) {
                if (!it.isEmpty()) {
                    view.renderRecommendationList(it)
                    view.renderDigitalTitle(R.string.digital_my_channel_top_up_tagihan_title)
                } else {
                    view.fetchCategoryList()
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (isViewAttached) {
                    view.showError(R.string.digital_channel_error_default)
                }
            }
        })
    }

    override fun detachView() {
        recommendationUseCase.unsubscribe()
        super.detachView()
    }

}