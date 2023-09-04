package com.tokopedia.scp_rewards.detail.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.scp_rewards.databinding.FragmentMedalBonusBottomSheetBinding
import com.tokopedia.scp_rewards.detail.domain.model.TabData
import com.tokopedia.scp_rewards_widgets.constants.CouponState
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.setCounter
import com.tokopedia.scp_rewards.R as scp_rewardsR

class MedalBonusBottomSheet : BottomSheetUnify(), CouponListFragment.OnCouponListCallBack {

    private var binding: FragmentMedalBonusBottomSheetBinding? = null
    private var listOfTabs: List<TabData>? = null

    @SuppressLint("DeprecatedMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val couponList = arguments?.getParcelableArrayList<MedalBenefitModel?>(COUPON_LIST)
        val medaliSlug = arguments?.getString(MEDALI_SLUG).orEmpty()

        listOfTabs = listOf(
            TabData(
                title = getString(scp_rewardsR.string.title_active),
                status = CouponState.ACTIVE,
                list = couponList,
                medaliSlug = medaliSlug
            ),
            TabData(
                title = getString(scp_rewardsR.string.title_history),
                status = CouponState.INACTIVE,
                list = null,
                medaliSlug = medaliSlug
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        clearContentPadding = true
        isFullpage = true
        binding = FragmentMedalBonusBottomSheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(scp_rewardsR.string.title_medali_bonus))
        binding?.apply {
            listOfTabs?.let { safeList ->
                safeList.forEach { tabs.addNewTab(it.title).setCounter(it.list?.size.orZero()) }
                viewPager.adapter =
                    BonusPagerAdapter(safeList, childFragmentManager, this@MedalBonusBottomSheet)
                tabs.setupWithViewPager(viewPager)
            }
        }
    }

    companion object {

        private const val TAG = "SCP_MEDAL_BONUS_BOTTOM_SHEET"
        private const val MEDALI_SLUG = "medaliSlug"
        private const val COUPON_LIST = "couponList"

        fun show(
            fragmentManager: FragmentManager,
            medaliSlug: String,
            list: List<MedalBenefitModel>?
        ) {
            val bundle = Bundle().apply {
                putString(MEDALI_SLUG, medaliSlug)
                putParcelableArrayList(COUPON_LIST, list as ArrayList<MedalBenefitModel>)
            }
            val medalBonusBottomSheet = MedalBonusBottomSheet().apply {
                arguments = bundle
            }
            medalBonusBottomSheet.show(fragmentManager, TAG)
        }
    }

    override fun onReceiveCoupons(couponStatus: String, count: Int) {
        val position = listOfTabs?.indexOfFirst { it.status == couponStatus }.orZero()
        binding?.tabs?.tabLayout?.getTabAt(position)?.setCounter(count)
    }
}
