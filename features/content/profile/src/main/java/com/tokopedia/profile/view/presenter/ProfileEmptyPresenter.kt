package com.tokopedia.profile.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.profile.domain.usecase.GetProfileHeaderUseCase
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.subscriber.GetProfileHeaderSubscriber
import javax.inject.Inject

/**
 * @author by milhamj on 22/10/18.
 */

class ProfileEmptyPresenter @Inject constructor(
        private val getProfileHeaderUseCase: GetProfileHeaderUseCase)
    : BaseDaggerPresenter<ProfileEmptyContract.View>(), ProfileEmptyContract.Presenter {

    override fun detachView() {
        super.detachView()
        getProfileHeaderUseCase.unsubscribe()
    }

    override fun getProfileHeader(userId: Int) {
        getProfileHeaderUseCase.execute(
                GetProfileHeaderUseCase.createRequestParams(userId),
                GetProfileHeaderSubscriber(view, userId)
        )
    }
}
