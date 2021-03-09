package com.tokopedia.power_merchant.subscribe.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.domain.interactor.GetPMShopInfoUseCase

/**
 * Created By @ilhamsuaib on 09/03/21
 */

class PowerMerchantSubscriptionViewModel constructor(
        private val getPMShopInfoUseCase: Lazy<GetPMShopInfoUseCase>,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {


}