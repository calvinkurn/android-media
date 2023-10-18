package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionNameParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.data.response.UpdateWishlistCollectionNameResponse
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionNamesUseCase
import com.tokopedia.wishlistcollection.domain.UpdateWishlistCollectionNameUseCase
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import javax.inject.Inject

class BottomSheetUpdateWishlistCollectionNameViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase,
    private val updateWishlistCollectionNameUseCase: UpdateWishlistCollectionNameUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collectionNames = MutableLiveData<Result<GetWishlistCollectionNamesResponse.GetWishlistCollectionNames>>()
    val collectionNames: LiveData<Result<GetWishlistCollectionNamesResponse.GetWishlistCollectionNames>>
        get() = _collectionNames

    private val _updateWishlistCollectionNameResult = MutableLiveData<Result<UpdateWishlistCollectionNameResponse.UpdateWishlistCollectionName>>()
    val updateWishlistCollectionNameResult: LiveData<Result<UpdateWishlistCollectionNameResponse.UpdateWishlistCollectionName>>
        get() = _updateWishlistCollectionNameResult

    fun getWishlistCollectionNames() {
        launchCatchError(block = {
            val result = getWishlistCollectionNamesUseCase(Unit)
            if (result.getWishlistCollectionNames.status == WishlistV2CommonConsts.OK && result.getWishlistCollectionNames.errorMessage.isEmpty()) {
                _collectionNames.value = Success(result.getWishlistCollectionNames)
            } else {
                _collectionNames.value = Fail(Throwable())
            }
        }, onError = {
                _collectionNames.value = Fail(it)
            })
    }

    fun updateWishlistCollectionName(param: UpdateWishlistCollectionNameParams) {
        launchCatchError(block = {
            val result = updateWishlistCollectionNameUseCase(param)
            if (result.updateWishlistCollectionName.status == WishlistV2CommonConsts.OK && result.updateWishlistCollectionName.errorMessage.isEmpty()) {
                _updateWishlistCollectionNameResult.value = Success(result.updateWishlistCollectionName)
            } else {
                _updateWishlistCollectionNameResult.value = Fail(Throwable())
            }
        }, onError = {
                _updateWishlistCollectionNameResult.value = Fail(it)
            })
    }
}
