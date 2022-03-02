package com.tokopedia.developer_options.applink.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.applink.di.component.AppLinkComponent
import com.tokopedia.developer_options.applink.di.component.DaggerAppLinkComponent
import com.tokopedia.developer_options.applink.presentation.fragment.AppLinkListFragment

class AppLinkListActivity: AppCompatActivity(), HasComponent<AppLinkComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applink_list)
        setupToolbar()
        inflateFragment(savedInstanceState)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.applink_list)
        toolbar.subtitle = getString(R.string.tokopedia_name)
        setSupportActionBar(toolbar)
    }

    private fun inflateFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, AppLinkListFragment.newInstance(), AppLinkListFragment.TAG)
                .commit()
        }
    }

    override fun getComponent(): AppLinkComponent {
        return DaggerAppLinkComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, AppLinkListActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}