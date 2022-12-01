package com.tokopedia.mvc.presentation.creation.step2

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentVoucherCreationStepTwoBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherCodeSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherNameSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherPeriodSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherTargetSectionBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class VoucherCreationStepTwoFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance() = VoucherCreationStepTwoFragment()
    }

    // binding
    private var binding by autoClearedNullable<SmvcFragmentVoucherCreationStepTwoBinding>()
    private var voucherTargetSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherTargetSectionBinding>()
    private var voucherNameSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherNameSectionBinding>()
    private var voucherCodeSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherCodeSectionBinding>()
    private var voucherPeriodSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherPeriodSectionBinding>()

    //viewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherCreationStepTwoViewModel::class.java) }

    override fun getScreenName(): String =
        VoucherCreationStepTwoFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentVoucherCreationStepTwoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
