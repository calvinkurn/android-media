package com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.MultiComponentItemUiModel

abstract class MultiComponentItemViewHolder<T: MultiComponentItemUiModel>(itemView: View): RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(data: T)

}
