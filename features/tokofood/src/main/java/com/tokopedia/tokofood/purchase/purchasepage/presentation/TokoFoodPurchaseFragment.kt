package com.tokopedia.tokofood.purchase.purchasepage.presentation

import android.content.Context
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.fragment.IBaseMultiFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.EXTRA_SELECTED_ADDRESS_DATA
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.LayoutFragmentPurchaseBinding
import com.tokopedia.tokofood.example.ExampleTokofoodActivity
import com.tokopedia.tokofood.example.HasViewModel
import com.tokopedia.tokofood.example.MultipleFragmentsViewModel
import com.tokopedia.tokofood.purchase.promopage.presentation.TokoFoodPromoFragment
import com.tokopedia.tokofood.purchase.purchasepage.di.DaggerTokoFoodPurchaseComponent
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapter
import com.tokopedia.tokofood.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory
import com.tokopedia.tokofood.purchase.purchasepage.presentation.subview.TokoFoodPurchaseGlobalErrorBottomSheet
import com.tokopedia.tokofood.purchase.purchasepage.presentation.subview.TokoFoodPurchaseNoteBottomSheet
import com.tokopedia.tokofood.purchase.purchasepage.presentation.toolbar.TokoFoodPurchaseToolbar
import com.tokopedia.tokofood.purchase.purchasepage.presentation.toolbar.TokoFoodPurchaseToolbarListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodPurchaseFragment : BaseListFragment<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory>(),
        TokoFoodPurchaseActionListener, TokoFoodPurchaseToolbarListener, IBaseMultiFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var parentActivity: HasViewModel<MultipleFragmentsViewModel>? = null

    private val activityViewModel: MultipleFragmentsViewModel?
        get() = parentActivity?.viewModel()

    private var viewBinding by autoClearedNullable<LayoutFragmentPurchaseBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodPurchaseViewModel::class.java)
    }

    private var toolbar: TokoFoodPurchaseToolbar? = null

    companion object {
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        const val REQUEST_CODE_CHANGE_ADDRESS = 111
        const val REQUEST_CODE_SET_PINPOINT = 112

        fun createInstance(): TokoFoodPurchaseFragment {
            return TokoFoodPurchaseFragment()
        }
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parentActivity = activity as? HasViewModel<MultipleFragmentsViewModel>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = LayoutFragmentPurchaseBinding.inflate(inflater, container, false)
        val view = viewBinding?.root
        (getRecyclerView(view)?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
        initializeToolbar()
        initializeRecyclerViewScrollListener()
        observeList()
        observeFragmentUiModel()
        observeUiEvent()
        loadData()
    }

    override fun getFragmentToolbar(): Toolbar? {
        return null
    }

    override fun getFragmentTitle(): String? {
        return ""
    }

    override fun navigateToNewFragment(fragment: Fragment) {
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // No-op
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view_purchase

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodPurchaseComponent
                    .builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun showLoading() {
        super.showLoading()
        toolbar?.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
        toolbar?.hideLoading()
    }

    override fun loadData(page: Int) {

    }

    private fun loadData() {
        viewBinding?.layoutGlobalErrorPurchase?.gone()
        viewBinding?.recyclerViewPurchase?.show()
        adapter.clearAllElements()
        showLoading()
        viewModel.loadData()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory> {
        return TokoFoodPurchaseAdapter(adapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): TokoFoodPurchaseAdapterTypeFactory {
        return TokoFoodPurchaseAdapterTypeFactory(this)
    }

    override fun onBackPressed() {
        (activity as ExampleTokofoodActivity).onBackPressed()
    }

    private fun initializeToolbar() {
        activity?.let {
            viewBinding?.toolbarPurchase?.removeAllViews()
            val tokoFoodPurchaseToolbar = TokoFoodPurchaseToolbar(it).apply {
                listener = this@TokoFoodPurchaseFragment
            }

            toolbar = tokoFoodPurchaseToolbar

            toolbar?.let {
                viewBinding?.toolbarPurchase?.addView(toolbar)
                it.setContentInsetsAbsolute(0, 0);
                (activity as AppCompatActivity).setSupportActionBar(viewBinding?.toolbarPurchase)
            }

            setToolbarShadowVisibility(false)
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (show) {
            viewBinding?.appBarLayout?.elevation = HAS_ELEVATION.toFloat()
        } else {
            viewBinding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
        }
    }

    private fun setBackground() {
        activity?.let {
            it.window.decorView.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
        }
    }

    private fun initializeRecyclerViewScrollListener() {
        getRecyclerView(view)?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // No-op
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollVertically(-1)) {
                    setToolbarShadowVisibility(true)
                } else {
                    setToolbarShadowVisibility(false)
                }
            }
        })
    }

    private fun observeList() {
        viewModel.visitables.observe(viewLifecycleOwner, {
            (adapter as TokoFoodPurchaseAdapter).updateList(it)
        })
    }

    private fun observeFragmentUiModel() {
        viewModel.fragmentUiModel.observe(viewLifecycleOwner, {
            toolbar?.setToolbarData(it.shopName, it.shopLocation)
        })
    }

    private fun observeUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner, {
            when (it.state) {
                UiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE -> {
                    hideLoading()
                    renderRecyclerView()
                    // Todo : update cart data on shared viewmodel
                }
                UiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE -> {
                    hideLoading()
                    renderGlobalError(it.throwable ?: ResponseErrorException())
                }
                UiEvent.EVENT_REMOVE_ALL_PRODUCT -> navigateToMerchantPage()
                UiEvent.EVENT_SUCCESS_REMOVE_PRODUCT -> onSuccessRemoveProduct(it.data as Int)
                UiEvent.EVENT_SCROLL_TO_UNAVAILABLE_ITEMS -> scrollToIndex(it.data as Int)
                UiEvent.EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG -> showBulkDeleteConfirmationDialog(it.data as Int)
                UiEvent.EVENT_NAVIGATE_TO_SET_PINPOINT -> navigateToSetPinpoint(it.data as LocationPass)
            }
        })
    }

    private fun renderRecyclerView() {
        viewBinding?.let {
            it.layoutGlobalErrorPurchase.gone()
            it.recyclerViewPurchase.show()
        }
    }

    private fun renderGlobalError(throwable: Throwable) {
        viewBinding?.let {
            it.layoutGlobalErrorPurchase.show()
            it.recyclerViewPurchase.gone()
            val errorType = getGlobalErrorType(throwable)
            it.layoutGlobalErrorPurchase.setType(errorType)
            it.layoutGlobalErrorPurchase.setActionClickListener {
                loadInitialData()
            }
        }
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    private fun navigateToSetPinpoint(locationPass: LocationPass) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
        val bundle = Bundle().apply {
            putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
            putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
        }
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_SET_PINPOINT)
    }

    private fun showBulkDeleteConfirmationDialog(productCount: Int) {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle("Hapus $productCount item yang tidak bisa diproses?")
                setDescription("Semua item ini akan dihapus dari keranjangmu.")
                setPrimaryCTAText("Hapus")
                setSecondaryCTAText("Kembali")
                setPrimaryCTAClickListener {
                    viewModel.bulkDeleteUnavailableProducts()
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }.show()
        }
    }

    private fun onSuccessRemoveProduct(productCount: Int) {
        view?.let {
            Toaster.build(
                    view = it,
                    text = "$productCount item berhasil dihapus.",
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_NORMAL,
                    actionText = "Oke"
            ).show()
        }
    }

    private fun navigateToMerchantPage() {
        // Todo : navigate to merchant page
    }

    private fun scrollToIndex(index: Int) {
        activity?.let { activity ->
            getRecyclerView(view)?.layoutManager?.let {
                val linearSmoothScroller = object : LinearSmoothScroller(activity) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
                linearSmoothScroller.targetPosition = index
                it.startSmoothScroll(linearSmoothScroller)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CHANGE_ADDRESS -> onResultFromChangeAddress(resultCode, data)
            REQUEST_CODE_SET_PINPOINT -> onResultFromSetPinpoint(resultCode, data)
        }
    }

    private fun onResultFromSetPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val locationPass = it.getParcelableExtra(LogisticConstant.EXTRA_EXISTING_LOCATION) as? LocationPass
                locationPass?.let {
                    viewModel.updateAddressPinpoint()
                }
            }
        }
    }

    private fun onResultFromChangeAddress(resultCode: Int, data: Intent?) {
        data?.let {
            val chosenAddressModel = it.getParcelableExtra(EXTRA_SELECTED_ADDRESS_DATA) as? ChosenAddressModel
            chosenAddressModel?.let {
                viewModel.updateAddress(it)
            }
        }
    }

    override fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        return viewModel.getNextItems(currentIndex, count)
    }

    override fun onTextChangeShippingAddressClicked() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
        startActivityForResult(intent, REQUEST_CODE_CHANGE_ADDRESS)
    }

    override fun onTextSetPinpointClicked() {
        viewModel.validateSetPinpoint()
    }

    override fun onTextAddItemClicked() {
        // Todo : navigate to merchant page
        view?.let {
            Toaster.build(it, "onTextAddItemClicked", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onTextBulkDeleteUnavailableProductsClicked() {
        viewModel.validateBulkDelete()
    }

    override fun onQuantityChanged() {
        viewModel.calculateTotal()
    }

    override fun onIconDeleteProductClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        viewModel.deleteProduct(element.id)
    }

    override fun onTextChangeNotesClicked(element: TokoFoodPurchaseProductTokoFoodPurchaseUiModel) {
        val addOnBottomSheet = TokoFoodPurchaseNoteBottomSheet(element.notes,
                object : TokoFoodPurchaseNoteBottomSheet.Listener {
                    override fun onSaveNotesClicked(notes: String) {
                        viewModel.updateNotes(element, notes)
                        view?.let {
                            Toaster.build(
                                    view = it,
                                    text = "Sip! Catatan berhasil disimpan.",
                                    duration = Toaster.LENGTH_SHORT,
                                    type = Toaster.TYPE_NORMAL,
                                    actionText = "Oke"
                            ).show()
                        }
                    }
                }
        )
        addOnBottomSheet.show(parentFragmentManager, "")
    }

    override fun onTextChangeNoteAndVariantClicked() {
        // Todo : navigate to edit variant page
        view?.let {
            Toaster.build(it, "onTextChangeNoteAndVariantClicked", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel.toggleUnavailableProductsAccordion()
    }

    override fun onTextShowUnavailableItemClicked() {
        viewModel.scrollToUnavailableItem()
    }

    override fun onPromoWidgetClicked() {
        navigateToNewFragment(TokoFoodPromoFragment.createInstance())
    }

    override fun onButtonCheckoutClicked() {
        // Todo : hit checkout API, validate response, show global error / toaster, reload
        val bottomSheet = TokoFoodPurchaseGlobalErrorBottomSheet(
                outOfService = null,
                listener = object : TokoFoodPurchaseGlobalErrorBottomSheet.Listener {
                    override fun onGoToHome() {

                    }

                    override fun onRetry() {
                        loadInitialData()
                    }

                    override fun onCheckOtherMerchant() {

                    }

                    override fun onStayOnCurrentPage() {

                    }
                }
        )
        bottomSheet.show(parentFragmentManager, "")
    }
}