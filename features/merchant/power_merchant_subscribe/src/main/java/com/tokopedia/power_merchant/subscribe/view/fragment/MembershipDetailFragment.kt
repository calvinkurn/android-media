package com.tokopedia.power_merchant.subscribe.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.power_merchant.subscribe.databinding.FragmentMembershipDetailBinding
import com.tokopedia.power_merchant.subscribe.di.PowerMerchantSubscribeComponent
import com.tokopedia.power_merchant.subscribe.view.adapter.GradeBenefitAdapter
import com.tokopedia.power_merchant.subscribe.view.model.MembershipDataUiModel

/**
 * Created by @ilhamsuaib on 23/05/22.
 */

class MembershipDetailFragment : BaseDaggerFragment() {

    companion object {
        private const val ARG_DATA = "arg_data"

        fun createInstance(data: MembershipDataUiModel): MembershipDetailFragment {
            return MembershipDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DATA, data)
                }
            }
        }
    }

    private var binding: FragmentMembershipDetailBinding? = null
    private var data: MembershipDataUiModel? = null

    override fun getScreenName(): String = String.EMPTY

    override fun initInjector() {
        getComponent(PowerMerchantSubscribeComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMembershipDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        setupView()
        setupBenefitList()
    }

    private fun setupBenefitList() {
        binding?.run {
            data?.let {
                val benefitAdapter = GradeBenefitAdapter(it.benefitList)
                rvPmGradeBenefit.layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean = false
                }
                rvPmGradeBenefit.adapter = benefitAdapter
            }
        }
    }

    private fun initData() {
        this.data = arguments?.getParcelable(ARG_DATA)
    }

    private fun setupView() {
        binding?.run {

        }
    }
}