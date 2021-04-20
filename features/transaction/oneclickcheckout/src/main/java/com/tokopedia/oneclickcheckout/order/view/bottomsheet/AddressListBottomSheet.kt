package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.address.AddressListItemAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.*
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class AddressListBottomSheet(private val useCase: GetAddressCornerUseCase, private val listener: AddressListBottomSheetListener) : CoroutineScope {

    private var bottomSheet: BottomSheetUnify? = null

    private var searchAddress: SearchBarUnify? = null
    private var rvAddressList: RecyclerView? = null
    private var progressBar: LoaderUnify? = null

    private var textSearchError: Typography? = null
    private var globalErrorLayout: GlobalError? = null

    private var adapter: AddressListItemAdapter? = null
    private var endlessScrollListener: EndlessRecyclerViewScrollListener? = null

    private var fragment: Fragment? = null

    fun show(fragment: OrderSummaryPageFragment, addressId: String, addressState: Int) {
        this.fragment = fragment
        selectedId = addressId
        this.addressState = addressState
        fragment.context?.let { context ->
            fragment.fragmentManager?.let {
                bottomSheet?.dismiss()
                bottomSheet = BottomSheetUnify().apply {
                    isDragable = true
                    isHideable = true
                    isFullpage = true
                    clearContentPadding = true
                    setTitle(context.getString(R.string.bottom_sheet_title_choose_address))
                    val child = View.inflate(context, R.layout.bottom_sheet_address_list, null)
                    setupChild(context, child)
                    fragment.view?.height?.div(2)?.let { height ->
                        customPeekHeight = height
                    }
                    setChild(child)
                    val actionIconParam = getIconUnifyDrawable(context, IconUnify.ADD)
                    setAction(actionIconParam) {
                        this@AddressListBottomSheet.bottomSheet?.dismiss()
                        listener.onAddAddress(token)
                    }
                    setShowListener {
                        try {
                            val displayMetrics = context.resources.displayMetrics
                            bottomSheetAction.layoutParams.height = 20.dpToPx(displayMetrics)
                            bottomSheetAction.layoutParams.width = 20.dpToPx(displayMetrics)
                        } catch (t: Throwable) {
                            Timber.d(t)
                        }
                    }
                    show(it, null)
                    setOnDismissListener {
                        compositeSubscription.clear()
                        coroutineContext.cancel()
                    }
                }
            }
        }
    }

    private fun getLocalCacheAddressId(): String {
        fragment?.context?.let {
            return ChooseAddressUtils.getLocalizingAddressData(it)?.address_id ?: "0"
        }

        return "0"
    }

    private fun setupChild(context: Context, child: View) {
        rvAddressList = child.findViewById(R.id.rv_address_list)
        progressBar = child.findViewById(R.id.progress_bar)
        globalErrorLayout = child.findViewById(R.id.global_error)
        searchAddress = child.findViewById(R.id.search_input_view)
        textSearchError = child.findViewById(R.id.text_search_error)

        adapter = AddressListItemAdapter(getAddressAdapterListener())
        rvAddressList?.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvAddressList?.layoutManager = linearLayoutManager
        rvAddressList?.clearOnScrollListeners()
        endlessScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                loadMore()
            }
        }
        endlessScrollListener?.let {
            rvAddressList?.addOnScrollListener(it)
        }

        initSearchView(context)
    }

    private fun getAddressAdapterListener(): AddressListItemAdapter.OnSelectedListener {
        return object : AddressListItemAdapter.OnSelectedListener {
            override fun onSelect(addressModel: RecipientAddressModel) {
                bottomSheet?.dismiss()
                listener.onSelect(addressModel)
            }
        }
    }

    private fun initSearchView(context: Context) {
        searchAddress?.searchBarTextField?.setOnClickListener {
            searchAddress?.searchBarTextField?.isCursorVisible = true
            openSoftKeyboard()
        }
        searchAddress?.searchBarTextField?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAddress?.clearFocus()
                searchAddress(searchAddress?.searchBarTextField?.text?.toString() ?: "")
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        searchAddress?.clearListener = {
            searchAddress("")
        }
        searchAddress?.searchBarPlaceholder = context.getString(com.tokopedia.purchase_platform.common.R.string.label_hint_search_address)

        searchAddress("")
    }

    private fun openSoftKeyboard() {
        searchAddress?.searchBarTextField?.let {
            (it.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        showGlobalError(GlobalError.SERVER_ERROR)
                    }
                }
            }
            else -> {
                showGlobalError(GlobalError.SERVER_ERROR)
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalErrorLayout?.setType(type)
        globalErrorLayout?.setActionClickListener {
            searchAddress?.searchBarTextField?.setText("")
            searchAddress("")
        }
        searchAddress?.gone()
        textSearchError?.gone()
        rvAddressList?.gone()
        globalErrorLayout?.visible()
    }

    private fun onChangeData(addressList: OccState<AddressListModel>) {
        when (addressList) {
            is OccState.FirstLoad -> {
                progressBar?.gone()
                globalErrorLayout?.gone()
                if (addressList.data.listAddress.isEmpty()) {
                    textSearchError?.visible()
                } else {
                    textSearchError?.gone()
                }
                searchAddress?.visible()
                rvAddressList?.scrollToPosition(0)
                rvAddressList?.visible()
                adapter?.setData(addressList.data.listAddress, addressList.data.hasNext ?: false)
                endlessScrollListener?.resetState()
                endlessScrollListener?.setHasNextPage(addressList.data.hasNext ?: false)
            }

            is OccState.Success -> {
                adapter?.setData(addressList.data.listAddress, addressList.data.hasNext ?: false)
                endlessScrollListener?.updateStateAfterGetData()
                endlessScrollListener?.setHasNextPage(addressList.data.hasNext ?: false)
            }

            is OccState.Failed -> {
                progressBar?.gone()
                addressList.getFailure()?.let { failure ->
                    handleError(failure.throwable)
                }
            }

            is OccState.Loading -> {
                progressBar?.visible()
                rvAddressList?.gone()
                globalErrorLayout?.gone()
                textSearchError?.gone()
            }
        }
    }

    // ================ VIEW MODEL LOGIC ==================

    var savedQuery: String = ""
    private var selectedId = "-1"
    private var destinationLatitude: String = ""
    private var destinationLongitude: String = ""
    private var destinationDistrict: String = ""
    private var destinationPostalCode: String = ""
    private var addressState: Int = 0
    var token: Token? = null

    private var page = 1
    private var isLoadingMore = false
    private var addressListModel: AddressListModel? = null

    private val compositeSubscription = CompositeSubscription()

    private fun searchAddress(query: String) {
        onChangeData(OccState.Loading)
        OccIdlingResource.increment()
        compositeSubscription.add(
                fragment?.context?.let { ChooseAddressUtils.isRollOutUser(it) }?.let {
                    useCase.execute(query, addressState, getLocalCacheAddressId().toIntOrZero(), it)
                            .subscribe(object : rx.Observer<AddressListModel> {
                                override fun onError(e: Throwable?) {
                                    onChangeData(OccState.Failed(Failure(e)))
                                    OccIdlingResource.decrement()
                                    isLoadingMore = false
                                }

                                override fun onNext(t: AddressListModel) {
                                    token = t.token
                                    logicSelection(t)
                                    savedQuery = query
                                    page = 1
                                    isLoadingMore = false
                                }

                                override fun onCompleted() {
                                    OccIdlingResource.decrement()
                                }
                            })
                }
        )
    }

    fun loadMore() {
        if (progressBar?.visibility == View.GONE && !isLoadingMore) {
            isLoadingMore = true
            OccIdlingResource.increment()
            compositeSubscription.add(
                    fragment?.context?.let { ChooseAddressUtils.isRollOutUser(it) }?.let {
                        useCase.loadMore(savedQuery, ++this.page, addressState, getLocalCacheAddressId().toIntOrZero(), it)
                                .subscribe(object : rx.Observer<AddressListModel> {
                                    override fun onError(e: Throwable?) {
                                        onChangeData(OccState.Failed(Failure(e)))
                                        OccIdlingResource.decrement()
                                        isLoadingMore = false
                                    }

                                    override fun onNext(t: AddressListModel) {
                                        logicSelection(t, isLoadMore = true)
                                    }

                                    override fun onCompleted() {
                                        OccIdlingResource.decrement()
                                        isLoadingMore = false
                                    }
                                })
                    }
            )
        }
    }

    private fun logicSelection(addressListModel: AddressListModel, isLoadMore: Boolean = false, isChangeSelection: Boolean = false) {
        launch {
            OccIdlingResource.increment()
            withContext(Dispatchers.Default) {
                val addressList = addressListModel.listAddress
                for (item in addressList) {
                    item.isSelected = item.id == selectedId
                    if (item.id == selectedId) {
                        destinationDistrict = item.destinationDistrictId
                        destinationLatitude = item.latitude
                        destinationLongitude = item.longitude
                        destinationPostalCode = item.postalCode
                    }
                }
                addressListModel.listAddress = if (isLoadMore) {
                    (this@AddressListBottomSheet.addressListModel?.listAddress
                            ?: emptyList()) + addressList
                } else {
                    addressList
                }
                if (!isChangeSelection) {
                    addressListModel.hasNext = addressList.size == 10
                }
            }
            this@AddressListBottomSheet.addressListModel = addressListModel
            onChangeData(if (isLoadMore || isChangeSelection) {
                OccState.Success(addressListModel)
            } else {
                OccState.FirstLoad(addressListModel)
            })
            OccIdlingResource.decrement()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    interface AddressListBottomSheetListener {
        fun onSelect(addressModel: RecipientAddressModel)

        fun onAddAddress(token: Token?)
    }
}