package com.tokopedia.buy_more_get_more.presentation.olp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.buy_more_get_more.databinding.FragmentOfferLandingPageBinding
import com.tokopedia.buy_more_get_more.di.component.DaggerBuyMoreGetMoreComponent
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.utils.lifecycle.autoClearedNullable

class OfferLandingPageFragment:
    BaseDaggerFragment(),
    HasPaginatedList by HasPaginatedListImpl() {

    private var binding by autoClearedNullable<FragmentOfferLandingPageBinding>()

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerBuyMoreGetMoreComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfferLandingPageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
