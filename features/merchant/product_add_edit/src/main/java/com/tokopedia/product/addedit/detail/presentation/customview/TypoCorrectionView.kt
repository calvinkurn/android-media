package com.tokopedia.product.addedit.detail.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.detail.presentation.adapter.TypoCorrectionAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify

class TypoCorrectionView : BaseCustomView {

    private val adapter = TypoCorrectionAdapter()
    private var typoKeywords: List<Pair<String, String>> = emptyList()

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setKeywords(typoKeywords: List<Pair<String, String>>) {
        this.show()
        this.typoKeywords = typoKeywords
        refreshViews()
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.add_edit_product_typo_correction_layout, this)
        val rvTypos: RecyclerView = view.findViewById(R.id.rv_typos)
        val chipsHide: ChipsUnify = view.findViewById(R.id.chips_hide)

        rvTypos.adapter = adapter
        rvTypos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvTypos.addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)))
        chipsHide.setOnClickListener {
            this@TypoCorrectionView.hide()
        }

        refreshViews()
    }

    private fun refreshViews() {
        adapter.setTypoKeywords(typoKeywords)
    }
}