package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.ProductViewPagerAdapter
import com.tokopedia.product.detail.view.fragment.ProductFullDescriptionFragment
import com.tokopedia.product.detail.view.fragment.ProductSpecificationFragment
import com.tokopedia.product.detail.view.util.doSuccessOrFail
import com.tokopedia.product.detail.view.viewmodel.ProductFullDescriptionViewModel
import kotlinx.android.synthetic.main.activity_full_desc_tab.*
import javax.inject.Inject

class ProductFullDescriptionTabActivity : BaseSimpleActivity(), HasComponent<ProductDetailComponent> {

    @Inject
    lateinit var productDetailTracking: ProductDetailTracking

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var descriptionData: DescriptionData? = null
    private var catalogId: String = ""

    private var titleList = mutableListOf<String>()
    private val productViewPagerAdapter: ProductViewPagerAdapter by lazy {
        ProductViewPagerAdapter(supportFragmentManager, titleList, descriptionData
                ?: DescriptionData())
    }
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ProductFullDescriptionViewModel::class.java)
    }

    companion object {
        private const val PARAM_DESCRIPTION_DATA = "description_data"
        private const val PARAM_CATALOD_ID = "catalog_id"

        fun createIntent(context: Context, descriptionData: DescriptionData, catalogId: String): Intent {
            return Intent(context, ProductFullDescriptionTabActivity::class.java).apply {
                putExtra(PARAM_DESCRIPTION_DATA, descriptionData)
                putExtra(PARAM_CATALOD_ID, catalogId)
            }
        }
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_full_desc_tab

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        intent?.run {
            descriptionData = getParcelableExtra(PARAM_DESCRIPTION_DATA)
            catalogId = getStringExtra(PARAM_CATALOD_ID) ?: ""
        }

        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_default))
        titleList.add(getString(R.string.label_desc))
        viewPagerDesc?.adapter = productViewPagerAdapter

        tabLayoutDesc?.run {
            setupWithViewPager(viewPagerDesc)
        }

        tabLayoutDesc?.onTabSelected {
            val position = it.position
            when (productViewPagerAdapter.getItem(position)) {
                is ProductFullDescriptionFragment -> {
                    productDetailTracking.eventClickDescriptionTabOnProductDescription(
                            descriptionData?.basicId ?: ""
                    )
                }
                is ProductSpecificationFragment -> {
                    productDetailTracking.eventClickSpecificationTabOnProductDescription(
                            descriptionData?.basicId ?: ""
                    )
                }
                else -> {
                }
            }
        }

        getSpecificationData()
        observeLiveData()
    }

    private fun getSpecificationData() {
        if (catalogId.isNotEmpty() && catalogId.toIntOrZero() != 0) {
            viewModel.setCatalogId(catalogId)
        }
    }

    private fun observeLiveData() {
        observe(viewModel.specificationResponseData) {
            it.doSuccessOrFail({
                if (titleList.size == 2) return@doSuccessOrFail
                tabLayoutDesc?.show()
                titleList.add(getString(R.string.label_spec))
                productViewPagerAdapter.setSpecificationData(it.data.productCatalogQuery.data.catalog.specification)
                productViewPagerAdapter.notifyDataSetChanged()
            }) {
                tabLayoutDesc?.hide()
            }
        }
    }

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
}