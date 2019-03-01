package com.tokopedia.checkout.view.feature.promomerchant.view

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import javax.inject.Inject

/**
 * Created by fwidjaja on 01/03/19.
 */
class PromoMerchantPresenter @Inject
constructor() : BaseDaggerPresenter<PromoMerchantContract.View>(), PromoMerchantContract.Presenter {
    override fun loadPromoMerchantList() {}
}