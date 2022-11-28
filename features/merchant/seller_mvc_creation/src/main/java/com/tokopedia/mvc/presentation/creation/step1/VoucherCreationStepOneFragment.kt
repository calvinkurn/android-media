package com.tokopedia.mvc.presentation.creation.step1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.mvc.R
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.mvc.databinding.SmvcFragmentVoucherCreationStepOneBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class VoucherCreationStepOneFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance() = VoucherCreationStepOneFragment()
    }

    // binding
    private var binding by autoClearedNullable<SmvcFragmentVoucherCreationStepOneBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherCreationStepOneViewModel::class.java) }

    override fun getScreenName(): String =
        VoucherCreationStepOneFragment::class.java.canonicalName.orEmpty()

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
        binding = SmvcFragmentVoucherCreationStepOneBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.run {
            voucherTypeSelectionShop.apply {
                radioButton?.setOnClickListener {
                    selectStoreVoucherType()
                }
            }
            voucherTypeSelectionProduct.apply {
                radioButton?.setOnClickListener {
                selectProductVoucherType()
            }
            }
        }
    }

    private fun selectStoreVoucherType() {
        binding?.run {
            header.headerSubTitle = getString(R.string.smvc_creation_step_one_out_of_three_sub_title_label)
            voucherTypeSelectionShop.isActive = true
            voucherTypeSelectionProduct.isActive = false
        }
    }

    private fun selectProductVoucherType() {
        binding?.run {
            header.headerSubTitle = getString(R.string.smvc_creation_step_one_out_of_four_sub_title_label)
            voucherTypeSelectionShop.isActive = false
            voucherTypeSelectionProduct.isActive = true
        }
    }
}
