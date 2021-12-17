package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.databinding.WidgetEmoneyPdpProductListBinding
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.EmoneyPdpProductAdapter
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.EmoneyPdpProductViewHolder
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 31/03/21
 */
class EmoneyPdpProductWidget @JvmOverloads constructor(@NotNull context: Context,
                                                       attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val adapter = EmoneyPdpProductAdapter()
    var binding : WidgetEmoneyPdpProductListBinding

    init {
        binding = WidgetEmoneyPdpProductListBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    private fun initView() {
        binding.emoneyProductListRecyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
        binding.emoneyProductListRecyclerView.adapter = adapter
    }

    fun setTitle(title: String) {
        binding.emoneyProductWidgetTitle.text = title
    }

    fun setProducts(products: List<CatalogProduct>) {
        adapter.selectedProductIndex = null
        adapter.updateProducts(products)
        showContent()
    }

    fun setListener(listener: EmoneyPdpProductViewHolder.ActionListener) {
        adapter.listener = listener
    }

    fun showShimmering() {
        if (!binding.emoneyProductShimmering.root.isShown) binding.emoneyProductShimmering.root.show()
        if (binding.emoneyProductWidgetTitle.isShown) binding.emoneyProductWidgetTitle.hide()
        if (binding.emoneyProductListRecyclerView.isShown) binding.emoneyProductListRecyclerView.hide()
    }

    private fun showContent() {
        if (binding.emoneyProductShimmering.root.isShown) binding.emoneyProductShimmering.root.hide()
        if (!binding.emoneyProductWidgetTitle.isShown) binding.emoneyProductWidgetTitle.show()
        if (!binding.emoneyProductListRecyclerView.isShown) binding.emoneyProductListRecyclerView.show()
    }

    fun showPaddingBottom(paddingHeight: Int) {
        binding.emoneyProductListRecyclerView.setPadding(0, 0, 0, paddingHeight)
    }
}