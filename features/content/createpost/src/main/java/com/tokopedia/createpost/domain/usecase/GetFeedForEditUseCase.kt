package com.tokopedia.createpost.domain.usecase

import com.tokopedia.createpost.domain.entity.FeedDetail
import com.tokopedia.createpost.view.viewmodel.MediaModel
import com.tokopedia.createpost.view.viewmodel.MediaType
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.MultimediaGridViewModel
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
        val media = this.contentList.flatMap { getMediaModel(it) }
        val postTagId = this.postTag.items.map { it.id }.filter { it.isNotBlank() }
        val caption = this.caption.text
        return FeedDetail(postId, type, media, postTagId, caption)
    }

    private fun getMediaModel(item: BasePostViewModel): List<MediaModel> {
        return when(item){
            is ImagePostViewModel -> listOf(MediaModel(item.image, MediaType.IMAGE))
            is VideoViewModel -> listOf(MediaModel(item.url, MediaType.VIDEO))
            is MultimediaGridViewModel -> getMediaModelFromMultimediaGrid(item)
            else -> emptyList()
        }
    }

    private fun getMediaModelFromMultimediaGrid(item: MultimediaGridViewModel): List<MediaModel> {
        return item.mediaItemList.map {
            MediaModel(
                    path = it.thumbnail,
                    type = it.type
            )
        }
    }
}