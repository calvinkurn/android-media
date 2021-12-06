package com.tokopedia.shop.score.common.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
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
        showFirstStatePenaltyDetail()
    }

    private fun attachFragments() {
        initiateListFragment()
        attachPenaltyListFragment()
        showFirstStatePenaltyDetail()
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
        val penaltyDetailFragment = ShopPenaltyDetailFragment.newInstance(keyCacheManagerId)
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
        binding?.ivPenaltyDetailWelcomeIllustration?.loadImage(
            ShopScoreConstant.EMPTY_STATE_PENALTY_URL
        )
    }

    private fun attachPenaltyDetailFragment(keyCacheManagerId: String) {
        binding?.run {
            val detailFragment = initiateDetailFragment(keyCacheManagerId)
            childFragmentManager.beginTransaction().replace(
                R.id.fragmentPenaltyDetail,
                detailFragment,
                detailFragment::class.java.simpleName)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commitAllowingStateLoss()
            showPenaltyDetail()
        }
    }

    private fun showFirstStatePenaltyDetail() {
        binding?.run {
            context?.let {
                fragmentPenaltyDetail.setBackgroundColor(
                    ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN50)
                )
            }
            fragmentPenaltyDetail.hide()
            ivPenaltyDetailWelcomeIllustration.show()
            tvPenaltyDetailWelcome.show()
        }
    }

    private fun showPenaltyDetail() {
        binding?.run {
            context?.let {
                fragmentPenaltyDetail.setBackgroundColor(
                    ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background)
                )
            }
            fragmentPenaltyDetail.show()
            ivPenaltyDetailWelcomeIllustration.gone()
            tvPenaltyDetailWelcome.gone()
        }
    }

    companion object {
        fun newInstance(): ShopPenaltyContainerFragment {
            return ShopPenaltyContainerFragment()
        }
    }
}