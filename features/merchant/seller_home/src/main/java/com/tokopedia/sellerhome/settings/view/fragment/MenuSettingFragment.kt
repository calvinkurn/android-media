package com.tokopedia.sellerhome.settings.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.DividerUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.IndentedSettingTitleUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.SettingTitleMenuUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.DividerType
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import kotlinx.android.synthetic.main.fragment_menu_setting.*
import kotlinx.android.synthetic.main.setting_logout.view.*
import kotlinx.android.synthetic.main.setting_tc.view.*

class MenuSettingFragment : BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>() {

    companion object {
        private const val PENGATURAN_TOKO = "Pengaturan Toko"
        private const val PROFIL_TOKO = "PROFIL TOKO"
        private const val INFORMASI_DASAR = "Informasi Dasar"
        private const val CATATAN_TOKO = "Catatan Toko"
        private const val JAM_BUKA_TUTUP_TOKO = "Jam buka tutup toko"
        private const val LOKASI_DAN_PENGIRIMAN = "LOKASI DAN PENGIRIMAN"
        private const val TAMBAH_DAN_LOKASI_TOKO = "Tambah dan lokasi toko"
        private const val ATUR_LAYANAN_PENGIRIMAN = "Atur layanan pengiriman"
        private const val FITUR_EKSKLUSIF = "FITUR EKSKLUSIF"
        private const val PENGATURAN_AKUN = "Pengaturan Akun"
        private const val PROFIL_DIRI = "Profil Diri"
        private const val REKENING_BANK = "Rekening Bank"
        private const val KATA_SANDI = "Kata Sandi"
        private const val PENGATURAN_APLIKASI = "Pengaturan Aplikasi"
        private const val CHAT_DAN_NOTIFIKASI = "Chat & Notifikasi"
        private const val BAGIKAN_APLIKASI = "Bagikan Aplikasi"
        private const val REVIEW_APLIKASI = "Review Aplikasi"

        @JvmStatic
        fun createInstance(): MenuSettingFragment = MenuSettingFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory = OtherMenuAdapterTypeFactory()

    override fun onItemClicked(t: SettingUiModel?) {

    }

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun loadData(page: Int) {

    }

    private fun setupView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        val settingList = listOf(
                SettingTitleMenuUiModel(PENGATURAN_TOKO, R.drawable.ic_seller_edu),
                IndentedSettingTitleUiModel(PROFIL_TOKO),
                MenuItemUiModel(INFORMASI_DASAR),
                MenuItemUiModel(CATATAN_TOKO),
                MenuItemUiModel(JAM_BUKA_TUTUP_TOKO),
                DividerUiModel(DividerType.THIN_INDENTED),
                IndentedSettingTitleUiModel(LOKASI_DAN_PENGIRIMAN),
                MenuItemUiModel(TAMBAH_DAN_LOKASI_TOKO),
                MenuItemUiModel(ATUR_LAYANAN_PENGIRIMAN),
                DividerUiModel(DividerType.THIN_INDENTED),
                IndentedSettingTitleUiModel(FITUR_EKSKLUSIF),
                MenuItemUiModel(TAMBAH_DAN_LOKASI_TOKO),
                MenuItemUiModel(ATUR_LAYANAN_PENGIRIMAN),
                DividerUiModel(DividerType.THICK),
                SettingTitleMenuUiModel(PENGATURAN_AKUN, R.drawable.ic_account),
                MenuItemUiModel(PROFIL_DIRI),
                MenuItemUiModel(REKENING_BANK),
                MenuItemUiModel(KATA_SANDI),
                DividerUiModel(DividerType.THICK),
                SettingTitleMenuUiModel(PENGATURAN_APLIKASI, R.drawable.ic_app_setting),
                MenuItemUiModel(CHAT_DAN_NOTIFIKASI),
                MenuItemUiModel(BAGIKAN_APLIKASI),
                MenuItemUiModel(REVIEW_APLIKASI),
                DividerUiModel(DividerType.THIN_INDENTED)
        )
        adapter.data.addAll(settingList)
        adapter.notifyDataSetChanged()
        renderList(settingList)

        logoutLayout.run {
            appVersionText.text = getString(R.string.setting_application_version, GlobalConfig.VERSION_NAME)
            setOnClickListener {
                //Goto logout
            }
        }

        tcLayout.run {
            settingTC.setOnClickListener {

            }
            settingPrivacy.setOnClickListener {

            }
        }

    }
}