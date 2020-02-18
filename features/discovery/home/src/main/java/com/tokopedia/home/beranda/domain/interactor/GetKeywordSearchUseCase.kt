package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.home.beranda.data.repository.HomeRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetKeywordSearchUseCase @Inject constructor(
        private val repository: HomeRepository
){
    fun execute() = repository.getKeywordSearch()
}