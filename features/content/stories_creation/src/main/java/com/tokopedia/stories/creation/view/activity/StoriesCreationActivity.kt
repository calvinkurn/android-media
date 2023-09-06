package com.tokopedia.stories.creation.view.activity

import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.stories.creation.databinding.ActivityStoriesCreationBinding
import com.tokopedia.stories.creation.di.DaggerStoriesCreationComponent
import com.tokopedia.stories.creation.di.StoriesCreationModule
import com.tokopedia.stories.creation.view.fragment.StoriesCreationFragment
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
class StoriesCreationActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var binding: ActivityStoriesCreationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setupFragmentFactory()

        super.onCreate(savedInstanceState)

        setupContentView()
        setupFragmentContainer()
    }

    private fun inject() {
        DaggerStoriesCreationComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .storiesCreationModule(StoriesCreationModule(this))
            .build()
            .inject(this)
    }

    private fun setupFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupContentView() {
        binding = ActivityStoriesCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupFragmentContainer() {
        var bundle = intent.extras

        if (intent.data != null) {
            bundle = UriUtil.destructiveUriBundle(
                ApplinkConstInternalContent.INTERNAL_STORIES_CREATION,
                intent.data,
                bundle,
            )
        }

        supportFragmentManager.beginTransaction()
            .replace(
                binding.container.id,
                StoriesCreationFragment.getFragment(
                    supportFragmentManager,
                    classLoader,
                    bundle ?: Bundle()
                )
            )
            .commit()
    }
}
