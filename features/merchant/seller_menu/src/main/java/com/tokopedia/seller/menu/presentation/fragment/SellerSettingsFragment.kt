package com.tokopedia.seller.menu.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.analytics.SettingTrackingListener
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.databinding.FragmentSellerSettingsBinding
import com.tokopedia.seller.menu.di.component.DaggerSellerMenuComponent
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuAdapter
import com.tokopedia.seller.menu.presentation.util.SellerSettingsList
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SellerSettingsFragment: Fragment(), SettingTrackingListener {

    @Inject
    lateinit var sellerMenuTracker: SellerMenuTracker
    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentSellerSettingsBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSellerSettingsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSettingsList()
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
            val adapter = SellerMenuAdapter(OtherMenuAdapterTypeFactory(
                this,
                sellerMenuTracker = sellerMenuTracker,
                userSession = userSession
            ))

            binding?.listSettings?.run {
                this.adapter = adapter
                layoutManager = LinearLayoutManager(context)
            }

            adapter.addElement(settingsList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun startShopActiveService() {
        context?.let {
            UpdateShopActiveService.startService(it)
        }
    }

}