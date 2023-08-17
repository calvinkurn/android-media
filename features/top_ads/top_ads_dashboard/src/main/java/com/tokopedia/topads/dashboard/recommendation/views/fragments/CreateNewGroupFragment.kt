package com.tokopedia.topads.dashboard.recommendation.views.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.view.sheet.CreateGroupBudgetHelpSheet
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.databinding.FragmentTopadsCreateNewGroupBinding
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.BASIC_DATE_FORMAT
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.INSIGHT_CENTRE_GQL_SOURCE
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.MAXIMUM_DAILY_BUDGET_DEFAULT_VALUE
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants.MINIMUM_DAILY_BUDGET_DEFAULT_VALUE
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopadsProductListState
import com.tokopedia.topads.dashboard.recommendation.viewmodel.ProductRecommendationViewModel
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import javax.inject.Inject

class CreateNewGroupFragment : BaseDaggerFragment() {

    private var binding: FragmentTopadsCreateNewGroupBinding? = null
    private var autofillGroupName: String? = null
    private var validGroupName: Boolean = false
    private var validBudget: Boolean = false
    private var counter: Int = 0
    private var minDailyBudget: Int = MINIMUM_DAILY_BUDGET_DEFAULT_VALUE
    private var maxDailyBudget: Int = MAXIMUM_DAILY_BUDGET_DEFAULT_VALUE

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ProductRecommendationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[ProductRecommendationViewModel::class.java]
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
        autoFillGroupName()
        attachClickListeners()
    }

    private fun init() {
        val suggestions = arrayListOf(DataSuggestions("", listOf()))
        viewModel.getBidInfo(suggestions)

        when (val products = viewModel.productItemsLiveData.value) {
            is TopadsProductListState.Success -> {
                val selectedItems =
                    products.data.filter {
                        (it as? ProductItemUiModel)?.isSelected ?: false
                    }
                binding?.btnSubmit?.text = String.format(
                    getString(R.string.topads_insight_centre_advertise_product_with_count),
                    selectedItems.size
                )
            }
            else -> {}
        }
    }

    private fun autoFillGroupName() {
        val groupName: String =
            getString(com.tokopedia.topads.common.R.string.topads_common_group) + " " + DateUtil.getCurrentDate()
                .formatTo(BASIC_DATE_FORMAT)
        autofillGroupName = groupName
        viewModel.validateGroup(groupName)
    }

    private fun observeViewModel() {
        viewModel.validateNameLiveData.observe(viewLifecycleOwner) {
            if (it.errors.isEmpty()) {
                binding?.groupName?.editText?.text =
                    Editable.Factory().newEditable(it.data.groupName)
                binding?.groupName?.isLoading = false
                validGroupName = true
                checkAllFieldsValidations()
            } else {
                validGroupName = false
                checkAllFieldsValidations()
                viewModel.validateGroup(
                    "$autofillGroupName ($counter)"
                )
                counter++
                binding?.groupName?.isLoading = true
            }
        }

        viewModel.bidInfoLiveData.observe(viewLifecycleOwner){data ->
            data.firstOrNull()?.let {
                minDailyBudget = it.minDailyBudget
                maxDailyBudget = if (it.maxDailyBudget == 0) MAXIMUM_DAILY_BUDGET_DEFAULT_VALUE else it.maxDailyBudget
                binding?.dailyBudget?.editText?.text = Editable.Factory()
                    .newEditable(CurrencyFormatHelper.convertToRupiah(it.minDailyBudget.toString()))
            }
        }
    }

    private fun checkAllFieldsValidations() {
        binding?.btnSubmit?.isEnabled = validGroupName && validBudget
    }

    private fun attachClickListeners() {
        binding?.dailyBudget?.icon1?.setOnClickListener {
            showDailyBudgetTipsBottomsheet()
        }
    }

    private fun showDailyBudgetTipsBottomsheet() {
        CreateGroupBudgetHelpSheet().show(
            childFragmentManager,
            javaClass.name
        )
    }

    companion object {
        fun createInstance(): CreateNewGroupFragment {
            return CreateNewGroupFragment()
        }
    }

    override fun getScreenName(): String = javaClass.name
    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }
}
