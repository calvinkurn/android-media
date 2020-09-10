package com.tokopedia.productcard.options

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.manager.*
import com.tokopedia.product.share.ProductShare
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.productcard.options.item.ProductCardOptionsItemViewHolder
import com.tokopedia.productcard.options.tracking.ProductCardOptionsTracking
import kotlinx.android.synthetic.main.product_card_options_fragment_layout.*


internal class ProductCardOptionsFragment: TkpdBaseV4Fragment() {

    private var productCardOptionsViewModel: ProductCardOptionsViewModel? = null

    override fun getScreenName(): String {
        return "product card options"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.product_card_options_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        observeViewModelData()
    }

    private fun initViewModel() {
        activity?.let {
            productCardOptionsViewModel = ViewModelProvider(it).get(ProductCardOptionsViewModel::class.java)
        }
    }

    private fun observeViewModelData() {
        observeOptionListLiveData()
        observeRouteToSimilarSearchEventLiveData()
        observeCloseProductCardOptionsEventLiveData()
        observeAddWishlistEventLiveData()
        observeTrackingSeeSimilarProductsEventLiveData()
        observeAddToCartEventLiveData()
        observeRouteToShopPageEvent()
        observeShareProductEvent()
    }

    private fun observeOptionListLiveData() {
        productCardOptionsViewModel?.getOptionsListLiveData()?.observe(viewLifecycleOwner, Observer {
            loadOptions(it)
        })
    }

    private fun loadOptions(optionList: List<Visitable<*>>) {
        productCardOptionsRecyclerView?.adapter = ProductCardOptionsAdapter(ProductCardOptionsTypeFactoryImpl()).also {
            it.setList(optionList)
        }

        productCardOptionsRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    /**
     * Currently, it only has 2 types of view, the Option Item View, and the Divider View.
     * To prevent overkill design, we only use a simple linear layout, and add view inside it.
     *
     * If the number of view types increases, consider using RecyclerView with Visitable + Type Factory or Adapter Delegate pattern.
    * */
//    private fun renderViewToBottomSheet(context: Context, itemView: Any) {
//        if (itemView is ProductCardOptionsItemModel) {
//            productCardOptionsBottomSheet?.addOptionView(context, itemView)
//        }
//        else if (itemView is ProductCardOptionsItemDivider){
//            productCardOptionsBottomSheet?.addDividerView(context)
//        }
//    }
//
//    private fun ConstraintLayout?.addOptionView(context: Context, optionItemModel: ProductCardOptionsItemModel) {
//        this?.addView(ProductCardOptionsItemView(context, optionItemModel))
//    }
//
//    private fun LinearLayout?.addDividerView(context: Context) {
//        View.inflate(context, R.layout.product_card_options_item_divider, this)
//    }

    private fun observeRouteToSimilarSearchEventLiveData() {
        productCardOptionsViewModel?.getRouteToSimilarSearchEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            routeToSimilarSearch()
        })
    }

    private fun routeToSimilarSearch() {
        activity?.let { activity ->
            val productCardOptionsModel = productCardOptionsViewModel?.productCardOptionsModel
                    ?: return

            startSimilarSearch(activity, productCardOptionsModel.productId, productCardOptionsModel.keyword)
        }
    }

    private fun observeCloseProductCardOptionsEventLiveData() {
        productCardOptionsViewModel?.getCloseProductCardOptionsEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            activity?.finish()
        })
    }

    private fun observeAddWishlistEventLiveData() {
        productCardOptionsViewModel?.getWishlistEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            sendProductCardOptionsResult(PRODUCT_CARD_OPTIONS_RESULT_CODE_WISHLIST)
        })
    }

    private fun sendProductCardOptionsResult(resultCode: Int) {
        activity?.setResult(resultCode, createProductCardOptionsResult())
        activity?.finish()
    }

    private fun createProductCardOptionsResult(): Intent {
        return Intent().also {
            it.putExtra(PRODUCT_CARD_OPTION_RESULT_PRODUCT, productCardOptionsViewModel?.productCardOptionsModel)
        }
    }

    private fun observeTrackingSeeSimilarProductsEventLiveData() {
        productCardOptionsViewModel?.getTrackingSeeSimilarProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            ProductCardOptionsTracking.eventClickSeeSimilarProduct(
                    productCardOptionsViewModel?.productCardOptionsModel?.seeSimilarProductEvent ?: "",
                    productCardOptionsViewModel?.productCardOptionsModel?.screenName ?: "",
                    productCardOptionsViewModel?.productCardOptionsModel?.keyword ?: "",
                    productCardOptionsViewModel?.productCardOptionsModel?.productId ?: ""
            )
        })
    }

    private fun observeAddToCartEventLiveData() {
        productCardOptionsViewModel?.getAddToCartEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            sendProductCardOptionsResult(PRODUCT_CARD_OPTIONS_RESULT_CODE_ATC)
        })
    }

    private fun observeRouteToShopPageEvent() {
        productCardOptionsViewModel?.getRouteToShopPageEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            routeToShopPage()
            sendProductCardOptionsResult(PRODUCT_CARD_OPTIONS_RESULT_CODE_VISIT_SHOP)
        })
    }

    private fun routeToShopPage() {
        context?.let { context ->
            val shopId = productCardOptionsViewModel?.productCardOptionsModel?.shopId ?: ""

            if (shopId.isNotEmpty())
                RouteManager.route(context, ApplinkConst.SHOP, shopId)
        }
    }

    private fun observeShareProductEvent() {
        productCardOptionsViewModel?.getShareProductEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            activity?.let { activity ->
                ProductShare(activity).share(it, {}, {
                    sendProductCardOptionsResult(PRODUCT_CARD_OPTIONS_RESULT_CODE_SHARE_PRODUCT)
                })
            }
        })
    }
}