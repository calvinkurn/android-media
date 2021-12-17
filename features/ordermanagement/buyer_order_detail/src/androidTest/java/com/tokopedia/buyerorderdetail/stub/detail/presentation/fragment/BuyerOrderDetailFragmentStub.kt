package com.tokopedia.buyerorderdetail.stub.detail.presentation.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactoryStub
import com.tokopedia.kotlin.extensions.view.gone

class BuyerOrderDetailFragmentStub : BuyerOrderDetailFragment() {
    override val typeFactory: BuyerOrderDetailTypeFactory
        get() = BuyerOrderDetailTypeFactoryStub(
            productBundlingViewListener = this,
            tickerViewHolderListener = this,
            digitalRecommendationData = digitalRecommendationData,
            digitalRecommendationListener = this,
            courierInfoViewHolderListener = this,
            productViewListener = this,
            navigator = navigator,
            buyerOrderDetailBindRecomWidgetListener = this
        )

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