package com.tokopedia.content.product.preview.view.activity

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.product.preview.databinding.ActivityProductPreviewBinding
import com.tokopedia.content.product.preview.di.ProductPreviewInjector
import com.tokopedia.content.product.preview.view.fragment.ProductPreviewFragment
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import javax.inject.Inject
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductPreviewActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private var bundle: Bundle? = null

    private lateinit var binding: ActivityProductPreviewBinding

    private var productPreviewData: ProductContentUiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        initFragmentFactory()
        super.onCreate(savedInstanceState)
        getData()
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

    private fun getData() {
        productPreviewData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(PRODUCT_PREVIEW_DATA, ProductContentUiModel::class.java)
        } else {
            intent.extras?.getParcelable(PRODUCT_PREVIEW_DATA)
        }
    }

    private fun setupViews() {
        binding = ActivityProductPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openFragment()
    }

    private fun openFragment() {
        supportFragmentManager.apply {
            executePendingTransactions()
            val existingFragment = findFragmentByTag(ProductPreviewFragment.TAG)
            if (existingFragment is ProductPreviewFragment && existingFragment.isVisible) return
            beginTransaction().apply {
                replace(
                    binding.fragmentContainer.id,
                    getMediaPreviewFragment(),
                    ProductPreviewFragment.TAG
                )
            }.commit()
        }
    }

    private fun getMediaPreviewFragment(): Fragment {
        return ProductPreviewFragment.getOrCreate(
            fragmentManager = supportFragmentManager,
            classLoader = classLoader,
            bundle = bundle ?: Bundle().apply {
                putParcelable(ProductPreviewFragment.PRODUCT_DATA, productPreviewData)
            }
        )
    }

    companion object {

        private const val PRODUCT_PREVIEW_DATA = "product_preview_data"

        fun createIntent(
            context: Context,
            productContentData: ProductContentUiModel,
        ): Intent {
            val intent = Intent(context, ProductPreviewActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(PRODUCT_PREVIEW_DATA, productContentData)
            intent.putExtras(bundle)
            return intent
        }
    }
}
