package com.tokopedia.home.beranda.data.newatf

import com.tokopedia.home.constant.AtfKey
import javax.inject.Inject

class AtfRepository @Inject constructor(
    private val dynamicPositionRepository: DynamicPositionRepository,
    private val bannerRepository: BannerRepository,
    private val iconRepository: DynamicIconRepository,
) {
    suspend fun getDynamicPosition() {
        dynamicPositionRepository.flow.collect { value ->
            value.listAtfMetadata.forEach { metadata ->
                when(metadata.component) {
                    AtfKey.TYPE_BANNER -> bannerRepository.getData(metadata.id.toString(), value.source)
                    AtfKey.TYPE_ICON -> iconRepository
                }
            }
            if(value.source == AtfSource.CACHE) dynamicPositionRepository.getData()
            else dynamicPositionRepository.getRemoteData()
        }
    }

//    suspend fun getAtf(): Flow<List<Visitable<*>>> {
//        dynamicPositionRepository.getCachedData()
//    }
}
