package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomSheetMerchantCampaignTncBinding
import com.tokopedia.shop.flashsale.common.extension.setNumberedText
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC.TncRequest
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class MerchantCampaignTNCBottomSheet : BottomSheetUnify() {

    companion object {
        private const val KEY_SHOW_TICKER_AND_BUTTON = "KEY_SHOW_TICKER_AND_BUTTON"
        private const val KEY_TNC_REQUEST = "KEY_TNC_REQUEST"
        private const val TAG = "MerchantCampaignTNCBottomSheet"

        @JvmStatic
        fun createInstance(
            showTickerAndButton: Boolean,
            tncRequest: TncRequest
        ): MerchantCampaignTNCBottomSheet =
            MerchantCampaignTNCBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_TNC_REQUEST, tncRequest)
                    putBoolean(KEY_SHOW_TICKER_AND_BUTTON, showTickerAndButton)
                }
            }

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(MerchantCampaignTNCViewModel::class.java) }

    private var binding by autoClearedNullable<SsfsBottomSheetMerchantCampaignTncBinding>()

    private var tncRequest: TncRequest = TncRequest()
    private var showTickerAndButton: Boolean? = true

    private var listener: ConfirmationClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsBottomSheetMerchantCampaignTncBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        tncRequest.let {
            viewModel.getMerchantCampaignTNC(
                it.campaignId,
                it.isUniqueBuyer,
                it.isCampaignRelation,
                it.paymentType
            )
        }
        setupObserver()
    }

    private fun setupDependencyInjection() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView() {
        showCloseIcon = true
        if (arguments != null) {
            tncRequest = arguments?.getParcelable(KEY_TNC_REQUEST) ?: TncRequest()
            showTickerAndButton = arguments?.getBoolean(KEY_SHOW_TICKER_AND_BUTTON)
        }
    }

    @SuppressLint("ResourcePackage")
    private fun populateData(data: MerchantCampaignTNC) {
        setTitle(getString(R.string.tnc_title_bottom_sheet))
        binding?.run {
            tgTncContent.setNumberedText(
                data.messages
            )
            when (showTickerAndButton) {
                true -> {
                    tickerTnc.visible()
                    btnAgree.visible()
                }
                false -> {
                    tickerTnc.gone()
                    btnAgree.gone()
                }
            }
            btnAgree.setOnClickListener {
                listener?.onTNCConfirmationClicked()
                dismiss()
            }
        }
    }

    private fun setupObserver() {
        viewModel.merchantCampaignTNC.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    populateData(result.data)
                }

                is Fail -> {

                }
            }
        }
    }

    fun setConfirmationClickListener(listener: ConfirmationClickListener) {
        this.listener = listener
    }

    interface ConfirmationClickListener {
        fun onTNCConfirmationClicked()
    }
}