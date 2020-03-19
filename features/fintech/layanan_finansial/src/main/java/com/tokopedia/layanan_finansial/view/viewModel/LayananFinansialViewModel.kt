package com.tokopedia.layanan_finansial.view.viewModel

import android.telephony.gsm.GsmCellLocation
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.layanan_finansial.di.LayananScope
import com.tokopedia.layanan_finansial.view.models.LayananFinansialModel
import com.tokopedia.layanan_finansial.view.models.LayananFinansialOuter
import com.tokopedia.layanan_finansial.view.usecase.LayananUsecase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import java.lang.NullPointerException
import javax.inject.Inject

@LayananScope
class LayananFinansialViewModel @Inject constructor(private val useCase: LayananUsecase) : BaseViewModel(Dispatchers.Main) {
    val liveData = MutableLiveData<Result<LayananFinansialModel>>()

    fun getDetail(){
        launchCatchError(block = {
          val data = getdata().data
            data?.let {
                liveData.value = Success(data = data)
            } ?: throw NullPointerException()

        }){
          liveData.value = Fail(it)
        }
    }

    fun getdata() : LayananFinansialOuter{
        return Gson().fromJson("{\n" +
                "    \"ft_genie_financial_widget\": {\n" +
                "      \"financial_widget\": [\n" +
                "        {\n" +
                "          \"title\": \"Layanan yang bisa kamu pakai\",\n" +
                "          \"subtitle\": \"Cek dan gunakan layanan ini untuk kembangkan tokomu!\",\n" +
                "          \"type\": \"vertical\",\n" +
                "          \"background\": \"#D4FBE6\",\n" +
                "          \"widget_list\": [\n" +
                "            {\n" +
                "              \"datalayer_status\": \"ELIGIBLE\",\n" +
                "              \"status\": \"Sedang Diproses\",\n" +
                "              \"status_text_color\": \"#ED5F0E\",\n" +
                "              \"status_bg_color\": \"#FFEEC9\",\n" +
                "              \"image_url\": \"https://ecs7.tokopedia.net/assets-fintech-frontend/staging/ic_modal-toko.png\",\n" +
                "              \"category\": \"Tambahan Modal\",\n" +
                "              \"name\": \"Modal Toko\",\n" +
                "              \"desc_1\": \"Potensi Limit\",\n" +
                "              \"desc_2\": \"Rp 10.000.000\",\n" +
                "              \"cta\": \"Ajukan Kembali\",\n" +
                "              \"url\": \"https://tokopedia.com/fm/modal-toko/\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"ELIGIBLE\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://ecs7.tokopedia.net/assets-fintech-frontend/staging/ic_saldo-prioritas.png\",\n" +
                "              \"category\": \"Percepat Pemasukan\",\n" +
                "              \"name\": \"Saldo Prioritas\",\n" +
                "              \"desc_1\": \"Tarik hasil jualan tanpa tunggu transaksi selesai\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"Aktifkan\",\n" +
                "              \"url\": \"https://www.tokopedia.com/saldo-prioritas/\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://ecs7.tokopedia.net/img/toppay/banner/bri-oba-banner2.jpg\",\n" +
                "              \"category\": \"PERBANKAN\",\n" +
                "              \"name\": \"Buka Rekening\",\n" +
                "              \"desc_1\": \"Anti ribet! Buka rekening tabungan bisa langsung dari HP kamu.\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"Lihat Detail\",\n" +
                "              \"url\": \"https://38.staging-feature.tokopedia.com/login?ld=/payment/open-bank-account/dashboard\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://ecs7.tokopedia.net/img/toppay/banner/bri-oba-banner2.jpg\",\n" +
                "              \"category\": \"PERBANKAN\",\n" +
                "              \"name\": \"Buka Rekening\",\n" +
                "              \"desc_1\": \"Anti ribet! Buka rekening tabungan bisa langsung dari HP kamu.\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"Lihat Detail\",\n" +
                "              \"url\": \"https://38.staging-feature.tokopedia.com/login?ld=/payment/open-bank-account/dashboard\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://ecs7.tokopedia.net/img/toppay/banner/bri-oba-banner2.jpg\",\n" +
                "              \"category\": \"PERBANKAN\",\n" +
                "              \"name\": \"Buka Rekening\",\n" +
                "              \"desc_1\": \"Anti ribet! Buka rekening tabungan bisa langsung dari HP kamu.\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"Lihat Detail\",\n" +
                "              \"url\": \"https://38.staging-feature.tokopedia.com/login?ld=/payment/open-bank-account/dashboard\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://ecs7.tokopedia.net/img/toppay/banner/bri-oba-banner2.jpg\",\n" +
                "              \"category\": \"PERBANKAN\",\n" +
                "              \"name\": \"Buka Rekening\",\n" +
                "              \"desc_1\": \"Anti ribet! Buka rekening tabungan bisa langsung dari HP kamu.\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"Lihat Detail\",\n" +
                "              \"url\": \"https://38.staging-feature.tokopedia.com/login?ld=/payment/open-bank-account/dashboard\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://ecs7.tokopedia.net/img/toppay/banner/bri-oba-banner2.jpg\",\n" +
                "              \"category\": \"PERBANKAN\",\n" +
                "              \"name\": \"Buka Rekening\",\n" +
                "              \"desc_1\": \"Anti ribet! Buka rekening tabungan bisa langsung dari HP kamu.\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"Lihat Detail\",\n" +
                "              \"url\": \"https://38.staging-feature.tokopedia.com/login?ld=/payment/open-bank-account/dashboard\"\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"title\": \"Layanan keuangan lainnya\",\n" +
                "          \"subtitle\": \"Terus tingkatkan penjualanmu agar bisa aktifkan layanan di bawah.\",\n" +
                "          \"type\": \"\",\n" +
                "          \"background\": \"\",\n" +
                "          \"widget_list\": [\n" +
                "            {\n" +
                "              \"datalayer_status\": \"INELIGIBLE\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://ecs7.tokopedia.net/assets-fintech-frontend/staging/ic_modal-toko.png\",\n" +
                "              \"category\": \"Tambahan Modal\",\n" +
                "              \"name\": \"Modal Toko\",\n" +
                "              \"desc_1\": \"Tarik dana hingga Rp300 juta secara instan\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"\",\n" +
                "              \"url\": \"https://tokopedia.com/fm/modal-toko/\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"ELIGIBLE\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://ecs7.tokopedia.net/assets-saldo/ic-modal-toko.png\",\n" +
                "              \"category\": \"Tambahan Modal\",\n" +
                "              \"name\": \"Pinjaman Modal\",\n" +
                "              \"desc_1\": \"Modal usaha dari berbagai mitra tepercaya\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"\",\n" +
                "              \"url\": \"https://www.tokopedia.com/mitra-toppers/\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"INELIGIBLE\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://cdn.tokopedia.net/img/toppay/rekening-premium.png\",\n" +
                "              \"category\": \"Program Tarik Saldo\",\n" +
                "              \"name\": \"Rekening Premium\",\n" +
                "              \"desc_1\": \"Bisa dapat hadiah tiap tarik saldo\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"\",\n" +
                "              \"url\": \"https://staging.tokopedia.com/payment/rekening-premium\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"INELIGIBLE\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://cdn.tokopedia.net/img/toppay/rekening-premium.png\",\n" +
                "              \"category\": \"Program Tarik Saldo\",\n" +
                "              \"name\": \"Rekening Premium\",\n" +
                "              \"desc_1\": \"Bisa dapat hadiah tiap tarik saldo\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"\",\n" +
                "              \"url\": \"https://staging.tokopedia.com/payment/rekening-premium\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"INELIGIBLE\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://cdn.tokopedia.net/img/toppay/rekening-premium.png\",\n" +
                "              \"category\": \"Program Tarik Saldo\",\n" +
                "              \"name\": \"Rekening Premium\",\n" +
                "              \"desc_1\": \"Bisa dapat hadiah tiap tarik saldo\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"\",\n" +
                "              \"url\": \"https://staging.tokopedia.com/payment/rekening-premium\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"INELIGIBLE\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://cdn.tokopedia.net/img/toppay/rekening-premium.png\",\n" +
                "              \"category\": \"Program Tarik Saldo\",\n" +
                "              \"name\": \"Rekening Premium\",\n" +
                "              \"desc_1\": \"Bisa dapat hadiah tiap tarik saldo\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"\",\n" +
                "              \"url\": \"https://staging.tokopedia.com/payment/rekening-premium\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"INELIGIBLE\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://cdn.tokopedia.net/img/toppay/rekening-premium.png\",\n" +
                "              \"category\": \"Program Tarik Saldo\",\n" +
                "              \"name\": \"Rekening Premium\",\n" +
                "              \"desc_1\": \"Bisa dapat hadiah tiap tarik saldo\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"\",\n" +
                "              \"url\": \"https://staging.tokopedia.com/payment/rekening-premium\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"datalayer_status\": \"INELIGIBLE\",\n" +
                "              \"status\": \"\",\n" +
                "              \"status_text_color\": \"\",\n" +
                "              \"status_bg_color\": \"\",\n" +
                "              \"image_url\": \"https://cdn.tokopedia.net/img/toppay/rekening-premium.png\",\n" +
                "              \"category\": \"Program Tarik Saldo\",\n" +
                "              \"name\": \"Rekening Premium\",\n" +
                "              \"desc_1\": \"Bisa dapat hadiah tiap tarik saldo\",\n" +
                "              \"desc_2\": \"\",\n" +
                "              \"cta\": \"\",\n" +
                "              \"url\": \"https://staging.tokopedia.com/payment/rekening-premium\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }",LayananFinansialOuter::class.java)
    }
}