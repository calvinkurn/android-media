package com.tokopedia.mvc.presentation.quota.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.utils.extension.applyRoundedRectangle
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.applyIconUnifyColor
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentQuotaInfoBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.presentation.quota.adapter.QuotaSourceAdapter
import com.tokopedia.mvc.presentation.quota.viewmodel.QuotaInfoViewModel
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.tracker.QuotaInfoTracker
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class QuotaInfoFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(
            voucherCreationQuota: VoucherCreationQuota? = null,
            showToolbar: Boolean = true
        ): QuotaInfoFragment {
            return QuotaInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_QUOTA, voucherCreationQuota)
                    putBoolean(BundleConstant.BUNDLE_KEY_SHOW_TOOLBAR, showToolbar)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SmvcFragmentQuotaInfoBinding>()
    private val voucherCreationQuota by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_QUOTA) as? VoucherCreationQuota }
    private val showToolbar by lazy { arguments?.getBoolean(BundleConstant.BUNDLE_KEY_SHOW_TOOLBAR, false) }

    @Inject
    lateinit var viewModel: QuotaInfoViewModel
    @Inject
    lateinit var tracker: QuotaInfoTracker

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
        setupInitialPage()
        setupObservables()
        setupPageData()
    }

    private fun setupInitialPage() {
        binding?.apply {
            header.setupHeader()
            setupExpandQuotaListButton(false)
            gePage.setActionClickListener {
                viewModel.getVoucherQuota()
            }
        }
    }

    private fun setupObservables() {
        viewModel.quotaInfo.observe(viewLifecycleOwner) {
            binding?.setupPage(it)
        }
        viewModel.sourceList.observe(viewLifecycleOwner) {
            binding?.setupQuotaSourceList(it)
        }
        viewModel.sourceListExpanded.observe(viewLifecycleOwner) { isExpanded ->
            binding?.setupExpandQuotaListButton(isExpanded)
        }
        viewModel.enableExpand.observe(viewLifecycleOwner) {
            binding?.groupExpandAction?.isVisible = it
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding?.displayError(it)
        }
    }

    private fun setupPageData() {
        if (voucherCreationQuota != null) {
            viewModel.setQuotaInfo(voucherCreationQuota ?: return)
        } else {
            viewModel.getVoucherQuota()
        }
    }

    private fun SmvcFragmentQuotaInfoBinding.setupPage(quotaInfo: VoucherCreationQuota) {
        gePage.isVisible = false
        binding?.scrollContent?.isVisible = true
        btnAction.text = quotaInfo.ctaText
        btnAction.setOnClickListener {
            val uri = Uri.parse(quotaInfo.ctaLink)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            tracker.sendClickButtonUpgradeEvent(quotaInfo.ctaText)
            startActivity(intent)
        }
        tickerQuotaInfo.setHtmlDescription(quotaInfo.tickerTitle)
        tickerQuotaInfo.isVisible = quotaInfo.tickerTitle.isNotEmpty()
        tfRemainingQuota.text = getString(R.string.smvc_quota_info_quota_remaining_format, quotaInfo.quotaUsageFormatted)
        tfMoreSource.text = getString(R.string.smvc_quota_info_quota_button_format, quotaInfo.sources.size)
        viewExpand.applyRoundedRectangle(com.tokopedia.unifyprinciples.R.color.Unify_NN50)
        viewModel.displayShortQuotaList()
    }

    private fun SmvcFragmentQuotaInfoBinding.setupQuotaSourceList(sources: List<VoucherCreationQuota.Sources>) {
        val adapter = QuotaSourceAdapter()
        rvQuotaSource.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvQuotaSource.adapter = adapter
        adapter.setDataList(sources)
    }

    private fun SmvcFragmentQuotaInfoBinding.setupExpandQuotaListButton(isExpanded: Boolean) {
        binding?.viewExpand?.setOnClickListener {
            val iconRes = if (isExpanded) IconUnify.CHEVRON_DOWN else IconUnify.CHEVRON_UP
            binding?.iconExpand?.setImage(iconRes)
            viewModel.toggleSourceListExpanded(isExpanded)
        }
    }

    private fun SmvcFragmentQuotaInfoBinding.displayError(it: Throwable) {
        gePage.errorDescription.text = ErrorHandler.getErrorMessage(context, it)
        gePage.isVisible = true
        scrollContent.isVisible = false
    }

    private fun HeaderUnify.setupHeader() {
        title = context.getString(R.string.smvc_quota_info_page_title)
        val iconClose = MethodChecker.getDrawable(context,
            com.tokopedia.abstraction.R.drawable.ic_close_default)
        applyIconUnifyColor(iconClose, com.tokopedia.unifyprinciples.R.color.Unify_NN950 )
        navigationIcon = iconClose
        setNavigationOnClickListener {
            activity?.finish()
            tracker.sendClickCloseEvent()
        }
        isVisible = showToolbar.orFalse()
    }
}
