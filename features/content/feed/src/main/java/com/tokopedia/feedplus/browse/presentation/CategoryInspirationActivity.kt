package com.tokopedia.feedplus.browse.presentation

import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.commit
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.browse.di.DaggerFeedBrowseComponent
import com.tokopedia.feedplus.databinding.ActivityFragmentOnlyBinding
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by kenny.hadisaputra on 20/09/23
 */
@Suppress("LateinitUsage")
class CategoryInspirationActivity : BaseActivity() {

    @Inject lateinit var fragmentFactory: FragmentFactory

    private lateinit var binding: ActivityFragmentOnlyBinding

    private val sourceType: String
        get() = intent?.data?.getQueryParameter(QUERY_SOURCE_TYPE).orEmpty()

    private val entryPoint: String
        get() = intent?.data?.getQueryParameter(QUERY_ENTRYPOINT).orEmpty()

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
        window.statusBarColor = MethodChecker.getColor(
            this,
            unifyprinciplesR.color.Unify_Background
        )

        if (supportFragmentManager.findFragmentByTag(CATEGORY_INSPIRATION_TAG) == null) {
            supportFragmentManager.commit {
                replace(
                    binding.root.id,
                    CategoryInspirationFragment::class.java,
                    CategoryInspirationFragment.createParams(sourceType),
                    CATEGORY_INSPIRATION_TAG
                )
            }
        }
    }

    companion object {
        private const val QUERY_SOURCE_TYPE = "source_type"
        private const val QUERY_ENTRYPOINT = "entrypoint"

        private const val CATEGORY_INSPIRATION_TAG = "cat_inspiration"
    }
}
