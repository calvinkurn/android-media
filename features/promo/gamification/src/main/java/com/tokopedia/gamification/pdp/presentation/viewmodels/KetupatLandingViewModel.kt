package com.tokopedia.gamification.pdp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.domain.usecase.KetupatLandingUseCase
import com.tokopedia.notifications.common.launchCatchError
import javax.inject.Inject

class KetupatLandingViewModel @Inject constructor(
    private val ketupatLandingUseCase: KetupatLandingUseCase
) : BaseViewModel() {

    private val errorMessage = MutableLiveData<Throwable>()
    var data: KetupatLandingPageData? = null

    fun getScratchCardsLandingInfo(slug: String = "") {
        launchCatchError(
            block = {
                data = ketupatLandingUseCase.getScratchCardLandingPage(slug)
            },
            onError = {
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun getErrorMessage(): LiveData<Throwable> = errorMessage
}
