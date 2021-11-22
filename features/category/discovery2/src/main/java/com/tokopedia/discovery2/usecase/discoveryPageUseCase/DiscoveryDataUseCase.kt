package com.tokopedia.discovery2.usecase.discoveryPageUseCase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery2.datamapper.DiscoveryPageData
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.datamapper.mapDiscoveryResponseToPageData
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class DiscoveryDataUseCase @Inject constructor(private val discoveryPageRepository: DiscoveryPageRepository,@ApplicationContext private val  context: Context) {

    suspend fun getDiscoveryPageDataUseCase(pageIdentifier: String,
                                            queryParameterMap: MutableMap<String, String?>,
                                            userAddressData: LocalCacheModel?): DiscoveryPageData {
        var userAddressDataCopy = userAddressData
        return mapDiscoveryResponseToPageData(discoveryPageData[pageIdentifier]?.let {
            it
        } ?: discoveryPageRepository.getDiscoveryPageData(pageIdentifier).apply {
            discoveryPageData[pageIdentifier] = this
            componentMap = HashMap()
            if(this.pageInfo.showChooseAddress && userAddressDataCopy == null)
                userAddressDataCopy = ChooseAddressUtils.getLocalizingAddressData(context)
            /***Chip Filter Require parent ID to function. Need to check on this later.***/
//            component = ComponentsItem(id = "PARENT_ID",pageEndPoint = pageInfo.identifier?:"").apply {
//                componentMap[id] = this
//            }
        }, queryParameterMap, userAddressDataCopy,UserSession(context).isLoggedIn)
    }

    fun clearPage(pageIdentifier: String) {
        discoveryPageData.remove(pageIdentifier)
    }
}
