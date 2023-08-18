package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.view.sheet.CreateGroupBudgetHelpSheet
import com.tokopedia.topads.constants.MpTopadsConst.AUTO_BID_CONST
import com.tokopedia.topads.constants.MpTopadsConst.BASIC_DATE_FORMAT
import com.tokopedia.topads.constants.MpTopadsConst.CONST_2
import com.tokopedia.topads.constants.MpTopadsConst.GROUP_DETAIL_PAGE
import com.tokopedia.topads.constants.MpTopadsConst.IDR_CONST
import com.tokopedia.topads.constants.MpTopadsConst.PRODUCT_ID_PARAM
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.FragmentMpCreateAdGroupBinding
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.trackers.MpTracker
import com.tokopedia.topads.view.model.MpAdsCreateGroupViewModel
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject

class MpCreateAdGroupFragment : BaseDaggerFragment() {

    private var binding: FragmentMpCreateAdGroupBinding? = null
    private var minDailyBudget: Int = 0
    private var maxDailyBudget: Int = 10000000
    private var createdGroupId: String? = null
    private var counter: Int = 1
    private var autofillGroupName: String? = null
    private var validGroupName: Boolean = false
    private var validBudget: Boolean = false

    private val successImageUrl =
        "https://images.tokopedia.net/img/android/topads/createads_success/mp_group_creation_success_dialog.png"
    private var productId: String? = null

    companion object {
        private const val REQUEST_CODE = 99
        fun newInstance(productId: String): MpCreateAdGroupFragment {
            return MpCreateAdGroupFragment().apply {
                arguments = Bundle().apply { putString(PRODUCT_ID_PARAM, productId) }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val createGroupViewModel: MpAdsCreateGroupViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MpAdsCreateGroupViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getString(PRODUCT_ID_PARAM)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMpCreateAdGroupBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        attachClickListeners()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    private fun init() {
        binding?.headerToolbar?.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding?.groupName?.isLoading = true
        binding?.btnSubmit?.isEnabled = false

        val suggestions = java.util.ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions("", listOf()))
        createGroupViewModel.getBidInfo(
            suggestions,
            GROUP_DETAIL_PAGE,
            this::onSuccessBidSuggestion
        )
        createGroupViewModel.getProduct(productId, this::onSuccessNameSuggestion)
    }

    private fun attachClickListeners() {
        binding?.dailyBudget?.iconContainer?.setOnClickListener {
            CreateGroupBudgetHelpSheet().show(
                childFragmentManager,
                MpCreateAdGroupFragment::class.java.name
            )
        }

        binding?.groupName?.addOnFocusChangeListener = { _, hasFocus ->
            if (hasFocus) {
                MpTracker.clickNewAdGroupEditName()
            } else {
                MpTracker.clickNewAdGroupEditName()
            }
        }

        binding?.dailyBudget?.addOnFocusChangeListener = { _, hasFocus ->
            if (hasFocus) {
                MpTracker.clickNewAdGroupEditBudget()
            }
        }

        binding?.groupName?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding?.groupName?.setMessage("")
                if (p0 != null && !p0.isEmpty()) {
                    if (p0.toString().length > 70) {
                        binding?.groupName?.setMessage(getString(R.string.ad_groups_character_limit_error_msg))
                        binding?.groupName?.isInputError = true
                        validGroupName = false
                        checkAllFieldsValidations()
                    } else {
                        binding?.groupName?.isInputError = false
                        createGroupViewModel.validateGroup(
                            p0.toString(),
                            ::checkGroupNameValidation
                        )
                        binding?.groupName?.isLoading = true
                    }
                } else {
                    binding?.groupName?.isInputError = true
                    validGroupName = false
                    checkAllFieldsValidations()
                }
            }
        })

