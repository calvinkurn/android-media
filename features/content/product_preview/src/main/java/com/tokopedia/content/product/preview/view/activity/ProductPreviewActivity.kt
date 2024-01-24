package com.tokopedia.content.product.preview.view.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.product.preview.databinding.ActivityProductPreviewBinding
import com.tokopedia.content.product.preview.di.ProductPreviewInjector
import com.tokopedia.content.product.preview.utils.PRODUCT_DATA
import com.tokopedia.content.product.preview.utils.PRODUCT_PREVIEW_FRAGMENT_TAG
import com.tokopedia.content.product.preview.view.fragment.ProductPreviewFragment
import com.tokopedia.content.product.preview.view.uimodel.product.ProductUiModel
import javax.inject.Inject
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductPreviewActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var binding: ActivityProductPreviewBinding

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
        binding = ActivityProductPreviewBinding.inflate(layoutInflater)
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
            bundle = intent.extras
        )
    }

    companion object {
        fun createIntent(
            context: Context,
            productContentData: ProductUiModel
        ): Intent {
            val intent = Intent(context, ProductPreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(PRODUCT_DATA, productContentData)
            intent.putExtras(bundle)
            return intent
        }
    }
}
