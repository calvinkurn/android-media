package com.tokopedia.wishlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.WishlistV2Response
import com.tokopedia.wishlist.domain.WishlistV2UseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 15/10/21.
 */
class WishlistV2ViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                             private val wishlistV2V2UseCase: WishlistV2UseCase) : BaseViewModel(dispatcher.main) {

    private val _wishlistV2Result = MutableLiveData<Result<WishlistV2Response.Data.WishlistV2>>()
    val wishlistV2Result: LiveData<Result<WishlistV2Response.Data.WishlistV2>>
        get() = _wishlistV2Result

    fun loadWishlistV2(params: WishlistV2Params) {
        launch {
            _wishlistV2Result.value = wishlistV2V2UseCase.executeSuspend(params)
        }
    }
}