package com.tokopedia.discovery2.viewcontrollers.adapter.mergeAdapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder

class LocalAdapter<T : RecyclerView.Adapter<AbstractViewHolder>>(adapter: T) {
    var mAdapter: T = adapter
    var mLocalPosition = 0
    var dataPosition = 0
    var mViewTypesMap: HashMap<Int, Int> = HashMap()
}