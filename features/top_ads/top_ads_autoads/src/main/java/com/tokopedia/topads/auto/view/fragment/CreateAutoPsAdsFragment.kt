package com.tokopedia.topads.auto.view.fragment

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.auto.R as topadsautoR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.DAILY_BUDGET_MULTIPLIER
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.SEEKBAR_DEFAULT_INCREMENT
import com.tokopedia.topads.auto.databinding.TopadsAutoadsPsLayoutBinding
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.view.viewmodel.AutoPsViewModel
import com.tokopedia.topads.auto.view.widget.Range
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.AutoAdsStatus
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.utils.TopadsCommonUtil
import com.tokopedia.topads.common.utils.TopadsCommonUtil.showErrorAutoAds
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.topads.common.view.sheet.TopAdsOutofCreditSheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.NumberTextWatcher
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.topads.common.R as topadscommonR

class CreateAutoPsAdsFragment : BaseDaggerFragment(), View.OnClickListener {

    private var minDailyBudget = Int.ZERO
    private var maxDailyBudget = Int.ZERO

    @JvmField
    @Inject
    var viewModelFactory: ViewModelFactory? = null

    private val viewModel: AutoPsViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory?.let {
            ViewModelProvider(
                requireActivity(),
                it
            )[AutoPsViewModel::class.java]
        }
    }

    private var binding: TopadsAutoadsPsLayoutBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopadsAutoadsPsLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel?.loadData()
        setViews()
        setupTooltip(view)
        setupObservers()
        setupListeners()
    }

    private fun setViews(){
        binding?.autoAdsCta?.text = getClickableString()
    }

    private fun setupTooltip(view: View) {
        val tooltipView =
            layoutInflater.inflate(topadscommonR.layout.tooltip_custom_view, null)
                .apply {
                    val tvToolTipText = this.findViewById<Typography>(topadscommonR.id.tooltip_text)
                    tvToolTipText?.text =
                        getString(topadscommonR.string.topads_common_daily_budget_tips)

                    val imgTooltipIcon =
                        this.findViewById<ImageUnify>(topadscommonR.id.tooltip_icon)
                    imgTooltipIcon?.setImageDrawable(view.context.getResDrawable(topadscommonR.drawable.topads_ic_tips))

                    val container = this.findViewById<ConstraintLayout>(topadscommonR.id.container)
                    container.background = ColorDrawable(
                        ContextCompat.getColor(
                            this@CreateAutoPsAdsFragment.requireContext(),
                            unifyprinciplesR.color.Unify_YN100
                        )
                    )
                }
        binding?.tipBtn?.addItem(tooltipView)
    }

    private fun setupObservers() {
        viewModel?.topAdsGetAutoAds?.observe(viewLifecycleOwner){
            setAutoAds(it.data)
        }

        viewModel?.bidInfo?.observe(viewLifecycleOwner) {
            it.data.firstOrNull()?.let { dataItem ->
                binding?.loading?.hide()
                binding?.seekbar?.value = dataItem.minDailyBudget
                binding?.rangeStart?.text = dataItem.minDailyBudgetFmt
                binding?.rangeEnd?.text = dataItem.maxDailyBudgetFmt
                minDailyBudget = dataItem.minDailyBudget
                maxDailyBudget = dataItem.maxDailyBudget
                binding?.dailyBudget?.editText?.setText(dataItem.minDailyBudgetFmt.removeCommaRawString())
                binding?.seekbar?.range = Range(
                    dataItem.minDailyBudget,
                    dataItem.maxDailyBudget,
                    SEEKBAR_DEFAULT_INCREMENT
                )
            }
        }

        viewModel?.autoAdsData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> postAutoPsSuccess()
                is Fail -> it.throwable.message?.let { errorMessage ->
                    view?.showErrorAutoAds(errorMessage)
                }
            }
            binding?.btnSubmit?.isLoading = false
        }

        viewModel?.budgetrecommendation?.observe(viewLifecycleOwner){
            when(it){
                is Success -> {
                    // under discussion
                }
                is Fail -> {
                    // under discussion
                }
            }
        }
    }

    private fun setAutoAds(data: AutoAdsResponse.TopAdsGetAutoAds.Data) {
        when (data.status) {
            AutoAdsStatus.STATUS_INACTIVE -> {
                binding?.autoAdsCard?.gone()
                binding?.manualAdsCard?.show()
                binding?.btnSubmit?.text = getString(topadsautoR.string.iklankan)
            }
            else -> {
                binding?.autoAdsCard?.show()
                binding?.manualAdsCard?.gone()
                binding?.btnSubmit?.text = getString(topadscommonR.string.topads_common_save_butt)
            }
        }
    }

    private fun postAutoPsSuccess() {
        if(viewModel?.checkDeposits() == true){
            moveToDashboard()
//            showInsufficientCredits()
        } else {
            showInsufficientCredits()
        }
    }

    private fun moveToDashboard() {
        val intent = RouteManager.getIntent(
            context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL
        ).apply {
            putExtra(
                TopAdsCommonConstant.TOPADS_AUTOADS_BUDGET_UPDATED,
                TopAdsCommonConstant.PARAM_AUTOADS_BUDGET
            )
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showInsufficientCredits(){
        val sheet = TopAdsOutofCreditSheet()
        sheet.overlayClickDismiss = false
        sheet.show(childFragmentManager)
    }

    private fun setupListeners() {
        binding?.tipBtn?.setOnClickListener(this)
        binding?.manualAdsCta?.setOnClickListener(this)
        binding?.btnSubmit?.setOnClickListener(this)
        binding?.infoTextCta?.setOnClickListener(this)

        binding?.seekbar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                updateDailyBudget(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding?.dailyBudget?.editText?.addTextChangedListener(object :
            NumberTextWatcher(binding?.dailyBudget?.editText!!) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val errorMsg = validateDailyBudget(number.toInt())
                if (errorMsg.isNotEmpty()) {
                    binding?.dailyBudget?.isInputError = true
                    binding?.dailyBudget?.setMessage(errorMsg)
                } else {
                    binding?.dailyBudget?.isInputError = false
                    binding?.dailyBudget?.setMessage(String.EMPTY)
                    binding?.potentialBroadcast?.text =
                        viewModel?.getPotentialImpression(number.toInt())
                }
                binding?.dailyBudget?.editText?.selectionEnd
                setSubmitCtaEnabled(errorMsg.isEmpty())

            }
        })
    }

    private fun updateDailyBudget(progress: Int) {
        binding?.dailyBudget?.editText?.setText(progress.toString())
    }

    private fun setSubmitCtaEnabled(isEnabled: Boolean) {
        binding?.btnSubmit?.isEnabled = isEnabled
    }

    private fun validateDailyBudget(budget: Int): String {
        return if (budget < minDailyBudget)
            String.format(
                getString(topadscommonR.string.topads_min_bid_error_msg_format),
                minDailyBudget
            )
        else if (budget > maxDailyBudget)
            String.format(
                getString(topadscommonR.string.topads_min_bid_error_msg_format),
                maxDailyBudget
            )
        else if (budget % DAILY_BUDGET_MULTIPLIER != Int.ZERO)
            getString(topadscommonR.string.error_bid_multiple_50)
        else String.EMPTY
    }

    private fun getClickableString() : SpannableString {
        val desc = getString(topadsautoR.string.topads_auto_ps_set_auto_ads_cta_desc)
        val ctaText = getString(topadsautoR.string.topads_auto_ps_set_auto_ads_cta)
        val ss = SpannableString("$desc $ctaText")
        val cs = object : ClickableSpan(){
            override fun onClick(p0: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(
                    requireContext(),
                    unifyprinciplesR.color.Unify_GN500
                )
                ds.isFakeBoldText = true
            }
        }

        ss.setSpan(cs, desc.length, desc.length + ctaText.length+1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding?.infoTextCta?.id -> RouteManager.route(
                context,
                ApplinkConstInternalGlobal.WEBVIEW,
                getString(topadsautoR.string.more_info)
            )

            binding?.manualAdsCta?.id -> RouteManager.route(
                context,
                ApplinkConstInternalTopAds.TOPADS_ADS_SELECTION
            )

            binding?.btnSubmit?.id -> {
                binding?.dailyBudget?.editText?.let {
                    viewModel?.postAutoPs(TopadsCommonUtil.convertMoneyToValue(it.text.toString()))
                    binding?.btnSubmit?.isLoading = true
                }
            }

            binding?.tipBtn?.id -> {
                val tipsList: ArrayList<TipsUiModel> = ArrayList()
                tipsList.apply {
                    add(
                        TipsUiRowModel(
                            topadsautoR.string.tip_desc_1,
                            topadscommonR.drawable.topads_create_ic_checklist
                        )
                    )
                    add(
                        TipsUiRowModel(
                            topadsautoR.string.tip_desc_2,
                            topadscommonR.drawable.topads_create_ic_checklist
                        )
                    )
                    add(
                        TipsUiRowModel(
                            topadsautoR.string.tip_desc_3,
                            topadscommonR.drawable.topads_create_ic_checklist
                        )
                    )
                }
                val tipsListSheet =
                    context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
                tipsListSheet?.showHeader = true
                tipsListSheet?.showKnob = true
                tipsListSheet?.isDragable = false
                tipsListSheet?.isHideable = false
                tipsListSheet?.setTitle(getString(topadscommonR.string.topads_common_daily_budget_tips))
                tipsListSheet?.show(childFragmentManager, String.EMPTY)
            }
        }
    }

    companion object {
        fun newInstance(): CreateAutoPsAdsFragment {
            return CreateAutoPsAdsFragment()
        }
    }

    override fun getScreenName(): String {
        return CreateAutoPsAdsFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(AutoAdsComponent::class.java).inject(this)
    }
}
