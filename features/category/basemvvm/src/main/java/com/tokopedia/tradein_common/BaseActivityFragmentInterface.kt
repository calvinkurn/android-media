package com.tokopedia.tradein_common

import com.tokopedia.tradein_common.viewmodel.BaseViewModel

interface BaseActivityFragmentInterface<T : BaseViewModel> {

    fun getViewModelType(): Class<T>

    fun setViewModel(viewModel: BaseViewModel)

    fun initInject() {}

    fun initView() {}

}