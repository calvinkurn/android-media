package com.tokopedia.db_inspector.presentation.schema

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.db_inspector.R
import com.tokopedia.db_inspector.di.DaggerDbInspectorComponent
import com.tokopedia.db_inspector.di.DbInspectorComponent
import com.tokopedia.db_inspector.presentation.Constants
import kotlinx.android.synthetic.main.activity_database_list.*

class SchemaActivity : BaseSimpleActivity(), HasComponent<DbInspectorComponent> {

    private val dbInspectorComponent: DbInspectorComponent by lazy { initInjector() }
    private val databaseName: String by lazy {
        intent.getStringExtra(Constants.Keys.DATABASE_NAME).orEmpty()
    }

    private val databasePath: String by lazy {
        intent.getStringExtra(Constants.Keys.DATABASE_PATH).orEmpty()
    }

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
        dbInspectorHeader.title = "Schema"
        dbInspectorHeader.headerSubTitle = databaseName
    }

    override fun getNewFragment() = SchemaFragment.newInstance(databaseName, databasePath)
    override fun getScreenName() = null
    override fun getComponent() = dbInspectorComponent
    override fun getLayoutRes() = R.layout.activity_database_list
    override fun getParentViewResourceID(): Int = R.id.dbInspectorFrame

}