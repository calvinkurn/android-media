package tokopedia.applink.deeplink

import com.tokopedia.applink.constant.DeeplinkConstant
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tokopedia.applink.util.DeepLinkUrlConstant
import tokopedia.applink.util.DeepLinkUrlConstant.PULSA_LINK_URL

@RunWith(RobolectricTestRunner::class)
class DeepLinkMapperFromHttpTest: DeepLinkMapperTestFixture() {

    @Test
    fun `check link url of pulsa then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&menu_id=2"
        assertEqualsDeepLinkMapper(PULSA_LINK_URL, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa axis then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=1&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.AXIS, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa IM3 then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=1&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.AXIS, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa bolt then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=3&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BOLT, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa tri then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=4&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TRI, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa xl then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=5&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.XL, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa smartfren then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=7&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.SMARTFREN, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa simpati then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=12&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.SIMPATI, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa as then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=15&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.AS, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa mentari then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=17&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.MENTARI, actualDeepLink)
    }

    @Test
    fun `check link url of paket data then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=2&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA, actualDeepLink)
    }

    @Test
    fun `check link url of token listrik then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=3&operator_id=6"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TOKEN_LISTRIK, actualDeepLink)
    }

    @Test
    fun `check link url of pulsa im3 then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=1&operator_id=2&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.IM3, actualDeepLink)
    }

    @Test
    fun `check link url of streaming then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.STREAMING, actualDeepLink)
    }

    @Test
    fun `check link url of pajak then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://webview?url=https://www.tokopedia.com/pajak"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAJAK, actualDeepLink)
    }

    @Test
    fun `check link url of PBB then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=22"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB, actualDeepLink)
    }

    @Test
    fun `check link url of retribusi then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=25"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.RETRIBUSI, actualDeepLink)
    }

    @Test
    fun `check link url of PDAM then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=5"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PDAM, actualDeepLink)
    }

    @Test
    fun `check link url of angsuran then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN, actualDeepLink)
    }

    @Test
    fun `check link url of pasca bayar then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=9&menu_id=3"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PASCA_BAYAR, actualDeepLink)
    }

    @Test
    fun `check link url of telepon then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=10"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TELEPON, actualDeepLink)
    }

    @Test
    fun `check link url of PGN then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=14"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PGN, actualDeepLink)
    }

    @Test
    fun `check link url of indovision then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=120"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.INDOVISION, actualDeepLink)
    }

    @Test
    fun `check link url of indiehome then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=125"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.INDIHOME, actualDeepLink)
    }

    @Test
    fun `check link url of transvision then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=121"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TRANSIVISION, actualDeepLink)
    }

    @Test
    fun `check link url of big tv then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=100"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BIG_TV, actualDeepLink)
    }

    @Test
    fun `check link url of orange tv prabayar then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=158"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ORANGE_TV_PRABAYAR, actualDeepLink)
    }

    @Test
    fun `check link url of orange tv pascabayar then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=157"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ORANGE_TV_PASCABAYAR, actualDeepLink)
    }

    @Test
    fun `check link url of kvision then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=173"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.K_VISION, actualDeepLink)
    }

    @Test
    fun `check link url of top tv then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=144"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TOP_TV, actualDeepLink)
    }

    @Test
    fun `check link url of first media then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=243"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.FIRST_MEDIA, actualDeepLink)
    }

    @Test
    fun `check link url of nex media then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=159"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.NEX_MEDIA, actualDeepLink)
    }

    @Test
    fun `check link url of okevision then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=8&operator_id=143"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.OKEVISION, actualDeepLink)
    }

    @Test
    fun `check link url of viu then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13&operator_id=155"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VIU, actualDeepLink)
    }

    @Test
    fun `check link url of joox then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13&operator_id=154"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.JOOX, actualDeepLink)
    }

    @Test
    fun `check link url of catch play then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13&operator_id=153"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.CATCH_PLAY, actualDeepLink)
    }

    @Test
    fun `check link url of iflix then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13&operator_id=160"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.IFLIX, actualDeepLink)
    }

    @Test
    fun `check link url of genflix then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13&operator_id=161"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.GENFLIX, actualDeepLink)
    }

    @Test
    fun `check link url of wifi id then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13&operator_id=202"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.WIFI_ID, actualDeepLink)
    }

    @Test
    fun `check link url of hooq then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13&operator_id=229"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.HOOQ, actualDeepLink)
    }

    @Test
    fun `check link url of tagihan listrik then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=3&operator_id=18"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TAGIHAN_LISTRIK, actualDeepLink)
    }

    @Test
    fun `check link url of donasi then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=12"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.DONASI, actualDeepLink)
    }

    @Test
    fun `check link url of voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER, actualDeepLink)
    }

    @Test
    fun `check link url of kfc voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=244"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_KFC, actualDeepLink)
    }

    @Test
    fun `check link url of  lotteria voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=248"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_LOTTERIA, actualDeepLink)
    }

    @Test
    fun `check link url of hop hop voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=247"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_HOP_HOP, actualDeepLink)
    }

    @Test
    fun `check link url of tour les jours voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=246"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_TOUS_LES_JOURS, actualDeepLink)
    }

    @Test
    fun `check link url of breadlife voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=245"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_BREADLIFE, actualDeepLink)
    }

    @Test
    fun `check link url of tagihan pdam then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=5"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TAGIHAN_PDAM, actualDeepLink)
    }

    @Test
    fun `check link url of angsuran mobil then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=168"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MOBIL, actualDeepLink)
    }

    @Test
    fun `check link url of angsuran motor then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=255"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MOTOR, actualDeepLink)
    }

    @Test
    fun `check link url of angsuran aeon then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=166"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_AEON, actualDeepLink)
    }

    @Test
    fun `check link url of angsuran astra then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=168"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_ASTRA, actualDeepLink)
    }

    @Test
    fun `check link url of angsuran kreditplus then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=150"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_KREDITPLUS, actualDeepLink)
    }

    @Test
    fun `check link url of angsuran mandiri tunas finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=124"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MANDIRI_TUNAS_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of artha prima finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=123"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_ARTHA_PRIMA_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of woka finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=122"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_WOKA_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of mega finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=119"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MEGA_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of al ijarah finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=111"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_AL_IJARAH_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of al bima finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=89"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_BIMA_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of mega auto finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=108"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MEGA_AUTO_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of mega central finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=109"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MEGA_CENTRAL_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of mpm finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=90"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_MPM_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of radana finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=88"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_RADANA_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of smart multi finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=110"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_SMART_MULTI_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of nsc finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=167"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_NSC_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of baf finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=91"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_BAF, actualDeepLink)
    }

    @Test
    fun `check link url of indomobil finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=255"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_INDOMOBIL_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of tagihan then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=3&operator_id=18"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TAGIHAN, actualDeepLink)
    }

    @Test
    fun `check link url of bpjs then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=4"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BPJS, actualDeepLink)
    }

    @Test
    fun `check link url of voucher es teler 77 then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=262"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_ESTELER_77, actualDeepLink)
    }

    @Test
    fun `check link url of roaming then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=20&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ROAMING, actualDeepLink)
    }

    @Test
    fun `check link url of roaming indosat then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=20&operator_id=2&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ROAMING_INDOSAT, actualDeepLink)
    }

    @Test
    fun `check link url of roaming xl then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=20&operator_id=5&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ROAMING_XL, actualDeepLink)
    }

    @Test
    fun `check link url of roaming telkomsel then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=20&operator_id=12&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ROAMING_TELKOMSEL, actualDeepLink)
    }

    @Test
    fun `check link url of top up mtix then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TOP_UP_MTIX, "")
    }

    @Test
    fun `check link url of tori ichi voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=257"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_TORI_ICHI, actualDeepLink)
    }

    @Test
    fun `check link url of excelso voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=295"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_EXCELSO, actualDeepLink)
    }

    @Test
    fun `check link url of bpjs kesehatan then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=4&operator_id=13"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BPJS_KESEHATAN, actualDeepLink)
    }

    @Test
    fun `check link url of bpjs ketenagakerjaan then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=4&operator_id=14"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BJPS_KETENAGAKERJAAN, actualDeepLink)
    }

    @Test
    fun `check link url of paket data telkomsel then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=2&operator_id=12&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_TELKOMSEL, actualDeepLink)
    }

    @Test
    fun `check link url of paket data indosat then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=2&operator_id=2&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_INDOSAT, actualDeepLink)
    }

    @Test
    fun `check link url of paket data xl then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=2&operator_id=5&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_XL, actualDeepLink)
    }

    @Test
    fun `check link url of paket data axis then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=2&operator_id=1&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_AXIS, actualDeepLink)
    }

    @Test
    fun `check link url of paket data im3 then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=2&operator_id=2&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_IM3, actualDeepLink)
    }

    @Test
    fun `check link url of paket data mentari then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=2&operator_id=17&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_MENTARI, actualDeepLink)
    }

    @Test
    fun `check link url of paket data tri then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=2&operator_id=4&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_TRI, actualDeepLink)
    }

    @Test
    fun `check link url of paket data bolt then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=2&operator_id=3&menu_id=2"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAKET_DATA_BOLT, actualDeepLink)
    }

    @Test
    fun `check link url of shaburi voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=354"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_SHABURI, actualDeepLink)
    }

    @Test
    fun `check link url of kintan buffet voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=355"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_KINTAN_BUFFET, actualDeepLink)
    }

    @Test
    fun `check link url of bakerzin voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=351"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_BAKERZIN, actualDeepLink)
    }

    @Test
    fun `check link url of boga group voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=360"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_BOGA_GROUP, actualDeepLink)
    }

    @Test
    fun `check link url of carls jr voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=370"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_CARLS_JR, actualDeepLink)
    }

    @Test
    fun `check link url of shihlin voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=518"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_SHIHLIN, actualDeepLink)
    }

    @Test
    fun `check link url of gaya gelato voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=519"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_GAYA_GELATO, actualDeepLink)
    }

    @Test
    fun `check link url of gokana voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=521"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_GOKANA, actualDeepLink)
    }

    @Test
    fun `check link url of platinum resto voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=522"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_PLATINUM_RESTO, actualDeepLink)
    }

    @Test
    fun `check link url of fish n co voucher then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=24&operator_id=523"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.VOUCHER_FISH_N_CO, actualDeepLink)
    }

    @Test
    fun `check link url of bfi finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=526"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_BFI_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of suzuki finance then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=628"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_SUZUKI_FINANCE, actualDeepLink)
    }

    @Test
    fun `check link url of olympindo then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=636"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_OLYMPINDO, actualDeepLink)
    }

    @Test
    fun `check link url of top up emoney then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=34&operator_id=578"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TOP_UP_EMONEY, actualDeepLink)
    }

    @Test
    fun `check link url of taralite then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=655"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGURAN_TARALITE, actualDeepLink)
    }

    @Test
    fun `check link url of kredivo then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=662"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_KREDIVO, actualDeepLink)
    }

    @Test
    fun `check link url of btn then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=661"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_BTN, actualDeepLink)
    }

    @Test
    fun `check link url of home credit then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=7&operator_id=653"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.ANGSURAN_HOME_CREDIT, actualDeepLink)
    }

    @Test
    fun `check link url of top up ovo then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=48&operator_id=685"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.TOP_UP_OVO, actualDeepLink)
    }

    @Test
    fun `check link url of squaline then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=111&operator_id=540"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_SQUALINE, actualDeepLink)
    }

    @Test
    fun `check link url of ruang guru then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=111&operator_id=702"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_RUANG_GURU, actualDeepLink)
    }

    @Test
    fun `check link url of quipper then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=111&operator_id=704"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_QUIPPER, actualDeepLink)
    }

    @Test
    fun `check link url of zenius then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=111&operator_id=705"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_ZENIUS, actualDeepLink)
    }

    @Test
    fun `check link url of ilp then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=111&operator_id=706"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_ILP, actualDeepLink)
    }

    @Test
    fun `check link url of algoritma then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=111&operator_id=707"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_ALGORITMA, actualDeepLink)
    }

    @Test
    fun `check link url of purwadhika then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=111&operator_id=708"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR_PURWADHIKA, actualDeepLink)
    }

    @Test
    fun `check link url of belajar then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=111"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BELAJAR, actualDeepLink)
    }

    @Test
    fun `check link url of pajak samsat then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=49"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PAJAK_SAMSAT, actualDeepLink)
    }

    @Test
    fun `check link url of pdam bekasi then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=5&operator_id=813"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PDAM_BEKASI, actualDeepLink)
    }

    @Test
    fun `check link url of bein sports then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13&operator_id=688"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.STREAMING_BEIN_SPORTS, actualDeepLink)
    }

    @Test
    fun `check link url of bea cukai then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=114"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.BEA_CUKAI, actualDeepLink)
    }

    @Test
    fun `check link url of spotify then should be equal to the actual`() {
        val actualDeepLink = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://digital/form?category_id=13&operator_id=775"
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.STREAMING_SPOTIFY, actualDeepLink)
    }

    @Test
    fun `check link url of pbb bekasi then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_BEKASI, "")
    }

    @Test
    fun `check link url of pbb bandung then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_BANDUNG, "")
    }

    @Test
    fun `check link url of pbb cirebon then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_CIREBON, "")
    }

    @Test
    fun `check link url of pbb kota serang then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_KOTA_SERANG, "")
    }

    @Test
    fun `check link url of pbb kab serang then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_KAB_SERANG, "")
    }

    @Test
    fun `check link url of pbb sukabumi then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_SUKABUMI, "")
    }

    @Test
    fun `check link url of pbb tangerang then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_TANGERANG, "")
    }

    @Test
    fun `check link url of pbb tangerang selatan then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_TANGERANG_SELATAN, "")
    }

    @Test
    fun `check link url of pbb lebak then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_KAB_LEBAK, "")
    }

    @Test
    fun `check link url of pbb kab bekasi then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_KAB_BEKASI, "")
    }

    @Test
    fun `check link url of pbb subang then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.PBB_SUBANG, "")
    }

    @Test
    fun `check link url of samsat jawa tengah then should be equal to the actual`() {
        assertEqualsDeepLinkMapper(DeepLinkUrlConstant.DIGITAL.SAMSAT_JAWA_TENGAH, "")
    }
}