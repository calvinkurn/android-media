package com.tokopedia.sellerhome.settings.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.seller.menu.common.constant.SellerMenuFreeShippingUrl
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.BottomSheetSettingsFreeShippingBinding
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.di.module.SellerHomeModule
import com.tokopedia.sellerhome.settings.analytics.SettingFreeShippingTracker
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SettingsFreeShippingBottomSheet : BottomSheetUnify() {

    companion object {
        private val TAG = SettingsFreeShippingBottomSheet::class.java.canonicalName

        fun createInstance(): SettingsFreeShippingBottomSheet {
            return SettingsFreeShippingBottomSheet()
        }
    }

    @Inject
    lateinit var freeShippingTracker: SettingFreeShippingTracker

    private var binding by autoClearedNullable<BottomSheetSettingsFreeShippingBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.settings_free_shipping_title))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetSettingsFreeShippingBinding.inflate(inflater)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnFreeShippingDetail?.setOnClickListener {
            freeShippingTracker.trackFreeShippingDetailClick()
            RouteManager.route(
                context, ApplinkConstInternalGlobal.WEBVIEW,
                SellerMenuFreeShippingUrl.URL_FREE_SHIPPING_INTERIM_PAGE
            )
            dismiss()
        }
    }

    fun show(fm: FragmentManager?) {
        fm?.let { show(it, TAG) }
    }

    private fun initInjector() {
        DaggerSellerHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .sellerHomeModule(SellerHomeModule(requireContext()))
            .build()
            .inject(this)
    }
}