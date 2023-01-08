package com.tokopedia.notifications.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifications.domain.data.NotificationSettingTrackerGqlResponse
import com.tokopedia.notifications.domain.data.SettingTrackerResponse
import com.tokopedia.notifications.domain.query.GQL_QUERY_SEND_NOTIF_SETTINGS_TRACKER_DATA
import com.tokopedia.notifications.domain.query.GetNotificationSettingTrackerGQLQuery
import javax.inject.Inject
import kotlin.collections.HashMap


@GqlQuery("GetCMHomeWidgetData", GQL_QUERY_SEND_NOTIF_SETTINGS_TRACKER_DATA)
class NotificationSettingTrackerUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<NotificationSettingTrackerGqlResponse>(graphqlRepository) {

    fun sendTrackerUserSettings(
        onSuccess: (SettingTrackerResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(NotificationSettingTrackerGqlResponse::class.java)
            this.setGraphqlQuery(GetNotificationSettingTrackerGQLQuery())
            this.setRequestParams(getRequestParams())
            this.execute(
                { result ->
                    if (result.settingTrackerResponse.isSuccess == 1) {
                        onSuccess(result.settingTrackerResponse)
                    } else {
                        onError(Throwable())
                    }
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(): HashMap<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[TYPE] = "pushnotif"
        requestParams[DATA] = getNotificationSettingsList()
        return requestParams
    }

    private fun getNotificationSettingsList(): ArrayList<HashMap<String, Any>> {
        val transaksiPenjualan =
            hashMapOf<String, Any>(NAME to "transaksi_penjualan", VALUE to true)
        val transaksiPembelian =
            hashMapOf<String, Any>(NAME to "transaksi_pembelian", VALUE to true)
        val chatDariTokopedia = hashMapOf<String, Any>(NAME to "chat_dari_tokopedia", VALUE to true)
        val chatPromosiDariPenjual =
            hashMapOf<String, Any>(NAME to "chat_promosi_dari_penjual", VALUE to true)
        val chatPersonal = hashMapOf<String, Any>(NAME to "chat_personal", VALUE to true)
        val diskusiProduk = hashMapOf<String, Any>(NAME to "diskusi_produk", VALUE to true)
        val ulasan = hashMapOf<String, Any>(NAME to "ulasan", VALUE to true)
        val aktivitas = hashMapOf<String, Any>(NAME to "aktivitas", VALUE to true)
        val aktivitasFeed = hashMapOf<String, Any>(NAME to "aktivitas_feed", VALUE to true)
        val aktivitasPemberitahuan =
            hashMapOf<String, Any>(NAME to "aktivitas_pemberitahuan", VALUE to true)
        val aktivitasPenawaranKhusus =
            hashMapOf<String, Any>(NAME to "aktivitas_penawaran_khusus", VALUE to true)
        val promoUntukPembeli = hashMapOf<String, Any>(NAME to "promo_untuk_pembeli", VALUE to true)
        val promoUntukPenjual = hashMapOf<String, Any>(NAME to "promo_untuk_penjual", VALUE to true)
        val infoUntukPembeli = hashMapOf<String, Any>(NAME to "info_untuk_pembeli", VALUE to true)
        val infoUntukPenjual = hashMapOf<String, Any>(NAME to "info_untuk_penjual", VALUE to true)
        val infoMonthlyReport = hashMapOf<String, Any>(NAME to "info_monthly_report", VALUE to true)
        val infoWeeklyReport = hashMapOf<String, Any>(NAME to "info_weekly_report", VALUE to true)
        val tokopediaPlay = hashMapOf<String, Any>(NAME to "tokopedia_play", VALUE to true)
        val bulletinNewsletter =
            hashMapOf<String, Any>(NAME to "bulletin_newsletter", VALUE to true)
        val promo = hashMapOf<String, Any>(NAME to "promo", VALUE to true)
        val salamSholatImsak = hashMapOf<String, Any>(NAME to "salam_sholat_imsak", VALUE to true)
        val salamSholatSubuh = hashMapOf<String, Any>(NAME to "salam_sholat_subuh", VALUE to true)
        val salamSholatDhuha = hashMapOf<String, Any>(NAME to "salam_sholat_dhuha", VALUE to true)
        val salamSholatDzuhur = hashMapOf<String, Any>(NAME to "salam_sholat_dzuhur", VALUE to true)
        val salamSholatAshar = hashMapOf<String, Any>(NAME to "salam_sholat_ashar", VALUE to true)
        val salamSholatMaghrib =
            hashMapOf<String, Any>(NAME to "salam_sholat_maghrib", VALUE to true)
        val salamSholatIsya = hashMapOf<String, Any>(NAME to "salam_sholat_isya", VALUE to true)
        val salamQuranAyatTerakhir =
            hashMapOf<String, Any>(NAME to "salam_quran_ayat_terakhir", VALUE to true)
        val salamGuranDoaHarian =
            hashMapOf<String, Any>(NAME to "salam_quran_doa_harian", VALUE to true)

        return arrayListOf(
            transaksiPenjualan,
            transaksiPembelian,
            chatDariTokopedia,
            chatPromosiDariPenjual,
            chatPersonal,
            diskusiProduk,
            ulasan,
            aktivitas,
            aktivitasFeed,
            aktivitasPemberitahuan,
            aktivitasPenawaranKhusus,
            promoUntukPembeli,
            promoUntukPenjual,
            infoUntukPembeli,
            infoUntukPenjual,
            infoMonthlyReport,
            infoWeeklyReport,
            tokopediaPlay,
            bulletinNewsletter,
            promo,
            salamSholatImsak,
            salamSholatSubuh,
            salamSholatDhuha,
            salamSholatDzuhur,
            salamSholatAshar,
            salamSholatMaghrib,
            salamSholatIsya,
            salamQuranAyatTerakhir,
            salamGuranDoaHarian
        )
    }

    companion object {
        private const val NAME = "name"
        private const val VALUE = "value"
        private const val DATA = "data"
        private const val TYPE = "type"
    }
}