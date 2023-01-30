package com.tokopedia.sellerpersona.data.remote.usecase

import com.google.gson.Gson
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerpersona.data.remote.mapper.QuestionnaireMapper
import com.tokopedia.sellerpersona.data.remote.model.FetchPersonaQuestionnaireResponse
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

@GqlQuery("FetchPersonaQuestionnaireGqlQuery", GetPersonaQuestionnaireUseCase.QUERY)
class GetPersonaQuestionnaireUseCase @Inject constructor(
    private val mapper: QuestionnaireMapper,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<FetchPersonaQuestionnaireResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(FetchPersonaQuestionnaireGqlQuery())
        setTypeClass(FetchPersonaQuestionnaireResponse::class.java)
    }

    suspend fun execute(): List<QuestionnairePagerUiModel> {
        try {
            val response = Gson().fromJson(DUMMY, FetchPersonaQuestionnaireResponse::class.java)
            //val response = super.executeOnBackground()
            if (response.data.error) {
                throw MessageErrorException(response.data.errorMsg)
            }
            return mapper.mapToUiModel(response.data.questionnaire)
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

    companion object {
        const val QUERY = """
           
        """

        private const val DUMMY = """
            {
                "fetchUserPersonaQuestionnaire" : {
                    "error": false,
                    "errorMsg": "",
                    "questionnaire": [
                        {
                            "id": 1,
                            "question": {
                                "title": "Apa peranmu di Tokopedia?"
                            },
                            "type": 1,
                            "options": [
                                {
                                    "value": "a",
                                    "title": "Pemilik toko"
                                },
                                {
                                    "value": "b",
                                    "title": "Supervisor (bukan pemilik toko)"
                                },
                                {
                                    "value": "c",
                                    "title": "Admin/pegawai"
                                }
                            ]
                        },
                        {
                            "id": 2,
                            "question": {
                                "title": "Apa hal yang biasa kamu lakukan di dashboard penjual Tokopedia?",
                                "subtitle": "(Catatan: Kamu bisa pilih lebih dari 1 jawaban)"
                            },
                            "type": 2,
                            "options": [
                                {
                                    "value": "a",
                                    "title": "Melakukan kegiatan operasional toko (contoh: balas chat dan diskusi, proses pesanan, pickup request, update stok produk, dan sebagainya)"
                                },
                                {
                                    "value": "b",
                                    "title": "Memonitor kondisi toko (termasuk performa iklan dan promo yang bisa diikuti)"
                                },
                                {
                                    "value": "c",
                                    "title": "Mengatur/memasang promosi toko (termasuk iklan)"
                                },
                                {
                                    "value": "d",
                                    "title": "Melakukan penarikan Saldo Penghasilan"
                                },
                                {
                                    "value": "e",
                                    "title": "Mengurus pengajuan pembatalan pesanan dan komplain"
                                }
                            ]
                        },
                        {
                            "id": 3,
                            "question": {
                                "title": "Berapa jumlah pegawai yang membantu aktivitas operasional sehari-hari di toko online?",
                                "subtitle": "(Catatan: \"Pegawai\" dalam hal ini adalah seseorang yang dipekerjakan dan digaji khusus untuk membantu aktivitas operasional sehari-hari di toko online)"
                            },
                            "type": 1,
                            "options": [
                                {
                                    "value": "a",
                                    "title": "1 pegawai"
                                },
                                {
                                    "value": "b",
                                    "title": "2 pegawai"
                                },
                                {
                                    "value": "c",
                                    "title": "3 hingga 5 pegawai"
                                },
                                {
                                    "value": "d",
                                    "title": "Lebih dari 5 pegawai"
                                },
                                {
                                    "value": "e",
                                    "title": "Tidak memiliki pegawai"
                                }
                            ],
                            "answersToSkipQuestions": [
                                {
                                    "answers": ["e"],
                                    "nextQuestionId": 5
                                }
                            ]
                        },
                        {
                            "id": 4,
                            "question": {
                                "title": "Manakah pernyataan berikut yang sesuai dengan kondisi tokomu?"
                            },
                            "type": 1,
                            "options": [
                                {
                                    "value": "a",
                                    "title": "Pegawai berwenang penuh mengambil keputusan utama strategi penjualan (termasuk promosi)"
                                },
                                {
                                    "value": "b",
                                    "title": "Pegawai tidak sepenuhnya berperan sebagai pengambil keputusan utama strategi penjualan (termasuk promosi)"
                                },
                                {
                                    "value": "c",
                                    "title": "Pegawai tidak memiliki peran apapun dalam pengambilan keputusan strategi penjualan  (termasuk promosi)"
                                }
                            ]
                        },
                        {
                            "id": 5,
                            "question": {
                                "title": "Berapa banyak jumlah toko fisik (offline store) yang dimiliki?"
                            },
                            "type": 1,
                            "options": [
                                {
                                    "value": "a",
                                    "title": "Hanya 1 toko fisik"
                                },
                                {
                                    "value": "b",
                                    "title": "2-3 toko fisik"
                                },
                                {
                                    "value": "c",
                                    "title": "4-5 toko fisik"
                                },
                                {
                                    "value": "d",
                                    "title": "6-10 toko fisik"
                                },
                                {
                                    "value": "e",
                                    "title": "Lebih dari 10 toko fisik"
                                },
                                {
                                    "value": "f",
                                    "title": "Tidak memiliki toko fisik"
                                }
                            ]
                        }
                    ]
                }
           }
        """
    }
}