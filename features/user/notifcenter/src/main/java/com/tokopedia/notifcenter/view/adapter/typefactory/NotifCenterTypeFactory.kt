package com.tokopedia.notifcenter.view.adapter.typefactory

import com.tokopedia.notifcenter.view.viewmodel.NotifItemViewModel

/**
 * @author by milhamj on 30/08/18.
 */

interface NotifCenterTypeFactory {
    fun type(viewModel: NotifItemViewModel): Int
}
