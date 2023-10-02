package com.tokopedia.shop.product.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.imageassets.TokopediaImageUrl.ILLUSTRATION_SHOP_ETALASE_NOT_FOUND
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.databinding.BottomSheetEtalaseNotFoundBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

open class ShopEtalaseNotFoundBottomSheet : BottomSheetUnify() {

    var onClickGoToShop: (() -> Unit)? = null

    companion object {
        private const val TAG = "ShopEtalaseNotFoundBottomSheet"

        fun createInstance(onClickGoToShop: (() -> Unit)? = null): ShopEtalaseNotFoundBottomSheet {
            return ShopEtalaseNotFoundBottomSheet().apply {
                isCancelable = false
                showCloseIcon = false
                this.onClickGoToShop = onClickGoToShop
            }
        }
    }


    private var binding by autoClearedNullable<BottomSheetEtalaseNotFoundBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetEtalaseNotFoundBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ivEtalaseNotFound?.loadImage(ILLUSTRATION_SHOP_ETALASE_NOT_FOUND)
        binding?.btnGotoShop?.setOnClickListener {
            dismiss()
            onClickGoToShop?.invoke()
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@ShopEtalaseNotFoundBottomSheet)?.commit()
        }
    }

    fun show(fm: FragmentManager) {
        if (!this.isAdded) {
            show(fm, TAG)
        }
    }

}
