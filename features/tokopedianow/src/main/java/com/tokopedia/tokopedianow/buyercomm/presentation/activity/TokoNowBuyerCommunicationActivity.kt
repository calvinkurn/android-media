package com.tokopedia.tokopedianow.buyercomm.presentation.activity

import android.content.Context
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.tokopedianow.buyercomm.presentation.data.BuyerCommunicationData
import com.tokopedia.tokopedianow.buyercomm.presentation.fragment.TokoNowBuyerCommunicationFragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity

class TokoNowBuyerCommunicationActivity: BaseTokoNowActivity() {

    companion object {
        fun startActivity(context: Context, data: BuyerCommunicationData) {
            val intent = RouteManager.getIntent(context,
                ApplinkConstInternalTokopediaNow.BUYER_COMMUNICATION_BOTTOM_SHEET)
            intent.putExtra(EXTRA_BUYER_COMMUNICATION_DATA, data)
            context.startActivity(intent)
        }

        private const val EXTRA_BUYER_COMMUNICATION_DATA = "extra_buyer_communication_data"
    }

    override fun getFragment(): Fragment {
        val data = intent
            .getParcelableExtra<BuyerCommunicationData>(EXTRA_BUYER_COMMUNICATION_DATA)
        return TokoNowBuyerCommunicationFragment.newInstance(data)
    }
}
