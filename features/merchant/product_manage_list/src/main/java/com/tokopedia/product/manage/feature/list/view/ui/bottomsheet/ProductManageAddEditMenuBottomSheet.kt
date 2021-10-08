package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.databinding.BottomSheetProductManageAddEditMenuBinding
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductManageAddEditMenuBottomSheet(
        container: View? = null,
        sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener,
        private val listener: AddEditMenuClickListener,
        private val fm: FragmentManager? = null
) : BottomSheetUnify() {

    companion object {
        private val TAG: String = ProductManageAddEditMenuBottomSheet::class.java.simpleName
    }

    private var sellerFeatureCarousel: SellerFeatureCarousel? = null

    init {
        if (container != null && fm != null) {
            val binding = BottomSheetProductManageAddEditMenuBinding.inflate(
                LayoutInflater.from(context),
                container as ViewGroup,
                false
            )

            sellerFeatureCarousel = binding.sellerFeatureCarousel

            sellerFeatureCarousel?.run {
                if (!GlobalConfig.isSellerApp()) {
                    show()
                    setListener(sellerFeatureCarouselListener)
                    this.addItemDecoration()
                }
            }

            binding.containerAddProductWithNoVariant.setOnClickListener {
                listener.onAddProductWithNoVariantClicked()
            }

            setChild(binding.root)
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