package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardPlaceholderAdapter
import com.tokopedia.play.widget.ui.itemdecoration.PlayWidgetCardPlaceholderItemDecoration
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 12/10/20
 */
class PlayWidgetPlaceholderView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val rvWidgetCardPlaceholder: RecyclerView
    private val adapter = PlayWidgetCardPlaceholderAdapter()

    init {
        val view = View.inflate(context, R.layout.view_play_widget_placeholder, this)
        rvWidgetCardPlaceholder = view.findViewById(R.id.rv_widget_card_placeholder)

        setupView(view)
    }

    private fun setupView(view: View) {
        rvWidgetCardPlaceholder.adapter = adapter
        rvWidgetCardPlaceholder.addItemDecoration(PlayWidgetCardPlaceholderItemDecoration(context))
        rvWidgetCardPlaceholder.suppressLayout(true)
    }

    fun setData() {
        adapter.setItemsAndAnimateChanges(List(PLACEHOLDER_COUNT) { PlayWidgetUiModel.Placeholder })
    }

    companion object {
        private const val PLACEHOLDER_COUNT = 5
    }
}