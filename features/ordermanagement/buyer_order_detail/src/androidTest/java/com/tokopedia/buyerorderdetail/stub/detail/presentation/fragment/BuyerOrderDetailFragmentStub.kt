package com.tokopedia.buyerorderdetail.stub.detail.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.BuyerOrderDetailAdapterStub
import com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactoryStub
import com.tokopedia.kotlin.extensions.view.gone

class BuyerOrderDetailFragmentStub : BuyerOrderDetailFragment() {
    override val typeFactory: BuyerOrderDetailTypeFactory by lazy {
        BuyerOrderDetailTypeFactoryStub(
            productBundlingViewListener = this,
            tickerViewHolderListener = this,
            digitalRecommendationData = digitalRecommendationData,
            digitalRecommendationListener = this,
            courierInfoViewHolderListener = this,
            productListToggleListener = this,
            scpRewardsMedalTouchPointWidgetListener = this,
            pofRefundInfoListener = this,
            owocInfoListener = this,
            productViewListener = this,
            bottomSheetListener = this,
            navigator = navigator,
            buyerOrderDetailBindRecomWidgetListener = this,
            orderResolutionListener = this
        )
    }

    override val adapter: BuyerOrderDetailAdapter by lazy {
        BuyerOrderDetailAdapterStub(typeFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderBuyerOrderDetail?.gone()
    }

    companion object {
        @JvmStatic
        fun newInstance(extras: Bundle): BuyerOrderDetailFragmentStub {
            return BuyerOrderDetailFragmentStub().apply {
                arguments = extras
            }
        }
    }
}
