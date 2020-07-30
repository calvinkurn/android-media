package com.tokopedia.home.account.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.home.account.R
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_add_product.view.*

class BottomSheetAddProduct(
        container: View? = null,
        sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener,
        private val listener: AddEditMenuClickListener,
        private val fm: FragmentManager? = null
) : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_add_product
        private val TAG: String = BottomSheetAddProduct::class.java.simpleName
    }

    private var sellerFeatureCarousel: SellerFeatureCarousel? = null

    init {
        if (container != null && fm != null) {
            val itemView = LayoutInflater.from(container.context)
                    .inflate(LAYOUT, (container as ViewGroup), false)

            setChild(itemView)

            sellerFeatureCarousel = itemView.sellerFeatureCarousel

            sellerFeatureCarousel?.run {
                show()
                setListener(sellerFeatureCarouselListener)
                this.addItemDecoration()
            }

            itemView.containerAddProductWithNoVariant.setOnClickListener { listener.onAddProductWithNoVariantClicked() }

            clearContentPadding = true
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@BottomSheetAddProduct)?.commit()
        }
    }

    fun show() {
        if (!GlobalConfig.isSellerApp()) {
            sellerFeatureCarousel?.setItems(listOf(
                    SellerFeatureUiModel.ProductManageSetVariantFeatureWithDataUiModel(Any())
            ))
        }

        fm?.let { show(it, TAG) }
    }

    interface AddEditMenuClickListener {
        fun onAddProductWithNoVariantClicked()
    }
}