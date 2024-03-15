package com.tokopedia.content.product.preview.view.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.product.preview.data.mapper.ProductPreviewSourceMapper
import com.tokopedia.content.product.preview.databinding.ActivityProductPreviewBinding
import com.tokopedia.content.product.preview.di.ProductPreviewInjector
import com.tokopedia.content.product.preview.utils.PRODUCT_PREVIEW_FRAGMENT_TAG
import com.tokopedia.content.product.preview.utils.PRODUCT_PREVIEW_SOURCE
import com.tokopedia.content.product.preview.view.fragment.ProductPreviewFragment
import com.tokopedia.content.product.preview.viewmodel.utils.ProductPreviewSourceModel
import javax.inject.Inject
import com.tokopedia.content.product.preview.R as contentproductpreviewR

@Suppress("LateinitUsage")
class ProductPreviewActivity @Inject constructor() : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private var _binding: ActivityProductPreviewBinding? = null
    private val binding: ActivityProductPreviewBinding
        get() = _binding!!

    private val productPreviewBundle: Bundle?
        get() {
            val productId = intent.data?.pathSegments?.first()

            val bundle = if (productId.isNullOrBlank()) {
                intent.extras
            } else {
                ProductPreviewSourceMapper(productId).mapSourceAppLink(intent)
            }

            if (bundle == null) finish()
            return bundle
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        initFragmentFactory()
        super.onCreate(savedInstanceState)
        setupViews()
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(contentproductpreviewR.style.ProductPreview_Theme, true)
        return theme
    }

    private fun inject() {
        ProductPreviewInjector.get(this).inject(this)
    }

    private fun initFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupViews() {
        _binding = ActivityProductPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openFragment()
    }

    private fun openFragment() {
        supportFragmentManager.apply {
            executePendingTransactions()
            val existingFragment = findFragmentByTag(PRODUCT_PREVIEW_FRAGMENT_TAG)
            if (existingFragment is ProductPreviewFragment && existingFragment.isVisible) return
            beginTransaction().apply {
                replace(
                    binding.fragmentContainer.id,
                    getMediaPreviewFragment(),
                    PRODUCT_PREVIEW_FRAGMENT_TAG
                )
            }.commit()
        }
    }

    private fun getMediaPreviewFragment(): Fragment {
        return ProductPreviewFragment.getOrCreate(
            fragmentManager = supportFragmentManager,
            classLoader = classLoader,
            bundle = productPreviewBundle
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun createIntent(
            context: Context,
            productPreviewSourceModel: ProductPreviewSourceModel
        ): Intent {
            val intent = Intent(context, ProductPreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(PRODUCT_PREVIEW_SOURCE, productPreviewSourceModel)
            intent.putExtras(bundle)
            return intent
        }
    }
}
