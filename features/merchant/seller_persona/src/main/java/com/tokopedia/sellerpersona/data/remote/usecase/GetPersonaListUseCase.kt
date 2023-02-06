package com.tokopedia.sellerpersona.data.remote.usecase

import com.google.gson.Gson
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerpersona.data.remote.mapper.PersonaListMapper
import com.tokopedia.sellerpersona.data.remote.model.FetchPersonaListResponse
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 29/01/23.
 */

@GqlQuery("FetchPersonaListQuery", GetPersonaListUseCase.QUERY)
class GetPersonaListUseCase @Inject constructor(
    private val mapper: PersonaListMapper,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<FetchPersonaListResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(FetchPersonaListQuery())
        setTypeClass(FetchPersonaListResponse::class.java)
    }

    suspend fun execute(): List<PersonaUiModel> {
        try {
            val response = Gson().fromJson(dummy, FetchPersonaListResponse::class.java)
            //val response = executeOnBackground()
            if (response.data.error) {
                throw MessageErrorException(response.data.errorMsg)
            }
            return mapper.mapToUiModel(response.data.personaList)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    companion object {
        const val QUERY = """
            
        """
        const val dummy = """
            {
                "fetchPersonaListData" : {
                    "error": false,
                    "errorMsg": "",
                    "data": [
                        {
                            "name": "mom-and-pop",
                            "header": {
                                "title": "Rumahan",
                                "subtitle": "Pemilik Toko",
                                "image": "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_avatar_rumahan-min.png",
                                "backgroundImage": "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_background_rumahan-min.png"
                            },
                            "body": {
                                "title": "Pilih tipe ini jika kamu:",
                                "itemList": [
                                    "Menerima min. 2 pesanan per minggu",
                                    "Belum punya toko fisik (offline)",
                                    "Mengurus sendiri operasional toko",
                                    "Mengakses Tokopedia Seller di desktop"
                                ]
                            }
                        },
                        {
                            "name": "corporate-supervisor-owner",
                            "header": {
                                "title": "Gedongan",
                                "subtitle": "Pemilik Toko",
                                "image": "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_avatar_gedongan-min.png",
                                "backgroundImage": "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_background_gedongan-min.png"
                            },
                            "body": {
                                "title": "Pilih tipe ini jika kamu:",
                                "itemList": [
                                    "Menerima 1-10 pesanan per hari",
                                    "Punya toko fisik (offline)",
                                    "Punya pegawai yang mengurus operasional toko"
                                ]
                            }
                        },
                        {
                            "name": "corporate-employee",
                            "header": {
                                "title": "Gedongan",
                                "subtitle": "Admin Toko",
                                "image": "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_avatar_gedongan-min.png",
                                "backgroundImage": "https://images.tokopedia.net/img/android/sellerapp/seller_persona/img_persona_background_gedongan-min.png"
                            },
                            "body": {
                                "title": "Pilih tipe ini jika kamu:",
                                "itemList": [
                                    "Menerima 1-10 pesanan per hari",
                                    "Punya toko fisik (offline)",
                                    "Mengurus operasional toko",
                                    "Mengakses Tokopedia Seller di desktop dan aplikasi HP",
                                    "Mengakses Tokopedia Seller di desktop dan aplikasi HP"
                                ]
                            }
                        }
                    ]
                }
            }
        """
    }
}