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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.create.R
import com.tokopedia.topads.create.databinding.FragmentMpCreateAdGroupBinding
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.debit.autotopup.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.trackers.MpTracker
import com.tokopedia.topads.view.model.MpAdsCreateGroupViewModel
import com.tokopedia.topads.view.sheet.MpCreateGroupBudgetHelpSheet
import com.tokopedia.utils.date.DateUtil
import javax.inject.Inject

private const val GROUP_DETAIL_PAGE = "android.group_detail"

class MpCreateAdGroupFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentMpCreateAdGroupBinding
    private var minDailyBudget: Int = 0
    private var maxDailyBudget: Int = 10000000
    private var createdGroupId: String? = null
    private var counter: Int = 1
    private var autofillGroupName: String? = null
    private var validGroupName: Boolean = false
    private var validBudget: Boolean = false

    private val successImageUrl = "https://images.tokopedia.net/img/android/topads/createads_success/mp_group_creation_success_dialog.png"
    private var productId: String? = null

    companion object {
        fun newInstance(productId: String): MpCreateAdGroupFragment {
            return MpCreateAdGroupFragment().apply { arguments = Bundle().apply { putString("productId", productId) } }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val createGroupViewModel: MpAdsCreateGroupViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MpAdsCreateGroupViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getString("productId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMpCreateAdGroupBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.headerToolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.groupName.isLoading = true
        binding.btnSubmit.isEnabled = false

        val suggestions = java.util.ArrayList<DataSuggestions>()
        suggestions.add(DataSuggestions("", listOf()))
        createGroupViewModel.getBidInfo(suggestions, GROUP_DETAIL_PAGE, this::onSuccessBidSuggestion)
        createGroupViewModel.getProduct("2150857075", this::onSuccessNameSuggestion)
    }

    private fun attachClickListeners() {
        binding.dailyBudget.iconContainer.setOnClickListener {
            MpCreateGroupBudgetHelpSheet().show(childFragmentManager, MpCreateAdGroupFragment::class.java.name)
        }

        binding.groupName.addOnFocusChangeListener = { _, hasFocus ->
            if (hasFocus) {
                MpTracker.clickNewAdGroupEditName()
            } else {
                MpTracker.clickNewAdGroupEditName()
            }
        }

        binding.dailyBudget.addOnFocusChangeListener = { _, hasFocus ->
            if (hasFocus) {
                MpTracker.clickNewAdGroupEditBudget()
            } else {
                MpTracker.clickNewAdGroupEditBudget()
            }
        }

        binding.groupName.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.groupName.setMessage("")
                if (p0 != null && !p0.isEmpty()) {
                    if (p0.toString().length > 70) {
                        binding.groupName.setMessage(getString(R.string.ad_groups_character_limit_error_msg))
                        binding.groupName.isInputError = true
                        validGroupName = false
                        checkAllFieldsValidations()
                    } else {
                        binding.groupName.isInputError = false
                        createGroupViewModel.validateGroup(
                            p0.toString(),
                            ::checkGroupNameValidation
                        )
                        binding.groupName.isLoading = true
                    }
                } else {
                    binding.groupName.isInputError = true
                    validGroupName = false
                    checkAllFieldsValidations()
                }
            }
        })

        binding.dailyBudget.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && !p0.toString().isEmpty() && p0.toString().toDouble() < minDailyBudget) {
                    binding.dailyBudget.isInputError = true
                    binding.dailyBudget.setMessage("${getString(R.string.min_budget_rp)} $minDailyBudget")
                    validBudget = false
                    checkAllFieldsValidations()
                } else if (p0 != null && !p0.toString().isEmpty() && p0.toString().toDouble() > maxDailyBudget) {
                    binding.dailyBudget.isInputError = true
                    binding.dailyBudget.setMessage("${getString(R.string.max_budget_rp)} $maxDailyBudget")
                    validBudget = false
                    checkAllFieldsValidations()
                } else if (p0 == null) {
                    binding.dailyBudget.isInputError = true
                    binding.dailyBudget.setMessage("")
                    validBudget = false
                    checkAllFieldsValidations()
                } else {
                    binding.dailyBudget.setMessage("")
                    binding.dailyBudget.isInputError = false
                    validBudget = true
                    checkAllFieldsValidations()
                }
            }
        })

        binding.btnSubmit.setOnClickListener {
            if (!binding.btnSubmit.isLoading) {
                binding.btnSubmit.isLoading = true
                MpTracker.clickCreateNewAdGroupCta()
                createGroupViewModel.topAdsCreate(
                    listOf(productId ?: ""),
                    binding.groupName.editText.text.toString(),
                    binding.dailyBudget.editText.text.toString().toDouble(),
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
            binding.dailyBudget.editText.text = Editable.Factory().newEditable(it.minDailyBudget.toString())
        }
    }

    private fun onSuccessNameSuggestion(response: TopAdsProductResponse) {
        response.product.let {
            var groupName: String = (it.category?.name ?: getString(R.string.group)) + " " + DateUtil.getCurrentDate().formatTo("dd/MM/yy")
            autofillGroupName = groupName
            createGroupViewModel.validateGroup(groupName, this::checkAutofillGroupNameValidation)
        }
    }

    private fun checkAutofillGroupNameValidation(data: ResponseGroupValidateName.TopAdsGroupValidateNameV2) {
        if (data.errors.isEmpty()) {
            binding.groupName.editText.text = Editable.Factory().newEditable(data.data.groupName)
            binding.groupName.isLoading = false
            validGroupName = true
            checkAllFieldsValidations()
        } else {
            validGroupName = false
            checkAllFieldsValidations()
            createGroupViewModel.validateGroup("$autofillGroupName ($counter)", this::checkAutofillGroupNameValidation)
            counter++
            binding.groupName.isLoading = true
        }
    }

    private fun onSuccessGroupCreate(groupId: String) {
        createdGroupId = groupId
        createGroupViewModel.getTopAdsDeposit(this::getTopAdsDeposits)
        binding.btnSubmit.isLoading = false
    }

    private fun onErrorGroupCreate(msg: String?) {
        binding.groupName.setMessage(msg!!.subSequence(0, msg.length))
        binding.btnSubmit.isLoading = false
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
            binding.groupName.isInputError = true
            binding.groupName.setMessage(getString(R.string.the_group_name_is_already_in_use))
            validGroupName = false
            checkAllFieldsValidations()
        }
        binding.groupName.isLoading = false
    }

    private fun checkAllFieldsValidations() {
        binding.btnSubmit.isEnabled = validGroupName && validBudget
    }

    private fun openSuccessDialog() {
        var dialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ILLUSTRATION)
        dialog.setImageUrl(successImageUrl)
        dialog.setDescription(getString(R.string.success_dailog_description))
        dialog.setTitle(getString(R.string.product_successfully_advertised))
        dialog.setPrimaryCTAText(getString(R.string.manage_ads_group))
        dialog.setSecondaryCTAText(getString(R.string.stay_here))
        dialog.setPrimaryCTAClickListener {
            MpTracker.clickAdGroupCreatedManageCta()
            val intent = Intent(context, TopAdsGroupDetailViewActivity::class.java)
            intent.putExtra(TopAdsDashboardConstant.GROUP_ID, createdGroupId)
            intent.putExtra(TopAdsDashboardConstant.PRICE_SPEND, binding.dailyBudget.editText.text.toString())
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            MpTracker.clickAdGroupCreatedStayHereCta()
            requireActivity().finish()
        }
        dialog.show()
    }

    private fun openInsufficientCreditsDialog(data: DepositAmount) {
        var dialog = DialogUnify(requireContext(), DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(getString(R.string.success_group_creation_insufficient_credits_text).replace("Rpx.xxx", "Rp ${data.amount}"))
        dialog.setTitle(getString(R.string.ads_created_successfully_but_cant_appear_yet))
        dialog.setPrimaryCTAText(getString(R.string.add_credit))
        dialog.setSecondaryCTAText(getString(R.string.later))
        dialog.setPrimaryCTAClickListener {
            MpTracker.clickAddCreditCta()
            val intent = Intent(activity, TopAdsAddCreditActivity::class.java)
            intent.putExtra(TopAdsAddCreditActivity.SHOW_FULL_SCREEN_BOTTOM_SHEET, true)
            startActivityForResult(intent, 99)
        }
        dialog.setSecondaryCTAClickListener {
            MpTracker.clickAddCreditLaterStayCta()
            requireActivity().finish()
        }
        dialog.show()
    }
}
