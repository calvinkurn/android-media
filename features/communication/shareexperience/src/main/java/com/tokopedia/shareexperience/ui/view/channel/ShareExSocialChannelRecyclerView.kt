package com.tokopedia.shareexperience.ui.view.channel

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelItemModel
import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import com.tokopedia.shareexperience.ui.adapter.channel.ShareExBaseChannelAdapter
import com.tokopedia.shareexperience.ui.adapter.channel.ShareExCommonChannelAdapter
import com.tokopedia.shareexperience.ui.adapter.channel.ShareExSocialChannelAdapter

class ShareExSocialChannelRecyclerView: ShareExBaseChannelRecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val adapter = ShareExSocialChannelAdapter()

    override fun getChannelAdapter(): ShareExBaseChannelAdapter {
        return adapter
    }

    init {
        init()
    }

    fun updateData(newData: ShareExChannelModel) {
        //TODO: set description
        adapter.updateData(newData.listChannel)
    }
}
