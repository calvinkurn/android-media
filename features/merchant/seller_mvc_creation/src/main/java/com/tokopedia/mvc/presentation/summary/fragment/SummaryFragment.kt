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
 import com.tokopedia.mvc.R
 import com.tokopedia.mvc.databinding.SmvcFragmentSummaryBinding
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

        val green2 = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        val green = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN200)
        val gd = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(green2, green)
        )
        val corner = 16.toPx().toFloat()
        gd.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, corner, corner, corner, corner)

        binding?.layoutPreview?.viewBg?.background = gd
        binding?.layoutSubmission?.cbTnc?.text = MethodChecker.fromHtml(
            getString(R.string.smvc_summary_page_tnc_text)
        )
    }

    private fun setupObservables() {

    }

    private fun SmvcFragmentSummaryBinding.setupView() {
        header.setupHeader()
    }

    private fun HeaderUnify.setupHeader() {
        title = context.getString(R.string.smvc_summary_page_title)
        setNavigationOnClickListener {
            activity?.finish()
        }
    }
}
