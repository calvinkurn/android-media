package com.tokopedia.discovery2.viewcontrollers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.analytics.DiscoveryAnalytics
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.END_POINT
import com.tokopedia.discovery2.viewcontrollers.adapter.AddChildAdapterCallback
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.mergeAdapter.MergeAdapters
import com.tokopedia.discovery2.viewcontrollers.customview.CustomTopChatView
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class DiscoveryFragment : BaseDaggerFragment(), AddChildAdapterCallback {

    private lateinit var discoveryViewModel: DiscoveryViewModel
    private lateinit var mDiscoveryFab: CustomTopChatView
    private lateinit var recyclerView: RecyclerView
    private lateinit var typographyHeader: Typography
    private lateinit var ivShare: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var permissionCheckerHelper: PermissionCheckerHelper
    private lateinit var globalError: GlobalError
    var pageEndPoint = ""
    private lateinit var mergeAdapters: MergeAdapters
    private lateinit var discoveryRecycleAdapter: DiscoveryRecycleAdapter

    @Inject
    lateinit var trackingQueue: TrackingQueue

    companion object {
        fun getInstance(endPoint: String?): Fragment {
            val bundle = Bundle()
            val fragment = DiscoveryFragment()
            if (!endPoint.isNullOrEmpty()) {
                bundle.putString(END_POINT, endPoint)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discovery, container, false)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        permissionCheckerHelper = PermissionCheckerHelper()
        mDiscoveryFab = view.findViewById(R.id.fab)
        typographyHeader = view.findViewById(R.id.typography_header)
        ivShare = view.findViewById(R.id.iv_share)
        ivSearch = view.findViewById(R.id.iv_search)
        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            getDiscoveryAnalytics().trackBackClick()
            activity?.onBackPressed()
        }
        globalError = view.findViewById(R.id.global_error)
        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener { activity?.onBackPressed() }
        recyclerView = view.findViewById(R.id.discovery_recyclerView)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        discoveryViewModel = (activity as DiscoveryActivity).getViewModel()

        discoveryRecycleAdapter = DiscoveryRecycleAdapter(this)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        mergeAdapters = MergeAdapters()
        mergeAdapters.addAdapter(discoveryRecycleAdapter)
        recyclerView.adapter = mergeAdapters

        discoveryViewModel.pageIdentifier = arguments?.getString(END_POINT, "") ?: ""
        pageEndPoint = discoveryViewModel.pageIdentifier
        discoveryViewModel.getDiscoveryData()

        setUpObserver()
    }

    private fun setUpObserver() {
        discoveryViewModel.getDiscoveryResponseList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    discoveryRecycleAdapter.setDataList(it.data)
                    mergeAdapters.notifyDataSetChanged()
                }
            }
        })

        discoveryViewModel.getDiscoveryFabLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    it.data.data?.get(0)?.let { data ->
                        setFloatingActionButton(data)
                        setAnimationOnScroll()
                    }
                }

                is Fail -> {
                    mDiscoveryFab.hide()
                }
            }
        })

        discoveryViewModel.getDiscoveryPageInfo().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    ivSearch.show()
                    setPageInfo(it.data)
                }

                is Fail -> {
                    typographyHeader.text = getString(R.string.discovery)
                    ivSearch.hide()
                    ivShare.hide()

                    if (it.throwable is UnknownHostException
                            || it.throwable is SocketTimeoutException) {
                        globalError.setType(GlobalError.NO_CONNECTION)
                    } else {
                        globalError.setType(GlobalError.SERVER_ERROR)
                    }
                    globalError.show()
                    globalError.setOnClickListener {
                        globalError.hide()
                        discoveryViewModel.getDiscoveryData()
                    }
                }
            }
        })
    }

    private fun setPageInfo(data: PageInfo?) {
        typographyHeader.text = data?.name
        if (data?.share?.enabled == true) {
            ivShare.show()
            ivShare.setOnClickListener {
                getDiscoveryAnalytics().trackShareClick()
                permissionHelper {
                    Utils.shareData(activity, data.share.description, data.share.url, discoveryViewModel.getBitmapFromURL(data.share.image))
                }
            }
            ivSearch.setOnClickListener {
                if (data.searchApplink?.isNotEmpty() == true) {
                    RouteManager.route(context, data.searchApplink)
                } else {
                    RouteManager.route(context, Utils.SEARCH_DEEPLINK)
                }
            }
        } else {
            ivShare.hide()
        }

    }

    private fun setAnimationOnScroll() {
        recyclerView.addOnScrollListener(mDiscoveryFab.getScrollListener())
    }

    private fun setFloatingActionButton(data: DataItem) {
        mDiscoveryFab.apply {
            show()
            showTextAnimation(data)
            data.thumbnailUrlMobile?.let { showImageOnFab(context, it) }
            setClick(data.applinks?.toEmptyStringIfNull().toString(), data.shopId?.toIntOrNull()
                    ?: 0)
        }
    }

    private fun setClick(appLinks: String, shopId: Int) {
        mDiscoveryFab.getFabButton().setOnClickListener {
            getDiscoveryAnalytics().trackClickCustomTopChat()
            if (appLinks.isNotEmpty() && shopId != 0) {
                activity?.let { it1 -> discoveryViewModel.openCustomTopChat(it1, appLinks, shopId) }
            }
        }
    }

    private fun permissionHelper(grantedPermission: () -> Unit) {
        permissionCheckerHelper.checkPermission(this, PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE, object : PermissionCheckerHelper.PermissionCheckListener {
            override fun onPermissionDenied(permissionText: String) {
                context?.let {
                    permissionCheckerHelper.onPermissionDenied(it, permissionText)
                }
            }

            override fun onNeverAskAgain(permissionText: String) {
                context?.let {
                    permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                }
            }


            override fun onPermissionGranted() {
                grantedPermission()
            }

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(context!!,
                    requestCode, permissions,
                    grantResults)
        }
    }

    override fun addChildAdapter(discoveryRecycleAdapter: DiscoveryRecycleAdapter) {
        mergeAdapters.addAdapter(discoveryRecycleAdapter)
    }

    override fun notifyMergeAdapter() {
        if (!recyclerView.isComputingLayout) {
            mergeAdapters.notifyDataSetChanged()
        }
    }

    fun getDiscoveryRecyclerViewAdapter() = discoveryRecycleAdapter

    fun getDiscoveryAnalytics(): DiscoveryAnalytics {
        val discoveryAnalytics: DiscoveryAnalytics by lazy { DiscoveryAnalytics(trackingQueue = trackingQueue, pagePath = discoveryViewModel.pagePath, pageType = discoveryViewModel.pageType) }
        return discoveryAnalytics
    }
}