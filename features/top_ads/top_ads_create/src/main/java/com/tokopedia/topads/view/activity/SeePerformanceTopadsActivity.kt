package com.tokopedia.topads.view.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.fragment.AdsPerformanceDateRangePickerBottomSheet
import com.tokopedia.topads.view.fragment.ListBottomSheet
import com.tokopedia.topads.view.uimodel.ItemListUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SeePerformanceTopadsActivity : AppCompatActivity() {

    private lateinit var seePerformanceTopadsBottomSheet: BottomSheetUnify
    private var selectedDateFrom: Date = Date()
    private var selectedDateTo: Date = Date()
    private var customDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_performance_topads)
//        showChooseDateBottomSheet()
//        showAdsPlacingFilterBottomSheet()
//        showDescriptionBottomSheet("Nama Produk","test")
//        showDescriptionBottomSheet("Kenapa iklanmu tidak tampil?",
//            "   •  Kredit TopAds tidak mencukupi\n   •  Anggaran Harian mencapai batas maksimal\n   •  Stok produk kosong\n   •  Toko sedang tutup\n   •  Biaya iklan di bawah batas minimum")
        //TODO put color on colors.xml using dms
//        showDescriptionBottomSheet(
//            "Performa tampil", getColoredSpanned("52%", "#00AA5B", "250.000", "140"))
//        showDescriptionBottomSheet(
//            "Nama grup iklan", "test")
//        openCalendar()

        showMainBottomSheet()
    }

    private fun getColoredSpanned(text: String, color: String, multiply: String, other:String): Spanned {
        return MethodChecker.fromHtml("<strong><b><big><big><font color=$color>$text </font></big> <font color=#212121><big>teratas</font></big></big></b></strong> <br>${multiply}x teratas dari $other total tampil" )
    }

    private fun showMainBottomSheet() {
        val contentView = View.inflate(this, R.layout.bottomsheet_see_performance, null)
        seePerformanceTopadsBottomSheet = BottomSheetUnify().apply {
            setupContent(contentView)
            setChild(contentView)
            isDragable = true
            isHideable = true
            showKnob = true
            clearContentPadding = true
            showCloseIcon = false
            setTitle("Performa Iklan Produkmu")
        }
        seePerformanceTopadsBottomSheet.show(supportFragmentManager,"tagFragment")
    }

    private fun setupContent(content: View?) {
        setGreyCondition(content)
    }

    private fun setGreenCondition(content: View?) {
        content?.findViewById<ImageView>(R.id.block)?.setColorFilter(Color.parseColor("#00AA5B"))
        content?.findViewById<ImageView>(R.id.block_2)?.setColorFilter(Color.parseColor("#00AA5B"))
        content?.findViewById<ImageView>(R.id.block_3)?.setColorFilter(Color.parseColor("#00AA5B"))
    }


    private fun setYellowCondition(content: View?) {
        content?.findViewById<ImageView>(R.id.block)?.setColorFilter(Color.parseColor("#FF7F17"))
        content?.findViewById<ImageView>(R.id.block_2)?.setColorFilter(Color.parseColor("#FF7F17"))
        content?.findViewById<ImageView>(R.id.block_3)?.setColorFilter(Color.parseColor("#BFC9D9"))
    }


    private fun setRedCondition(content: View?) {
        content?.findViewById<ImageView>(R.id.block)?.setColorFilter(Color.parseColor("#F94D63"))
        content?.findViewById<ImageView>(R.id.block_2)?.setColorFilter(Color.parseColor("#BFC9D9"))
        content?.findViewById<ImageView>(R.id.block_3)?.setColorFilter(Color.parseColor("#BFC9D9"))
    }


    private fun setGreyCondition(content: View?) {
        content?.findViewById<ImageView>(R.id.block)?.setColorFilter(Color.parseColor("#BFC9D9"))
        content?.findViewById<ImageView>(R.id.block_2)?.setColorFilter(Color.parseColor("#BFC9D9"))
        content?.findViewById<ImageView>(R.id.block_3)?.setColorFilter(Color.parseColor("#BFC9D9"))
    }

    private fun showDescriptionBottomSheet(title: String, description: CharSequence) {
        val contentView = View.inflate(this, R.layout.bottomsheet_product_name_see_performance, null)
        val descriptionView = contentView.findViewById<Typography>(R.id.description)
        descriptionView.text = description
        val bottomSheet = BottomSheetUnify().apply {
            setChild(contentView)
            isDragable = true
            isHideable = false
            clearContentPadding = true
            showCloseIcon = true
            setTitle(title)
        }

        bottomSheet.show(supportFragmentManager,"tagFragment")
    }


    private fun showChooseDateBottomSheet() {
        val today = getDaysAgo(0)
        val yesterday = getDaysAgo(1)
        val daysAgo3 = getDaysAgo(3)
        val daysAgo7 = getDaysAgo(7)
        val daysAgo30 = getDaysAgo(30)
        val firstDateOfMonth = getFirstDateOfMonth()

        val temporaryData = arrayListOf(
            ItemListUiModel("Hari ini", today),
            ItemListUiModel("Kemarin", yesterday),
            ItemListUiModel("3 hari terakhir", "$daysAgo3 - $today"),
            ItemListUiModel("7 hari terakhir", "$daysAgo7 - $today"),
            ItemListUiModel("30 hari terakhir", "$daysAgo30 - $today"),
            ItemListUiModel("Bulan ini", "$firstDateOfMonth - $today"),
            ItemListUiModel("Custom", getRangeCustomDate()),
        )
        ListBottomSheet.show(supportFragmentManager, "Pilih rentang waktu", temporaryData)
    }

    private fun getRangeCustomDate(): String {
        if(customDate.isBlank()){
            return DEFAULT_CUSTOM_DATE_PLACEHOLDER
        }
        return "custom"
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDaysAgo(daysAgo: Int): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        cal.add(Calendar.DATE, -daysAgo)
        return dateFormat.format(cal.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getFirstDateOfMonth(): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val days = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
        cal.set(Calendar.DAY_OF_MONTH, days);
        return dateFormat.format(cal.time)
    }

    private fun showAdsPlacingFilterBottomSheet() {
        val temporaryData = arrayListOf(
            ItemListUiModel("Semua Penempatan", "Iklan produk yang tampil di halaman pencarian dan rekomendasi."),
            ItemListUiModel("Di Pencarian", "Iklan produk yang tampil di halaman pencarian"),
            ItemListUiModel("Di Rekomendasi", "Iklan produk yang tampil di bagian rekomendasi"),

        )
        ListBottomSheet.show(supportFragmentManager, "Penempatan Iklan", temporaryData)
    }

    fun openCalendar() {
        AdsPerformanceDateRangePickerBottomSheet.getInstanceRange(
            selectedDateFrom,
            selectedDateTo,
            AdsPerformanceDateRangePickerBottomSheet.MAX_RANGE_90
        ).show(supportFragmentManager, "TAG")
    }

    companion object {
        var DEFAULT_CUSTOM_DATE_PLACEHOLDER = "Pilih tanggal"
    }
}

