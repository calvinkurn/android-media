package com.tokopedia.vouchercreation.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.model.*
import com.tokopedia.vouchercreation.detail.view.VoucherDetailListener
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactoryImpl
import kotlinx.android.synthetic.main.fragment_mvc_voucher_detail.view.*

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class VoucherDetailFragment : BaseListFragment<VoucherDetailUiModel, VoucherDetailAdapterFactoryImpl>(),
        VoucherDetailListener {

    companion object {
        fun newInstance(): VoucherDetailFragment = VoucherDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_detail, container, false)
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

    override fun getRecyclerViewResourceId(): Int = R.id.rvMvcVoucherDetail

    override fun getAdapterTypeFactory(): VoucherDetailAdapterFactoryImpl {
        return VoucherDetailAdapterFactoryImpl(this)
    }

    override fun getScreenName(): String = this::class.java.simpleName

    override fun initInjector() {

    }

    override fun onItemClicked(t: VoucherDetailUiModel?) {

    }

    override fun loadData(page: Int) {

    }

    override fun onFooterButtonClickListener() {

    }

    private fun setupView() = view?.run {
        setupActionBar()
    }

    private fun setupActionBar() = view?.run {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(toolbarMvcVoucherDetail)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

            activity.supportActionBar?.title = "Voucher Hura Test Doang"
        }
    }

    private fun showDummyData() {
        val dummy = listOf(
                VoucherHeaderUiModel(),
                PromoPerformanceUiModel("Rp2.600.000", 30, 120),
                UsageProgressUiModel(30),
                DividerUiModel(8),
                TipsUiModel("Bagikan voucher untuk menjangkau lebih banyak pembeli. Lihat tips & trik"),
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
                        SubInfoItemUiModel("Syarat Pembelian", "Min. pembelian Rp50.000 - Max. potongan Rp20.000")
                )),
                DividerUiModel(2),
                InfoContainerUiModel("Periode tampil", listOf(
                        SubInfoItemUiModel("Periode", "17 Jan 2020, 08:30 WIB - 17 Feb 2020, 22:00 WIB")
                )),
                DividerUiModel(8),
                FooterButtonUiModel("Bagikan Voucher", ""),
                FooterUiModel("Untuk menghentikan promosi, klik <font color=\"#03AC0E\"><b>disini</b></font>")
        )
        renderList(dummy)
    }
}