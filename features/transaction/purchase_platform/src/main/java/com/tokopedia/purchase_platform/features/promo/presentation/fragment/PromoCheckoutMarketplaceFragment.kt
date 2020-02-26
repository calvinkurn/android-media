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
import javax.inject.Inject

class PromoCheckoutMarketplaceFragment : BaseListFragment<Visitable<*>, PromoCheckoutMarketplaceAdapterTypeFactory>(),
        PromoCheckoutMarketplaceActionListener, ToolbarPromoCheckoutListener {

    @Inject
    lateinit var itemDecorator: PromoDecoration

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PromoCheckoutAdapter

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
//        button_apply_promo.setOnClickListener {
        // Todo : add action to hit API
//        }
    }

    private fun setupToolbar(view: View) {
        activity?.let {
            val appbar = view.findViewById<Toolbar>(R.id.toolbar)
            appbar.removeAllViews()
            val toolbar = toolbarPromoCheckout()
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
                    title = "Ini promo global"
                    subTitle = "Pilih satu aja cuy"
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
                },
                uiState = PromoListItemUiModel.UiState().apply {

                }
        )
        adapter.addVisitable(promoListItemUiModel)

        val promoListItemUiModel1 = PromoListItemUiModel(
                uiData = PromoListItemUiModel.UiData().apply {
                    promoId = 1
                    parentIdentifierId = 1
                },
                uiState = PromoListItemUiModel.UiState().apply {

                }
        )
        adapter.addVisitable(promoListItemUiModel1)

        val promoListItemUiModel2 = PromoListItemUiModel(
                uiData = PromoListItemUiModel.UiData().apply {
                    promoId = 2
                    parentIdentifierId = 1
                },
                uiState = PromoListItemUiModel.UiState().apply {

                }
        )
        adapter.addVisitable(promoListItemUiModel2)

        val promoListHeaderUiModel1 = PromoListHeaderUiModel(
                uiData = PromoListHeaderUiModel.UiData().apply {
                    title = "Ini promo power merchant"
                    subTitle = "Pilih satu aja cuy"
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

                }
        )
        adapter.addVisitable(promoListItemUiModel3)

        val promoListHeaderUiModel2 = PromoListHeaderUiModel(
                uiData = PromoListHeaderUiModel.UiData().apply {
                    title = "Ini promo official store"
                    subTitle = "Pilih satu aja cuy"
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

    override fun onClickResetPromo() {

    }

    override fun onClickApplyRecommendedPromo() {

    }

    override fun onClickApplyManualInputPromo(promoCode: String) {

    }

    override fun onClickPromoListHeader(itemPosition: Int) {
        if (itemPosition < adapter.data.size) {
            val oldData = adapter.data[itemPosition]
            if (oldData is PromoListHeaderUiModel) {
                if (!oldData.uiState.isCollapsed) {
                    val newData = PromoListHeaderUiModel(
                            uiData = PromoListHeaderUiModel.UiData().apply {
                                title = oldData.uiData.title
                                subTitle = oldData.uiData.subTitle
                                promoType = oldData.uiData.promoType
                            },
                            uiState = PromoListHeaderUiModel.UiState().apply {
                                isCollapsed = !oldData.uiState.isCollapsed
                            }
                    )
                    adapter.modifyData(itemPosition, newData)

                    val modifiedData = ArrayList<PromoListItemUiModel>()
                    val startIndex = itemPosition + 1
                    for (index in startIndex until adapter.data.size) {
                        if (adapter.data[index] !is PromoListItemUiModel) break
                        val oldPromoItem = adapter.data[index] as PromoListItemUiModel
//                        if (oldPromoItem.uiData.parentIdentifierId == newData.uiData.identifierId) {
                        modifiedData.add(oldPromoItem)
//                        }
                    }
                    newData.uiData.tmpPromoItemList = modifiedData
                    adapter.removeList(modifiedData)
                } else {
                    val newData = PromoListHeaderUiModel(
                            uiData = PromoListHeaderUiModel.UiData().apply {
                                identifierId = oldData.uiData.identifierId
                                title = oldData.uiData.title
                                subTitle = oldData.uiData.subTitle
                                promoType = oldData.uiData.promoType
                                tmpPromoItemList = emptyList()
                            },
                            uiState = PromoListHeaderUiModel.UiState().apply {
                                isCollapsed = oldData.uiState.isCollapsed.not()
                            }
                    )
                    adapter.modifyData(itemPosition, newData)
                    adapter.addVisitableList(itemPosition + 1, oldData.uiData.tmpPromoItemList)
                }
            }
        }
    }

    override fun onClickPromoListItem(itemPosition: Int) {
        if (itemPosition < adapter.data.size) {
            val oldData = adapter.data[itemPosition]
            if (oldData is PromoListItemUiModel) {
                val newData = PromoListItemUiModel(
                        uiData = PromoListItemUiModel.UiData().apply {
                            promoId = oldData.uiData.promoId
                            parentIdentifierId = oldData.uiData.parentIdentifierId
                        },
                        uiState = PromoListItemUiModel.UiState().apply {
                            isSellected = !oldData.uiState.isSellected
                        }
                )
                adapter.modifyData(itemPosition, newData)
            }
        }
    }
}