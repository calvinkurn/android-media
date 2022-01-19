package com.tokopedia.digital_product_detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpPulsaBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPPulsaActivity
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPPulsaViewModel
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_component.widget.RechargeClientNumberWidget
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.math.abs

/**
 * @author by firmanda on 04/01/21
 */

class DigitalPDPPulsaFragment : BaseDaggerFragment()  {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPPulsaViewModel

    private var binding by autoClearedNullable<FragmentDigitalPdpPulsaBinding>()

    private var dynamicSpacerHeightRes = R.dimen.dynamic_banner_space
    private var operatorData: TelcoCatalogPrefixSelect = TelcoCatalogPrefixSelect(
        RechargeCatalogPrefixSelect())

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPPulsaViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDigitalPdpPulsaBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClientNumberWidget()
        setAnimationAppBarLayout()
        getCatalogMenuDetail()
        getPrefixOperatorData()
        observeData()
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
                if (rechargePdpPulsaClientNumberWidget.getInputNumber()
                        .length in MINIMUM_VALID_NUMBER_LENGTH .. MAXIMUM_VALID_NUMBER_LENGTH) {

                    rechargePdpPulsaClientNumberWidget.run {
                        showOperatorIcon(selectedOperator.operator.attributes.imageUrl)
                    }
                    showDenomGrid()
                    showRecommendation()
                    showMCCM()
                    showTicker()
                }

