package com.tokopedia.shop.flash_sale.presentation.draft.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignCancellationListUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DraftDeleteViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val cancellationListUseCase: GetSellerCampaignCancellationListUseCase
) : BaseViewModel(dispatchers.main) {

    fun getData(param: String) {
        println(param)
        launchCatchError(block = {
            val aaa = withContext(dispatchers.io) {
                cancellationListUseCase.execute()
            }

            println(aaa)
        }, onError = {

        })
    }
}