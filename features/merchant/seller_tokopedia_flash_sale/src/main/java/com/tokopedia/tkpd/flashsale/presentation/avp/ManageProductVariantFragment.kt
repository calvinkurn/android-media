package com.tokopedia.tkpd.flashsale.presentation.avp

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.base.BaseCampaignManageProductDetailFragment
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentManageProductVariantBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.detail.CampaignDetailFragment
import com.tokopedia.tkpd.flashsale.presentation.detail.CampaignDetailViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ManageProductVariantFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance() = ManageProductVariantFragment()
    }

    //viewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ManageProductVariantViewModel::class.java) }

    override fun getScreenName(): String =
        ManageProductVariantFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

//    override fun onBackArrowClicked() {
//        TODO("Not yet implemented")
//    }
//
//    override fun getHeaderUnifyTitle(): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun onSubmitButtonClicked() {
//        TODO("Not yet implemented")
//    }
}