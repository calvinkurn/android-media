package com.tokopedia.wishlistcollection.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BottomSheetAddCollectionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getWishlistCollectionsBottomSheetUseCase: com.tokopedia.wishlistcollection.domain.GetWishlistCollectionsBottomSheetUseCase
) : BaseViewModel(dispatcher.main) {

    private val _collectionsBottomSheet = MutableLiveData<Result<com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet>>()
    val collectionsBottomSheet: LiveData<Result<com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet>>
        get() = _collectionsBottomSheet

    fun getWishlistCollections(productId: String, source: String) {
        getWishlistCollectionsBottomSheetUseCase.setParams(productId, source)
        launch(dispatcher.main) {
            val result =
                withContext(dispatcher.io) { getWishlistCollectionsBottomSheetUseCase.executeOnBackground() }
            if (result is Success) {
                _collectionsBottomSheet.value = result
            } else {
                val error = (result as Fail).throwable
                _collectionsBottomSheet.value = Fail(error)
            }
        }
    }
}