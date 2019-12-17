package com.tokopedia.attachinvoice.usecase

import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class GetInvoiceUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetInvoiceResponse>
) {

}