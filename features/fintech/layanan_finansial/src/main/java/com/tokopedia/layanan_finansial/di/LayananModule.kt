package com.tokopedia.layanan_finansial.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.layanan_finansial.R
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class LayananModule(private val context: Context) {

    @Provides
    fun getContext() = context

    @Provides
    fun getRepository() = Interactor.getInstance().graphqlRepository

    @Provides
    fun getUseCase(reppository : GraphqlRepository) = MultiRequestGraphqlUseCase(reppository)

    @Provides
    @Named(LAYANAN_DETAIL_QUERY)
    fun getLayananQurey() : String {
        return GraphqlHelper.loadRawString(context.getResources(),
                R.raw.lf_query_layanan_detail)
    }

    companion object{
        const val LAYANAN_DETAIL_QUERY = "lf_query_layanan_detail"
    }
}