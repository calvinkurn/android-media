package com.tokopedia.autocompletecomponent.universal

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.universal.presentation.fragment.UniversalSearchFragment
import com.tokopedia.autocompletecomponent.universal.presentation.fragment.UniversalSearchFragment.Companion.UNIVERSAL_SEARCH_FRAGMENT_TAG

open class UniversalSearchActivity : BaseActivity(), HasComponent<BaseAppComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.universal_search_activity_layout)

        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.universal_search_container,
                UniversalSearchFragment.newInstance(null),
                UNIVERSAL_SEARCH_FRAGMENT_TAG
            )
            .commit()
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }
}