        binding?.dailyBudget?.editText?.addTextChangedListener(object :
            NumberTextWatcher(binding?.dailyBudget?.editText!!) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                if (number < minDailyBudget) {
                    binding?.dailyBudget?.isInputError = true
                    binding?.dailyBudget?.setMessage("${getString(R.string.min_budget_rp)} $minDailyBudget")
                    validBudget = false
                    checkAllFieldsValidations()
                } else if (number > maxDailyBudget) {
                    binding?.dailyBudget?.isInputError = true
                    binding?.dailyBudget?.setMessage("${getString(R.string.max_budget_rp)} $maxDailyBudget")
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
                MpTracker.clickCreateNewAdGroupCta()
                createGroupViewModel.topAdsCreate(
                    listOf(productId ?: ""),
                    binding?.groupName?.editText?.text.toString(),
                    CurrencyFormatHelper.convertRupiahToDouble(binding?.dailyBudget?.editText?.text.toString()),
                    this::onSuccessGroupCreate,
                    this::onErrorGroupCreate
                )
            }
        }
    }

    private fun onSuccessBidSuggestion(data: List<TopadsBidInfo.DataItem>) {
        data.firstOrNull()?.let {
            minDailyBudget = it.minDailyBudget
            maxDailyBudget = if (it.maxDailyBudget == 0) 10000000 else it.maxDailyBudget
            binding?.dailyBudget?.editText?.text = Editable.Factory()
                .newEditable(CurrencyFormatHelper.convertToRupiah(it.minDailyBudget.toString()))
        }
    }

    private fun onSuccessNameSuggestion(response: TopAdsProductResponse) {
        response.product.let {
            var groupName: String =
                (if (it.category?.name.isNullOrEmpty()) getString(com.tokopedia.topads.common.R.string.topads_common_group) else it.category?.name) + " " + DateUtil.getCurrentDate()
                    .formatTo(BASIC_DATE_FORMAT)
            autofillGroupName = groupName
            createGroupViewModel.validateGroup(groupName, this::checkAutofillGroupNameValidation)
        }
    }

    private fun checkAutofillGroupNameValidation(data: ResponseGroupValidateName.TopAdsGroupValidateNameV2) {
        if (data.errors.isEmpty()) {
            binding?.groupName?.editText?.text = Editable.Factory().newEditable(data.data.groupName)
            binding?.groupName?.isLoading = false
            validGroupName = true
            checkAllFieldsValidations()
        } else {
            validGroupName = false
            checkAllFieldsValidations()
            createGroupViewModel.validateGroup(
                "$autofillGroupName ($counter)",
                this::checkAutofillGroupNameValidation
            )
            counter++
            binding?.groupName?.isLoading = true
        }
    }

    private fun onSuccessGroupCreate(groupId: String) {
        createdGroupId = groupId
        createGroupViewModel.getTopAdsDeposit(this::getTopAdsDeposits)
        binding?.btnSubmit?.isLoading = false
    }

    private fun onErrorGroupCreate(msg: String?) {
        binding?.groupName?.setMessage(msg!!.subSequence(0, msg.length))
        binding?.btnSubmit?.isLoading = false
    }

    private fun getTopAdsDeposits(data: DepositAmount) {
        val isEnoughDeposit = data.amount > 0
        if (isEnoughDeposit) {
            openSuccessDialog()
        } else {
            openInsufficientCreditsDialog(data)
        }
    }

    private fun checkGroupNameValidation(data: ResponseGroupValidateName.TopAdsGroupValidateNameV2) {
        if (data.errors.isEmpty()) {
            validGroupName = true
            checkAllFieldsValidations()
        } else {
            binding?.groupName?.isInputError = true
            binding?.groupName?.setMessage(getString(R.string.the_group_name_is_already_in_use))
            validGroupName = false
            checkAllFieldsValidations()
        }
        binding?.groupName?.isLoading = false
    }

    private fun checkAllFieldsValidations() {
        binding?.btnSubmit?.isEnabled = validGroupName && validBudget
    }

    private fun openSuccessDialog() {
        var dialog = DialogUnify(
            requireContext(),
            DialogUnify.VERTICAL_ACTION,
            DialogUnify.WITH_ILLUSTRATION
        )
        dialog.setImageUrl(successImageUrl)
        dialog.setDescription(getString(com.tokopedia.topads.common.R.string.topads_common_create_group_success_dailog_desc))
        dialog.setTitle(getString(com.tokopedia.topads.common.R.string.topads_common_product_successfully_advertised))
        dialog.setPrimaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_manage_ads_group))
        dialog.setSecondaryCTAText(getString(com.tokopedia.topads.common.R.string.topads_common_stay_here))
        dialog.setPrimaryCTAClickListener {
            MpTracker.clickAdGroupCreatedManageCta()
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
        dialog.setSecondaryCTAClickListener {
            MpTracker.clickAdGroupCreatedStayHereCta()
            requireActivity().finish()
        }
        dialog.show()
    }

    private fun openInsufficientCreditsDialog(data: DepositAmount) {
        var dialog =
            DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(
            getString(R.string.success_group_creation_insufficient_credits_text).replace(
                IDR_CONST,
                "Rp${data.amount}"
            )
        )
        dialog.setTitle(getString(R.string.ads_created_successfully_but_cant_appear_yet))
        dialog.setPrimaryCTAText(getString(R.string.add_credit))
        dialog.setSecondaryCTAText(getString(R.string.later))
        dialog.setPrimaryCTAClickListener {
            MpTracker.clickAddCreditCta()
            val intent = Intent(activity, TopAdsAddCreditActivity::class.java)
            intent.putExtra(TopAdsAddCreditActivity.SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
            startActivityForResult(intent, REQUEST_CODE)
        }
        dialog.setSecondaryCTAClickListener {
            MpTracker.clickAddCreditLaterStayCta()
            requireActivity().finish()
        }
        dialog.show()
    }
}
