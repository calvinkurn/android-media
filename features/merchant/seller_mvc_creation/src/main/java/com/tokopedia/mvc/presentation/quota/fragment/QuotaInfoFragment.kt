package com.tokopedia.mvc.presentation.quota.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.applyIconUnifyColor
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentQuotaInfoBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.presentation.quota.viewmodel.QuotaInfoViewModel
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class QuotaInfoFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(voucherCreationQuota: VoucherCreationQuota): QuotaInfoFragment {
            return QuotaInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_QUOTA, voucherCreationQuota)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SmvcFragmentQuotaInfoBinding>()
    private val voucherCreationQuota by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_QUOTA) as? VoucherCreationQuota }

    @Inject
    lateinit var viewModel: QuotaInfoViewModel

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentQuotaInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        binding?.setupView()
        setupObservables()
        setupPage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupPage() {
        voucherCreationQuota?.let {
            viewModel.setQuotaInfo(it)
        }
    }

    private fun setupObservables() {
        //
    }

    private fun SmvcFragmentQuotaInfoBinding.setupView() {
        header.setupHeader()
    }

    private fun HeaderUnify.setupHeader() {
        title = context.getString(R.string.smvc_quota_info_page_title)
        val iconClose = MethodChecker.getDrawable(context,
            com.tokopedia.abstraction.R.drawable.ic_close_default)
        applyIconUnifyColor(iconClose, com.tokopedia.unifyprinciples.R.color.Unify_NN950 )
        navigationIcon = iconClose
        setNavigationOnClickListener {
            activity?.finish()
        }
    }
}
