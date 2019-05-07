package com.tokopedia.discovery.common.repository.gql

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.common.repository.Specification

abstract class GqlSpecification(val context: Context) : Specification {

    override fun getQuery(): String {
        return GraphqlHelper.loadRawString(context.resources, getResources())
    }

    abstract fun getResources() : Int
}