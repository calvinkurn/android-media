package com.tokopedia.stories.widget.settings.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.commit
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.content.common.util.Router
import com.tokopedia.header.HeaderUnify
import com.tokopedia.stories.widget.R
import com.tokopedia.stories.widget.settings.di.DaggerStoriesSettingsComponent
import com.tokopedia.stories.widget.settings.tracking.StoriesSettingsTracking
import javax.inject.Inject

class StoriesSettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var analytics: StoriesSettingsTracking

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()

        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories_settings)

        val backHeader : HeaderUnify = findViewById(R.id.header)
        backHeader.setNavigationOnClickListener {
            onBackPressed()
        }

        supportFragmentManager.commit {
            replace(
                R.id.container,
                StoriesSettingsFragment.getOrCreateFragment(supportFragmentManager, classLoader),
                StoriesSettingsFragment.TAG
            )
        }
    }

    private fun inject() {
        DaggerStoriesSettingsComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}
