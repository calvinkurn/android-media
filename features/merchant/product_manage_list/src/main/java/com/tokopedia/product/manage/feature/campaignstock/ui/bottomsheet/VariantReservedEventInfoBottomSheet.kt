package com.tokopedia.product.manage.feature.campaignstock.ui.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetProductManageVariantOngoingPromotionBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.adapter.VariantReservedEventInfoAdapter
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.VariantReservedEventInfoWrapper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class VariantReservedEventInfoBottomSheet : BottomSheetUnify() {

    private var variantReservedEventInfoUiModels: List<ReservedEventInfoUiModel>? = null
    private var variantName: String? = null

    private var binding by autoClearedNullable<BottomSheetProductManageVariantOngoingPromotionBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setVariantInfoFromArguments()
        } else {
            restoreSavedVariantInfo(savedInstanceState)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clearContentPadding = true
        setTitle(context?.getString(R.string.product_manage_campaign_stock_on_variant).orEmpty())

        binding = BottomSheetProductManageVariantOngoingPromotionBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VARIANT_RESERVED_NAME_KEY, variantName.orEmpty())
        outState.putParcelableArrayList(
            VARIANT_RESERVED_EVENT_INFO_KEY,
            ArrayList(variantReservedEventInfoUiModels.orEmpty())
        )
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun setVariantInfoFromArguments() {
        val cacheId = arguments?.getString(VARIANT_RESERVED_EVENT_INFO_CACHE_ID)
        variantReservedEventInfoUiModels = context?.let {
            SaveInstanceCacheManager(it, cacheId)
        }?.let { cacheManager ->
            cacheManager.get<VariantReservedEventInfoWrapper>(
                VARIANT_RESERVED_EVENT_INFO_KEY,
                VariantReservedEventInfoWrapper::class.java
            )?.reservedEventInfoUiModels
        }
        variantName = arguments?.getString(VARIANT_RESERVED_NAME_KEY)
    }

    private fun restoreSavedVariantInfo(savedInstanceState: Bundle) {
        variantReservedEventInfoUiModels =
            savedInstanceState.getParcelableArrayList(VARIANT_RESERVED_EVENT_INFO_KEY)
        variantName = savedInstanceState.getString(VARIANT_RESERVED_NAME_KEY)
    }

    private fun setupView() {
        binding?.tvVariantOngoingPromotionName?.text = variantName.orEmpty()
        binding?.rvVariantOngoingPromotion?.run {
            layoutManager = LinearLayoutManager(this@VariantReservedEventInfoBottomSheet.context)
            adapter = VariantReservedEventInfoAdapter(variantReservedEventInfoUiModels.orEmpty())
        }
    }

    companion object {
        private const val TAG = "VariantReservedEventInfoBottomSheet"

        private const val VARIANT_RESERVED_NAME_KEY = "variant_reserved_key"
        private const val VARIANT_RESERVED_EVENT_INFO_KEY = "variant_reserved_event_info_key"
        private const val VARIANT_RESERVED_EVENT_INFO_CACHE_ID =
            "variant_reserved_event_info_cache_id"

        fun createInstance(
            context: Context,
            variantName: String,
            reservedEventInfoUiModels: ArrayList<ReservedEventInfoUiModel>
        ): VariantReservedEventInfoBottomSheet {
            return VariantReservedEventInfoBottomSheet().apply {
                val cacheManager = SaveInstanceCacheManager(context, true).apply {
                    put(
                        VARIANT_RESERVED_EVENT_INFO_KEY,
                        VariantReservedEventInfoWrapper(reservedEventInfoUiModels)
                    )
                }
                arguments = Bundle().apply {
                    putString(VARIANT_RESERVED_NAME_KEY, variantName)
                    putString(VARIANT_RESERVED_EVENT_INFO_CACHE_ID, cacheManager.id)
                }
            }
        }
    }
}