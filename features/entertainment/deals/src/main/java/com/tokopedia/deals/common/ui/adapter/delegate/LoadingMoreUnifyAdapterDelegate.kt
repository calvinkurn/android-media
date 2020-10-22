package com.tokopedia.deals.common.ui.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.deals.common.model.LoadingMoreUnifyModel
import com.tokopedia.deals.common.ui.adapter.viewholder.LoadingMoreUnifyViewHolder

class LoadingMoreUnifyAdapterDelegate : TypedAdapterDelegate<LoadingMoreUnifyModel, Any, LoadingMoreUnifyViewHolder>(LoadingMoreUnifyViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: LoadingMoreUnifyModel, holder: LoadingMoreUnifyViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): LoadingMoreUnifyViewHolder {
        return LoadingMoreUnifyViewHolder(basicView)
    }
}