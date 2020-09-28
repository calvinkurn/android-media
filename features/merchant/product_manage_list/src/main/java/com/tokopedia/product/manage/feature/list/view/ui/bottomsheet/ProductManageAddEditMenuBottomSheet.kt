package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.R
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_product_manage_add_edit_menu.view.*

class ProductManageAddEditMenuBottomSheet(
        container: View? = null,
        sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener,
        private val listener: AddEditMenuClickListener,
        private val fm: FragmentManager? = null
) : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.bottom_sheet_product_manage_add_edit_menu
        private val TAG: String = ProductManageAddEditMenuBottomSheet::class.java.simpleName
    }

    private var sellerFeatureCarousel: SellerFeatureCarousel? = null

    init {
        if (container != null && fm != null) {
            val itemView = LayoutInflater.from(container.context)
                    .inflate(LAYOUT, (container as ViewGroup), false)

            setChild(itemView)

            sellerFeatureCarousel = itemView.sellerFeatureCarousel

            sellerFeatureCarousel?.run {
                if (!GlobalConfig.isSellerApp()) {
                    show()
                    setListener(sellerFeatureCarouselListener)
                    this.addItemDecoration()
                }
            }

            itemView.containerAddProductWithNoVariant.setOnClickListener { listener.onAddProductWithNoVariantClicked() }

            clearContentPadding = true
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@ProductManageAddEditMenuBottomSheet)?.commit()
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