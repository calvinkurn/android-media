package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.home.beranda.data.repository.HomeRepository
import javax.inject.Inject

class GetHomeTokopointsDataUseCase @Inject constructor(
        private val repository: HomeRepository
){
    fun execute() = repository.getTokopoints()
}