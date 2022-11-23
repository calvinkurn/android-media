
package com.tokopedia.product.manage.feature.list.view.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.databinding.BottomSheetProductManageStockAvailableBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class StockAvailableBottomSheet(
    private val fm: FragmentManager? = null
) : BottomSheetUnify() {

    companion object {
        private val TAG: String = StockAvailableBottomSheet::class.java.simpleName
        private const val SELLER_EDU = "https://seller.tokopedia.com/edu/reward-tanda-stok-tersedia/"
    }

    private var binding by autoClearedNullable<BottomSheetProductManageStockAvailableBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageStockAvailableBinding.inflate(
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
            parentFragment?.childFragmentManager?.beginTransaction()?.remove(this@StockAvailableBottomSheet)?.commit()
        }
    }

    fun show() {
        fm?.let { show(it, TAG) }
    }

    private fun setupView() {
        val title = context?.getString(com.tokopedia.product.manage.common.R.string.product_manage_stock_available).orEmpty()
        setTitle(title)

        val description = context?.getString(com.tokopedia.product.manage.common.R.string.product_manage_stock_description_information_stock_available)
            .orEmpty()
        val padding = context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4).orZero()

        binding?.tvMessage?.text = description
        binding?.btnArticle?.setOnClickListener {
            dismiss()
            RouteManager.route(requireContext(), SELLER_EDU)
        }
        binding?.root?.setPadding(0, 0, 0, padding)
    }
}
