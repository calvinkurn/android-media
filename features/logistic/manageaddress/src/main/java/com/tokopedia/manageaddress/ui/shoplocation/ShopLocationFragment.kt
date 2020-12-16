package com.tokopedia.manageaddress.ui.shoplocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.shoplocation.ShopLocationComponent
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationState
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.util.ManageAddressConstant.BOTTOMSHEET_TITLE_ATUR_LOKASI
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ShopLocationFragment : BaseDaggerFragment(), ShopLocationItemAdapter.ShopLocationItemAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface

    private val adapter = ShopLocationItemAdapter(this)

    private val viewModel: ShopLocationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopLocationViewModel::class.java)
    }

    private var addressList: RecyclerView? = null
    private var bottomSheetAddressType: BottomSheetUnify? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var globalErrorLayout: GlobalError? = null
    private var buttonSetLocationStatus: Typography? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_location, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopLocationComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initViewModel()
        fetchData()
    }

    private fun initViews() {
        addressList = view?.findViewById(R.id.address_list)
        globalErrorLayout = view?.findViewById(R.id.global_error)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh)
        buttonSetLocationStatus = view?.findViewById(R.id.btn_set_location_status)

        addressList?.adapter = adapter
        addressList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initViewModel() {
        viewModel.shopLocation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShopLocationState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    globalErrorLayout?.gone()
                    updateData(it.data)
                }

                is ShopLocationState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                else -> {
                    swipeRefreshLayout?.isRefreshing = true
                }
            }
        })
    }

    private fun fetchData() {
        viewModel.getShopLocationList(userSession.shopId.toIntOrNull())
    }

    private fun updateData(data: List<Warehouse>) {
        adapter.addList(data)
    }

    private fun openBottomSheetAddressType(shopLocation: Warehouse) {
        bottomSheetAddressType = BottomSheetUnify()
        val viewBottomSheetAddressType = View.inflate(context, R.layout.bottomsheet_action_shop_address, null).apply {
            if (shopLocation.status == 1) {
                buttonSetLocationStatus?.text = getString(R.string.deactivate_location)
                buttonSetLocationStatus?.setOnClickListener {
                    viewModel.setShopLocationState(shopLocation.warehouseId, shopLocation.status)
                    if (viewModel.shopLocationStateStatus) view?.let { view -> Toaster.build(view, getString(R.string.text_deactivate_success, shopLocation.warehouseName), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
                    bottomSheetAddressType?.dismiss()
                }
            } else if (shopLocation.status == 2)   {
                buttonSetLocationStatus?.text = getString(R.string.activate_location)
                buttonSetLocationStatus?.setOnClickListener {
                    viewModel.setShopLocationState(shopLocation.warehouseId, shopLocation.status)
                    if (viewModel.shopLocationStateStatus) view?.let { view -> Toaster.build(view, getString(R.string.text_activate_success, shopLocation.warehouseName), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
                    bottomSheetAddressType?.dismiss()
                }
            }
        }

        bottomSheetAddressType?.apply {
            setTitle(BOTTOMSHEET_TITLE_ATUR_LOKASI)
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetAddressType)
            setOnDismissListener { dismiss() }
        }

        fragmentManager?.let {
            bottomSheetAddressType?.show(it, "show")
        }
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
                            Toaster.build(it, ManageAddressConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(it, throwable.message
                            ?: ManageAddressConstant.DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalErrorLayout?.setType(type)
        globalErrorLayout?.setActionClickListener {
            viewModel.getShopLocationList(userSession.shopId.toInt())
        }
        addressList?.gone()
        globalErrorLayout?.visible()
    }

    override fun onShopLocationStateStatusClicked(data: Warehouse) {
        openBottomSheetAddressType(data)
    }

    override fun onShopEditAddress(data: Warehouse) {
        openFormShopEditAddress(data)
    }

    private fun openFormShopEditAddress(data: Warehouse) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.SHOP_EDIT_ADDRESS)
        intent.putExtra("WAREHOUSE_DATA", data)
        startActivityForResult(intent, 121)
    }

}