package com.tokopedia.product.detail.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.component.ToasterError
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.installment.InstallmentBank
import com.tokopedia.product.detail.data.util.InstallmentTypeDef
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.ProductInstallmentAdapter
import com.tokopedia.product.detail.view.fragment.ProductInstallmentFragment
import com.tokopedia.product.detail.view.viewmodel.ProductInstallmentViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ProductInstallmentActivity: BaseTabActivity(), HasComponent<ProductDetailComponent> {
    private val installmentsType = intArrayOf(InstallmentTypeDef.MONTH_3,
            InstallmentTypeDef.MONTH_6, InstallmentTypeDef.MONTH_12)

    lateinit var adapter: ProductInstallmentAdapter
    private val data: MutableMap<Int, List<InstallmentBank>> = mutableMapOf(
            InstallmentTypeDef.MONTH_3 to listOf(),
            InstallmentTypeDef.MONTH_6 to listOf(),
            InstallmentTypeDef.MONTH_12 to listOf()
    )

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ProductInstallmentViewModel

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
        .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    companion object {
        private const val IS_OS_SHOP = "is_os_shop"
        private const val PRODUCT_PRICE = "product_price"

        fun createIntent(context: Context, isOsShop: Boolean, productPrice: Float): Intent =
                Intent(context, ProductInstallmentActivity::class.java)
                        .putExtra(IS_OS_SHOP, isOsShop)
                        .putExtra(PRODUCT_PRICE, productPrice)
    }

    override fun getViewPagerAdapter(): PagerAdapter {
        if (!::adapter.isInitialized) adapter = ProductInstallmentAdapter(supportFragmentManager,
                intent.getBooleanExtra(IS_OS_SHOP, false))
        return adapter
    }

    override fun getPageLimit(): Int = 2

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                updateFragmentPager(position)
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_close_default))

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductInstallmentViewModel::class.java)
        viewModel.transformedInstallment.observe(this, onInstallmentChange)
        viewModel.loadInstallment(intent.getFloatExtra(PRODUCT_PRICE, 0f))
    }

    private val onInstallmentChange = Observer<Result<Map<Int, List<InstallmentBank>>>>{
        when(it){
            is Success -> {
                it.data.keys.forEach { key -> data[key] = it.data[key] ?: listOf() }
                viewPager.post { updateFragmentPager(viewPager.currentItem) }
            }
            is Fail -> {
                ToasterError.make(findViewById(R.id.coordinator), ErrorHandler.getErrorMessage(this, it.throwable))
                        .setAction(R.string.retry_label){
                            viewModel.loadInstallment(intent.getFloatExtra(PRODUCT_PRICE, 0f))
                        }
            }
        }
    }

    private fun updateFragmentPager(currentItem: Int) {
        val fragment = adapter.registeredFragment[currentItem]
        val type = installmentsType[currentItem]
        fragment?.let { f ->
            if (f is ProductInstallmentFragment) f.updateInstallment(data[type] ?: listOf(),
                    type)
        }
    }

}