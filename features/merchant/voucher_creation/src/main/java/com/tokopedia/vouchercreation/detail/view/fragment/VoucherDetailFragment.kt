package com.tokopedia.vouchercreation.detail.view.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.TipsTrickBottomSheet
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailFragment : BaseDetailFragment() {

    companion object {
        fun newInstance(): VoucherDetailFragment = VoucherDetailFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupView()
        showDummyData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getScreenName(): String = this::class.java.simpleName

    override fun initInjector() {

    }

    override fun loadData(page: Int) {

    }

    override fun onFooterButtonClickListener() {
        showShareBottomSheet()
    }

    override fun onFooterCtaTextClickListener() {
        StopVoucherDialog(context ?: return)
                .setOnPrimaryClickListener {

                }
                .show(VoucherUiModel("Voucher Hura Test Doang", "", true))
    }

    override fun showTipsAndTrickBottomSheet() {
        if (!isAdded) return
        TipsTrickBottomSheet(context ?: return, true)
                .setOnDownloadClickListener {
                    showDownloadBottomSheet()
                }
                .setOnShareClickListener {
                    showShareBottomSheet()
                }
                .show(childFragmentManager)
    }

    override fun showDownloadBottomSheet() {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        DownloadVoucherBottomSheet(parent)
                .setOnDownloadClickListener {

                }
                .show(childFragmentManager)
    }

    private fun setupView() = view?.run {

    }

    private fun showShareBottomSheet() {
        if (!isAdded) return
        val parent = view as? ViewGroup ?: return
        ShareVoucherBottomSheet(parent)
                .setOnItemClickListener {

                }
                .show(childFragmentManager)
    }

    private fun showDummyData() {
        val dummy = listOf(
                VoucherHeaderUiModel(),
                //PromoPerformanceUiModel("Rp3.000.000", 30, 120),
                UsageProgressUiModel(30),
                DividerUiModel(8),
                TipsUiModel("Bagikan voucher untuk menjangkau lebih banyak pembeli. Lihat tips & trik", "Lihat tips & trik"),
                DividerUiModel(8),
                InfoContainerUiModel("Info voucher", listOf(
                        SubInfoItemUiModel("Target Voucher", "Khusus"),
                        SubInfoItemUiModel("Nama Voucher", "Voucher Hura Test Doang"),
                        SubInfoItemUiModel("Kode Promo", "TESTDOANG", true)
                )),
                DividerUiModel(2),
                InfoContainerUiModel("Keuntungan voucher", listOf(
                        SubInfoItemUiModel("Tipe Voucher", "Cashback"),
                        SubInfoItemUiModel("Nominal Diskon", "10%"),
                        SubInfoItemUiModel("Kuota", "100"),
                        SubInfoItemUiModel("Syarat Pembelian", "Min. pembelian Rp50.000 - <br>Max. potongan Rp20.000")
                )),
                DividerUiModel(2),
                InfoContainerUiModel("Periode tampil", listOf(
                        SubInfoItemUiModel("Periode", "17 Jan 2020, 08:30 WIB - <br>17 Feb 2020, 22:00 WIB")
                )),
                DividerUiModel(8),
                FooterButtonUiModel("Bagikan Voucher", ""),
                FooterUiModel("Untuk menghentikan promosi, klik disini", "disini")
        )
        renderList(dummy)
    }
}