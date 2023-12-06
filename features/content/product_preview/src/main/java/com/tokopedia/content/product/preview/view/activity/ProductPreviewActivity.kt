package com.tokopedia.content.product.preview.view.activity

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.content.product.preview.databinding.ActivityProductPreviewBinding
import com.tokopedia.content.product.preview.di.ProductPreviewInjector
import com.tokopedia.content.product.preview.view.fragment.ProductPreviewFragment
import javax.inject.Inject
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductPreviewActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private var bundle: Bundle? = null

    private var _binding: ActivityProductPreviewBinding? = null
    private val binding: ActivityProductPreviewBinding
        get() = _binding!!

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
    }

    private fun setupViews() {
        _binding = ActivityProductPreviewBinding.inflate(layoutInflater)
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
                    ProductPreviewFragment.TAG,
                )
            }.commit()
        }
    }

    private fun getMediaPreviewFragment(): Fragment {
        return ProductPreviewFragment.getOrCreate(
            fragmentManager = supportFragmentManager,
            classLoader = classLoader,
            bundle = bundle ?: Bundle(),
        )
    }

}
