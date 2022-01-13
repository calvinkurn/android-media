package com.tokopedia.vouchercreation.product.create.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.FragmentCouponSettingBinding
import com.tokopedia.vouchercreation.databinding.FragmentProductCouponPreviewBinding


class CouponSettingFragment : Fragment() {

    private var binding : FragmentCouponSettingBinding by autoCleared()

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

    companion object {
        fun newInstance():  CouponSettingFragment {
            val args = Bundle()
            val fragment = CouponSettingFragment()
            fragment.arguments = args
            return fragment
        }
    }

    fun setOnCouponSettingDataCompleted() {

    }
}