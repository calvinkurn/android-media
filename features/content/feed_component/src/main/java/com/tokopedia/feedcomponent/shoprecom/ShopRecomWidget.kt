package com.tokopedia.feedcomponent.shoprecom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.feedcomponent.databinding.LayoutShopRecommendationBinding
import com.tokopedia.feedcomponent.shoprecom.adapter.ShopRecomAdapter
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.decor.ShopRecomItemDecoration
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * created by fachrizalmrsln on 07/10/22
 **/
class ShopRecomWidget : ConstraintLayout, LifecycleObserver, ShopRecomWidgetCallback {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var binding = LayoutShopRecommendationBinding.inflate(
        LayoutInflater.from(this.context),
        this,
        true
    )

    private var mListener: ShopRecomWidgetCallback? = null

    private val linearLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private var nextCursor: String = ""
    private val mAdapterShopRecom: ShopRecomAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ShopRecomAdapter(this) { mListener?.onShopRecomLoadingNextPage(nextCursor) }
    }

    init {
        setupView()
    }

    private fun setupView() = with(binding.rvShopRecom) {
        layoutManager = linearLayoutManager
        adapter = mAdapterShopRecom
        if (itemDecorationCount == 0) addItemDecoration(ShopRecomItemDecoration(context))
    }

    fun setListener(lifecycleOwner: LifecycleOwner, listener: ShopRecomWidgetCallback) {
        lifecycleOwner.lifecycle.addObserver(this)
        mListener = listener
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun setData(data: ShopRecomUiModel) = with(binding) {
        nextCursor = data.nextCursor
        txtHeaderShopRecom.text = data.title
        val model = buildList {
            addAll(data.items.map { ShopRecomAdapter.Model.ShopRecomWidget(it) })
            if (data.loadNextPage) add(ShopRecomAdapter.Model.Loading)
        }
        if (rvShopRecom.isComputingLayout.not()) mAdapterShopRecom.setItemsAndAnimateChanges(model)
        if (data.isRefresh && mAdapterShopRecom.itemCount > 0) rvShopRecom.scrollToPosition(0)
    }

    fun showLoadingShopRecom() = with(binding) {
        txtHeaderShopRecom.hide()
        rvShopRecom.hide()
        shimmerShopRecom.root.show()
    }

    fun showContentShopRecom() = with(binding) {
        txtHeaderShopRecom.show()
        rvShopRecom.show()
        shimmerShopRecom.root.hide()
    }

    fun showEmptyShopRecom() = with(binding) {
        txtHeaderShopRecom.hide()
        rvShopRecom.hide()
        shimmerShopRecom.root.hide()
    }

    override fun onShopRecomCloseClicked(itemID: Long) {
        mListener?.onShopRecomCloseClicked(itemID)
    }

    override fun onShopRecomFollowClicked(itemID: Long) {
        mListener?.onShopRecomFollowClicked(itemID)
    }

    override fun onShopRecomItemClicked(
        itemID: Long,
        appLink: String,
        imageUrl: String,
        postPosition: Int
    ) {
        mListener?.onShopRecomItemClicked(itemID, appLink, imageUrl, postPosition)
    }

    override fun onShopRecomItemImpress(item: ShopRecomUiModelItem, postPosition: Int) {
        mListener?.onShopRecomItemImpress(item, postPosition)
    }

    override fun onShopRecomLoadingNextPage(nextCursor: String) {
        mListener?.onShopRecomLoadingNextPage(nextCursor)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mListener = null
    }

}
