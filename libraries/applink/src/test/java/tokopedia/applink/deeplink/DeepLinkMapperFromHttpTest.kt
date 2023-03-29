package tokopedia.applink.deeplink

import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.home.DeeplinkMapperHome
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tokopedia.applink.util.DeepLinkUrlConstant

@RunWith(RobolectricTestRunner::class)
class DeepLinkMapperFromHttpTest : DeepLinkMapperTestFixture() {

    @Test
    fun `check link url of pulsa then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.PULSA_LINK_URL, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa axis then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2&client_number=0838"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.AXIS, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa IM3 then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2&client_number=0856"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.IM3, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa bolt then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.PULSA_LINK_URL, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa tri then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2&client_number=0897"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TRI, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa xl then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2&client_number=0817"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.XL, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa smartfren then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2&client_number=0888"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.SMARTFREN, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa simpati then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2&client_number=0812"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.SIMPATI, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa as then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2&client_number=0812"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.AS, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa mentari then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2&client_number=0856"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.MENTARI, expectedDeepLink)
    }

    @Test
    fun `check link url of paket data then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=2&menu_id=290&template=paketdatav2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA, expectedDeepLink)
    }

    @Test
    fun `check link url of token listrik then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=3&menu_id=113&operator_id=6&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TOKEN_LISTRIK, expectedDeepLink)
    }

    @Test
    fun `check link url of pulsa im3 then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PULSA}?category_id=1&menu_id=289&template=pulsav2&client_number=0856"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.IM3, expectedDeepLink)
    }

    @Test
    fun `check link url of streaming then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=13&menu_id=81&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.STREAMING, expectedDeepLink)
    }

    @Test
    fun `check link url of pajak then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAJAK, expectedDeepLink)
    }

    @Test
    fun `check link url of PBB then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB, expectedDeepLink)
    }

    @Test
    fun `check link url of retribusi then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=25&menu_id=125&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.RETRIBUSI, expectedDeepLink)
    }

    @Test
    fun `check link url of PDAM then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=5&menu_id=120&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PDAM, expectedDeepLink)
    }

    @Test
    fun `check link url of angsuran then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN, expectedDeepLink)
    }

    @Test
    fun `check link url of pasca bayar then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.TELCO_POSTPAID_DIGITAL}?category_id=9&menu_id=3&template=telcopost"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PASCA_BAYAR, expectedDeepLink)
    }

    @Test
    fun `check link url of telepon then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=10&menu_id=121&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TELEPON, expectedDeepLink)
    }

    @Test
    fun `check link url of PGN then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=14&menu_id=124&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PGN, expectedDeepLink)
    }

    @Test
    fun `check link url of indovision then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=8&menu_id=154&operator_id=120&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.INDOVISION, expectedDeepLink)
    }

    @Test
    fun `check link url of indihome then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=8&menu_id=154&operator_id=125&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.INDIHOME, expectedDeepLink)
    }

    @Test
    fun `check link url of transvision then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=8&menu_id=154&operator_id=121&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TRANSIVISION, expectedDeepLink)
    }

    @Test
    fun `check link url of big tv then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=8&menu_id=154&operator_id=100&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BIG_TV, expectedDeepLink)
    }

    @Test
    fun `check link url of kvision then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=8&menu_id=154&operator_id=173&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.K_VISION, expectedDeepLink)
    }

    @Test
    fun `check link url of first media then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=8&menu_id=154&operator_id=243&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.FIRST_MEDIA, expectedDeepLink)
    }

    @Test
    fun `check link url of viu then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=13&menu_id=81&operator_id=155&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VIU, expectedDeepLink)
    }

    @Test
    fun `check link url of joox then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=13&menu_id=81&operator_id=154&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.JOOX, expectedDeepLink)
    }

    @Test
    fun `check link url of catch play then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=13&operator_id=153&menu_id=81&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.CATCH_PLAY, expectedDeepLink)
    }

    @Test
    fun `check link url of iflix then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=13&operator_id=160&menu_id=81&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.IFLIX, expectedDeepLink)
    }

    @Test
    fun `check link url of genflix then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=13&operator_id=161&menu_id=81&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.GENFLIX, expectedDeepLink)
    }

    @Test
    fun `check link url of wifi id then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=13&menu_id=81&operator_id=202&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.WIFI_ID, expectedDeepLink)
    }

    @Test
    fun `check link url of tagihan listrik then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=3&menu_id=113&operator_id=18&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TAGIHAN_LISTRIK, expectedDeepLink)
    }

    @Test
    fun `check link url of donasi then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=12&menu_id=83&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.DONASI, expectedDeepLink)
    }

    @Test
    fun `check link url of tagihan pdam then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=5&menu_id=120&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TAGIHAN_PDAM, expectedDeepLink)
    }

    @Test
    fun `check link url of angsuran mobil then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=168&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MOBIL, expectedDeepLink)
    }

    @Test
    fun `check link url of angsuran motor then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=255&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MOTOR, expectedDeepLink)
    }

    @Test
    fun `check link url of angsuran aeon then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=166&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_AEON, expectedDeepLink)
    }

    @Test
    fun `check link url of angsuran astra then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=168&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_ASTRA, expectedDeepLink)
    }

    @Test
    fun `check link url of angsuran kreditplus then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=150&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_KREDITPLUS, expectedDeepLink)
    }

    @Test
    fun `check link url of angsuran mandiri tunas finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=124&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MANDIRI_TUNAS_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of artha prima finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=123&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_ARTHA_PRIMA_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of woka finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=122&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_WOKA_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of mega finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=119&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MEGA_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of al ijarah finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=111&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_AL_IJARAH_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of al bima finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=89&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_BIMA_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of mega auto finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=108&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MEGA_AUTO_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of mega central finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=109&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MEGA_CENTRAL_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of mpm finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=90&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MPM_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of radana finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=88&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_RADANA_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of smart multi finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=110&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_SMART_MULTI_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of nsc finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=167&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_NSC_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of baf finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=91&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_BAF, expectedDeepLink)
    }

    @Test
    fun `check link url of indomobil finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=255&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_INDOMOBIL_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of tagihan then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=3&menu_id=113&operator_id=18&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TAGIHAN, expectedDeepLink)
    }

    @Test
    fun `check link url of bpjs then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=4&menu_id=126&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BPJS, expectedDeepLink)
    }

    @Test
    fun `check link url of roaming then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=20&menu_id=314&template=roamingv2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ROAMING, expectedDeepLink)
    }

    @Test
    fun `check link url of roaming indosat then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=20&menu_id=314&template=roamingv2&client_number=0856"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ROAMING_INDOSAT, expectedDeepLink)
    }

    @Test
    fun `check link url of roaming xl then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=20&menu_id=314&template=roamingv2&client_number=0817"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ROAMING_XL, expectedDeepLink)
    }

    @Test
    fun `check link url of roaming telkomsel then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=20&menu_id=314&template=roamingv2&client_number=0812"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ROAMING_TELKOMSEL, expectedDeepLink)
    }

    @Test
    fun `check link url of top up mtix then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=31&menu_id=128&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TOP_UP_MTIX, expectedDeepLink)
    }

    @Test
    fun `check link url of bpjs kesehatan then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=4&menu_id=126&operator_id=13&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BPJS_KESEHATAN, expectedDeepLink)
    }

    @Test
    fun `check link url of bpjs ketenagakerjaan then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=4&menu_id=126&operator_id=14&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BJPS_KETENAGAKERJAAN, expectedDeepLink)
    }

    @Test
    fun `check link url of paket data telkomsel then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=2&menu_id=290&template=paketdatav2&client_number=0812"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_TELKOMSEL, expectedDeepLink)
    }

    @Test
    fun `check link url of paket data indosat then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=2&menu_id=290&template=paketdatav2&client_number=0856"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_INDOSAT, expectedDeepLink)
    }

    @Test
    fun `check link url of paket data xl then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=2&menu_id=290&template=paketdatav2&client_number=0817"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_XL, expectedDeepLink)
    }

    @Test
    fun `check link url of paket data axis then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=2&menu_id=290&template=paketdatav2&client_number=0838"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_AXIS, expectedDeepLink)
    }

    @Test
    fun `check link url of paket data im3 then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=2&menu_id=290&template=paketdatav2&client_number=0856"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_IM3, expectedDeepLink)
    }

    @Test
    fun `check link url of paket data mentari then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=2&menu_id=290&template=paketdatav2&client_number=0856"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_MENTARI, expectedDeepLink)
    }

    @Test
    fun `check link url of paket data tri then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=2&menu_id=290&template=paketdatav2&client_number=0897"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_TRI, expectedDeepLink)
    }

    @Test
    fun `check link url of paket data bolt then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.DIGITAL_PDP_PAKET_DATA}?category_id=2&menu_id=290&template=paketdatav2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_BOLT, expectedDeepLink)
    }

    @Test
    fun `check link url of bfi finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=526&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_BFI_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of suzuki finance then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=628&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_SUZUKI_FINANCE, expectedDeepLink)
    }

    @Test
    fun `check link url of olympindo then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=636&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_OLYMPINDO, expectedDeepLink)
    }

    @Test
    fun `check link url of kredivo then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=662&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_KREDIVO, expectedDeepLink)
    }

    @Test
    fun `check link url of btn then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=661&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_BTN, expectedDeepLink)
    }

    @Test
    fun `check link url of home credit then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=7&menu_id=123&operator_id=653&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_HOME_CREDIT, expectedDeepLink)
    }

    @Test
    fun `check link url of top up ovo then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=48&menu_id=162&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TOP_UP_OVO, expectedDeepLink)
    }

    @Test
    fun `check link url of squline then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=111&menu_id=84&operator_id=540&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_SQUALINE, expectedDeepLink)
    }

    @Test
    fun `check link url of ruang guru then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=111&menu_id=84&operator_id=702&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_RUANG_GURU, expectedDeepLink)
    }

    @Test
    fun `check link url of quipper then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=111&menu_id=84&operator_id=704&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_QUIPPER, expectedDeepLink)
    }

    @Test
    fun `check link url of zenius then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=111&menu_id=84&operator_id=705&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_ZENIUS, expectedDeepLink)
    }

    @Test
    fun `check link url of purwadhika then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=111&menu_id=84&operator_id=708&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_PURWADHIKA, expectedDeepLink)
    }

    @Test
    fun `check link url of belajar then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=111&menu_id=84&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR, expectedDeepLink)
    }

    @Test
    fun `check link url of pajak samsat then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=49&menu_id=155&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAJAK_SAMSAT, expectedDeepLink)
    }

    @Test
    fun `check link url of bein sports then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=13&menu_id=81&operator_id=688&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.STREAMING_BEIN_SPORTS, expectedDeepLink)
    }

    @Test
    fun `check link url of spotify then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.VOUCHER_GAME}?category_id=13&menu_id=81&operator_id=775&template=voucher"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.STREAMING_SPOTIFY, expectedDeepLink)
    }

    @Test
    fun `check link url of pbb bandung then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&operator_id=1034&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_BANDUNG, expectedDeepLink)
    }

    @Test
    fun `check link url of pbb cirebon then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&operator_id=1035&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_CIREBON, expectedDeepLink)
    }

    @Test
    fun `check link url of pbb kota serang then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&operator_id=1032&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_KOTA_SERANG, expectedDeepLink)
    }

    @Test
    fun `check link url of pbb sukabumi then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&operator_id=1031&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_SUKABUMI, expectedDeepLink)
    }

    @Test
    fun `check link url of pbb tangerang then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&operator_id=1030&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_TANGERANG, expectedDeepLink)
    }

    @Test
    fun `check link url of pbb tangerang selatan then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&operator_id=1029&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_TANGERANG_SELATAN, expectedDeepLink)
    }

    @Test
    fun `check link url of pbb lebak then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&operator_id=1024&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_KAB_LEBAK, expectedDeepLink)
    }

    @Test
    fun `check link url of pbb kab bekasi then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&operator_id=1028&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_KAB_BEKASI, expectedDeepLink)
    }

    @Test
    fun `check link url of pbb subang then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConsInternalDigital.GENERAL_TEMPLATE}?category_id=22&operator_id=1027&menu_id=127&template=general"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_SUBANG, expectedDeepLink)
    }

    @Test
    fun `check link url of play room from home then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConstInternalContent.INTERNAL_PLAY}/${DeepLinkUrlConstant.CONTENT.PLAY_CHANNEL_ID}?source_type=HOME"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.CONTENT.PLAY_FROM_HOME, expectedDeepLink)
    }

    @Test
    fun `check link url of play room from feed then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConstInternalContent.INTERNAL_PLAY}/${DeepLinkUrlConstant.CONTENT.PLAY_CHANNEL_ID}?source_type=FEED"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.CONTENT.PLAY_FROM_FEED, expectedDeepLink)
    }

    @Test
    fun `check link url of play room with starting time then should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConstInternalContent.INTERNAL_PLAY}/${DeepLinkUrlConstant.CONTENT.PLAY_CHANNEL_ID}?start_time=60000"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.CONTENT.PLAY_WITH_START_TIME, expectedDeepLink)
    }

    @Test
    fun `check link url for video tab in feed should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConstInternalContent.INTERNAL_FEED_HOME_NAVIGATION}?${DeeplinkMapperHome.EXTRA_TAB_POSITION}=1&${ApplinkConstInternalContent.EXTRA_FEED_TAB_POSITION}=3&tab=live"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.CONTENT.VIDEO_TAB_ON_FEED, expectedDeepLink)
    }

    @Test
    fun `check link url of recommendation with id then should be redirected to discovery page with id`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${ApplinkConsInternalHome.AUTHORITY_DISCOVERY}/${ApplinkConsInternalHome.PATH_REKOMENDASI}?recomProdId=3190804069"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.RECOMMENDATION.RECOMMENDATION_WITH_ID, expectedDeepLink)
    }

    @Test
    fun `check link url of recommendation with id and param then should be redirected to discovery page with id and param`() {
        val expectedDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${ApplinkConsInternalHome.AUTHORITY_DISCOVERY}/${ApplinkConsInternalHome.PATH_REKOMENDASI}?recomProdId=2137719991&msrc=product-feed&utm_source=facebook&utm_medium=ocpm&utm_campaign=alon-smda-DPO-WIB-SER-18-55-MF-AUTO-180-SMDA-NWB-PG-11110000-0020-alon-smda&ref=fbdpa"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.RECOMMENDATION.RECOMMENDATION_WITH_ID_AND_QUERY, expectedDeepLink)
    }

    @Test
    fun `check link url of searching something on now page should be equal to the actual`() {
        val expectedDeepLink = "${ApplinkConstInternalTokopediaNow.SEARCH}?q=jj%20royal"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.TOKOPEDIANOW_SEARCH_LINK_URL, expectedDeepLink)
    }

    @Test
    fun `check amp find url with search query should return tokopedia internal search in customerapp`() {
        val expectedDeepLink =
            ApplinkConstInternalDiscovery.SEARCH_RESULT +
                "?q=3%20ply%20masker" +
                "&navsource=find"

        assertEqualsDeepLinkMapper(
            "https://www.tokopedia.com/amp/find/3-ply-masker",
            expectedDeepLink
        )
    }

    @Test
    fun `check find appLink with search query then should return tokopedia internal search in customerapp`() {
        val expectedDeepLink =
            ApplinkConstInternalDiscovery.SEARCH_RESULT +
                "?q=3%20ply%20masker" +
                "&navsource=find"

        assertEqualsDeepLinkMapper(
            "https://www.tokopedia.com/find/3-ply-masker",
            expectedDeepLink
        )
    }

    @Test
    fun `check find appLink with query and city then should return tokopedia internal search in customerapp`() {
        val expectedDeepLink =
            ApplinkConstInternalDiscovery.SEARCH_RESULT +
                "?q=3%20ply%20masker%20di%20dki%20jakarta" +
                "&navsource=find"

        assertEqualsDeepLinkMapper(
            "https://www.tokopedia.com/find/3-ply-masker/c/dki-jakarta",
            expectedDeepLink
        )
    }

    @Test
    fun `non-find url containing find word should not redirect to search`() {
        assertEqualsDeepLinkMapper(
            "https://www.tokopedia.com/findustri/plat-besi-5mm-steel-plate-harga-per-1-cm2",
            ""
        )
    }
}
