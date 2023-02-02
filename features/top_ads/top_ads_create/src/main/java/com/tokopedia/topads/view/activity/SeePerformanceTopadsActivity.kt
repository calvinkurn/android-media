package com.tokopedia.topads.view.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.fragment.AdsPerformanceDateRangePickerBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class SeePerformanceTopadsActivity : AppCompatActivity() {

    private lateinit var seePerformanceTopadsBottomSheet: BottomSheetUnify
    private var selectedDateFrom: Date = Date()
    private var selectedDateTo: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_performance_topads)
        showMainBottomSheet()
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
        content?.findViewById<Typography>(R.id.product_name)?.setOnClickListener {
            openCalender()
        }
    }
    private fun showProductNameBottomSheet() {
        val contentView = View.inflate(this, R.layout.bottomsheet_product_name_see_performance, null)
        val productNameBottomSheet = BottomSheetUnify().apply {
            setChild(contentView)
            isDragable = true
            isHideable = true
            clearContentPadding = true
            showCloseIcon = false
            setTitle("Nama Produk")
        }

        productNameBottomSheet.show(supportFragmentManager,"tagFragment")
    }

    private fun openCalender() {
        AdsPerformanceDateRangePickerBottomSheet.getInstanceRange(
            selectedDateFrom,
            selectedDateTo,
            AdsPerformanceDateRangePickerBottomSheet.MAX_RANGE_90
        ).show(supportFragmentManager, "TAG")
    }

}

