package com.tokopedia.topads.edit.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel.EditProductViewModel
import com.tokopedia.topads.edit.view.fragment.edit.EditAdGroupFragment
import javax.inject.Inject

class EditAdGroupActivity : BaseSimpleActivity(), HasComponent<TopAdsEditComponent>, OnProductAction, OnKeywordAction {

    private var addedItems: MutableList<EditProductViewModel>? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_edit_activity_edit_ad_group)
        initInject()
    }

    override fun getNewFragment() = EditAdGroupFragment.newInstance().apply {
        arguments = intent.extras
    }
    private fun initInject() {
        component.inject(this)
    }

    override fun getComponent(): TopAdsEditComponent {
        return DaggerTopAdsEditComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent
        )
            .topAdEditModule(TopAdEditModule(this)).build()
    }


    override fun onAction(items: Bundle) {
        (fragment as EditAdGroupFragment).sendData(items)
    }

    override fun onAction(data: HashMap<String, Any?>) {
        (fragment as EditAdGroupFragment).sendKeywordData(data)
    }

}

interface OnProductAction{
    fun onAction(items: Bundle)
}

interface OnKeywordAction{
    fun onAction(data: HashMap<String, Any?>)
}
