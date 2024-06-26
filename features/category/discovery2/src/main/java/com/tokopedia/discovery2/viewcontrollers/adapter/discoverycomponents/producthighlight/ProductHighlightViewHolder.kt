package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.producthighlight

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery2.Constant.ProductHighlight.DOUBLE
import com.tokopedia.discovery2.Constant.ProductHighlight.DOUBLESINGLEEMPTY
import com.tokopedia.discovery2.Constant.ProductHighlight.TRIPLE
import com.tokopedia.discovery2.Constant.ProductHighlight.TRIPLEDOUBLEEMPTY
import com.tokopedia.discovery2.Constant.ProductHighlight.TRIPLESINGLEEMPTY
import com.tokopedia.discovery2.Constant.ProductHighlight.V2_STYLE
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.producthighlight.DiscoveryOCSDataModel
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.unifycomponents.LocalLoad
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.item_empty_error_state.view.*

class ProductHighlightViewHolder(
    itemView: View,
    private val fragment: Fragment
) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner) {

    private val container: ConstraintLayout = itemView.findViewById(R.id.banner_container_layout)

    private var mProductHighlightViewModel: ProductHighlightViewModel? = null
    private var bannerName: String = ""
    var productHighlightItemList: ArrayList<BaseProductHighlightItem> = arrayListOf()

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        mProductHighlightViewModel = discoveryBaseViewModel as ProductHighlightViewModel
        mProductHighlightViewModel?.let { viewModel ->
            getSubComponent().inject(viewModel)
        }
        addShimmer()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        super.setUpObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductHighlightViewModel?.getProductHighlightCardItemsListData()?.observe(it) { item ->
                if (!item.data.isNullOrEmpty()) {
                    container.removeAllViews()
                    productHighlightItemList = ArrayList()
                    bannerName = item?.name ?: ""
                    addProductHighlightCard(item.data!!, item.properties?.type)
                }
            }

            mProductHighlightViewModel?.showErrorState?.observe(it) { shouldShowErrorState ->
                if (shouldShowErrorState) handleErrorState()
            }

            mProductHighlightViewModel?.hideShimmer?.observe(it) { shouldHideShimmer ->
                if (shouldHideShimmer) {
                    container.removeAllViews()
                }
            }

            mProductHighlightViewModel?.redirectToOCS?.observe(it) { result ->
                trackSuccessOCS(result)
                redirectToCheckoutPage()
            }

            mProductHighlightViewModel?.ocsErrorMessage?.observe(it) { message ->
                handleErrorOnOCS(message)
            }
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            mProductHighlightViewModel?.getProductHighlightCardItemsListData()?.removeObservers(it)
            mProductHighlightViewModel?.showErrorState?.removeObservers(it)
            mProductHighlightViewModel?.hideShimmer?.removeObservers(it)
            mProductHighlightViewModel?.redirectToOCS?.removeObservers(it)
        }
    }

    private fun addProductHighlightCard(data: List<DataItem>, compType: String?) {
        val constraintSet = ConstraintSet()
        val properties = mProductHighlightViewModel?.components?.properties

        val mutableData = data.toMutableList()

        if (compType == DOUBLE && mutableData.size == 1) {
            mutableData.firstOrNull()?.typeProductHighlightComponentCard = DOUBLE
            mutableData.add(DataItem(typeProductHighlightComponentCard = DOUBLESINGLEEMPTY))
        } else if (compType == TRIPLE && mutableData.size == 1) {
            mutableData.firstOrNull()?.typeProductHighlightComponentCard = TRIPLE
            mutableData.add(DataItem(typeProductHighlightComponentCard = TRIPLEDOUBLEEMPTY))
        } else if (compType == TRIPLE && mutableData.size == 2) {
            mutableData.forEach { dataItem ->
                dataItem.typeProductHighlightComponentCard = TRIPLE
            }
            mutableData.add(DataItem(typeProductHighlightComponentCard = TRIPLESINGLEEMPTY))
        } else {
            mutableData.forEach { dataItem ->
                dataItem.typeProductHighlightComponentCard = compType
            }
        }

        for ((index, productHighlightItem) in mutableData.withIndex()) {
            var productHighlightView: BaseProductHighlightItem
            val isLastItem = index == mutableData.size - 1
            if (productHighlightItem.parentComponentName.isNullOrEmpty()) {
                productHighlightItem.parentComponentName = bannerName
            }
            mProductHighlightViewModel?.let { viewModel ->
                productHighlightItem.positionForParentItem = viewModel.position
            }

            val previous = if (index == 0) null else productHighlightItemList[index - 1]

            productHighlightView = if (properties?.style == V2_STYLE) {
                val isBackgroundPresent = mProductHighlightViewModel?.components?.isBackgroundPresent ?: false

                ProductHighlightRevampItem(
                    productHighlightItem,
                    properties,
                    container,
                    constraintSet,
                    index,
                    previous,
                    itemView.context,
                    isLastItem,
                    compType,
                    isBackgroundPresent
                )
            } else {
                ProductHighlightItem(
                    productHighlightItem,
                    properties,
                    container,
                    constraintSet,
                    index,
                    previous,
                    itemView.context,
                    isLastItem,
                    compType
                )
            }

            productHighlightItemList.add(productHighlightView)

            setClickOnProductHighlight(productHighlightItem, index)
            setClickOnOCSButton(productHighlightItem, index)
        }
        sendImpressionEventForProductHighlight(data)
    }

    private fun sendImpressionEventForProductHighlight(data: List<DataItem>) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackPromoProductHighlightImpression(
            data,
            mProductHighlightViewModel?.components
        )
    }

    private fun setClickOnProductHighlight(itemData: DataItem, index: Int) {
        productHighlightItemList[index].productHighlightView.setOnClickListener {
            mProductHighlightViewModel?.onCardClicked(index, itemView.context)
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
                ?.trackProductHighlightClick(itemData, index, mProductHighlightViewModel?.components, mProductHighlightViewModel?.isUserLoggedIn() ?: false)
        }
    }

    private fun setClickOnOCSButton(itemData: DataItem, index: Int) {
        val phItem = productHighlightItemList[index]
        if (phItem !is ProductHighlightRevampItem) return

        phItem.onOCSButtonClicked {
            mProductHighlightViewModel?.onOCSClicked(itemView.context, itemData)
        }
    }

    private fun redirectToCheckoutPage() {
        val intent = RouteManager.getIntent(
            itemView.context,
            ApplinkConstInternalMarketplace.CHECKOUT
        )

        intent.putExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, true)

        itemView.context.startActivity(intent)
    }

    private fun trackSuccessOCS(result: DiscoveryOCSDataModel) {
        val analytics = (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()

        mProductHighlightViewModel?.components?.run {
            analytics?.trackProductHighlightOCSClick(
                result,
                Utils.getParentPosition(this),
                parentComponentId
            )
        }
    }

    private fun handleErrorState() {
        with(container) {
            removeAllViews()
            val emptyStateParentView = inflateLayout(R.layout.item_empty_error_state, false)
            val emptyStateView: LocalLoad = emptyStateParentView.findViewById(R.id.viewEmptyState)
            emptyStateView.apply {
                val errorLoadUnifyView = emptyStateView.viewEmptyState
                errorLoadUnifyView.title?.text = context?.getString(R.string.discovery_product_empty_state_title).orEmpty()
                errorLoadUnifyView.description?.text =
                    context?.getString(R.string.discovery_product_empty_state_description).orEmpty()
                errorLoadUnifyView.refreshBtn?.setOnClickListener {
                    hide()
                    removeAllViews()
                    val shimmerView = mProductHighlightViewModel?.layoutSelector()?.let { it1 -> inflateLayout(it1, false) }
                    addView(shimmerView)
                    mProductHighlightViewModel?.reload()
                }
            }
            emptyStateView.isVisible = true
            addView(emptyStateParentView)
        }
    }

    private fun handleErrorOnOCS(message: String) {
        Toaster.build(
            view = itemView,
            text = message,
            duration = Toaster.LENGTH_LONG,
            type = Toaster.TYPE_ERROR,
            actionText = itemView.context.getString(R.string.discovery_product_highlight_ocs_error_act)
        ).show()
    }

    private fun addShimmer() {
        mProductHighlightViewModel?.let { viewModel ->
            if (viewModel.shouldShowShimmer()) {
                with(container) {
                    removeAllViews()
                    val shimmerView = inflateLayout(viewModel.layoutSelector(), false)
                    addView(shimmerView)
                }
            }
        }
    }
}
