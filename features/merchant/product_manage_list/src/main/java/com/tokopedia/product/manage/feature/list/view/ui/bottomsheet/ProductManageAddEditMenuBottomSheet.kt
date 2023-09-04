package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.manage.common.util.ProductManageConfig
import com.tokopedia.product.manage.databinding.BottomSheetProductManageAddEditMenuBinding
import com.tokopedia.seller_migration_common.presentation.model.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.widget.SellerFeatureCarousel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class ProductManageAddEditMenuBottomSheet(
        private val sellerFeatureCarouselListener: SellerFeatureCarousel.SellerFeatureClickListener,
        private val listener: AddEditMenuClickListener,
        private val fm: FragmentManager? = null
) : BottomSheetUnify() {

    companion object {
        private val TAG: String = ProductManageAddEditMenuBottomSheet::class.java.simpleName
    }

    private var sellerFeatureCarousel: SellerFeatureCarousel? = null

    private var binding by autoClearedNullable<BottomSheetProductManageAddEditMenuBinding>()

    init {
        clearContentPadding = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageAddEditMenuBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@ProductManageAddEditMenuBottomSheet)?.commit()
        }
    }

    fun show() {
        if (!ProductManageConfig.IS_SELLER_APP) {
            sellerFeatureCarousel?.setItems(listOf(
                    SellerFeatureUiModel.ProductManageSetVariantFeatureWithDataUiModel(Any())
            ))
        }

        fm?.let { show(it, TAG) }
    }

    private fun setupView() {
        binding?.sellerFeatureCarousel?.run {
            if (!ProductManageConfig.IS_SELLER_APP) {
                show()
                setListener(sellerFeatureCarouselListener)
                this.addItemDecoration()
            }
        }

        binding?.containerAddProductWithNoVariant?.setOnClickListener {
            listener.onAddProductWithNoVariantClicked()
        }
    }

    interface AddEditMenuClickListener {
        fun onAddProductWithNoVariantClicked()
    }
}
