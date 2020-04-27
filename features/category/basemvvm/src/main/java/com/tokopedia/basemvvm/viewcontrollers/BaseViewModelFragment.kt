package com.tokopedia.basemvvm.viewcontrollers

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.basemvvm.BaseActivityFragmentInterface
import com.tokopedia.basemvvm.viewmodel.BaseLifeCycleObserver
import com.tokopedia.basemvvm.viewmodel.BaseViewModel

abstract class BaseViewModelFragment< T : BaseViewModel>: TkpdBaseV4Fragment(),BaseActivityFragmentInterface<T> {

    private lateinit var bVM: T

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        setViewModel()
        initView()
    }

    private fun setViewModel() {
        bVM = ViewModelProviders.of(this).get(getViewModelType())
        setViewModel(bVM)
        lifecycle.addObserver(getLifeCycleObserver(bVM))
    }


    private fun getLifeCycleObserver(viewModel: BaseViewModel): BaseLifeCycleObserver {
        return BaseLifeCycleObserver(viewModel)
    }
}