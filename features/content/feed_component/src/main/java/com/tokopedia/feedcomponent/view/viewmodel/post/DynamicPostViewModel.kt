package com.tokopedia.feedcomponent.view.viewmodel.post

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.*
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by milhamj on 28/11/18.
 */
data class DynamicPostViewModel(
        val id: Int = 0,
        val title: Title = Title(),
        val header: Header = Header(),
        val postTag: PostTag = PostTag(),
        val footer: Footer = Footer(),
        val caption: Caption = Caption(),
        var contentList: MutableList<BasePostViewModel> = mutableListOf(),
        val template: Template = Template(),
        val trackingPostModel: TrackingPostModel = TrackingPostModel(),
        val tracking: MutableList<TrackingViewModel> = mutableListOf(),
        val feedType: String = "",
        val activityName: String = "",
        val impressHolder: ImpressHolder = ImpressHolder()

) : Visitable<DynamicFeedTypeFactory> {
    
    override fun type(typeFactory: DynamicFeedTypeFactory?): Int {
        return typeFactory!!.type(this)
    }

    fun copy(): DynamicPostViewModel {
        val newTrackingList: MutableList<TrackingViewModel> = arrayListOf()
        for (track in tracking) {
            newTrackingList.add(track.copy())
        }
        return DynamicPostViewModel(
                id,
                title.copy(),
                header.copy(),
                postTag.copy(),
                footer.copy(),
                caption.copy(),
                contentList,
                template,
                trackingPostModel.copy(),
                newTrackingList,
                feedType,
                activityName,
                impressHolder
        )
    }
}