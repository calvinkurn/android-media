package com.tokopedia.tokochat_common.view.adapter.delegate.experiment

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.tokochat_common.view.adapter.viewholder.experiment.StringViewHolder
import com.tokopedia.tokochat_common.view.uimodel.StringUiModel

class StringDelegate: TypedAdapterDelegate<StringUiModel, Any, StringViewHolder>(StringViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: StringUiModel, holder: StringViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): StringViewHolder {
        return StringViewHolder(basicView)
    }
}
