package com.tokopedia.createpost.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.createpost.createpost.databinding.ActivityProductTagBinding
import com.tokopedia.createpost.di.CreatePostModule
import com.tokopedia.createpost.di.DaggerCreatePostComponent
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.SearchParamUiModel
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

                    override fun onFinishProductTag(products: List<ProductUiModel>) {
                        val product = products.firstOrNull()

                        if(product == null) {
                            finish()
                            return
                        }

                        val data = Intent().apply {
                            putExtra(RESULT_PRODUCT_ID, product.id)
                            putExtra(RESULT_PRODUCT_NAME, product.name)
                            putExtra(RESULT_PRODUCT_PRICE, if(product.isDiscount) product.priceDiscountFmt else product.priceFmt)
                            putExtra(RESULT_PRODUCT_IMAGE, product.coverURL)
                            putExtra(RESULT_PRODUCT_PRICE_ORIGINAL_FMT, product.priceOriginalFmt)
                            putExtra(RESULT_PRODUCT_PRICE_DISCOUNT_FMT, product.discountFmt)
                            putExtra(RESULT_PRODUCT_IS_DISCOUNT, product.isDiscount)
                        }

                        setResult(Activity.RESULT_OK, data)
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
        return ProductTagParentFragment.getFragment(
            supportFragmentManager,
            classLoader,
            ContentProductTagArgument.Builder()
                .setShopBadge(shopBadge)
                .setAuthorId(authorId)
                .setAuthorType(authorType)
                .setProductTagSource(productTagList)
                .setMultipleSelectionProduct(false)
                .setFullPageAutocomplete(true)
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

        const val RESULT_PRODUCT_ID = "RESULT_PRODUCT_ID"
        const val RESULT_PRODUCT_NAME = "RESULT_PRODUCT_NAME"
        const val RESULT_PRODUCT_PRICE = "RESULT_PRODUCT_PRICE"
        const val RESULT_PRODUCT_IMAGE = "RESULT_PRODUCT_IMAGE"
        const val RESULT_PRODUCT_PRICE_ORIGINAL_FMT = "RESULT_PRODUCT_PRICE_ORIGINAL_FMT"
        const val RESULT_PRODUCT_PRICE_DISCOUNT_FMT = "RESULT_PRODUCT_PRICE_DISCOUNT_FMT"
        const val RESULT_PRODUCT_IS_DISCOUNT = "RESULT_PRODUCT_IS_DISCOUNT"

        private const val PRODUCT = "product"
        private const val SHOP = "shop"
    }
}