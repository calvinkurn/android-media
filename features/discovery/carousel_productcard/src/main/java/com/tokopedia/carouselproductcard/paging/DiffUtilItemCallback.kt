package com.tokopedia.carouselproductcard.paging

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

internal class DiffUtilItemCallback: DiffUtil.ItemCallback<Visitable<TypeFactory>>() {

    override fun areItemsTheSame(
        oldItem: Visitable<TypeFactory>,
        newItem: Visitable<TypeFactory>
    ): Boolean = false

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: Visitable<TypeFactory>,
        newItem: Visitable<TypeFactory>
    ): Boolean = oldItem == newItem

}
