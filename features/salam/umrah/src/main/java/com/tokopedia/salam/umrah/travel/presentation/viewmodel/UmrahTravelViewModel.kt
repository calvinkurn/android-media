package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.salam.umrah.common.util.UmrahDispatchersProvider
import com.tokopedia.salam.umrah.travel.presentation.usecase.UmrahTravelUseCase
import javax.inject.Inject

class UmrahTravelViewModel  @Inject constructor(private val umrahTravelUseCase: UmrahTravelUseCase,
                                                dispatcher: UmrahDispatchersProvider)
    : BaseViewModel(dispatcher.Main) {

}