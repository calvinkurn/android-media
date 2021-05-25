package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetUserShopInfoUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) {

}