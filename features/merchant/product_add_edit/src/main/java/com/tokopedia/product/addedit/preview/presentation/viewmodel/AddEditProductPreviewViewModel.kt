package com.tokopedia.product.addedit.preview.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddEditProductPreviewViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher
) : BaseViewModel(coroutineDispatcher)