package com.tokopedia.stories.widget.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.commit
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.stories.widget.R
import javax.inject.Inject

class StoriesSettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var factory: StoriesSettingsFactory.Creator

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()

        supportFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return when (className) {
                    StoriesSettingsFragment::class.java.name -> StoriesSettingsFragment(
                        factory,
                    )

                    else -> super.instantiate(classLoader, className)
                }
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stories_settings)

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
