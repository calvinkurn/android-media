package com.tokopedia.sellerorder.list.presentation.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.fragment.SomFilterFragment

/**
 * Created by fwidjaja on 2019-09-10.
 */
class SomFilterActivity: BaseSimpleActivity(), HasComponent<SomListComponent> {
    override fun getLayoutRes(): Int = R.layout.activity_filter

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        toolbar = findViewById<View>(R.id.toolbar_filter) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.title_filter)
        }
    }

    override fun getNewFragment(): Fragment? {
        return SomFilterFragment.newInstance()
    }

    override fun inflateFragment() {
        val newFragment = newFragment ?: return
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view, newFragment, tagFragment)
                .commit()
    }

    override fun getComponent(): SomListComponent =
        DaggerSomListComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()
}