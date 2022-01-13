package com.tokopedia.vouchercreation.product.create.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.vouchercreation.databinding.FragmentProductCouponPreviewBinding


class ProductCouponPreviewFragment : Fragment() {

    private var binding: FragmentProductCouponPreviewBinding by autoCleared()
    private var onNavigateToCouponInformationPage: () -> Unit = {}
    private var onNavigateToCouponSettingsPage: () -> Unit = {}
    private var onNavigateToProductListPage: () -> Unit = {}

    companion object {
        fun newInstance(): ProductCouponPreviewFragment {
            val args = Bundle()
            val fragment = ProductCouponPreviewFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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