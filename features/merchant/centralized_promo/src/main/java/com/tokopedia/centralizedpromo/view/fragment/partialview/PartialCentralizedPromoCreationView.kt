package com.tokopedia.centralizedpromo.view.fragment.partialview

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.centralizedpromo.R
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_INCREASE_AVERAGE_ORDER_VALUE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoTracking
import com.tokopedia.centralizedpromo.databinding.CentralizedPromoPartialPromoCreationBinding
import com.tokopedia.centralizedpromo.databinding.FragmentCentralizedPromoBinding
import com.tokopedia.centralizedpromo.view.LoadingType
import com.tokopedia.centralizedpromo.view.adapter.CentralizedPromoAdapterTypeFactory
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.FilterPromoUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationListUiModel
import com.tokopedia.centralizedpromo.view.model.PromoCreationUiModel
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class PartialCentralizedPromoCreationView(
    private val refreshButtonClickListener: RefreshPromotionClickListener,
    binding: FragmentCentralizedPromoBinding,
    adapterTypeFactory: CentralizedPromoAdapterTypeFactory,
    coachMarkListener: CoachMarkListener,
    showCoachMark: Boolean,
    val onSelectedFilter: ((FilterPromoUiModel) -> Unit)? = null,
    private val onAovFilterImpressed: (Boolean) -> Unit
) : BasePartialListView<PromoCreationListUiModel, CentralizedPromoAdapterTypeFactory, PromoCreationUiModel>(
    binding,
    adapterTypeFactory,
    coachMarkListener,
    showCoachMark
) {

    var selectedTab: FilterPromoUiModel? = null

    var currentLoadingType = LoadingType.ALL

    companion object {
        const val SPAN_COUNT: Int = 2
    }

    private val promoCreationBinding =
        CentralizedPromoPartialPromoCreationBinding.bind(binding.root)

    init {
        setupPromoRecommendation()
    }

    override fun renderError(cause: Throwable) {
        with(promoCreationBinding) {
            dividerCentralizedPromoCreation.hide()
            when (currentLoadingType) {
                LoadingType.FILTER -> {
                    filter.hide()
                    centralizedFilterPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.hide()
                }
                LoadingType.PROMO_LIST -> {
                    showError(cause)
                    rvCentralizedPromoCreation.hide()
                    centralizedPromoCreationError.localLoadPromoCreation.progressState = false
                    centralizedPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.hide()
                }
                else -> {
                    showError(cause)
                    rvCentralizedPromoCreation.hide()
                    filter.hide()
                    centralizedPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.hide()
                    centralizedFilterPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.hide()
                }
            }
        }
        super.renderError(cause)
    }

    override fun renderLoading(loadingType: LoadingType) = with(promoCreationBinding) {
        dividerCentralizedPromoCreation.hide()
        currentLoadingType = loadingType
        when (loadingType) {
            LoadingType.FILTER -> {
                filter.hide()
                centralizedFilterPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.show()
            }
            LoadingType.PROMO_LIST -> {
                rvCentralizedPromoCreation.hide()
                centralizedPromoCreationError.layoutCentralizedPromoCreationError.hide()
                centralizedPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.show()
            }
            else -> {
                rvCentralizedPromoCreation.hide()
                filter.hide()
                centralizedPromoCreationError.layoutCentralizedPromoCreationError.hide()
                centralizedPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.show()
                centralizedFilterPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.show()
            }
        }
    }

    override fun onRecyclerViewResultDispatched() = with(promoCreationBinding) {
        rvCentralizedPromoCreation.show()
        filter.show()
        dividerCentralizedPromoCreation.show()
        centralizedPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.hide()
        centralizedFilterPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.hide()
        centralizedPromoCreationError.layoutCentralizedPromoCreationError.hide()
        centralizedPromoCreationError.localLoadPromoCreation.progressState = false
    }

    override fun shouldShowCoachMark(): Boolean =
        showCoachMark && promoCreationBinding.tvCentralizedPromoCreationTitle.isShown

    override fun getCoachMarkItem() = with(promoCreationBinding) {
        promoCreationBinding.tvCentralizedPromoCreationTitle.setBackgroundColor(
            ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_NN0)
        )
        CoachMarkItem(
            promoCreationBinding.tvCentralizedPromoCreationTitle,
            root.context.getString(R.string.sh_coachmark_title_promo_creation),
            root.context.getString(R.string.sh_coachmark_desc_promo_creation)
        )
    }

    override fun onRecyclerViewItemEmpty() = with(promoCreationBinding) {
        rvCentralizedPromoCreation.hide()
        centralizedPromoCreationError.layoutCentralizedPromoCreationError.hide()
        centralizedPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.hide()
    }

    override fun bindSuccessData(data: PromoCreationListUiModel) = with(promoCreationBinding) {
        if (data.filterItems.isNotEmpty()) {
            centralizedFilterPromoCreationShimmering.layoutCentralizedPromoCreationShimmering.hide()
            filter.show()
            centralizedPromoCreationError.layoutCentralizedPromoCreationError.hide()
            setupFilter(data)
        }
    }

    private fun showError(cause: Throwable) {
        with(promoCreationBinding) {
            val errorMessage =
                if (cause is MessageErrorException && !cause.message.isNullOrBlank()) {
                    cause.message
                } else {
                    root.context.getString(R.string.sah_label_on_going_promotion_error)
                }
            centralizedPromoCreationError.localLoadPromoCreation.title?.text = errorMessage

            centralizedPromoCreationError.localLoadPromoCreation.description?.text =
                root.context.getString(R.string.sah_label_on_going_promotion_retry)
            centralizedPromoCreationError.localLoadPromoCreation.progressState = false

            centralizedPromoCreationError.layoutCentralizedPromoCreationError.show()
            centralizedPromoCreationError.localLoadPromoCreation.refreshBtn?.setOnClickListener {
                centralizedPromoCreationError.localLoadPromoCreation.progressState = true
                refreshButtonClickListener.onRefreshPromotionListButtonClicked()
            }
        }
    }

    private fun setupPromoRecommendation() = with(promoCreationBinding) {
        rvCentralizedPromoCreation.apply {
            layoutManager = GridLayoutManager(context, SPAN_COUNT)
            adapter =
                this@PartialCentralizedPromoCreationView.adapter.apply { setHasStableIds(true) }
            isNestedScrollingEnabled = false
        }
    }

    private fun setupFilter(data: PromoCreationListUiModel) = with(promoCreationBinding) {
        val dataFilter = data.filterItems
        if (dataFilter.isNotEmpty()) {
            val filterItems = dataFilter.map {
                SortFilterItem(it.name) {
                    selectedTab = it
                    onSelectedFilter?.invoke(it)
                    adapter.clearAllElements()
                }
            }
            val selectedTabIndex: Int
            if (selectedTab == null) {
                filterItems[Int.ZERO].type = ChipsUnify.TYPE_SELECTED
                selectedTab = dataFilter[Int.ZERO]
                selectedTabIndex = Int.ZERO
                CentralizedPromoTracking.sendClickFilter(selectedTab?.id.toIntOrZero().toString())
            } else {
                selectedTabIndex = dataFilter.indexOf(selectedTab)
                filterItems[selectedTabIndex].type = ChipsUnify.TYPE_SELECTED
            }

            filter.addItem(ArrayList(filterItems))

            setupAovTracker(data, selectedTabIndex)
        }
    }

    private fun setupAovTracker(data: PromoCreationListUiModel, selectedTabIndex: Int) {
        try {
            val aovIndex = data.filterItems.indexOfFirst { it.id == ID_FILTER_INCREASE_AVERAGE_ORDER_VALUE }
            if (aovIndex != RecyclerView.NO_POSITION) {
                promoCreationBinding.filter.sortFilterItems.getChildAt(aovIndex)?.addOnImpressionListener(
                    data.aovFilterImpressHolder
                ) {
                    val isSelected = aovIndex == selectedTabIndex
                    onAovFilterImpressed.invoke(isSelected)
                }
            }
        } catch (ignored: Exception) {
            // no-op
        }
    }

    interface RefreshPromotionClickListener {
        fun onRefreshPromotionListButtonClicked()
    }
}
