package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.salam.umrah.travel.data.UmrahGalleriesEntity
import com.tokopedia.salam.umrah.travel.data.UmrahGalleriesInput
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UmrahTravelGalleryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      val dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val mutableGalleryResult = MutableLiveData<Result<UmrahGalleriesEntity>>()
    val galleryResult: LiveData<Result<UmrahGalleriesEntity>>
        get() = mutableGalleryResult


    fun getDataGallery(page: Int, travelSlugName: String, rawQuery: String) {
        galleryParam.entitySlugName = travelSlugName
        galleryParam.page = page
        galleryParam.limit = PARAM_LIMIT
        galleryParam.entityName = listOf(PARAM_ENTITY_LIMIT)
        launchCatchError(block = {
            val params = mapOf(PARAM_UMRAH_GALLERY to galleryParam)
            val graphqlRequest = GraphqlRequest(rawQuery, UmrahGalleriesEntity::class.java, params)

            val response = withContext(dispatcher.io) {
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }
            mutableGalleryResult.value = Success(response.getSuccessData())
        }) {
            mutableGalleryResult.value = Fail(it)
        }
    }

    companion object {
        private var galleryParam: UmrahGalleriesInput = UmrahGalleriesInput()
        const val PARAM_UMRAH_GALLERY = "params"
        const val PARAM_LIMIT = 5
        const val PARAM_ENTITY_LIMIT = "TRAVEL_AGENT"
    }

}