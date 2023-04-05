package com.tokopedia.shop_nib.presentation.submission_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.shop_nib.databinding.SsnFragmentNibSubmissionSuccessBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class NibSubmissionSuccessFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(): NibSubmissionSuccessFragment {
            return NibSubmissionSuccessFragment()
        }
        private const val IMAGE_URL_ALREADY_SUBMITTED = "https://images.tokopedia.net/img/android/campaign/shop-nib/already_submitted.png"
        private const val IMAGE_URL_SUBMISSION_SUCCESS = "https://images.tokopedia.net/img/android/campaign/shop-nib/submission_success.png"
    }

    private var binding by autoClearedNullable<SsnFragmentNibSubmissionSuccessBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsnFragmentNibSubmissionSuccessBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding?.emptyState?.setImageUrl(IMAGE_URL_SUBMISSION_SUCCESS)
        binding?.emptyState?.setPrimaryCTAClickListener {
            RouteManager.route(activity ?: return@setPrimaryCTAClickListener, ApplinkConst.SellerApp.SELLER_APP_HOME)
        }
    }


}
