package com.tokopedia.purchase_platform.features.promo.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.data.*
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.di.DaggerPromoCheckoutMarketplaceComponent
import com.tokopedia.purchase_platform.features.promo.presentation.*
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapter
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutMarketplaceAdapterTypeFactory
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckout
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckoutListener
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutMarketplaceActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import kotlinx.android.synthetic.main.fragment_promo_checkout_marketplace.*
import javax.inject.Inject

class PromoCheckoutMarketplaceFragment : BaseListFragment<Visitable<*>, PromoCheckoutMarketplaceAdapterTypeFactory>(),
        PromoCheckoutMarketplaceActionListener, ToolbarPromoCheckoutListener {

    @Inject
    lateinit var itemDecorator: PromoDecoration

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PromoCheckoutAdapter
    private lateinit var fragmentUiModel: FragmentUiModel

    private var toolbar: ToolbarPromoCheckout? = null

    override fun initInjector() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerPromoCheckoutMarketplaceComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_promo_checkout_marketplace, container, false)
        recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(itemDecorator)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(view)
        button_apply_promo.setOnClickListener {
            // Todo : add action to hit API
        }

        initFragmentUiModel()
        checkHasPromoSellected()
        renderFragmentState()
    }

    private fun initFragmentUiModel() {
        fragmentUiModel = FragmentUiModel(
                uiState = FragmentUiModel.UiState().apply {
                    hasAnyPromoSellected = false
                    hasFailedToLoad = false
                }
        )
    }

    private fun renderFragmentState() {
        if (fragmentUiModel.uiState.hasAnyPromoSellected) {
            toolbar?.enableResetButton()
        } else {
            toolbar?.disableResetButton()
        }
    }

    private fun setupToolbar(view: View) {
        activity?.let {
            val appbar = view.findViewById<Toolbar>(R.id.toolbar)
            appbar.removeAllViews()
            toolbar = toolbarPromoCheckout()
            toolbar?.let {
                appbar.addView(toolbar)
                (activity as AppCompatActivity).setSupportActionBar(appbar)
            }
        }
    }

    private fun toolbarPromoCheckout(): ToolbarPromoCheckout? {
        activity?.let {
            return ToolbarPromoCheckout(it).apply {
                listener = this@PromoCheckoutMarketplaceFragment
            }
        }

        return null
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, PromoCheckoutMarketplaceAdapterTypeFactory> {
        adapter = PromoCheckoutAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getAdapterTypeFactory(): PromoCheckoutMarketplaceAdapterTypeFactory {
        return PromoCheckoutMarketplaceAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun getScreenName(): String {
        return ""
    }

    override fun loadData(page: Int) {
        hideLoading()
        adapter.addVisitable(mockPromoRecommendation())
        adapter.addVisitable(mockPromoInput())

        adapter.addVisitable(mockEligibleHeader())
        adapter.addVisitableList(mockEligiblePromoGlobalSection())
        adapter.addVisitableList(mockEligiblePromoGoldMerchantSection())
        adapter.addVisitableList(mockEligiblePromoOfficialStoreSection())

        adapter.addVisitable(mockIneligibleHeader())
        adapter.addVisitableList(mockIneligiblePromoGlobalSection())
        adapter.addVisitableList(mockIneligiblePromoGoldMerchantSection())
        adapter.addVisitableList(mockIneligiblePromoOfficialStoreSection())
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    // --- FRAGMENT LEVEL ACTION

    override fun onClickResetPromo() {
        val promoList = HashMap<Int, Visitable<*>>()
        adapter.data.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSellected) {
                val newData = PromoListItemUiModel.clone(it).apply {
                    uiState.isSellected = false
                }
                promoList[adapter.data.indexOf(it)] = newData
            } else if (it is PromoListHeaderUiModel) {
                val newData = PromoListHeaderUiModel.clone(it).apply {
                    uiData.subTitle = "Hanya bisa pilih 1"
                    it.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                        promoListItemUiModel.uiState.isSellected = false
                    }
                }
                promoList[adapter.data.indexOf(it)] = newData
            }
        }
        adapter.modifyDataList(promoList)

        fragmentUiModel.uiState.hasAnyPromoSellected = false
        renderFragmentState()
    }

    // --- END OF FRAGMENT LEVEL ACTION


    // --- RECOMMENDATION SECTION

    override fun onClickApplyRecommendedPromo(element: PromoRecommendationUiModel) {
        val newData = PromoRecommendationUiModel.clone(element).apply {
            uiState.isButtonSelectEnabled = false
        }
        adapter.modifyData(adapter.data.indexOf(element), newData)

        // Todo : get recommendation promo from current list
        // Todo : hit API
        // Todo : if success >> update recommendation layout; if failed >> show toast error
    }

    // --- END OF RECOMMENDATION SECTION


    // --- PROMO LIST SECTION

    override fun onClickApplyManualInputPromo(promoCode: String) {

    }

    override fun onClickPromoListHeader(itemPosition: Int) {
        if (itemPosition < adapter.data.size) {
            val oldData = adapter.data[itemPosition]
            if (oldData is PromoListHeaderUiModel) {
                if (!oldData.uiState.isCollapsed) {
                    val newData = PromoListHeaderUiModel.clone(oldData).apply {
                        uiState.isCollapsed = !oldData.uiState.isCollapsed
                    }
                    adapter.modifyData(itemPosition, newData)

                    val modifiedData = ArrayList<PromoListItemUiModel>()
                    val startIndex = itemPosition + 1
                    for (index in startIndex until adapter.data.size) {
                        if (adapter.data[index] !is PromoListItemUiModel) break
                        val oldPromoItem = adapter.data[index] as PromoListItemUiModel
                        modifiedData.add(oldPromoItem)
                    }
                    newData.uiData.tmpPromoItemList = modifiedData
                    adapter.removeList(modifiedData)
                } else {
                    val newData = PromoListHeaderUiModel.clone(oldData).apply {
                        uiData.tmpPromoItemList = emptyList()
                        uiState.isCollapsed = !oldData.uiState.isCollapsed
                    }
                    adapter.modifyData(itemPosition, newData)
                    adapter.addVisitableList(itemPosition + 1, oldData.uiData.tmpPromoItemList)
                }
            }
        }
    }

    override fun onClickPromoListItem(element: PromoListItemUiModel) {
        // Update header sub total
        var oldData: PromoListHeaderUiModel? = null
        adapter.data.forEach {
            if (it is PromoListHeaderUiModel && it.uiData.identifierId == element.uiData.parentIdentifierId) {
                oldData = it
                return@forEach
            }
        }
        oldData?.let {
            val hasSelectPromo = checkHasPromoSellected(it.uiData.identifierId)
            val newData = PromoListHeaderUiModel.clone(it).apply {
                if (hasSelectPromo) {
                    uiData.subTitle = "Promo dipilih"
                } else {
                    uiData.subTitle = "Hanya bisa pilih 1"
                }
            }
            renderFragmentState()
            val headerIndex = adapter.data.indexOf(oldData)
            adapter.modifyData(headerIndex, newData)

            // Un check other
            var unCheckIndex = 0
            var newPromoItemData: PromoListItemUiModel? = null
            for (index in headerIndex + 1 until adapter.data.size) {
                if (adapter.data[index] !is PromoListItemUiModel) {
                    break
                } else {
                    val promoItem = adapter.data[index] as PromoListItemUiModel
                    if (promoItem.uiData.promoId != element.uiData.promoId && promoItem.uiState.isSellected) {
                        newPromoItemData = PromoListItemUiModel.clone(promoItem).apply {
                            uiState.isSellected = false
                        }
                        unCheckIndex = index
                        break
                    }
                }
            }
            newPromoItemData?.let { promoListItemUiModel ->
                adapter.modifyData(unCheckIndex, promoListItemUiModel)
            }
        }
    }

    private fun checkHasPromoSellected(): Boolean {
        var hasAnyPromoSellected = false
        adapter.list.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSellected) {
                hasAnyPromoSellected = true
                return@forEach
            }
        }
        fragmentUiModel.uiState.hasAnyPromoSellected = hasAnyPromoSellected

        return hasAnyPromoSellected
    }

    private fun checkHasPromoSellected(parentIdentifierId: Int): Boolean {
        var hasAnyPromoSellected = false
        adapter.list.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSellected && it.uiData.parentIdentifierId == parentIdentifierId) {
                hasAnyPromoSellected = true
                return@forEach
            }
        }
        fragmentUiModel.uiState.hasAnyPromoSellected = hasAnyPromoSellected

        return hasAnyPromoSellected
    }

    override fun onClickPromoItemDetail(element: PromoListItemUiModel) {
        Toast.makeText(context, "Go to detail promo", Toast.LENGTH_SHORT).show()
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE).apply {
            putExtra(EXTRA_KUPON_CODE, "12345")
            putExtra(EXTRA_IS_USE, true)
            putExtra(ONE_CLICK_SHIPMENT, false)
            putExtra(PAGE_TRACKING, FROM_CART)
//            putExtra(CHECK_PROMO_CODE_FIRST_STEP_PARAM, Any())
        }
        startActivity(intent)
    }

    // -- END OF PROMO LIST SECTION
}