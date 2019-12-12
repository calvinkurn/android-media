package com.tokopedia.topchat.chatsearch.usecase

import javax.inject.Inject

class GetSearchQueryUseCase @Inject constructor() {

    var isSearching: Boolean = false

    fun cancelRunningSearch() {

    }

    fun doSearch(
            onSuccess: () -> Unit,
            onError: (Throwable) -> Unit
    ) {

    }
}