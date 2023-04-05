package com.tokopedia.shop_nib.presentation.submitted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.shop_nib.databinding.SsnFragmentNibAlreadySubmittedBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class NibAlreadySubmittedFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(): NibAlreadySubmittedFragment {
            return NibAlreadySubmittedFragment()
        }
        private const val URL_TOKOPEDIA_CARE_SELLER = "https://www.tokopedia.com/help/seller"
        private const val IMAGE_URL_ALREADY_SUBMITTED = "https://images.tokopedia.net/img/android/campaign/shop-nib/already_submitted.png"
    }

    private var binding by autoClearedNullable<SsnFragmentNibAlreadySubmittedBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsnFragmentNibAlreadySubmittedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding?.run {
            emptyState.setImageUrl(IMAGE_URL_ALREADY_SUBMITTED)
            emptyState.setPrimaryCTAClickListener {
                RouteManager.route(activity ?: return@setPrimaryCTAClickListener, ApplinkConst.SellerApp.SELLER_APP_HOME)
            }
            emptyState.setSecondaryCTAClickListener {
                routeToUrl(URL_TOKOPEDIA_CARE_SELLER)
            }
        }
    }


}
