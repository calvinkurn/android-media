package com.tokopedia.shop.score.common.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.FragmentShopPenaltyContainerBinding
import com.tokopedia.shop.score.databinding.FragmentShopPerformanceBinding
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyDetailFragment
import com.tokopedia.shop.score.penalty.presentation.fragment.tablet.ShopPenaltyListTabletFragment
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ShopPenaltyContainerFragment : TkpdBaseV4Fragment() {

    private var binding by autoClearedNullable<FragmentShopPenaltyContainerBinding>()

    private var shopPenaltyListTabletFragment: ShopPenaltyListTabletFragment? = null
    private var shopPenaltyDetailFragment: ShopPenaltyDetailFragment? = null

    override fun getScreenName(): String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopPenaltyContainerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onFragmentBackPressed(): Boolean {
        var handled = false
        shopPenaltyListTabletFragment?.let { handled = it.onFragmentBackPressed() }
        shopPenaltyDetailFragment?.let { handled = handled || it.onFragmentBackPressed() }
        return handled
    }

    private fun setupViews() {
        binding?.ivPenaltyDetailWelcomeIllustration?.loadImage(URL_WELCOME_ILLUSTRATION)
    }

    companion object {
        private const val URL_WELCOME_ILLUSTRATION =
            "https://images.tokopedia.net/img/android/sellerorder/ic_som_welcome_page_illustration.png"

        fun newInstance(): ShopPenaltyContainerFragment {
            return ShopPenaltyContainerFragment()
        }
    }
}