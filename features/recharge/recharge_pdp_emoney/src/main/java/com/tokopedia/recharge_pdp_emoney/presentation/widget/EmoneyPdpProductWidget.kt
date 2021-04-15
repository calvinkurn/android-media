package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.EmoneyPdpProductAdapter
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.EmoneyPdpProductViewHolder
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_emoney_pdp_product_list.view.*
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 31/03/21
 */
class EmoneyPdpProductWidget @JvmOverloads constructor(@NotNull context: Context,
                                                       attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val adapter = EmoneyPdpProductAdapter()

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_emoney_pdp_product_list, this, true)
        initView()
    }

    private fun initView() {
        emoneyProductListRecyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
        emoneyProductListRecyclerView.adapter = adapter
    }

    var titleText: String = ""
        set(title) {
            field = title
            emoneyProductWidgetTitle.text = title
        }

    fun setProducts(products: List<CatalogProduct>) {
        adapter.products = products
        adapter.selectedProductIndex = null
        showContent()
    }

    fun setListener(listener: EmoneyPdpProductViewHolder.ActionListener) {
        adapter.listener = listener
    }

    fun showShimmering() {
        emoneyProductShimmering.show()
        emoneyProductWidgetTitle.hide()
        emoneyProductListRecyclerView.hide()
    }

    private fun showContent() {
        emoneyProductShimmering.hide()
        emoneyProductWidgetTitle.show()
        emoneyProductListRecyclerView.show()
    }

    fun showPaddingBottom(isShow: Boolean) {
        emoneyProductListRecyclerView.setPadding(0, 0, 0,
                if (isShow) resources.getDimensionPixelOffset(com.tokopedia.unifycomponents.R.dimen.layout_lvl9)
                else resources.getDimensionPixelOffset(com.tokopedia.unifycomponents.R.dimen.spacing_lvl6))
    }
}