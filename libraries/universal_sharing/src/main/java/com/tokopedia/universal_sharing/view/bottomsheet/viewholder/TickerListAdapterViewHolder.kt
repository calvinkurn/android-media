package com.tokopedia.universal_sharing.view.bottomsheet.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyprinciples.Typography

class TickerListAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title = view.findViewById<Typography>(com.tokopedia.universal_sharing.R.id.tv_title_ticker)
    val description = view.findViewById<Typography>(com.tokopedia.universal_sharing.R.id.tv_description_ticker)
    val image = view.findViewById<ImageView>(com.tokopedia.universal_sharing.R.id.iv_ticker)
}
