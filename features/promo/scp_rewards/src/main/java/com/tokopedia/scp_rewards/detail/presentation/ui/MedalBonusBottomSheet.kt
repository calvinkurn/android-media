package com.tokopedia.scp_rewards.detail.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.scp_rewards.databinding.FragmentMedalBonusBottomSheetBinding
import com.tokopedia.scp_rewards.detail.di.DaggerMedalDetailComponent
import com.tokopedia.scp_rewards.detail.domain.model.CouponList
import com.tokopedia.scp_rewards_widgets.constants.CouponState
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.Locale
import com.tokopedia.scp_rewards.R as scp_rewardsR

class MedalBonusBottomSheet : BottomSheetUnify() {

    private var binding: FragmentMedalBonusBottomSheetBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerMedalDetailComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent).build().inject(this)
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

    @SuppressLint("DeprecatedMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(scp_rewardsR.string.title_medali_bonus))
        binding?.apply {
            val couponList = arguments?.getParcelableArrayList<MedalBenefitModel?>(COUPON_LIST)

            val list = listOf(
                    CouponList(
                            title = String.format(Locale.getDefault(), getString(scp_rewardsR.string.title_active), couponList?.size.orZero()),
                            status = CouponState.ACTIVE,
                            list = couponList,
                    ),
                    CouponList(
                            title = getString(scp_rewardsR.string.title_history),
                            status = CouponState.INACTIVE,
                            list = null))

            list.forEach { tabs.addNewTab(it.title) }
            viewPager.adapter = BonusPagerAdapter(list, childFragmentManager)
            tabs.setupWithViewPager(viewPager)
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
}
