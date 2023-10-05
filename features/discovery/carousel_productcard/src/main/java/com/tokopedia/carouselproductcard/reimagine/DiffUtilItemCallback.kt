package com.tokopedia.carouselproductcard.reimagine

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

internal class DiffUtilItemCallback(
    private val typeFactory: CarouselProductCardTypeFactory,
): DiffUtil.ItemCallback<Visitable<CarouselProductCardTypeFactory>>() {

    override fun areItemsTheSame(
        oldItem: Visitable<CarouselProductCardTypeFactory>,
        newItem: Visitable<CarouselProductCardTypeFactory>
    ): Boolean = oldItem.type(typeFactory) == newItem.type(typeFactory)

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: Visitable<CarouselProductCardTypeFactory>,
        newItem: Visitable<CarouselProductCardTypeFactory>
    ): Boolean = oldItem == newItem

}
