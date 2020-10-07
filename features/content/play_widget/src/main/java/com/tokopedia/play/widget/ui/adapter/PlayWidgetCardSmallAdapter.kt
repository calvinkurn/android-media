package com.tokopedia.play.widget.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.adapter.viewholder.PlayWidgetCardSmallViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetCardUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetCardSmallAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mCardList: MutableList<PlayWidgetCardUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlayWidgetCardSmallViewHolder(
                LayoutInflater.from(parent.context).inflate(PlayWidgetCardSmallViewHolder.LAYOUT, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mCardList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlayWidgetCardSmallViewHolder).bind(mCardList[position])
    }

    fun setList(itemList: List<PlayWidgetCardUiModel>) {
        mCardList.clear()
        mCardList.addAll(itemList)
    }
}