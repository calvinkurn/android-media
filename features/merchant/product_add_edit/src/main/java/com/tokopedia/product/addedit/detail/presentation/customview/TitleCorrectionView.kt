package com.tokopedia.product.addedit.detail.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.detail.presentation.adapter.TitleCorrectionAdapter
import com.tokopedia.unifycomponents.BaseCustomView

class TitleCorrectionView : BaseCustomView {

    private val adapter = TitleCorrectionAdapter()
    private var errorKeywords: List<String> = emptyList()
    private var warningKeywords: List<String> = emptyList()

    var title: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setTitleAndKeywords(title: String, errorKeywords: List<String>, warningKeywords: List<String>) {
        this.errorKeywords = errorKeywords
        this.warningKeywords = warningKeywords
        this.title = title
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.add_edit_product_title_correction_layout, this)
        val rvTitle: RecyclerView = view.findViewById(R.id.rv_title)

        rvTitle.adapter = adapter
        setRecyclerViewToFlex(rvTitle)

        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TitleCorrectionView, 0, 0)

            try {
                title = styledAttributes.getString(R.styleable.TitleCorrectionView_title).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        adapter.setErrorKeywords(errorKeywords)
        adapter.setWarningKeywords(warningKeywords)
        adapter.setTitle(title)
    }

    private fun setRecyclerViewToFlex(recyclerView: RecyclerView) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        recyclerView.apply {
            layoutManager = flexboxLayoutManager
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)))
        }
    }
}