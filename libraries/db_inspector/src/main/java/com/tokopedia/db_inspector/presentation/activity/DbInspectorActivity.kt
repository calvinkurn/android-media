package com.tokopedia.db_inspector.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class DbInspectorActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment() = null
    override fun getScreenName() = null

    companion object {
        fun getIntent(context: Context) = Intent(context, DbInspectorActivity::class.java)
    }

}