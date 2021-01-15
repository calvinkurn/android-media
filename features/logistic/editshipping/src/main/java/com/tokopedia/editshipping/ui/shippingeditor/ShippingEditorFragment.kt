package com.tokopedia.editshipping.ui.shippingeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.di.shippingeditor.ShippingEditorComponent
import com.tokopedia.editshipping.domain.model.shippingEditor.ShippingEditorState
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorConventionalAdapter
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorOnDemandItemAdapter
import com.tokopedia.editshipping.util.EditShippingConstant
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.shippingeditor.OnDemandModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ShippingEditorFragment: BaseDaggerFragment(), ShippingEditorOnDemandItemAdapter.ShippingEditorItemAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel: ShippingEditorViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShippingEditorViewModel::class.java)
    }

    private var tickerShipperInfo: Ticker? = null
    private var shipperListOnDemand: RecyclerView? = null
    private var shipperListConventional: RecyclerView? = null
    private var btnSaveShipper: UnifyButton? = null

    private var bottomSheetShipperInfo: BottomSheetUnify? = null
    private var bottomSheetImageInfo: DeferredImageView? = null
    private var bottomSheetInfoCourier: Typography? = null
    private var bottomSheetInfoCourierDetail: Typography? = null
    private var btnShipperBottomSheet: UnifyButton? = null
    private var bottomSheetShipperInfoType: Int? = -1

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var globalErrorLayout: GlobalError? = null

    private var shippingEditorOnDemandAdapter = ShippingEditorOnDemandItemAdapter(this)
    private var shippingEditorConventionalAdapter = ShippingEditorConventionalAdapter()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShippingEditorComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shipping_editor_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initAdapter()
        initViewModel()
        fetchData()
    }

    private fun initViews() {
        tickerShipperInfo = view?.findViewById(R.id.ticker_shipper_info)
        shipperListOnDemand = view?.findViewById(R.id.rv_on_demand)
        shipperListConventional = view?.findViewById(R.id.rv_conventional)
        btnSaveShipper = view?.findViewById(R.id.btn_save_shipper)
        globalErrorLayout = view?.findViewById(R.id.global_error)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh)
    }

    private fun initAdapter() {
        shipperListOnDemand?.adapter = shippingEditorConventionalAdapter
        shipperListConventional?.adapter = shippingEditorConventionalAdapter
        shipperListOnDemand?.layoutManager = LinearLayoutManager(context)
        shipperListConventional?.layoutManager = LinearLayoutManager(context)
    }

    private fun initViewModel() {
        viewModel.shipperList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShippingEditorState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    globalErrorLayout?.gone()
                    updateData(it.data.shippers.onDemand)
                }

                is ShippingEditorState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                else -> swipeRefreshLayout?.isRefreshing = true
            }
        })
    }

    private fun fetchData() {
        viewModel.getShipperList(userSession?.shopId.toInt())
    }

    private fun updateData(data: List<OnDemandModel>) {
        shippingEditorOnDemandAdapter.updateData(data)
    }

    override fun onShipperInfoClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun openBottomSheetShipperInfo() {
        bottomSheetShipperInfo = BottomSheetUnify()
        val viewBottomSheetShipperInfo = View.inflate(context, R.layout.bottomsheet_shipper_info, null)
        setupBottomSheetShipperInfoChild(viewBottomSheetShipperInfo)

        bottomSheetShipperInfo?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetShipperInfo)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetShipperInfo?.show(it, "show")
        }
    }

    private fun setupBottomSheetShipperInfoChild(child: View) {
        bottomSheetImageInfo = child.findViewById(R.id.img_info_courier)
        bottomSheetInfoCourier = child.findViewById(R.id.tv_info_courier)
        bottomSheetInfoCourierDetail = child.findViewById(R.id.tv_info_courier_detail)
        btnShipperBottomSheet = child.findViewById(R.id.btn_close)

        if(bottomSheetShipperInfoType == 1) {
            bottomSheetImageInfo?.loadRemoteImageDrawable("AWB")
            bottomSheetInfoCourier?.text = getString(R.string.awb_otomatis_title)
            bottomSheetInfoCourierDetail?.text = getString(R.string.awb_otomatis_detail)

        } else {
            bottomSheetImageInfo?.loadRemoteImageDrawable("Non Tunai")
            bottomSheetInfoCourier?.text = getString(R.string.non_tunai_title)
            bottomSheetInfoCourierDetail?.text = getString(R.string.non_tunai_detail)
        }
        btnShipperBottomSheet?.setOnClickListener { bottomSheetShipperInfo?.dismiss() }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, EditShippingConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                        }!!
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(it, throwable.message
                            ?: EditShippingConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalErrorLayout?.setType(type)
        globalErrorLayout?.setActionClickListener {
            viewModel.getShipperList(userSession.shopId.toInt())
        }
        /*satu layout gone*/
        shipperListOnDemand?.gone()
        globalErrorLayout?.visible()
    }


}