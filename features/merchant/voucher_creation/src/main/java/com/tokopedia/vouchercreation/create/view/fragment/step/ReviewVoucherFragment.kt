package com.tokopedia.vouchercreation.create.view.fragment.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.voucherimage.PostVoucherUiModel
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.detail.view.fragment.BaseDetailFragment

class ReviewVoucherFragment : BaseDetailFragment() {

    companion object {
        @JvmStatic
        fun createInstance(): ReviewVoucherFragment = ReviewVoucherFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_base_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDummyData()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun loadData(page: Int) {}

    override fun showDownloadBottomSheet() {}

    private fun showDummyData() {
        val dummyList = listOf(
                PostVoucherUiModel(
                        VoucherImageType.FreeDelivery(0),
                        "harusnyadaristep1",
                        "Ini Harusnya dari Backend",
                        "https://ecs7.tokopedia.net/img/cache/215-square/shops-1/2020/5/6/1479278/1479278_3bab5e93-003a-4819-a68a-421f69224a59.jpg",
                        "CODETEST",
                        "10 Mei 2020"),
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
                FooterButtonUiModel("Tambah Voucher", ""),
                FooterUiModel("Dengan klik Tambah Voucher, saya menyetujui syarat dan ketentuan yang berlaku", "syarat dan ketentuan")
        )
        renderList(dummyList)
    }
}