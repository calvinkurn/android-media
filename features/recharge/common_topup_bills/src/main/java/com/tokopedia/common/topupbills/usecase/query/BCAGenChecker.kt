package com.tokopedia.common.topupbills.usecase.query

import com.tokopedia.common.topupbills.usecase.query.BCAGenChecker.BCA_GEN_CHECKER_OPERATION_NAME
import com.tokopedia.common.topupbills.usecase.query.BCAGenChecker.BCA_GEN_CHECKER_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(BCA_GEN_CHECKER_OPERATION_NAME, BCA_GEN_CHECKER_QUERY)
object BCAGenChecker {
    const val BCA_GEN_CHECKER_OPERATION_NAME = "BCAGenCheckerQuery"
    const val BCA_GEN_CHECKER_QUERY = """
        query $BCA_GEN_CHECKER_OPERATION_NAME(${'$'}input: DigiPersoGetPersonalizedItemsRequest!){
            digiPersoGetPersonalizedItems(input: ${'$'}input) {
                items {
    	             id
                     label1
                     label2
                }
            }
        }
    """
}
