package com.tokopedia.shop.settings.basicinfo.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopDomainSuggestionAdapter
import com.tokopedia.shop.settings.basicinfo.view.adapter.ShopDomainSuggestionAdapterFactoryImpl
import com.tokopedia.shop.settings.basicinfo.view.model.ShopDomainSuggestion
import kotlinx.android.synthetic.main.layout_shop_domain_suggestion.view.*

class ShopDomainSuggestionView: FrameLayout {

    private var adapter: ShopDomainSuggestionAdapter? = null
    private var onClickItemListener: ((String) -> Unit)? = null

    constructor (context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        inflate(context, R.layout.layout_shop_domain_suggestion, this)

        adapter = ShopDomainSuggestionAdapter(ShopDomainSuggestionAdapterFactoryImpl {
            onClickItemListener?.invoke(it)
        })

        suggestionList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        suggestionList.adapter = adapter
    }

    fun show(data: List<String>) {
        val suggestions = data.map { ShopDomainSuggestion(it) }
        adapter?.clearAllElements()
        adapter?.addElement(suggestions)
        adapter?.notifyDataSetChanged()
        visibility = View.VISIBLE
    }

    fun setOnItemClickListener(onClickItemListener: (String) -> Unit) {
        this.onClickItemListener = onClickItemListener
    }
}