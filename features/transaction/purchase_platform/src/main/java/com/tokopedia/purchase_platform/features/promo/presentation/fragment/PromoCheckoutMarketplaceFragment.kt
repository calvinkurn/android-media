package com.tokopedia.purchase_platform.features.promo.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutMarketplaceActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoInputUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoRecommendationUiModel
import javax.inject.Inject

class PromoCheckoutMarketplaceFragment: BaseListFragment<Visitable<*>, PromoCheckoutMarketplaceAdapterTypeFactory>(), PromoCheckoutMarketplaceActionListener {

    @Inject
    lateinit var itemDecorator: PromoDecoration

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PromoCheckoutAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_promo_checkout_marketplace, container, false)
        recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(itemDecorator)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        return view
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
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun onClickApplyManualInputPromo(promoCode: String) {

    }

}