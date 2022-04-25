package com.tokopedia.createpost.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.createpost.createpost.databinding.ActivityProductTagBinding
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import kotlinx.android.synthetic.main.search_result_item_toko_view.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class ProductTagActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var binding: ActivityProductTagBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
        binding = ActivityProductTagBinding.inflate(
            LayoutInflater.from(this),
            null,
            false
        )
        setContentView(binding.root)

        val productTagList = intent.getStringExtra(EXTRA_PRODUCT_TAG_LIST) ?: ""
        val shopBadge = intent.getStringExtra(EXTRA_SHOP_BADGE) ?: ""

        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, getParentFragment(productTagList, shopBadge))
            .commit()
    }

    private fun inject() {
        DaggerCreatePostComponent.builder()
            .createPostModule(CreatePostModule(this))
            .createPostCommonModule(CreatePostCommonModule(applicationContext))
            .build()
            .inject(this)
    }

    private fun getParentFragment(
        productTagList: String,
        shopBadge: String,
    ): ProductTagParentFragment {
        return ProductTagParentFragment.getFragment(
            supportFragmentManager,
            classLoader,
            productTagList,
            shopBadge,
            ProductTagParentFragment.SOURCE_FEED
        )
    }

    companion object {
        private const val EXTRA_PRODUCT_TAG_LIST = "product_tag_source"
        private const val EXTRA_SHOP_BADGE = "shop_badge"
    }
}