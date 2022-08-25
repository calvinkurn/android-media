package com.tokopedia.content.common.sample

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.common.databinding.ActivityContentProductTagSampleBinding
import com.tokopedia.content.common.di.DaggerContentProductTagSampleComponent
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 23, 2022
 */
class ContentProductTagSampleActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var binding: ActivityContentProductTagSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        binding = ActivityContentProductTagSampleBinding.inflate(
            layoutInflater, null, false
        )
        setContentView(binding.root)

        setupDefault()
        setupListener()
    }

    private fun setupDefault() {
        binding.rbFeed.isChecked = true
        binding.rbUser.isChecked = true
    }

    private fun setupListener() {
        binding.btnOpen.setOnClickListener {
            setupFragment()
        }
    }

    private fun setupFragment() {
        val fragment = getFragment()

        if(fragment == null) {
            Toast.makeText(this, "Fragment is not found.", Toast.LENGTH_SHORT).show()
            return
        }

        supportFragmentManager.beginTransaction()
            .replace(
                binding.fragmentContainer.id,
                fragment,
                ProductTagParentFragment.TAG
            )
            .commit()
    }

    private fun getFragment(): Fragment? {
        return when(binding.rgSource.checkedRadioButtonId) {
            binding.rbFeed.id -> {
                ProductTagParentFragment.getFragmentWithFeedSource(
                    supportFragmentManager,
                    classLoader,
                    "global_search,own_shop,last_purchase",
                    "",
                    getAuthorId(),
                    getAuthorType(),
                )
            }
            binding.rbPlay.id -> {
                ProductTagParentFragment.getFragmentWithPlaySource(
                    supportFragmentManager,
                    classLoader,
                    "global_search,own_shop,last_purchase",
                    "",
                    getAuthorId(),
                    getAuthorType(),
                )
            }
            else -> null
        }
    }

    private fun getAuthorId(): String {
        return when(binding.rgOpenAs.checkedRadioButtonId) {
            binding.rbUser.id -> userSession.userId
            binding.rbSeller.id -> userSession.shopId
            else -> ""
        }
    }

    private fun getAuthorType(): String {
        return when(binding.rgOpenAs.checkedRadioButtonId) {
            binding.rbUser.id -> "content-user"
            binding.rbSeller.id -> "content-shop"
            else -> ""
        }
    }

    private fun inject() {
        DaggerContentProductTagSampleComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}