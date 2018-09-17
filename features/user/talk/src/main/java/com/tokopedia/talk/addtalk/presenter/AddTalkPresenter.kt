package com.tokopedia.talk.addtalk.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.talk.addtalk.view.listener.AddTalkContract
import com.tokopedia.talk.common.di.TalkScope
import com.tokopedia.talk.common.domain.usecase.DeleteCommentTalkUseCase
import com.tokopedia.talk.common.domain.usecase.MarkTalkNotFraudUseCase
import com.tokopedia.talk.producttalk.domain.usecase.GetProductTalkUseCase
import com.tokopedia.talk.producttalk.view.listener.ProductTalkContract
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

/**
 * @author : Steven 17/09/18
 */

class AddTalkPresenter @Inject constructor(@TalkScope val userSession: UserSession) :
        AddTalkContract.Presenter,
        BaseDaggerPresenter<AddTalkContract.View>() {

}