package com.tokopedia.data_explorer.db_explorer.presentation.databases

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.di.DaggerDataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.di.DataExplorerComponent
import kotlinx.android.synthetic.main.activity_database_list.*

class DbExplorerActivity : BaseSimpleActivity(), HasComponent<DataExplorerComponent> {

    private val dataExplorerComponent: DataExplorerComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }

    private fun initInjector() =
        DaggerDataExplorerComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    private fun setUpToolBar() {
        dataExplorerHeader.isShowBackButton = true
        toolbar = dataExplorerHeader
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        dataExplorerHeader.title = "Database Explorer"
    }

    override fun getNewFragment() = DatabaseListFragment.newInstance()
    override fun getScreenName() = null
    override fun getComponent() = dataExplorerComponent
    override fun getLayoutRes() = R.layout.activity_database_list
    override fun getParentViewResourceID(): Int = R.id.dbInspectorFrame

}