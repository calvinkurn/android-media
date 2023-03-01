package com.tokopedia.oneclickcheckout.address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.domain.mapper.TargetedTickerMapper.toUiModel
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.param.GetTargetedTickerParam
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.logisticCommon.util.TargetedTickerHelper.renderTargetedTickerView
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.Failure
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.databinding.BottomSheetAddressListBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class AddressListBottomSheet(
    private val useCase: GetAddressCornerUseCase,
    private val getTargetedTicker: GetTargetedTickerUseCase,
    private val listener: AddressListBottomSheetListener
) : CoroutineScope {

    private var bottomSheet: BottomSheetUnify? = null

    private var binding: BottomSheetAddressListBinding? = null

    private var adapter: AddressListItemAdapter? = null
    private var endlessScrollListener: EndlessRecyclerViewScrollListener? = null

    private var fragment: Fragment? = null

    fun show(fragment: OrderSummaryPageFragment, addressId: String, addressState: Int) {
        this.fragment = fragment
        selectedId = addressId
        this.addressState = addressState
        if (!fragment.isAdded) return
        fragment.context?.let { context ->
            fragment.parentFragmentManager.let {
                bottomSheet?.dismiss()
                bottomSheet = BottomSheetUnify().apply {
                    isDragable = true
                    isHideable = true
                    isFullpage = true
                    clearContentPadding = true
                    setTitle(context.getString(R.string.bottom_sheet_title_choose_address))
                    val child = BottomSheetAddressListBinding.inflate(LayoutInflater.from(context))
                    setupChild(context, child)
                    fragment.view?.height?.div(2)?.let { height ->
                        customPeekHeight = height
                    }
                    setChild(child.root)
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
                        binding = null
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

    private fun setupChild(context: Context, binding: BottomSheetAddressListBinding) {
        this.binding = binding

        binding.apply {
            adapter = AddressListItemAdapter(getAddressAdapterListener())
            rvAddressList.adapter = adapter
            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvAddressList.layoutManager = linearLayoutManager
            rvAddressList.clearOnScrollListeners()
            endlessScrollListener =
                object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        loadMore()
                    }
                }
            endlessScrollListener?.let {
                rvAddressList.addOnScrollListener(it)
            }
        }

        initSearchView(context, binding)
        initAddressTicker(context)
    }

    private fun initAddressTicker(context: Context) {
        launch {
            try {
                val param = GetTargetedTickerParam(page = GetTargetedTickerParam.ADDRESS_LIST_OCC, target = listOf())
                val response = getTargetedTicker(param)
                val model = response.getTargetedTickerData.toUiModel()
                binding?.tickerOccAddressList?.renderTargetedTickerView(
                    context,
                    model,
                    onClickApplink = { listener.onClickAddressTickerApplink(it) },
                    onClickUrl = { listener.onClickAddressTickerUrl(it) }
                )
            } catch (e: java.lang.Exception) {
                binding?.tickerOccAddressList?.gone()
            }
        }
    }

    private fun getAddressAdapterListener(): AddressListItemAdapter.OnSelectedListener {
        return object : AddressListItemAdapter.OnSelectedListener {
            override fun onSelect(addressModel: RecipientAddressModel) {
                bottomSheet?.dismiss()
                listener.onSelect(addressModel)
            }
        }
    }

    private fun initSearchView(context: Context, binding: BottomSheetAddressListBinding) {
        binding.apply {
            searchInputView.searchBarTextField.setOnClickListener {
                searchInputView.searchBarTextField.isCursorVisible = true
                openSoftKeyboard()
            }
            searchInputView.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchInputView.clearFocus()
                    searchAddress(searchInputView.searchBarTextField.text?.toString() ?: "")
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            searchInputView.clearListener = {
                searchAddress("")
            }
            searchInputView.searchBarPlaceholder =
                context.getString(com.tokopedia.purchase_platform.common.R.string.label_hint_search_address)
        }

        searchAddress("")
    }

    private fun openSoftKeyboard() {
        binding?.searchInputView?.searchBarTextField?.let {
            (it.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                it,
                InputMethodManager.SHOW_IMPLICIT
            )
        }
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(
                        GlobalError.NO_CONNECTION
                    )
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
        binding?.apply {
            globalError.setType(type)
            globalError.setActionClickListener {
                searchInputView.searchBarTextField.setText("")
                searchAddress("")
            }
            searchInputView.gone()
            textSearchError.gone()
            rvAddressList.gone()
            globalError.visible()
        }
    }

    private fun onChangeData(addressList: OccState<AddressListModel>) {
        when (addressList) {
            is OccState.FirstLoad -> {
                binding?.apply {
                    progressBar.gone()
                    globalError.gone()
                    if (addressList.data.listAddress.isEmpty()) {
                        textSearchError.visible()
                    } else {
                        textSearchError.gone()
                    }
                    searchInputView.visible()
                    rvAddressList.scrollToPosition(0)
                    rvAddressList.visible()
                    adapter?.setData(
                        addressList.data.listAddress,
                        addressList.data.hasNext
                            ?: false
                    )
                    endlessScrollListener?.resetState()
                    endlessScrollListener?.setHasNextPage(addressList.data.hasNext ?: false)
                }
            }

            is OccState.Success -> {
                adapter?.setData(addressList.data.listAddress, addressList.data.hasNext ?: false)
                endlessScrollListener?.updateStateAfterGetData()
                endlessScrollListener?.setHasNextPage(addressList.data.hasNext ?: false)
            }

            is OccState.Failed -> {
                binding?.apply {
                    progressBar.gone()
                    addressList.getFailure()?.let { failure ->
                        handleError(failure.throwable)
                    }
                }
            }

            is OccState.Loading -> {
                binding?.apply {
                    progressBar.visible()
                    rvAddressList.gone()
                    globalError.gone()
                    textSearchError.gone()
                }
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
            useCase.execute(query, addressState, getLocalCacheAddressId().toLongOrZero(), true)
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
        )
    }

    fun loadMore() {
        if (binding?.progressBar?.visibility == View.GONE && !isLoadingMore) {
            isLoadingMore = true
            OccIdlingResource.increment()
            compositeSubscription.add(
                useCase.loadMore(
                    savedQuery,
                    ++this.page,
                    addressState,
                    getLocalCacheAddressId().toLongOrZero(),
                    true
                )
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
            )
        }
    }

    private fun logicSelection(
        addressListModel: AddressListModel,
        isLoadMore: Boolean = false,
        isChangeSelection: Boolean = false
    ) {
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
                    (
                        this@AddressListBottomSheet.addressListModel?.listAddress
                            ?: emptyList()
                        ) + addressList
                } else {
                    addressList
                }
                if (!isChangeSelection) {
                    addressListModel.hasNext = addressList.size == ADDRESS_LIST_PAGE_LIMIT
                }
            }
            this@AddressListBottomSheet.addressListModel = addressListModel
            onChangeData(
                if (isLoadMore || isChangeSelection) {
                    OccState.Success(addressListModel)
                } else {
                    OccState.FirstLoad(addressListModel)
                }
            )
            OccIdlingResource.decrement()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    interface AddressListBottomSheetListener {
        fun onSelect(addressModel: RecipientAddressModel)

        fun onAddAddress(token: Token?)

        fun onClickAddressTickerApplink(applink: String)

        fun onClickAddressTickerUrl(url: String)
    }

    companion object {
        private const val ADDRESS_LIST_PAGE_LIMIT = 10
    }
}
