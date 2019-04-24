package com.tokopedia.loginphone.addname.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.loginphone.addname.listener.AddNameListener
import javax.inject.Inject

/**
 * @author by nisie on 23/04/19.
 */
class AddNamePresenter @Inject(val loginRegisterPhoneNumberUseCase :
LoginRegisterPhoneNumberUseCase):
        BaseDaggerPresenter<AddNameListener.View>(), AddNameListener
.Presenter {

    override fun registerPhoneNumberAndName(name: String) {
            view.showLoading()
            loginRegisterPhoneNumberUseCase.execute(
                    RegisterPhoneNumberUseCase.getParamsWithName(
                            view.getPhoneNumber(), name),
                    AddNameSubscriber(view))

    }

}