                // [Misael] add checkoutPassData and update checkoutPassData with new input number
            }
        }
    }

    private fun observeData() {
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
    }

    private fun getCatalogMenuDetail() {
        getFavoriteNumber()
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
            }
        }
    }

    private fun onSuccessGetPrefixOperator(operatorList: TelcoCatalogPrefixSelect) {
        this.operatorData = operatorList
        renderProduct()
    }

    private fun onFailedGetFavoriteNumber() {

    }

    private fun onFailedGetPrefixOperator() {

    }

    private fun initClientNumberWidget() {
        binding?.rechargePdpPulsaClientNumberWidget?.run {

            setInputFieldStaticLabel(getString(
                com.tokopedia.recharge_component.R.string.label_recharge_client_number))
            setInputFieldType(RechargeClientNumberWidget.InputFieldType.Telco)
            setInputNumberValidator { true }
            setListener(
                inputFieldListener = object: RechargeClientNumberWidget.ClientNumberInputFieldListener {
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
                },
                autoCompleteListener = object: RechargeClientNumberWidget.ClientNumberAutoCompleteListener {
                    override fun onClickAutoComplete(isFavoriteContact: Boolean) {
                        // do nothing
                    }
                },
                filterChipListener = object: RechargeClientNumberWidget.ClientNumberFilterChipListener {
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

    private fun showDenomGrid(){
        binding?.let {
            it.rechargePdpPulsaDenomGridWidget.renderDenomGridLayout(denomGridListener = object:
                RechargeDenomGridListener {
                override fun onDenomGridClicked(denomGrid: DenomData, position: Int) {
                    // TODO("Not yet implemented")
                }
            }, DenomWidgetModel(
                mainTitle = "Diskon Rp15.000 buat pengguna baru, nih!",
                listDenomData = listOf(
                    DenomData(
                        title="15 ribu",
                        specialLabel = "Any campaign label",
                        price = "Rp500",
                        slashPrice = "Rp16.500",
                    ),
                    DenomData(
                        title="15 ribu",
                        specialLabel = "Any campaign label",
                        price = "Rp500",
                        discountLabel = "10%",
                        slashPrice = "Rp16.500",
                        appLink = "tokopedia://deals",
                        expiredDate = "December 2021",
                        flashSaleLabel = "Segera Habis",
                        flashSalePercentage = 80
                    ),
                    DenomData(
                        title="50 ribu",
                        price = "Rp35.500",
                    ),
                    DenomData(
                        title="50 ribu",
                        price = "Rp35.500",
                        slashPrice = "75.000"
                    ),
                    DenomData(
                        title="100 ribu",
                        price = "Rp85.500",
                        slashPrice = "105.000"
                    ),
                    DenomData(
                        title="50 ribu",
                        specialLabel = "Any campaign label",
                        price = "Rp35.500",
                    ),
                    DenomData(
                        title="50 ribu",
                        price = "Rp35.500",
                    ),
                    DenomData(
                        title="50 ribu",
                        price = "Rp35.500",
                    ),
                    DenomData(
                        title="15 ribu",
                        //specialLabel = "Any campaign label",
                        specialLabel = "Any campaign label",
                        price = "Rp500",
//                        discountLabel = "10%",
                        discountLabel = "10%",
                        slashPrice = "Rp16.500",
                        appLink = "tokopedia://deals",
                        expiredDate = "December 2021",
                        flashSaleLabel = "Segera Habis",
                        flashSalePercentage = 80
                    ),
                    DenomData(
                        title="50 ribu",
                        price = "Rp35.500",
                        slashPrice = "Rp16.500",
//                        appLink = "tokopedia://deals",
//                        expiredDate = "December 2021",
//                        flashSaleLabel = "Segera Habis",
//                        flashSalePercentage = 80
                    )
                )
            ))
        }
    }

    fun showRecommendation() {
        binding?.let {
            it.rechargePdpPulsaRecommendationWidget.renderRecommendationLayout(recommendationListener = object :
                RechargeRecommendationCardListener {
                    override fun onProductRecommendationCardClicked(applinkUrl: String) {
                        context?.let {
    //                        RouteManager.route(it, applinkUrl)
                        }
                    }
                },
                "Paling sering kamu beli",
                listOf(
                    RecommendationCardWidgetModel(
                        RecommendationCardEnum.SMALL,
                        "https://ecs7.tokopedia.net/img/attachment/2021/11/18/59205941/59205941_4206fd77-877d-46aa-a4f7-3ddb752da681.png",
                        "Token Listrik 100ribu",
                        "Rp101.500",
                        "tokopedia://deals"
                    ),
                    RecommendationCardWidgetModel(
                        RecommendationCardEnum.SMALL,
                        "https://ecs7.tokopedia.net/img/attachment/2021/11/18/59205941/59205941_4206fd77-877d-46aa-a4f7-3ddb752da681.png",
                        "Token Listrik 20 ribu",
                        "Rp20.500",
                        "tokopedia://deals"
                    ),
                    RecommendationCardWidgetModel(
                        RecommendationCardEnum.SMALL,
                        "https://ecs7.tokopedia.net/img/attachment/2021/11/18/59205941/59205941_4206fd77-877d-46aa-a4f7-3ddb752da681.png",
                        "Token Listrik 30 ribu",
                        "Rp30.500",
                        "tokopedia://deals"
                    )
                )
            )
        }
    }

    private fun showMCCM(){
        binding?.let {
            it.rechargePdpPulsaPromoWidget.renderMCCMGrid(
                denomGridListener = object: RechargeDenomGridListener{
                    override fun onDenomGridClicked(denomGrid: DenomData, position: Int) {
                        // TODO("Not yet implemented")
                    }
                }, DenomWidgetModel(
                    mainTitle = "Diskon Rp15.000 buat pengguna baru, nih!",
                    listDenomData = listOf(
                        DenomData(
                            title="15 ribu",
                            specialLabel = "Any campaign label",
                            price = "Rp500",
                            discountLabel = "10%",
                            slashPrice = "Rp16.500",
                            appLink = "tokopedia://deals",
                        ),
                        DenomData(
                            title="15 ribu",
                            specialLabel = "Any campaign label",
                            price = "Rp500",
                            discountLabel = "10%",
                            slashPrice = "Rp16.500",
                            appLink = "tokopedia://deals",
                        ),
                        DenomData(
                            title="15 ribu",
                            specialLabel = "Any campaign label",
                            price = "Rp500",
                            discountLabel = "10%",
                            slashPrice = "Rp16.500",
                            appLink = "tokopedia://deals",
                        ),
                        DenomData(
                            title="15 ribu",
                            specialLabel = "Any campaign label",
                            price = "Rp500",
                            discountLabel = "10%",
                            slashPrice = "Rp16.500",
                            appLink = "tokopedia://deals",
                        ),
                        DenomData(
                            title="15 ribu",
                            specialLabel = "Any campaign label",
                            price = "Rp500",
                            discountLabel = "10%",
                            slashPrice = "Rp16.500",
                            appLink = "tokopedia://deals",
                        ),
                        DenomData(
                            title="15 ribu",
                            specialLabel = "Any campaign label",
                            price = "Rp500",
                            discountLabel = "10%",
                            slashPrice = "Rp16.500",
                            appLink = "tokopedia://deals",
                        )
                    )
                )
            )
        }
    }

    private fun showTicker() {
        binding?.rechargePdpPulsaTickerWidget?.run {
            setText("Transaksi selama <b>23:40-00:20 WIB</b> baru akan diproses pada <b>00:45 WIB</b>. <a href=\"\">Selengkapnya</a>")
            show()
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
                            && binding?.rechargePdpPulsaClientNumberWidget?.getInputNumber()?.isNotEmpty() == true
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