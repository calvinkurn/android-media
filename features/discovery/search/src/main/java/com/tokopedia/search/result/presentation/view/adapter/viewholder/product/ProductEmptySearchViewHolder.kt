package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.result.presentation.model.EmptySearchProductDataView
import com.tokopedia.search.result.presentation.view.adapter.viewholder.decoration.SpacingItemDecoration
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener
import com.tokopedia.topads.sdk.base.Config
import com.tokopedia.topads.sdk.base.Endpoint
import com.tokopedia.topads.sdk.domain.TopAdsParams
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener
import com.tokopedia.topads.sdk.widget.TopAdsBannerView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.R

class ProductEmptySearchViewHolder(
        view: View,
        private val emptyStateListener: EmptyStateListener,
        private val bannerAdsListener: BannerAdsListener,
        topAdsConfig: Config?
) : AbstractViewHolder<EmptySearchProductDataView>(view), TopAdsItemClickListener {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = com.tokopedia.search.R.layout.search_empty_state_product
        private const val SEARCH_NF_VALUE = "1"
    }

    private val context: Context? = itemView.context
    private val noResultImage: ImageView? = itemView.findViewById(com.tokopedia.search.R.id.no_result_image)
    private val emptyTitleTextView: TextView? = itemView.findViewById(com.tokopedia.search.R.id.text_view_empty_title_text)
    private val emptyContentTextView: TextView? = itemView.findViewById(com.tokopedia.search.R.id.text_view_empty_content_text)
    private val emptyButtonItemButton: TextView? = itemView.findViewById(com.tokopedia.search.R.id.button_add_promo)
    private val topAdsBannerView: TopAdsBannerView? = itemView.findViewById(com.tokopedia.search.R.id.banner_ads)
    private val buttonEmptySearchToGlobalSearch: UnifyButton? =
            itemView.findViewById(com.tokopedia.search.R.id.buttonEmptySearchToGlobalSearch)
    private val selectedFilterRecyclerView: RecyclerView? =
            itemView.findViewById(com.tokopedia.search.R.id.selectedFilterRecyclerView)

    private var topAdsParams: TopAdsParams? = null
    private var productSelectedFilterAdapter: ProductSelectedFilterAdapter? = null
    private var boundedEmptySearchModel: EmptySearchProductDataView? = null

    init {
        val topAdsParams = topAdsConfig?.topAdsParams
        topAdsParams?.param?.put(TopAdsParams.KEY_SEARCH_NF, SEARCH_NF_VALUE)

        initSelectedFilterRecyclerView()
    }

    private fun initSelectedFilterRecyclerView() {
        selectedFilterRecyclerView ?: return

        val layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = itemView.context.resources.getDimensionPixelOffset(R.dimen.unify_space_8)

        selectedFilterRecyclerView.addItemDecoration(SpacingItemDecoration(staticDimen8dp))
        selectedFilterRecyclerView.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(selectedFilterRecyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)

        productSelectedFilterAdapter = ProductSelectedFilterAdapter(emptyStateListener)
        selectedFilterRecyclerView.adapter = productSelectedFilterAdapter
    }

    override fun onProductItemClicked(position: Int, product: Product) {
        val intent = getProductIntent(product.id) ?: return
        context?.startActivity(intent)
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (context != null)
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        else null
    }

    override fun onShopItemClicked(position: Int, shop: Shop) {
        //Not implemented just leave empty
    }

    override fun onAddFavorite(position: Int, data: Data) {
        //Not implemented just leave empty
    }

    override fun bind(model: EmptySearchProductDataView) {
        boundedEmptySearchModel = model

        bindNoResultImage()
        bindTitleTextView()
        bindContentTextView()
        bindNewSearchButton()
        bindRecyclerView()
        bindGlobalSearchButton()
    }

    private fun bindNoResultImage() {
        noResultImage?.setImageResource(com.tokopedia.resources.common.R.drawable.ic_product_search_not_found)
    }

    private fun bindTitleTextView() {
        if (boundedEmptySearchModel?.isLocalSearch == true)
            emptyTitleTextView?.text = getString(com.tokopedia.search.R.string.msg_empty_search_product_title_local)
        else
            emptyTitleTextView?.text = getString(com.tokopedia.search.R.string.msg_empty_search_product_title)
    }

    private fun bindContentTextView() {
        when {
            boundedEmptySearchModel?.isLocalSearch == true ->
                emptyContentTextView?.text = getLocalSearchEmptyMessage()
            boundedEmptySearchModel?.isFilterActive == true ->
                emptyContentTextView?.text = getString(com.tokopedia.search.R.string.msg_empty_search_product_content_with_filter)
            else ->
                emptyContentTextView?.text = getString(com.tokopedia.search.R.string.msg_empty_search_product_content)
        }
    }

    private fun getLocalSearchEmptyMessage() =
            if (itemView.context == null) ""
            else itemView.context.getString(
                com.tokopedia.search.R.string.msg_empty_search_product_content_local,
                boundedEmptySearchModel?.keyword ?: "",
                boundedEmptySearchModel?.pageTitle ?: ""
            )

    private fun bindNewSearchButton() {
        val shouldShow = boundedEmptySearchModel?.isFilterActive == false

        emptyButtonItemButton?.shouldShowWithAction(shouldShow) {
            emptyButtonItemButton.setOnClickListener(this::newSearchButtonOnClick)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun newSearchButtonOnClick(view: View) {
        emptyStateListener.onEmptyButtonClicked()
    }

    private fun bindRecyclerView() {
        val selectedFilterFromEmptyStateListener = emptyStateListener.getSelectedFilterAsOptionList() ?: return
        val shouldShowSelectedFilterList = selectedFilterFromEmptyStateListener.isNotEmpty()

        selectedFilterRecyclerView?.shouldShowWithAction(shouldShowSelectedFilterList) {
            populateSelectedFilterToRecyclerView(selectedFilterFromEmptyStateListener)
        }
    }

    private fun populateSelectedFilterToRecyclerView(selectedFilterOptionList: List<Option>) {
        productSelectedFilterAdapter?.setOptionList(selectedFilterOptionList)
    }

    private fun bindBannerAds() {
        topAdsParams?.let {
            loadBannerAds(it)
        }
    }

    private fun loadBannerAds(topAdsParams: TopAdsParams) {
        val emptySearchModel = boundedEmptySearchModel ?: return
        if (!emptySearchModel.isBannerAdsAllowed) return

        val bannerAdsConfig = Config.Builder()
                .setSessionId(emptyStateListener.getRegistrationId())
                .setUserId(emptyStateListener.getUserId())
                .withMerlinCategory()
                .topAdsParams(topAdsParams)
                .setEndpoint(Endpoint.CPM)
                .build()

        topAdsBannerView?.let { topAdsBannerView ->
            topAdsBannerView.setConfig(bannerAdsConfig)
            topAdsBannerView.setTopAdsBannerClickListener { position, appLink: String?, data: CpmData? ->
                bannerAdsListener.onBannerAdsClicked(position, appLink, data)
            }
            topAdsBannerView.loadTopAds()
        }
    }

    private fun bindGlobalSearchButton() {
        val shouldShowButtonToGlobalSearch = boundedEmptySearchModel?.isLocalSearch == true
        buttonEmptySearchToGlobalSearch?.shouldShowWithAction(shouldShowButtonToGlobalSearch) {
            buttonEmptySearchToGlobalSearch.setOnClickListener(this::onEmptySearchToGlobalSearchClicked)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onEmptySearchToGlobalSearchClicked(view: View) {
        emptyStateListener.onEmptySearchToGlobalSearchClicked(boundedEmptySearchModel?.globalSearchApplink ?: "")
    }

    private class ProductSelectedFilterAdapter(
            private val clickListener: EmptyStateListener
        ) : RecyclerView.Adapter<ProductSelectedFilterItemViewHolder>() {

        private val optionList = mutableListOf<Option>()

        fun setOptionList(optionList: List<Option>) {
            this.optionList.clear()
            this.optionList.addAll(optionList)

            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSelectedFilterItemViewHolder {
            val view = LayoutInflater
                    .from(parent.context)
                    .inflate(
                            com.tokopedia.search.R.layout.search_product_empty_state_selected_filter_item,
                            parent,
                            false,
                    )

            return ProductSelectedFilterItemViewHolder(view, clickListener)
        }

        override fun onBindViewHolder(holder: ProductSelectedFilterItemViewHolder, position: Int) {
            holder.bind(optionList[position])
        }

        override fun getItemCount(): Int {
            return optionList.size
        }
    }

    private class ProductSelectedFilterItemViewHolder(
            itemView: View,
            private val clickListener: EmptyStateListener
        ) : RecyclerView.ViewHolder(itemView) {

        private val filterText: TextView = itemView.findViewById(com.tokopedia.search.R.id.filter_text)

        fun bind(option: Option) {
            filterText.text = option.name
            filterText.setOnClickListener {
                clickListener.onSelectedFilterRemoved(option.uniqueId)
            }
        }
    }
}