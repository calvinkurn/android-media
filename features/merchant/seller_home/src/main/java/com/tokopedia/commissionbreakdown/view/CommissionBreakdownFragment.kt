package com.tokopedia.commissionbreakdown.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.sellerhome.R
import com.tokopedia.commissionbreakdown.di.component.CommissionBreakdownComponent
import com.tokopedia.commissionbreakdown.util.setSafeOnClickListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.date.DateUtil
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CommissionBreakdownFragment: BaseDaggerFragment(), OnDateRangeSelectListener {
    private var selectedDateFrom: Date = Date()
    private var selectedDateTo: Date = Date()
    private var datePlaceholderText: com.tokopedia.unifyprinciples.Typography? = null
    private var downloadButton: UnifyButton? = null

    @Inject
    lateinit var userSession: UserSession

    companion object {
        fun createInstance(): CommissionBreakdownFragment {
            return CommissionBreakdownFragment()
        }
    }

    override fun getScreenName(): String {
        return ""
    }


    override fun initInjector() {
        activity?.let {
            val component = getComponent(CommissionBreakdownComponent::class.java)
            component.inject(this)
        }
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
        downloadButton?.setSafeOnClickListener {
            downloadFile()
        }

        view.findViewById<View>(R.id.trx_download_date_card)?.setSafeOnClickListener {
            openCalender()
        }


    }

    private fun downloadFile() {
        downloadButton?.isLoading = true
        showSuccessToaster()
//        val request: DownloadManager.Request = DownloadManager.Request(Uri.parse("url"))
//        val fileName = "commission_report_${getDatePlaceholderText()}.XLS"
//        request.addRequestHeader("Origin", "tokopedia.com")
//        request.addRequestHeader(
//            "Cookie",
//            "TOPATK-DEVEL=tlGzDl_VRHGMJYqbcnhuIQ; _SID_Tokopedia_=l06OoggM_RKhCHoKOhUyrcBgPG7DmBYiVNqlX9ZWG3YV56YTyvtt_wJzoRlJz_J75ztVTIF2SNahLpq4S3kD0d_zRbcotEVosn4-glTcHz6dD0g7OXuB0JkyKkoWbpgD"
//        )
//        request.addRequestHeader(
//            "Authorization",
//            "Bearer TKPD Tokopedia:${userSession.accessToken}"
//        )
//        request.setTitle(fileName)
//        request.allowScanningByMediaScanner()
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
//        val dm: DownloadManager? = activity?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
//        dm?.enqueue(request)
//        showSuccessToaster()
    }

    private fun openCalender() {
        CommissionBreakdownDateRangePickerBottomSheet.getInstanceRange(
            selectedDateFrom,
            selectedDateTo,
            CommissionBreakdownDateRangePickerBottomSheet.MAX_RANGE_90,
            CommissionBreakdownDateRangePickerBottomSheet.TWO_YEAR_MILLIS
        )
            .show(childFragmentManager, "")
    }

    private fun setDateRangeChanged(dateFrom: Date, endDate: Date) {
        this.selectedDateFrom = dateFrom
        this.selectedDateTo = endDate
        datePlaceholderText?.text = getDatePlaceholderText()
        downloadButton?.isEnabled = true
        downloadButton?.visibility = View.VISIBLE
    }

    private fun getDatePlaceholderText(): String {
        val dateFormat = SimpleDateFormat(DateUtil.DEFAULT_VIEW_FORMAT, DateUtil.DEFAULT_LOCALE)
        val startDateStr = dateFormat.format(selectedDateFrom)
        val endDateStr = dateFormat.format(selectedDateTo)
       return "$startDateStr - $endDateStr"
    }

    override fun onDateRangeSelected(dateFrom: Date, dateTo: Date) {
        setDateRangeChanged(dateFrom, dateTo)
    }

    private fun showSuccessToaster() {
        Toaster.build(requireView(), "Laporan biaya transaksi berhasil di download", Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
    }

}