package com.tokopedia.feedplus.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 23/02/23
 */
class FeedPostViewModel @Inject constructor(
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io)
