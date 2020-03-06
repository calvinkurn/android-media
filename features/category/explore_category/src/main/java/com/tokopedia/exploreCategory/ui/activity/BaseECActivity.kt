package com.tokopedia.exploreCategory.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.exploreCategory.di.DaggerECComponent
import com.tokopedia.exploreCategory.di.ECComponent
import com.tokopedia.exploreCategory.viewmodel.BaseECViewModel

abstract class BaseECActivity<T : BaseECViewModel> : BaseViewModelActivity<T>() {
    private lateinit var baseECViewModel: T

    abstract override fun getViewModelType(): Class<T>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        baseECViewModel = ViewModelProviders.of(this, getVMFactory()).get(getViewModelType())
    }

    fun getComponent(): ECComponent =
            DaggerECComponent
                    .builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
}