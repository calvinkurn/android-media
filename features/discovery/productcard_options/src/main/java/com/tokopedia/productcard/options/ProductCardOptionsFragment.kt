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
}