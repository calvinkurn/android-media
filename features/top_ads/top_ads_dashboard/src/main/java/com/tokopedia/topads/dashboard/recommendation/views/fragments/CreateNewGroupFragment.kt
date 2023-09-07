package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.view.sheet.CreateGroupBudgetHelpSheet
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.common.R as topadscommonR
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsCreateNewGroupBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.AUTO_BID_CONST
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.BASIC_DATE_FORMAT
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.CONST_2
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.COUNT_ZERO
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.CREATE_GROUP_SUCCESS_DIALOG_IMG_URL
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.DEFAULT_TOPADS_DEPOSITS
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.IDR_CONST
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.MAXIMUM_DAILY_BUDGET_DEFAULT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.MINIMUM_DAILY_BUDGET_DEFAULT_VALUE
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsCreditTopUpActivity
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject

class CreateNewGroupFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsCreateNewGroupBinding? = null
    private var isAutoFillGroupNameComplete = false
    private var validGroupName: Boolean = false
    private var validBudget: Boolean = false
    private var counter: Int = 0
    private var minDailyBudget: Int = MINIMUM_DAILY_BUDGET_DEFAULT_VALUE
    private var maxDailyBudget: Int = MAXIMUM_DAILY_BUDGET_DEFAULT_VALUE
    private var topadsDeposits: Int = DEFAULT_TOPADS_DEPOSITS
    private val tipsBottomSheet = CreateGroupBudgetHelpSheet()

    @JvmField
    @Inject
    var viewModelFactory: ViewModelFactory? = null

    private val viewModel: ProductRecommendationViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory?.let {
            ViewModelProvider(
                requireActivity(),
                it
            )[ProductRecommendationViewModel::class.java]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopadsCreateNewGroupBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        observeViewModel()
        checkForAutoFillGroupName()
        attachListeners()
    }

    private fun init() {
        val suggestions = arrayListOf(DataSuggestions("", listOf()))
        viewModel?.getBidInfo(suggestions)
        viewModel?.getTopAdsDeposit()

        binding?.btnSubmit?.text = String.format(
            getString(R.string.topads_insight_centre_advertise_product_with_count),
            viewModel?.getSelectedProductItems()?.size
        )
    }

    private fun checkForAutoFillGroupName() {
        binding?.groupName?.isLoading = true
        val groupName: String =
            getString(topadscommonR.string.topads_common_group) + " " + DateUtil.getCurrentDate()
                .formatTo(BASIC_DATE_FORMAT) + (if (counter == 0) "" else " ($counter)")
        counter++
        viewModel?.validateGroupName(groupName)
    }

    private fun autoFillGroupName(groupName: String) {
        binding?.groupName?.editText?.text = Editable.Factory().newEditable(groupName)
        binding?.groupName?.editText?.addTextChangedListener(groupNameTextWatcher)
        isAutoFillGroupNameComplete = true
        binding?.groupName?.isLoading = false
        validGroupName = true
        checkAllFieldsValidations()
    }

    private fun observeViewModel() {
        viewModel?.validateNameLiveData?.observe(viewLifecycleOwner) {
            if (!isAutoFillGroupNameComplete) {
                if (it.errors.isEmpty()) {
                    autoFillGroupName(it.data.groupName)
                } else {
                    checkForAutoFillGroupName()
                }
            } else {
                if (it.errors.isEmpty()) {
                    validGroupName = true
                    checkAllFieldsValidations()
                    binding?.groupName?.isInputError = false
                } else {
                    validGroupName = false
                    checkAllFieldsValidations()
                    binding?.groupName?.setMessage(getString(topadscommonR.string.topads_common_the_group_name_is_already_in_use))
                    binding?.groupName?.isInputError = true
                }
            }
        }

        viewModel?.bidInfoLiveData?.observe(viewLifecycleOwner) { data ->
            data.firstOrNull()?.let {
                minDailyBudget = it.minDailyBudget
                maxDailyBudget =
                    if (it.maxDailyBudget == 0) MAXIMUM_DAILY_BUDGET_DEFAULT_VALUE else it.maxDailyBudget
                binding?.dailyBudget?.editText?.text = Editable.Factory()
                    .newEditable(CurrencyFormatHelper.convertToRupiah(it.minDailyBudget.toString()))
                validBudget = true
                checkAllFieldsValidations()
            }
        }

        viewModel?.createGroupLiveData?.observe(viewLifecycleOwner) {
            when (val data = it) {
                is TopadsProductListState.Success -> {
                    if (topadsDeposits > DEFAULT_TOPADS_DEPOSITS) {
                        openSuccessDialog(data.data)
                    } else {
                        openInsufficientCreditsDialog()
                    }
                }
                else -> {
                    view?.let {
                        Toaster.build(
                            it,
                            getString(topadscommonR.string.topads_common_failed_to_create_ads_toast_msg),
                            Snackbar.LENGTH_LONG,
                            Toaster.TYPE_ERROR,
                            getString(topadscommonR.string.topads_common_try_again)
                        ).show()
                    }

                    binding?.btnSubmit?.let {
                        if (it.isLoading) {
                            it.isLoading = false
                        }
                    }
                }
            }
        }

        viewModel?.topadsDeposits?.observe(viewLifecycleOwner) {
            topadsDeposits = it.topadsDashboardDeposits.data.amount
        }
    }

    private fun openSuccessDialog(createdGroupId: String) {
        val dialog = DialogUnify(
            requireContext(),
            DialogUnify.VERTICAL_ACTION,
            DialogUnify.WITH_ILLUSTRATION
        )
        dialog.setImageUrl(CREATE_GROUP_SUCCESS_DIALOG_IMG_URL)
        dialog.setDescription(getString(topadscommonR.string.topads_common_create_group_success_dailog_desc))
        dialog.setTitle(getString(topadscommonR.string.topads_common_product_successfully_advertised))
        dialog.setPrimaryCTAText(getString(topadscommonR.string.topads_common_manage_ads_group))
        dialog.setSecondaryCTAText(getString(topadscommonR.string.topads_common_stay_here))
        dialog.setPrimaryCTAClickListener {
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS).apply {
                    putExtra(TopAdsDashboardConstant.TAB_POSITION, CONST_2)
                    putExtra(TopAdsDashboardConstant.GROUPID, createdGroupId)
                    putExtra(TopAdsDashboardConstant.GROUP_STRATEGY, AUTO_BID_CONST)
                }
            startActivity(intent)
            dialog.dismiss()
            requireActivity().finish()
        }
        dialog.setSecondaryCTAClickListener { activity?.finish() }
        dialog.show()
    }

    private fun openInsufficientCreditsDialog() {
        val dialog =
            DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(
            getString(topadscommonR.string.topads_common_insufficient_credits_error_desc).replace(
                IDR_CONST,
                "Rp${topadsDeposits}"
            )
        )
        dialog.setTitle(getString(topadscommonR.string.topads_common_ads_created_successfully_but_cant_appear_yet))
        dialog.setPrimaryCTAText(getString(topadscommonR.string.topads_common_add_credit))
        dialog.setSecondaryCTAText(getString(topadscommonR.string.topads_common_later_keyword))
        dialog.setPrimaryCTAClickListener {
            openNewAutoTopUpBottomSheet()
        }
        dialog.setSecondaryCTAClickListener { activity?.finish() }
        dialog.show()
    }

    private fun openNewAutoTopUpBottomSheet() {
        val intent = Intent(activity, TopAdsCreditTopUpActivity::class.java)
        intent.putExtra(TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_ACTIVE, false)
        intent.putExtra(TopAdsCreditTopUpActivity.IS_AUTO_TOP_UP_SELECTED, false)
        intent.putExtra(TopAdsCreditTopUpActivity.CREDIT_PERFORMANCE, "")
        intent.putExtra(TopAdsCreditTopUpActivity.TOP_UP_COUNT, COUNT_ZERO)
        intent.putExtra(TopAdsCreditTopUpActivity.AUTO_TOP_UP_BONUS, COUNT_ZERO)
        startActivityForResult(intent, TopAdsDashboardConstant.REQUEST_CODE_TOP_UP_CREDIT)
    }

    private fun checkAllFieldsValidations() {
        binding?.btnSubmit?.isEnabled = validGroupName && validBudget
    }

    private fun attachListeners() {
        binding?.dailyBudget?.icon1?.setOnClickListener {
            showDailyBudgetTipsBottomsheet()
        }

        binding?.dailyBudget?.editText?.addTextChangedListener(object :
            NumberTextWatcher(binding?.dailyBudget?.editText!!) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                if (number < minDailyBudget) {
                    binding?.dailyBudget?.isInputError = true
                    binding?.dailyBudget?.setMessage(String.format(getString(R.string.topads_insight_min_budget_rp),minDailyBudget))
                    validBudget = false
                    checkAllFieldsValidations()
                } else if (number > maxDailyBudget) {
                    binding?.dailyBudget?.isInputError = true
                    binding?.dailyBudget?.setMessage(String.format(getString(R.string.topads_insight_max_budget_rp),maxDailyBudget))
                    validBudget = false
                    checkAllFieldsValidations()
                } else {
                    binding?.dailyBudget?.setMessage("")
                    binding?.dailyBudget?.isInputError = false
                    validBudget = true
                    checkAllFieldsValidations()
                }
            }
        })

        binding?.btnSubmit?.setOnClickListener {
            if (binding?.btnSubmit?.isLoading != null && !(binding?.btnSubmit?.isLoading!!)) {
                binding?.btnSubmit?.isLoading = true
                viewModel?.topAdsCreateGroup(
                    getSelectedProductIds(),
                    binding?.groupName?.editText?.text.toString(),
                    CurrencyFormatHelper.convertRupiahToDouble(binding?.dailyBudget?.editText?.text.toString()),
                )
            }
        }
    }

    private fun showDailyBudgetTipsBottomsheet() {
        if(!tipsBottomSheet.isVisible){
            tipsBottomSheet.show(
                childFragmentManager,
                javaClass.name
            )
        }
    }

    private fun getSelectedProductIds(): List<String> {
        val items = viewModel?.getSelectedProductItems()
        val productIds = mutableListOf<String>()
        items?.forEach {
            productIds.add(it.id())
        }
        return productIds
    }

    companion object {
        fun createInstance(): CreateNewGroupFragment {
            return CreateNewGroupFragment()
        }
    }

    private val groupNameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            binding?.groupName?.setMessage("")
            if (p0 != null && !p0.isEmpty()) {
                if (p0.toString().length > 70) {
                    binding?.groupName?.setMessage(getString(topadscommonR.string.topads_common_ad_groups_character_limit_error_msg))
                    binding?.groupName?.isInputError = true
                    validGroupName = false
                    checkAllFieldsValidations()
                } else {
                    binding?.groupName?.isInputError = false
                    viewModel?.validateGroupName(
                        p0.toString()
                    )
                }
            } else {
                binding?.groupName?.isInputError = true
                validGroupName = false
                checkAllFieldsValidations()
            }
        }
    }

    override fun getScreenName(): String = javaClass.name
    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }
}
