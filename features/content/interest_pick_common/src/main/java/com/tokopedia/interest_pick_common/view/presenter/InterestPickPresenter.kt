package com.tokopedia.interest_pick_common.view.presenter

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.interest_pick_common.domain.usecase.GetInterestPickUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-11-08
 */

class InterestPickPresenter @Inject constructor(
        baseDispatcher: CoroutineDispatcher,
        getInterestPickUseCase: GetInterestPickUseCase
):BaseViewModel(baseDispatcher) {

    companion object {
        val PARAM_SOURCE_RECOM_PROFILE_CLICK = "click_recom_profile"
        val PARAM_SOURCE_SEE_ALL_CLICK = "click_see_all"
    }


}