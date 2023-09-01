package com.tokopedia.scp_rewards.detail.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.scp_rewards.databinding.FragmentMedalBonusBottomSheetBinding
import com.tokopedia.scp_rewards.detail.di.DaggerMedalDetailComponent
import com.tokopedia.unifycomponents.BottomSheetUnify

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
        binding = FragmentMedalBonusBottomSheetBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("Medali Bonus")
        binding?.apply {
            viewPager.adapter = BonusPagerAdapter()
            tabs.setupWithViewPager(viewPager)
        }
    }

    companion object {

        private const val TAG = "SCP_MEDAL_BONUS_BOTTOM_SHEET"
        private const val MEDALI_SLUG = "medaliSlug"

        fun show(
            childFragmentManager: FragmentManager,
            medaliSlug: String
        ) {
            val bundle = Bundle()
            bundle.putString(MEDALI_SLUG, medaliSlug)
            val medalBonusBottomSheet = MedalBonusBottomSheet().apply {
                arguments = bundle
            }
            medalBonusBottomSheet.show(childFragmentManager, TAG)
        }
    }
}
