package com.tokopedia.productcard.options

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.manager.startSimilarSearch
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
            productCardOptionsViewModel = ViewModelProviders.of(it).get(ProductCardOptionsViewModel::class.java)
        }
    }

    private fun observeViewModelData() {
        observeOptionListLiveData()
        observeRouteToSimilarSearchEventLiveData()
        observeCloseProductCardOptionsEventLiveData()
        observeRouteToLoginPageEventLiveData()
    }

    private fun observeOptionListLiveData() {
        productCardOptionsViewModel?.getOptionsListLiveData()?.observe(viewLifecycleOwner, Observer {
            loadOptions(it)
        })
    }

    private fun loadOptions(optionList: List<Any>) {
        activity?.let { activity ->
            optionList.forEach {
                renderViewToBottomSheet(activity, it)
            }
        }
    }

    private fun renderViewToBottomSheet(context: Context, itemView: Any) {
        if (itemView is ProductCardOptionsItemModel) {
            productCardOptionsBottomSheet?.addOptionView(context, itemView)
        }
        else if (itemView is ProductCardOptionsItemDivider){
            productCardOptionsBottomSheet?.addDividerView(context)
        }
    }

    private fun LinearLayout?.addOptionView(context: Context, optionItemModel: ProductCardOptionsItemModel) {
        this?.addView(ProductCardOptionsItemView(context, optionItemModel))
    }

    private fun LinearLayout?.addDividerView(context: Context) {
        View.inflate(context, R.layout.product_card_options_item_divider, this)
    }

    private fun observeRouteToSimilarSearchEventLiveData() {
        productCardOptionsViewModel?.getRouteToSimilarSearchEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            routeToSimilarSearch()
        })
    }

    private fun routeToSimilarSearch() {
        activity?.let {
            val productCardOptionsModel = productCardOptionsViewModel?.productCardOptionsModel
                    ?: return

            startSimilarSearch(it, productCardOptionsModel.productId, productCardOptionsModel.keyword)
        }
    }

    private fun observeCloseProductCardOptionsEventLiveData() {
        productCardOptionsViewModel?.getCloseProductCardOptionsEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            finish()
        })
    }

    private fun finish() {
        activity?.finish()
    }

    private fun observeRouteToLoginPageEventLiveData() {
        productCardOptionsViewModel?.getRouteToLoginPageEventLiveData()?.observe(viewLifecycleOwner, EventObserver {
            routeToLoginPage()
        })
    }

    private fun routeToLoginPage() {
        activity?.let { activity ->
            RouteManager.route(activity, ApplinkConst.LOGIN)
        }
    }
}