package com.tokopedia.digital_product_detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsTicker
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpPulsaBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPPulsaActivity
import com.tokopedia.digital_product_detail.presentation.bottomsheet.SummaryPulsaBottomsheet
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPPulsaViewModel
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.denom.MenuDetailModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_component.widget.RechargeClientNumberWidget
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.math.abs

/**
 * @author by firmanda on 04/01/21
 */

class DigitalPDPPulsaFragment : BaseDaggerFragment(),
    RechargeDenomGridListener,
    RechargeBuyWidgetListener
{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPPulsaViewModel

    private var binding by autoClearedNullable<FragmentDigitalPdpPulsaBinding>()

    private var dynamicSpacerHeightRes = R.dimen.dynamic_banner_space
    private var operatorData: TelcoCatalogPrefixSelect = TelcoCatalogPrefixSelect(
        RechargeCatalogPrefixSelect()
    )

    private var operatorId = ""

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPPulsaViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDigitalPdpPulsaBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClientNumberWidget()
        initEmptyState()
        setAnimationAppBarLayout()
        observeData()

        getCatalogMenuDetail()
        getFavoriteNumber()
        getPrefixOperatorData()
    }

    private fun renderProduct() {
        binding?.run {
            if (rechargePdpPulsaClientNumberWidget.getInputNumber().length >= MINIMUM_OPERATOR_PREFIX) {

                /* operator check */
                val selectedOperator =
                    operatorData.rechargeCatalogPrefixSelect.prefixes.single {
                        rechargePdpPulsaClientNumberWidget.getInputNumber().startsWith(it.value)
                    }

                // [Misael] operatorId state & checker
                if (operatorId != selectedOperator.operator.id || rechargePdpPulsaClientNumberWidget.getInputNumber()
                        .length in MINIMUM_VALID_NUMBER_LENGTH .. MAXIMUM_VALID_NUMBER_LENGTH
                ) {
                    operatorId = selectedOperator.operator.id
                    rechargePdpPulsaClientNumberWidget.run {
                        showOperatorIcon(selectedOperator.operator.attributes.imageUrl)
                    }
                    hideEmptyState()
                    getCatalogProductInput(selectedOperator.key)
                } else {
                    onHideBuyWidget()
                    showEmptyState()
                }

                // [Misael] add checkoutPassData and update checkoutPassData with new input number
            }
        }
    }

    private fun observeData() {
        viewModel.menuDetailData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetMenuDetail(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetMenuDetail()
                is RechargeNetworkResult.Loading -> {}
            }
        })
        viewModel.favoriteNumberData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetFavoriteNumber(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetFavoriteNumber()
                is RechargeNetworkResult.Loading -> {}
            }
        })

        viewModel.catalogPrefixSelect.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> onSuccessGetPrefixOperator(it.data)
                is RechargeNetworkResult.Fail -> onFailedGetPrefixOperator()
                is RechargeNetworkResult.Loading -> {}
            }
        })


        viewModel.observableDenomData.observe(viewLifecycleOwner, { denomData ->
            when (denomData) {
                is RechargeNetworkResult.Success -> {
                    onSuccessDenomGrid(denomData.data)
                }

                is RechargeNetworkResult.Fail -> {
                    view?.let {
                        onFailedDenomGrid()
                        //TODO add fail
                    }
                }

                is RechargeNetworkResult.Loading -> {
                    onShimmeringDenomGrid()
                }
            }
        })

        viewModel.observableMCCMData.observe(viewLifecycleOwner, { mccmData ->
            when (mccmData) {
                is RechargeNetworkResult.Success -> {
                    onSuccessMCCM(mccmData.data)
                }
            }
        })
    }

    private fun getCatalogProductInput(selectedOperatorKey: String) {
        viewModel.getRechargeCatalogInput(MENU_ID, selectedOperatorKey)
    }

    private fun getCatalogMenuDetail() {
        viewModel.getMenuDetail(MENU_ID)
    }

    private fun getPrefixOperatorData() {
        viewModel.getPrefixOperator(MENU_ID)
    }

    private fun getFavoriteNumber(
        categoryId: String = TelcoCategoryType.CATEGORY_PULSA.toString(),
        shouldRefreshInputNumber: Boolean = true
    ) {
        viewModel.getFavoriteNumber(listOf(categoryId))
    }

    private fun onSuccessGetMenuDetail(data: MenuDetailModel) {
        (activity as BaseSimpleActivity).updateTitle(data.catalog.label)
        renderRecommendation(data.recommendations)
        renderTicker(data.tickers)
    }

    private fun onSuccessGetFavoriteNumber(favoriteNumber: List<TopupBillsSeamlessFavNumberItem>) {
        binding?.rechargePdpPulsaClientNumberWidget?.run {
            if (favoriteNumber.isNotEmpty()) {
                // -- start -- TODO: Add shouldRefreshinputNumber
                setInputNumber(favoriteNumber[0].clientNumber)
                setContactName(favoriteNumber[0].clientName)
                // -- end --
                setFilterChipShimmer(false, favoriteNumber.isEmpty())
                setFavoriteNumber(favoriteNumber)
                setAutoCompleteList(favoriteNumber)
                dynamicSpacerHeightRes = R.dimen.dynamic_banner_space_extended
            } else {
                showEmptyState()
            }
        }
    }

    private fun onSuccessGetPrefixOperator(operatorList: TelcoCatalogPrefixSelect) {
        this.operatorData = operatorList
        renderProduct()
    }

    private fun onFailedGetMenuDetail() {

    }

    private fun onFailedGetFavoriteNumber() {

    }

    private fun onFailedGetPrefixOperator() {

    }

    private fun initClientNumberWidget() {
        binding?.rechargePdpPulsaClientNumberWidget?.run {

            setInputFieldStaticLabel(
                getString(
                    com.tokopedia.recharge_component.R.string.label_recharge_client_number
                )
            )
            setInputFieldType(RechargeClientNumberWidget.InputFieldType.Telco)
            setInputNumberValidator { true }
            setListener(
                inputFieldListener = object :
                    RechargeClientNumberWidget.ClientNumberInputFieldListener {
                    override fun onRenderOperator(isDelayed: Boolean) {
                        binding?.rechargePdpPulsaClientNumberWidget?.setLoading(true)
                        operatorData.rechargeCatalogPrefixSelect.prefixes.isEmpty().let {
                            if (it) {
                                getPrefixOperatorData()
                            } else {
                                renderProduct()
                            }
                            binding?.rechargePdpPulsaClientNumberWidget?.setLoading(false)
                        }
                    }

                    override fun onClearInput() {
                        operatorId = ""
                        showEmptyState()
                    }
                },
                autoCompleteListener = object :
                    RechargeClientNumberWidget.ClientNumberAutoCompleteListener {
                    override fun onClickAutoComplete(isFavoriteContact: Boolean) {
                        // do nothing
                    }
                },
                filterChipListener = object :
                    RechargeClientNumberWidget.ClientNumberFilterChipListener {
                    override fun onShowFilterChip(isLabeled: Boolean) {
                        // do nothing
                    }

                    override fun onClickFilterChip(isLabeled: Boolean) {
                        // do nothing
                    }

                    override fun onClickIcon() {
                        // do nothing
                    }
                }
            )
        }
    }

    private fun initEmptyState() {
        // [Misael] replace with catalogMenuDetail.banners
        binding?.rechargePdpPulsaEmptyStateWidget?.setImageUrl(
            "https://images.tokopedia.net/img/ULHhFV/2022/1/7/8324919c-fa15-46d9-84f7-426adb6994e0.jpg"
        )
    }

    private fun onSuccessDenomGrid(denomData: DenomWidgetModel) {
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.renderDenomGridLayout(this, denomData)
            it.rechargePdpPulsaDenomGridWidget.show()
        }
    }

    private fun onFailedDenomGrid() {
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.renderFailDenomGrid()
        }
    }

    private fun onShimmeringDenomGrid() {
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.renderDenomGridShimmering()
        }
    }

    private fun onClearSelectedDenomGrid(){
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.clearSelectedProduct()
        }
    }

    fun renderRecommendation(recommendations: List<RecommendationCardWidgetModel>) {
        binding?.let {
            it.rechargePdpPulsaRecommendationWidget.show()
            it.rechargePdpPulsaRecommendationWidget.renderRecommendationLayout(recommendationListener = object :
                RechargeRecommendationCardListener {
                    override fun onProductRecommendationCardClicked(applinkUrl: String) {
                        context?.let {
                            //                        RouteManager.route(it, applinkUrl)
                        }
                    }
                },
                getString(R.string.digital_pdp_recommendation_title),
                recommendations
            )
        }
    }

    private fun onSuccessMCCM(denomGrid: DenomWidgetModel) {
        binding?.let {
            it.rechargePdpPulsaPromoWidget.show()
            it.rechargePdpPulsaPromoWidget.renderMCCMGrid(this, denomGrid, getString(com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
    }

    private fun onClearSelectedMCCM(){
        binding?.let {
            it.rechargePdpPulsaPromoWidget.clearSelectedProduct()
        }
    }

    private fun onShowBuyWidget(denomGrid: DenomData){
        binding?.let {
            it.rechargePdpPulsaBuyWidget.showBuyWidget(denomGrid, this)
        }
    }

    private fun onHideBuyWidget(){
        binding?.let {
            it.rechargePdpPulsaBuyWidget.hideBuyWidget()
        }
    }

    private fun renderTicker(tickers: List<TopupBillsTicker>) {
        if (tickers.isNotEmpty()) {
            val messages = ArrayList<TickerData>()
            for (item in tickers) {
                messages.add(
                    TickerData(
                        item.name, item.content,
                        when (item.type) {
                            "warning" -> Ticker.TYPE_WARNING
                            "info" -> Ticker.TYPE_INFORMATION
                            "success" -> Ticker.TYPE_ANNOUNCEMENT
                            "error" -> Ticker.TYPE_ERROR
                            else -> Ticker.TYPE_INFORMATION
                        }
                    )
                )
            }
            binding?.rechargePdpPulsaTicker?.run {
                addPagerView(TickerPagerAdapter(
                    this@DigitalPDPPulsaFragment.requireContext(), messages), messages)
                show()
            }
        } else {
            binding?.rechargePdpPulsaTicker?.hide()
        }
    }

    private fun showEmptyState() {
        binding?.run {
            if (!rechargePdpPulsaEmptyStateWidget.isVisible) {
                rechargePdpPulsaEmptyStateWidget.show()
                rechargePdpPulsaPromoWidget.hide()
                rechargePdpPulsaRecommendationWidget.hide()
                rechargePdpPulsaDenomGridWidget.hide()
            }
        }
    }

    private fun hideEmptyState() {
        binding?.run {
            if (rechargePdpPulsaEmptyStateWidget.isVisible) {
                rechargePdpPulsaEmptyStateWidget.hide()
                rechargePdpPulsaRecommendationWidget.show()
            }
        }
    }

    private fun setAnimationAppBarLayout() {
        //initial appBar state is expanded
        (activity as? DigitalPDPPulsaActivity)?.setupAppBar()

        binding?.rechargePdpPulsaAppbar?.run {
            addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                var lastOffset = -1
                var lastIsCollapsed = false

                override fun onOffsetChanged(p0: AppBarLayout?, verticalOffSet: Int) {
                    if (lastOffset == verticalOffSet) return

                    lastOffset = verticalOffSet
                    if (abs(verticalOffSet) >= totalScrollRange && !lastIsCollapsed) {
                        if (binding?.rechargePdpPulsaClientNumberWidget?.isErrorMessageShown() == false
                            && binding?.rechargePdpPulsaClientNumberWidget?.getInputNumber()
                                ?.isNotEmpty() == true
                        ) {
                            //Collapsed
                            lastIsCollapsed = true
                            onCollapseAppBar()
                        }
                    } else if (verticalOffSet == 0 && lastIsCollapsed) {
                        //Expanded
                        lastIsCollapsed = false
                        onExpandAppBar()
                    }
                }
            })
        }
    }

    private fun showDynamicSpacer() {
        binding?.rechargePdpPulsaDynamicBannerSpacer?.layoutParams?.height =
            context?.resources?.getDimensionPixelSize(dynamicSpacerHeightRes)
                ?: DEFAULT_SPACE_HEIGHT
        binding?.rechargePdpPulsaDynamicBannerSpacer?.requestLayout()
    }

    private fun hideDynamicSpacer() {
        binding?.rechargePdpPulsaDynamicBannerSpacer?.layoutParams?.height = 0
        binding?.rechargePdpPulsaDynamicBannerSpacer?.requestLayout()
    }

    fun onCollapseAppBar() {
        binding?.run {
            rechargePdpPulsaClientNumberWidget.setVisibleSimplifiedLayout(true)
            showDynamicSpacer()
        }
    }

    fun onExpandAppBar() {
        binding?.run {
            rechargePdpPulsaClientNumberWidget.setVisibleSimplifiedLayout(false)
            hideDynamicSpacer()
        }
    }

    /**
     * RechargeDenomGridListener
     */
    override fun onDenomGridClicked(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int,
                                    isShowBuyWidget: Boolean) {
        if (layoutType == DenomWidgetEnum.MCCM_TYPE){
            onClearSelectedDenomGrid()
        } else if (layoutType == DenomWidgetEnum.GRID_TYPE){
            onClearSelectedMCCM()
        }

        if (isShowBuyWidget) {
            onShowBuyWidget(denomGrid)
        } else {
            onHideBuyWidget()
        }
    }

    override fun onClickedButtonLanjutkan(denom: DenomData) {

    }

    override fun onClickedChevron(denom: DenomData) {
        fragmentManager?.let {
            SummaryPulsaBottomsheet(getString(R.string.summary_transaction), denom).show(it, "")
        }
    }


    companion object {
        fun newInstance() = DigitalPDPPulsaFragment()

        const val MENU_ID = 148
        const val MINIMUM_OPERATOR_PREFIX = 4
        const val MINIMUM_VALID_NUMBER_LENGTH = 10
        const val MAXIMUM_VALID_NUMBER_LENGTH = 14

        const val FADE_IN_DURATION: Long = 300
        const val FADE_OUT_DURATION: Long = 300

        const val DEFAULT_SPACE_HEIGHT = 81
    }
}