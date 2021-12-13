package com.tokopedia.data_explorer.db_explorer.presentation.content

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.di.DaggerDataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.di.DataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.presentation.Constants
import kotlinx.android.synthetic.main.activity_database_list.*

class ContentActivity : BaseSimpleActivity(), HasComponent<DataExplorerComponent> {
    private val dataExplorerComponent: DataExplorerComponent by lazy(LazyThreadSafetyMode.NONE) { initInjector() }
    private val databaseName: String by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra(Constants.Keys.DATABASE_NAME).orEmpty()
    }

    private val databasePath: String by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra(Constants.Keys.DATABASE_PATH).orEmpty()
    }

    private val schemaName: String by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra(Constants.Keys.SCHEMA_NAME).orEmpty()
    }

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
        dataExplorerHeader.title = "Table"
        dataExplorerHeader.headerSubTitle = schemaName
    }

    override fun getNewFragment() =
        ContentFragment.newInstance(
            schemaName,
            databaseName,
            databasePath
        )
    override fun getScreenName() = null
    override fun getComponent() = dataExplorerComponent
    override fun getLayoutRes() = R.layout.activity_database_list
    override fun getParentViewResourceID(): Int = R.id.dbInspectorFrame


}