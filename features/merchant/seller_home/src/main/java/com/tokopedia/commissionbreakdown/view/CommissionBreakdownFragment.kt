package com.tokopedia.commissionbreakdown.view

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.UriUtil
import com.tokopedia.commissionbreakdown.di.component.CommissionBreakdownComponent
import com.tokopedia.commissionbreakdown.util.setSafeOnClickListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.permission.PermissionCheckerHelper
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CommissionBreakdownFragment : BaseDaggerFragment(), OnDateRangeSelectListener {

    companion object {
        private const val DOWNLOAD_URL =
            "https://api-staging.tokopedia.com/v1/commission/report/download"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_START_DATE = "start_date"
        private const val PARAM_END_DATE = "end_date"
        private const val HEADER_ORIGIN = "Origin"
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val ORIGIN_TOKOPEDIA = "tokopedia.com"
        private const val FORMAT_AUTHORIZATION = "Bearer %s"
        private const val FORMAT_FILE_NAME = "commission_report_%s.xlsx"

        fun createInstance(): CommissionBreakdownFragment {
            return CommissionBreakdownFragment()
        }
    }

    @Inject
    lateinit var userSession: UserSession

    private var selectedDateFrom: Date = Date()
    private var selectedDateTo: Date = Date()
    private var datePlaceholderText: com.tokopedia.unifyprinciples.Typography? = null
    private var downloadButton: UnifyButton? = null
    private var infoDownloadXls: com.tokopedia.unifyprinciples.Typography? = null
    private var infoCommissionBreakdown: com.tokopedia.unifyprinciples.Typography? = null
    private var permissionCheckerHelper: PermissionCheckerHelper? = null

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
    ): View {
        val view = inflater.inflate(
            R.layout.fragment_commission_breakdown,
            container,
            false
        )
        initView(view)
        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper?.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
        }
    }

    private fun initView(view: View) {
        datePlaceholderText = view.findViewById(R.id.trx_download_date_selected)
        downloadButton = view.findViewById(R.id.trx_fee_download)
        infoDownloadXls = view.findViewById(R.id.info_download_excel_report)
        infoCommissionBreakdown = view.findViewById(R.id.info_commission_breakdown)
        downloadButton?.setSafeOnClickListener {
            checkPermissionDownload {
                downloadFile("01/01/2022", "31/03/2022")
            }
        }

        view.findViewById<View>(R.id.trx_download_date_card)?.setSafeOnClickListener {
            openCalender()
        }



        infoCommissionBreakdown?.apply {
            text = MethodChecker.fromHtml(activity?.getString(R.string.info_commission_breakdown))
            setOnClickListener {
                //TODO route edu
            }
        }


    }

    private fun checkPermissionDownload(onGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissionCheckerHelper = PermissionCheckerHelper()
            permissionCheckerHelper?.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                    }

                    override fun onPermissionGranted() {
                        onGranted()
                    }
                })
        } else {
            onGranted()
        }
    }

    private fun downloadFile(startDate: String, endDate: String) {
        try {
            startDownload(startDate, endDate)
        } catch (se: SecurityException) {
            //error security
            downloadButton?.isLoading = false
        } catch (ise: IllegalStateException) {
            //error uri
            downloadButton?.isLoading = false
        } catch (e: Exception) {
            //error download
            downloadButton?.isLoading = false
        }
    }

    private fun startDownload(startDate: String, endDate: String) {
        activity?.let { activity ->
            downloadButton?.isLoading = true
            val param = mapOf<String, Any>(
                PARAM_SHOP_ID to userSession.shopId,
                PARAM_START_DATE to startDate,
                PARAM_END_DATE to endDate
            )
            val url = UriUtil.buildUriAppendParams(DOWNLOAD_URL, param)
            val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
            val fileName = String.format(FORMAT_FILE_NAME, getDatePlaceholderText())
            val authorization = String.format(FORMAT_AUTHORIZATION, userSession.accessToken)
            request.run {
                addRequestHeader(HEADER_ORIGIN, ORIGIN_TOKOPEDIA)
                addRequestHeader(HEADER_AUTHORIZATION, authorization)
                setTitle(fileName)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            }
            val dm: DownloadManager = activity
                .getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            activity.registerReceiver(
                onComplete,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
            dm.enqueue(request)
        }
    }

    private val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            showSuccessToaster()
            downloadButton?.isLoading = false
            unsubscribeDownLoadHelper()
        }
    }

    private fun unsubscribeDownLoadHelper() {
        context?.unregisterReceiver(onComplete)
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
        downloadButton?.show()
        infoDownloadXls?.show()
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
        Toaster.build(
            requireView(),
            "Laporan biaya transaksi berhasil di download",
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_NORMAL
        ).show()
    }

}