package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.product_tag_menu_options.*

class ProductActionBottomSheet : BottomSheetUnify() {

    var addToCartCB: (() -> Unit)? = null
    var addToWIshListCB: (() -> Unit)? = null
    var shareProductCB: (() -> Unit)? = null
    var isLogin = false

    companion object {

        fun newInstance(login: Bundle): ProductActionBottomSheet {
            val frag = ProductActionBottomSheet()
            frag.arguments = login
            return frag
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
        isLogin = arguments?.getBoolean("isLogin")?:true
        addToWishList?.showWithCondition(isLogin)
        div1?.showWithCondition(isLogin)
        div2?.showWithCondition(isLogin)
        addToCart?.showWithCondition(isLogin)

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