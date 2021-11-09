package com.tokopedia.db_inspector.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.db_inspector.di.DaggerDbInspectorComponent
import com.tokopedia.db_inspector.di.DbInspectorComponent

class DbInspectorActivity : BaseSimpleActivity(), HasComponent<DbInspectorComponent> {

    private val dbInspectorComponent: DbInspectorComponent by lazy { initInjector() }

    private fun initInjector() =
        DaggerDbInspectorComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getNewFragment() = DatabaseListFragment.newInstance()
    override fun getScreenName() = null
    override fun getComponent() = dbInspectorComponent

    companion object {
        fun getIntent(context: Context) = Intent(context, DbInspectorActivity::class.java)
    }


}