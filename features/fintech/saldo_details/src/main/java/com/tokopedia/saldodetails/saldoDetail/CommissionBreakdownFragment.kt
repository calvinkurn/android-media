package com.tokopedia.saldodetails.saldoDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.collapseView
import com.tokopedia.saldodetails.commom.expandView
import com.tokopedia.saldodetails.commom.listener.setSafeOnClickListener
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui.DateRangePickerBottomSheet
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.ui.OnDateRangeSelectListener
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.date.DateUtil
import java.text.SimpleDateFormat
import java.util.*


class CommissionBreakdownFragment : TkpdBaseV4Fragment(),
    OnDateRangeSelectListener {
    private val animationDuration: Long = 300
    private var selectedDateFrom: Date = Date()
    private var selectedDateTo: Date = Date()
    private var datePlaceholderText: com.tokopedia.unifyprinciples.Typography? = null
    private var downloadButton: UnifyButton? = null

    companion object {
        fun createInstance(): CommissionBreakdownFragment {
            return CommissionBreakdownFragment()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_commission_breakdown,
            container,
            false
        )
        initView(view)
        return view
    }

    private fun initView(view: View) {
        datePlaceholderText = view.findViewById(R.id.trx_download_date_selected)
        downloadButton = view.findViewById(R.id.trx_fee_download)
        val downloadSectionExpand = view.findViewById<View>(R.id.trx_fee_card_view)
        view.findViewById<IconUnify>(R.id.download_trx_fee_report_expand)
            ?.setOnClickListener { icon ->
                icon.animate().rotation(icon.rotation + 180f).duration = animationDuration
                downloadSectionExpand?.let {
                    if (icon.rotation % 360 == 0f) {
                        downloadSectionExpand.expandView()
                    } else {
                        downloadSectionExpand.collapseView()
                    }
                }

            }
        downloadButton?.setSafeOnClickListener {
            downloadFile()
        }

        view.findViewById<View>(R.id.trx_download_date_card)?.setSafeOnClickListener {
            openCalender()
        }


    }

    private fun downloadFile() {
        downloadButton?.isLoading = true
//        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse("url"))
//        val fileName = "commission_report_$startDateStr_$endDateStr.XLS"
//        request.addRequestHeader("Origin", "tokopedia.com")
//        request.addRequestHeader(
//            "Cookie",
//            "TOPATK-DEVEL=tlGzDl_VRHGMJYqbcnhuIQ; _SID_Tokopedia_=l06OoggM_RKhCHoKOhUyrcBgPG7DmBYiVNqlX9ZWG3YV56YTyvtt_wJzoRlJz_J75ztVTIF2SNahLpq4S3kD0d_zRbcotEVosn4-glTcHz6dD0g7OXuB0JkyKkoWbpgD"
//        )
//        request.addRequestHeader(
//            "Authorization",
//            "Bearer TKPD Tokopedia:YzMwODE1NGI0Yjg0YTNkN2FiMDQwOTBiMGRhMzBjZDQ5MGZlMjY2Ng=="
//        )
//        request.setTitle(fileName)
//        request.allowScanningByMediaScanner()
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
//        val dm: DownloadManager? = activity?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
//        dm?.enqueue(request)
//        Toast.makeText(
//            ApplicationProvider.getApplicationContext<Context>(),
//            "Downloading File : $fileName", Toast.LENGTH_LONG
//        ).show()
    }

    private fun openCalender() {
        CommissionBreakdownDateRangePickerBottomSheet.getInstanceRange(
            selectedDateFrom,
            selectedDateTo,
            DateRangePickerBottomSheet.MAX_RANGE_90,
            DateRangePickerBottomSheet.TWO_YEAR_MILLIS
        )
            .show(childFragmentManager, "")
    }

    private fun setDateRangeChanged(dateFrom: Date, endDate: Date) {
        this.selectedDateFrom = dateFrom
        this.selectedDateTo = endDate
        val dateFormat = SimpleDateFormat(DateUtil.DEFAULT_VIEW_FORMAT, DateUtil.DEFAULT_LOCALE)
        val startDateStr = dateFormat.format(selectedDateFrom)
        val endDateStr = dateFormat.format(endDate)
        datePlaceholderText?.text = "$startDateStr - $endDateStr"
        downloadButton?.isEnabled = true
        downloadButton?.visibility = View.VISIBLE
    }

    override fun onDateRangeSelected(dateFrom: Date, dateTo: Date) {
        setDateRangeChanged(dateFrom, dateTo)
    }
}