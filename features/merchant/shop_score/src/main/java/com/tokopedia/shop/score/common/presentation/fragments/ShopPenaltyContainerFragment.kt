package com.tokopedia.shop.score.common.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.FragmentShopPenaltyContainerBinding
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyDetailFragment
import com.tokopedia.shop.score.penalty.presentation.fragment.tablet.ShopPenaltyListTabletFragment
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ShopPenaltyContainerFragment : TkpdBaseV4Fragment(),
    ShopPenaltyListTabletFragment.PenaltyListListener {

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
        attachFragments()
    }

    override fun onFragmentBackPressed(): Boolean {
        var handled = false
        shopPenaltyListTabletFragment?.let { handled = it.onFragmentBackPressed() }
        shopPenaltyDetailFragment?.let { handled = handled || it.onFragmentBackPressed() }
        return handled
    }

    override fun onItemPenaltyClicked(penaltyFilterUiModel: ItemPenaltyUiModel) {
        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(
                ShopPenaltyDetailFragment.KEY_ITEM_PENALTY_DETAIL,
                penaltyFilterUiModel
            )
            attachPenaltyDetailFragment(cacheManager.id.orEmpty())
        }
    }

    override fun closePenaltyDetail() {
        binding?.run {
            fragmentPenaltyDetail.gone()
            ivPenaltyDetailWelcomeIllustration.show()
            tvPenaltyDetailWelcome.show()
        }
    }

    private fun attachFragments() {
        initiateListFragment()
        attachPenaltyListFragment()
        showPenaltyDetail()
    }

    private fun initiateListFragment() {
        if (!isAdded) return
        shopPenaltyListTabletFragment = shopPenaltyListTabletFragment
            ?: childFragmentManager.findFragmentByTag(
                ShopPenaltyListTabletFragment::class.java.simpleName
            ) as? ShopPenaltyListTabletFragment ?: ShopPenaltyListTabletFragment()
        shopPenaltyListTabletFragment?.apply {
            setPenaltyListListener(this@ShopPenaltyContainerFragment)
        }
    }

    private fun initiateDetailFragment(
        keyCacheManagerId: String
    ): ShopPenaltyDetailFragment {
        val penaltyDetailFragment = this.shopPenaltyDetailFragment
            ?: childFragmentManager.findFragmentByTag(
                ShopPenaltyDetailFragment::class.java.simpleName
            ) as? ShopPenaltyDetailFragment
            ?: ShopPenaltyDetailFragment.newInstance(keyCacheManagerId)
        this.shopPenaltyDetailFragment = penaltyDetailFragment
        return penaltyDetailFragment
    }

    private fun attachPenaltyListFragment() {
        if (!isAdded) return
        shopPenaltyListTabletFragment?.let {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentPenaltyList, it, it::class.java.simpleName)
                .commitAllowingStateLoss()
        }
    }

    private fun setupViews() {
        binding?.ivPenaltyDetailWelcomeIllustration?.loadImage(URL_WELCOME_ILLUSTRATION)
    }

    private fun attachPenaltyDetailFragment(keyCacheManagerId: String) {
        binding?.run {
            if (shopPenaltyDetailFragment == null) {
                if (!isAdded) return
                val detailFragment = initiateDetailFragment(keyCacheManagerId)
                childFragmentManager.beginTransaction().replace(
                        R.id.fragmentPenaltyDetail,
                        detailFragment,
                        detailFragment::class.java.simpleName
                    ).commitAllowingStateLoss()
            }
            showPenaltyDetail()
        }
    }

    private fun showPenaltyDetail() {
        binding?.run {
            fragmentPenaltyDetail.show()
            ivPenaltyDetailWelcomeIllustration.gone()
            tvPenaltyDetailWelcome.gone()
        }
    }

    companion object {
        private const val URL_WELCOME_ILLUSTRATION =
            "https://images.tokopedia.net/img/android/sellerorder/ic_som_welcome_page_illustration.png"

        fun newInstance(): ShopPenaltyContainerFragment {
            return ShopPenaltyContainerFragment()
        }
    }
}