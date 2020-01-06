package com.tokopedia.basemvvm

import com.tokopedia.basemvvm.viewmodel.BaseViewModel

interface BaseActivityFragmentInterface<T : BaseViewModel> {

    fun getViewModelType(): Class<T>

    fun setViewModel(viewModel: BaseViewModel)

    fun initInject() {}

    fun initView() {}

}