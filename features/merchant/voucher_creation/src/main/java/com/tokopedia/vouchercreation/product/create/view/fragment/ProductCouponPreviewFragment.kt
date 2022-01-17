package com.tokopedia.vouchercreation.product.create.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.databinding.FragmentProductCouponPreviewBinding
import com.tokopedia.vouchercreation.product.create.view.viewmodel.ProductCouponPreviewViewModel
import javax.inject.Inject


class ProductCouponPreviewFragment : BaseDaggerFragment() {

    companion object {
        private const val SCREEN_NAME = "Product coupon preview page"
        fun newInstance(): ProductCouponPreviewFragment {
            val args = Bundle()
            val fragment = ProductCouponPreviewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var binding: FragmentProductCouponPreviewBinding by autoCleared()
    private var onNavigateToCouponInformationPage: () -> Unit = {}
    private var onNavigateToCouponSettingsPage: () -> Unit = {}
    private var onNavigateToProductListPage: () -> Unit = {}

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ProductCouponPreviewViewModel::class.java) }

    override fun getScreenName() = SCREEN_NAME
    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductCouponPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.tpgReadArticle.setOnClickListener { onNavigateToCouponInformationPage() }
        binding.tpgCouponInformation.setOnClickListener { onNavigateToCouponInformationPage() }
        binding.tpgCouponSetting.setOnClickListener { onNavigateToCouponSettingsPage() }
        binding.tpgAddProduct.setOnClickListener { onNavigateToProductListPage() }
    }

    fun setOnNavigateToCouponInformationPageListener(onNavigateToCouponInformationPage: () -> Unit) {
        this.onNavigateToCouponInformationPage = onNavigateToCouponInformationPage
    }

    fun setOnNavigateToCouponSettingsPageListener(onNavigateToCouponSettingsPage: () -> Unit) {
        this.onNavigateToCouponSettingsPage = onNavigateToCouponSettingsPage
    }

    fun setOnNavigateToProductListPageListener(onNavigateToProductListPage: () -> Unit) {
        this.onNavigateToProductListPage = onNavigateToProductListPage
    }
}