package com.tokopedia.vouchercreation.product.create.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.vouchercreation.databinding.FragmentCouponSettingBinding


class CouponSettingFragment : Fragment() {

    private var binding : FragmentCouponSettingBinding by autoCleared()

    companion object {
        fun newInstance():  CouponSettingFragment {
            val args = Bundle()
            val fragment = CouponSettingFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCouponSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.chipCashback.chip_container.setOnClickListener {
            binding.chipCashback.chipType = ChipsUnify.TYPE_SELECTED
        }

        binding.chipCashback.selectedChangeListener = { isActive ->

        }


        binding.chipFreeShipping.selectedChangeListener = { isActive ->

        }

    }


    fun setOnCouponSettingDataCompleted() {

    }
}