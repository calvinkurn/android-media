package com.tokopedia.shop_nib.presentation.submission_success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.shop_nib.databinding.SsnFragmentNibSubmissionSuccessBinding
import com.tokopedia.shop_nib.di.component.DaggerShopNibComponent
import com.tokopedia.shop_nib.util.tracker.NibSubmissionSuccessPageTracker
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class NibSubmissionSuccessFragment : BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): NibSubmissionSuccessFragment {
            return NibSubmissionSuccessFragment()
        }
        private const val IMAGE_URL_SUBMISSION_SUCCESS = "https://images.tokopedia.net/img/android/campaign/shop-nib/submission_success.png"
    }


    private var binding by autoClearedNullable<SsnFragmentNibSubmissionSuccessBinding>()

    @Inject
    lateinit var tracker: NibSubmissionSuccessPageTracker

    override fun getScreenName(): String = NibSubmissionSuccessFragment::class.java.canonicalName.orEmpty()

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
        binding = SsnFragmentNibSubmissionSuccessBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        tracker.sendPageImpression()
        applyUnifyBackgroundColor()
    }

    private fun setupView() {
        binding?.emptyState?.setImageUrl(IMAGE_URL_SUBMISSION_SUCCESS)
        binding?.emptyState?.setPrimaryCTAClickListener { activity?.finish() }
    }


}
