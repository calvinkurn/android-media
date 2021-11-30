package com.tokopedia.data_explorer.presentation.databases

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.di.DaggerDbInspectorComponent
import com.tokopedia.data_explorer.di.DbInspectorComponent
import kotlinx.android.synthetic.main.activity_database_list.*

class DbInspectorActivity : BaseSimpleActivity(), HasComponent<DbInspectorComponent> {

    private val dbInspectorComponent: DbInspectorComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    private fun initInjector() =
        DaggerDbInspectorComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    private fun setUpToolBar() {
        dbInspectorHeader.isShowBackButton = true
        toolbar = dbInspectorHeader
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        dbInspectorHeader.title = "Db Inspector"
    }

    override fun getNewFragment() = DatabaseListFragment.newInstance()
    override fun getScreenName() = null
    override fun getComponent() = dbInspectorComponent
    override fun getLayoutRes() = R.layout.activity_database_list
    override fun getParentViewResourceID(): Int = R.id.dbInspectorFrame

}