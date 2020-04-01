package com.tokopedia.product.addedit.description.domain.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RestResponse
import java.lang.reflect.Type
import javax.inject.Inject

class GetYoutubeVideoUseCase @Inject constructor(
        private val repository: RestRepository
): RestRequestUseCase(repository) {

    companion object {
        val YOUTUBE_API_KEY = "AIzaSyADrnEdJGwsVM1Z6uWWnWAgZZf1sSfnIVQ"
        val YOUTUBE_LINK = "https://www.googleapis.com/youtube/v3/videos"
        val PART = "part"
        val ID = "id"
        val KEY = "key"
        val SNIPPET_CONTENT_DETAILS = "snippet,contentDetails"
    }

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        return super.executeOnBackground()
    }


}