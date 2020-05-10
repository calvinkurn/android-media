package com.tokopedia.vouchercreation.detail.view.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.voucherlist.model.VoucherUiModel
import kotlinx.android.synthetic.main.fragment_mvc_voucher_detail.view.*

/**
 * Created By @ilhamsuaib on 09/05/20
 */

class DuplicateVoucherFragment : BaseDetailFragment() {

    companion object {
        fun newInstance(): DuplicateVoucherFragment {
            return DuplicateVoucherFragment()
        }

        private const val DATA_KEY_VOUCHER_INFO = "infoVoucher"
        private const val DATA_KEY_VOUCHER_BENEFIT = "benefitVoucher"
        private const val DATA_KEY_VOUCHER_PERIOD = "periodeVoucher"
    }

    private val dummyVoucher = VoucherUiModel("Voucher Hura Test Doang", "Ini voucher dummy doang", true)

    override fun getScreenName(): String = this::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupView()
        getDummy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initInjector() {

    }

    override fun loadData(page: Int) {

    }

    override fun onFooterButtonClickListener() {

    }

    override fun onInfoContainerCtaClick(dataKey: String) {
        when (dataKey) {
            DATA_KEY_VOUCHER_INFO -> editVoucherInfo()
            DATA_KEY_VOUCHER_BENEFIT -> editVoucherBenefit()
            DATA_KEY_VOUCHER_PERIOD -> setVoucherPeriod()
        }
    }

    override fun showDownloadBottomSheet() {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        DownloadVoucherBottomSheet(parent)
                .setOnDownloadClickListener {

                }
                .show(childFragmentManager)
    }

    private fun setupView() {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.supportActionBar?.title = dummyVoucher.name
            view?.toolbarMvcVoucherDetail?.headerSubTitle = getString(R.string.mvc_voucher_review)
        }

    }

    private fun editVoucherInfo() {

    }

    private fun editVoucherBenefit() {

    }

    private fun setVoucherPeriod() {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        VoucherPeriodBottomSheet(parent, dummyVoucher)
                .setOnSaveClickListener {

                }
                .show(childFragmentManager)
    }

    private fun getDummy() {
        val dummy = listOf(
                VoucherPreviewUiModel("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQeAXT6zjKmrSVxiyxhNo36-3Rd7WNBtXjevcmrN9azk562wctF&usqp=CAU"),
                TipsUiModel("<b>Voucher kamu akan tampil</b> di halaman detail produk dan halaman toko. Lihat Tampilan", "Lihat Tampilan"),
                DividerUiModel(2),
                InfoContainerUiModel("Info voucher", listOf(
                        SubInfoItemUiModel("Target Voucher", "Khusus"),
                        SubInfoItemUiModel("Nama Voucher", "Voucher Hura Test Doang"),
                        SubInfoItemUiModel("Kode Promo", "TESTDOANG")
                ), DATA_KEY_VOUCHER_INFO, true),
                DividerUiModel(2),
                InfoContainerUiModel("Keuntungan voucher", listOf(
                        SubInfoItemUiModel("Tipe Voucher", "Cashback"),
                        SubInfoItemUiModel("Nominal Diskon", "10%"),
                        SubInfoItemUiModel("Kuota", "100"),
                        SubInfoItemUiModel("Syarat Pembelian", "Min. pembelian Rp50.000 - <br>Max. potongan Rp20.000")
                ), DATA_KEY_VOUCHER_BENEFIT, true),
                VoucherTickerUiModel("Estimasi Maks. Pengeluaran", "Dipotong dari transaksi selesai", "Rp3.000.000", true),
                DividerUiModel(2),
                WarningPeriodUiModel(DATA_KEY_VOUCHER_PERIOD),
                DividerUiModel(8),
                FooterButtonUiModel(getString(R.string.mvc_add_voucher), ""),
                FooterUiModel("Dengan klik Tambah Voucher, saya menyetujui <br>syarat dan ketentuan yang berlaku.", "syarat dan ketentuan")
        )
        renderList(dummy)
    }
}