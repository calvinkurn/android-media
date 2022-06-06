package com.tokopedia.shop.flash_sale.presentation.creation.campaign_rule.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flash_sale.common.extension.setNumberedText
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flash_sale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flash_sale.domain.entity.MerchantCampaignTNC.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class MerchantCampaignTNCBottomSheet : BottomSheetUnify() {

    companion object {
        private const val KEY_SHOW_TICKER_AND_BUTTON = "KEY_SHOW_TICKER_AND_BUTTON"
        private const val KEY_TNC_REQUEST = "KEY_TNC_REQUEST"

        @JvmStatic
        fun createInstance(
            context: Context,
            showTickerAndButton: Boolean,
            tncRequest: TncRequest
        ): MerchantCampaignTNCBottomSheet =
            MerchantCampaignTNCBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_TNC_REQUEST, tncRequest)
                    putBoolean(KEY_SHOW_TICKER_AND_BUTTON, showTickerAndButton)
                }
                val view = View.inflate(
                    context,
                    R.layout.ssfs_bottom_sheet_merchant_campaign_tnc,
                    null
                )
                setChild(view)
            }

        private const val TAG = "MerchantCampaignTNCBottomSheet"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(MerchantCampaignTNCViewModel::class.java) }

    private var tncRequest: TncRequest = TncRequest()
    private var showTickerAndButton: Boolean? = true

    private var tgTncContent: Typography? = null
    private var ticker: Ticker? = null
    private var btnAgree: UnifyButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        tncRequest.let {
            viewModel.getMerchantCampaignTNC(
                it.campaignId,
                it.isUniqueBuyer,
                it.isCampaignRelation,
                it.paymentProfile
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

    private fun setupView(view: View) {
        showCloseIcon = true
        if (arguments != null) {
            tncRequest = arguments?.getParcelable(KEY_TNC_REQUEST) ?: TncRequest()
            showTickerAndButton = arguments?.getBoolean(KEY_SHOW_TICKER_AND_BUTTON)
        }
        tgTncContent = view.findViewById(R.id.tg_tnc_content)
        ticker = view.findViewById(R.id.ticker_tnc)
        btnAgree = view.findViewById(R.id.btn_agree)

        btnAgree?.setOnClickListener {
            dismiss()
        }
    }

    @SuppressLint("ResourcePackage")
    private fun populateData(data: MerchantCampaignTNC) {
        setTitle(data.title)
        tgTncContent?.setNumberedText(
            data.messages,
            resources.getDimensionPixelSize(R.dimen.dp_8)
        )

        when (showTickerAndButton) {
            true -> {
                ticker?.visible()
                btnAgree?.visible()
            }
            false -> {
                ticker?.gone()
                btnAgree?.gone()
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
}