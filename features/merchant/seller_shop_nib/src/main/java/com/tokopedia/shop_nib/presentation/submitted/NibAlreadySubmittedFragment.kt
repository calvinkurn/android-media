package com.tokopedia.shop_nib.presentation.submitted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.shop_nib.databinding.SsnFragmentNibAlreadySubmittedBinding
import com.tokopedia.shop_nib.di.component.DaggerShopNibComponent
import com.tokopedia.shop_nib.util.constant.UrlConstant
import com.tokopedia.shop_nib.util.tracker.NibAlreadySubmittedPageTracker
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class NibAlreadySubmittedFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): NibAlreadySubmittedFragment {
            return NibAlreadySubmittedFragment()
        }

        private const val IMAGE_URL_ALREADY_SUBMITTED =
            "https://images.tokopedia.net/img/android/campaign/shop-nib/already_submitted.png"
    }

    private var binding by autoClearedNullable<SsnFragmentNibAlreadySubmittedBinding>()

    @Inject
    lateinit var tracker: NibAlreadySubmittedPageTracker

    override fun getScreenName(): String = NibAlreadySubmittedFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopNibComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

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
        applyUnifyBackgroundColor()
        tracker.sendPageImpression()
    }

    private fun setupView() {
        binding?.run {
            emptyState.setImageUrl(IMAGE_URL_ALREADY_SUBMITTED)
            emptyState.setPrimaryCTAClickListener { activity?.finish() }
            emptyState.setSecondaryCTAClickListener {
                routeToUrl(UrlConstant.URL_TOKOPEDIA_CARE_SELLER)
            }
        }
    }


}
