package com.tokopedia.profilecompletion.common

import com.tokopedia.graphql.data.model.GraphqlError

data class SubmitProfileError(val data: GraphqlError, val errorMsg: String): Exception(errorMsg)
