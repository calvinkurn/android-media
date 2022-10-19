package com.tokopedia.tkpd.flashsale.presentation.ineligibleaccess

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.components.bottomsheet.rbac.IneligibleAccessWarningBottomSheet
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentIneligibleAccessBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class IneligibleAccessFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): IneligibleAccessFragment {
            return IneligibleAccessFragment()
        }

        private const val IMAGE_INELIGIBLE_ACCESS_URL =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/FS_tkpd_ineligible_access_illustration.png"
        private const val FEATURE_LEARN_MORE_ARTICLE_URL =
            "https://seller.tokopedia.com/edu/fitur-admin-toko/"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(IneligibleAccessViewModel::class.java) }

    //binding
    private var binding by autoClearedNullable<StfsFragmentIneligibleAccessBinding>()


    override fun getScreenName(): String =
        IneligibleAccessFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentIneligibleAccessBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        showIneligibleAccessBottomSheet()
    }

    private fun setupView() {
        binding?.apply {
            header.setNavigationOnClickListener { activity?.finish() }
            imgIneligibleAccess.setImageUrl(IMAGE_INELIGIBLE_ACCESS_URL)
            btnGoToLearnArticle.setOnClickListener {
                routeToUrl(FEATURE_LEARN_MORE_ARTICLE_URL)
            }
        }
    }

    private fun showIneligibleAccessBottomSheet() {
        val bottomSheet = IneligibleAccessWarningBottomSheet.newInstance()
        bottomSheet.setOnButtonClicked {
            routeToUrl(FEATURE_LEARN_MORE_ARTICLE_URL)
        }
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }
}
