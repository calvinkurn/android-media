package com.tokopedia.ordermanagement.snapshot.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.IS_SNAPSHOT_FROM_SOM
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_ORDER_DETAIL_ID
import com.tokopedia.applink.internal.ApplinkConstInternalOrder.PARAM_ORDER_ID
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.analytics.SnapshotAnalytics
import com.tokopedia.ordermanagement.snapshot.data.model.GetOrderSnapshot
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotParam
import com.tokopedia.ordermanagement.snapshot.di.DaggerSnapshotComponent
import com.tokopedia.ordermanagement.snapshot.di.SnapshotModule
import com.tokopedia.ordermanagement.snapshot.util.SnapshotIdlingResource
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter
import com.tokopedia.ordermanagement.snapshot.view.viewmodel.SnapshotViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class SnapshotFragment : BaseDaggerFragment(), SnapshotAdapter.ActionListener, RefreshHandler.OnRefreshHandlerListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var snapshotAdapter: SnapshotAdapter
    private lateinit var userSession: UserSession
    private var rv: RecyclerView? = null
    private var btnSnapshotToPdp: UnifyButton? = null
    private val REQUEST_CODE_LOGIN = 588
    private var orderId = ""
    private var orderDetailId = ""
    private var isSnapshotFromSOM = false
    private var responseSnapshot = GetOrderSnapshot()
    private var srlSnapshot: SwipeToRefresh? = null
    private var refreshHandler: RefreshHandler? = null

    private val snapshotViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SnapshotViewModel::class.java]
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SnapshotFragment {
            return SnapshotFragment().apply {
                arguments = bundle
            }
        }
    }

    private lateinit var viewModel: SnapshotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(context)
        if (userSession.isLoggedIn) {
            initialLoad()
        } else {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val contentView = inflater.inflate(R.layout.snapshot_fragment, container, false)
        rv = contentView.findViewById(R.id.rv_snapshot)
        srlSnapshot = contentView.findViewById(R.id.snapshot_swipe_to_refresh)
        btnSnapshotToPdp = contentView.findViewById(R.id.btn_snapshot_to_pdp)
        return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SnapshotViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                initialLoad()
            } else {
                activity?.finish()
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            DaggerSnapshotComponent.builder()
                    .baseAppComponent(getBaseAppComponent())
                    .snapshotModule(context?.let { SnapshotModule(it) })
                    .build()
                    .inject(this)
        }
    }

    private fun initialLoad() {
        if (arguments?.getString(PARAM_ORDER_ID) != null && arguments?.getString(PARAM_ORDER_DETAIL_ID) != null) {
            orderId = arguments?.getString(PARAM_ORDER_ID).toString()
            orderDetailId = arguments?.getString(PARAM_ORDER_DETAIL_ID).toString()
            isSnapshotFromSOM = arguments?.getBoolean(IS_SNAPSHOT_FROM_SOM) ?: false
            val paramSnapshot = SnapshotParam(orderId = orderId, orderDetailId = orderDetailId)
            snapshotViewModel.loadSnapshot(paramSnapshot)
        }
    }

    private fun getBaseAppComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        observingData()
    }

    private fun prepareLayout() {
        refreshHandler = RefreshHandler(srlSnapshot, this)
        refreshHandler?.setPullEnabled(true)
        snapshotAdapter = SnapshotAdapter().apply {
            setActionListener(this@SnapshotFragment)
        }
        rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = snapshotAdapter
        }
    }

    private fun observingData() {
        snapshotAdapter.showLoader()
        snapshotViewModel.snapshotResponse.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    SnapshotIdlingResource.decrement()
                    refreshHandler?.finishRefresh()
                    responseSnapshot = result.data
                    snapshotAdapter.snapshotResponse = result.data
                    snapshotAdapter.showContent()
                    btnSnapshotToPdp?.apply {
                        visible()
                        text = getString(R.string.btn_snapshot_to_pdp_label)
                        setOnClickListener {
                            RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, result.data.orderDetail.productId)
                            userSession.userId?.let { userId ->
                                if (isSnapshotFromSOM) {
                                    SnapshotAnalytics.clickSeeProductPageFromSOM(result.data.orderDetail.productId, userId)
                                } else {
                                    SnapshotAnalytics.clickLihatHalamanProduk(result.data.orderDetail.productId, userId)
                                }
                            }
                        }
                    }

                }
                is Fail -> {
                    showToaster(getString(R.string.snapshot_error_common), Toaster.TYPE_ERROR)
                }
            }
        })
    }

    private fun showToaster(message: String, type: Int) {
        val toasterSuccess = Toaster
        view?.let { v ->
            toasterSuccess.build(v, message, Toaster.LENGTH_SHORT, type, "").show()
        }
    }

    override fun onSnapshotImgClicked(position: Int) {
        activity?.let {
            val strings: ArrayList<String> = ArrayList()
            responseSnapshot.productImageSecondary.forEach {
                strings.add(it.imageUrl)
            }
            it.startActivity(ImagePreviewActivity.getCallingIntent(it,
                    strings,
                    null, position))
        }
    }

    override fun onSnapshotShopClicked(shopId: String) {
        activity?.let {
            RouteManager.route(it, ApplinkConst.SHOP, shopId)
        }
        userSession.userId?.let { userId ->
            if (isSnapshotFromSOM) {
                SnapshotAnalytics.clickShopPageFromSOM(shopId, userId)
            } else {
                SnapshotAnalytics.clickShopPage(shopId, userId)
            }
        }
    }

    override fun onRefresh(view: View?) {
        initialLoad()
    }
}