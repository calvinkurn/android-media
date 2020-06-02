package com.tokopedia.vouchercreation.detail.view.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.common.model.MerchantVoucherModel
import com.tokopedia.vouchercreation.common.model.VoucherMapper
import com.tokopedia.vouchercreation.detail.model.*
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

    private val dummyVoucher = VoucherMapper().mapRemoteModelToUiModel(listOf(MerchantVoucherModel(
            voucherName = "Voucher Hura Test Doang", discountAmtFormatted = "Cashback 10%"
    ))).first()

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
//        DownloadVoucherBottomSheet(parent)
//                .setOnDownloadClickListener {
//
//                }
//                .show(childFragmentManager)
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
        VoucherPeriodBottomSheet.createInstance(parent, dummyVoucher)
                .setOnSuccessClickListener {
                    onSuccessUpdateVoucherPeriod()
                }
                .setOnFailClickListener {

                }
                .show(childFragmentManager)
    }

    private fun getDummy() {
        val dummy = listOf(
                VoucherPreviewUiModel("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQeAXT6zjKmrSVxiyxhNo36-3Rd7WNBtXjevcmrN9azk562wctF&usqp=CAU"),
                TipsUiModel("<b>Voucher kamu akan tampil</b> di halaman detail produk dan halaman toko. Lihat Tampilan", "Lihat Tampilan"),
                DividerUiModel(2),
                InfoContainerUiModel(R.string.mvc_detail_voucher_info, listOf(
                        SubInfoItemUiModel(R.string.mvc_voucher_target, "Khusus"),
                        SubInfoItemUiModel(R.string.mvc_detail_voucher_name, "Voucher Hura Test Doang"),
                        SubInfoItemUiModel(R.string.mvc_detail_promo_code, "TESTDOANG")
                ), DATA_KEY_VOUCHER_INFO, true),
                DividerUiModel(2),
                InfoContainerUiModel(R.string.mvc_detail_voucher_benefit, listOf(
                        SubInfoItemUiModel(R.string.mvc_type_of_voucher, "Cashback"),
                        SubInfoItemUiModel(R.string.mvc_detail_discount_amount, "10%"),
                        SubInfoItemUiModel(R.string.mvc_detail_quota, "100"),
                        SubInfoItemUiModel(R.string.mvc_detail_terms, "Min. pembelian Rp50.000 - <br>Max. potongan Rp20.000")
                ), DATA_KEY_VOUCHER_BENEFIT, true),
                VoucherTickerUiModel("Rp3.000.000", true),
                DividerUiModel(2),
                WarningPeriodUiModel(DATA_KEY_VOUCHER_PERIOD),
                DividerUiModel(8),
                FooterButtonUiModel(getString(R.string.mvc_add_voucher), ""),
                FooterUiModel("Dengan klik Tambah Voucher, saya menyetujui <br>syarat dan ketentuan yang berlaku.", "syarat dan ketentuan")
        )
        renderList(dummy)
    }

    private fun onSuccessUpdateVoucherPeriod() {

    }
}