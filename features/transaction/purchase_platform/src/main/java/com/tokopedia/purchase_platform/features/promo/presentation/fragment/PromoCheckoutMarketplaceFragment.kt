package com.tokopedia.purchase_platform.features.promo.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
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
import com.tokopedia.purchase_platform.features.promo.presentation.viewmodel.PromoCheckoutViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_promo_checkout_marketplace.*
import javax.inject.Inject

class PromoCheckoutMarketplaceFragment : BaseListFragment<Visitable<*>, PromoCheckoutMarketplaceAdapterTypeFactory>(),
        PromoCheckoutMarketplaceActionListener, ToolbarPromoCheckoutListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var itemDecorator: PromoDecoration

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PromoCheckoutAdapter

    private var toolbar: ToolbarPromoCheckout? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[PromoCheckoutViewModel::class.java]
    }

    companion object {
        private val HAS_ELEVATION = 6
        private val NO_ELEVATION = 0
    }

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

        var lastHeaderUiModel: PromoListHeaderUiModel? = null
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val topItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val lastData = adapter.data[topItemPosition]

                var isShow = false
                if (lastData is PromoListHeaderUiModel && lastData.uiState.isEnabled && !lastData.uiState.isCollapsed) {
                    lastHeaderUiModel = lastData
                    isShow = true
                } else if (lastHeaderUiModel != null && lastData is PromoListItemUiModel && lastData.uiData.parentIdentifierId == lastHeaderUiModel?.uiData?.identifierId) {
                    isShow = true
                } else if (lastData is PromoListItemUiModel) {
                    if (lastHeaderUiModel != null && lastData.uiData.parentIdentifierId == lastHeaderUiModel?.uiData?.identifierId) {
                        isShow = true
                    } else {
                        var foundHeader = false
                        adapter.data.forEach {
                            if (it is PromoListHeaderUiModel && it.uiData.identifierId == lastData.uiData.parentIdentifierId) {
                                lastHeaderUiModel = it
                                foundHeader = true
                                return@forEach
                            }
                        }
                        isShow = foundHeader
                    }
                } else {
                    isShow = false
                }

                if (lastHeaderUiModel != null) {

                    if (lastHeaderUiModel?.uiData?.iconUrl?.isNotBlank() == true) {
                        ImageHandler.loadImageRounded2(context, section_image_promo_list_header, lastHeaderUiModel?.uiData?.iconUrl)
                        section_image_promo_list_header.show()
                    } else {
                        section_image_promo_list_header.gone()
                    }

                    section_label_promo_list_header_title.text = lastHeaderUiModel?.uiData?.title
                    section_label_promo_list_header_sub_title.text = lastHeaderUiModel?.uiData?.subTitle

                    if (lastHeaderUiModel?.uiState?.isCollapsed == false) {
                        section_image_chevron.rotation = 180f
                    } else {
                        section_image_chevron.rotation = 0f
                    }

                    setImageFilterNormal(section_image_promo_list_header)
                    section_label_promo_list_header_sub_title.show()
                    section_image_chevron.show()
                    section_image_chevron.setOnClickListener {
                        if (lastHeaderUiModel != null) {
                            onClickPromoListHeader(adapter.data.indexOf(lastHeaderUiModel), lastHeaderUiModel!!)
                        }
                    }
                }

                if (isShow) {
                    header_promo_section.show()
                    setToolbarShadowVisibility(false)
                } else {
                    header_promo_section.gone()
                    setToolbarShadowVisibility(true)
                }
            }
        })

        observeFragmentUiModel()
        observePromoRecommendationUiModel()
        observePromoInputUiModel()
        observePromoListUiModel()
        observeVisitableChangeUiModel()
        observeVisitableListChangeUiModel()
        observeEmptyStateUiModel()
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (show) {
                app_bar_layout.elevation = HAS_ELEVATION.toFloat()
            } else {
                app_bar_layout.elevation = NO_ELEVATION.toFloat()
            }
        }
    }

    private fun observeFragmentUiModel() {
        viewModel.fragmentUiModel.observe(this, Observer {
            renderFragmentState(it)
        })
    }

    private fun observeEmptyStateUiModel() {
        viewModel.promoEmptyStateUiModel.observe(this, Observer {
            if (adapter.data.contains(it)) {
                adapter.modifyData(adapter.data.indexOf(it))
            } else {
                adapter.addVisitable(it)
            }
        })
    }

    private fun observePromoRecommendationUiModel() {
        viewModel.promoRecommendationUiModel.observe(this, Observer {
            if (adapter.data.contains(it)) {
                adapter.modifyData(adapter.data.indexOf(it))
            } else {
                adapter.addVisitable(it)
            }
        })
    }

    private fun observePromoInputUiModel() {
        viewModel.promoInputUiModel.observe(this, Observer {
            if (adapter.data.contains(it)) {
                adapter.modifyData(adapter.data.indexOf(it))
            } else {
                adapter.addVisitable(it)
            }
        })
    }

    private fun observePromoListUiModel() {
        viewModel.promoListUiModel.observe(this, Observer {
            adapter.addVisitableList(it)
        })
    }

    private fun observeVisitableChangeUiModel() {
        viewModel.tmpUiModel.observe(this, Observer {
            when (it) {
                is Update -> {
                    adapter.modifyData(adapter.data.indexOf(it.data))
                }
                is Delete -> {
                    adapter.removeData(it.data)
                }
            }
        })
    }

    private fun observeVisitableListChangeUiModel() {
        viewModel.tmpListUiModel.observe(this, Observer {
            when (it) {
                is Insert -> {
                    it.data.forEach {
                        adapter.addVisitableList((adapter.data.indexOf(it.key) + 1), it.value)
                    }
                }
            }
        })
    }

    private fun renderFragmentState(fragmentUiModel: FragmentUiModel) {
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
        viewModel.loadData()
        hideLoading()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun updateHeightPromoInputView(height: Int) {
        viewModel.updateHeightPromoInputView(height)
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
        viewModel.resetSelectedPromo()
    }

    // --- END OF FRAGMENT LEVEL ACTION


    // --- RECOMMENDATION SECTION

    override fun onClickApplyRecommendedPromo(element: PromoRecommendationUiModel) {
        element.uiState.isButtonSelectEnabled = false
        adapter.modifyData(adapter.data.indexOf(element))
    }

    // --- END OF RECOMMENDATION SECTION


    // --- PROMO LIST SECTION

    override fun onClickApplyManualInputPromo(promoCode: String) {

    }

    override fun onClickPromoListHeader(itemPosition: Int, element: PromoListHeaderUiModel) {
        viewModel.updatePromoListAfterClickPromoHeader(element)
    }

    override fun onClickPromoListItem(position: Int, element: PromoListItemUiModel) {
        viewModel.updatePromoListAfterClickPromoItem(element)
    }

    private fun isPromoScopeHasAnySelectedItem(parentIdentifierId: Int): Boolean {
        return viewModel.isPromoScopeHasAnySelectedItem(parentIdentifierId)
    }

    override fun onClickPromoItemDetail(element: PromoListItemUiModel) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_MARKETPLACE).apply {
            putExtra(EXTRA_KUPON_CODE, element.uiData.promoCode)
            putExtra(EXTRA_IS_USE, true)
            putExtra(ONE_CLICK_SHIPMENT, false)
            putExtra(PAGE_TRACKING, FROM_CART)
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
            adapter.removeDataList(modifiedData)
        } else {
            element.uiState.isCollapsed = !element.uiState.isCollapsed

            adapter.modifyData(position)
            adapter.addVisitableList(position + 1, element.uiData.tmpPromo)
            element.uiData.tmpPromo = emptyList()
        }
    }

    // -- END OF PROMO LIST SECTION
}