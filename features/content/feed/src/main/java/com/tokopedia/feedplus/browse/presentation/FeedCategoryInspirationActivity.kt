package com.tokopedia.feedplus.browse.presentation

import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.commit
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.feedplus.browse.di.DaggerFeedBrowseComponent
import com.tokopedia.feedplus.databinding.ActivityFragmentOnlyBinding
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 20/09/23
 */
class FeedCategoryInspirationActivity : BaseActivity() {

    @Inject lateinit var fragmentFactory: FragmentFactory

    private lateinit var binding: ActivityFragmentOnlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        binding = ActivityFragmentOnlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun inject() {
        DaggerFeedBrowseComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupView() {
        supportFragmentManager.commit {
            replace(
                binding.root.id,
                FeedCategoryInspirationFragment::class.java,
                null,
                CATEGORY_INSPIRATION_TAG
            )
        }
    }

    companion object {
        private const val CATEGORY_INSPIRATION_TAG = "cat_inspiration"
    }
}
