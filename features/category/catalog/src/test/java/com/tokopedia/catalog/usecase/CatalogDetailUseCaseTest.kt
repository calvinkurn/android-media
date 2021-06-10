package com.tokopedia.catalog.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.catalog.model.datamodel.CatalogDetailDataModel
import com.tokopedia.catalog.model.raw.CatalogResponseData
import com.tokopedia.catalog.repository.catalogdetail.CatalogDetailRepository
import com.tokopedia.catalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.catalog.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.lang.reflect.Type

class CatalogDetailUseCaseTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    val catalogDetailRepository : CatalogDetailRepository = mockk(relaxed = true)
    var catalogDetailUseCase = spyk(CatalogDetailUseCase(catalogDetailRepository))

    @Test
    fun `Get Catalog Detail Response`() {

    }
}