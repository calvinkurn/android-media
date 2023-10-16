package com.tokopedia.feedcomponent.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.databinding.ProductTagMenuOptionsBinding
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductActionBottomSheet : BottomSheetUnify() {

    private var _binding: ProductTagMenuOptionsBinding? = null
    private val binding: ProductTagMenuOptionsBinding
        get() = _binding!!

    var addToCartCB: (() -> Unit)? = null
    var addToWIshListCB: (() -> Unit)? = null
    var shareProductCB: (() -> Unit)? = null
    var isLogin = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductTagMenuOptionsBinding.inflate(layoutInflater)
        setChild(binding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLogin = arguments?.getBoolean(KEY_IS_LOGIN).orTrue()

        with(binding) {
            addToWishList.showWithCondition(isLogin)
            div1.showWithCondition(isLogin)
            div2.showWithCondition(isLogin)
            addToCart.showWithCondition(isLogin)

            addToWishList.setOnClickListener {
                addToWIshListCB?.invoke()
                dismiss()
            }
            addToCart.setOnClickListener {
                addToCartCB?.invoke()
                dismiss()
            }
            shareProduct.setOnClickListener {
                shareProductCB?.invoke()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_IS_LOGIN = "isLogin"
        fun newInstance(login: Bundle): ProductActionBottomSheet {
            val frag = ProductActionBottomSheet()
            frag.arguments = login
            return frag
        }
    }

}
