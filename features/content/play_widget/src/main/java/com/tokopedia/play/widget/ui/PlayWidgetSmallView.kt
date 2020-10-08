package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.adapter.PlayWidgetCardSmallAdapter
import com.tokopedia.play.widget.ui.itemdecoration.PlayWidgetCardSmallItemDecoration
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSmallView : ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val tvTitle: TextView
    private val tvSeeAll: TextView
    private val rvWidgetCardSmall: RecyclerView

    private val adapter = PlayWidgetCardSmallAdapter()

    init {
        val view = View.inflate(context, R.layout.view_play_widget_small, this)
        tvTitle = view.findViewById(R.id.tv_title)
        tvSeeAll = view.findViewById(R.id.tv_see_all)
        rvWidgetCardSmall = view.findViewById(R.id.rv_widget_card_small)

        setupView(view)
    }

    fun setData(data: PlayWidgetUiModel.Small) {
        when (data) {
            PlayWidgetUiModel.Small.Empty -> {}
            is PlayWidgetUiModel.Small.Widget -> {
                tvTitle.text = data.title
                tvSeeAll.text = data.actionTitle

                adapter.setItemsAndAnimateChanges(data.items)
            }
        }
    }

    private fun setupView(view: View) {
        rvWidgetCardSmall.adapter = adapter
        rvWidgetCardSmall.addItemDecoration(PlayWidgetCardSmallItemDecoration(context))
    }
}