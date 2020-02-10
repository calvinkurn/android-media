package com.tokopedia.sellerorder.list.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LIST_ORDER
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.sellerorder.list.di.DaggerSomListComponent
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.fragment.SomFilterFragment
import kotlinx.android.synthetic.main.partial_toolbar_reset_button.*

/**
 * Created by fwidjaja on 2019-09-10.
 */
class SomFilterActivity: BaseSimpleActivity(), HasComponent<SomListComponent> {
    override fun getLayoutRes(): Int = R.layout.activity_filter

    companion object {
        @JvmStatic
        fun createIntent(context: Context, currentFilterParams: SomListOrderParam): Intent =
                Intent(context, SomFilterActivity::class.java)
                        .putExtra(PARAM_LIST_ORDER, currentFilterParams)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        setSupportActionBar(toolbar_filter)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.title_filter)
        }

        label_reset.setOnClickListener {
            (fragment as SomFilterFragment).onResetClicked()
        }
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras
        } else {
            bundle.putString(PARAM_LIST_ORDER, "")
        }
        return SomFilterFragment.newInstance(bundle)
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

    override fun onBackPressed() {
        super.onBackPressed()
        SomAnalytics.eventClickBackButtonOnFilterPage()
    }
}