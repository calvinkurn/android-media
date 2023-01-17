package com.tokopedia.createpost.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.createpost.createpost.databinding.ActivityProductTagBinding
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
class ProductTagActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var productTagAnalytic: ContentProductTagAnalytic

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

                    override fun onFinishProductTag(products: List<SelectedProductUiModel>) {
                        val product = products.firstOrNull()

                        if(product == null) {
                            finish()
                            return
                        }

                        val data = Intent().apply {
                            putExtra(RESULT_PRODUCT_ID, product.id)
                            putExtra(RESULT_PRODUCT_NAME, product.name)
                            putExtra(RESULT_PRODUCT_PRICE, if(product.isDiscount) product.priceDiscount else product.price)
                            putExtra(RESULT_PRODUCT_IMAGE, product.cover)
                            putExtra(RESULT_PRODUCT_PRICE_ORIGINAL_FMT, product.priceOriginal)
                            putExtra(RESULT_PRODUCT_PRICE_DISCOUNT_FMT, product.discount)
                            putExtra(RESULT_PRODUCT_IS_DISCOUNT, product.isDiscount)
                        }

                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }

                    override fun onMaxSelectedProductReached() {
                        /** No implementation */
                    }
                })

                fragment.setAnalytic(productTagAnalytic)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ProductTagParentFragment.findFragment(supportFragmentManager)?.onNewIntent(intent)
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
        return ProductTagParentFragment.getFragment(
            supportFragmentManager,
            classLoader,
            ContentProductTagArgument.Builder()
                .setShopBadge(shopBadge)
                .setAuthorId(authorId)
                .setAuthorType(authorType)
                .setProductTagSource(productTagList)
                .setMultipleSelectionProduct(false, 0)
                .setFullPageAutocomplete(true, ApplinkConst.FEED_CREATION_PRODUCT_SEARCH)
                .setBackButton(ContentProductTagConfig.BackButton.Back)
                .setIsShowActionBarDivider(true)
                .setIsAutoHandleBackPressed(true)
        )
    }

    companion object {
        private const val EXTRA_PRODUCT_TAG_LIST = "product_tag_source"
        private const val EXTRA_SHOP_BADGE = "shop_badge"
        private const val EXTRA_AUTHOR_ID = "author_id"
        private const val EXTRA_AUTHOR_TYPE = "author_type"

        const val RESULT_PRODUCT_ID = "RESULT_PRODUCT_ID"
        const val RESULT_PRODUCT_NAME = "RESULT_PRODUCT_NAME"
        const val RESULT_PRODUCT_PRICE = "RESULT_PRODUCT_PRICE"
        const val RESULT_PRODUCT_IMAGE = "RESULT_PRODUCT_IMAGE"
        const val RESULT_PRODUCT_PRICE_ORIGINAL_FMT = "RESULT_PRODUCT_PRICE_ORIGINAL_FMT"
        const val RESULT_PRODUCT_PRICE_DISCOUNT_FMT = "RESULT_PRODUCT_PRICE_DISCOUNT_FMT"
        const val RESULT_PRODUCT_IS_DISCOUNT = "RESULT_PRODUCT_IS_DISCOUNT"
    }
}