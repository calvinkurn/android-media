package com.tokopedia.scp_rewards.detail.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.scp_rewards.databinding.FragmentMedalBonusBottomSheetBinding
import com.tokopedia.scp_rewards.detail.domain.model.TabData
import com.tokopedia.scp_rewards_widgets.constants.CouponStatus
import com.tokopedia.scp_rewards_widgets.model.FilterModel
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.setCounter
import com.tokopedia.scp_rewards.R as scp_rewardsR

class MedalBonusBottomSheet : BottomSheetUnify(), CouponListFragment.OnCouponListCallBack {

    private var binding: FragmentMedalBonusBottomSheetBinding? = null
    private var listOfTabs: List<TabData>? = null
    private var filtersList: List<FilterModel>? = null
    private var activeTabCouponStatus: String? = null

    @SuppressLint("DeprecatedMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTabs()
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
        binding?.apply {
            listOfTabs?.let { safeList ->
                safeList.forEach { tabs.addNewTab(it.title).setCounter(it.list?.size.orZero()) }
                viewPager.adapter =
                    BonusPagerAdapter(
                        safeList,
                        filtersList,
                        childFragmentManager,
                        this@MedalBonusBottomSheet
                    )
                tabs.setupWithViewPager(viewPager)
            }
            when (activeTabCouponStatus) {
                CouponStatus.ACTIVE -> setTitle(getString(scp_rewardsR.string.title_medali_bonus))
                CouponStatus.INACTIVE -> {
                    setTitle(getString(scp_rewardsR.string.title_medali_bonus_inactive))
                    tabs.hide()
                }
                CouponStatus.EMPTY -> {
                    setTitle(getString(scp_rewardsR.string.title_medali_bonus))
                    goToTab(CouponStatus.EXPIRED)
                }
            }
        }
    }

    private fun setupTabs() {
        val couponList = arguments?.getParcelableArrayList<MedalBenefitModel?>(COUPON_LIST)
        filtersList = arguments?.getParcelableArrayList<FilterModel?>(FILTER_LIST)
        val medaliSlug = arguments?.getString(MEDALI_SLUG).orEmpty()

        activeTabCouponStatus = couponList?.firstOrNull()?.status
        listOfTabs = when (activeTabCouponStatus) {
            CouponStatus.INACTIVE -> {
                listOf(
                    TabData(
                        title = "",
                        status = CouponStatus.INACTIVE,
                        list = couponList,
                        medaliSlug = medaliSlug
                    )
                )
            }
            CouponStatus.EMPTY -> {
                listOf(
                    TabData(
                        title = getString(scp_rewardsR.string.title_active),
                        status = CouponStatus.EMPTY,
                        list = couponList,
                        medaliSlug = medaliSlug
                    ),
                    TabData(
                        title = getString(scp_rewardsR.string.title_history),
                        status = CouponStatus.EXPIRED,
                        list = null,
                        medaliSlug = medaliSlug
                    )
                )
            }
            CouponStatus.ACTIVE -> {
                listOf(
                    TabData(
                        title = getString(scp_rewardsR.string.title_active),
                        status = CouponStatus.ACTIVE,
                        list = couponList,
                        medaliSlug = medaliSlug
                    ),
                    TabData(
                        title = getString(scp_rewardsR.string.title_history),
                        status = CouponStatus.EXPIRED,
                        list = null,
                        medaliSlug = medaliSlug
                    )
                )
            }
            else -> {
                dismiss()
                null
            }
        }
    }

    companion object {

        private const val TAG = "SCP_MEDAL_BONUS_BOTTOM_SHEET"
        private const val MEDALI_SLUG = "medaliSlug"
        private const val COUPON_LIST = "couponList"
        private const val FILTER_LIST = "filterList"

        fun show(
            fragmentManager: FragmentManager,
            medaliSlug: String,
            list: List<MedalBenefitModel>?,
            filterList: List<FilterModel>?
        ) {
            val bundle = Bundle().apply {
                putString(MEDALI_SLUG, medaliSlug)
                putParcelableArrayList(COUPON_LIST, list as ArrayList<MedalBenefitModel>?)
                putParcelableArrayList(FILTER_LIST, filterList as ArrayList<FilterModel>)
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

    override fun goToTab(couponStatus: String) {
        val position = listOfTabs?.indexOfFirst { it.status == couponStatus }.orZero()
        binding?.tabs?.tabLayout?.getTabAt(position)?.select()
    }
}
