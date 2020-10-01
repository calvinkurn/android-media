package com.tokopedia.developer_options.drawonpicture.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.developer_options.drawonpicture.DispatcherProvider
import javax.inject.Inject

/**
 * @author by furqan on 01/10/2020
 */
class DrawOnPictureViewModel @Inject constructor(dispatcherProvider: DispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {
}