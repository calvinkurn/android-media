package com.tokopedia.sellerorder.buyer_request_cancel.presentation

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerorder.common.domain.usecase.SomAcceptOrderUseCase
import com.tokopedia.sellerorder.common.domain.usecase.SomEditRefNumUseCase
import com.tokopedia.sellerorder.common.domain.usecase.SomRejectCancelOrderUseCase
import com.tokopedia.sellerorder.common.domain.usecase.SomRejectOrderUseCase
import com.tokopedia.sellerorder.common.domain.usecase.SomValidateOrderUseCase
import com.tokopedia.sellerorder.common.presenter.viewmodel.SomOrderBaseViewModel
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class BuyerRequestCancelRespondViewModel @Inject constructor(
    somAcceptOrderUseCase: SomAcceptOrderUseCase,
    somRejectOrderUseCase: SomRejectOrderUseCase,
    somEditRefNumUseCase: SomEditRefNumUseCase,
    somRejectCancelOrderRequest: SomRejectCancelOrderUseCase,
    somValidateOrderUseCase: SomValidateOrderUseCase,
    userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers,
    authorizeSomDetailAccessUseCase: AuthorizeAccessUseCase,
    authorizeReplyChatAccessUseCase: AuthorizeAccessUseCase
) : SomOrderBaseViewModel(
    dispatcher, userSession, somAcceptOrderUseCase, somRejectOrderUseCase,
    somEditRefNumUseCase, somRejectCancelOrderRequest, somValidateOrderUseCase,
    authorizeSomDetailAccessUseCase, authorizeReplyChatAccessUseCase
)
