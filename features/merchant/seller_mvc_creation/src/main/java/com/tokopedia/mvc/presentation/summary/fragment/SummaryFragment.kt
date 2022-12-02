package com.tokopedia.mvc.presentation.summary.fragment

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentSummaryBinding
import com.tokopedia.mvc.databinding.SmvcFragmentSummaryPreviewBinding
import com.tokopedia.mvc.databinding.SmvcFragmentSummarySubmissionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailProductSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherInfoSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherSettingSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherDetailVoucherTypeSectionBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.presentation.summary.viewmodel.SummaryViewModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SummaryFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<SmvcFragmentSummaryBinding>()

    @Inject
    lateinit var viewModel: SummaryViewModel

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
        binding = SmvcFragmentSummaryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        binding?.setupView()
        setupObservables()
        viewModel.aaa()
    }

    private fun setupObservables() {

    }

    private fun SmvcFragmentSummaryBinding.setupView() {
        header.setupHeader()
        layoutPreview.setupLayoutPreview()
        layoutType.setupLayoutType()
        layoutInfo.setupLayoutInfo()
        layoutSetting.setupLayoutSetting()
        layoutProducts.setupLayoutProducts()
        layoutSubmission.setupLayoutSubmission()
    }

    private fun HeaderUnify.setupHeader() {
        title = context.getString(R.string.smvc_summary_page_title)
        setNavigationOnClickListener {
            activity?.finish()
        }
    }

    private fun SmvcFragmentSummaryPreviewBinding.setupLayoutPreview() {
        val green2 = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        val green = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN200)
        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(green2, green)
        )
        val corner = 16.toPx().toFloat()
        gd.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, corner, corner, corner, corner)
        viewBg.background = gd
    }

    private fun SmvcVoucherDetailVoucherTypeSectionBinding.setupLayoutType() {
        tpgEditAction.visible()
    }

    private fun SmvcVoucherDetailVoucherInfoSectionBinding.setupLayoutInfo() {
        tpgEditAction.visible()
    }

    private fun SmvcVoucherDetailVoucherSettingSectionBinding.setupLayoutSetting() {
        tpgEditAction.visible()
    }

    private fun SmvcVoucherDetailProductSectionBinding.setupLayoutProducts() {
        tpgEditAction.visible()
    }

    private fun SmvcFragmentSummarySubmissionBinding.setupLayoutSubmission() {
        cbTnc.text = MethodChecker.fromHtml(getString(R.string.smvc_summary_page_tnc_text))
    }
}
