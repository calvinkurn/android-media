package com.tokopedia.stories.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.stories.di.StoriesInjector
import com.tokopedia.stories.view.fragment.StoriesFragment
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.factory.StoriesViewModelFactory
import javax.inject.Inject

@Suppress("LateinitUsage")
class StoriesActivity : BaseSimpleActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: StoriesViewModelFactory.Creator

    private val viewModel by viewModels<StoriesViewModel> {
        viewModelFactory.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()

        super.onCreate(savedInstanceState)

        getIntentData()
    }

    private fun inject() {
        StoriesInjector.get(this).inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun getIntentData() {
        val data = intent.data ?: return
        viewModel.saveInitialData(data.pathSegments)
    }

    override fun getNewFragment(): Fragment {
        return StoriesFragment.getFragment(
            supportFragmentManager,
            classLoader,
        )
    }

}
