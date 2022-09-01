package com.tokopedia.product.manage.feature.stockreminder.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.BottomSheetStockLimitInfoBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class StockRemainingInfoBottomSheet(
    private val fm: FragmentManager? = null
) : BottomSheetUnify() {

    companion object {
        private val TAG: String = StockRemainingInfoBottomSheet::class.java.simpleName
    }

    private var binding by autoClearedNullable<BottomSheetStockLimitInfoBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetStockLimitInfoBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = context?.getString(R.string.product_stock_reminder_info_stock_remain).orEmpty()
        setTitle(title)

        val padding =
            context?.resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                .orZero()
        binding?.textDescription?.text =
            context?.getString(R.string.product_stock_reminder_info_stock_remain_desc).orEmpty()
        binding?.root?.setPadding(0, 0, 0, padding)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.run {
            parentFragment?.childFragmentManager?.beginTransaction()
                ?.remove(this@StockRemainingInfoBottomSheet)?.commit()
        }
    }

    fun show() {
        fm?.let {
            show(it, TAG)
        }
    }

}