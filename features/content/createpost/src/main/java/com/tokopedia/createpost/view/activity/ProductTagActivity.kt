package com.tokopedia.createpost.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.createpost.createpost.databinding.ActivityProductTagBinding
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.createpost.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.SearchParamUiModel
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
        val authorId = intent.getStringExtra(EXTRA_AUTHOR_ID) ?: ""
        val authorType = intent.getStringExtra(EXTRA_AUTHOR_TYPE) ?: ""

        supportFragmentManager.beginTransaction()
            .replace(
                binding.container.id,
                getParentFragment(productTagList, shopBadge, authorId, authorType,),
                ProductTagParentFragment.TAG
            )
            .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        when(fragment) {
            is ProductTagParentFragment -> {
                fragment.setListener(object : ProductTagParentFragment.Listener {
                    override fun onCloseProductTag() {
                        finish()
                    }
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val path = intent?.data?.path.toString()
        val source = if(path.contains(PRODUCT)) ProductTagSource.GlobalSearch
                    else if(path.contains(SHOP)) ProductTagSource.Shop
                    else ProductTagSource.Unknown

        val query = intent?.extras?.getString(SearchParamUiModel.KEY_QUERY) ?: ""
        val shopId = intent?.data?.lastPathSegment ?: ""
        val componentId = intent?.extras?.getString(SearchParamUiModel.KEY_COMPONENT_ID) ?: ""

        ProductTagParentFragment.findFragment(supportFragmentManager)?.onNewIntent(source, query, shopId, componentId)
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
        authorId: String,
        authorType: String,
    ): ProductTagParentFragment {
        return ProductTagParentFragment.getFragmentWithFeedSource(
            supportFragmentManager,
            classLoader,
            productTagList,
            shopBadge,
            authorId,
            authorType,
        )
    }

    override fun onBackPressed() {
        ProductTagParentFragment.findFragment(supportFragmentManager)?.let {
            it.onBackPressed()
        } ?: super.onBackPressed()
    }

    companion object {
        private const val EXTRA_PRODUCT_TAG_LIST = "product_tag_source"
        private const val EXTRA_SHOP_BADGE = "shop_badge"
        private const val EXTRA_AUTHOR_ID = "author_id"
        private const val EXTRA_AUTHOR_TYPE = "author_type"

        private const val PRODUCT = "product"
        private const val SHOP = "shop"
    }
}