package com.tokopedia.attachproduct

import com.tokopedia.attachproduct.fake.di.GraphqlRepositoryStub
import javax.inject.Inject

class AttachProductDepedency {

    @Inject
    lateinit var repositoryStub: GraphqlRepositoryStub
}