package com.tokopedia.tokopedianow.buyercomm.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.buyercomm.presentation.bottomsheet.TokoNowBuyerCommunicationBottomSheet
import com.tokopedia.tokopedianow.buyercomm.presentation.data.BuyerCommunicationData

class TokoNowBuyerCommunicationFragment: Fragment() {

    companion object {
        fun newInstance(
            data: BuyerCommunicationData?
        ): TokoNowBuyerCommunicationFragment {
            return TokoNowBuyerCommunicationFragment().apply {
                this.data = data
            }
        }
    }

    private var data: BuyerCommunicationData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tokopedianow_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomSheet()
    }

    private fun showBottomSheet() {
        val bottomSheet = TokoNowBuyerCommunicationBottomSheet.newInstance(data)
        bottomSheet.show(childFragmentManager)
    }
}
