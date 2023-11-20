package com.tokopedia.home_component.widget.shop_tab

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
            ShopTabDiffUtilCallback(),
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

    fun setShopTabs(
        tabList: List<ShopTabDataModel>,
        listener: ShopTabListener,
    ) {
        this.listener = listener
        updateShopTabList(tabList)
    }

    private fun updateShopTabList(
        tabList: List<ShopTabDataModel>,
        onUpdateFinished: () -> Unit = { },
    ) {
        tabAdapter.submitList(tabList) {
            onUpdateFinished.invoke()
        }
    }

    override fun onShopTabClick(element: ShopTabDataModel) {
        val newList = tabAdapter.currentList.map {
            it.copy(isActivated = it.id == element.id)
        }
        updateShopTabList(newList) {
            listener?.onShopTabClick(element)
        }
    }
}
