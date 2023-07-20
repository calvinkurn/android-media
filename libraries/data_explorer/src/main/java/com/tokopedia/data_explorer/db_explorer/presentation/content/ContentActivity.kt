package com.tokopedia.data_explorer.db_explorer.presentation.content

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.di.DaggerDataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.di.DataExplorerComponent
import com.tokopedia.data_explorer.db_explorer.presentation.Constants
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.clearImage
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
        dataExplorerHeader.apply {
            isShowBackButton = true
            title = "Table"
            headerSubTitle = schemaName
            addRightIcon(0).apply {
                clearImage()
                setImageDrawable(getIconUnifyDrawable(context, IconUnify.DELETE, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)))
                setOnClickListener {
                    clearContents()
                }
            }
        }
        toolbar = dataExplorerHeader
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
    }

    private fun clearContents() {
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle("Clear Table")
            setDescription("Do you want to clear contents of this table")
            setPrimaryCTAText("Yes! Just Do It")
            setPrimaryCTAClickListener { fragment?.let {
                if (it is ContentFragment) it.clearContents()
                dismiss()
            } }
            setSecondaryCTAText("No! Please don't")
            setSecondaryCTAClickListener { dismiss() }
            show()
        }

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