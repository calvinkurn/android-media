package com.tokopedia.purchase_platform.features.promo.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.di.DaggerPromoCheckoutMarketplaceComponent
import com.tokopedia.purchase_platform.features.promo.presentation.PromoDecoration
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapter
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutMarketplaceAdapterTypeFactory
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckout
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckoutListener
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutMarketplaceActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel.UiData.Companion.PROMO_TYPE_GLOBAL
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel.UiData.Companion.PROMO_TYPE_MERCHANT_OFFICIAL
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoListHeaderUiModel.UiData.Companion.PROMO_TYPE_POWER_MERCHANT
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
        val promoRecommendationUiModel = PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply {
                    title = "Title aaaaaa"
                    subTitle = "Sub title aaaa"
                },
                uiState = PromoRecommendationUiModel.UiState().apply {
                    isButtonSelectEnabled = true
                }
        )
        adapter.addVisitable(promoRecommendationUiModel)

        val promoInputUiModel = PromoInputUiModel(
                uiData = PromoInputUiModel.UiData().apply {
                    promoCode = ""
                },
                uiState = PromoInputUiModel.UiState().apply {
                    isButtonSelectEnabled = false
                }
        )
        adapter.addVisitable(promoInputUiModel)

        val promoEligibleHeaderUiModel = PromoEligibleHeaderUiModel(
                uiData = PromoEligibleHeaderUiModel.UiData().apply {
                    title = "Kamu bisa bisa bisa pakai promo"
                    subTitle = "Pilih salah satu aja"
                }
        )
        adapter.addVisitable(promoEligibleHeaderUiModel)

        val promoListHeaderUiModel = PromoListHeaderUiModel(
                uiData = PromoListHeaderUiModel.UiData().apply {
                    title = "Kupon saya global"
                    subTitle = "Hanya bisa pilih 1"
                    promoType = PROMO_TYPE_GLOBAL
                    identifierId = 1
                },
                uiState = PromoListHeaderUiModel.UiState().apply {
                    isCollapsed = false
                }
        )
        adapter.addVisitable(promoListHeaderUiModel)

        val promoListItemUiModel = PromoListItemUiModel(
                uiData = PromoListItemUiModel.UiData().apply {
                    promoId = 0
                    parentIdentifierId = 1
                    title = "Promo pertama"
                    subTitle = "Berakhir 1 jam lagi"
                    imageResourceUrl = "https://cdn2.tstatic.net/jatim/foto/bank/images/cara-isi-ulang-saldo-ovo.jpg"
                },
                uiState = PromoListItemUiModel.UiState().apply {
                    isEnabled = true
                }
        )
        adapter.addVisitable(promoListItemUiModel)

        val promoListItemUiModel1 = PromoListItemUiModel(
                uiData = PromoListItemUiModel.UiData().apply {
                    promoId = 1
                    parentIdentifierId = 1
                    title = "Promo kedua"
                    subTitle = "Berakhir 2 jam lagi"
                    errorMessage = "Kena Error"
                },
                uiState = PromoListItemUiModel.UiState().apply {
                    isEnabled = false
                }
        )
        adapter.addVisitable(promoListItemUiModel1)

        val promoListItemUiModel2 = PromoListItemUiModel(
                uiData = PromoListItemUiModel.UiData().apply {
                    promoId = 2
                    parentIdentifierId = 1
                    title = "Promo ketiga"
                    subTitle = "Berakhir 3 jam lagi"
                },
                uiState = PromoListItemUiModel.UiState().apply {
                    isEnabled = true
                }
        )
        adapter.addVisitable(promoListItemUiModel2)

        val promoListHeaderUiModel1 = PromoListHeaderUiModel(
                uiData = PromoListHeaderUiModel.UiData().apply {
                    title = "Ini promo power merchant"
                    subTitle = "Hanya bisa pilih 1"
                    promoType = PROMO_TYPE_POWER_MERCHANT
                },
                uiState = PromoListHeaderUiModel.UiState().apply {
                    isCollapsed = true
                }
        )
        adapter.addVisitable(promoListHeaderUiModel1)

        val promoListItemUiModel3 = PromoListItemUiModel(
                uiData = PromoListItemUiModel.UiData().apply {
                    promoId = 3
                    parentIdentifierId = 2
                },
                uiState = PromoListItemUiModel.UiState().apply {
                    isEnabled = true
                }
        )
        adapter.addVisitable(promoListItemUiModel3)

        val promoListHeaderUiModel2 = PromoListHeaderUiModel(
                uiData = PromoListHeaderUiModel.UiData().apply {
                    title = "Ini promo official store"
                    subTitle = "Hanya bisa pilih 1"
                    promoType = PROMO_TYPE_MERCHANT_OFFICIAL
                },
                uiState = PromoListHeaderUiModel.UiState().apply {
                    isCollapsed = true
                }
        )
        adapter.addVisitable(promoListHeaderUiModel2)

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

    override fun onClickApplyRecommendedPromo() {
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

    // -- END OF PROMO LIST SECTION
}