package com.tokopedia.topads.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_POSITIVE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_TYPE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.EXACT_POSITIVE
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.databinding.TopadsSummaryEditAdGroupBinding
import com.tokopedia.topads.common.domain.model.createedit.CreateAdGroupDailyBudgetItemUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateAdGroupItemUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemState
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemTag
import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupAdapter
import com.tokopedia.topads.common.view.adapter.createedit.CreateEditAdGroupTypeFactory
import com.tokopedia.topads.common.view.adapter.createedit.decorator.CustomDividerItemDecoration
import com.tokopedia.topads.common.view.sheet.CreateEditAdGroupNameBottomSheet
import com.tokopedia.topads.common.view.sheet.TopAdsOutofCreditSheet
import com.tokopedia.topads.common.view.sheet.TopAdsSuccessSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.recommendation.common.TopAdsProductRecommendationConstants
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.SummaryViewModel
import com.tokopedia.topads.view.sheet.CreateKeywordListBottomSheet
import com.tokopedia.topads.view.sheet.CreateProductListBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.HashMap
import javax.inject.Inject
import com.tokopedia.topads.common.R as topadscommonR

class ProductSummaryAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private var binding by autoClearedNullable<TopadsSummaryEditAdGroupBinding>()
    private val createEditAdGroupAdapter by lazy {
        CreateEditAdGroupAdapter(CreateEditAdGroupTypeFactory())
    }
    var counter = Int.ZERO
    private var adsItemsList: ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> =
        arrayListOf()
    private var keywordsList: MutableList<KeySharedModel> = mutableListOf()
    private var strategies: MutableList<String> = mutableListOf()
    private var bidTypeData: ArrayList<TopAdsBidSettingsModel>? = arrayListOf()
    private var isDailyBudgetOn: Boolean = false
    private var isAutoPsWhitelisted: Boolean = false

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private var viewModel: SummaryViewModel? = null


    companion object {

        private const val MULTIPLIER = 40
        private const val AUTOBID_DEFUALT_BUDGET = 16000

        fun createInstance(): Fragment {
            val fragment = ProductSummaryAdsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getManualList(context: Context): MutableList<Visitable<*>> {
        return mutableListOf(
            CreateAdGroupItemUiModel(
                CreateEditAdGroupItemTag.NAME,
                getString(R.string.topads_ads_name_title),
                getString(R.string.topads_ads_name_subtitle),
                isEditable = true,
                isItemClickable = true
            ) {
                editGroupName()
            },
            CreateAdGroupItemUiModel(
                CreateEditAdGroupItemTag.PRODUCT,
                getString(R.string.topads_create_product),
                String.format(getString(R.string.topads_create_product_prefix), stepperModel?.selectedProductIds?.count()),
                isItemClickable = true
            ) { openProductListBottomSheet() },
            CreateAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_SEARCH,
                getString(R.string.topads_ads_search_bid_title),
                subtitleOne = getString(R.string.ad_group_stats_advertisement_costs_bottomsheet_title),
                subtitleOneValue = String.format(getString(R.string.topads_ads_search_browse_bid_subtitle_prefix), stepperModel?.finalSearchBidPerClick),
                subtitleTwo = getString(R.string.keywords),
                subtitleTwoValue = String.format(getString(R.string.topads_ads_search_bid_subtitle_two_prefix),
                    stepperModel?.selectedKeywordStage?.count(),
                    stepperModel?.minBid.toString(),
                    stepperModel?.maxBid.toString()),
                isManualAdBid = true,
                isItemClickable = true
            ) { openKeywordListBottomSheet() },
            CreateAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_RECOMMENDATION,
                getString(R.string.topads_ads_browse_bid_item_title),
                subtitleOne = getString(R.string.topads_ads_browse_bid_subtitle),
                subtitleOneValue = String.format(getString(R.string.topads_ads_search_browse_bid_subtitle_prefix), stepperModel?.finalRecommendationBidPerClick),
                isManualAdBid = true,
                hasDivider = true
            ) {},

            CreateAdGroupDailyBudgetItemUiModel(
                getString(R.string.topads_ads_daily_budget_item_title),
                getString(R.string.topads_ads_daily_budget_item_subtitle),
                hasDivider = true,
                dailyBudget = stepperModel?.dailyBudget.toString(),
                isDailyBudgetEnabled = false,
                onSwitchChange = ::onDailyBudgetSwitchToggle,
                onDailyBudgetChange = ::onDailyBudgetChange
            ) {},
            CreateEditAdGroupItemAdsPotentialUiModel(
                CreateEditAdGroupItemTag.POTENTIAL_PERFORMANCE,
                getString(R.string.topads_ads_performance_item_title),
                getString(R.string.topads_ads_performance_item_subtitle),
                String.EMPTY,
                if (stepperModel != null)
                    mutableListOf(
                        CreateEditAdGroupItemAdsPotentialWidgetUiModel(
                            getString(topadscommonR.string.topads_ads_performance_search_stats), stepperModel?.searchPrediction.toString()
                        ),
                        CreateEditAdGroupItemAdsPotentialWidgetUiModel(
                            getString(topadscommonR.string.topads_ads_performance_browse_stats), stepperModel?.recomPrediction.toString()
                        ),
                        CreateEditAdGroupItemAdsPotentialWidgetUiModel(
                            getString(topadscommonR.string.topads_ads_performance_total_stats), (stepperModel?.searchPrediction!! + stepperModel?.recomPrediction!!).toString()
                        ),

                        )
                else mutableListOf(),
                true,
                CreateEditAdGroupItemState.LOADED
            )
        )
    }

    private fun openKeywordListBottomSheet() {
        stepperModel?.selectedKeywordStage?.let {
            CreateKeywordListBottomSheet.newInstance(it).show(childFragmentManager)
        }
    }

    private fun openProductListBottomSheet() {
        stepperModel?.productList?.let {
            CreateProductListBottomSheet.newInstance(it).show(childFragmentManager)
        }
    }

    private fun editGroupName() {
        stepperModel?.groupName?.let {
            CreateEditAdGroupNameBottomSheet.newInstance(it, "")
                .show(childFragmentManager) { name ->
                    viewModel?.validateGroup(name, ::onValidateNameSuccess, ::onFailure)
                }
        }
    }

    private fun onDailyBudgetSwitchToggle(isChecked: Boolean) {
        isDailyBudgetOn = isChecked
    }

    private fun onDailyBudgetChange(isEnable: Boolean, dailyBudget:Double) {
        binding?.createProductAdButton?.isEnabled = isEnable
        stepperModel?.dailyBudget = dailyBudget.toInt()
    }

    private fun createProductAdGroup() {
        binding?.createLoading?.show()
        binding?.createProductAdButton?.isEnabled = false
        viewModel?.topAdsCreated(getProductData(),
            getKeywordData(), getGroupData(),
            this::onSuccessActivation, this::onErrorActivation)
    }

    private fun getKeywordData(): HashMap<String, Any?> {
        val dataKeyword = HashMap<String, Any?>()
        keywordsList.clear()

        if (stepperModel?.autoBidState?.isEmpty() == true && (stepperModel?.selectedKeywordStage?.count()
                ?: Int.ZERO) > Int.ZERO) {
            stepperModel?.selectedKeywordStage?.forEachIndexed { index, _ ->
                addKeywords(index)
            }
        }
        dataKeyword[ParamObject.POSITIVE_CREATE] = keywordsList
        stepperModel?.suggestedBidPerClick?.toFloat()?.let {
            if (it > 0.0f) {
                val suggestionBidSettings = listOf(
                    GroupEditInput.Group.TopadsSuggestionBidSetting(ParamObject.PRODUCT_SEARCH, it),
                    GroupEditInput.Group.TopadsSuggestionBidSetting(ParamObject.PRODUCT_BROWSE, it)
                )
                dataKeyword[ParamObject.SUGGESTION_BID_SETTINGS] = suggestionBidSettings
            }
        }
        return dataKeyword
    }

    private fun getProductData(): Bundle {
        val datProduct = Bundle()
        adsItemsList.clear()
        if ((stepperModel?.selectedProductIds?.count() ?: Int.ZERO) > Int.ZERO) {
            stepperModel?.selectedProductIds?.forEachIndexed { index, _ ->
                addProducts(index)
            }
        }
        datProduct.putParcelableArrayList(ParamObject.ADDED_PRODUCTS, adsItemsList)
        return datProduct
    }

    private fun getGroupData(): HashMap<String, Any?> {
        val dataMap = HashMap<String, Any?>()

        dataMap[ParamObject.BUDGET_LIMITED] = isDailyBudgetOn

        dataMap[ParamObject.DAILY_BUDGET] = stepperModel?.dailyBudget
        dataMap[ParamObject.GROUP_NAME] = stepperModel?.groupName ?: String.EMPTY
        dataMap[ParamObject.GROUPID] = String.EMPTY
        dataMap[ParamObject.NAME_EDIT] = true
        dataMap[ParamObject.ACTION_TYPE] = ParamObject.ACTION_CREATE
        if (stepperModel?.autoBidState?.isNotEmpty() == true) {
            strategies.clear()
            strategies.add(stepperModel?.autoBidState!!)
        } else {
            bidTypeData?.add(TopAdsBidSettingsModel(ParamObject.PRODUCT_SEARCH,
                stepperModel?.finalSearchBidPerClick?.toFloat()))
            bidTypeData?.add(TopAdsBidSettingsModel(ParamObject.PRODUCT_BROWSE,
                stepperModel?.finalRecommendationBidPerClick?.toFloat()))
            dataMap[ParamObject.BID_TYPE] = bidTypeData
        }

        dataMap[ParamObject.STRATEGIES] = strategies
        return dataMap

    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(R.string.summary_page_step))
    }

    override fun getScreenName(): String {
        return SummaryAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory?.let { ViewModelProvider(this, it)[SummaryViewModel::class.java] }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = TopadsSummaryEditAdGroupBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    private fun setUpView() {
        binding?.header?.hide()
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = createEditAdGroupAdapter
            activity?.let {
                createEditAdGroupAdapter.updateList(getListOnBasisOfBid(it))
            }
        }
        setDividerOnRecyclerView()
        setUpApplyButton()

    }

    private fun getListOnBasisOfBid(it: FragmentActivity): List<Visitable<*>> {
        return if (stepperModel?.autoBidState?.isEmpty() == true) getManualList(it) else getAutoList(it)
    }

    private fun getAutoList(it: FragmentActivity): List<Visitable<*>> {
        return mutableListOf(
            CreateAdGroupItemUiModel(
                CreateEditAdGroupItemTag.NAME,
                getString(R.string.topads_ads_name_title),
                getString(R.string.topads_ads_name_subtitle),
                isEditable = true,
                isItemClickable = true,
            ) { editGroupName() },
            CreateAdGroupItemUiModel(
                CreateEditAdGroupItemTag.PRODUCT,
                getString(R.string.topads_create_product),
                String.format(getString(R.string.topads_create_product_prefix), stepperModel?.selectedProductIds?.count()),
                isItemClickable = true
            ) { openProductListBottomSheet() },
            CreateAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_SEARCH,
                getString(R.string.topads_ads_search_bid_title),
                subtitle = getString(R.string.topads_ads_search_browse_bid_auto_subtitle)
            ) {},
            CreateAdGroupItemUiModel(
                CreateEditAdGroupItemTag.ADS_RECOMMENDATION,
                getString(R.string.topads_ads_browse_bid_item_title),
                subtitle = getString(R.string.topads_ads_search_browse_bid_auto_subtitle),
                hasDivider = true
            ) {},

            CreateAdGroupDailyBudgetItemUiModel(
                getString(R.string.topads_ads_daily_budget_item_title),
                getString(R.string.topads_ads_daily_budget_item_subtitle),
                hasDivider = true,
                dailyBudget = stepperModel?.dailyBudget.toString(),
                isDailyBudgetEnabled = false,
                onSwitchChange = ::onDailyBudgetSwitchToggle,
                onDailyBudgetChange = ::onDailyBudgetChange
            ) {},
        )
    }

    private fun setUpApplyButton() {
        binding?.captionOne?.text = MethodChecker.fromHtml(context?.getString(topadscommonR.string.top_ads_create_text_caption_one))
        binding?.captionTwo?.text = MethodChecker.fromHtml(context?.getString(topadscommonR.string.top_ads_create_text_caption_two))
    }

    private fun setDividerOnRecyclerView() {
        val dividerPositions = createEditAdGroupAdapter.list
            .mapIndexedNotNull { index, item ->
                if (item is CreateAdGroupItemUiModel && item.hasDivider) {
                    index
                } else if (item is CreateEditAdGroupItemAdsPotentialUiModel && item.hasDivider) {
                    index
                } else if (item is CreateAdGroupDailyBudgetItemUiModel && item.hasDivider) {
                    index
                } else {
                    null
                }
            }

        val itemDecoration = CustomDividerItemDecoration(dividerPositions)
        binding?.recyclerView?.apply {
            addItemDecoration(itemDecoration)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.getVariantById()
        attachObservers()
        setDailyBudget()
        setUpView()
        setInitialNameCounter()
        checkForAutoFillGroupName()
        setUpClicksOnViews()
    }

    private fun attachObservers() {
        viewModel?.shopVariant?.observe(viewLifecycleOwner) { shopVariants ->
            isAutoPsWhitelisted = shopVariants.isNotEmpty() && shopVariants.filter {
                it.experiment == TopAdsCommonConstant.AUTOPS_EXPERIMENT &&
                    it.variant == TopAdsCommonConstant.AUTOPS_VARIANT
            }
                .isNotEmpty()
        }
    }

    private fun setInitialNameCounter() {
        counter = Int.ZERO
    }

    private fun setUpClicksOnViews() {
        binding?.createProductAdButton?.setOnClickListener {
            createProductAdGroup()
        }
    }

    private fun setDailyBudget() {
        if (stepperModel?.autoBidState?.isEmpty() == true) {
            stepperModel?.dailyBudget = if ((stepperModel?.finalSearchBidPerClick
                    ?: Int.ZERO) > (stepperModel?.finalRecommendationBidPerClick ?: Int.ZERO)) {
                (stepperModel?.finalSearchBidPerClick ?: Int.ZERO) * MULTIPLIER
            } else {
                (stepperModel?.finalRecommendationBidPerClick ?: Int.ZERO) * MULTIPLIER
            }
        } else {
            stepperModel?.dailyBudget = AUTOBID_DEFUALT_BUDGET
        }

    }

    private fun checkForAutoFillGroupName() {
        val groupName: String =
            getString(topadscommonR.string.topads_common_group) + " " + DateUtil.getCurrentDate()
                .formatTo(TopAdsProductRecommendationConstants.BASIC_DATE_FORMAT) + (if (counter == Int.ZERO) String.EMPTY else " ($counter)")
        counter++
        viewModel?.validateGroup(groupName, ::onValidateNameSuccess, ::onFailure)
    }

    private fun onValidateNameSuccess(topAdsGroupValidateNameV2: ResponseGroupValidateName.TopAdsGroupValidateNameV2) {
        autoFillGroupName(topAdsGroupValidateNameV2.data.groupName)
    }

    private fun onFailure() {
        checkForAutoFillGroupName()
    }

    private fun autoFillGroupName(groupName: String) {
        stepperModel?.groupName = groupName
        createEditAdGroupAdapter.updateValue(CreateEditAdGroupItemTag.NAME, groupName)

    }

    private fun addProducts(index: Int) {
        val id = stepperModel?.selectedProductIds?.get(index).toString()
        adsItemsList.add(GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem(id))
    }

    private fun addKeywords(index: Int) {
        val key = KeySharedModel()
        val type = stepperModel?.selectedKeywordStage?.get(index)?.keywordType
        val typeInt = if (type == BROAD_TYPE)
            BROAD_POSITIVE
        else
            EXACT_POSITIVE

        key.id = typeInt.toString()
        key.typeInt = typeInt
        key.name = stepperModel?.selectedKeywordStage?.get(index)?.keyword ?: ""
        if ((stepperModel?.selectedKeywordStage?.get(index)?.bidSuggest?.toDouble() ?: 0.0) != 0.0)
            key.priceBid = stepperModel?.selectedKeywordStage?.get(index)?.bidSuggest
                ?: Int.ZERO.toString()
        else
            key.priceBid = stepperModel?.minSuggestBidKeyword ?: "0"
        keywordsList.add(key)
    }

    private fun onSuccessActivation(groupId: String) {
        binding?.createLoading?.hide()
        if(isAutoPsWhitelisted){
            val source = activity?.intent?.extras?.getString(TopAdsCommonConstant.SOURCE_PACKAGE)
            val intent = Intent(context, TopAdsGroupDetailViewActivity::class.java)
            intent.putExtra(TopAdsCommonConstant.GROUP_ID_PARAM, groupId)
            if(!source.isNullOrEmpty()){
                intent.putExtra(TopAdsCommonConstant.SOURCE_PACKAGE,source)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        } else {
            viewModel?.getTopAdsDeposit(this::onSuccess, this::errorResponse)
        }
    }

    private fun onSuccess(data: DepositAmount) {
        val isEnoughDeposit = data.amount > Int.ZERO
        if (isEnoughDeposit) {
            val sheet = TopAdsSuccessSheet()
            sheet.overlayClickDismiss = false
            sheet.show(childFragmentManager)
        } else {
            val sheet = TopAdsOutofCreditSheet()
            sheet.overlayClickDismiss = false
            sheet.show(childFragmentManager)
        }
    }

    private fun errorResponse(throwable: Throwable) {
        SnackbarManager.make(
            activity,
            throwable.message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun onErrorActivation(error: String?) {
        val message = Utils.getErrorMessage(context, error ?: "")
        view?.let {
            Toaster.build(
                it, message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(topadscommonR.string.topads_common_text_ok)
            ).show()
        }
        binding?.createLoading?.hide()
        binding?.createProductAdButton?.isEnabled = true
    }
}
