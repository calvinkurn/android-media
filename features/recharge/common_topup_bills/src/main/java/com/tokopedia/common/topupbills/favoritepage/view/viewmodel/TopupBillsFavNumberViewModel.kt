package com.tokopedia.common.topupbills.favoritepage.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.favoritecommon.data.TopupBillsPersoFavNumber
import com.tokopedia.common.topupbills.favoritepage.data.UpdateFavoriteDetail
import com.tokopedia.common.topupbills.favoritepage.domain.usecase.RechargeFavoriteNumberUseCase
import com.tokopedia.common.topupbills.favoritepage.domain.usecase.ModifyRechargeFavoriteNumberUseCase
import com.tokopedia.common.topupbills.favoritepage.view.listener.FavoriteNumberDeletionListener
import com.tokopedia.common.topupbills.favoritepage.view.util.FavoriteNumberActionType
import com.tokopedia.common.topupbills.favoritepage.view.util.FavoriteNumberActionType.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TopupBillsFavNumberViewModel @Inject constructor(
    private val rechargeFavoriteNumberUseCase: RechargeFavoriteNumberUseCase,
    private val modifyRechargeFavoriteNumberUseCase: ModifyRechargeFavoriteNumberUseCase,
    val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io) {

    private val _persoFavNumberData = MutableLiveData<Result<Pair<TopupBillsPersoFavNumber, Boolean>>>()
    val persoFavNumberData: LiveData<Result<Pair<TopupBillsPersoFavNumber, Boolean>>>
        get() = _persoFavNumberData

    private val _seamlessFavNumberUpdateData = MutableLiveData<Result<UpdateFavoriteDetail>>()
    val seamlessFavNumberUpdateData: LiveData<Result<UpdateFavoriteDetail>>
        get() = _seamlessFavNumberUpdateData

    private val _seamlessFavNumberDeleteData = MutableLiveData<Result<UpdateFavoriteDetail>>()
    val seamlessFavNumberDeleteData: LiveData<Result<UpdateFavoriteDetail>>
        get() = _seamlessFavNumberDeleteData

    private val _seamlessFavNumberUndoDeleteData = MutableLiveData<Result<UpdateFavoriteDetail>>()
    val seamlessFavNumberUndoDeleteData: LiveData<Result<UpdateFavoriteDetail>>
        get() = _seamlessFavNumberUndoDeleteData

    fun getPersoFavoriteNumbers(
        categoryIds: List<Int>,
        operatorIds: List<Int>,
        shouldRefreshInputNumber: Boolean = true,
        prevActionType: FavoriteNumberActionType? = null
    ) {
        launchCatchError(block = {
            val favoriteNumber = rechargeFavoriteNumberUseCase.apply {
                setRequestParams(categoryIds, operatorIds, CHANNEL_FAVORITE_NUMBER_LIST)
            }.executeOnBackground()
            _persoFavNumberData.postValue(Success(favoriteNumber.persoFavoriteNumber to shouldRefreshInputNumber))
        }) {
            val errMsg = when (prevActionType) {
                UPDATE -> ERROR_FETCH_AFTER_UPDATE
                DELETE -> ERROR_FETCH_AFTER_DELETE
                UNDO_DELETE -> ERROR_FETCH_AFTER_UNDO_DELETE
                else -> it.message
            }
            _persoFavNumberData.postValue(Fail(Throwable(errMsg)))
        }
    }

    fun modifySeamlessFavoriteNumber(
        categoryId: Int,
        productId: Int,
        clientNumber: String,
        hashedClientNumber: String,
        totalTransaction: Int,
        label: String,
        isDelete: Boolean,
        source: String,
        actionType: FavoriteNumberActionType,
        operatorName: String = "",
        onDeleteCallback: FavoriteNumberDeletionListener? = null
    ) {
        launchCatchError(block = {
            val data = modifyRechargeFavoriteNumberUseCase.apply {
                setRequestParams(
                    categoryId,
                    productId,
                    clientNumber,
                    hashedClientNumber,
                    totalTransaction,
                    label,
                    isDelete,
                    source,
                )
            }.executeOnBackground()

            when (actionType) {
                UPDATE -> _seamlessFavNumberUpdateData.postValue(Success(data.updateFavoriteDetail))
                DELETE -> {
                    _seamlessFavNumberDeleteData.postValue(Success(data.updateFavoriteDetail))
                    onDeleteCallback?.onSuccessDelete(operatorName)
                }
                UNDO_DELETE -> _seamlessFavNumberUndoDeleteData.postValue(Success(data.updateFavoriteDetail))
            }
        }) {
            when (actionType) {
                UPDATE -> _seamlessFavNumberUpdateData.postValue(Fail(it))
                DELETE -> {
                    _seamlessFavNumberDeleteData.postValue(Fail(it))
                    onDeleteCallback?.onFailedDelete()
                }
                UNDO_DELETE -> _seamlessFavNumberUndoDeleteData.postValue(Fail(it))
            }
        }
    }

    fun createSourceParam(categoryIds: List<Int>): String {
        return modifyRechargeFavoriteNumberUseCase.createSourceParam(categoryIds)
    }

    companion object {
        const val CHANNEL_FAVORITE_NUMBER_LIST = "favorite_number_list"
        const val ERROR_FETCH_AFTER_UPDATE = "ERROR_UPDATE"
        const val ERROR_FETCH_AFTER_DELETE = "ERROR_DELETE"
        const val ERROR_FETCH_AFTER_UNDO_DELETE = "ERROR_UNDO_DELETE"
    }
}
