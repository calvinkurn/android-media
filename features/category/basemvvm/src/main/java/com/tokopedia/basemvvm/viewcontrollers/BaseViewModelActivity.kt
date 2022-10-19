package com.tokopedia.basemvvm.viewcontrollers

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.basemvvm.BaseActivityFragmentInterface
import com.tokopedia.basemvvm.viewmodel.BaseLifeCycleObserver
import com.tokopedia.basemvvm.viewmodel.BaseViewModel

abstract class BaseViewModelActivity<T : BaseViewModel> : BaseSimpleActivity(), BaseActivityFragmentInterface<T> {

    private lateinit var bVM: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        setViewModel()
        initView()
    }

    private fun setViewModel() {
        getVMFactory()?.let {
            bVM = ViewModelProvider(this, it).get(getViewModelType())
            setViewModel(bVM)
            lifecycle.addObserver(getLifeCycleObserver(bVM))
        }
    }

    protected open fun getVMFactory(): ViewModelProvider.Factory? {
        return ViewModelProvider.AndroidViewModelFactory(this.application)
    }


    private fun getLifeCycleObserver(viewModel: BaseViewModel): BaseLifeCycleObserver {
        return BaseLifeCycleObserver(viewModel)
    }

}
