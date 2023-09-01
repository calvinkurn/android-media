package com.tokopedia.home_component.widget.shop_flash_sale.tab

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.home_component.databinding.HomeComponentShopFlashSaleTabListBinding
import com.tokopedia.home_component.util.NpaLinearLayoutManager
import com.tokopedia.home_component.R as home_componentR

class ShopTabView : LinearLayout, ShopTabListener {
    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.home_component_shop_flash_sale_tab_list
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var listener: ShopTabListener? = null
    private var binding: HomeComponentShopFlashSaleTabListBinding? = null

    private val tabLayoutManager by lazy {
        NpaLinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL
        )
    }

    private val tabAdapter by lazy {
        ShopFlashSaleTabAdapter(
            ShopFlashSaleTabDiffUtilCallback(),
            this
        )
    }

    init {
        binding = HomeComponentShopFlashSaleTabListBinding.inflate(LayoutInflater.from(context), this, true)
        initShopTabAdapter()
    }

    private fun initShopTabAdapter() {
        binding?.homeComponentShopFlashSaleTabRv?.apply {
            layoutManager = tabLayoutManager
            itemAnimator = null
            adapter = tabAdapter
        }
    }

    fun submitList(
        tabList: List<ShopFlashSaleTabDataModel>,
        listener: ShopTabListener
    ) {
        this.listener = listener
        tabAdapter.submitList(tabList)
    }

    override fun onShopTabClick(element: ShopFlashSaleTabDataModel) {
        listener?.onShopTabClick(element)
    }
}
