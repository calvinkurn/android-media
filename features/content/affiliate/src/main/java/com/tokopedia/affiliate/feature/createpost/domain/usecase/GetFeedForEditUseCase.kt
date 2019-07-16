package com.tokopedia.affiliate.feature.createpost.domain.usecase

import com.tokopedia.affiliate.feature.createpost.domain.entity.FeedDetail
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.MediaModel
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.MediaType
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class GetFeedForEditUseCase @Inject constructor(private val dynamicFeedUseCase: GetDynamicFeedUseCase): UseCase<FeedDetail?>() {

    override fun createObservable(params: RequestParams?): Observable<FeedDetail?> {
        return dynamicFeedUseCase.createObservable(params)
                .map { it.postList.firstOrNull() }
                .map { (it as DynamicPostViewModel?)?.toFeedDetailForEditing()
        }
    }

    private fun DynamicPostViewModel.toFeedDetailForEditing(): FeedDetail{
        val postId = this.id.toString()
        val type = this.feedType
        val media = this.contentList.mapNotNull { getMediaModel(it) }
        val postTagId = this.postTag.items.map { it.id }.filter { it.isNotBlank() }
        val caption = this.caption.text
        return FeedDetail(postId, type, media, postTagId, caption)

    }

    private fun getMediaModel(item: BasePostViewModel): MediaModel?{
        return when(item){
            is ImagePostViewModel -> MediaModel(item.image, MediaType.IMAGE)
            is VideoViewModel -> MediaModel(item.url, MediaType.VIDEO)
            else -> null
        }
    }
}