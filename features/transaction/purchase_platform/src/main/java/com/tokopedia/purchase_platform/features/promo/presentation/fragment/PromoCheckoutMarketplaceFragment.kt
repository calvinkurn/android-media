package com.tokopedia.purchase_platform.features.promo.presentation.fragment

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.data.EXTRA_IS_USE
import com.tokopedia.promocheckout.common.data.EXTRA_KUPON_CODE
import com.tokopedia.promocheckout.common.data.ONE_CLICK_SHIPMENT
import com.tokopedia.promocheckout.common.data.PAGE_TRACKING
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.di.DaggerPromoCheckoutMarketplaceComponent
import com.tokopedia.purchase_platform.features.promo.presentation.*
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutAdapter
import com.tokopedia.purchase_platform.features.promo.presentation.adapter.PromoCheckoutMarketplaceAdapterTypeFactory
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckout
import com.tokopedia.purchase_platform.features.promo.presentation.compoundview.ToolbarPromoCheckoutListener
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutMarketplaceActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.*
import com.tokopedia.unifycomponents.Toaster
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
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
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
            button_apply_promo.isLoading = !button_apply_promo.isLoading
        }
        button_apply_no_promo.setOnClickListener {
            // Todo : add action to hit API
            button_apply_no_promo.isLoading = !button_apply_promo.isLoading
        }

        initFragmentUiModel()
        checkHasPromoSellected()
        renderFragmentState()
    }

    private fun initFragmentUiModel() {
        fragmentUiModel = FragmentUiModel(
                uiData = FragmentUiModel.UiData().apply {
                    promoInputViewHeight = 0
                },
                uiState = FragmentUiModel.UiState().apply {
                    hasPresellectedPromo = false
                    hasAnyPromoSelected = false
                    hasFailedToLoad = false
                }
        )
    }

    private fun renderFragmentState() {
        if (fragmentUiModel.uiState.hasAnyPromoSelected) {
            toolbar?.enableResetButton()
            label_total_promo_info.show()
            label_total_promo_amount.show()
            button_apply_promo.show()
            button_apply_no_promo.gone()
            container_action_bottom.show()
        } else {
            toolbar?.disableResetButton()
            if (fragmentUiModel.uiState.hasPresellectedPromo) {
                label_total_promo_info.gone()
                label_total_promo_amount.gone()
                button_apply_promo.gone()
                button_apply_no_promo.show()
                container_action_bottom.show()
            } else {
                container_action_bottom.gone()
            }
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
        itemDecorator.adapter = adapter

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

//        adapter.addVisitable(mockEmptyState())
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun updateHeightPromoInputView(height: Int) {
        fragmentUiModel.uiData.promoInputViewHeight = height
    }

    override fun getEmptyStateHeight(): Int {
        if (activity != null) {
            val displayMetrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

            val deviceHeight = displayMetrics.heightPixels
            val appBarHeight = DisplayMetricUtils.getStatusBarHeight(activity)
            var toolbarHeight = 0
            val styledAttributes = activity?.theme?.obtainStyledAttributes(
                    intArrayOf(android.R.attr.actionBarSize)
            )
            toolbarHeight = styledAttributes?.getDimension(0, 0f)?.toInt() ?: 0
            styledAttributes?.recycle()

            val promoInputViewHeight = fragmentUiModel.uiData.promoInputViewHeight
            val heightInPx = deviceHeight - appBarHeight - toolbarHeight - promoInputViewHeight

            return heightInPx / (activity?.resources?.displayMetrics?.density?.toInt() ?: 1)
        } else {
            return 0
        }
    }

    fun showToastMessage(message: String) {
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
        }
    }

    fun showToastMessage(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        showToastMessage(errorMessage)
    }

    // --- FRAGMENT LEVEL ACTION

    private fun showSavePromoDialog() {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle("Simpan promo sebelum keluar?")
                setDescription("Kamu baru saja mengubah pilihan promo. Mau disimpan?")
                setPrimaryCTAText("Simpan Promo Baru")
                setSecondaryCTAText("Keluar Halaman")
                setPrimaryCTAClickListener {
                    // Todo : Hit validate use
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                    it.finish()
                }
            }.show()
        }
    }

    override fun onBackPressed() {
        // Todo : validate if has any changes but have not hit validate use
        showSavePromoDialog()
    }

    override fun onClickResetPromo() {
        val promoList = HashMap<Int, Visitable<*>>()
        adapter.data.forEach {
            if (it is PromoListItemUiModel && it.uiState.isSellected) {
                it.uiState.isSellected = false
                promoList[adapter.data.indexOf(it)] = it
            } else if (it is PromoListHeaderUiModel) {
                it.uiData.subTitle = "Hanya bisa pilih 1"
                it.uiData.tmpPromoItemList.forEach { promoListItemUiModel ->
                    promoListItemUiModel.uiState.isSellected = false
                }
                promoList[adapter.data.indexOf(it)] = it
            }
        }
        adapter.modifyDataList(promoList)

        fragmentUiModel.uiState.hasAnyPromoSelected = false
        renderFragmentState()
    }

    // --- END OF FRAGMENT LEVEL ACTION


    // --- RECOMMENDATION SECTION

    override fun onClickApplyRecommendedPromo(element: PromoRecommendationUiModel) {
        element.uiState.isButtonSelectEnabled = false
        adapter.modifyData(adapter.data.indexOf(element))

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
                    oldData.uiState.isCollapsed = !oldData.uiState.isCollapsed
                    adapter.modifyData(itemPosition)

                    val modifiedData = ArrayList<PromoListItemUiModel>()
                    val startIndex = itemPosition + 1
                    for (index in startIndex until adapter.data.size) {
                        if (adapter.data[index] !is PromoListItemUiModel) break
                        val oldPromoItem = adapter.data[index] as PromoListItemUiModel
                        modifiedData.add(oldPromoItem)
                    }
                    oldData.uiData.tmpPromoItemList = modifiedData
                    adapter.removeList(modifiedData)
                } else {
                    oldData.uiState.isCollapsed = !oldData.uiState.isCollapsed
                    adapter.modifyData(itemPosition)
                    adapter.addVisitableList(itemPosition + 1, oldData.uiData.tmpPromoItemList)
                    oldData.uiData.tmpPromoItemList = emptyList()
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
            if (hasSelectPromo) {
                it.uiData.subTitle = "Promo dipilih"
            } else {
                it.uiData.subTitle = "Hanya bisa pilih 1"
            }
            renderFragmentState()
            val headerIndex = adapter.data.indexOf(oldData)
            adapter.modifyData(headerIndex)

            // Un check other
            var unCheckIndex = 0
            var newPromoItemData: PromoListItemUiModel? = null
            for (index in headerIndex + 1 until adapter.data.size) {
                if (adapter.data[index] !is PromoListItemUiModel) {
                    break
                } else {
                    val promoItem = adapter.data[index] as PromoListItemUiModel
                    if (promoItem.uiData.promoId != element.uiData.promoId && promoItem.uiState.isSellected) {
                        promoItem.uiState.isSellected = false
                        unCheckIndex = index
                        break
                    }
                }
            }
            newPromoItemData?.let {
                adapter.modifyData(unCheckIndex)
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
        fragmentUiModel.uiState.hasAnyPromoSelected = hasAnyPromoSellected

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
        fragmentUiModel.uiState.hasAnyPromoSelected = hasAnyPromoSellected

        return hasAnyPromoSellected
    }

    override fun onClickPromoItemDetail(element: PromoListItemUiModel) {
        Toast.makeText(context, "Go to detail promo", Toast.LENGTH_SHORT).show()
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE).apply {
            putExtra(EXTRA_KUPON_CODE, "FONSBBLIF29DDV9L51M")
            putExtra(EXTRA_IS_USE, true)
            putExtra(ONE_CLICK_SHIPMENT, false)
            putExtra(PAGE_TRACKING, FROM_CART)
//            putExtra(CHECK_PROMO_CODE_FIRST_STEP_PARAM, Any())
        }
        startActivity(intent)
    }

    override fun onClickPromoEligibilityHeader(position: Int, element: PromoEligibilityHeaderUiModel) {
        val modifiedData = ArrayList<Visitable<*>>()

        if (!element.uiState.isCollapsed) {
            val startIndex = position + 1
            for (index in startIndex until adapter.data.size) {
                val oldPromoItem = adapter.data[index]
                modifiedData.add(oldPromoItem)
            }

            element.uiState.isCollapsed = !element.uiState.isCollapsed
            element.uiData.tmpPromo = modifiedData

            adapter.modifyData(position)
            adapter.removeList(modifiedData)
        } else {
            element.uiState.isCollapsed = !element.uiState.isCollapsed

            adapter.modifyData(position)
            adapter.addVisitableList(position + 1, element.uiData.tmpPromo)
            element.uiData.tmpPromo = emptyList()
        }
    }

    // -- END OF PROMO LIST SECTION
}