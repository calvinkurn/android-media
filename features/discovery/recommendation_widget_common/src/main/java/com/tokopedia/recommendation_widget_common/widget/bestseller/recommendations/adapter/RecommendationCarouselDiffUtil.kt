package com.tokopedia.recommendation_widget_common.widget.bestseller.recommendations.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by Lukas on 03/08/20.
 */

class RecommendationCarouselDiffUtil(
        private val oldList: List<Visitable<*>>,
        private val newList: List<Visitable<*>>
) : DiffUtil.Callback(){
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]::class.java == newList[newItemPosition]::class.java
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return Bundle()
    }
}