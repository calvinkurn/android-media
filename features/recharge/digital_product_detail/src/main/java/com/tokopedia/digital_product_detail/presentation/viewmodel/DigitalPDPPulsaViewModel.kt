package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberData
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.view.fragment.TopupBillsFavoriteNumberFragment
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.*
import javax.inject.Inject

class DigitalPDPPulsaViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _dummy = MutableLiveData<Result<Boolean>>()
    val dummy: LiveData<Result<Boolean>>
        get() = _dummy

    private val _favoriteNumberData = MutableLiveData<Result<List<TopupBillsSeamlessFavNumberItem>>>()
    val favoriteNumberData: LiveData<Result<List<TopupBillsSeamlessFavNumberItem>>>
        get() = _favoriteNumberData

    private val _catalogProductInput = MutableLiveData<Result<List<DenomWidgetModel>>>()
    val catalogProductInput: LiveData<Result<List<DenomWidgetModel>>>
        get() = _catalogProductInput

    private var debounceJob: Job? = null

    private val _errorMessage = MutableLiveData<Result<String>>()
    val errorMessage: LiveData<Result<String>>
        get() = _errorMessage

    fun getDelayedResponse() {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(coroutineContext).launch {
            launchCatchError(block = {
                delay(1000)
                _dummy.postValue(Success(true))
            }) {

            }
        }
    }

    fun getFavoriteNumber() {
        val favoriteNumber = listOf<TopupBillsSeamlessFavNumberItem>(
            TopupBillsSeamlessFavNumberItem(
                "", "081208120812", "", "", "", ""),
            TopupBillsSeamlessFavNumberItem(
                "", "081208120812", "AAAAaaaaAA", "", "", ""),
            TopupBillsSeamlessFavNumberItem(
                "", "081208120812", "BBBBBBBBbbbbbb", "", "", ""),
            TopupBillsSeamlessFavNumberItem(
                "", "087808780878", "", "", "", ""),
        )
        _favoriteNumberData.postValue(Success(favoriteNumber))
    }
}