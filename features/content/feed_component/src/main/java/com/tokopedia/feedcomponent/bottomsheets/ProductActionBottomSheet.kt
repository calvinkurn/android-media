package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.product_tag_menu_options.*

class ProductActionBottomSheet : BottomSheetUnify() {

    var addToCartCB: (() -> Unit)? = null
    var addToWIshListCB: (() -> Unit)? = null
    var shareProductCB: (() -> Unit)? = null

    companion object {

        fun newInstance(): ProductActionBottomSheet {
            return ProductActionBottomSheet()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = View.inflate(context, R.layout.product_tag_menu_options, null)
        setChild(contentView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addToWishList?.setOnClickListener {
            addToWIshListCB?.invoke()
            dismiss()
        }
        addToCart?.setOnClickListener {
            addToCartCB?.invoke()
            dismiss()
        }
        shareProduct?.setOnClickListener {
            shareProductCB?.invoke()
            dismiss()
        }

    }
}