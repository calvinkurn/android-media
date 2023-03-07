package com.tokopedia.seller.menu.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.seller.active.common.worker.UpdateShopActiveWorker
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.databinding.FragmentSellerSettingsBinding
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuAdapter
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuAdapterTypeFactory
import com.tokopedia.seller.menu.presentation.util.SellerSettingsList
import com.tokopedia.seller.menu.presentation.viewmodel.SellerMenuViewModel
import com.tokopedia.seller.menu.presentation.viewmodel.SellerSettingViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SellerSettingsFragment : Fragment(), SettingTrackingListener {

    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: SellerSettingViewModel

    private var binding by autoClearedNullable<FragmentSellerSettingsBinding>()

    private val sellerMenuAdapter by lazy {
        SellerMenuAdapter(
            SellerMenuAdapterTypeFactory(
                this,
                sellerMenuTracker = sellerMenuTracker,
                userSession = userSession
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellerSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSettingsList()
        viewModel.getShopLocEligible(userSession.shopId.toLong())
        observe(viewModel.shopLocEligible, ::setupLocationSettings)
        startShopActiveService()
    }

    override fun sendImpressionDataIris(settingShopInfoImpressionTrackable: SettingShopInfoImpressionTrackable) {}

    private fun initInjector() {
        DaggerSellerMenuComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupSettingsList() {
        context?.let { context ->
            val settingsList = SellerSettingsList.create(context)

            binding?.listSettings?.run {
                this.adapter = sellerMenuAdapter
                layoutManager = LinearLayoutManager(context)
            }

            sellerMenuAdapter.addElement(settingsList)
            sellerMenuAdapter.notifyDataSetChanged()
        }
    }

    private fun setupLocationSettings(isEligibleMultiloc: Result<Boolean>) {
        when (isEligibleMultiloc) {
            is Success -> {
                val settingsList = context?.let { SellerSettingsList.create(it,isEligibleMultiloc.data) }
                sellerMenuAdapter.clearAllElements()
                sellerMenuAdapter.addElement(settingsList)
                sellerMenuAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun startShopActiveService() {
        context?.let {
            UpdateShopActiveWorker.execute(it)
        }
    }

}