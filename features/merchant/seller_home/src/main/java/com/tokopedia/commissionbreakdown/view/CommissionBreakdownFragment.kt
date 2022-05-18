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
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.commissionbreakdown.di.component.CommissionBreakdownComponent
import com.tokopedia.commissionbreakdown.util.setSafeOnClickListener
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.FragmentCommissionBreakdownBinding
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.permission.PermissionCheckerHelper
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CommissionBreakdownFragment : BaseDaggerFragment(), OnDateRangeSelectListener {

    companion object {
        private const val DOWNLOAD_URL =
            "https://api-staging.tokopedia.com/v1/commission/report/download"
        private const val SELLER_EDU = "https://seller.tokopedia.com/edu/biaya-layanan-tokopedia"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_START_DATE = "start_date"
        private const val PARAM_END_DATE = "end_date"
        private const val HEADER_ORIGIN = "Origin"
        private const val HEADER_AUTHORIZATION = "Accounts-Authorization"
        private const val ORIGIN_TOKOPEDIA = "tokopedia.com"
        private const val FORMAT_AUTHORIZATION = "Bearer %s"
        private const val FORMAT_FILE_NAME = "commission_report_%s.xlsx"
        private const val DATE_FORMAT = "dd/MM/yyyy"
        private const val LOADING_DELAY = 2000L

        fun createInstance(): CommissionBreakdownFragment {
            return CommissionBreakdownFragment()
        }
    }

    @Inject
    lateinit var userSession: UserSession

    private var selectedDateFrom: Date = Date()
    private var selectedDateTo: Date = Date()
    private var permissionCheckerHelper: PermissionCheckerHelper? = null

    private val downloadReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(ctxt: Context, intent: Intent) {
            showSuccessToaster()
            dismissLoading()
            unsubscribeDownLoadHelper()
        }
    }

    private var binding: FragmentCommissionBreakdownBinding? = null

    override fun getScreenName(): String = String.EMPTY

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
        binding = FragmentCommissionBreakdownBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper?.onRequestPermissionsResult(
                context,
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    override fun onDateRangeSelected(dateFrom: Date, dateTo: Date) {
        setDateRangeChanged(dateFrom, dateTo)
    }

    fun showSuccessToaster() {
        Toaster.build(
            requireView(),
            getString(R.string.sah_commission_download_complete_message),
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_NORMAL
        ).show()
    }

    private fun initView() {
        binding?.run {
            sahTrxFeeDownload.setSafeOnClickListener {
                checkPermissionDownload {
                    downloadFile()
                }
            }

            sahTrxDownloadDateCard.setSafeOnClickListener {
                openCalender()
            }

            sahInfoCommissionBreakdown.apply {
                text = getString(R.string.info_commission_breakdown).parseAsHtml()
                setOnClickListener {
                    openSellerEdu()
                }
            }

            setBackdropBackground()
        }
    }

    private fun openSellerEdu() {
        RouteManager.route(context, SELLER_EDU)
    }

    private fun setBackdropBackground() {
        try {
            binding?.sahCommissionBg?.setBackgroundResource(R.drawable.bg_sah_download_commission)
        } catch (e: Exception) {
            val resColor = requireContext().getResColor(
                com.tokopedia.unifyprinciples.R.color.Unify_GN50
            )
            binding?.sahCommissionBg?.setBackgroundColor(resColor)
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

    private fun downloadFile() {
        try {
            startDownload()
        } catch (e: Exception) {
            dismissLoading()
            setOnDownloadError(e)
        }
    }

    private fun setOnDownloadError(exception: Exception) {
        Timber.e(exception)
    }

    private fun startDownload() {
        activity?.let { activity ->
            val startDate: String = DateTimeUtil.format(selectedDateFrom.time, DATE_FORMAT)
            val endDate: String = DateTimeUtil.format(selectedDateTo.time, DATE_FORMAT)
            binding?.sahTrxFeeDownload?.isLoading = true
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
                downloadReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            )
            dm.enqueue(request)

            Handler(Looper.getMainLooper()).postDelayed({
                dismissLoading()
            }, LOADING_DELAY)
        }
    }

    private fun dismissLoading() {
        binding?.sahTrxFeeDownload?.isLoading = false
    }

    private fun unsubscribeDownLoadHelper() {
        activity?.unregisterReceiver(downloadReceiver)
    }

    private fun openCalender() {
        CommissionBreakdownDateRangePickerBottomSheet.getInstanceRange(
            selectedDateFrom,
            selectedDateTo,
            CommissionBreakdownDateRangePickerBottomSheet.MAX_RANGE_90
        ).show(childFragmentManager, CommissionBreakdownDateRangePickerBottomSheet.TAG)
    }

    private fun setDateRangeChanged(dateFrom: Date, endDate: Date) {
        this.selectedDateFrom = dateFrom
        this.selectedDateTo = endDate
        binding?.run {
            sahTrxDownloadDateSelected.text = getDatePlaceholderText()
            sahTrxFeeDownload.isEnabled = true
            sahTrxFeeDownload.show()
            sahInfoDownloadExcelReport.show()
        }
    }

    private fun getDatePlaceholderText(): String {
        val dateFormat = SimpleDateFormat(DateUtil.DEFAULT_VIEW_FORMAT, DateUtil.DEFAULT_LOCALE)
        val startDateStr = dateFormat.format(selectedDateFrom)
        val endDateStr = dateFormat.format(selectedDateTo)
        return "$startDateStr - $endDateStr"
    }
}