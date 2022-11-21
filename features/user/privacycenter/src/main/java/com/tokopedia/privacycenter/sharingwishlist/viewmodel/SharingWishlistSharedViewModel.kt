package com.tokopedia.privacycenter.sharingwishlist.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SharingWishlistSharedViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _wishlistCollectionSate = MutableSharedFlow<Int>(extraBufferCapacity = 10, replay = 1)
    val wishlistCollectionState = _wishlistCollectionSate.asSharedFlow()

    fun notifyPager(access: Int) {
        launch(coroutineContext) {
            _wishlistCollectionSate.tryEmit(access)
        }
    }
}
