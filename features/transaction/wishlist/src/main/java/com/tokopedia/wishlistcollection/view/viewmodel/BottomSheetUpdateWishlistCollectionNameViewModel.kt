package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcollection.data.params.UpdateWishlistCollectionNameParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionNamesResponse
import com.tokopedia.wishlistcollection.data.response.UpdateWishlistCollectionNameResponse
import com.tokopedia.wishlistcollection.domain.GetWishlistCollectionNamesUseCase
import com.tokopedia.wishlistcollection.domain.UpdateWishlistCollectionNameUseCase
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BottomSheetUpdateWishlistCollectionNameViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionNamesUseCase: GetWishlistCollectionNamesUseCase,
    private val updateWishlistCollectionNameUseCase: UpdateWishlistCollectionNameUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collectionNames = MutableLiveData<Result<GetWishlistCollectionNamesResponse.Data.GetWishlistCollectionNames>>()
    val collectionNames: LiveData<Result<GetWishlistCollectionNamesResponse.Data.GetWishlistCollectionNames>>
        get() = _collectionNames

    private val _updateWishlistCollectionNameResult = MutableLiveData<Result<UpdateWishlistCollectionNameResponse.Data.UpdateWishlistCollectionName>>()
    val updateWishlistCollectionNameResult: LiveData<Result<UpdateWishlistCollectionNameResponse.Data.UpdateWishlistCollectionName>>
        get() = _updateWishlistCollectionNameResult

    fun getWishlistCollectionNames() {
        launch(dispatcher.main) {
            val result =
                withContext(dispatcher.io) { getWishlistCollectionNamesUseCase.executeOnBackground() }
            if (result is Success) {
                _collectionNames.value = result
            } else {
                val error = (result as Fail).throwable
                _collectionNames.value = Fail(error)
            }
        }
    }

    fun updateWishlistCollectionName(collectionId: String, collectionName: String) {
        launch(dispatcher.main) {
            val result =
                withContext(dispatcher.io) {
                    val updateWishlistCollectionParams = UpdateWishlistCollectionNameParams(collectionId = collectionId, collectionName = collectionName)
                    updateWishlistCollectionNameUseCase.setParams(updateWishlistCollectionParams)
                    updateWishlistCollectionNameUseCase.executeOnBackground()
                }
            if (result is Success) {
                _updateWishlistCollectionNameResult.value = result
            } else {
                val error = (result as Fail).throwable
                _updateWishlistCollectionNameResult.value = Fail(error)
            }
        }
    }
}