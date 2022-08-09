package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.const.SellerHomeUrl
import com.tokopedia.sellerhomecommon.databinding.ShcEmptyStateCommonBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetErrorBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetLoadingBinding
import com.tokopedia.sellerhomecommon.databinding.ShcUnificationWidgetSuccessBinding
import com.tokopedia.sellerhomecommon.presentation.model.TableDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationTabUiModel
import com.tokopedia.sellerhomecommon.presentation.model.UnificationWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.WidgetEmptyStateUiModel
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.NotificationUnify

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
    private val emptyStateBinding by lazy {
        ShcEmptyStateCommonBinding.bind(successStateBinding.root)
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

        binding.root.addOnImpressionListener(element.impressHolder) {
            listener.sendUnificationImpressionEvent(element)
        }
    }

    private fun showLoadingState() {
        binding.tvShcUnificationTab.gone()
        binding.viewShcUnificationTabBg.gone()
        binding.icShcUnificationTab.gone()
        binding.shcNotifTagTab.gone()

        successStateBinding.containerShcUnificationSuccess.gone()
        errorStateBinding.containerShcUnificationError.gone()
        loadingStateBinding.containerShcUnificationLoading.visible()
    }

    private fun showErrorState(element: UnificationWidgetUiModel) {
        loadingStateBinding.containerShcUnificationLoading.gone()
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
            binding.viewShcUnificationTabBg.gone()
            binding.icShcUnificationTab.gone()
            binding.shcNotifTagTab.gone()
        } else {
            setupDropDownView(element)
        }
    }

    private fun showSuccessState(element: UnificationWidgetUiModel) {
        loadingStateBinding.containerShcUnificationLoading.gone()
        with(successStateBinding) {
            containerShcUnificationSuccess.visible()

            val data = element.data ?: return@with
            val tab = data.tabs.firstOrNull { it.isSelected }
                ?: data.tabs.firstOrNull() ?: return@with

            val isError = !tab.data?.error.isNullOrBlank()
            if (isError) {
                showErrorState(element)
                return@with
            }

            if (tab.isUnauthorized) {
                showUnauthorizedState(element)
            } else {
                showTableWidget(element, tab)
                val isEmpty = tab.data?.isWidgetEmpty().orTrue()
                if (isEmpty) {
                    showEmptyState(element, tab.config.emptyStateUiModel)
                } else {
                    hideEmptyState()
                }
                setupWidgetCta(element, tab)
            }

            setupDropDownView(element)
            setupLastUpdate(element)

            listener.showUnificationWidgetCoachMark(binding.tvShcUnificationTitle)
        }
    }

    private fun showUnauthorizedState(element: UnificationWidgetUiModel) {
        val unauthorizedStateModel = WidgetEmptyStateUiModel(
            imageUrl = SellerHomeUrl.IMG_NO_ACCESS_STATE,
            title = getString(R.string.shc_no_access_state_message)
        )
        successStateBinding.btnShcUnificationCta.gone()
        showEmptyState(element, unauthorizedStateModel)
    }

    private fun hideEmptyState() {
        emptyStateBinding.shcViewEmptyStateCommon.gone()
    }

    private fun showEmptyState(
        element: UnificationWidgetUiModel,
        emptyState: WidgetEmptyStateUiModel
    ) {
        successStateBinding.tableShcUnification.gone()
        with(emptyStateBinding) {
            shcViewEmptyStateCommon.visible()
            imgShcEmptyCommon.loadImage(emptyState.imageUrl)
            tvShcEmptyTitleCommon.text = emptyState.title
            tvShcEmptyDescriptionCommon.isVisible = emptyState.description.isNotBlank()
            if (emptyState.description.isNotBlank()) {
                tvShcEmptyDescriptionCommon.text = emptyState.description
            }

            val shouldShowCta = emptyState.ctaText.isNotBlank() && emptyState.appLink.isNotBlank()
            btnShcEmptyCommon.isVisible = shouldShowCta
            if (shouldShowCta) {
                btnShcEmptyCommon.text = emptyState.ctaText
                btnShcEmptyCommon.setOnClickListener {
                    openAppLink(emptyState.appLink)
                    listener.sendUnificationEmptyStateCtaClickEvent(element)
                }
            }

            val tab = element.data?.tabs?.firstOrNull { it.isSelected } ?: return
            if (!tab.isUnauthorized) {
                root.addOnImpressionListener(tab.impressHolder) {
                    listener.sendUnificationTabImpressionEvent(element)
                }
            }
        }
    }

    private fun openAppLink(appLink: String) {
        RouteManager.route(itemView.context, appLink)
    }

    private fun showTableWidget(element: UnificationWidgetUiModel, tab: UnificationTabUiModel) {
        with(successStateBinding) {
            val tableData = tab.data as? TableDataUiModel ?: return@with

            val shouldShowPageControl = tableData.dataSet.size > Int.ONE
            shcTableViewPageControl.isVisible = shouldShowPageControl
            shcTableViewPageControl.setIndicator(tableData.dataSet.size)

            tableShcUnification.visible()
            tableShcUnification.showTable(tableData.dataSet)
            tableShcUnification.resetHeight()
            tableShcUnification.setPageIndicatorEnabled(false)
            tableShcUnification.setOnSwipeListener { position, _, _ ->
                shcTableViewPageControl.setCurrentIndicator(position)
            }
            tableShcUnification.addOnHtmlClickListener { url, _ ->
                openAppLink(url)
                listener.sendUnificationTableItemClickEvent(element)
            }
            tableShcUnification.addOnImpressionListener(tab.impressHolder) {
                listener.sendUnificationTabImpressionEvent(element)
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

    private fun setupWidgetCta(element: UnificationWidgetUiModel, tab: UnificationTabUiModel) {
        with(successStateBinding) {
            val shouldShowCta = tab.config.appLink.isNotBlank()
                    && tab.config.ctaText.isNotBlank() && !tab.data?.isWidgetEmpty().orTrue()
            if (shouldShowCta) {
                val iconWidth = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                val iconHeight = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                val iconColor = root.context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_G400
                )
                btnShcUnificationCta.setUnifyDrawableEnd(
                    IconUnify.CHEVRON_RIGHT,
                    iconColor,
                    iconWidth,
                    iconHeight
                )
                btnShcUnificationCta.visible()
                btnShcUnificationCta.text = tab.config.ctaText
                btnShcUnificationCta.setOnClickListener {
                    openAppLink(tab.config.appLink)
                    listener.sendUnificationSeeMoreClickEvent(element.dataKey, tab)
                }
            } else {
                btnShcUnificationCta.gone()
            }
        }
    }

    private fun setupDropDownView(element: UnificationWidgetUiModel) {
        with(binding) {
            val data = element.data ?: return@with
            val tab = data.tabs.firstOrNull { it.isSelected }
                ?: data.tabs.firstOrNull() ?: return@with

            tvShcUnificationTab.visible()
            viewShcUnificationTabBg.visible()
            icShcUnificationTab.visible()

            tvShcUnificationTab.text = String.format(
                DROP_DOWN_FORMAT, tab.title, tab.itemCount.toString()
            )
            viewShcUnificationTabBg.setOnClickListener {
                listener.showUnificationTabBottomSheets(element)
            }

            showNewTag(tab)
        }
    }

    private fun showNewTag(tab: UnificationTabUiModel) {
        with(binding) {
            shcNotifTagTab.isVisible = tab.isNew
            if (tab.isNew) {
                val newTag = root.context.getString(R.string.shc_new)
                shcNotifTagTab.setNotification(
                    newTag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    interface Listener : BaseViewHolderListener {
        fun showUnificationTabBottomSheets(element: UnificationWidgetUiModel) {}

        fun sendUnificationImpressionEvent(element: UnificationWidgetUiModel) {}

        fun sendUnificationTabImpressionEvent(element: UnificationWidgetUiModel) {}

        fun sendUnificationSeeMoreClickEvent(dataKey: String, tab: UnificationTabUiModel) {}

        fun sendUnificationEmptyStateCtaClickEvent(element: UnificationWidgetUiModel) {}

        fun sendUnificationTableItemClickEvent(element: UnificationWidgetUiModel) {}

        fun showUnificationWidgetCoachMark(anchor: View) {}
    }
}