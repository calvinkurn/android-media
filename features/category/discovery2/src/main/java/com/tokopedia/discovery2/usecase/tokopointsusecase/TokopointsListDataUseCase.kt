package com.tokopedia.discovery2.usecase.tokopointsusecase

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.tokopoints.TokopointsRepository
import javax.inject.Inject

class TokopointsListDataUseCase @Inject constructor(private val tokopointsRepository: TokopointsRepository) {
    companion object {
        private const val RPC_PAGE_NUMBER_KEY = "rpc_page_number"
        private const val RPC_PAGE_SIZE = "rpc_page_size"
    }

    suspend fun getTokopointsDataUseCase(componentId: String, pageEndPoint: String): Boolean {
        val componentsItem = getComponent(componentId, pageEndPoint)
        componentsItem?.let { component ->
            component?.setComponentsItem(tokopointsRepository.getTokopointsData(componentId, getQueryParameterMap(1.toString(), 6), pageEndPoint), component.tabName)
            component?.noOfPagesLoaded = 1
            return true
        }

        return false

    }

    private fun getQueryParameterMap(pageNum: String, productPerPage: Int): MutableMap<String, Any> {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap[RPC_PAGE_NUMBER_KEY] = pageNum
        queryParameterMap[RPC_PAGE_SIZE] = productPerPage
        return queryParameterMap
    }
}