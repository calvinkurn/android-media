package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetErrorBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetLoadingBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetSuccessBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationWidgetUiModel

/**
 * Created by @ilhamsuaib on 06/07/22.
 */

class UnificationViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<UnificationWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_unification_widget
        private const val DROP_DOWN_FORMAT = "%s (%s)"
    }

    private val binding by lazy { ShcUnificationWidgetBinding.bind(itemView) }
    private val loadingStateBinding by lazy {
        val view = binding.stubShcUnificationLoading.inflate()
        ShcUnificationWidgetLoadingBinding.bind(view)
    }
    private val errorStateBinding by lazy {
        val view = binding.stubShcUnificationError.inflate()
        ShcUnificationWidgetErrorBinding.bind(view)
    }
    private val successStateBinding by lazy {
        val view = binding.stubShcUnificationSuccess.inflate()
        ShcUnificationWidgetSuccessBinding.bind(view)
    }

    override fun bind(element: UnificationWidgetUiModel) {
        setTitle(element.title)
        observeState(element)
    }

    private fun setTitle(title: String) {
        binding.tvShcUnificationTitle.text = title.parseAsHtml()
    }

    private fun observeState(element: UnificationWidgetUiModel) {
        val data = element.data
        when {
            data == null || element.showLoadingState -> showLoadingState()
            data.error.isNotEmpty() -> showErrorState(element)
            else -> showSuccessState(element)
        }
    }

    private fun showLoadingState() {
        successStateBinding.containerShcUnificationSuccess.gone()
        errorStateBinding.containerShcUnificationError.gone()
        loadingStateBinding.containerShcRecommendationLoading.visible()
    }

    private fun showErrorState(element: UnificationWidgetUiModel) {
        loadingStateBinding.containerShcRecommendationLoading.gone()
        successStateBinding.containerShcUnificationSuccess.gone()
        errorStateBinding.containerShcUnificationError.visible()

        with(errorStateBinding) {
            shcUnificationCommonErrorView.imgWidgetOnError
                .loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
            btnShcUnificationRetry.setOnClickListener {
                listener.onReloadWidget(element)
            }
        }

        if (element.data?.tabs.isNullOrEmpty()) {
            binding.tvShcUnificationTab.gone()
        } else {
            binding.tvShcUnificationTab.visible()
            setupDropDownView(element)
        }
    }

    private fun showSuccessState(element: UnificationWidgetUiModel) {
        loadingStateBinding.containerShcRecommendationLoading.gone()
        with(successStateBinding) {
            containerShcUnificationSuccess.visible()

            val data = element.data ?: return@with
            val tab = data.tabs.firstOrNull { it.isSelected }
                ?: data.tabs.firstOrNull() ?: return@with

            if (!tab.data?.error.isNullOrBlank()) {
                showErrorState(element)
                return@with
            }

            showTableWidget(tab)
            setupDropDownView(element)
            setupWidgetCta(tab)
            setupLastUpdate(element)
        }
    }

    private fun showTableWidget(tab: UnificationTabUiModel) {
        with(successStateBinding) {
            val tableData = tab.data as? TableDataUiModel ?: return@with
            tableShcUnification.visible()
            tableShcUnification.showTable(tableData.dataSet)
            tableShcUnification.addOnSlideImpressionListener { position, maxPosition, isEmpty ->

            }
            tableShcUnification.setOnSwipeListener { position, maxPosition, isEmpty ->
//                listener.sendTableOnSwipeEvent(element, position, maxPosition, isEmpty)
            }
            tableShcUnification.addOnHtmlClickListener { url, isEmpty ->
//                listener.sendTableHyperlinkClickEvent(element.dataKey, url, isEmpty)
            }
        }
    }

    private fun setupLastUpdate(element: UnificationWidgetUiModel) {
        with(successStateBinding.luvShcUnification) {
            element.data?.lastUpdated?.let { lastUpdated ->
                isVisible = lastUpdated.isEnabled
                setLastUpdated(lastUpdated.lastUpdatedInMillis)
                setRefreshButtonVisibility(lastUpdated.needToUpdated)
                setRefreshButtonClickListener {
                    listener.onReloadWidget(element)
                }
            }
        }
    }

    private fun setupWidgetCta(tab: UnificationTabUiModel) {
        with(successStateBinding) {
            val shouldShowCta = tab.config.appLink.isNotBlank()
                    && tab.config.ctaText.isNotBlank()
            if (shouldShowCta) {
                btnShcUnificationCta.visible()
                btnShcUnificationCta.setOnClickListener {
                    setOnCtaClicked(tab.config.appLink)
                }
            } else {
                btnShcUnificationCta.gone()
            }
        }
    }

    private fun setOnCtaClicked(appLink: String) {
        RouteManager.route(itemView.context, appLink)
    }

    private fun setupDropDownView(element: UnificationWidgetUiModel) {
        with(binding) {
            val data = element.data ?: return@with
            val tab = data.tabs.firstOrNull { it.isSelected }
                ?: data.tabs.firstOrNull() ?: return@with

            tvShcUnificationTab.text = String.format(
                DROP_DOWN_FORMAT, tab.title, tab.itemCount.toString()
            )
            tvShcUnificationTab.setOnClickListener {
                listener.showUnificationTabBottomSheets(element)
            }
        }
    }

    interface Listener : BaseViewHolderListener {
        fun showUnificationTabBottomSheets(element: UnificationWidgetUiModel) {}
    }
}