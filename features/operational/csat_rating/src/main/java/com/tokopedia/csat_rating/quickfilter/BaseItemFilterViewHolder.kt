package com.tokopedia.csat_rating.quickfilter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseItemFilterViewHolder(itemView: View, protected var listener: QuickSingleFilterListener) : RecyclerView.ViewHolder(itemView)