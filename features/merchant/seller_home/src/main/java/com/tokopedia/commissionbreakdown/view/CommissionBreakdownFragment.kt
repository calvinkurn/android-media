package com.tokopedia.commissionbreakdown.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
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
import com.tokopedia.commissionbreakdown.common.ImageUrl
import com.tokopedia.commissionbreakdown.di.component.CommissionBreakdownComponent
import com.tokopedia.commissionbreakdown.tracker.CommissionTracker
import com.tokopedia.commissionbreakdown.util.CommissionWebViewClient
import com.tokopedia.commissionbreakdown.util.CommissionWebViewClient.HEADER_AUTHORIZATION
import com.tokopedia.commissionbreakdown.util.CommissionWebViewClient.HEADER_ORIGIN
import com.tokopedia.commissionbreakdown.util.CommissionWebViewClient.ORIGIN_TOKOPEDIA
import com.tokopedia.commissionbreakdown.util.setSafeOnClickListener
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.FragmentCommissionBreakdownBinding
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.permission.PermissionCheckerHelper
import timber.log.Timber
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CommissionBreakdownFragment :
    BaseDaggerFragment(),
    CommissionBreakdownDateRangePickerBottomSheet.OnDateRangeSelectListener {

    companion object {
        private val DOWNLOAD_URL = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            "https://api-staging.tokopedia.com/v1/commission/report/download"
        } else {
            "https://api.tokopedia.com/v1/commission/report/download"
        }
        private const val SELLER_EDU = "https://seller.tokopedia.com/edu/biaya-layanan-tokopedia"
        private const val PARAM_SHOP_ID = "shop_id"
        private const val PARAM_START_DATE = "start_date"
        private const val PARAM_END_DATE = "end_date"
        private const val FORMAT_AUTHORIZATION = "Bearer %s"
        private const val FORMAT_FILE_NAME = "commission_report_%s.xlsx"
        private const val DATE_FORMAT = "dd/MM/yyyy"
        private const val LOADING_DELAY = 2000L
        private const val TOASTER_BOTTOM_MARGIN = 64

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

    private fun showSuccessToaster() {
        context?.let {
            Toaster.toasterCustomBottomHeight =
                it.dpToPx(TOASTER_BOTTOM_MARGIN).toInt()
            view?.let { view ->
                Toaster.build(
                    view,
                    getString(R.string.sah_commission_download_complete_message),
                    Snackbar.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL
                ).show()
            }
        }
    }

    private fun showErrorToaster(message: String) {
        context?.let {
            Toaster.toasterCustomBottomHeight =
                it.dpToPx(TOASTER_BOTTOM_MARGIN).toInt()
            view?.let { view ->
                Toaster.build(
                    view,
                    message,
                    Snackbar.LENGTH_SHORT,
                    Toaster.TYPE_ERROR,
                    getString(R.string.trx_refresh)
                ) {
                    openWebView()
                }.show()
            }
        }
    }

    private fun initView() {
        binding?.run {
            trxFeeDownload.setSafeOnClickListener {
                CommissionTracker.sendDownloadCtaClickEvent()

                checkPermissionDownload {
                    openWebView()
                }
            }

            trxDownloadDateCard.setSafeOnClickListener {
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
        binding?.trxCommissionIllustrationBg?.loadImage(ImageUrl.IMG_BG_BACKDROP)
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
                }
            )
        } else {
            onGranted()
        }
    }

    @SuppressLint("DeprecatedMethod", "SetJavaScriptEnabled")
    private fun openWebView() {
        binding?.tmpWebView?.run {
            val startDate: String = DateTimeUtil.format(selectedDateFrom.time, DATE_FORMAT)
            val endDate: String = DateTimeUtil.format(selectedDateTo.time, DATE_FORMAT)
            showLoading()

            val authorization = String.format(FORMAT_AUTHORIZATION, userSession.accessToken)
            settings.javaScriptEnabled = true
            webViewClient = CommissionWebViewClient.getWebViewClient(
                authorization,
                ::setOnDownloadError
            )
            setDownloadListener { url, _, _, _, _ ->
                try {
                    startDownload(url)
                } catch (e: Exception) {
                    dismissLoading()
                }
                dismissLoading()
            }

            val param = mapOf<String, Any>(
                PARAM_SHOP_ID to userSession.shopId,
                PARAM_START_DATE to startDate,
                PARAM_END_DATE to endDate
            )
            val url = UriUtil.buildUriAppendParams(DOWNLOAD_URL, param)

            val header = mapOf(
                HEADER_ORIGIN to ORIGIN_TOKOPEDIA,
                HEADER_AUTHORIZATION to authorization
            )
            loadUrl(url, header)
        }
    }

    private fun setOnDownloadError(exception: Exception) {
        Timber.e(exception)
        dismissLoading()
        exception.printStackTrace()

        val message = when (exception) {
            is UnknownHostException -> getString(R.string.trx_no_network_error_message)
            else -> getString(R.string.trx_server_error_message)
        }
        showErrorToaster(message)
    }

    private fun startDownload(url: String) {
        activity?.let { activity ->
            showLoading()
            val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
            val fileName = String.format(FORMAT_FILE_NAME, getDatePlaceholderText())
            val authorization = String.format(FORMAT_AUTHORIZATION, userSession.accessToken)
            request.run {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                setAllowedOverRoaming(true)
                addRequestHeader(HEADER_ORIGIN, ORIGIN_TOKOPEDIA)
                addRequestHeader(HEADER_AUTHORIZATION, authorization)
                setTitle(fileName)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            }
            val dm: DownloadManager = activity
                .getSystemService(DOWNLOAD_SERVICE) as DownloadManager
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

    private fun showLoading() {
        Handler(Looper.getMainLooper()).post {
            binding?.trxFeeDownload?.isLoading = true
        }
    }

    private fun dismissLoading() {
        Handler(Looper.getMainLooper()).post {
            binding?.trxFeeDownload?.isLoading = false
        }
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
            trxDownloadDateSelected.text = getDatePlaceholderText()
            dismissLoading()
            trxFeeDownload.show()
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
