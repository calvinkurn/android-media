package com.tokopedia.data_explorer.db_explorer.presentation

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.data_explorer.R
import kotlinx.android.synthetic.main.activity_database_list.*

class DataExplorerActivity : BaseSimpleActivity() {

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
        dataExplorerHeader.title = "Data Explorer"
    }

    override fun getNewFragment() = DataExplorerFragment.newInstance()
    override fun getScreenName() = null
    override fun getLayoutRes() = R.layout.activity_database_list
    override fun getParentViewResourceID(): Int = R.id.dbInspectorFrame

}